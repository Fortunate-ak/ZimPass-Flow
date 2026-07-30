from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from ..schemas import DashboardData, ProfileUpdate
from ..database import get_db
from ..dependencies import get_current_user
from .. import crud

router = APIRouter()


@router.get("/dashboard", response_model=DashboardData)
def dashboard(current_user=Depends(get_current_user), db: Session = Depends(get_db)):
    data = crud.get_dashboard_data(db, current_user)
    return data


@router.put("/profile", status_code=204)
def update_profile(profile_data: ProfileUpdate, current_user=Depends(get_current_user), db: Session = Depends(get_db)):
    crud.update_user_profile(db, current_user, profile_data)
    return None
