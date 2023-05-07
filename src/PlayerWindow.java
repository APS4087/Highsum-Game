import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

public class PlayerWindow extends JFrame implements ActionListener {

    private JLabel titleLabel;
    private JLabel usernameLabel;
    private JTextField usernameField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton resetButton;
    ArrayList<Player> players = (ArrayList<Player>) Player.getAllPlayers();
    Admin ad = new Admin();
    PlayerWindow(){
        super("Player window");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(500, 400));

        // Create components
        titleLabel = new JLabel("Player Login");
        titleLabel.setFont(new Font("Arial",Font.BOLD,24));
        usernameLabel = new JLabel("Username:");
        usernameField  = new JTextField(20);
        passwordLabel = new JLabel("Password:");
        passwordField= new JPasswordField(20);
        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        resetButton = new JButton("Reset");
        resetButton.addActionListener(this);

        // setting up layout
        // using flexible gridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(titleLabel,gbc);
        gbc.gridy = 1;
        panel.add(usernameLabel, gbc);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(loginButton,gbc);
        gbc.gridx = 1;
        panel.add(resetButton,gbc);

        // Add panel to frame
        add(panel);

        // Pack and display the frame
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new PlayerWindow();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==loginButton){
            String pwd = new String(passwordField.getPassword());  // getting text from password field
            Iterator<Player> iter = players.iterator();
            boolean playerFound = false;
            while (iter.hasNext()){
                Player player = iter.next();
                String loginName = player.getLoginName();
                String hashedPassword = player.getHashedPassword();

                if(usernameField.getText().equals(loginName) && ad.passwordHash(pwd).equals(hashedPassword)){
                    System.out.println("Login successful");
                    playerFound = true;
                    break;
                }
            }
            if(!playerFound){
                JOptionPane.showMessageDialog(null,"Invalid username or password!","Invalid Login",JOptionPane.ERROR_MESSAGE);
            }
            if(playerFound){
                dispose();
            }
        }
        if (e.getSource()==resetButton){
            // reseting the fields to default
            usernameField.setText("");
            passwordField.setText("");
        }
    }
}
