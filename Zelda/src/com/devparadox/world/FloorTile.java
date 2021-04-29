package com.devparadox.world;

import java.awt.image.BufferedImage;

import com.devparadox.main.Game;

public class FloorTile extends Tile
{
	public static BufferedImage TILE_FLOOR = Game.spritesheet.GetSprite(0, 0, 16, 16);
	
	public FloorTile(int x, int y, BufferedImage sprite) 
	{
		super(x, y, sprite);
	}

}
