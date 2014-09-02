package bjsim;

import java.util.*;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Container;
import java.awt.Font;
import java.awt.Color;


/** Models a Hand of playing cards
 * @author Nick
 *
 *
 */
public class Hand{
	
		
	int value;
	Vector Cards;
	int numAces = 0;
	int numAcesFlipped = 0;
	double bet;
	boolean blackjack;
	Player p;
	boolean insured;
	int xPos;
	int yPos;
	Container cont;
	JLabel currentValue = new JLabel("0");
	JLabel check;
	
	/** constructor for dealer's hand
	 * @param xPos x position of where hand is to be drawn
	 * @param yPos same as x position
	 * @param cont Container in whcih this will be drawn
	 */
	public Hand(int xPos, int yPos, Container cont){
		value = 0;
		Cards = new Vector();
		blackjack =false;
		this.xPos = xPos;
		this.yPos = yPos;
		this.cont = cont;
		currentValue.setBounds(0,0,40,40);
		Font font = new Font("Dialog.bold",Font.BOLD, 20);
		currentValue.setFont(font);
		currentValue.setForeground(Color.yellow);

	}
	
	/** constructor for player's hands
	 * @param bet amount associated with this hand
	 * @param p
	 * @param xPos
	 * @param yPos
	 * @param cont
	 */
	public Hand(double bet, Player p, int xPos, int yPos, Container cont){
		value = 0;
		Cards = new Vector();
		this.bet = bet;
		blackjack = false;
		this.p = p;
		this.xPos = xPos;
		this.yPos = yPos;
		this.cont = cont;
		currentValue.setBounds(0,0,40,40);
		Font font = new Font("Dialog.bold",Font.BOLD, 20);
		currentValue.setFont(font);
		currentValue.setForeground(Color.yellow);

	}
	
	/** add card to hand
	 * @param c
	 */
	public void addCard(Card c){
		Cards.add(c);
		value+=c.value;
		if (c.symbol=="a"){
			numAces++;
		}
		if (value>21 && numAces>0){
			value-=10;
			numAces--;
		}
		
	}

	/** play this hand
	 * @param Split pointer to Split button, if it needs to be displayed
	 * @param Double same as Split
	 */
	public void playHand(JButton Split, JButton Double){

		drawHand();
		// you have the choice to double, and the possibility to split
		if (((Card) Cards.elementAt(0)).value == ((Card) Cards.elementAt(1)).value){
			Split.setVisible(true);
			Double.setVisible(true);
		}else{
			Double.setVisible(true);
			//if new hand from split
			Split.setVisible(false);
		}
	}
	
	/** check to see if busted
	 * @return 
	 */
	public int checkHand(){
		if (value>21){
			return 1;
		}else return 0;
	}
	
	/** check if hand is a blackjack, set blackjack boolean to correspond
	 * @return
	 */
	public boolean hasBJ(){
		if(value==21){
			blackjack = true;
		}
		return blackjack;
	}
	
	
	/** play dealers hand to completion
	 * @param deck deck to add cards from
	 */
	public void dealerPlayHand(Deck deck){
		drawHand();
		while(value<17){
			addCard(deck.getTopcard());
			refreshHand();
		}	
	}
	
	/** hit hand
	 * @param deck deck to draw cards from
	 * @return whether hand has busted
	 */
	public int hit(Deck deck){
		addCard(deck.getTopcard());
		refreshHand();
		return checkHand();
	}
	
	
	/** double down on hand
	 * @param deck 
	 */
	public void doubledown(Deck deck){
		p.bankroll -=bet;
		bet*=2;
		addCard(deck.getTopcard());
		refreshHand();
	}
	
	/** Split hand
	 * @param deck
	 * @param Hands vector to keep track of all hands
	 * @param Split  pointer to button
	 * @param Double same as Split
	 * @param splits int to keep track of how many times we have split
	 */
	public void split(Deck deck, Vector Hands,JButton Split, JButton Double, int splits){
		if (bet<p.bankroll){
			p.bankroll -= bet;
			Hand hand2 = new Hand(bet, p,260-130*splits,yPos,cont);
			if (splits==0) xPos+=130;
			hand2.addCard((Card)Cards.elementAt(1));
			if (((Card)Cards.elementAt(1)).symbol=="a")value+=10;
			value-=((Card)Cards.elementAt(1)).value;
			Cards.removeElementAt(1);
			addCard(deck.getTopcard());
			hand2.addCard(deck.getTopcard());
			Hands.add(hand2);
			refreshHand();
			hand2.drawHand();
			playHand( Split, Double);
		}
	}
	
	/**
	 *  draw hand 
	 */
	public void drawHand(){
		int i = 2;
		int x = xPos;
		for (Enumeration e = Cards.elements() ; e.hasMoreElements() ;) {
			Card c = (Card) e.nextElement();
			cont.add(c.label,new Integer(i));
			c.label.setLocation(x,yPos);
			i++;
			x+=13;
		}
		cont.add(currentValue,new Integer(100));
		currentValue.setText(Integer.toString(value));
		currentValue.setLocation(xPos,yPos+100);
	}
	
	/**
	 *  draw first card in dealer's hand
	 */
	public void drawDealer(){
		Card c = (Card) Cards.firstElement();
		cont.add(c.label,new Integer(1));
		c.label.setLocation(xPos,yPos);
	}
	
	/**
	 * remove hand from container (no longer draw)
	 */
	public void removeHand(){
		for (Enumeration e = Cards.elements() ; e.hasMoreElements() ;) {
			Card c = (Card) e.nextElement();
			cont.remove(c.label);
		}
		cont.remove(currentValue);		
	}
	
	
	/**
	 *  redraw hand
	 */
	public void refreshHand(){
		removeHand();
		drawHand();
	}
	
	/** draw a check mark when hand is finished
	 * @param label check mark image
	 */
	public void drawCheck(JLabel label){
		check = label;
		cont.add(check,new Integer(5));
		check.setBounds(xPos-30,yPos+100,check.getIcon().getIconWidth(),check.getIcon().getIconHeight());
	}
	
	/**
	 * remove check mark from Container
	 */
	public void removeCheck(){
			cont.remove(check);
	}
	
	/**
	 * set whether player insured this hand
	 */
	public void setInsured(){
		insured = true;
		p.bankroll-=bet/2;
	}
	
	
	
}