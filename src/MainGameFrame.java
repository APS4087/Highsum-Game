import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TimerTask;

// Main game GUI
public class MainGameFrame extends JFrame {

    CardPanel cardPanel;
    GameDetailsPanel gameDetailsPanel;
    PlayerDetailsPanel playerDetailsPanel;
    private Player player;
    private Dealer dealer;
    private int pot;
    private int betAmount;
    private String wonBy;
    private boolean play;
    public MainGameFrame(){}
    public MainGameFrame(Player player, Dealer dealer ) {
        super("HighSum Game");
        this.dealer = dealer;
        this.player = player;
        cardPanel = new CardPanel(dealer,player);
        playerDetailsPanel = new PlayerDetailsPanel(player);
        gameDetailsPanel = new GameDetailsPanel();

        // Set up the JFrame

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 600);


        // top panel
        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.decode("#804000"));
        // Create the labels for the dealer
        JLabel dealerLabel = new JLabel("Dealer");
        dealerLabel.setForeground(Color.lightGray);
        dealerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        dealerLabel.setBounds(0, 800, 100, 30);
        add(dealerLabel);
        topPanel.add(dealerLabel);


        // Right panel
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.decode("#804000"));
        rightPanel.setPreferredSize(new Dimension(100,100));

        // Set the layout of the main panel to BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Add the left and right panels to the main panel
        mainPanel.add(rightPanel,BorderLayout.EAST);
        mainPanel.add(topPanel,BorderLayout.NORTH);
        mainPanel.add(playerDetailsPanel,BorderLayout.SOUTH);
        mainPanel.add(gameDetailsPanel, BorderLayout.WEST);
        mainPanel.add(cardPanel, BorderLayout.CENTER);

        // Add the main panel to the JFrame's content pane
        getContentPane().add(mainPanel);
        setVisible(true);
        setLocationRelativeTo(null);


    }

    public int getPot() {
        return pot;
    }

    public void updateGameTable()
    {
        cardPanel.repaint();
    }
    public void updatePlayerDetails(){playerDetailsPanel.repaint();}


    public String showWhoWon(int playerCard, int dealerCard, int playerSuit, int dealerSuit) {
        if (playerCard > dealerCard) {
            return "player";

        } else if (playerCard < dealerCard) {
            return "dealer";

        } else {
            if (playerSuit > dealerSuit) {
                return "player";

            } else if (playerSuit < dealerSuit) {
                return "dealer";

            } else {

                return "tie";
            }
        }
    }

     // GUI POPUP GETTING BET AMOUNT
    public int getBetAmount() {
        int betAmount = 0;
        boolean validInput = false;

        while (!validInput) {
            String betAmountString = JOptionPane.showInputDialog(this, "Player call, Enter your bet amount:");

            try {
                betAmount = Integer.parseInt(betAmountString);
                if (betAmount <= 0) {
                    JOptionPane.showMessageDialog(this, "Bet amount must be greater than zero!");
                } else if (betAmount > player.getChips()) {
                    JOptionPane.showMessageDialog(this,"You do not have enough chips.");
                }else {
                    validInput = true;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid bet amount!");
            }
        }

        return betAmount;
    }

    public void gameEnd() {
        JFrame frame = new JFrame();
        frame.setTitle("Game Over");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel messageLabel = new JLabel("Thanks for playing the game!");
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(messageLabel);

        JLabel chipsLabel = new JLabel(player.getLoginName() + ", you are left with " + player.getChips() + " chips.");
        chipsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(chipsLabel);

        JButton okButton = new JButton("OK");
        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        panel.add(okButton);

        frame.add(panel);
        frame.setVisible(true);


    }
    public boolean betAutomation(int playerCard, int dealerCard, int playerSuit, int dealerSuit) {
        wonBy = showWhoWon(playerCard, dealerCard, playerSuit, dealerSuit);
        if (wonBy.equalsIgnoreCase("dealer")) { // ignore all case
            // Dealer won
            betAmount = 10;  // dealer default bet
            JOptionPane.showMessageDialog(null, "Dealer Call,\nState bet: " + betAmount);

            // Asking player if they want to follow or quit
            boolean loop = true;
            while (loop) {
                Object[] options = { "Follow", "Quit" };
                int selectedOption = JOptionPane.showOptionDialog(null, "Do you want to follow?", "Follow or Quit",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                if (selectedOption == JOptionPane.YES_OPTION) { // Follow selected
                    if (betAmount > player.getChips()) { // if player don't have enough chips
                        JOptionPane.showMessageDialog(null, "Not enough chips.");
                        gameEnd();
                        return false;
                    }
                    player.deductChips(betAmount);
                    return true;
                } else if (selectedOption == JOptionPane.NO_OPTION) { // Quit selected
                    return false;
                }
            }
        } else if (wonBy.equalsIgnoreCase("player")) {
            // Player won
            // loop to ask if player want to call or quit
            boolean loop = true;
            while (loop) {
                Object[] options = { "Call", "Quit" };
                int selectedOption = JOptionPane.showOptionDialog(null, "Player call,\nDo you want to Call or Quit?","Call or Quit",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                if (selectedOption == JOptionPane.YES_OPTION) { // call selected
                    betAmount = getBetAmount();
                    player.deductChips(betAmount);
                    return true;
                } else if (selectedOption == JOptionPane.NO_OPTION) { // Quit selected
                    return false;
                }
            }

        }
        return true;
    }


    // place to store all the bet chips
    public void addToPot(int chips) {
        pot += chips;
    }

    public static void welcomeGUI() {
        JOptionPane.showOptionDialog(null, "Welcome to HighSum game.\nPress Continue to start the game", "HighSum Game",
                                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                                    new Object[]{"Continue"}, "Continue");

    }

    public void gameResult(Player player, Dealer dealer){
        int playerCardValue = player.getTotalCardsValue();
        int dealerCardValue = dealer.getTotalCardsValue();

        String resultMessage;

        if (playerCardValue > dealerCardValue) {
            // Player won
            player.addChips(pot);
            resultMessage = player.getLoginName() + " wins! You have " + player.getChips() + " chips left.";
        } else if (playerCardValue < dealerCardValue) {
            // Dealer won
            resultMessage = "Dealer wins... You have " + player.getChips() + " chips left.";
        } else {
            resultMessage = "The game is a tie!";
        }

        JOptionPane.showMessageDialog(null, resultMessage, "Game Result", JOptionPane.INFORMATION_MESSAGE);
    }

    public void run() {

        welcomeGUI();
        play = true;
        boolean game = true;
        while (play) {
            while (game) {

                ShuffleAnimation.show(this,1500);
                new DealingCardAnimation(this,1300);
                // First round
                dealer.dealCardTo(player);
                dealer.dealCardTo(dealer);
                dealer.dealCardTo(player);
                dealer.dealCardTo(dealer);
                pause();
                // UPDATING THE TABLES
                updateGameTable();

                // Getting the card value to compare
                int playerSecondCardValue = player.getCardValue(player.getCardsOnHand(), 1);
                int dealerSecondCardValue = player.getCardValue(dealer.getCardsOnHand(), 1);
                int playerSecondSuitValue = player.getSuitRank(player.getCardsOnHand(), 1);
                int dealerSecondSuitValue = player.getSuitRank(dealer.getCardsOnHand(), 1);

                updatePlayerDetails();

                // Stopping if player quits in bet automation
                if (betAutomation(playerSecondCardValue, dealerSecondCardValue, playerSecondSuitValue, dealerSecondSuitValue)) {

                    //dealer bet amount same as bet amount
                    int dealerBetAmount = betAmount;
                    addToPot(betAmount + dealerBetAmount);

                    gameDetailsPanel.setChipsOnTable(pot);   // repainting chips on table
                    updatePlayerDetails();

                    // 2ND ROUND AND ON WARDS
                    for (int i = 2; i < 5; i++) {
                        new DealingCardAnimation(this,1300);
                        dealer.dealCardTo(player);
                        dealer.dealCardTo(dealer);
                        pause();
                        updateGameTable();
                        // Getting the card value to compare
                        int playerCardValue = player.getCardValue(player.getCardsOnHand(), i);
                        int dealerCardValue = player.getCardValue(dealer.getCardsOnHand(), i);
                        int playerSuitValue = player.getSuitRank(player.getCardsOnHand(), i);
                        int dealerSuitValue = player.getSuitRank(dealer.getCardsOnHand(), i);
                        updatePlayerDetails();

                        if (betAutomation(playerCardValue, dealerCardValue, playerSuitValue, dealerSuitValue)) {

                            dealerBetAmount = betAmount;
                            addToPot(betAmount+dealerBetAmount);

                            gameDetailsPanel.setChipsOnTable(pot);
                            updatePlayerDetails();
                            System.out.println("Round" + i);
                        }
                        else {
                            break;
                        }
                        // Last round , display result
                        if(i==4){

                            // showing all dealer cards
                            JOptionPane.showMessageDialog(this, "Revealing all dealer's cards");
                            cardPanel.setShowAllDealerCards(true);
                            updateGameTable();

                            // Showing who won
                            gameResult(player,dealer);

                            // Save player back to player.bin <add an option that shows saving to file>


                        }

                    }

                }
                boolean anotherGame = askAnotherGame();
                if(!anotherGame){        // if user don't want to play another game
                    game = false;
                    play = false;
                    gameEnd();
                    dispose();
                }else {
                    // User choose to play another game
                    // Adding cards back to deck
                    dealer.addCardsBackToDeck(player.getCardsOnHand());
                    dealer.addCardsBackToDeck(dealer.getCardsOnHand());
                    // Clearing the arraylist
                    player.getCardsOnHand().clear();
                    dealer.getCardsOnHand().clear();
                    // refreshing pot
                    pot = 0;
                    play = true;
                    cardPanel.setShowAllDealerCards(false);  // Setting the dealer first card to hidden
                    updateGameTable();
                    updatePlayerDetails();
                    gameDetailsPanel.setChipsOnTable(pot);
                }
            }

        }


    }

    public boolean askAnotherGame() {
        boolean anotherGame = true;
        int again = JOptionPane.showConfirmDialog(null,"Another game?");
        if(again==JOptionPane.NO_OPTION || again==JOptionPane.CANCEL_OPTION) {
            anotherGame=false;
        }
        return anotherGame;
    }

    private void pause() {
        try{
            Thread.sleep(500);

        }catch(Exception e){}
    }

}
