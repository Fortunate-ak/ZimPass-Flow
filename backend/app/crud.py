from datetime import datetime
from sqlalchemy import select, or_
from sqlalchemy.orm import Session
from . import models
from .schemas import RegisterRequest, ProfileUpdate, WalletTopUpRequest
from .auth import get_password_hash, verify_password


def get_user_by_email(db: Session, email: str):
    return db.execute(select(models.User).filter(models.User.email == email)).scalar_one_or_none()


def get_user(db: Session, user_id: str):
    return db.execute(select(models.User).filter(models.User.id == user_id)).scalar_one_or_none()


def create_user(db: Session, user_data: RegisterRequest):
    hashed_password = get_password_hash(user_data.password)
    user = models.User(
        full_name=user_data.full_name,
        national_id=user_data.national_id,
        phone_number=user_data.phone_number,
        email=user_data.email,
        hashed_password=hashed_password,
    )
    db.add(user)
    db.commit()
    db.refresh(user)

    wallet = models.Wallet(user_id=user.id, balance=0.0)
    db.add(wallet)
    db.commit()
    db.refresh(wallet)

    return user


def authenticate_user(db: Session, email: str, password: str):
    user = get_user_by_email(db, email)
    if not user:
        return None
    if not verify_password(password, user.hashed_password):
        return None
    return user


def update_user_profile(db: Session, user: models.User, profile_data: ProfileUpdate):
    if profile_data.full_name is not None:
        user.full_name = profile_data.full_name
    if profile_data.national_id is not None:
        user.national_id = profile_data.national_id
    if profile_data.phone_number is not None:
        user.phone_number = profile_data.phone_number
    if profile_data.email is not None:
        user.email = profile_data.email

    db.add(user)
    db.commit()
    db.refresh(user)
    return user


def change_user_password(db: Session, user: models.User, new_password: str):
    user.hashed_password = get_password_hash(new_password)
    db.add(user)
    db.commit()
    db.refresh(user)
    return user


def get_wallet(db: Session, user_id: str):
    return db.execute(select(models.Wallet).filter(models.Wallet.user_id == user_id)).scalar_one_or_none()


def top_up_wallet(db: Session, wallet: models.Wallet, top_up_data: WalletTopUpRequest):
    wallet.balance += top_up_data.amount
    db.add(wallet)
    db.commit()
    db.refresh(wallet)

    notification = models.Notification(
        title="Wallet Top Up",
        message=f"Your wallet was topped up by {top_up_data.amount:.2f} units.",
        notification_type="TOP_UP",
        user_id=wallet.user_id,
    )
    db.add(notification)
    db.commit()
    return wallet


def create_transaction(db: Session, user: models.User, vehicle: models.Vehicle, amount: float, tollgate_name: str, status: str):
    transaction = models.Transaction(
        user_id=user.id,
        vehicle_id=vehicle.id,
        amount=amount,
        tollgate_name=tollgate_name,
        status=status,
        timestamp=datetime.utcnow(),
    )
    db.add(transaction)
    db.commit()
    db.refresh(transaction)
    return transaction


def create_notification(db: Session, user_id: str, title: str, message: str, notification_type: str):
    notify = models.Notification(
        title=title,
        message=message,
        notification_type=notification_type,
        user_id=user_id,
    )
    db.add(notify)
    db.commit()
    db.refresh(notify)
    return notify


def get_vehicles(db: Session, user_id: str):
    return db.execute(select(models.Vehicle).filter(models.Vehicle.owner_id == user_id)).scalars().all()


def get_vehicle(db: Session, user_id: str, vehicle_id: str):
    return db.execute(
        select(models.Vehicle)
        .filter(models.Vehicle.owner_id == user_id)
        .filter(models.Vehicle.id == vehicle_id)
    ).scalar_one_or_none()


def create_vehicle(db: Session, user: models.User, vehicle_data):
    vehicle = models.Vehicle(
        plate_number=vehicle_data.plate_number,
        vehicle_type=vehicle_data.vehicle_type,
        manufacturer=vehicle_data.manufacturer,
        model=vehicle_data.model,
        colour=vehicle_data.colour,
        auto_pay_enabled=True,
        owner_id=user.id,
    )
    db.add(vehicle)
    db.commit()
    db.refresh(vehicle)
    create_notification(
        db,
        user.id,
        "Vehicle Registered",
        f"Vehicle {vehicle.plate_number} was successfully added.",
        "VEHICLE_REGISTERED",
    )
    return vehicle


def update_vehicle(db: Session, vehicle: models.Vehicle, update_data):
    vehicle.plate_number = update_data.plate_number
    vehicle.vehicle_type = update_data.vehicle_type
    vehicle.manufacturer = update_data.manufacturer
    vehicle.model = update_data.model
    vehicle.colour = update_data.colour
    if getattr(update_data, "auto_pay_enabled", None) is not None:
        vehicle.auto_pay_enabled = update_data.auto_pay_enabled
    db.add(vehicle)
    db.commit()
    db.refresh(vehicle)
    return vehicle


def delete_vehicle(db: Session, vehicle: models.Vehicle):
    db.delete(vehicle)
    db.commit()


def toggle_vehicle_autopay(db: Session, vehicle: models.Vehicle, enabled: bool):
    vehicle.auto_pay_enabled = enabled
    db.add(vehicle)
    db.commit()
    db.refresh(vehicle)
    return vehicle


def get_transactions(db: Session, user_id: str, search: str | None = None, status: str | None = None):
    query = select(models.Transaction).filter(models.Transaction.user_id == user_id)
    if search:
        search_term = f"%{search}%"
        query = query.filter(
            or_(
                models.Transaction.tollgate_name.ilike(search_term),
                models.Transaction.status.ilike(search_term),
            )
        )
    if status:
        query = query.filter(models.Transaction.status == status)
    return db.execute(query.order_by(models.Transaction.timestamp.desc())).scalars().all()


def get_notifications(db: Session, user_id: str):
    return db.execute(
        select(models.Notification)
        .filter(models.Notification.user_id == user_id)
        .order_by(models.Notification.created_at.desc())
    ).scalars().all()


def get_dashboard_data(db: Session, user: models.User):
    wallet = get_wallet(db, user.id)
    vehicles = get_vehicles(db, user.id)
    transactions = get_transactions(db, user.id)
    notifications = get_notifications(db, user.id)
    return {
        "user": user,
        "wallet_balance": wallet.balance if wallet else 0.0,
        "vehicles": vehicles,
        "recent_transactions": transactions[:5],
        "notifications": notifications[:5],
        "welcome_message": f"Welcome back, {user.full_name}!",
    }


def recognize_plate(db: Session, plate_number: str):
    return db.execute(select(models.Vehicle).filter(models.Vehicle.plate_number == plate_number)).scalar_one_or_none()
