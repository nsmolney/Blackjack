package bjsim;

import java.util.*;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.net.URL;



/** Object for modeling a deck of cards
 * @author Nick
 *
 * 
 */
public class Deck{
	
	
	/** The root folder of all objects */
	private final static String ROOT_FOLDER = "http://www.eden.rutgers.edu/~nsmolney/blackjack/";
	
	/** The prefix for the relative path to the card image files */
	private final static String CARD_IMAGE_FILE_PREFIX = "cards/";

	/** The suffix for the relative path to the card image file */
	private final static String CARD_IMAGE_FILE_SUFFIX = ".gif";

	
	private Vector cards;
	//array of cards
	private  Card[] shuffled= new Card[416];
	Random rand = new Random();
	//top card in deck
	int topcard;
	
	/**
	 * constructor
	 * @param decks number of 52 card decks to include (not implemented)
	 */
	public Deck(int decks){
		topcard = 0;
		cards = new Vector();
	}
	
	/**
	 * randomly shuffle cards
	 */
	public void shuffle(){
		int random;
		for (int j = 0; j<416; j++){
			cards.add((shuffled[j]));
		}
		
		for (int i =0; i <416; i++){
			random = (int) (rand.nextDouble()*cards.size())/1;
			shuffled[i] = ((Card) cards.elementAt(random));
			cards.removeElementAt(random);	
		}
		topcard = 0;	
	}
	
	/**
	 *  build new deck of 8 standard decks
	 */
	public void buildNewDeck(){
		int spot =0;
		// eight decks
		for (int i =0; i<8;i++){
			// four suits
			spot = buildSuit("c",spot);
			spot = buildSuit("s",spot);
			spot = buildSuit("h",spot);
			spot = buildSuit("d",spot);	
		}	
	}
	
	/** build all cards in a given suit
	 * @param suit abbreviation for a given suit
	 * @param spot current spot in array we are filling
	 * @return spot we have filled upto
	 */
	public int buildSuit(String suit, int spot){
		try{
			shuffled[spot] = new Card("a",11,new JLabel(new ImageIcon(new URL(ROOT_FOLDER+CARD_IMAGE_FILE_PREFIX+"a"+suit+CARD_IMAGE_FILE_SUFFIX))));
			spot++;
			shuffled[spot] = new Card("k",10,new JLabel(new ImageIcon(new URL(ROOT_FOLDER+CARD_IMAGE_FILE_PREFIX+"k"+suit+CARD_IMAGE_FILE_SUFFIX))));
			spot++;
			shuffled[spot] = new Card("q",10,new JLabel(new ImageIcon(new URL(ROOT_FOLDER+CARD_IMAGE_FILE_PREFIX+"q"+suit+CARD_IMAGE_FILE_SUFFIX))));
			spot++;
			shuffled[spot] = new Card("j",10,new JLabel(new ImageIcon(new URL(ROOT_FOLDER+CARD_IMAGE_FILE_PREFIX+"j"+suit+CARD_IMAGE_FILE_SUFFIX))));
			spot++;
			shuffled[spot] = new Card("t",10,new JLabel(new ImageIcon(new URL(ROOT_FOLDER+CARD_IMAGE_FILE_PREFIX+"t"+suit+CARD_IMAGE_FILE_SUFFIX))));
			spot++;
			for (int k =2; k<10; k++){
				shuffled[spot] = new Card(Integer.toString(k),k,new JLabel(new ImageIcon(new URL(ROOT_FOLDER+CARD_IMAGE_FILE_PREFIX+Integer.toString(k)+suit+CARD_IMAGE_FILE_SUFFIX))));
				spot++;
			}
		}catch (Exception e){
		}
		return spot;
	}
	
	/**
	 * @return Card object at top of deck
	 */
	public Card getTopcard(){
		Card card =  shuffled[topcard];
		topcard++;
		return card;
	}

}