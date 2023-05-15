import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private CardPanel cardPanel;
    private Dealer dealer;
    private Player player;
    private int count=0;

    public GameFrame(Dealer dealer, Player player)
    {
        this.dealer = dealer;
        this.player = player;
        cardPanel = new CardPanel(dealer,player);
        this.count=0;


        add(cardPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void updateGameTable()
    {
        cardPanel.repaint();
    }


    public void run() {

        ShuffleAnimation.show(this,2000);
        for(int i=0;i<5;i++) {
            dealer.dealCardTo(dealer);
            dealer.dealCardTo(player);
            pause();
            updateGameTable();

        }

    }

    //pause for 500msec
    private void pause() {
        try{
            Thread.sleep(500);

        }catch(Exception e){}
    }
}
