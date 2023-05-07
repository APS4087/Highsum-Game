import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame implements ActionListener {

    private JLabel titleLabel;
    private JButton adminButton;
    private JButton playerButton;

    MainMenu(){

        // Set up the JFrame
        super("Main menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(400, 200));


        // Create components
        titleLabel = new JLabel("Welcome to Highsum Game!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        adminButton = new JButton("Login as Admin");
        adminButton.setFocusable(false);
        playerButton = new JButton("Login as Player");
        playerButton.setFocusable(false);

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
            System.out.println("Going to admin page");

        }
        if (e.getSource()==playerButton){
            System.out.println("Going to player page");
            dispose();
            PlayerWindow playerWindow = new PlayerWindow();
        }
    }
}
