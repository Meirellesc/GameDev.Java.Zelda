package com.devparadox.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.devparadox.graphics.Spritesheet;
import com.devparadox.main.Game;
import com.devparadox.world.Camera;
import com.devparadox.world.World;

public class Player extends Entity
{
	//Movement
	public boolean up;
	public boolean down;
	public boolean right;
	public boolean left;
	
	//Speed
	public double speed = 1;
	
	//Life
	public double life = 100;
	public static final double MAX_LIFE = 100; 
	
	//Mana
	public double mana = 100;
	public static final double MAX_MANA = 100; 
	
	//Damage
	public boolean isDamaged;
	public int damageFrames = 0;
	
	//Weapon
	private boolean hasWeapon;
	
	//Skill action
	public boolean isShooting;
	private boolean isLastMoveRight;
	private boolean isLastMoveLeft;
	private boolean isLastMoveDown;
	private boolean isLastMoveUp;
	
	//Player's frames
	private int frames = 0;
	private int maxFrames = 12;
	private int index = 0;
	private int maxIndex = 2;
	private boolean moved;
	
	//Sprites
	private BufferedImage[] downPlayer;
	private BufferedImage[] upPlayer;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	
	private BufferedImage lastPlayerMove;
	
	private BufferedImage playerDamage;
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) 
	{
		super(x, y, width, height, sprite);
		
		downPlayer = new BufferedImage[3];
		for(int i=0; i<3; i++)
		{
			downPlayer[i] = Game.spritesheet.GetSprite(32 + (i*16), 0, 16, 16);
		}		
		upPlayer = new BufferedImage[3];
		for(int i=0; i<2; i++)
		{
			upPlayer[i] = Game.spritesheet.GetSprite(80 + (i*16), 0, 16, 16);
		}
		upPlayer[2] = Game.spritesheet.GetSprite(80, 0, 16, 16);
		
		rightPlayer = new BufferedImage[3];
		for(int i=0; i<3; i++)
		{
			rightPlayer[i] = Game.spritesheet.GetSprite(112 + (i*16), 0, 16, 16);
		}	
		leftPlayer = new BufferedImage[3];
		for(int i=0; i<3; i++)
		{
			leftPlayer[i] = Game.spritesheet.GetSprite((i*16), 16, 16, 16);
		}
		
		lastPlayerMove = downPlayer[0];
		
		playerDamage = Game.spritesheet.GetSprite(0, 32, World.TILE_SIZE, World.TILE_SIZE); 
		
		SetFullMask(4, 4,width / 2, height / 2);		
	}
	
	/*
	 * Tick the actions
	 */
	public void Tick()
	{
		moved = false;
		
		if(up && 
			 World.isFree(this.GetX(), (int)(this.GetY() - speed)) &&
			 !IsCollidingWithEnemy(this.GetX(), (int)(this.GetY() - speed)))
		{
			
			moved = true;
			y -= speed;
		}
		else if(down && 
					World.isFree(this.GetX(), (int)(this.GetY() + speed)) &&
					!IsCollidingWithEnemy(this.GetX(), (int)(this.GetY() + speed)))
		{
			moved = true;
			y += speed;
		}
		
		if(right && 
				World.isFree((int)(this.GetX() + speed), this.GetY()) &&
				!IsCollidingWithEnemy((int)(this.GetX() + speed), this.GetY()))
		{
			moved = true;
			x += speed;
		}
		else if(left && 
					World.isFree((int)(this.GetX() - speed), this.GetY()) &&
					!IsCollidingWithEnemy((int)(this.GetX() - speed), this.GetY()))
		{
			moved = true;
			x -= speed;
		}
		
		if(moved)
		{
			frames++;
			
			if(frames == maxFrames)
			{
				frames = 0;
				index++;
				if(index > maxIndex)
				{
					index = 0;
				}
			}
		}
		
		IsCollidingWithWeapon();
		IsCollidingWithPotion();
		IsCollidingWithLifePack();
		
		//Damage system
		if(this.isDamaged)
		{
			this.damageFrames++;
			if(this.damageFrames == 5 )
			{
				this.isDamaged = false;
				this.damageFrames = 0;
			}
		}
		
		//Skill Shoot System
		SkillShoot();
		
		IsGameOver();
		
		Camera.x = Camera.Clamp(this.GetX() - (Game.WIDTH/2), 0, (World.WIDTH * 16) - Game.WIDTH);
		Camera.y = Camera.Clamp(this.GetY() - (Game.HEIGHT/2), 0, (World.HEIGHT * 16) - Game.HEIGHT);
	}
	
	public void SkillShoot()
	{
		if(this.isShooting)
		{
			this.isShooting = false;
			
			if(this.hasWeapon && this.mana > 0)
			{
				this.mana--;
				
				int dx = 0;
				int dy = 0;
				int px = 0;
				int py = 0;
				
				if(right)
				{	
					dx = 1;
				}
				else if(left)
				{
					dx = -1;
				}
				
				if(down)
				{
					dy = 1;
				}
				else if(up)
				{
					dy = -1;
				}
				
				if(dx == 0 && dy == 0)
				{
					if(isLastMoveRight)
					{
						dx = 1;
						dy = 0;
					}
					else if(isLastMoveLeft)
					{
						dx = -1;
						dy = 0;
					}
					else if(isLastMoveDown)
					{
						dx = 0;
						dy = 1;
					}
					else if(isLastMoveUp)
					{
						dx = 0;
						dy = -1;
					}
				}
				
				Skill skill = new Skill(this.GetX() + px, this.GetY() + py, 3, 3, null, dx, dy);
				Game.skills.add(skill);
			}
		}
	}
	
	public void IsGameOver()
	{
		if(life <= 0)
		{
			//Initialize sprite sheet
			Game.spritesheet = new Spritesheet("/spritesheet.png");
			
			//Create the player
			Game.player = new Player(0,0,16,16,Game.spritesheet.GetSprite(32, 0, 16, 16));

			//Initialize enemies (used for check the enemies collision between themselves)
			Game.entities.clear();
			Game.entities = new ArrayList<Entity>();
			
			//Initialize entities
			Game.enemies.clear();
			Game.enemies = new ArrayList<Enemy>();
			
			//Initialize potions
			Game.potions.clear();
			Game.potions = new ArrayList<Potion>();
			
			//Initialize life packs
			Game.lifePacks.clear();
			Game.lifePacks = new ArrayList<LifePack>();
					
			//Initialize the world
			Game.world = new World("/map.png");		
			
			//Add into entities
			Game.entities.add(Game.player);
		}
	}
	
	private boolean IsCollidingWithEnemy(int xNext, int yNext)
	{
		Rectangle playerMask = new Rectangle(xNext + xMask, yNext + yMask, wMask, hMask);
		
		for(int i=0; i < Game.enemies.size(); i++)
		{
			Enemy enemy = Game.enemies.get(i);
			
			//Gets the target enemy
			Rectangle enemyMask = new Rectangle(enemy.GetX() + xMask, enemy.GetY() + yMask, wMask, hMask);
			
			//Do the collision comparison
			if(playerMask.intersects(enemyMask))
			{
				return true;
			}
			
		}
		
		return false;
	}
		
	public void IsCollidingWithWeapon()
	{
		for(int i=0; i < Game.weapons.size(); i++)
		{
			Weapon actualWeapon = Game.weapons.get(i);
			
			if(IsColliding(this, actualWeapon))
			{
				hasWeapon = true;
				
				Game.weapons.remove(i);
				Game.entities.remove(actualWeapon);
			}
		}
	}
 	
	public void IsCollidingWithPotion()
	{
		if(this.mana < this.MAX_MANA)
		{
			for(int i=0; i < Game.potions.size(); i++)
			{
				Potion actualPotion = Game.potions.get(i);
				
				if(IsColliding(this, actualPotion))
				{
					this.mana += 10;
					
					if(mana > 100) mana = 100;
					
					Game.potions.remove(i);
					Game.entities.remove(actualPotion);
				}
			}
		}
	}

	public void IsCollidingWithLifePack()
	{
		if(this.life < this.MAX_LIFE)
		{
			for(int i=0; i < Game.lifePacks.size(); i++)
			{
				LifePack actualLifePack = Game.lifePacks.get(i);
				
				if(IsColliding(this, actualLifePack))
				{
					this.life += 10;
					
					if(life > 100) life = 100;
					
					Game.lifePacks.remove(i );
					Game.entities.remove(actualLifePack);
				}
			}
		}
	}

	public void Render(Graphics g)
	{	
		if(!isDamaged)
		{
			//Draw player
			if(up)
			{
				g.drawImage(upPlayer[index], this.GetX() - Camera.x, this.GetY() - Camera.y, null);
				lastPlayerMove = upPlayer[index];
				isLastMoveUp= true;
				isLastMoveRight = false;
				isLastMoveLeft = false;
				isLastMoveDown = false;
			}
			else if(down)
			{
				g.drawImage(downPlayer[index], this.GetX() - Camera.x, this.GetY() - Camera.y, null);
				lastPlayerMove = downPlayer[index];
				isLastMoveDown = true;
				isLastMoveRight = false;
				isLastMoveLeft = false;
				isLastMoveUp = false;
			}
			
			if(right)
			{
				g.drawImage(rightPlayer[index], this.GetX() - Camera.x, this.GetY() - Camera.y, null);
				lastPlayerMove = rightPlayer[index];
				isLastMoveRight = true;
				isLastMoveLeft = false;
				isLastMoveDown = false;
				isLastMoveUp = false;
			}
			else if(left)
			{
				g.drawImage(leftPlayer[index], this.GetX() - Camera.x, this.GetY() - Camera.y, null);
				lastPlayerMove = leftPlayer[index];
				isLastMoveLeft = true;
				isLastMoveRight = false;
				isLastMoveDown = false;
				isLastMoveUp = false;
			}
			else
			{
				g.drawImage(lastPlayerMove, this.GetX() - Camera.x, this.GetY() - Camera.y , null);
			}
			
			//Draw Weapon
			if(hasWeapon)
			{
				g.drawImage(Entity.WEAPON_EN, this.GetX() - Camera.x, this.GetY() - Camera.y - 4, null);
			}
		}
		else
		{
			g.drawImage(playerDamage, this.GetX() - Camera.x, this.GetY() - Camera.y, null);
		}
		//Set the mask to see the collision
		//g.setColor(Color.LIGHT_GRAY);
		//g.drawRect(this.GetX() + xMask - Camera.x, this.GetY() + yMask - Camera.y, wMask, hMask);
	}

}
