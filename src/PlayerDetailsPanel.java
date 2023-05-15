import javax.swing.*;
import java.awt.*;

// Panel that display the player's card value and chips
public class PlayerDetailsPanel extends JPanel {
    private Player player;

    public PlayerDetailsPanel(Player player) {
        this.player = player;
        setPreferredSize(new Dimension(0, 100));
        setBackground(Color.decode("#804000"));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // set font and color
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.setColor(Color.lightGray);

        // draw player label
        String playerLabel = "Player: " + player.getLoginName();
        int playerLabelWidth = g.getFontMetrics().stringWidth(playerLabel);
        int playerLabelX = (getWidth() - playerLabelWidth) / 2;
        int playerLabelY = getHeight() / 2 - 15;
        g.drawString(playerLabel, playerLabelX, playerLabelY);

        // draw value and balance chips labels
        String valueLabel = "Value: " + player.getTotalCardsValue();
        String balanceChipsLabel = "Balance chips: " + player.getChips();
        g.setFont(new Font("Arial", Font.PLAIN, 15));
        g.setColor(Color.lightGray);
        int valueLabelWidth = g.getFontMetrics().stringWidth(valueLabel);
        int balanceChipsLabelWidth = g.getFontMetrics().stringWidth(balanceChipsLabel);
        int labelsX = (getWidth() - (valueLabelWidth + balanceChipsLabelWidth + 10)) / 2; // add 10 for spacing
        int labelsY = getHeight() / 2 + 15;
        g.drawString(valueLabel, labelsX, labelsY);
        g.drawString(balanceChipsLabel, labelsX + valueLabelWidth + 10, labelsY);
    }

}
