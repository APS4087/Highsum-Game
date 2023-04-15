import java.util.*;

public class GameModule {

    static Scanner scan = new Scanner(System.in);
    private Dealer dealer;
    private Player player;
    private static String loginName;
    private static String password;
    private int betAmount;
    private int dealerBetAmount;
    private int totalBetAmount;
    private int pot;
    private String wonBy;
    private char answerByUser;
    private boolean play ;

    public GameModule(String loginName, String password) {
        dealer = new Dealer();
        player = new Player(loginName, password, 100);

    }

    // place to store all the bet
    public void addToPot(int chips){
        pot+=chips;
    }


    // method that does the automations of betting
    public void betAutomation(int playerCard, int dealerCard, int playerSuit, int dealerSuit) {
        wonBy = showWhoWon(playerCard, dealerCard, playerSuit, dealerSuit);
        if (wonBy.equalsIgnoreCase("dealer")) {      //ignore all case
            // Dealer won
            System.out.println();
            System.out.println("Dealer Call");
            betAmount = dealer.dealerCallBet();
            System.out.println("State bet: " + betAmount);

            // Asking player if they want to follow or quit

            while (play) {
                boolean validAnswer = false;
                while (!validAnswer) {
                    System.out.print("\nDo you want to follow? [Y/N] : ");
                    try {
                        answerByUser = scan.next().toUpperCase().charAt(0);     // take only the first char no matter what the user want to enter
                        scan.nextLine();
                        if (answerByUser == 'Y') {

                            if (!(player.deductChips(betAmount))) {         // if player don't have enough chips and don't want to all in
                                System.out.println();
                                System.out.println("Thanks for playing.....");
                                validAnswer = true;
                                play = false;
                                break;
                            }
                            player.deductChips(betAmount);
                            validAnswer = true;
                            play =false;
                            break;

                        } else if (answerByUser == 'N') {
                            System.out.println("Thanks for playing.....");
                            validAnswer = true;
                            play = false;
                            break;   //exit both loop
                        } else {
                            System.out.println("Invalid input...");
                        }
                    }catch (Exception e){
                        System.out.println("Invalid input...");
                        scan.nextLine();
                    }
                }
            }
        } else if (wonBy.equalsIgnoreCase("player")) {
            // Player won
            System.out.println();
            //loop to ask if player want to call or quit
            while (play){
                boolean validAnswer = false;
                while(!validAnswer){
                    System.out.println("Do you want to call or quit? [C/Q] : ");
                    try{
                        answerByUser = scan.next().toUpperCase().charAt(0);
                        scan.nextLine();
                        if(answerByUser == 'C'){
                            betAmount = player.playerCallBet();
                            player.deductChips(betAmount);
                            System.out.println("State bet: " + betAmount);
                            validAnswer = true;
                            play = false;
                            break;
                        } else if (answerByUser=='Q') {
                            System.out.println("Thanks for playing....");
                            validAnswer = true;
                            play = false;
                            break;
                        } else {
                            System.out.println("Invalid input....");
                        }
                    }catch (Exception e ){
                        System.out.println("Invalid input....");
                        scan.nextLine();
                    }
                }
            }

        }
    }
    public String showWhoWon(int playerCard, int dealerCard, int playerSuit, int dealerSuit){
        if(playerCard>dealerCard){
            return "player";

        }else if(playerCard<dealerCard){
            return "dealer";

        }else{
            if(playerSuit>dealerSuit){
                return "player";

            }else if(playerSuit<dealerSuit){
                return "dealer";

            }else{

                return "tie";
            }
        }
    }

    public void run(){
        System.out.println();
        System.out.println("Highsum Game!");
        System.out.println("================================================================");
        System.out.println(loginName+", You have "+player.getChips()+" chips.");
        System.out.println("----------------------------------------------------------------");

         //if player quit set to false
        play = true;
        dealer.shuffle();



        while(play) {


            // ROUND 1
            System.out.println("Round 1 ");
            System.out.println("----------------------------------------------------------------");
            // Dealer two cards in round 1
            dealer.dealCardTo(player);
            dealer.dealCardTo(dealer);
            dealer.dealCardTo(player);
            dealer.dealCardTo(dealer);
            // Show cards
            dealer.showCardsOnHandWithHidden();
            System.out.println();
            player.revealAllCardsOnHand();
            player.showTotalCardsValue();

            // Getting the card value to compare
            int playerSecondCardValue = player.getCardValue(player.getCardsOnHand(),1);
            int dealerSecondCardValue = player.getCardValue(dealer.getCardsOnHand(),1);
            int playerSecondSuitValue = player.getSuitRank(player.getCardsOnHand(),1);
            int dealerSecondSuitValue = player.getSuitRank(dealer.getCardsOnHand(),1);


            wonBy=showWhoWon(playerSecondCardValue,dealerSecondCardValue,playerSecondSuitValue,dealerSecondSuitValue);

            if(wonBy.equalsIgnoreCase("player")){
                System.out.println("Player call,");
                betAmount = player.playerCallBet();


            }else if(wonBy.equalsIgnoreCase("dealer")){
                System.out.println("Dealer call,");
                betAmount = dealer.dealerCallBet();

            }else if(wonBy.equalsIgnoreCase("tie")){
                System.out.println("Its a tie");
                System.out.println("Nobody call");
            }
            //dealer bet amount same as bet amount

            dealerBetAmount = betAmount;
            System.out.println("State bet: "+betAmount);
            player.deductChips(betAmount);   // Don't need to deduct chips from dealer
            totalBetAmount = betAmount+dealerBetAmount;
            addToPot(totalBetAmount);

            System.out.println(loginName+", You are left with  "+player.getChips()+" chips.");

            System.out.println("Bet on table : "+pot);


            // ROUND 2
            System.out.println();
            System.out.println("Round 2 ");
            System.out.println("----------------------------------------------------------------");
            // Dealer deal cards
            dealer.dealCardTo(player);
            dealer.dealCardTo(dealer);
            // Show cards
            dealer.showCardsOnHandWithHidden();
            System.out.println();
            player.revealAllCardsOnHand();
            player.showTotalCardsValue();

            // Getting the card value to compare
            int playerThirdCardValue = player.getCardValue(player.getCardsOnHand(),2);
            int dealerThirdCardValue = player.getCardValue(dealer.getCardsOnHand(),2);
            int playerThirdSuitValue = player.getSuitRank(player.getCardsOnHand(),2);
            int dealerThirdSuitValue = player.getSuitRank(dealer.getCardsOnHand(),2);

            betAutomation(playerThirdCardValue,dealerThirdCardValue,playerThirdSuitValue,dealerThirdSuitValue);

            //dealer bet amount same as bet amount
            dealerBetAmount = betAmount;
            totalBetAmount = betAmount+dealerBetAmount;
            addToPot(totalBetAmount);

            System.out.println(loginName+", You are left with  "+player.getChips()+" chips.");

            System.out.println("Bet on table : "+pot);

            // ROUND 3
            System.out.println();
            System.out.println("Round 3 ");
            System.out.println("----------------------------------------------------------------");
            // Dealer deal cards
            dealer.dealCardTo(player);
            dealer.dealCardTo(dealer);
            // Show cards
            dealer.showCardsOnHandWithHidden();
            System.out.println();
            player.revealAllCardsOnHand();
            player.showTotalCardsValue();




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
