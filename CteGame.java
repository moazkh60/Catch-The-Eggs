import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

class MenuPanel extends JPanel{

	JButton play = new JButton("");
	JButton help = new JButton("");
	JButton exit = new JButton("");
	
	Image menubkg = new ImageIcon("images\\menubkg.png").getImage();  //menu background
	
	/* Setting icons on buttons */
	ImageIcon playbtn = new ImageIcon("buttons\\play.png"); 
	ImageIcon helpbtn = new ImageIcon("buttons\\help.png");
	ImageIcon exitbtn = new ImageIcon("buttons\\exit.png");

	JPanel center = new JPanel(); //adding another jpanel in a panel for boxLayout
	
	MenuPanel(){
		
		center.setLayout(new BoxLayout(center,BoxLayout.Y_AXIS)); //setting box layout 
		add(center); //adding the panel to anothe JPanel
		
		/* setting icons on buttons */
		play.setIcon(playbtn); 
		help.setIcon(helpbtn);
		exit.setIcon(exitbtn);
		
		/* adding the buttons in the panel */
		center.add(play);
		center.add(help);
		center.add(exit);
				
		/* adding mouseListeners on buttons */
		play.addMouseListener(new Click());
		help.addMouseListener(new Click());
		exit.addMouseListener(new Click());
		
	}//end constructor
	
	class Click extends MouseAdapter{ //internal friendly class
	
		public void mouseClicked(MouseEvent me){
			if(me.getSource()== play){
				CteGame.cl.show(CteGame.cards, "GamePanel"); //show gamePanel when play is clicked
			}
			if(me.getSource()== help){
				CteGame.cl.show(CteGame.cards, "HelpPanel"); //show helpPanel when help is clicked
			}	
			if(me.getSource()== exit){
				System.exit(0);  //exit application when exit is clicked
			}
		}//end mouseClick
	}//end mouseAdapter
	
	public void paintComponent(Graphics g){
		super.paintComponent(g); //calling the super class functions
		Graphics2D g2d = (Graphics2D)g; //casting to graphcis2D
		setFocusable(true);		 //setting the focus on the panel
		
		g2d.drawImage(menubkg, 0,0, null); //draw menu image
		repaint();
	}//end paintComponent
}//end GamePanel

///////////////////////////////// Help Panel \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
class HelpPanel extends JPanel{

	Image helpbkg = new ImageIcon("images\\helpbkg.png").getImage(); //help image background
	JButton back = new JButton("Back"); //back button
	
	HelpPanel(){
		setFocusable(true); //setting the focus
		add(back);			//adding back button in the panel
		
		back.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent me){
						CteGame.cl.show(CteGame.cards, "MenuPanel"); // show menuPanel when back button is clicked
			}	
		  });
	}//end constructor
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.drawImage(helpbkg, 0,0, null); // draw help background
		repaint();
	}//end paintComponent
}//end class


/////////////////////////////////// GAME PANEL \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
class GamePanel extends JPanel{ 
	
	Image gamebkg = new ImageIcon("images\\gamebkg.png").getImage();
	Image basket  = new ImageIcon("images\\basket.png").getImage();
	Image egg     = new ImageIcon("images\\egg.png").getImage();
	Image gameOverbkg= new ImageIcon("images\\gameOverbkg.png").getImage();
	Image tempbkg; //temporary background
	
	int x_basket,y_basket; //basket x and y  coordinates
	int x_egg,y_egg; // x and y coord of egg
	Random rand = new Random(); // for randomizing xcoord of eggs
	
	JLabel time;
	JLabel points;
	
	int pointsCount = 0;
	int timeleft = 59;
	int counter  = 0;
	
	boolean gameOver = false;
	
	GamePanel(){
		
		setLayout(null);
		setFocusable(true);
		tempbkg = gamebkg;
		
		x_basket = 450; y_basket = 600;
		x_egg = (int)rand.nextInt(1000);
		y_egg = 0;
		
	    time = new JLabel("Time: 59");
		time.setBounds(20, 10, 50, 20); //setting the time label on screen
	    
	    
	    points = new JLabel("Points: 0");
		points.setBounds(100,10,100,20);
		
		/*adding both components in jpanel*/
		add(time);
		add(points);
		
		addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent ke){
				
				if(ke.getKeyCode() == ke.VK_LEFT & x_basket>10){
					x_basket-=10;
					repaint(); // redraw at new position
				}
				if(ke.getKeyCode() == ke.VK_RIGHT & x_basket<1000){
					x_basket+=10; // redraw at new position
					repaint();
				}
			}//end keypressed
		});	
	}//end constructor
	
	void fallEgg(){
		if(y_egg >=650){
			y_egg = 0;
			x_egg = rand.nextInt(1000);
		}
		else
			y_egg++;
	}
	
	void updateTime(){
		counter++;
		if(counter == 100) //we count to 60 and then dec timeleft by 1 for slowing speed
		{
		   timeleft--;  //dec time left after 60 counts
		   counter = 0; //reset counter
		}
		time.setText("Time:"+timeleft);
	}
	
	void detectCollision(){
		Rectangle basketRect = new Rectangle(x_basket,y_basket,100,65); //making a rectangle on the basket
		Rectangle eggRect    = new Rectangle(x_egg,y_egg,45,67); //making a rectangle on egg
		
		if(eggRect.intersects(basketRect)){
			pointsCount+=5; // give 5 points on each catch
			points.setText("Points:"+pointsCount); // set the count
			y_egg = 0; // for next egg
			x_egg = rand.nextInt(1000); // again randomizing x axis of egg
		}
	}//end collision detection
	
	void checkGameOver(){
		if(timeleft <= 0)
		{
			JLabel yourScore = new JLabel("Your SCORE :" + pointsCount);
			tempbkg = gameOverbkg;
			yourScore.setBounds(400, 400, 200, 100);
			gameOver = true;
			yourScore.setForeground(Color.RED);
			add(yourScore);
		}
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.drawImage(tempbkg,0,0,null); //game background
		
		checkGameOver();
		
		if(gameOver == false){
			setFocusable(true);
			grabFocus();
			updateTime();
			
			fallEgg();
			detectCollision();
		
			g2d.drawImage(egg, x_egg, y_egg,null); //drawing egg at new position
			g2d.drawImage(basket, x_basket, y_basket, null); //drawing basket
		}
		
		repaint();	
	}//end paintComponent
}//end class

/////////////////////////// Catch the Eggs Game Panel \\\\\\\\\\\\\\\\\\\\\\\\\
public class CteGame extends JFrame{
	
	static MenuPanel mp = new MenuPanel();
	static HelpPanel hp = new HelpPanel();
	static GamePanel gp = new GamePanel();
	
	static CardLayout cl = new CardLayout();
	static JPanel cards = new JPanel(); // to contain the panels as cards
	
	CteGame(){
		
		cards.setLayout(cl);// setting the layout to cardlayout
		cards.add(mp, "MenuPanel");
		cards.add(hp, "HelpPanel");
		cards.add(gp, "GamePanel");
		cl.show(cards, "MenuPanel");
		add(cards); //adding the panel with cardlayout in JFrame
		
		setTitle("Catch The Eggs Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1024, 700); //frame size
		setVisible(true);   //frame visibility	
	}//end constructor
	
	public static void main(String args[]){
		new CteGame();//making an object
	}//end main
}//end class

