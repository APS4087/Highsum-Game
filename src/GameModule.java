
import java.util.*;
public class GameModule {

    static Scanner scan = new Scanner(System.in);
    private final Dealer dealer;
    private final Player player;
    private static String loginName;
    private static String password;
    private int betAmount;
    private int pot;
    private String wonBy;
    private char answerByUser;
    private boolean play ;

    public GameModule(String loginName, String password) {
        dealer = new Dealer();
        player = new Player(loginName, password, 100);
    }

    // place to store all the bet chips
    public void addToPot(int chips){
        pot+=chips;
    }

    // Method to end game
    public void gameEnd(){
        System.out.println("Thanks for playing the game...");
        System.out.println(loginName+", You are left with  "+player.getChips()+" chips.");
        play = false;
    }

    // method that does the automations of betting
    public boolean betAutomation(int playerCard, int dealerCard, int playerSuit, int dealerSuit) {
        wonBy = showWhoWon(playerCard, dealerCard, playerSuit, dealerSuit);
        if (wonBy.equalsIgnoreCase("dealer")) {      //ignore all case
            // Dealer won

            System.out.println("Dealer Call");
            betAmount = dealer.dealerCallBet();
            System.out.println("State bet: " + betAmount);

            // Asking player if they want to follow or quit
            boolean loop = true;
            while (loop) {
                boolean validAnswer = false;
                while (!validAnswer) {
                    System.out.print("\nDo you want to follow? [Y/N] : ");
                    try {
                        answerByUser = scan.next().toUpperCase().charAt(0);     // take only the first char no matter what the user want to enter
                        scan.nextLine();
                        if (answerByUser == 'Y') {
                            if (!(player.deductChips(betAmount))) {         // if player don't have enough chips
                                System.out.println();
                                validAnswer = true;
                                gameEnd();
                                loop = false;
                                return true;
                            }
                            player.deductChips(betAmount);
                            validAnswer = true;
                            loop = false;
                            return true;
                        } else if (answerByUser == 'N') {
                            loop = false;
                            return false;
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
            System.out.println("Player Call");
            //loop to ask if player want to call or quit
            boolean loop = true;
            while (loop){
                boolean validAnswer = false;
                while(!validAnswer){
                    System.out.print("\nDo you want to call or quit? [C/Q] : ");
                    try{
                        answerByUser = scan.next().toUpperCase().charAt(0);
                        scan.nextLine();
                        if(answerByUser == 'C'){
                            betAmount = player.playerCallBet();
                            player.deductChips(betAmount);
                            System.out.println("State bet: " + betAmount);
                            validAnswer = true;
                            loop = false;
                            return true;
                        } else if (answerByUser=='Q') {

                            validAnswer = true;
                            loop = false;
                            return false;
                        } else {
                            System.out.println("Invalid input....");
                        }
                    }catch (Exception e ){
                        System.out.println("Invalid input....");
                        scan.nextLine();
                    }
                }
            }

        }return true;
    }

    // Method that shows who won
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
    // deal and show the cards with hidden for dealer
    public void dealCardAndShow(Player player, Dealer dealer){
        // Dealer deals cards
        dealer.dealCardTo(player);
        dealer.dealCardTo(dealer);
        // Show cards
        dealer.showCardsOnHandWithHidden();
        System.out.println();
        player.revealAllCardsOnHand();
        player.showTotalCardsValue();
    }

    // Run method
    public void run(){
         //if player quit set to false
        play = true;
        boolean game = true;
        while (game) {
            while (play) {

                System.out.println();
                System.out.println("Highsum Game!");
                System.out.println("================================================================");
                System.out.println(loginName+", You have "+player.getChips()+" chips.");
                System.out.println("----------------------------------------------------------------");
                System.out.print("Game starts - ");
                dealer.shuffle();
                // ROUND 1
                System.out.println("----------------------------------------------------------------");
                System.out.println("Dealer dealing cards - Round 1 ");
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
                int playerSecondCardValue = player.getCardValue(player.getCardsOnHand(), 1);
                int dealerSecondCardValue = player.getCardValue(dealer.getCardsOnHand(), 1);
                int playerSecondSuitValue = player.getSuitRank(player.getCardsOnHand(), 1);
                int dealerSecondSuitValue = player.getSuitRank(dealer.getCardsOnHand(), 1);

                wonBy = showWhoWon(playerSecondCardValue, dealerSecondCardValue, playerSecondSuitValue, dealerSecondSuitValue);
                if (wonBy.equalsIgnoreCase("player")) {
                    System.out.print("Player call, ");
                    betAmount = player.playerCallBet();
                } else if (wonBy.equalsIgnoreCase("dealer")) {
                    System.out.print("Dealer call, ");
                    betAmount = dealer.dealerCallBet();
                } else if (wonBy.equalsIgnoreCase("tie")) {
                    System.out.println("Its a tie");
                    System.out.println("Nobody call");
                }

                //dealer bet amount same as bet amount
                int dealerBetAmount = betAmount;
                System.out.println("State bet: " + betAmount);
                player.deductChips(betAmount);                           // Don't need to deduct chips from dealer
                int totalBetAmount = betAmount + dealerBetAmount;
                addToPot(totalBetAmount);

                System.out.println(loginName + ", You are left with  " + player.getChips() + " chips.");
                System.out.println("Bet on table : " + pot);

                // ROUND 2
                System.out.println();
                System.out.println("----------------------------------------------------------------");
                System.out.println("Dealer dealing cards - Round 2 ");
                System.out.println("----------------------------------------------------------------");

                dealCardAndShow(player, dealer);

                // Getting the card value to compare
                int playerThirdCardValue = player.getCardValue(player.getCardsOnHand(), 2);
                int dealerThirdCardValue = player.getCardValue(dealer.getCardsOnHand(), 2);
                int playerThirdSuitValue = player.getSuitRank(player.getCardsOnHand(), 2);
                int dealerThirdSuitValue = player.getSuitRank(dealer.getCardsOnHand(), 2);

                //stopping if player quit in betAutomation
                if (betAutomation(playerThirdCardValue, dealerThirdCardValue, playerThirdSuitValue, dealerThirdSuitValue)) {
                    //dealer bet amount same as bet amount
                    dealerBetAmount = betAmount;
                    totalBetAmount = betAmount + dealerBetAmount;
                    addToPot(totalBetAmount);
                    System.out.println(loginName + ", You are left with  " + player.getChips() + " chips.");
                    System.out.println("Bet on table : " + pot);

                    // ROUND 3
                    System.out.println();
                    System.out.println("----------------------------------------------------------------");
                    System.out.println("Dealer dealing cards - Round 3 ");
                    System.out.println("----------------------------------------------------------------");
                    // Dealer deal cards
                    dealCardAndShow(player, dealer);

                    // Getting the card value to compare
                    int playerFourthCardValue = player.getCardValue(player.getCardsOnHand(), 3);
                    int dealerFourthCardValue = player.getCardValue(dealer.getCardsOnHand(), 3);
                    int playerFourthSuitValue = player.getSuitRank(player.getCardsOnHand(), 3);
                    int dealerFourthSuitValue = player.getSuitRank(dealer.getCardsOnHand(), 3);

                    if (betAutomation(playerFourthCardValue, dealerFourthCardValue, playerFourthSuitValue, dealerFourthSuitValue)) {
                        //dealer bet amount same as bet amount
                        dealerBetAmount = betAmount;
                        totalBetAmount = betAmount + dealerBetAmount;
                        addToPot(totalBetAmount);
                        System.out.println(loginName + ", You are left with  " + player.getChips() + " chips.");
                        System.out.println("Bet on table : " + pot);

                        // ROUND 4
                        System.out.println();
                        System.out.println("----------------------------------------------------------------");
                        System.out.println("Dealer dealing cards - Round 4");
                        System.out.println("----------------------------------------------------------------");

                        dealCardAndShow(player, dealer);

                        // Getting the card value to compare
                        int playerFifthCardValue = player.getCardValue(player.getCardsOnHand(), 4);
                        int dealerFifthCardValue = player.getCardValue(dealer.getCardsOnHand(), 4);
                        int playerFifthSuitValue = player.getSuitRank(player.getCardsOnHand(), 4);
                        int dealerFifthSuitValue = player.getSuitRank(dealer.getCardsOnHand(), 4);

                        if (betAutomation(playerFifthCardValue, dealerFifthCardValue, playerFifthSuitValue, dealerFifthSuitValue)) {
                            //dealer bet amount same as bet amount
                            dealerBetAmount = betAmount;
                            totalBetAmount = betAmount + dealerBetAmount;
                            addToPot(totalBetAmount);
                            System.out.println(loginName + ", You are left with  " + player.getChips() + " chips.");
                            System.out.println("Bet on table : " + pot);

                            // END GAME RESULTS
                            System.out.println();
                            System.out.println("----------------------------------------------------------------");
                            System.out.println("Game End - Dealer reveals hidden cards");
                            System.out.println("----------------------------------------------------------------");

                            dealer.revealAllCardsOnHand();   // Dealer reveals all cards on hand
                            dealer.showTotalCardsValue();

                            player.revealAllCardsOnHand();
                            player.showTotalCardsValue();

                            int playerCardValue = player.getTotalCardsValue();
                            int dealerCardValue = dealer.getTotalCardsValue();

                            if (playerCardValue > dealerCardValue) {
                                System.out.println(loginName + " wins.");
                                player.addChips(pot);
                                System.out.println(loginName + ", You have " + player.getChips() + " chips.");

                            } else if (playerCardValue < dealerCardValue) {
                                System.out.println("Dealer wins...");
                                System.out.println(loginName + ", You have " + player.getChips() + " chips.");
                            } else {
                                System.out.println("The game is a tie!");

                            }
                            System.out.println("Dealer shuffles used cards and place behind the deck.");
                        } else {
                            game = false;
                        }
                    } else {
                        game = false;
                    }
                }
                play = false;
                boolean validAnswer = false;
                while (!validAnswer) {
                    System.out.print("Next game? (Y/N) : ");
                    try {
                        answerByUser = scan.next().toUpperCase().charAt(0);     // take only the first char no matter what the user want to enter
                        scan.nextLine();
                        if (answerByUser == 'Y') {


                            validAnswer = true;
                            //game = false;
                            play = true;
                            break;
                        } else if (answerByUser == 'N') {
                            validAnswer = true;
                            game = false;
                            gameEnd();
                            break;

                        } else {
                            System.out.println("Invalid input....");
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid input....");
                        scan.nextLine();
                    }
                }
                // Adding the games back to deck
                dealer.addCardsBackToDeck(player.getCardsOnHand());
                dealer.addCardsBackToDeck(dealer.getCardsOnHand());

                // setting the player chips back to 100
                player.setChips(100);
                // No need to save player chips balance in assignment 1
                // Refreshing pot
                pot =0;
            }
        }
    }

    // Main method
    public static void main(String[] args) {
        System.out.println("HighSum Game");
        System.out.println("================================================================");
        System.out.println();

        // Simple login check
        loginName = "Icepeak";   // Hardcoding login name and password for assignment 1
        password = "password";
        boolean logIn = false;

        do {

            System.out.print("Enter Login Name: ");
            String enteredName = scan.next();
            scan.nextLine();
            System.out.print("Enter password: ");
            String enteredPassword = scan.next();
            scan.nextLine();


            if (enteredName.equals(loginName) && enteredPassword.equals(password)) {
                System.out.println("Login successful!");
                logIn = true;
                System.out.println();
                GameModule app = new GameModule(loginName, password);
                app.run();
            } else {
                System.out.println("Unsuccessful login to game!");
                System.out.println();
            }
        }while (!logIn);
        scan.close();
    }
}
