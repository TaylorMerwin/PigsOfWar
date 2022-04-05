import java.util.Collections;
import java.util.Stack;

//Taylor Merwin

public class Deck {

  final Stack<Card> deckCards;
  
  //constructor
  public Deck() {
    this.deckCards = initializeDeck();
  }
  
  //new deck objects create a stack with every card
  private Stack<Card> initializeDeck(){
    final Stack<Card> deckCards = new Stack<>();
    
    
    for(final Card.Suit suit : Card.Suit.values()) {
      for(final Card.Rank rank : Card.Rank.values()) {
          deckCards.push(Card.getCard(rank, suit));
      }
    }
    return deckCards;
  }
  
  void shuffle(){
    Collections.shuffle(deckCards);
  }
  
 @Override
  public String toString() {
    return "" + deckCards;
  }

 //Key method for gameplay
 public Card drawCard() {
   Card pulledCard = deckCards.firstElement();
   deckCards.remove(0);
   
   return pulledCard;
 }
 
}
