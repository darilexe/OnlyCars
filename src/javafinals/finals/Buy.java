package javafinals.finals;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JOptionPane;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;



import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.List;

public class Buy extends JFrame {
    private static final String FILE_PATH = "Cars.txt";
    private JPanel leftPanel; // For categories
    private JPanel rightPanel; // For vehicle display
    private List<Car> Cars;
    private String currentCategory = "ALL";


    public Buy() {
        Cars = new ArrayList<>();
        checkAndCreateDefaultFile();
        initializeUI();
        loadCarsFromFile();
        displayCars();
    }

    private void checkAndCreateDefaultFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
                writer.println("SPORTS,Ferrari,F8,Automatic,2023,300000.00,710,2,10000,false");
                writer.println("SPORTS,Porsche,911,Manual,2023,120000.00,450,2,5000,false");
                writer.println("SEDAN,Toyota,Camry,Automatic,2023,25000.00,200,5,15000,false");
                writer.println("SEDAN,Honda,Accord,CVT,2023,27000.00,192,5,12000,false");
                writer.println("SUV,Ford,Explorer,Automatic,2023,35000.00,300,7,8000,false");
                writer.println("SUV,Toyota,RAV4,Automatic,2023,28000.00,203,5,20000,false");
                writer.println("TRUCK,Ford,F-150,Automatic,2023,45000.00,400,5,5000,true");
                writer.println("TRUCK,Chevrolet,Silverado,Automatic,2023,43000.00,355,5,7000,true");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                    "Error creating default file: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void initializeUI() {
        setTitle("Vehicle Sales System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(1, 2, 10, 0));

        // Left panel for categories
        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Categories"));

        // Create category buttons with corresponding type values
        addCategoryButton("All Cars", "ALL");
        addCategoryButton("Sports Cars", "SPORTS");
        addCategoryButton("Sedans", "SEDAN");
        addCategoryButton("SUVs", "SUV");
        addCategoryButton("Trucks", "TRUCK");



        // Right panel for vehicle display
        rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(0, 2, 10, 10));
        rightPanel.setBorder(BorderFactory.createTitledBorder("Available Cars"));

        add(new JScrollPane(leftPanel));
        add(new JScrollPane(rightPanel));
    }

    private void addCategoryButton(String label, String categoryType) {
        JButton categoryBtn = new JButton(label);
        categoryBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        categoryBtn.addActionListener(e -> {
            currentCategory = categoryType;
            displayCars();
        });
        leftPanel.add(categoryBtn);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private void loadCarsFromFile() {
        Cars.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 10) {
                    Cars.add(createCarFromString(parts));
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Error loading Cars: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private Car createCarFromString(String[] parts) {
        try {
            String type = parts[0].trim();
            String brand = parts[1].trim();
            String model = parts[2].trim();
            String transmission = parts[3].trim();
            int year = Integer.parseInt(parts[4].trim());
            double price = Double.parseDouble(parts[5].trim());
            int topSpeed = Integer.parseInt(parts[6].trim());
            int seats = Integer.parseInt(parts[7].trim());
            int mileage = Integer.parseInt(parts[8].trim());
            boolean hasTruckBed = Boolean.parseBoolean(parts[9].trim());

            return switch (type) {
                case "SPORTS" -> new SportsCar(brand, model, transmission, year, price, topSpeed, mileage, seats);
                case "SEDAN" -> new Sedan(brand, model, transmission, year, price, topSpeed, mileage, seats);
                case "SUV" -> new SUV(brand, model, transmission, year, price, topSpeed, mileage, seats);
                case "TRUCK" -> new Truck(brand, model, transmission, year, price, topSpeed, mileage, seats);
                default -> throw new IllegalArgumentException("Unknown vehicle type: " + type);
            };
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing vehicle data: " + e.getMessage());
        }
    }

    private void saveCarsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Car car : Cars) {
                writer.println(car.toFileString());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Error saving Cars: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayCars() {
        rightPanel.removeAll();
        for (Car car : Cars) {
            if (car.belongsToCategory(currentCategory)) {
                JButton carButton = new JButton("<html>" + car.toString().replace("\n", "<br/>") + "</html>");
                carButton.setPreferredSize(new Dimension(250, 120));
                carButton.addActionListener(e -> showTransactionBuy(car));
                rightPanel.add(carButton);
            }
        }
        rightPanel.revalidate();
        rightPanel.repaint();
    }

private void showTransactionBuy(Car car) {
    TransactionBuyGUI transactionGUI = new TransactionBuyGUI((float) car.getPrice(), () -> {
        displayCars();
        showPersonalInfoPrompt(car);
        Cars.remove(car);
        saveCarsToFile();
    });
    transactionGUI.setVisible(true);
}

private void showPersonalInfoPrompt(Car car) {
    // Prompt for user details
    JTextField nameField = new JTextField();
    JTextField addressField = new JTextField();
    JTextField contactField = new JTextField();

    Object[] message = {
        "Name:", nameField,
        "Address:", addressField,
        "Contact Number:", contactField
    };

    int option = JOptionPane.showConfirmDialog(
        this,
        message,
        "Enter Personal Information",
        JOptionPane.OK_CANCEL_OPTION
    );

    if (option == JOptionPane.OK_OPTION) {
        String name = nameField.getText().trim();
        String address = addressField.getText().trim();
        String contact = contactField.getText().trim();

        if (name.isEmpty() || address.isEmpty() || contact.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "All fields are required. Please try again.",
                "Input Error",
                JOptionPane.ERROR_MESSAGE
            );
            showPersonalInfoPrompt(car); // Retry if inputs are invalid
        } else {
            showReceipt(name, address, contact, car);
        }
    }
}

private void showReceipt(String name, String address, String contact, Car car) {
    // Generate receipt details
    String receipt = String.format(
        """
        RECEIPT
        ------------------------------
        Buyer Information:
        Name: %s
        Address: %s
        Contact: %s

        Vehicle Details:
        Car Name: %s %s
        Price: %.2f
        Monthly Payment: %.2f
        ------------------------------
        Thank you for your purchase!
        """,
        name,
        address,
        contact,
        car.getBrand(),
        car.getModel(),
        car.getPrice(),
        calculateMonthlyPayment(car.getPrice())
    );

    // Show the receipt
    JTextArea receiptArea = new JTextArea(receipt);
    receiptArea.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(receiptArea);
    JOptionPane.showMessageDialog(
        this,
        scrollPane,
        "Purchase Receipt",
        JOptionPane.INFORMATION_MESSAGE
    );
}

private float calculateMonthlyPayment(double vehicleCost) {//nn
    // Dummy calculation: assumes 12-month plan with a 5% interest rate
    float downPayment = (float) vehicleCost * 0.20f;
    float loanAmount = (float) vehicleCost - downPayment;
    float interestRate = 0.05f / 12; // Monthly interest for 12 months
    int months = 12; // Fixed to 12 months
    return (float) ((loanAmount * interestRate) / (1 - Math.pow(1 + interestRate, -months)));
}



    private static class TransactionBuyGUI extends JFrame {

        public TransactionBuyGUI(float vehicleCost, Runnable onPurchase) {

            setTitle("Buy at OnlyCars");
            setSize(400, 400);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            setLayout(new BorderLayout());

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(6, 2, 10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Vehicle Cost
            JLabel costLabel = new JLabel("Vehicle Cost:");
            JTextField costField = new JTextField(String.format("$%.2f%n", vehicleCost));
            costField.setEditable(false);

            // Down Payment
            JLabel downPaymentLabel = new JLabel("Down Payment (20%):");
            JTextField downPaymentField = new JTextField(String.format("$%.2f%n", vehicleCost * 0.20));
            downPaymentField.setEditable(false);

            // Loan Amount
            JLabel loanAmountLabel = new JLabel("Auto Loan:");
            JTextField loanAmountField = new JTextField();
            loanAmountField.setEditable(false);

            // Payment Duration
            JLabel durationLabel = new JLabel("Payment Duration:");
            JComboBox<String> durationBox = new JComboBox<>(new String[]{"12 Months (5%)", "24 Months (7%)", "48 Months (15%)"});

            // Monthly Payment
            JLabel paymentLabel = new JLabel("Monthly Payment:");
            JTextField paymentField = new JTextField();
            paymentField.setEditable(false);

            // Calculate Button
            JButton calculateButton = new JButton("Calculate");
            calculateButton.addActionListener(e -> {
                try {
                    float downPayment = vehicleCost * 0.20f; // 20% Down Payment
                    float loanAmount = vehicleCost - downPayment;
                    loanAmountField.setText(String.format("$%.2f%n", loanAmount));

                    String selected = (String) durationBox.getSelectedItem();
                    int months = switch (selected) {
                        case "12 Months (5%)" -> 12;
                        case "24 Months (7%)" -> 24;
                        case "48 Months (15%)" -> 48;
                        default -> 0;
                    };

                    float interestRate = switch (months) {
                        case 12 -> 0.05f / 12; // Monthly interest rate for 12 months
                        case 24 -> 0.07f / 24; // Monthly interest rate for 24 months
                        case 48 -> 0.15f / 48; // Monthly interest rate for 48 months
                        default -> 0;
                    };

                    float monthlyPayment = (float) ((loanAmount * interestRate) / (1 - Math.pow(1 + interestRate, -months)));
                    paymentField.setText(String.format("$%.2f%n", monthlyPayment));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error during calculation: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            // Confirm Purchase Button
            JButton confirmButton = new JButton("Confirm Purchase");
            confirmButton.addActionListener(e -> {
                if (loanAmountField.getText().isEmpty() || paymentField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "Please calculate the loan and monthly payment first.",
                        "Incomplete Calculation",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                onPurchase.run();
                dispose();
            });

            // Adding components to the panel
            panel.add(costLabel);
            panel.add(costField);
            panel.add(downPaymentLabel);
            panel.add(downPaymentField);
            panel.add(durationLabel);
            panel.add(durationBox);
            panel.add(loanAmountLabel);
            panel.add(loanAmountField);
            panel.add(paymentLabel);
            panel.add(paymentField);
            panel.add(calculateButton);
            panel.add(confirmButton);

            add(panel, BorderLayout.CENTER);
        }
    }
}


