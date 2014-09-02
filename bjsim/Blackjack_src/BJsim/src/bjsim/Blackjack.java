package bjsim;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.awt.Color;
import java.awt.event.*;
import java.awt.Insets;
import javax.swing.*;
import java.util.*;


/**
 * Blackjack is the main blackjack program that can be run as an applet or as
 * an application.
 *
 * @author Nick Smolney
 */
public class Blackjack extends JApplet implements ActionListener{
	static JFrame frame;
	
	/** The root folder of all objects */
	private final static String ROOT_FOLDER = "http://www.eden.rutgers.edu/~nsmolney/blackjack/";

	/** The background image */
	private final static String BACKGROUND_IMAGE = "table.png";

	/** intialize bet */
	private int bet = 0;
	
	// all Icons, Buttons, Clips, Labels we will need 

	ImageIcon chipIcon1, chipIcon5, chipIcon25, chipIcon100, loseIcon, winIcon, pushIcon, bjIcon,bustIcon, checkIcon;
	JButton chip1, chip5, chip25, chip100, Deal, ClearBet, Hit, Stand, Double, Split, yesInsure, noInsure;
	AudioClip bj, bust, card, shuffle, lose, win, push, chip, insure;
	JLabel text, text2, insurance, gameover;
	JLayeredPane layeredPane;

	// create player, deck, and hands
	Player p = new Player(500);
	Deck deck = new Deck(0);
	Vector Hands = new Vector();
	Hand currentHand;
	Hand dealerHand;
	//number of hands player has 
	int handNumber =1;
	int splits = 0;
	// to have chips in betting circle 
	int numChips =0;
	Vector Chips = new Vector();
	// Vector for keeping track of win/lose logos 
	Vector textLabels = new Vector();



	/**
	 * Default public empty constructor.
	 */
	public Blackjack() {
		super();
	}


	/**
	 * Command line runnable method for running program is an application.
	 *
	 * @param args the String array of command line arguments
	 */
	public static void main(String[] args) {
		
		final Blackjack blackJack = new Blackjack();
		frame = new JFrame("Blackjack");

		blackJack.buildBoard(frame.getContentPane());
		frame.addWindowListener(new WindowAdapter() {
			public void windowIconified(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		}
		);
		try{
			ImageIcon image = new ImageIcon(new URL(ROOT_FOLDER+"Blackjack.GIF"));
			frame.setIconImage(image.getImage());
			
		}
		catch(Exception e){}
		frame.setSize(640, 480);
		frame.setVisible(true);
		
	}
	
	
	/** called by browser form of this applet */
	public void init() {
		buildBoard(getContentPane());
	}
	/** load all necessary files, and create all necessary buttons, labels, and clips 
	 *  @param c the Container in which the game will reside (instance of Blackjack)
	 * */
	public void buildBoard(Container c){
		try{
			
			//main drawing area
			layeredPane = new JLayeredPane();
			layeredPane.setPreferredSize(new Dimension(640, 480));
			c.add(layeredPane, BorderLayout.CENTER);
			//back of cards
			ImageIcon back = new ImageIcon(new URL("http://www.eden.rutgers.edu/~nsmolney/blackjack/cards/b.gif"));
			JLabel backlabel = new JLabel(back);
			layeredPane.add(backlabel, new Integer(2));
			backlabel.setBounds(550, 10, back.getIconWidth(), back.getIconHeight());
			//background image
			ImageIcon backIcon = new ImageIcon(new URL(ROOT_FOLDER+"table.PNG"));
			JLabel backLabel = new JLabel(backIcon);
			backLabel.setBounds(0, 0, backIcon.getIconWidth(), backIcon.getIconHeight());
			layeredPane.add(backLabel, new Integer(0));
			//audio
			bust = Applet.newAudioClip(new URL ("http://www.eden.rutgers.edu/~nsmolney/blackjack/bust.wav"));
			card = Applet.newAudioClip(new URL ("http://www.eden.rutgers.edu/~nsmolney/blackjack/card2.wav"));
			shuffle = Applet.newAudioClip(new URL ("http://www.eden.rutgers.edu/~nsmolney/blackjack/card_shuffle.wav"));
			lose = Applet.newAudioClip(new URL ("http://www.eden.rutgers.edu/~nsmolney/blackjack/lose.wav"));
			push = Applet.newAudioClip(new URL ("http://www.eden.rutgers.edu/~nsmolney/blackjack/push.wav"));
			win = Applet.newAudioClip(new URL ("http://www.eden.rutgers.edu/~nsmolney/blackjack/win.wav"));
			chip = Applet.newAudioClip(new URL ("http://www.eden.rutgers.edu/~nsmolney/blackjack/chip.wav"));
			bj = Applet.newAudioClip(new URL ("http://www.eden.rutgers.edu/~nsmolney/blackjack/blackjack.wav"));
			insure = Applet.newAudioClip(new URL ("http://www.eden.rutgers.edu/~nsmolney/blackjack/v_buyins.wav"));
			//chip buttons and icons
			chip1 = new JButton();
			chip5 = new JButton();
			chip25 = new JButton();
			chip100 = new JButton();
			chipIcon1 = new ImageIcon(new URL(ROOT_FOLDER+"chip1.gif"));
			chipIcon5 = new ImageIcon(new URL(ROOT_FOLDER+"chip5.gif"));
			chipIcon25 = new ImageIcon(new URL(ROOT_FOLDER+"chip25.gif"));
			chipIcon100 = new ImageIcon(new URL(ROOT_FOLDER+"chip100.gif"));
			setupChip(chip1,380,430,chipIcon1,"add 1");
			setupChip(chip5,420,430,chipIcon5,"add 5");
			setupChip(chip25,460,430,chipIcon25,"add 25");
			setupChip(chip100,500,430,chipIcon100,"add 100");
			// action icons
			loseIcon = new ImageIcon(new URL(ROOT_FOLDER+"losetext.gif"));
			winIcon = new ImageIcon(new URL(ROOT_FOLDER+"wintext.gif"));
			pushIcon = new ImageIcon(new URL(ROOT_FOLDER+"pushtext.gif"));
			bjIcon = new ImageIcon(new URL(ROOT_FOLDER+"bjtext.gif"));
			bustIcon = new ImageIcon(new URL(ROOT_FOLDER+"busttext.gif"));
			checkIcon = new ImageIcon(new URL(ROOT_FOLDER+"check.gif"));
			ImageIcon insureIcon = new ImageIcon(new URL(ROOT_FOLDER+"insuretext.gif"));
			//bet and bank text labels
			text = new JLabel("Bet: "+Integer.toString(bet));
			text.setBackground(Color.white);
			layeredPane.add(text, new Integer(1));
			text.setBounds(540, 420, 100,30);
			
			text2 = new JLabel("Bank: "+Integer.toString(p.bankroll));
			text2.setBackground(Color.white);
			layeredPane.add(text2, new Integer(1));
			text2.setBounds(540, 400, 100,30);
			// label for insurance
			insurance = new JLabel(insureIcon);
			layeredPane.add(insurance,new Integer(1));
			insurance.setBounds(540-insureIcon.getIconWidth(),360, insureIcon.getIconWidth(), insureIcon.getIconHeight());
			insurance.setVisible(false);
			//action buttons
			yesInsure = new JButton("Yes");
			noInsure = new JButton("No");
			Deal = new JButton("Deal");
			ClearBet = new JButton("Clear Bet");
			Hit = new JButton("Hit");
			Stand = new JButton("Stand");
			Double = new JButton("Double");
			Split = new JButton("Split");
			setupActionButton(yesInsure,540-insureIcon.getIconWidth(),360+insureIcon.getIconHeight(),"yesInsure");
			setupActionButton(noInsure,540-insureIcon.getIconWidth()+75,360+insureIcon.getIconHeight(),"noInsure");
			setupActionButton(Deal,540,380,"Deal");
			setupActionButton(ClearBet,540,360,"Clear");
			setupActionButton(Hit,540,380,"Hit");
			setupActionButton(Stand,540,360,"Stand");
			setupActionButton(Double,540,340,"Double");
			setupActionButton(Split,540,320,"Split");
			yesInsure.setVisible(false);
			noInsure.setVisible(false);
			ClearBet.setMargin(new Insets(0,0,0,0));
			Hit.setVisible(false);
			Stand.setVisible(false);
			Double.setVisible(false);
			Split.setVisible(false);
			// label for gameover logo
			ImageIcon gameoverIcon = new ImageIcon(new URL(ROOT_FOLDER+"gameover.gif"));
			gameover = new JLabel(gameoverIcon);
			gameover.setBounds(50,140,gameoverIcon.getIconWidth(), gameoverIcon.getIconHeight());
			layeredPane.add(gameover, new Integer(500));
			gameover.setVisible(false);
			
		}
		catch (Exception e){
		}
		deck.buildNewDeck();
		//mix cards up a bit
		for(int i =0; i<10;i++){
			deck.shuffle();
		}
		shuffle.play();
	}
	
	
	/**  Begin playing game */
	private void playgame(){
		Hand playerHand = new Hand(bet,p,260,300, layeredPane);
		dealerHand = new Hand(260,10,layeredPane);
		playerHand.addCard(deck.getTopcard());
		Card dealerTop = deck.getTopcard();
		dealerHand.addCard(dealerTop);
		playerHand.addCard(deck.getTopcard());				
		dealerHand.addCard(deck.getTopcard());
		dealerHand.drawDealer();
		Hands.add(playerHand);
		
		//if dealer showing an Ace, offer insurance
		currentHand = (Hand) Hands.firstElement();
		currentHand.drawHand();
		if(dealerTop.symbol=="a"){
			insure.play();
			insurance.setVisible(true);
			yesInsure.setVisible(true);
			noInsure.setVisible(true);
			Hit.setVisible(false);
			Stand.setVisible(false);
		}
		else{
			determinePlayerAction();
		}
	}
	
	/** complete dealers hand */
	public void dealerPlays(){
		//dealer plays
		dealerHand.dealerPlayHand(deck);
	}
	
	/** figure out if player has won, draw/play applicable message/sound */
	public void findWinner(){
		for (Enumeration e = Hands.elements() ; e.hasMoreElements() ;) {
			Hand h = (Hand) e.nextElement();
			
			//figure out if insurance paid
			if (dealerHand.blackjack == true && h.insured){
					h.p.bankroll +=h.bet;
			}
			

			if (h.blackjack == true){
				if (dealerHand.blackjack == true){
					h.p.bankroll +=h.bet;
					JLabel label = new JLabel(pushIcon);
					layeredPane.add(label,new Integer(200));
					textLabels.add(label);
					label.setBounds(h.xPos,h.yPos+40,pushIcon.getIconWidth(),pushIcon.getIconHeight());
					push.play();
				}
				else {
					h.p.bankroll +=2.5*h.bet;
					JLabel label = new JLabel(bjIcon);
					layeredPane.add(label,new Integer(200));
					textLabels.add(label);
					label.setBounds(h.xPos-30,h.yPos+40,bjIcon.getIconWidth(),bjIcon.getIconHeight());
					bj.play();	
				} 
			}
			else if (dealerHand.value==h.value&&h.value<=21){
				h.p.bankroll +=h.bet;
				JLabel label = new JLabel(pushIcon);
				layeredPane.add(label,new Integer(200));
				textLabels.add(label);
				label.setBounds(h.xPos,h.yPos+40,pushIcon.getIconWidth(),pushIcon.getIconHeight());
				push.play();
			}
			else if ((h.value>dealerHand.value || dealerHand.value>21)&& h.value<=21){
				h.p.bankroll+= 2*h.bet;
				JLabel label = new JLabel(winIcon);
				layeredPane.add(label,new Integer(200));
				textLabels.add(label);
				label.setBounds(h.xPos,h.yPos+40,winIcon.getIconWidth(),winIcon.getIconHeight());
				win.play();

			}
			else if(h.value>21){

			}
			
			else{
				JLabel loseLabel = new JLabel(loseIcon);
				layeredPane.add(loseLabel,new Integer(200));
				textLabels.add(loseLabel);
				loseLabel.setBounds(h.xPos,h.yPos+40,loseIcon.getIconWidth(),loseIcon.getIconHeight());
				lose.play();
			}
			
			if (p.bankroll==0){
				gameover.setVisible(true);
			}
			if (deck.topcard>312){
				deck.shuffle();
				shuffle.play();
			}
		}

	}
	
	
	public void actionPerformed(ActionEvent e) {
		if ("add 1".equals(e.getActionCommand())) {
			if(p.bankroll>=1){	
				bet++;
				p.bankroll--;
				updateMoney();
				addChip(chipIcon1);
			}
		} else if("add 5".equals(e.getActionCommand())){
			if(p.bankroll>=5){
				bet+=5;
				p.bankroll-=5;
				updateMoney();
				addChip(chipIcon5);
			}
		}else if("add 25".equals(e.getActionCommand())){
			if(p.bankroll>=25){
				bet+=25;
				p.bankroll-=25;
				updateMoney();
				addChip(chipIcon25);
			}

		}else if("add 100".equals(e.getActionCommand())){
			if(p.bankroll>=100){
				bet+=100;
				p.bankroll-=100;
				updateMoney();
				addChip(chipIcon100);
			}

		}else if("Deal".equals(e.getActionCommand())){
			if (bet>0){
				card.play();
				for (Enumeration e2 = Hands.elements() ; e2.hasMoreElements() ;) {
					Hand h = (Hand) e2.nextElement();
					h.removeHand();
					h.removeCheck();
				}
				for (Enumeration e2 = textLabels.elements() ; e2.hasMoreElements() ;) {
					JLabel label = (JLabel) e2.nextElement();
					layeredPane.remove(label);	
				}
				Hands.clear();
				handNumber=1;
				splits =0;
				if(dealerHand!=null){
					dealerHand.removeHand();
				}
				card.play();
				ClearBet.setVisible(false);
				Deal.setVisible(false);
				chip1.setVisible(false);
				chip5.setVisible(false);
				chip25.setVisible(false);
				chip100.setVisible(false);
				Hit.setVisible(true);
				Stand.setVisible(true);
				card.play();
				playgame();
				card.play();
			}
			
		}
		else if("Clear".equals(e.getActionCommand())){
			p.bankroll +=bet;
			bet=0;
			updateMoney();
			numChips=0;
			for (Enumeration e2 = Chips.elements() ; e2.hasMoreElements() ;) {
				JLabel label = (JLabel) e2.nextElement();
				layeredPane.remove(label);
				layeredPane.repaint();	
			}
		}
		else if("Hit".equals(e.getActionCommand())){
			card.play();
			if(currentHand.hit(deck)==1){
				JLabel label = new JLabel(bustIcon);
				layeredPane.add(label,new Integer(200));
				textLabels.add(label);
				label.setBounds(currentHand.xPos,currentHand.yPos+40,bustIcon.getIconWidth(),bustIcon.getIconHeight());
				bust.play();
				checkForMoreHands();
			}
			Double.setVisible(false);
			Split.setVisible(false);
		}
		
		else if("Double".equals(e.getActionCommand())){
			if (bet<p.bankroll){
				card.play();
				currentHand.doubledown(deck);
				updateMoney();
				checkForMoreHands();
			}
		}
		else if("Stand".equals(e.getActionCommand())){
			checkForMoreHands();
		}
		else if("Split".equals(e.getActionCommand())){
			if (splits<3){
				card.play();
				currentHand.split(deck, Hands,Split,Double,splits);
				card.play();
				splits++;
			}
			updateMoney();
		}
		else if("yesInsure".equals(e.getActionCommand())){
			if (bet/2<p.bankroll){
				currentHand.setInsured();
				updateMoney();
			}
			insurance.setVisible(false);
			yesInsure.setVisible(false);
			noInsure.setVisible(false);
			determinePlayerAction();
			
		}
		else if("noInsure".equals(e.getActionCommand())){
			insurance.setVisible(false);
			yesInsure.setVisible(false);
			noInsure.setVisible(false);
			determinePlayerAction();
		}
		
			
	}
	
	/**
	 * method for drawing chips in betting circle
	 * @param chipIcon image of the chip to be added
	 */
	private void addChip(Icon chipIcon){
		JLabel chipLabel = new JLabel(chipIcon);
		layeredPane.add(chipLabel, new Integer(numChips+5));
		chipLabel.setBounds(308,410-7*numChips,chipIcon.getIconWidth(), chipIcon.getIconHeight());
		Chips.add(chipLabel);
		numChips++;
		chip.play();
	}
	
	/**
	 * method for setting up chip buttons
	 * 
	 * @param chipButton
	 * @param x x position
	 * @param y y position
	 * @param chipIcon image of button
	 * @param action the associated action
	 */
	private void setupChip(JButton chipButton, int x, int y, Icon chipIcon, String action){
		chipButton.setBounds(x, y, chipIcon.getIconWidth(), chipIcon.getIconHeight());
		chipButton.setBorderPainted(false);
		chipButton.setOpaque( false );
		chipButton.setContentAreaFilled( false );
		layeredPane.add(chipButton, new Integer(1));
		chipButton.setIcon(chipIcon);
		chipButton.setActionCommand(action);
		chipButton.addActionListener(this);
	}
	
	/**
	 * method for setting up action buttons (i.e. Hit)
	 * 
	 * @param actionButton 
	 * @param x x position
	 * @param y y position
	 * @param action associated action
	 */
	private void setupActionButton(JButton actionButton, int x, int y, String action){
		actionButton.setBounds(x,y,75,20);
		layeredPane.add(actionButton, new Integer(2));
		actionButton.setActionCommand(action);
		actionButton.addActionListener(this);	
	}
	
	
	
	/**
	 * method called for updating bank/bet label
	 */
	private void updateMoney(){
		text2.setText("Bank: "+Integer.toString(p.bankroll));
		text.setText("Bet: "+Integer.toString(bet));
	}
	
	/**
	 *  called once hand is over
	 */
	private void completePlay(){
		dealerPlays();
		findWinner();
		bet=0;
		numChips=0;
		for (Enumeration e = Chips.elements() ; e.hasMoreElements() ;) {
			JLabel label = (JLabel) e.nextElement();
			layeredPane.remove(label);	
		}
		layeredPane.repaint();
		updateMoney();	
		ClearBet.setVisible(true);
		Deal.setVisible(true);
		chip1.setVisible(true);
		chip5.setVisible(true);
		chip25.setVisible(true);
		chip100.setVisible(true);
		Hit.setVisible(false);
		Stand.setVisible(false);
		Double.setVisible(false);
		Split.setVisible(false);
	}
	
	/**
	 * finish current hand, and move on to next, if any were generated by splitting
	 */
	private void checkForMoreHands(){
		currentHand.drawCheck(new JLabel(checkIcon));
		if(handNumber<Hands.size()){
			handNumber++;
			currentHand = (Hand) Hands.elementAt(handNumber-1);
			currentHand.playHand(Split,Double);
		}
		else completePlay();
	}
	
	/**
	 *  determine player's choices given the cards
	 */
	private void determinePlayerAction(){
		if(!dealerHand.hasBJ()){		
		//player plays
			Hit.setVisible(true);
			Stand.setVisible(true);
			if(currentHand.hasBJ()){
				checkForMoreHands();
			}
			else currentHand.playHand(Split,Double);
		}
		else{
			currentHand.hasBJ();
			checkForMoreHands();
		}
	}
}
