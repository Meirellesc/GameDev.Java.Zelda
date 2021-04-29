package com.devparadox.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Tile 
{
	private BufferedImage sprite;
	
	private int x;
	private int y;
	
	public Tile(int x, int y, BufferedImage sprite)
	{
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	public void Render(Graphics g)
	{
		g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
	}
}
