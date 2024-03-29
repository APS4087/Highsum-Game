import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Scanner;

// ** MAIN MENU TO RUN **
public class MainMenu extends JFrame implements ActionListener {

    private JButton adminButton;
    private JButton playerButton;

    MainMenu(){

        // Set up the JFrame
        super("Main menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(400, 250));



        // Create components
        JLabel titleLabel = new JLabel("Welcome to Highsum Game!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        adminButton = new JButton("Login as Admin");
        adminButton.setFocusable(false);
        playerButton = new JButton("Login as Player");
        playerButton.setFocusable(false);
        JButton exitButton = new JButton("Exit");
        exitButton.setFocusable(false);
        // using lambda expression
        exitButton.addActionListener(e -> System.exit(0));


        // Set up layout manager
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(titleLabel, gbc);
        gbc.gridy = 1;
        panel.add(adminButton, gbc);
        gbc.gridy = 2;
        panel.add(playerButton, gbc);
        gbc.gridy = 3;
        panel.add(exitButton, gbc);

        panel.setBackground(Color.lightGray);
        // Add panel to frame
        add(panel);

        // Add action listeners to buttons
        adminButton.addActionListener(this);
        playerButton.addActionListener(this);

        // Pack and display the frame
        pack();
        setLocationRelativeTo(null);

        setVisible(true);
    }

    public static void main(String[] args) {
        new MainMenu();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==adminButton){
            // Currently admin function be accessed by console only
            Scanner scan = new Scanner(System.in);
            Admin ad = new Admin();
            dispose();
            JOptionPane.showMessageDialog(null,"Please proceed to console to access admin functions.");
            System.out.println("--------Welcome to admin portal---------");


            System.out.print("Enter password: ");
            String password = scan.next();
            scan.nextLine();

            if (ad.checkAdminLogin(password)) {
                System.out.println("Login approved.....");

                ad.adminMenu();
            } else {
                System.out.println("Incorrect admin password");
                System.out.println();
                new MainMenu();
            }





        }
        if (e.getSource()==playerButton){
            System.out.println("Going to player page");
            dispose();
            new PlayerWindow();
        }

    }
}
