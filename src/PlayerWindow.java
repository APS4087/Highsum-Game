import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

// GUI player login window
public class PlayerWindow extends JFrame implements ActionListener {

    private JLabel titleLabel;
    private JLabel usernameLabel;
    private JTextField usernameField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton resetButton;
    private Dealer dealer = new Dealer();
    ArrayList<Player> players = (ArrayList<Player>) Player.getAllPlayers();
    private JList<String> playersJList;
    private DefaultListModel<String> playersListModel;

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

        // Setting up the Available players
        DefaultListModel<String> playersListModel = new DefaultListModel<>();
        for (Player player: players) {
            playersListModel.addElement(player.getLoginName());
        }
        playersJList = new JList<>(playersListModel);
        playersJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane playersListScrollPane = new JScrollPane(playersJList);
        playersListScrollPane.setPreferredSize(new Dimension(150,50));
        System.out.println(playersListScrollPane.getPreferredSize().height+","+playersListScrollPane.getPreferredSize().width);
        // Getting the selected player
        String selectedPlayer = playersJList.getSelectedValue();

        // Autofill to usernameField
        playersJList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedPlayer = playersJList.getSelectedValue();
                    if (selectedPlayer != null) {
                        usernameField.setText(selectedPlayer);
                    }
                }
            }
        });
        // Label
        JLabel playerListLabel = new JLabel("Available Players:");

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
        panel.add(playerListLabel, gbc);
        gbc.gridx = 1;
        panel.add(playersListScrollPane, gbc);
        gbc.gridy = 2;
        gbc.gridx = 0;

        panel.add(usernameLabel, gbc);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
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
            boolean hasEnoughChips = false;
            while (iter.hasNext()){
                Player player = iter.next();
                String loginName = player.getLoginName();
                String hashedPassword = player.getHashedPassword();

                if(usernameField.getText().equals(loginName) && ad.passwordHash(pwd).equals(hashedPassword)){
                    if(player.getChips()>=50) {
                        System.out.println("Login successful");
                        dispose();
                        dealer.shuffle();
                        MainGameFrame game = new MainGameFrame(player, dealer);
                        System.out.println(player.getLoginName());
                        playerFound = true;
                        hasEnoughChips = true;
                        game.run();
                        break;
                    }
                    else {
                        JOptionPane.showMessageDialog(null,"Player need at least 50 chips to play","Invalid Login",JOptionPane.ERROR_MESSAGE);
                        playerFound = true;

                    }
                }
            }
            if(!playerFound){
                JOptionPane.showMessageDialog(null,"Invalid username or password!","Invalid Login",JOptionPane.ERROR_MESSAGE);
            }
            if(playerFound && hasEnoughChips){
                dispose();
            }
        }
        if (e.getSource()==resetButton){
            // resetting the fields to default
            usernameField.setText("");
            passwordField.setText("");
        }
    }
}
