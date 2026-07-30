from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from sqlalchemy.orm import Session
from .routers import auth, users, vehicles, wallet, transactions, notifications, anpr, health
from .database import engine, SessionLocal
from .models import Base
from .crud import get_user_by_email, create_user, create_vehicle, top_up_wallet, create_transaction
from .schemas import RegisterRequest, VehicleCreate, WalletTopUpRequest

app = FastAPI(
    title="ZimPass Flow Backend",
    description="FastAPI backend for ZimPass Flow mobile application.",
    version="1.0.0",
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(auth.router, prefix="/auth", tags=["Authentication"])
app.include_router(users.router, prefix="/user", tags=["User"])
app.include_router(vehicles.router, tags=["Vehicles"])
app.include_router(wallet.router, prefix="/wallet", tags=["Wallet"])
app.include_router(transactions.router, tags=["Transactions"])
app.include_router(notifications.router, tags=["Notifications"])
app.include_router(anpr.router, prefix="/anpr", tags=["ANPR"])
app.include_router(health.router, tags=["Health"])


def seed_initial_data(db: Session) -> None:
    existing = get_user_by_email(db, "test@zimflow.com")
    if existing:
        return

    default_user = create_user(
        db,
        RegisterRequest(
            full_name="Test Driver",
            national_id="990123456V",
            phone_number="+263772123456",
            email="test@zimflow.com",
            password="Pass1234",
        ),
    )

    sample_vehicle = create_vehicle(
        db,
        default_user,
        VehicleCreate(
            plate_number="ABC1234",
            vehicle_type="Sedan",
            manufacturer="Toyota",
            model="Corolla",
            colour="White",
        ),
    )

    top_up_wallet(db, default_user.wallet, WalletTopUpRequest(amount=200.0))
    create_transaction(db, default_user, sample_vehicle, 12.50, "Harare Tollgate", "PAID")
    create_transaction(db, default_user, sample_vehicle, 8.20, "Mutare Gate", "PAID")


@app.on_event("startup")
def on_startup() -> None:
    Base.metadata.create_all(bind=engine)
    db = SessionLocal()
    try:
        seed_initial_data(db)
    finally:
        db.close()


@app.get("/", summary="API Root")
def root() -> dict:
    return {"message": "ZimPass Flow Backend is running."}
