package javafinals.finals;

public abstract class Car {
    private String brand;
    private String model;
    private String transmission;
    private int year;
    private double price;
    private int topSpeed;
    private int mileage;
    private int seats;

    public Car(String brand, String model, String transmission, int year, double price, int topSpeed, int mileage, int seats) {
        this.brand = brand;
        this.model = model;
        this.transmission = transmission;
        this.year = year;
        this.price = price;
        this.topSpeed = topSpeed;
        this.mileage = mileage;
        this.seats = seats;
    }

    // Getters
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public String getTransmission() { return transmission; }
    public int getYear() { return year; }
    public double getPrice() { return price; }
    public int getTopSpeed() { return topSpeed; }
    public int getMileage() { return mileage; }
    public int getSeats() { return seats; }

    // Abstract methods
    public abstract String getType();
    public abstract boolean belongsToCategory(String category);

    public String toFileString() {
        return String.format("%s,%s,%s,%s,%d,%.2f,%d,%d,%d,%b",
            getType(),
            brand,
            model,
            transmission,
            year,
            price,
            topSpeed,
            seats,
            mileage,
            false);  // Default hasTruckBed value
    }

    @Override
    public String toString() {
        return String.format("%d %s %s%nTransmission: %s%nPrice: $%.2f%nTop Speed: %d mph%nMileage: %d%nSeats: %d",
            year, brand, model, transmission, price, topSpeed, mileage, seats);
    }
}