public class Card {
    private final String suit;
    private final String name;
    private final int value;

    public Card(String suit, String name , int value){
        this.suit = suit;
        this.name= name;
        this.value = value;
    }

    public String getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "<"+this.suit+" "+this.name+">";
    }

}
