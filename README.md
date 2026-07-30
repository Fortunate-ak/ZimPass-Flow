# ZimPass Flow Backend

## Overview

ZimPass Flow is a smart toll payment system designed to reduce congestion at Zimbabwe tollgates by enabling digital payments, vehicle registration, and automatic toll verification.

This repository contains the backend API built with Python and FastAPI. It provides secure authentication, vehicle management, wallet management, toll transaction processing, and communication with the mobile application.

---

## Objectives

- Reduce congestion at tollgates
- Eliminate slow manual payment processes
- Enable secure digital toll payments
- Support automatic toll verification
- Provide a scalable backend for future AI-powered toll systems

---

## Features

- User Registration
- User Authentication (JWT)
- Vehicle Registration
- Multiple Vehicles per User
- Shared Digital Wallet
- Wallet Top-Up
- Toll Transaction History
- Notifications
- REST API
- PostgreSQL Database
- Secure Password Hashing

---

## Technology Stack

- Python
- FastAPI
- PostgreSQL
- SQLAlchemy
- Alembic
- Pydantic
- JWT Authentication
- Uvicorn

---

## Project Structure

```
app/
│
├── api/
├── models/
├── schemas/
├── services/
├── repositories/
├── database/
├── security/
├── middleware/
├── utils/
└── main.py
```

---

## Installation

### Clone the repository

```bash
git clone https://github.com/yourusername/ZimPassFlow-Backend.git
```

### Navigate into the project

```bash
cd ZimPassFlow-Backend
```

### Create a virtual environment

```bash
python -m venv venv
```

### Activate the virtual environment

Windows

```bash
venv\Scripts\activate
```

Linux/macOS

```bash
source venv/bin/activate
```

### Install dependencies

```bash
pip install -r requirements.txt
```

---

## Environment Variables

Create a `.env` file and configure:

```
DATABASE_URL=
SECRET_KEY=
ALGORITHM=
ACCESS_TOKEN_EXPIRE_MINUTES=
```

---

## Running the Application

```bash
uvicorn app.main:app --reload
```

Server:

```
http://127.0.0.1:8000
```

Swagger API Documentation:

```
http://127.0.0.1:8000/docs
```

ReDoc Documentation:

```
http://127.0.0.1:8000/redoc
```

---

## Database

Database: PostgreSQL

Main Tables:

- Users
- Vehicles
- Wallets
- Wallet Transactions
- Toll Transactions
- Notifications

---

## API Modules

- Authentication
- Users
- Vehicles
- Wallet
- Transactions
- Notifications

---

## Security

- JWT Authentication
- Password Hashing
- Protected Endpoints
- Input Validation

---

## Future Enhancements

The backend is designed to support future integration with:

- Automatic Number Plate Recognition (ANPR)
- AI Fraud Detection
- Smart Tollgate Controllers
- Toll Operator Dashboard
- ZINARA Central Management System
- Real-Time Toll Verification

---

## Contributors

Developer:

Fortunate T. Misihairahwi

Software Engineering Student

Harare Institute of Technology

---

## License

This project is licensed under the MIT License.
