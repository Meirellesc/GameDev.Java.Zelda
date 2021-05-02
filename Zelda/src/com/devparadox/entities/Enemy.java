package com.devparadox.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.devparadox.main.Game;
import com.devparadox.world.Camera;
import com.devparadox.world.World;

public class Enemy extends Entity 
{
	private double speed = 0.4;
	
	//Enemy's frames
	private int frames = 0;
	private int maxFrames = 12;
	private int index = 0;
	private int maxIndex = 2;
	
	private BufferedImage[] sprites;
	
	public Enemy(double x, double y, int width, int height, BufferedImage sprite) 
	{
		super(x, y, width, height, null);
		sprites = new BufferedImage[3];
		sprites[0] = Game.spritesheet.GetSprite(112, 16, 16, 16);
		sprites[1] = Game.spritesheet.GetSprite(112+16, 16, 16, 16);
		sprites[2] = Game.spritesheet.GetSprite(112+32, 16, 16, 16);
		
		SetFullMask(0,0,15,15);
	} 
	
	public void Tick()
	{
		//Check if isn't colliding with player
		if(!IsCollidingWithPlayer())
		{
			//Random -> one way to randomize the enemy "AI" movements
			if(Game.random.nextInt(100) < 70)
			{
				if((int)this.x < Game.player.GetX() && 
						World.isFree((int)(x+speed), this.GetY()) &&
						!IsCollidingWithEnemy((int)(x+speed), this.GetY()))
				{
					x += speed;
				}
				else if((int)this.x > Game.player.GetX() && 
						World.isFree((int)(x-speed), this.GetY()) &&
						!IsCollidingWithEnemy((int)(x-speed), this.GetY()))
				{
					x -= speed;
				}
				
				if((int)this.y < Game.player.GetY() && 
						World.isFree(this.GetX(), (int)(y+speed)) &&
						!IsCollidingWithEnemy(this.GetX(), (int)(y+speed)))
				{
					y += speed;
				}
				else if((int)this.y > Game.player.GetY() && 
						World.isFree(this.GetX(), (int)(y-speed)) &&
						!IsCollidingWithEnemy(this.GetX(), (int)(y-speed)))
				{
					y -= speed;
				}
			}
		}
		else
		{
			if(Game.random.nextInt(100) < 10)
			{
				Game.player.life--;
				System.out.println("Player`s Life: " + Game.player.life);
			}
		}
		
		//Update frames
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
	
	/**
	 * Method to check the collision with all enemies in the game
	 * @param xNext
	 * @param yNext
	 * @return
	 */
	public boolean IsCollidingWithEnemy(int xNext, int yNext)
	{
		Rectangle currentEnemyMask = new Rectangle(xNext + xMask, yNext + yMask, wMask, hMask);
		
		for(int i=0; i < Game.enemies.size(); i++)
		{
			Enemy enemy = Game.enemies.get(i);
			
			//Check if is not himself
			if(enemy != this)
			{
				//Gets the target enemy
				Rectangle targetEnemyMask = new Rectangle(enemy.GetX() + xMask, enemy.GetY() + yMask, wMask, hMask);
				
				//Do the collision comparison
				if(currentEnemyMask.intersects(targetEnemyMask))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	public boolean IsCollidingWithPlayer()
	{
		return IsColliding(this, Game.player);
	}
	
	public void Render(Graphics g)
	{
		g.drawImage(sprites[index], this.GetX() - Camera.x, this.GetY() - Camera.y, null);
		
		//Set the mask to see the collision
		//g.setColor(Color.LIGHT_GRAY);
		//g.drawRect(this.GetX() + xMask - Camera.x, this.GetY() + yMask - Camera.y, wMask, hMask);
	}
}
