package javafinals.finals;


public class Truck extends Car {
    private boolean hasTruckBed = true;

    public Truck(String brand, String model, String transmission, int year, double price, int topSpeed, int mileage, int seats) {
        super(brand, model, transmission, year, price, topSpeed, mileage, seats);
    }

    @Override
    public String getType() {
        return "TRUCK";
    }

    @Override
    public boolean belongsToCategory(String category) {
        return category.equals("TRUCK") || category.equals("ALL");
    }

    @Override
    public String toFileString() {
        return String.format("%s,%s,%s,%s,%d,%.2f,%d,%d,%d,%b",
            getType(),
            getBrand(),
            getModel(),
            getTransmission(),
            getYear(),
            getPrice(),
            getTopSpeed(),
            getSeats(),
            getMileage(),
            hasTruckBed);
    }
}