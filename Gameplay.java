import java.util.Scanner;
import java.util.concurrent.TimeUnit;


// malcolm

public class Gameplay extends Throwable {

  public Gameplay(Scanner s) throws InterruptedException {
    System.out.println("Welcome to PIGS OF WAR!!! \nHow many players do you have? \nPlease type '1' or '2'");

    // needs to only accept 1 or 2
    while (!s.hasNextInt()) {
      System.out.println("Please enter please enter a valid number.");
      s.next();
    }
    int players = s.nextInt();

    while (players != 1 && players != 2) {
      System.out.println("Please type '1' or '2'");
      
      while (!s.hasNextInt()) {
        System.out.println("Please enter please enter a valid number.");
        s.next();
      }
      players = s.nextInt();
    }
    if (players == 2) {
      twoPlayerGame(s);
    }
    else {
      singlePlayerGame(s);
    }
  }

  private class Client {
    private String name;
    private int pointTotal;

    public Client(String name) {
      this.name = name;
      this.pointTotal = 0;
    }

    public String getName() {
      return name;
    }

    public int getPointTotal() {
      return pointTotal;
    }

    public void setPointTotal(int x) {
      pointTotal = x;
    }

    public boolean decide(int diceSides, int rollsThusFar, int pointsScored, int rolls, int playTo) {
      
      //double version of playTo, points, scored and point total
      double dpTo = (double) playTo, dPS = (double) pointsScored, dPT = (double) pointTotal;
      //how many rolls you've taken out of the chance you will roll a 1
      double oneChance = (double) (rollsThusFar + 1) / (diceSides);
      //how many points you get if you rolled the most likely outcome of the two dice for every roll you had
      double likelyOutcome = ((diceSides + 1) * rolls);
      //half of likelyOutcome
      double desiredOutcome = likelyOutcome / 2; 
      //The likelihood that the AI will roll again
      //Decreases as AI's total points increase
      double boldness = ((desiredOutcome - pointsScored) / (desiredOutcome)) * (((dpTo + dpTo / 3) - dPT) / dpTo);

      /*
       * STATS (for debugging)
       * System.out.println("One Chance = " + oneChance + " (" + (rollsThusFar + 1) + " / " + diceSides + ")");
       * System.out.println("Boldness = " + boldness);
       * System.out.println("likely outcome = " + likelyOutcome);
       * System.out.println("desiredOutcome = " + desiredOutcome);
       */
      
      if (0 > boldness) return false;
      else if (oneChance < boldness) return true;
      else return false;
    }
  }


  public int clientTurn(Client client, Scanner s, Deck gameDeck, Die gameDie) {
    int turnPoints = 0;
    Card turnCard = gameDeck.drawCard();
    System.out.println(client.name + " drew the " + turnCard.toString());
    System.out.println(client.name + " has " + turnCard.getRank().rankValue + " rolls left");
    int rolls = turnCard.getRank().rankValue;

    while (rolls > 0) {
      System.out.println("Enter 'roll' to roll die, or 'end' to end your turn.");
      String input = s.nextLine();

      if (input.equals("roll")) {
        int roll1 = gameDie.roll();
        int roll2 = gameDie.roll();
        int rollTotal = roll1 + roll2;
        rolls--;

        System.out.println(client.name + " has rolled a " + "[" + roll1 + "]" + " and a " + "[" + roll2 + "]");
        if (rollTotal == 2) {
          turnPoints = 0;
          client.setPointTotal(0);
          System.out.println("You have rolled snake-eyes. Haha, get rekt lmao. \n Turn points reduced to 0. \n Total points reduced to 0. \n Turn ended.\n");
          return 0;
        }
        else if (roll1 == 1 || roll2 == 1) {
          turnPoints = 0;
          System.out.println("You rolled a 1! \n Turn points reduced to 0. \n Turn ended.\n");
          return 0;
        }
        else {
          turnPoints += rollTotal;
          System.out.println(rollTotal + " added to turn point total. \n Turn points increased to "
              + turnPoints + ".\n");
        }
      }
      else if (input.equals("end")) {
        client.setPointTotal(client.pointTotal + turnPoints);
        System.out.println(turnPoints + " points scored! New point total is " + client.pointTotal);
        System.out.println(" Turn ended.");
        return turnPoints;
      }
      else {
        System.out.println("please enter a valid command.");
        s.next();
      }
    }

    System.out.println("Out of rolls.");
    client.setPointTotal(client.pointTotal + turnPoints);
    System.out.println(turnPoints + " points scored! " + client.name + "'s point total is " + client.pointTotal);
    System.out.println(" Turn ended.");
    System.out.println("");
    return turnPoints;
  }

  // computer turn for each time it is the AI's turn
  public int computerTurn(Client client, Scanner s, Deck gameDeck, Die gameDie, int playTo)
      throws InterruptedException {
    int turnPoints = 0;
    Card turnCard = gameDeck.drawCard();
    System.out.println(client.name + " drew the " + turnCard.toString());
    System.out.println(client.name + " has " + turnCard.getRank().rankValue + " rolls left");
    int rolls = turnCard.getRank().rankValue;
    int rollsThusFar = 0;

    while (rolls > 0) {
      // So there can be a wait between rolls (for aesthetics)
      TimeUnit.SECONDS.sleep((long) (2 + (Math.random() * 3)));

      boolean willRoll = client.decide(gameDie.getSides(), rollsThusFar, turnPoints, rolls, playTo);

      if (willRoll || rollsThusFar == 0) {
        int roll1 = gameDie.roll();
        int roll2 = gameDie.roll();
        int rollTotal = roll1 + roll2;
        rolls--;
        rollsThusFar++;
        System.out.println(client.name + " rolls the dice");
        System.out.println(client.name + " has rolled a " + "[" + roll1 + "]" + " and a " + "[" + roll2 + "]");

        if (rollTotal == 2) {
          turnPoints = 0;
          client.setPointTotal(0);
          System.out.println("AI rolled snake-eyes. Haha, get rekt lmao. \n Turn points reduced to 0. \n Total points reduced to 0. \n Turn ended.\n");
          return 0;
        }
        else if (roll1 == 1 || roll2 == 1) {
          turnPoints = 0;
          System.out.println("AI rolled a 1! \n Turn points reduced to 0. \n Turn ended.\n");
          return 0;
        }
        else {
          turnPoints += rollTotal;
          System.out.println(rollTotal + " added to turn point total. \n Turn points increased to "
              + turnPoints + ".\n");
        }
      }
      else {
        // end of AI turn
        System.out.println("The AI decides to hold");
        client.setPointTotal(client.pointTotal + turnPoints);
        System.out.println(turnPoints + " points scored! New point total is " + client.pointTotal);
        System.out.println(" Turn ended.");
        return turnPoints;
      }
    }
    System.out.println("Out of rolls.");
    client.setPointTotal(client.pointTotal + turnPoints);
    System.out.println(turnPoints + " points scored! " + client.name + "'s point total is " + client.pointTotal);
    System.out.println(" Turn ended.");
    System.out.println("");
    return turnPoints;
  }

  public Client twoPlayerGame(Scanner s) {
    Client winner = null;
    Deck gameDeck = new Deck();
    gameDeck.shuffle();

    System.out.println("How many sides should the game dice have?");
    while (!s.hasNextInt() || s.nextInt() < 1) {
      System.out.println("Please enter valid number of sides (ex. '6')");
      s.next();
    }
    Die gameDie = new Die(s.nextInt());
    
    while(gameDie.getSides() < 1) {
      System.out.println("Dice must have at least 1 side - try again");
      s.next();
      gameDie = new Die(s.nextInt());
    }
    s.nextLine();

    System.out.println("How many points will you play to?");
    while (!s.hasNextInt()) {
      System.out.println("Please enter valid number (ex. '100')");
      s.nextLine();
    }
    int playTo = s.nextInt();
    s.nextLine();

    System.out.print("Please enter Player 1 name: ");
    Client p1 = new Client(s.nextLine());
    System.out.println(p1.getName() + " entered.");

    System.out.print("Please enter Player 2 name: ");
    Client p2 = new Client(s.nextLine());
    System.out.println(p2.getName() + " entered.");

    int rounds = 0;

    while (winner == null) {
      rounds++;
      System.out.println("----------------------------");
      System.out.println("          Round " + rounds);
      System.out.println("----------------------------");
      System.out.println(p1.getName() + "'s Point Total: " + p1.getPointTotal());
      System.out.println(p2.getName() + "'s Point Total: " + p2.getPointTotal());
      System.out.println("----------------------------");


      clientTurn(p1, s, gameDeck, gameDie);
      if (p1.getPointTotal() >= playTo) {
        System.out.println(p1.name + " is the winner with a total of " + p1.getPointTotal()
            + " points! " + p2.getName() + " can go cry about it :).");
        winner = p1;
        return winner;
      }

      System.out.println("----------------------------");

      clientTurn(p2, s, gameDeck, gameDie);
      if (p2.getPointTotal() >= playTo) {
        System.out.println(p2.name + " is the winner with a total of " + p2.getPointTotal()
            + " points!" + p1.getName() + " is a n00b!");
        winner = p2;
        return winner;
      }
    }
    return winner;
  }

  public Client singlePlayerGame(Scanner s) throws InterruptedException {

    Client winner = null;
    Deck gameDeck = new Deck();
    gameDeck.shuffle();

    // A selection of comical names for the AI
    String[] goofyNames = {"Robo Pig Farmer", "Destructonator", "00010111011100101",
        "Your Mom's Toaster", "The Ogre", "Pork Bot 900", "Farmer Joseph", "Xx_NotARealPerson_xX",
        "Squibble", "German Technology"}; // 10 (0-9)

    System.out.println("How many sides should the game dice have?");
    while (!s.hasNextInt()) {
      System.out.println("Please enter valid number of sides (ex. '6')");
      s.nextLine();
    }
    Die gameDie = new Die(s.nextInt());
    s.nextLine();

    System.out.println("How many points will you play to?");
    while (!s.hasNextInt()) {
      System.out.println("Please enter valid number (ex. '100')");
      s.nextLine();
    }
    int playTo = s.nextInt();
    s.nextLine();

    System.out.print("Please enter Player 1 name: ");
    Client p1 = new Client(s.nextLine());
    System.out.println(p1.getName() + " entered.\n");

    int nameSelector = (int) (Math.random() * 9);

    Client p2 = new Client(goofyNames[nameSelector]);
    System.out.println(p2.getName() + " has entered the arena! (Computer opponent generated)");

    int rounds = 0;

    while (winner == null) {
      rounds++;
      System.out.println("----------------------------");
      System.out.println("          Round " + rounds);
      System.out.println("----------------------------");
      System.out.println(p1.getName() + "'s Point Total: " + p1.getPointTotal());
      System.out.println(p2.getName() + "'s Point Total: " + p2.getPointTotal());
      System.out.println("----------------------------");


      clientTurn(p1, s, gameDeck, gameDie);


      if (p1.getPointTotal() >= playTo) {
        System.out
            .println(p1.getName() + " has won the game! Congrats! You've beaten the system :).");
        winner = p1;
        return winner;
      }

      System.out.println("----------------------------");

      computerTurn(p2, s, gameDeck, gameDie, playTo);
      if (p2.getPointTotal() >= playTo) {
        System.out.println(p1.getName() + " got beaten by a computer, how unfortunate! " + p2.name
            + " is declared victorious!");
        winner = p2;
        return winner;
      }
    }
    return winner;
  }

  public static void main(String[] args) throws InterruptedException {

    Scanner gameScan = new Scanner(System.in);

    Gameplay newGame = new Gameplay(gameScan);
  }
}
