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
	private double speed = 0.7;
	
	//Collision masks
	private int xMask;
	private int yMask;
	private int wMask = World.TILE_SIZE - 1;
	private int hMask = World.TILE_SIZE - 1;
	
	public Enemy(double x, double y, int width, int height, BufferedImage sprite) 
	{
		super(x, y, width, height, sprite);
	}
	
	public void Tick()
	{
		//Random -> one way to randomize the enemy "AI" movements
		if(Game.random.nextInt(100) < 70)
		{
			if((int)this.x < Game.player.GetX() && 
					World.isFree((int)(x+speed), this.GetY()) &&
					!IsColliding((int)(x+speed), this.GetY()))
			{
				x += speed;
			}
			else if((int)this.x > Game.player.GetX() && 
					World.isFree((int)(x-speed), this.GetY()) &&
					!IsColliding((int)(x-speed), this.GetY()))
			{
				x -= speed;
			}
			
			if((int)this.y < Game.player.GetY() && 
					World.isFree(this.GetX(), (int)(y+speed)) &&
					!IsColliding(this.GetX(), (int)(y+speed)))
			{
				y += speed;
			}
			else if((int)this.y > Game.player.GetY() && 
					World.isFree(this.GetX(), (int)(y-speed)) &&
					!IsColliding(this.GetX(), (int)(y-speed)))
			{
				y -= speed;
			}
		}
	}
	
	/**
	 * Method to check the collision with all enemies in the game
	 * @param xNext
	 * @param yNext
	 * @return
	 */
	public boolean IsColliding(int xNext, int yNext)
	{
		Rectangle currentEnemy = new Rectangle(xNext + xMask, yNext + yMask, wMask, hMask);
		
		for(int i=0; i < Game.enemies.size(); i++)
		{
			Enemy enemy = Game.enemies.get(i);
			
			//Check if is not himself
			if(enemy == this)
			{
				continue;
			}
			
			//Gets the target enemy
			Rectangle targetEnemy = new Rectangle(enemy.GetX() + xMask, enemy.GetY() + yMask, wMask, hMask);
			
			//Do the collision comparison
			if(currentEnemy.intersects(targetEnemy)) 
			{
				return true;
			}					
		}
		
		return false;
	}
	
	public void Render(Graphics g)
	{
		super.Render(g);
		
		//Set the mask
		g.setColor(Color.LIGHT_GRAY);
		g.drawRect(this.GetX() + xMask - Camera.x, this.GetY() + yMask - Camera.y, wMask, hMask);
	}
}
