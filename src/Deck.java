
import java.util.*;
public class Deck {

    private ArrayList<Card> cards;

    //method to create the deck
    public Deck(){
        cards = new ArrayList<Card>();
        String[] suits = {"Spades","Hearts","Clubs","Diamonds"};
        for(String suit: suits){
            Card aceCard = new Card(suit,"Ace",1);
            cards.add(aceCard);
            for (int i =2; i<=10;i++){
                Card card = new Card(suit,i+"",i);
                cards.add(card);
            }
            String[] pictureCards ={"Jack","Queen","King"};
            for (String pictureName: pictureCards){
                Card pictureCard = new Card(suit,pictureName,10);
                cards.add(pictureCard);
            }
        }
    }

    //method that shuffles the deck
    public void shuffle(){
        Random random = new Random();
        for(int i=0; i<10000;i++){      //shuffling 10000 times
            int indexA = random.nextInt(cards.size());
            int indexB = random.nextInt(cards.size());
            Card cardA = cards.get(indexA);
            Card cardB = cards.get(indexB);
            cards.set(indexA,cardB);   //swapping card positions
            cards.set(indexB,cardA);
        }
    }

    //showing all cards in deck
    public void showCards(){
        for(Card card:cards){
            System.out.println(card);
        }
    }

    //removing card at index 0 and dealing it to player
    public Card dealCard(){
        return cards.remove(0);
    }

    //adding card back to deck
    public void appendCard(Card card){
        cards.add(card);
    }

    //adding a list of card to deck
    public void appendCard(ArrayList<Card> cards){
        for (Card card:cards){
            appendCard(card);
        }
    }

    //testing deck class
    public static void main(String[] args) {
        Deck deck = new Deck();
        //deck.shuffle();

        //deck.showCards();
        Card card1 =deck.dealCard();
        Card card2 =deck.dealCard();
        Card card3 =deck.dealCard();

        /*
        System.out.println(card1);
        System.out.println(card2);
        System.out.println(card3);
        System.out.println();
        deck.showCards();
        */


        System.out.println();

        deck.showCards();
    }
}
