package com.devparadox.entities;

import java.awt.image.BufferedImage;

public class LifePack extends Entity
{

	public LifePack(double x, double y, int width, int height, BufferedImage sprite) 
	{
		super(x, y, width, height, sprite);
		
		SetFullMask(2, 6, 11, 10);
	}

}
