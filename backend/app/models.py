from datetime import datetime
import uuid
from sqlalchemy import Column, String, DateTime, Boolean, ForeignKey, Float, Text
from sqlalchemy.orm import relationship
from .database import Base


def generate_uuid() -> str:
    return str(uuid.uuid4())


class User(Base):
    __tablename__ = "users"

    id = Column(String, primary_key=True, default=generate_uuid)
    full_name = Column(String(120), nullable=False)
    national_id = Column(String(50), nullable=False)
    phone_number = Column(String(50), nullable=False)
    email = Column(String(120), unique=True, nullable=False)
    hashed_password = Column(String(255), nullable=False)
    created_at = Column(DateTime, default=datetime.utcnow)

    vehicles = relationship("Vehicle", back_populates="owner", cascade="all, delete-orphan")
    wallet = relationship("Wallet", back_populates="user", uselist=False, cascade="all, delete-orphan")
    transactions = relationship("Transaction", back_populates="user", cascade="all, delete-orphan")
    notifications = relationship("Notification", back_populates="user", cascade="all, delete-orphan")


class Vehicle(Base):
    __tablename__ = "vehicles"

    id = Column(String, primary_key=True, default=generate_uuid)
    plate_number = Column(String(50), nullable=False)
    vehicle_type = Column(String(80), nullable=False)
    manufacturer = Column(String(80), nullable=False)
    model = Column(String(80), nullable=False)
    colour = Column(String(80), nullable=False)
    auto_pay_enabled = Column(Boolean, default=True)
    owner_id = Column(String, ForeignKey("users.id"), nullable=False)

    owner = relationship("User", back_populates="vehicles")
    transactions = relationship("Transaction", back_populates="vehicle", cascade="all, delete-orphan")


class Wallet(Base):
    __tablename__ = "wallets"

    id = Column(String, primary_key=True, default=generate_uuid)
    balance = Column(Float, default=0.0)
    user_id = Column(String, ForeignKey("users.id"), unique=True, nullable=False)

    user = relationship("User", back_populates="wallet")


class Transaction(Base):
    __tablename__ = "transactions"

    id = Column(String, primary_key=True, default=generate_uuid)
    amount = Column(Float, nullable=False)
    tollgate_name = Column(String(120), nullable=False)
    status = Column(String(50), nullable=False)
    timestamp = Column(DateTime, default=datetime.utcnow)
    user_id = Column(String, ForeignKey("users.id"), nullable=False)
    vehicle_id = Column(String, ForeignKey("vehicles.id"), nullable=False)

    user = relationship("User", back_populates="transactions")
    vehicle = relationship("Vehicle", back_populates="transactions")


class Notification(Base):
    __tablename__ = "notifications"

    id = Column(String, primary_key=True, default=generate_uuid)
    title = Column(String(160), nullable=False)
    message = Column(Text, nullable=False)
    notification_type = Column(String(80), nullable=False)
    created_at = Column(DateTime, default=datetime.utcnow)
    user_id = Column(String, ForeignKey("users.id"), nullable=False)

    user = relationship("User", back_populates="notifications")
