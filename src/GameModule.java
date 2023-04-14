import java.util.*;

public class GameModule {

    static Scanner scan = new Scanner(System.in);
    private Dealer dealer;
    private Player player;
    private static String loginName;
    private static String password;
    private int betAmount;

    public GameModule(String loginName, String password) {
        dealer = new Dealer();
        player = new Player(loginName, password, 100);

    }

    public boolean checkBetAmount(int betAmount) {


        if (betAmount <= 0 || betAmount > player.getChips()) {
            return false;
        }
        return true;
    }


    public void run(){
        System.out.println();
        System.out.println("Highsum Game!");
        System.out.println("================================================================");
        System.out.println(loginName+", You have "+player.getChips()+" chips.");
        System.out.println("----------------------------------------------------------------");

        boolean play = true; //if player quit set to false

        dealer.shuffle();
        System.out.println("----------------------------------------------------------------");

        while(play) {

            //round 1

            dealer.dealCardTo(player);
            dealer.dealCardTo(dealer);
            dealer.dealCardTo(player);
            dealer.dealCardTo(dealer);

            System.out.println();
            dealer.showCardsOnHandWithHidden();
            System.out.println();
            player.revealAllCardsOnHand();
            player.showTotalCardsValue();

            // Determine the highest second visible card
            int playerSecondCardValue = player.getCardValue(player.getCardsOnHand(),1);
            int dealerSecondCardValue = player.getCardValue(dealer.getCardsOnHand(),1);
            int playerSecondSuitValue = player.getSuitRank(player.getCardsOnHand(),1);
            int dealerSecondSuitValue = player.getSuitRank(dealer.getCardsOnHand(),1);

            if(playerSecondCardValue>dealerSecondCardValue){
                System.out.println();
                System.out.println("Player call,");
                betAmount =player.playerCallBet();

                //System.out.println(betAmount);
            }else if(playerSecondCardValue<dealerSecondCardValue){
                betAmount = dealer.dealerCallBet();
            }else{
                if(playerSecondSuitValue>dealerSecondSuitValue){
                    System.out.println();
                    System.out.println("Player call,");
                    betAmount =player.playerCallBet();
                }else if(playerSecondSuitValue<dealerSecondSuitValue){
                    betAmount = dealer.dealerCallBet();
                }else{
                    System.out.println("\nIt's a tie");
                }
            }

            System.out.println(betAmount);


            play = false; //test ending
        }
    }
    public static void main(String[] args) {
        System.out.println("HighSum Game");
        System.out.println("================================================================");
        System.out.println();
        System.out.print("Enter Login Name: ");
        loginName = scan.next();
        scan.nextLine();
        System.out.print("Enter password: ");
        password = scan.next();
        scan.nextLine();
        GameModule app = new GameModule(loginName,password);


        app.run();
    }
}
