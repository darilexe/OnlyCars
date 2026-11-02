package javafinals.finals;


public class SportsCar extends Car {
    public SportsCar(String brand, String model, String transmission, int year, double price, int topSpeed, int mileage, int seats) {
        super(brand, model, transmission, year, price, topSpeed, mileage, seats);
    }

    @Override
    public String getType() {
        return "SPORTS";
    }

    @Override
    public boolean belongsToCategory(String category) {
        return category.equals("SPORTS") || category.equals("ALL");
    }
}


