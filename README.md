# Metrics Dashboard Application

A microservices-based web application for analyzing execution metrics through dashboards and graphical representations.

## Architecture

- **Frontend**: Angular 17+ with PrimeNG components
- **Backend**: Java Spring Boot microservices
- **Database**: H2 in-memory database (for development)

## Features

- Interactive dashboard with data grid, sorting, filtering, pagination
- RAG (Red/Amber/Green) color coding based on configurable thresholds
- Trend visualization with quarterly and daily views
- Configuration management for threshold settings
- Data export functionality
- Cascading delete operations

## Services

1. **Dashboard Service** - Data aggregation and dashboard display
2. **Graph Service** - Trend data generation
3. **Config Service** - Threshold configuration management
4. **Email Service** - Notification system
5. **Export Service** - Data export functionality

## Getting Started

### Backend
```bash
cd backend
./mvnw spring-boot:run
```

### Frontend
```bash
cd frontend
npm install
ng serve
```

## API Endpoints

- `GET /api/dashboard/data` - Dashboard data with filtering
- `GET /api/dashboard/summary` - Aggregated summary
- `GET /api/config/thresholds` - Get threshold configuration
- `PUT /api/config/thresholds` - Update thresholds
- `GET /api/graph/quarterly` - Quarterly trend data
- `GET /api/graph/daily` - Daily trend data
- `GET /api/export/csv` - Export data as CSV
- `DELETE /api/plans/{id}` - Delete plan with cascading

## Development

This application implements the use cases defined in the requirements document:
- UC-01: View Dashboard
- UC-02: Configure Thresholds  
- UC-03: View Graphical Trends
- UC-04: Cascading Delete of Child Records
