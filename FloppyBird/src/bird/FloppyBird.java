package bird;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class FloppyBird extends JPanel implements ActionListener,KeyListener{
	  int boardWidth = 460;
	  int boardHeight = 640;
	  
	  // images 
	  Image BackgroundImg;
	  Image BirdImg;
	  Image TopPipeImg;
	  Image BottomPipeImg;
	  // bird 
	  int birdX = boardWidth / 8;
	  int birdY = boardHeight / 2;
	  int BirdWidth = 34;
	  int BirdHeight = 24;
	  //create a new class according to Bird //
	  class Bird{
		  int x = birdX;
		  int y = birdY;
		  int width = BirdWidth;
		  int height = BirdHeight;
		  Image img; // ==> [1] this image which equal to 
		  
		  Bird(Image img){
			  this.img = img; // ^ this image
			  
		  }
	  }
	  
	  // create pipes 
	  int Pipex = boardWidth;
	  int PipeY = 0;
	  int PipeW = 64;   // scaled by 1/6
	  int PipeH = 512;
	  
	  class Pipe{
		  int x = Pipex;
		  int y = PipeY;
		  int width = PipeW;
		  int height = PipeH;
		  Image img;
		  boolean passed = false;
		  
		  
		  Pipe(Image img){
			  this.img = img;
		  }
	  }
	  
	  
	  // game logic 
	  Bird bobo;
	  int VelocityX = -4;
	  int VelocityY = 0;
	  int gravity = 1;
	  ArrayList<Pipe> pipes; 
	  Random rando = new Random();
			  // to implements something like that we need move method //
	  // before we make the bird move we need a game loop //
	  // so we need timer to this loop //
	  Timer gameLoop;
	  Timer placePipeTimer;
	  
	  boolean gameOver = false;
	  double score = 0;
	  
	  FloppyBird(){
		  setPreferredSize(new Dimension(boardWidth,boardHeight));
		  setFocusable(true);
		  addKeyListener(this);
		  //setBackground(Color.blue);
		  // so you need to load these imags to our constructor 
		  // cause you already create instance in main as Bird //
		  BackgroundImg = new ImageIcon(getClass().getResource("/flappybirdbg.png")).getImage();
		  BirdImg = new ImageIcon(getClass().getResource("/flappybird.png")).getImage();
		  TopPipeImg = new ImageIcon(getClass().getResource("/toppipe.png")).getImage();
		  BottomPipeImg = new ImageIcon(getClass().getResource("/bottompipe.png")).getImage();
		  // propreties to the bird images //
		  bobo = new Bird(BirdImg);
		  pipes = new ArrayList<Pipe>();
		  // add placePipeTimer 
		  placePipeTimer = new Timer(1500,new ActionListener() {
			  @Override
			  public void actionPerformed(ActionEvent e) {
				  placePipe();
			  }
		  });
		  placePipeTimer.start();
		  // game Timer 
		  gameLoop = new Timer(1000/60 ,this);
		  gameLoop.start();
	  }
	//always with the constructor must be this method == paintComponent(Graphics g) 
	 public void placePipe() {
		 int randomPipeY = (int)(PipeY - PipeH/4 - Math.random()*(PipeH/2) );
		 int openSpace = boardHeight/4;
		 Pipe topPipe = new Pipe(TopPipeImg);
		 topPipe.y = randomPipeY;
		 pipes.add(topPipe);
		 Pipe bottomPipe = new Pipe(BottomPipeImg);
		 bottomPipe.y = topPipe.y + PipeH + openSpace;
		 pipes.add(bottomPipe);
		 
		 
	 } 
	  
	  
	 
      public void paintComponent(Graphics g) {
    	  super.paintComponent(g);
    	  draw(g);  
      } 
      public void draw(Graphics g) {
    	  //draw the images you loaded as background 
    	  g.drawImage(BackgroundImg,0,0,boardWidth,boardHeight,null);
    	  // draw the bird //
    	  g.drawImage(bobo.img , bobo.x , bobo.y , bobo.width , bobo.height , null);
    	  // draw pipes 
    	  //System.out.println(pipes.size());
    	  for(int i = 0; i < pipes.size() ; i++) {
    		  Pipe pep = pipes.get(i);
    		  g.drawImage(pep.img , pep.x , pep.y , pep.width , pep.height , null);
    	  }
    	  //draw a score
    	  g.setColor(Color.DARK_GRAY);
    	  g.setFont(new Font("ubuntu" , Font.PLAIN , 32));
    	  if(gameOver) {
    		  g.drawString("Game Over : " + String.valueOf((int) score), 10 ,35);
    	  }
    	  else {
    		  g.drawString(String.valueOf((int)score) , 10 , 35);
    	  }
      }
      
      
    public void move() {
    	// consider a bird we will update our bird-x/y positions //
    	VelocityY += gravity;
    	bobo.y += VelocityY;
    	bobo.y = Math.max(bobo.y,0);
    	
    	
    	  for(int i = 0; i < pipes.size() ; i++) {
    		  Pipe pep = pipes.get(i);
    		  pep.x += VelocityX;
    		  
    		  
    		  if(!pep.passed && bobo.x > pep.x + pep.width) {
    			  pep.passed = true;
    			  score += 0.5;
    		  }
    		  
    		  
    		  if(collision(bobo,pep)) {
    	    	   gameOver = true;
    	    	}  
    		  
    	  }
    	
    	
    	
    	if(bobo.y > boardHeight) {
    		gameOver = true;
    	}  	  
    } 
    
    public boolean collision(Bird a ,Pipe b) {
    	return a.x < b.x + b.width &&
    			a.x + a.width > b.x &&
    			a.y < b.y + b.height &&
    			a.y + a.height > b.y;
    }
	@Override
	public void actionPerformed(ActionEvent e) {
	    move();
		repaint();
		if(gameOver) {
			placePipeTimer.stop();
			gameLoop.stop();
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			VelocityY = -9;
			if(gameOver) {
				bobo.y = birdY;
				VelocityY = 0;
				pipes.clear();
				score = 0;
				gameOver = false;
				gameLoop.start();
				placePipeTimer.start();
			}
		}
		
	}
	
	
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
