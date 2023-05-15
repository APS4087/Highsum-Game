import javax.swing.*;
import java.awt.*;

// Panel that shows the deck and chips on table
public class GameDetailsPanel extends JPanel {

    private ImageIcon deckIcon;
    private int chipsOnTable;
    private JPanel deckImagePanel;
    private JPanel chipsPanel;


    public GameDetailsPanel(){

        chipsOnTable = 0;   // initialize with 0 with game starts
        setBackground(Color.decode("#804000"));
        setPreferredSize(new Dimension(250, 0));

        // panel to store deck image
        deckImagePanel = new JPanel();
        ImageIcon originalDeckIcon = new ImageIcon("cardDeck.jpg");
        // Resizing image
        Image originalDeckIconImage = originalDeckIcon.getImage();
        Image resizedDeckIconImage = originalDeckIconImage.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        deckIcon = new ImageIcon(resizedDeckIconImage);

        // Adding the icon to deckImagePanel
        JLabel picOfDeck = new JLabel("Deck ",deckIcon,SwingConstants.RIGHT);
        picOfDeck.setForeground(Color.lightGray);
        deckImagePanel.add(picOfDeck);
        deckImagePanel.setBackground(Color.decode("#804000"));


        // Panel to store chips icon and display amount of chips
        chipsPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // draw chips icon and chips count
                ImageIcon originalChipsIcon = new ImageIcon("chipsImg.jpg");
                Image originalChipsIconImg = originalChipsIcon.getImage();
                Image resizedChipsIconImg = originalChipsIconImg.getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                ImageIcon chipsIcon = new ImageIcon(resizedChipsIconImg);
                String chipsCount = "Chips on table: " + chipsOnTable;

                g.drawImage(chipsIcon.getImage(), 0, 0, 70, 70, null);
                g.setFont(new Font("Arial", Font.PLAIN, 15));
                g.setColor(Color.lightGray);
                g.drawString(chipsCount, 80, 40);
            }
        };
        chipsPanel.setPreferredSize(new Dimension(0, 70));
        chipsPanel.setBackground(Color.decode("#804000"));

        setLayout(new BorderLayout());
        add(deckImagePanel, BorderLayout.NORTH);
        add(chipsPanel, BorderLayout.SOUTH);
    }

    public void setChipsOnTable(int chipsOnTable) {
        this.chipsOnTable = chipsOnTable;
        repaint();
    }
}
