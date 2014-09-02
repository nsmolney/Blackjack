package bjsim;

import javax.swing.JLabel;

/**Models a playing card
 * @author Nick
 *
 * 
 */
public class Card{
	
	String symbol;
	double value;
	JLabel label;
	
	/** constructor
	 * @param symbol A,K,...
	 * @param value numerical value
	 * @param label label (image) associated with card
	 */
	public Card(String symbol, double value, JLabel label){
		this.symbol= symbol;
		this.value = value;	
		this.label = label;
		label.setBounds(0,0,72,96);
		
	}
	
	
	
}
