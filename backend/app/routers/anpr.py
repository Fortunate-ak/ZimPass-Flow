from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from ..schemas import ANPRScanRequest, ANPRScanResponse
from ..database import get_db
from ..dependencies import get_current_user
from .. import crud

router = APIRouter()


@router.post("/scan", response_model=ANPRScanResponse)
def scan_plate(request: ANPRScanRequest, current_user=Depends(get_current_user), db: Session = Depends(get_db)):
    vehicle = crud.recognize_plate(db, request.plate_number)
    if not vehicle:
        return ANPRScanResponse(
            plate_number=request.plate_number,
            recognized=False,
            vehicle_id=None,
            owner_id=None,
            auto_pay_enabled=None,
            message="Plate not recognized.",
        )

    if vehicle.owner_id != current_user.id:
        raise HTTPException(status_code=403, detail="Plate belongs to another account.")

    return ANPRScanResponse(
        plate_number=vehicle.plate_number,
        recognized=True,
        vehicle_id=vehicle.id,
        owner_id=vehicle.owner_id,
        auto_pay_enabled=vehicle.auto_pay_enabled,
        message="Vehicle recognized and ready for toll processing.",
    )
