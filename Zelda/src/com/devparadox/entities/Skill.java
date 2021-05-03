package com.devparadox.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.devparadox.main.Game;
import com.devparadox.world.Camera;

public class Skill extends Entity
{
	public int dx;
	public int dy;
	
	private double spd = 2.5;
	
	private final int MAX_LIFE = 50;
	private int currentLife = 0;
	
	public Skill(double x, double y, int width, int height, BufferedImage sprite, int dx, int dy) 
	{
		super(x, y, width, height, sprite);
		
		this.dx = dx;
		this.dy = dy;
		SetFullMask(3, 6, 7, 10);
	}
	
	public void Tick()
	{
		x += dx * spd;
		y += dy * spd;
		
		currentLife++;
		if(currentLife == MAX_LIFE)
		{
			Game.skills.remove(this);
			return;
		}
	}
	
	public void Render(Graphics g)
	{
		//g.setColor(Color.YELLOW);
		//g.fillOval(this.GetX() - Camera.x, this.GetY() - Camera.y, width, height);
		
		g.drawImage(Entity.SKILL_EN, this.GetX() - Camera.x, this.GetY() - Camera.y, null);
	}
}
