
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Player extends User implements Serializable{
    private int chips;
    private final ArrayList<Card> cardsOnHand = new ArrayList<>();
    static Scanner scan = new Scanner(System.in);


    public Player(String loginName, String haashedPassword, int chips){
        super(loginName,haashedPassword);
        this.chips = chips;
    }

    public static List<Player> getAllPlayers() {
        List<Player> players = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("players.bin"))) {
            while (true) {
                try {
                    Player player = (Player) ois.readObject();
                    players.add(player);
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("An error occurred while trying to load player data.");
            e.printStackTrace();
        }
        return players;
    }



    public void addChips(int amount){
        if(amount<0){
            System.out.println("Invalid chips amount");
        }
        else{
        this.chips+=amount;
        }
    }

    public boolean deductChips(int amount) {
        if (getChips() >= amount) {
            chips -= amount;
            return true;
        }else {
            System.out.println("Do not have enough chips to make this bet.");
            return false;
        }
    }

    public void setChips(int chips) {
        this.chips = chips;
    }

    //adding card to player hand
    public void addCards(Card card){
        this.cardsOnHand.add(card);
    }

    //show cards on player hands with the first card hidden
    public void showCardsOnHandWithHidden(){
        System.out.println(getLoginName());
        System.out.print("<Hidden card> ");                   //hiding the first card
        for (int i = 1; i < this.cardsOnHand.size(); i++) {   //by starting from the 2nd item in the list
            System.out.print(cardsOnHand.get(i) + " ");
        }
        System.out.println();
    }

    //reveal all card on hand
    public void revealAllCardsOnHand(){
        System.out.println(getLoginName());
        for(Card card:cardsOnHand){
            System.out.print(card+" ");
        }
        System.out.println();
    }


    //return the amount of chips
    public int getChips() {
        return this.chips;
    }

    // return total value of cards on hand
    public int getTotalCardsValue(){
        int total = 0;
        for(Card card:cardsOnHand){
            total+= card.getValue();
        }
        return total;
    }

    // method to show total value of cards
    public void showTotalCardsValue(){
        System.out.println("Value: "+getTotalCardsValue());
        System.out.println();
    }

    public ArrayList<Card> getCardsOnHand() {
        return cardsOnHand;
    }

    // get the value of card e.g.(Ace,1,2,Queen)
    public int getCardValue(ArrayList<Card> hand,int index){
        int intValueOfCard ;
        if(hand.size()<2){
            throw new IllegalArgumentException("Two cards are needed in hand");
        }
        Card cardToCheck = hand.get(index);
        String stringValueOfCard = cardToCheck.getName();
        // Using switch case to assign temp numbers to cards to compare
        intValueOfCard = switch (stringValueOfCard) {
            case "Ace" -> 1;
            case "Jack" -> 11;
            case "Queen" -> 12;
            case "King" -> 13;             // Giving King the highest temp value
            default -> Integer.parseInt(stringValueOfCard);
        };
        return intValueOfCard;
    }

    // getting the rank of suit
    public int getSuitRank(ArrayList<Card> hand, int i) {
        int suitRankValue;
        if(hand.size()<2){
            throw new IllegalArgumentException("Two cards are needed in hand");
        }
        Card cardToCheck = hand.get(i);
        String cardRankString = cardToCheck.getSuit();
        // Using switch case to assign temp numbers to cards to compare
        suitRankValue = switch (cardRankString) {
            case "Spades" -> 4;                           // Giving spades the highest value
            case "Hearts" -> 3;
            case "Clubs" -> 2;
            case "Diamonds" -> 1;
            default -> throw new IllegalArgumentException("Invalid error!");
        };
        return suitRankValue;
    }

    // method that check the player bet
    public int playerCallBet() {
        boolean validBet = false;
        int betAmount=0;
        while (!validBet) {
            System.out.print("Enter bet amount: ");
            try {
                betAmount = Integer.parseInt(scan.nextLine());
                if (betAmount <= 0) {
                    System.out.println("Invalid bet amount. Please enter a number between 1 and " + getChips() + ".");
                } else if (betAmount > getChips()) {
                    System.out.println("Invalid bet amount. You do no have enough bet amount.");
                } else {
                    validBet = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and " + getChips() + ".");
            }
        }return betAmount;
    }


}
