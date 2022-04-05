import java.util.HashMap;
import java.util.Map;

// Taylor Merwin

public class Card {

  private Rank rank;
  private Suit suit;

  
  //each Card has attributes Rank and Suit
  //the attributes for each card are permanent
  enum Suit {
    HEARTS, DIAMONDS, SPADES, CLUBS
  }

  enum Rank {
    ACE(1), ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), JACK(
        10), QUEEN(10), KING(10);

    
    //each rank is assigned an int value, that allows the gameplay class to utilize the card
    int rankValue;

    Rank(int rankValue) {
      this.rankValue = rankValue;
    }
  }

  private final static Map<String, Card> CARD_CACHE = initCache();

  //Card Cache map contains every possible card in a deck of 52
  //allows deck objects to create a stack of card objects
  
  private static Map<String, Card> initCache() {

    Map<String, Card> cardCache = new HashMap<>();

    for (Suit suit : Suit.values()) {
      for (Rank rank : Rank.values()) {
        cardCache.put(cardKey(rank, suit), new Card(rank, suit));
      }
    }
    return cardCache;
  }

  static Card getCard(Rank rank, Suit suit) {
    final Card card = CARD_CACHE.get(cardKey(rank, suit));
    if (card != null) {
      return card;
    }
    throw new RuntimeException("Invalid card ! " + rank + " " + suit);
  }

  public Rank getRank() {
    return this.rank;
  }

  public Suit getSuit() {
    return this.suit;
  }

  private static String cardKey(final Rank rank, final Suit suit) {
    return rank + " of " + suit;
  }

  public Card(final Rank rank, final Suit suit) {
    this.rank = rank;
    this.suit = suit;
  }

  @Override
  public String toString() {
    return this.rank + " of " + this.suit;
  }

}
