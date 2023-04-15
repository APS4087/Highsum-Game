import javax.print.DocFlavor;
import java.awt.desktop.SystemEventListener;
import java.util.ArrayList;
import java.util.Scanner;

public class Player extends User{
    private int chips;
    private ArrayList<Card> cardsOnHand;
    static Scanner scan = new Scanner(System.in);

    public Player(String loginName, String password, int chips){
        super(loginName,password);
        this.chips = chips;
        this.cardsOnHand = new ArrayList<Card>();
    }

    public void addChips(int amount){
        if(amount<0){
            System.out.println("Invalid chips amount");
        }
        else{
        this.chips+=amount;   //need error check
        }
    }

    public boolean deductChips(int amount) {
        if (getChips() >= amount) {
            chips -= amount;
            return true;
        }else {

            System.out.println("Do not have enought chips to make this bet.");
            return false;
        }
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

    public int getTotalCardsValue(){
        int total = 0;
        for(Card card:cardsOnHand){
            total+= card.getValue();
        }
        return total;
    }
    public void showTotalCardsValue(){
        System.out.println("Value: "+getTotalCardsValue());
    }

    public ArrayList<Card> getCardsOnHand() {
        return cardsOnHand;
    }

    // get the value of card e.g.(Ace,1,2,Queen)
    public int getCardValue(ArrayList<Card> hand,int index){
        int intValueOfCard =0;
        if(hand.size()<2){
            throw new IllegalArgumentException("Two cards are needed in hand");
        }
        Card cardToCheck = hand.get(index);
        String stringValueOfCard = cardToCheck.getName();

        if(stringValueOfCard.equals("Ace")){
            intValueOfCard = 1;
        }else if (stringValueOfCard.equals("Jack")) {
            intValueOfCard = 11;
        }else if (stringValueOfCard.equals("Queen")) {
            intValueOfCard = 12;
        }else if (stringValueOfCard.equals("King")) {
            intValueOfCard = 13;
        }
        else {
            intValueOfCard = Integer.parseInt(stringValueOfCard);
        }
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
        if(cardRankString.equals("Spades")){          // Giving spades the highest value
            suitRankValue = 4;
        }else if(cardRankString.equals("Hearts")){
            suitRankValue = 3 ;
        } else if (cardRankString.equals("Clubs")) {
            suitRankValue = 2 ;
        }else if (cardRankString.equals("Diamonds")){
            suitRankValue = 1 ;
        }else {
            throw new IllegalArgumentException("Invalid error!");
        }
        return suitRankValue;
    }

    /*
    public int hasEnoughChips(int betAmount){
        if(getChips()>=betAmount){
            return betAmount;
        }else {
            System.out.println("You do not have enough chi");
        }
    }
*/

    public int playerCallBet() {
        boolean validBet = false;
        int betAmount=0;
        while (!validBet) {
            System.out.print("\nEnter bet amount: ");
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

    public static void main(String[] args) {
        Player player = new Player("IcePeak","password",100);

        Deck deck = new Deck();
        deck.shuffle();

        Card card1 = deck.dealCard();
        Card card2 = deck.dealCard();

        player.addCards(card1);
        player.addCards(card2);
        player.showCardsOnHandWithHidden();
        player.showTotalCardsValue();
        ArrayList<Card> cards = player.getCardsOnHand();
        deck.appendCard(cards);
        deck.showCards();
    }
}
