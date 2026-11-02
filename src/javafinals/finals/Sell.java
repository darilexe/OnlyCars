package javafinals.finals;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;


public class Sell extends JFrame implements ActionListener {
    private static final String FILE_PATH = "Cars.txt";
    private final List<Car> cars;
    private final JTextField brand;
    private final JTextField model;
    private final JTextField transmission;
    private final JTextField year;
    private final JTextField horsepower;
    private final JTextField seats;
    private final JTextField mileage;
    private final JTextField initialPrice;
    private final JTextField buyingPrice;
    private final JComboBox<String> carType;
    private final JButton bargainButton;
    private final JButton sellButton;
    private final JButton denySellButton;

    private static final String[] CAR_TYPES = {"Sedan", "Truck", "SportsCar", "SUV"};

    public Sell() {
        super("Sell Menu");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        cars = new ArrayList<>();
        loadCarsFromFile();

        // Initialize input panel
        JPanel inputPanel = new JPanel(new GridLayout(12, 1, 3, 3));

        // Brand input
        inputPanel.add(new JLabel("Enter Car Brand:"));
        brand = new JTextField();
        inputPanel.add(brand);

        // Model input
        inputPanel.add(new JLabel("Enter Car Model:"));
        model = new JTextField();
        inputPanel.add(model);

        // Transmission input
        inputPanel.add(new JLabel("Enter Transmission:"));
        transmission = new JTextField();
        inputPanel.add(transmission);

        // Year input
        inputPanel.add(new JLabel("Enter Year:"));
        year = new JTextField();
        inputPanel.add(year);

        // Horsepower input
        inputPanel.add(new JLabel("Enter Horsepower:"));
        horsepower = new JTextField();
        inputPanel.add(horsepower);

        // Seats input
        inputPanel.add(new JLabel("Enter Number of Seats:"));
        seats = new JTextField();
        inputPanel.add(seats);

        // Mileage input
        inputPanel.add(new JLabel("Enter Mileage here:"));
        mileage = new JTextField();
        inputPanel.add(mileage);

        // Initial price input
        inputPanel.add(new JLabel("Enter your initial price here:"));
        initialPrice = new JTextField();
        inputPanel.add(initialPrice);

        // Bargain button
        bargainButton = new JButton("Calculate Offer");
        bargainButton.addActionListener(this);
        inputPanel.add(bargainButton);

        // Car type combo box
        carType = new JComboBox<>(CAR_TYPES);
        inputPanel.add(carType);

        // Buying price display
        inputPanel.add(new JLabel("We would like to buy this car for:"));
        buyingPrice = new JTextField();
        buyingPrice.setEditable(false);
        inputPanel.add(buyingPrice);

        // Action buttons
        denySellButton = new JButton("No, Price is unreasonable");
        denySellButton.addActionListener(this);
        inputPanel.add(denySellButton);

        sellButton = new JButton("Yes, I will sell it for that Price");
        sellButton.addActionListener(this);
        inputPanel.add(sellButton);

        add(inputPanel, BorderLayout.NORTH);
        add (new JScrollPane(inputPanel));
        pack();
        setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bargainButton) {
            calculateOffer();
        } else if (e.getSource() == denySellButton || e.getSource() == sellButton) {
            if (e.getSource() == sellButton) {
                saveCar();
            }
            openOptionsAndClose();
        }
    }

    private void calculateOffer() {
        try {
            float mileageValue = Float.parseFloat(mileage.getText());
            float initialPriceValue = Float.parseFloat(initialPrice.getText());
            float discount;

            if (mileageValue < 0) {
                throw new IllegalArgumentException("Mileage cannot be negative");
            }

            if (mileageValue <= 5000) {
                discount = 0.05f;
            } else if (mileageValue <= 9999) {
                discount = 0.10f;
            } else if (mileageValue <= 19999) {
                discount = 0.15f;
            } else if (mileageValue <= 49999) {
                discount = 0.20f;
            } else {
                discount = 0.30f;
            }

            float finalPrice = initialPriceValue * (1 - discount);
            buyingPrice.setText(String.format("%.2f", finalPrice));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Please enter valid numbers for mileage and price.",
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveCar() {
        try {
            Car newCar = createCarFromInput();
            cars.add(newCar);
            saveCarsToFile();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error saving car: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private Car createCarFromInput() {
        try {
            String brandText = brand.getText().trim();
            String modelText = model.getText().trim();
            String transmissionText = transmission.getText().trim();
            int yearValue = Integer.parseInt(year.getText().trim());
            double price = Double.parseDouble(buyingPrice.getText().trim());
            int horsepowerValue = Integer.parseInt(horsepower.getText().trim());
            int seatsValue = Integer.parseInt(seats.getText().trim());
            int mileageValue = Integer.parseInt(mileage.getText().trim());
            String selectedType = (String) carType.getSelectedItem();

            return switch (selectedType) {
                case "SportsCar" -> new SportsCar(brandText, modelText, transmissionText, yearValue, price, horsepowerValue, mileageValue, seatsValue);
                case "Truck" -> new Truck(brandText, modelText, transmissionText, yearValue, price, horsepowerValue, mileageValue, seatsValue);
                case "SUV" -> new SUV(brandText, modelText, transmissionText, yearValue, price, horsepowerValue, mileageValue, seatsValue);
                default -> new Sedan(brandText, modelText, transmissionText, yearValue, price, horsepowerValue, mileageValue, seatsValue);
            };
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Please ensure all numeric fields contain valid numbers");
        }
    }

    private void loadCarsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 10) {
                    continue; // skip blank or malformed lines
                }
                try {
                    cars.add(createCarFromString(parts));
                } catch (IllegalArgumentException e) {
                    System.out.println("Skipping invalid car entry: " + line);
                }
            }
        } catch (IOException e) {
            // If file doesn't exist yet, that's okay - just start with empty list
            System.out.println("No existing cars file found. Starting fresh.");
        }
    }

    private void saveCarsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Car car : cars) {
                writer.println(car.toFileString());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Error saving cars: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openOptionsAndClose() {
        Options opt = new Options();
        opt.setVisible(true);
        opt.setSize(600, 300);
        this.dispose();
    }

    private Car createCarFromString(String[] parts) {
        try {
            String type = parts[0].trim();
            String brand = parts[1].trim();
            String model = parts[2].trim();
            String transmission = parts[3].trim();
            int year = Integer.parseInt(parts[4].trim());
            double price = Double.parseDouble(parts[5].trim());
            int horsepower = Integer.parseInt(parts[6].trim());
            int seats = Integer.parseInt(parts[7].trim());
            int mileage = Integer.parseInt(parts[8].trim());
            boolean hasTruckBed = Boolean.parseBoolean(parts[9].trim());

            return switch (type) {
                case "SPORTS" -> new SportsCar(brand, model, transmission, year, price, horsepower, mileage, seats);
                case "SEDAN" -> new Sedan(brand, model, transmission, year, price, horsepower, mileage, seats);
                case "SUV" -> new SUV(brand, model, transmission, year, price, horsepower, mileage, seats);
                case "TRUCK" -> new Truck(brand, model, transmission, year, price, horsepower, mileage, seats);
                default -> throw new IllegalArgumentException("Unknown vehicle type: " + type);
            };
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing vehicle data: " + e.getMessage());
        }
    }

}