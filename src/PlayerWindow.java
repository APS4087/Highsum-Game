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

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton resetButton;
    private Dealer dealer;
    ArrayList<Player> players = (ArrayList<Player>) Player.getAllPlayers();
    private JList<String> playersJList;


    private Admin ad = new Admin();
    public PlayerWindow(){
        super("Player window");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(500, 400));

        dealer = new Dealer();
        // Create components
        JLabel titleLabel = new JLabel("Player Login");
        titleLabel.setFont(new Font("Arial",Font.BOLD,24));
        JLabel usernameLabel = new JLabel("Username:");
        usernameField  = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password:");
        passwordField= new JPasswordField(20);
        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        resetButton = new JButton("Reset");
        resetButton.addActionListener(this);

        // Setting up the Available players
        String separator = " / ";
        DefaultListModel<String> playersListModel = new DefaultListModel<>();
        for (Player player: players) {
            playersListModel.addElement(player.getChips()+separator+player.getLoginName());
        }
        playersJList = new JList<>(playersListModel);
        playersJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane playersListScrollPane = new JScrollPane(playersJList);
        playersListScrollPane.setPreferredSize(new Dimension(220,60));

        System.out.println(playersListModel);


        // Autofill to usernameField
        // using lambda expression
        playersJList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                // Getting the selected player
                String selectedString = playersJList.getSelectedValue();   // Getting only the player name and removing the chips
                int separatorPosition = selectedString.indexOf(separator);
                if(separatorPosition==-1){
                    System.out.println("");
                }
                String selectedPlayer = selectedString.substring(separatorPosition+separator.length());
                usernameField.setText(selectedPlayer);
            }
        });
        // Label
        JLabel playerListLabel = new JLabel("Available chips / Available Players:");

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


        panel.setBackground(Color.lightGray);
        // Add panel to frame
        add(panel);

        // Pack and display the frame
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
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

                        passwordField.setText("");
                        playerFound = true;

                    }
                }
            }
            if(!playerFound){
                JOptionPane.showMessageDialog(null,"Invalid username or password!","Invalid Login",JOptionPane.ERROR_MESSAGE);

                passwordField.setText("");

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
