package com.devparadox.entities;

import java.awt.image.BufferedImage;

public class Potion extends Entity 
{

	public Potion(double x, double y, int width, int height, BufferedImage sprite) 
	{
		super(x, y, width, height, sprite);
		
		SetFullMask(3, 6, 7, 10);
	}

}
