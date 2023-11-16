import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.*;

/**
 * The suit (shape) of a Card
 */
enum Suit {
    Club(1), Diamond(2), Heart(3), Spade(4), None(0);
    private final Integer suit;
    private Suit(int suit) {this.suit = suit;}
    /**
     * Returns the integer attached to the variant of the enum
     * @return The integer attached to the variant of the enum 
     */
    public Integer toInt() {return this.suit;}
}

/**
 * The pip (number) of a Card
 */
enum Pip {
    A(1), Two(2), Three(3), Four(4), Five(5), Six(6), Seven(7), Eight(8), Nine(9), Ten(10), J(11), Q(12), K(13), None(0);
    private final Integer pip;
    private Pip(int pip) {this.pip = pip;}
    /**
     * Returns the integer attached to the variant of the enum
     * @return The integer attached to the variant of the enum 
     */
    public Integer toInt() {return this.pip;}
}

/**
 * A class representing a card that can be drawn from a standard deck of cards, with a suit (shape) and pip (number)
 */
class Card {
	/**
	 * The suit (shape) of the card
	 */
    Suit suit;
    /**
     * The pip (number) of the card
     */
    Pip pip;

    Card(Suit suit, Pip pip) {
        this.suit = suit;
        this.pip = pip;
    }
    
    /**
     * Returns the ImageIcon asset corresponding to the suit (shape) and pip (number) of the class
     * @return The ImageIcon asset corresponding to the suit (shape) and pip (number) of the class
     */
    ImageIcon imageIcon() {
    	if (this.suit==Suit.None || this.pip==Pip.None) {
    		return new ImageIcon("data/card_back.gif");
    	}
        String filename = "data/card_" + this.suit.toInt().toString() + this.pip.toInt().toString() + ".gif";
        return new ImageIcon(filename);
    }
    
    /**
     * Determines if a given card is special, i.e. has the pip (number) of J, Q or K
     * @param card
     * @return Whether the card is a special card (J, Q or K)
     */
    static boolean isSpecial(Card card) {
        if (card.pip==Pip.J||card.pip==Pip.Q||card.pip==Pip.K) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Determines if a given card is NOT special, i.e. does NOT have the pip (number) of J, Q or K
     * @param card
     * @return Whether the card is NOT a special card (J, Q or K)
     */
    static boolean isNotSpecial(Card card) {return !isSpecial(card);}
}

/**
 * A class to store information of a bet session and methods to used in the game
 */
class Session {
    private int betAmount = 0;
    private ArrayList<Card> deck = new ArrayList<>(52);
    private ArrayList<Card> cardsPlayer = new ArrayList<>(3);
    private ArrayList<Card> cardsDealer = new ArrayList<>(3);
    private int cardsDrawnPlayer = 0;
    
    /**
     * Constructor of the Session class. Generates a deck of cards, shuffles the deck 
     * and sets the initial cards of the player and dealer to be empty cards
     * @param betAmount The amount of money the player has entered in the bet amount box
     */
    Session(int betAmount) {
        this.betAmount = betAmount;
        for (Suit suit: Suit.values()) {
            for (Pip pip: Pip.values()) {
            	if (suit!=Suit.None & pip!=Pip.None) {
            		this.deck.add(new Card(suit, pip));
            	}
            }
        }
        Collections.shuffle(this.deck);
        for (int i=0; i<3; i++) {
        	this.cardsPlayer.add(new Card(Suit.None, Pip.None));
        	this.cardsDealer.add(new Card(Suit.None, Pip.None));
        }
        this.renderDealerCards();
        this.renderPlayerCards();
    }
    
    private void renderDealerCards() {
    	for (int i=1; i<=3; i++) {
    		Components.<JLabel>get("1-"+i).setIcon(this.cardsDealer.get(i-1).imageIcon());
    	}
    }
    
    private void renderPlayerCards() {
    	for (int i=1; i<=3; i++) {
    		Components.<JLabel>get("2-"+i).setIcon(this.cardsPlayer.get(i-1).imageIcon());
    	}
    }
    
    /**
     * Returns the amount of money at stake in this betting session
     * @return The amount of money at stake in this betting session
     */
    public int betAmount() {
        return this.betAmount;
    }
    
    /**
     * Draws the three cards of the dealer, stores them, but does not show them on screen
     */
    public void drawDealerCards() {
        for (int i=0; i<3; i++) {
            if (this.deck.size()>0) {
            	Card drawn = this.deck.remove(0);
            	this.cardsDealer.set(i, drawn);
            }
        }
    }
    
    /**
     * Replaces a given player card and shows the new one on screen
     * @param i The card to be replaced -- 1, 2 or 3
     */
    public void replacePlayerCard(int i) {
        if (this.cardsDrawnPlayer < 5 && this.deck.size()>0) {
            Card drawn = this.deck.remove(0);
            if (drawn != null) {
                this.cardsDrawnPlayer += 1;
                System.out.println(this.cardsDrawnPlayer);
                Components.<JButton>get("ReplaceCard"+i).setEnabled(false);
                if (this.cardsDrawnPlayer >= 5) {
                    for (int j=1; j<=3; j++) {
                        Components.<JButton>get("ReplaceCard"+j).setEnabled(false);
                    }
                }
                System.out.println("old "+this.cardsPlayer.get(i-1).pip.toInt());
            	this.cardsPlayer.set(i-1, drawn);
            	System.out.println("new "+this.cardsPlayer.get(i-1).pip.toInt());
            	this.renderPlayerCards();
            	System.out.println("rendered player cards");
            }
        }
    }
    
    /**
     * Shows the dealer's cards on screen, the determine if the player has won
     * using the following rules: 1) the one with more special cards wins, if 
     * the number is the same; 2) add up the values of non-special cards, divide by 
     * 10, the one with higher remainder wins, if these are the same; 3) the dealer wins
     * @return Whether the player has won in this betting session
     */
    public boolean didPlayerWin() {
    	this.renderDealerCards();
    	System.out.println(this.cardsPlayer.size());
        int specialCardsPlayer = (int)this.cardsPlayer.stream()
                                                        .filter(Card::isSpecial)
                                                        .count();
        int specialCardsDealer = (int)this.cardsDealer.stream()
                                                        .filter(Card::isSpecial)
                                                        .count();
        System.out.println("specialCardsPlayer " + specialCardsPlayer);
        System.out.println("specialCardsDealer " + specialCardsDealer);
        if (specialCardsPlayer != specialCardsDealer) {
            return specialCardsPlayer > specialCardsDealer;
        } else {
            int remainderPlayer = this.cardsPlayer.stream()
                                                    .filter(Card::isNotSpecial)
                                                    .map(card -> card.pip.toInt())
                                                    .reduce(0, Integer::sum) % 10;
            System.out.println("remainderPlayer" + remainderPlayer);
            int remainderDealer = this.cardsDealer.stream()
                                                    .filter(Card::isNotSpecial)
                                                    .map(card -> card.pip.toInt())
                                                    .reduce(0, Integer::sum) % 10;
            System.out.println("remainderDealer" + remainderDealer);
            if (remainderPlayer != remainderDealer) {
                return remainderPlayer > remainderDealer;
            } else {
                for (int j=1; j<=3; j++) {
                    Components.<JButton>get("ReplaceCard"+j).setEnabled(false);
                }
                Components.<JButton>get("Result").setEnabled(false);
                return false;
            }
        }
    }
}


/**
 * A class to store information of the main app, UI code and button actions
 */
public class App implements ActionListener {

    private int moneyLeft = 100;

    private Session session;
    
    /**
     * Constructs the main UI of the app using a custom, declarative abstraction layer over Java Swing
     * @return A JFrame containing all Components in the UI, with all initial states and properties set
     * @see FrameBuilder
     * @see BoxBuilder
     * @see MenuBuilder
     * @see ButtonBuilder
     * @see TextFieldBuilder
     */
    JFrame view() {
        return
        new FrameBuilder()
            .name("Testy test")
            .minSize(450, 450)
            .menuBar(new MenuBuilder()
                        .label("Control")
                        .add(new JMenuItem("Exit"))
                        .itemsActionListener(this)
                        .build())
            .add(new BoxBuilder("MainFrame")
                    .layoutAxis(BoxLayout.PAGE_AXIS)
                    .alignX(Component.CENTER_ALIGNMENT)
                    .alignY(Component.CENTER_ALIGNMENT)
                    .add(new BoxBuilder("Row1")
                            .layoutAxis(BoxLayout.LINE_AXIS)
                            .add("1-1", new JLabel(new ImageIcon("data/card_back.gif")))
                            .add("1-2", new JLabel(new ImageIcon("data/card_back.gif")))
                            .add("1-3", new JLabel(new ImageIcon("data/card_back.gif")))
                            .build())
                    .add(new BoxBuilder("Row2")
                            .layoutAxis(BoxLayout.LINE_AXIS)
                            .add("2-1", new JLabel(new ImageIcon("data/card_back.gif")))
                            .add("2-2", new JLabel(new ImageIcon("data/card_back.gif")))
                            .add("2-3", new JLabel(new ImageIcon("data/card_back.gif")))
                            .build())
                    .add(new BoxBuilder("Row3")
                            .layoutAxis(BoxLayout.LINE_AXIS)
                            .add(new ButtonBuilder("ReplaceCard1")
                                    .text("Replace Card 1")
                                    .enabled(false)
                                    .actionListener(this)
                                    .build())
                            .add(new ButtonBuilder("ReplaceCard2")
                                    .text("Replace Card 2")
                                    .enabled(false)
                                    .actionListener(this)
                                    .build())
                            .add(new ButtonBuilder("ReplaceCard3")
                                    .text("Replace Card 3")
                                    .enabled(false)
                                    .actionListener(this)
                                    .build())
                            .build())
                    .add(new BoxBuilder("Row4")
                            .layoutAxis(BoxLayout.LINE_AXIS)
                            .add(new JLabel("Bet: $"))
                            .add(new TextFieldBuilder("BetInputField")
                                    .preferredSize(90, 25)
                                    .build())
                            .add(new ButtonBuilder("StartButton")
                                    .text("Start")
                                    .actionListener(this)
                                    .build())
                            .add(new ButtonBuilder("ResultButton")
                                    .text("Result")
                                    .enabled(false)
                                    .actionListener(this)
                                    .build())
                            .build())
                    .add(new BoxBuilder("Row5")
                            .layoutAxis(BoxLayout.LINE_AXIS)
                            .add("CurrentBetLabel", new JLabel("Please place your bet!"))
                            .add("BalanceLabel", new JLabel(" Amount of money you have: $100"))
                            .build())
                    .build())
            .pack()
            .build();
    }
    
    /**
     * Set up responses to each button click event
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        System.out.println(event.getActionCommand());
        switch (event.getActionCommand()) {
            case "Start":
                int betAmount = 0;
                betAmount = Integer.parseInt(Components.<JTextField>get("BetInputField").getText());
                if (betAmount <= 0) {break;}
                this.session = new Session(betAmount);
                this.session.drawDealerCards();
                for (int i=1; i<=3; i++) {
                    this.session.replacePlayerCard(i);
                }
                Components.<JLabel>get("CurrentBetLabel").setText("Your current bet is: $"+betAmount);
                Components.<JButton>get("StartButton").setEnabled(false);
                Components.<JButton>get("ResultButton").setEnabled(true);
                for (int j=1; j<=3; j++) {
                    Components.<JButton>get("ReplaceCard"+j).setEnabled(true);
                }
                Components.<JTextField>get("BetInputField").setText("");
                Components.<JTextField>get("BetInputField").setEnabled(false);;
                break;

            case "Replace Card 1":
                this.session.replacePlayerCard(1);
                break;
            
            case "Replace Card 2":
                this.session.replacePlayerCard(2);
                break;

            case "Replace Card 3":
                this.session.replacePlayerCard(3);
                break;

            case "Result":
                if (this.session.didPlayerWin()) {
                    this.moneyLeft += this.session.betAmount();
                    Components.<JLabel>get("BalanceLabel").setText(" Amount of money you have: $"+this.moneyLeft);
                    Components.<JButton>get("StartButton").setEnabled(true);
                    Components.<JTextField>get("BetInputField").setEnabled(true);
                    Components.<JButton>get("ResultButton").setEnabled(false);
                    Components.<JLabel>get("CurrentBetLabel").setText("Please place your bet!");
                    for (int i=1; i<=3; i++) {
                        Components.<JButton>get("ReplaceCard"+i).setEnabled(false);
                    }
                    JOptionPane.showMessageDialog(null, "Congratulations! You win this round!", "Round over", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    this.moneyLeft -= this.session.betAmount();
                    if (this.moneyLeft > 0) {
                        Components.<JLabel>get("BalanceLabel").setText(" Amount of money you have: $"+this.moneyLeft);
                        Components.<JButton>get("StartButton").setEnabled(true);
                        Components.<JTextField>get("BetInputField").setEnabled(true);
                        Components.<JButton>get("ResultButton").setEnabled(false);
                        Components.<JLabel>get("CurrentBetLabel").setText("Please place your bet!");
                        JOptionPane.showMessageDialog(null, "Sorry! The Dealer wins this round!", "Round over", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        Components.<JLabel>get("CurrentBetLabel").setText("");
                        Components.<JLabel>get("BalanceLabel").setText("You have no more money! Please start a new game!");
                        JOptionPane.showMessageDialog(null, "Game over! You have no more money! Please start a new game!", "Round over", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                for (int i=1; i<=3; i++) {
                    Components.<JLabel>get("1-"+i).setIcon(new ImageIcon("data/card_back.gif"));
                    Components.<JLabel>get("2-"+i).setIcon(new ImageIcon("data/card_back.gif"));
                }
                break;

            case "Exit":
                System.exit(0);
                break;
        
            default:
                break;
        }
    }
    
    /**
     * Constructor of the App class. Creates the UI and sets it to visible.
     */
    App() {
        view()
            .setVisible(true);
    }
    
    /**
     * Creates a new instance of the App
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        new App();
    }
}
