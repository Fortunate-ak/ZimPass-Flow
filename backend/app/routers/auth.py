from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session
from ..schemas import AuthResponse, LoginRequest, RegisterRequest
from ..database import get_db
from ..dependencies import get_current_user
from .. import crud
from ..auth import create_access_token

router = APIRouter()


@router.post("/register", response_model=AuthResponse)
def register(user_data: RegisterRequest, db: Session = Depends(get_db)):
    existing_user = crud.get_user_by_email(db, user_data.email)
    if existing_user:
        raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail="Email already registered.")
    user = crud.create_user(db, user_data)
    access_token = create_access_token(data={"user_id": user.id})
    return AuthResponse(access_token=access_token, user=user)


@router.post("/login", response_model=AuthResponse)
def login(credentials: LoginRequest, db: Session = Depends(get_db)):
    user = crud.authenticate_user(db, credentials.email, credentials.password)
    if not user:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="Invalid email or password.")
    access_token = create_access_token(data={"user_id": user.id})
    return AuthResponse(access_token=access_token, user=user)


@router.post("/forgot-password", status_code=status.HTTP_204_NO_CONTENT)
def forgot_password(email: str, db: Session = Depends(get_db)):
    user = crud.get_user_by_email(db, email)
    if not user:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="User not found.")
    # In a production environment, send an email or SMS.
    return None


@router.post("/change-password", status_code=status.HTTP_204_NO_CONTENT)
def change_password(new_password: str, current_user=Depends(get_current_user), db: Session = Depends(get_db)):
    crud.change_user_password(db, current_user, new_password)
    return None
