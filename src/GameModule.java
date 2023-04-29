
import java.sql.SQLOutput;
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
    private boolean play;


    public GameModule(String loginName, String password, int chips) {
        dealer = new Dealer();
        player = new Player(loginName, password, chips);
    }

    // place to store all the bet chips
    public void addToPot(int chips) {
        pot += chips;
    }

    // Method to end game
    public void gameEnd() {
        System.out.println("Thanks for playing the game...");
        System.out.println(player.getLoginName() + ", You are left with  " + player.getChips() + " chips.");
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
                    } catch (Exception e) {
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
            while (loop) {
                boolean validAnswer = false;
                while (!validAnswer) {
                    System.out.print("\nDo you want to call or quit? [C/Q] : ");
                    try {
                        answerByUser = scan.next().toUpperCase().charAt(0);
                        scan.nextLine();
                        if (answerByUser == 'C') {
                            betAmount = player.playerCallBet();
                            player.deductChips(betAmount);
                            System.out.println("State bet: " + betAmount);
                            validAnswer = true;
                            loop = false;
                            return true;
                        } else if (answerByUser == 'Q') {

                            validAnswer = true;
                            loop = false;
                            return false;
                        } else {
                            System.out.println("Invalid input....");
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid input....");
                        scan.nextLine();
                    }
                }
            }

        }
        return true;
    }

    // Method that shows who won
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

    // deal and show the cards with hidden for dealer
    public void dealCardAndShow(Player player, Dealer dealer) {
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
    public void run() {
        //if player quit set to false
        String currentLoginName = player.getLoginName();
        play = true;
        boolean game = true;
        while (game) {
            while (play) {

                System.out.println();
                System.out.println("Highsum Game!");
                System.out.println("================================================================");
                System.out.println(currentLoginName + ", You have " + player.getChips() + " chips.");
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
                player.deductChips(betAmount);
                //dealer bet amount same as bet amount
                int dealerBetAmount = betAmount;
                System.out.println("State bet: " + betAmount);
                // Don't need to deduct chips from dealer
                int totalBetAmount = betAmount + dealerBetAmount;
                addToPot(totalBetAmount);

                System.out.println(currentLoginName + ", You are left with  " + player.getChips() + " chips.");
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
                    System.out.println(currentLoginName + ", You are left with  " + player.getChips() + " chips.");
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
                        System.out.println(currentLoginName + ", You are left with  " + player.getChips() + " chips.");
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
                            System.out.println(currentLoginName + ", You are left with  " + player.getChips() + " chips.");
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
                                System.out.println(currentLoginName + " wins.");
                                player.addChips(pot);
                                System.out.println(currentLoginName + ", You have " + player.getChips() + " chips.");

                            } else if (playerCardValue < dealerCardValue) {
                                System.out.println("Dealer wins...");
                                System.out.println(currentLoginName + ", You have " + player.getChips() + " chips.");
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

                        answerByUser = scan.next().toUpperCase().charAt(0);     // take only the first char no matter what the user want to enter
                        scan.nextLine();
                        if (answerByUser == 'Y') {
                            // Adding cards back to deck
                            dealer.addCardsBackToDeck(player.getCardsOnHand());
                            dealer.addCardsBackToDeck(dealer.getCardsOnHand());
                            player.getCardsOnHand().clear();        // Clearing the arraylist
                            dealer.getCardsOnHand().clear();

                            // Refreshing pot
                            pot = 0;
                            validAnswer = true;
                            //game = false;
                            play = true;
                            break;
                        } else if (answerByUser == 'N') {
                            validAnswer = true;
                            game = false;
                            int chipLift = player.getChips();
                            // Saving the current chips player have back to players.bin file
                            List<Player> players = Player.getAllPlayers();
                            for (Player player : players) {
                                String loginNameFromList = player.getLoginName();
                                if (currentLoginName.equals(loginNameFromList)) { // Making it case-sensitive
                                    player.setChips(chipLift);
                                    System.out.println(chipLift + " remaining chips have been save to player \"" + currentLoginName + "\"");
                                    break;
                                }
                            }
                            Admin ad = new Admin();
                            ad.writePlayersToFile(players, "players.bin");
                            gameEnd();
                            break;

                        } else {
                            System.out.println("Invalid input....");
                        }

                        scan.nextLine();
                    }
                }


            }
        }



    // Main method
    public static void main(String[] args) {
        System.out.println();
        System.out.println("HighSum Game");
        System.out.println("================================================================");

        Admin ad = new Admin(); // Creating admin object

        boolean quit = false;
        String option;
        while (!quit) {
            System.out.println("Login as \n1. Admin \n2. Player \n0. Quit");
            System.out.print("Enter your option number: ");
            option = scan.nextLine();
            System.out.println();
            switch (option) {

                // ADMIN PORTAL
                case "1" -> {
                    System.out.println("--------Welcome to admin portal---------");
                    boolean logIn = false;
                    do {
                        System.out.print("Enter password: ");
                        String password = scan.next();
                        scan.nextLine();

                        if (ad.checkAdminLogin(password)) {
                            System.out.println("Login approved.....");
                            logIn = true;
                        } else {
                            System.out.println("Incorrect admin password");
                            System.out.println();
                        }
                    } while (!logIn);
                    ad.adminMenu();

                }

                // PLAYER PORTAL
                case "2" -> {
                    System.out.println("------------Welcome to Player portal---------");
                    boolean logIn = false;
                    do {
                        List<Player> players = Player.getAllPlayers();
                        System.out.println("------------Available players to play---------");
                        ad.viewAllPlayer();
                        System.out.println();
                        System.out.print("Enter the username of the player you wants to play: ");
                        String playerToPlay = scan.nextLine();

                        Iterator<Player> iterator = players.iterator();
                        boolean playerFound = false;
                        while (iterator.hasNext()) {
                            Player gameplayer = iterator.next();
                            String loginName = gameplayer.getLoginName();
                            if (playerToPlay.equals(loginName)) {       // Making it case-sensitive
                                System.out.print("Enter password for player \"" + loginName + "\": ");
                                String enteredPW = scan.nextLine();
                                // Checking the player hashed password
                                if (gameplayer.getHashedPassword().equals(ad.passwordHash(enteredPW))) {
                                    System.out.println("Login successful!");
                                    logIn = true;
                                    System.out.println();
                                    GameModule game = new GameModule(gameplayer.getLoginName(), gameplayer.getHashedPassword(), gameplayer.getChips());

                                    // PLAYER NEED AT LEAST 50 CHIPS TO PLAY THE GAME
                                    if (gameplayer.getChips() >= 50) {
                                        game.run();
                                    } else {
                                        System.out.println("Player don't have sufficient chips to play the game.\nNeed at least 50 chips to play the game.");

                                    }
                                } else {
                                    System.out.println("Unsuccessful login to game! Incorrect password");
                                    System.out.println();

                                }
                                playerFound = true;
                                break;
                            } else {
                                logIn = true;
                            }
                        }
                        if (!playerFound) {
                            System.out.println("Could not find player. Going back to main menu....");
                        }

                    } while (!logIn);
                }
                case "0" -> {
                    quit = true;
                    System.out.println("Quiting Game.....");
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
            System.out.println();
        }
        scan.close();
    }
}
