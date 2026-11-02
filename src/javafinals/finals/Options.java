package javafinals.finals;


import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;



public class Options extends JFrame implements ActionListener {
    private static final String FILE_PATH = "Cars.txt";
    JLabel onlyC;
    JButton buyC;
    JButton sellC;
    JButton exit;
    JPanel right, left;
    JLabel pictureLabel;

    public Options() {
        super("Only Cars");
        setLayout(new BorderLayout());
        checkAndCreateDefaultFile();

        left = new JPanel();
        left.setLayout(new BorderLayout());

        ImageIcon imageIcon = new ImageIcon("C:\\Users\\nheno\\Documents\\NetBeansProjects\\TN26\\src\\dealership\\OnlyC.jpg");
        pictureLabel = new JLabel(imageIcon);
        Image scaledImage = imageIcon.getImage().getScaledInstance(420, 300, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        pictureLabel = new JLabel(scaledIcon);
        left.add(pictureLabel, BorderLayout.CENTER);

        right = new JPanel();
        right.setLayout(new GridLayout(5, 1, 10, 10));
        right.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        onlyC = new JLabel("Welcome to OnlyCars", JLabel.CENTER);
        right.add(onlyC);

        buyC = new JButton("Buy");
        right.add(buyC);

        sellC = new JButton("Sell");
        right.add(sellC);

        exit = new JButton("Exit");
        right.add(exit);

        buyC.addActionListener(this);
        sellC.addActionListener(this);
        exit.addActionListener(this);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        splitPane.setDividerLocation(650);
        splitPane.setResizeWeight(0.7);

        add(splitPane, BorderLayout.CENTER);
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buyC) {
            Buy buyCar = new Buy();
            buyCar.setVisible(true);
            buyCar.setSize(600, 300);
            this.dispose();
        } else if (e.getSource() == sellC) {
            Sell sellCar = new Sell();
            sellCar.setVisible(true);
            sellCar.setSize(600, 300);
            this.dispose();
        } else if (e.getSource() == exit) {
            System.exit(0);
        }
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

}
