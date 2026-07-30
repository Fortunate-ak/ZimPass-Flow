from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from ..database import get_db
from ..dependencies import get_current_user
from .. import crud
from ..schemas import TransactionOut

router = APIRouter()


@router.get("/transactions", response_model=list[TransactionOut])
def list_transactions(search: str | None = None, status: str | None = None, current_user=Depends(get_current_user), db: Session = Depends(get_db)):
    transactions = crud.get_transactions(db, current_user.id, search=search, status=status)
    return [
        TransactionOut(
            id=tx.id,
            amount=tx.amount,
            tollgate_name=tx.tollgate_name,
            status=tx.status,
            timestamp=tx.timestamp,
            vehicle_plate=tx.vehicle.plate_number if tx.vehicle else "",
        )
        for tx in transactions
    ]
