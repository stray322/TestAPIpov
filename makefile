# Makefile for SimbirSoft SDET Project
.PHONY: run stop down test

run:
	@echo "Starting services with Docker Compose..."
	docker-compose up --build -d

stop:
	@echo "Stopping services..."
	docker-compose stop

down:
	@echo "Stopping and removing services..."
	docker-compose down

test:
	@echo "Running tests..."
	mvn clean test

logs:
	@echo "Showing service logs..."
	docker-compose logs -f