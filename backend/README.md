# ZimPass Flow Backend

This is the FastAPI backend for the ZimPass Flow Android application.

## Setup

1. Create a Python virtual environment and install dependencies:

```powershell
cd "c:\Users\DELL\OneDrive\Documents\Zimpass flow\backend"
python -m venv .venv
.\.venv\Scripts\Activate.ps1
pip install -r requirements.txt
```

2. Copy `.env.example` to `.env` and update the PostgreSQL connection string.

3. Run the API:

```powershell
.\.venv\Scripts\uvicorn app.main:app --reload --host 0.0.0.0 --port 8000 --app-dir .
```

## API Endpoints

- `POST /auth/register`
- `POST /auth/login`
- `POST /auth/forgot-password`
- `POST /auth/change-password`
- `GET /user/dashboard`
- `PUT /user/profile`
- `GET /vehicles`
- `POST /vehicles`
- `PUT /vehicles/{id}`
- `DELETE /vehicles/{id}`
- `PUT /vehicles/{id}/toggle-autopay?enabled=true`
- `POST /wallet/topup`
- `GET /transactions`
- `GET /notifications`
- `POST /anpr/scan`

## Notes

- The backend seeds a sample user with the email `test@zimflow.com` and password `Pass1234` on first startup.
- The backend uses PostgreSQL by default. Update `DATABASE_URL` in `.env` if needed.
