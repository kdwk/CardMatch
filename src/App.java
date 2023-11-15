import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.*;

enum Suit {
    Club(1), Diamond(2), Heart(3), Spade(4);
    private final Integer suit;
    private Suit(int suit) {this.suit = suit;}
    public Integer toInt() {return this.suit;}
}

enum Pip {
    A(1), Two(2), Three(3), Four(4), Five(5), Six(6), Seven(7), Eight(8), Nine(9), Ten(10), J(11), Q(12), K(13);
    private final Integer pip;
    private Pip(int pip) {this.pip = pip;}
    public Integer toInt() {return this.pip;}
}

class Card {
    Suit suit;
    Pip pip;

    Card(Suit suit, Pip pip) {
        this.suit = suit;
        this.pip = pip;
    }

    ImageIcon imageIcon() {
        String filename = "data/card_" + this.suit.toInt() + this.pip.toInt() + ".gif";
        return new ImageIcon(filename);
    }

    static boolean isSpecial(Card card) {
        if (card.pip.equals(Pip.J)||card.pip.equals(Pip.Q)||card.pip.equals(Pip.K)) {
            return true;
        } else {
            return false;
        }
    }

    boolean isNotSpecial(Card card) {return !isSpecial(card);}
}

class Session {
    private int betAmount = 0;
    private ArrayList<Card> deck = new ArrayList<>(52);
    private ArrayList<Card> cardsPlayer = new ArrayList<>(3);
    private ArrayList<Card> cardsDealer = new ArrayList<>(3);
    private int cardsDrawnPlayer = 0;

    Session(int betAmount) {
        this.betAmount = betAmount;
        for (Suit suit: Suit.values()) {
            for (Pip pip: Pip.values()) {
                this.deck.add(new Card(suit, pip));
            }
        }
        Collections.shuffle(this.deck);
    }

    ArrayList<Card> deck() {return this.deck;}

    public int betAmount() {
        return this.betAmount;
    }

    public Card drawCardDealer() {
        if (this.deck.size()>0) {
            Card drawn = this.deck.remove(0);
            cardsDealer.add(drawn);
            return drawn;
        }
        return null;
    }

    public ArrayList<Card> cardsDealer() {
        return this.cardsDealer;
    }

    private Card drawCardPlayer() {
        if (this.cardsDrawnPlayer < 5 && this.deck.size()>0) {
            Card drawn = this.deck.remove(0);
            this.cardsDrawnPlayer += 1;
            System.out.println(this.cardsDrawnPlayer);
            if (this.cardsDrawnPlayer >= 5) {
                for (int i=1; i<=3; i++) {
                    Components.<JButton>get("ReplaceCard"+i).setEnabled(false);
                }
            }
            cardsPlayer.add(drawn);
            return drawn;
        }
        return null;
    }

    public Card replaceCardPlayer(int i) {
        Card drawn = this.drawCardPlayer();
        if (drawn != null) {
            this.cardsPlayer.remove(i-1);
            this.cardsPlayer.add(i-1, drawn);
            return drawn;
        }
        return null;
    }

    public boolean didPlayerWin() {
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
                                                    .filter(card -> Card.isSpecial(card))
                                                    .map(card -> card.pip.toInt())
                                                    .reduce(0, Integer::sum) % 10;
            System.out.println("remainderPlayer" + remainderPlayer);
            int remainderDealer = this.cardsDealer.stream()
                                                    .filter(card -> Card.isSpecial(card))
                                                    .map(card -> card.pip.toInt())
                                                    .reduce(0, Integer::sum) % 10;
            System.out.println("remainderDealer" + remainderDealer);
            if (remainderPlayer != remainderDealer) {
                return remainderPlayer > remainderDealer;
            } else {
                return false;
            }
        }
    }
}

public class App implements ActionListener {

    private int moneyLeft = 100;

    private Session session;

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

    @Override
    public void actionPerformed(ActionEvent event) {
        System.out.println(event.getActionCommand());
        switch (event.getActionCommand()) {
            case "Start":
                int betAmount = 0;
                betAmount = Integer.parseInt(Components.<JTextField>get("BetInputField").getText());
                if (betAmount <= 0) {break;}
                this.session = new Session(betAmount);
                for (int i=1; i<=3; i++) {
                    this.session.drawCardDealer();
                }
                for (int i=1; i<=3; i++) {
                    Components.<JLabel>get("2-"+i).setIcon(this.session.replaceCardPlayer(i).imageIcon());
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
                Components.<JLabel>get("2-1").setIcon(this.session.replaceCardPlayer(1).imageIcon());
                Components.<JButton>get("ReplaceCard1").setEnabled(false);
                break;
            
            case "Replace Card 2":
                Components.<JLabel>get("2-2").setIcon(this.session.replaceCardPlayer(2).imageIcon());
                Components.<JButton>get("ReplaceCard2").setEnabled(false);
                break;

            case "Replace Card 3":
                Components.<JLabel>get("2-3").setIcon(this.session.replaceCardPlayer(3).imageIcon());
                Components.<JButton>get("ReplaceCard3").setEnabled(false);
                break;

            case "Result":
                ArrayList<Card> cardsDealer = this.session.cardsDealer();
                for (int i=1; i<=3; i++) {
                    Components.<JLabel>get("1-"+i).setIcon(cardsDealer.get(i-1).imageIcon());
                }
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

    App() {
        view()
            .setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        new App();
    }
}
