from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from ..schemas import WalletTopUpRequest
from ..database import get_db
from ..dependencies import get_current_user
from .. import crud

router = APIRouter()


@router.post("/topup", status_code=204)
def top_up_wallet(request: WalletTopUpRequest, current_user=Depends(get_current_user), db: Session = Depends(get_db)):
    wallet = crud.get_wallet(db, current_user.id)
    if not wallet:
        raise HTTPException(status_code=404, detail="Wallet not found.")
    crud.top_up_wallet(db, wallet, request)
    return None
