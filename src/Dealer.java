import java.util.ArrayList;


public class Dealer extends Player{


    private final Deck deck ;
    public Dealer(){
        super("Dealer","",0);
        deck = new Deck();
    }
    public void shuffle(){
        System.out.println("Dealer shuffle deck....");
        deck.shuffle();
    }

    public void dealCardTo(Player player){
        Card card = deck.dealCard();
        player.addCards(card);
    }

    public void addCardsBackToDeck(ArrayList<Card> cardsOnHand){
        deck.appendCard(cardsOnHand);
    }
    public int dealerCallBet(){


        //Random random = new Random();
        //int dealerBet = random.nextInt(1,100);
        // assignment 1 dealer only bet 10 chips each round
        return 10;
    }

    //think of more actions the dealer need to do
    //conduct the game and implements the methods
    public static void main(String[] args) {
        Dealer dealer = new Dealer();
        dealer.dealerCallBet();
    }
}
