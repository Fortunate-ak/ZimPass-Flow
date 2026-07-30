from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session
from ..schemas import VehicleCreate, VehicleOut, VehicleUpdate
from ..database import get_db
from ..dependencies import get_current_user
from .. import crud

router = APIRouter()


@router.get("/vehicles", response_model=list[VehicleOut])
def list_vehicles(current_user=Depends(get_current_user), db: Session = Depends(get_db)):
    return crud.get_vehicles(db, current_user.id)


@router.post("/vehicles", response_model=VehicleOut, status_code=status.HTTP_201_CREATED)
def add_vehicle(vehicle_data: VehicleCreate, current_user=Depends(get_current_user), db: Session = Depends(get_db)):
    return crud.create_vehicle(db, current_user, vehicle_data)


@router.put("/vehicles/{vehicle_id}", response_model=VehicleOut)
def edit_vehicle(vehicle_id: str, vehicle_data: VehicleUpdate, current_user=Depends(get_current_user), db: Session = Depends(get_db)):
    vehicle = crud.get_vehicle(db, current_user.id, vehicle_id)
    if not vehicle:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Vehicle not found.")
    return crud.update_vehicle(db, vehicle, vehicle_data)


@router.delete("/vehicles/{vehicle_id}", status_code=status.HTTP_204_NO_CONTENT)
def delete_vehicle(vehicle_id: str, current_user=Depends(get_current_user), db: Session = Depends(get_db)):
    vehicle = crud.get_vehicle(db, current_user.id, vehicle_id)
    if not vehicle:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Vehicle not found.")
    crud.delete_vehicle(db, vehicle)
    return None


@router.put("/vehicles/{vehicle_id}/toggle-autopay", response_model=VehicleOut)
def toggle_autopay(vehicle_id: str, enabled: bool, current_user=Depends(get_current_user), db: Session = Depends(get_db)):
    vehicle = crud.get_vehicle(db, current_user.id, vehicle_id)
    if not vehicle:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Vehicle not found.")
    return crud.toggle_vehicle_autopay(db, vehicle, enabled)
