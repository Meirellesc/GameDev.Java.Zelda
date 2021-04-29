package com.devparadox.main;

import java.awt.Color;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.devparadox.entities.Enemy;
import com.devparadox.entities.Entity;
import com.devparadox.entities.Player;
import com.devparadox.graphics.Spritesheet;
import com.devparadox.world.World;

public class Game extends Canvas implements Runnable, KeyListener
{
	private static final long serialVersionUID = 1L;
	
	//Constants
	public final static int WIDTH = 240;
	public final static int HEIGHT = 160;
	private final static int SCALE = 3;
	
	//Thread - Game Loop
	private Thread thread;
	private boolean isRunning;
	
	//Sprite sheet
	public static Spritesheet spritesheet;
	
	//Background
	public static JFrame frame;
	private BufferedImage image;
	
	//World
	public static World world;
	
	//Entities
	public static List<Entity> entities;
	
	//Player
	public static Player player;
	
	//Enemy
	public static List<Enemy> enemies;
	
	//Random 
	public static Random random;
	
	/*
	 * Main
	 */
	public static void main(String[] args)
	{
		Game game = new Game();
		game.Start();
	}
	
	/*
	 * Game constructor
	 */
	public Game()
	{
		random = new Random();
		
		//Add Key Listener event for player's actions
		//'this' -> is the Responsible Listener Class (which in this case is the Game one)
		addKeyListener(this);
		
		//Set the resolution
		this.setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		
		//Initialize the frame (window of the game)
		InitFrame();
		
		//Initialize the image to be buffered into the game
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		//Initialize sprite sheet
		spritesheet = new Spritesheet("/spritesheet.png");
		
		//Create the player
		player = new Player(0,0,16,16,spritesheet.GetSprite(32, 0, 16, 16));
		

		//Initialize enemies (used for check the enemies collision between themselves)
		entities = new ArrayList<Entity>();
		
		//Initialize entities
		enemies = new ArrayList<Enemy>();
		 
		//Initialize the world
		world = new World("/map.png");		
		
		//Add into entities
		entities.add(player);
	}
	
	/*
	 * Initialize basic configuration of the game's window
	 */
	public void InitFrame()
	{
		//Instance a new Frame
		frame = new JFrame("Zelda");
		
		//Add the Canvas into Frame
		frame.add(this);
		
		//Packs the components within the window based on the component�s preferred sizes.
		frame.pack();
		
		//Set resize false => user cannot resize the window
		frame.setResizable(false);
		
		//Set the window at the center of the screen
		frame.setLocationRelativeTo(null);
		
		//When user click to "X" close the application
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Set the window visible
		frame.setVisible(true);
	}
	
	/*
	 * Start the game loop
	 */
	public synchronized void Start()
	{
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	/*
	 * Stop the game loop
	 */
	public synchronized void Stop()
	{
		try 
		{
			thread.join();
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
	
	
	/*
	 * Tick the actions
	 */
	public void Tick()
	{
		for(int i = 0; i < entities.size(); i++)
		{
			Entity e = entities.get(i);
			e.Tick();
		}
	}
	
	/*
	 * Render the graphic
	 */
	public void Render()
	{
		//Sequence of buffers into screen to optimize the rendering 
		BufferStrategy bs = this.getBufferStrategy();
		
		if(bs == null)
		{
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics graph = image.getGraphics();
		
		//Base layer
		graph.setColor(Color.BLACK);
		graph.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
		
		//Render World
		world.Render(graph);
		
		//Render Entities
		for(int i = 0; i < entities.size(); i++)
		{
			Entity e = entities.get(i);
			e.Render(graph);
		}
		
		graph.dispose();
		graph = bs.getDrawGraphics();
		graph.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		bs.show();
	}
	
	/*
	 * Game Loop
	 */
	public void run() 
	{
		//Set the focus to the game window 
		requestFocus();
		
		long lastTime = System.nanoTime();
		
		//Set to be 60 frames
		double amountOfTicks = 60.0;
		
		//1s in nanoSeconds divided per ticks
		//Used to calculate 60ticks per second = FPS
		double ns = 1000000000 / amountOfTicks;
		
		//To calculate the interval
		double delta = 0;
		
		int frames = 0;
		
		double timer = System.currentTimeMillis();
		
		while(isRunning)
		{
			//System.out.println("Running");
			
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			
			if (delta >= 1)
			{
				Tick();
				Render();
				
				frames++;
				delta--;
			}
			
			//Prove that is 60 frames per second
			if(System.currentTimeMillis() - timer >= 1000)
			{
				System.out.println("FPS : " + frames);
				frames = 0;
				timer = System.currentTimeMillis();
			}
		}
		
		Stop();
	}

	/*
	 * Mechanics
	 */
	@Override
	public void keyTyped(KeyEvent e) 
	{
		
		
	}

	@Override
	public void keyPressed(KeyEvent e) 
	{
		//Vertical actions
		if(e.getKeyCode() == KeyEvent.VK_UP ||
				e.getKeyCode() == KeyEvent.VK_W)
		{
			//Execute up action
			player.up = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN || 
					e.getKeyCode() == KeyEvent.VK_S)
		{
			//Execute down action
			player.down = true;
		}
				
		//Horizontal actions
		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode() == KeyEvent.VK_D)
		{
			//Execute right action
			player.right = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_LEFT || 
					e.getKeyCode() == KeyEvent.VK_A)
		{
			//Execute left action
			player.left = true;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) 
	{
		//Vertical actions
		if(e.getKeyCode() == KeyEvent.VK_UP ||
				e.getKeyCode() == KeyEvent.VK_W)
		{
			//Execute up action
			player.up = false;
		}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN || 
					e.getKeyCode() == KeyEvent.VK_S)
		{
			//Execute down action
			player.down = false;
		}
				
		//Horizontal actions
		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode() == KeyEvent.VK_D)
		{
			//Execute right action
			player.right = false;
		}
		else if(e.getKeyCode() == KeyEvent.VK_LEFT || 
					e.getKeyCode() == KeyEvent.VK_A)
		{
			//Execute left action
			player.left = false;
		}
				
	}

}

