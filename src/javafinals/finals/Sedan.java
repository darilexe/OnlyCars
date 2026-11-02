package javafinals.finals;


public class Sedan extends Car {
    public Sedan(String brand, String model, String transmission, int year, double price, int topSpeed, int mileage, int seats) {
        super(brand, model, transmission, year, price, topSpeed, mileage, seats);
    }

    @Override
    public String getType() {
        return "SEDAN";
    }

    @Override
    public boolean belongsToCategory(String category) {
        return category.equals("SEDAN") || category.equals("ALL");
    }
}

