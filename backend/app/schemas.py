from datetime import datetime
from typing import List, Optional
from pydantic import BaseModel, EmailStr, Field


class TokenData(BaseModel):
    user_id: Optional[str] = None


class UserOut(BaseModel):
    id: str
    full_name: str
    national_id: str
    phone_number: str
    email: EmailStr

    class Config:
        orm_mode = True


class AuthResponse(BaseModel):
    access_token: str
    token_type: str = "bearer"
    user: UserOut


class LoginRequest(BaseModel):
    email: EmailStr
    password: str


class RegisterRequest(BaseModel):
    full_name: str = Field(..., min_length=2)
    national_id: str = Field(..., min_length=5)
    phone_number: str = Field(..., min_length=5)
    email: EmailStr
    password: str = Field(..., min_length=6)


class PasswordChangeRequest(BaseModel):
    new_password: str = Field(..., min_length=6)


class ForgotPasswordRequest(BaseModel):
    email: EmailStr


class ProfileUpdate(BaseModel):
    full_name: Optional[str]
    national_id: Optional[str]
    phone_number: Optional[str]
    email: Optional[EmailStr]


class VehicleBase(BaseModel):
    plate_number: str
    vehicle_type: str
    manufacturer: str
    model: str
    colour: str


class VehicleCreate(VehicleBase):
    pass


class VehicleUpdate(VehicleBase):
    auto_pay_enabled: Optional[bool] = None


class VehicleOut(VehicleBase):
    id: str
    auto_pay_enabled: bool

    class Config:
        orm_mode = True


class WalletTopUpRequest(BaseModel):
    amount: float = Field(..., gt=0)


class TransactionOut(BaseModel):
    id: str
    amount: float
    tollgate_name: str
    status: str
    timestamp: datetime
    vehicle_plate: str

    class Config:
        orm_mode = True


class NotificationOut(BaseModel):
    id: str
    title: str
    message: str
    notification_type: str
    created_at: datetime

    class Config:
        orm_mode = True


class DashboardData(BaseModel):
    user: UserOut
    wallet_balance: float
    vehicles: List[VehicleOut]
    recent_transactions: List[TransactionOut]
    notifications: List[NotificationOut]
    welcome_message: str


class ANPRScanRequest(BaseModel):
    plate_number: str


class ANPRScanResponse(BaseModel):
    plate_number: str
    recognized: bool
    vehicle_id: Optional[str]
    owner_id: Optional[str]
    auto_pay_enabled: Optional[bool]
    message: str
