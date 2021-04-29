package com.devparadox.world;

import java.awt.image.BufferedImage;

import com.devparadox.main.Game;

public class WallTile extends Tile
{
	public static BufferedImage TILE_WALL = Game.spritesheet.GetSprite(16, 0, 16, 16);
	
	public WallTile(int x, int y, BufferedImage sprite) 
	{
		super(x, y, sprite);
	}

}
