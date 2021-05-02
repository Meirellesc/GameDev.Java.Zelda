package com.devparadox.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.devparadox.main.Game;
import com.devparadox.world.Camera;
import com.devparadox.world.World;

public class Entity 
{
	protected double x;
	protected double y;
	protected int width;
	protected int height;
	
	//Collision masks
	protected int xMask;
	protected int yMask;
	protected int wMask;
	protected int hMask;
	
	private BufferedImage sprite;
	
	public static BufferedImage LIFEPACK_EN = Game.spritesheet.GetSprite(48, 16, 16, 16);
	
	public static BufferedImage SKILL_EN = Game.spritesheet.GetSprite(64, 16, 16, 16);
	
	public static BufferedImage POTION_EN = Game.spritesheet.GetSprite(96, 16, 16, 16);
	
	public static BufferedImage ENEMY_EN = Game.spritesheet.GetSprite(112, 16, 16, 16);
			
	public Entity(double x, double y, int width, int height, BufferedImage sprite)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		
		this.xMask = 0;
		this.yMask = 0;
		this.wMask = width;
		this.hMask = height;
	}

	//Render sprite
	public void Render(Graphics g)
	{
		g.drawImage(sprite, this.GetX() - Camera.x, this.GetY() - Camera.y, null);
		
		//Set the mask to see the collision
		//g.setColor(Color.LIGHT_GRAY);
		//g.drawRect(this.GetX() + xMask - Camera.x, this.GetY() + yMask - Camera.y, wMask, hMask);
	}
	
	//Entity action
	public void Tick()
	{
		
	}
	
	public void SetMask(int xMask, int yMask)
	{
		this.xMask = xMask;
		this.yMask = yMask;
	}
	
	public void SetFullMask(int xMask, int yMask, int wMask, int hMask)
	{
		this.xMask = xMask;
		this.yMask = yMask;
		this.wMask = wMask;
		this.hMask = hMask;
	}
	
	public boolean IsColliding(Entity ent1, Entity ent2)
	{
		Rectangle ent1Mask = new Rectangle(ent1.GetX() + ent1.xMask, ent1.GetY() + ent1.yMask, ent1.wMask, ent1.hMask);
		Rectangle ent2Mask = new Rectangle(ent2.GetX() + ent2.xMask, ent2.GetY() + ent2.yMask, ent2.wMask, ent2.hMask);
		
		return ent1Mask.intersects(ent2Mask);		
	}
	
	//Getters and Setters
	public int GetX() {
		return (int)x;
	}

	public void SetX(int x) {
		this.x = x;
	}

	public int GetY() {
		return (int)y;
	}

	public void SetY(int y) {
		this.y = y;
	}

	public int GetWidth() {
		return width;
	}

	public void SetWidth(int width) {
		this.width = width;
	}

	public int GetHeight() {
		return height;
	}

	public void SetHeight(int height) {
		this.height = height;
	}
}
