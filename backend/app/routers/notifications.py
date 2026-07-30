from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from ..database import get_db
from ..dependencies import get_current_user
from .. import crud
from ..schemas import NotificationOut

router = APIRouter()


@router.get("/notifications", response_model=list[NotificationOut])
def list_notifications(current_user=Depends(get_current_user), db: Session = Depends(get_db)):
    notifications = crud.get_notifications(db, current_user.id)
    return notifications
