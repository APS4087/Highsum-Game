import javax.swing.*;
import java.awt.*;

// Panel that display the cards
public class CardPanel extends JPanel {
    private Player player;
    private Dealer dealer;
    private ImageIcon cardBackImage;
    private final int cardWidth = 600;

    boolean showAllDealerCards= false;

    public CardPanel(Dealer dealer, Player player) {

        setBackground(Color.GREEN);
        setPreferredSize(new Dimension(800, 500));

        cardBackImage = new ImageIcon("cards/back.png");
        this.dealer = dealer;
        this.player = player;



    }
    public void setShowAllDealerCards(boolean showAllDealerCards) {
        this.showAllDealerCards = showAllDealerCards;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int x = 50;
        int y = 70;
        int gap = 20; // adding gap

        int i = 0;

        for (Card c : dealer.getCardsOnHand()) {
            // display dealer cards
            Graphics2D g2d = (Graphics2D) g.create();// dispose the Graphics2D
            if(!showAllDealerCards) {
                if (i == 0d) {
                    // hiding first card
                    g2d.scale(0.037, 0.037);
                    cardBackImage.paintIcon(this, g2d, x, y);
                    i++;
                } else {
                    g2d.scale(0.2, 0.2); // set the scaling factor to 0.2
                    c.paintIcon(this, g2d, x, y);
                }
            }else {
                g2d.scale(0.2, 0.2); // set the scaling factor to 0.2
                c.paintIcon(this, g2d, x, y);
            }
            g2d.dispose();  // dispose the Graphics2D

            x +=(cardWidth)+gap;
        }
        // display player cards
        x = 50;
        y = 1100;

        for (Card c : player.getCardsOnHand()) {
            // display dealer cards
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.scale(0.2, 0.2); // set the scaling factor to 0.2
            c.paintIcon(this, g2d, x, y);
            g2d.dispose(); // dispose the Graphics2D object
            x +=(cardWidth)+gap;
        }
    }
}
