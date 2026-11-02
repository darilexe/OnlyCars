package javafinals.finals;


public class SUV extends Car {
    public SUV(String brand, String model, String transmission, int year, double price, int topSpeed, int mileage, int seats) {
        super(brand, model, transmission, year, price, topSpeed, mileage, seats);
    }

    @Override
    public String getType() {
        return "SUV";
    }

    @Override
    public boolean belongsToCategory(String category) {
        return category.equals("SUV") || category.equals("ALL");
    }
}

