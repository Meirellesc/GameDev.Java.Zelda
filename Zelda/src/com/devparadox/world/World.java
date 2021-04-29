package com.devparadox.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.devparadox.entities.Enemy;
import com.devparadox.entities.Entity;
import com.devparadox.entities.LifePack;
import com.devparadox.entities.Potion;
import com.devparadox.main.Game;

public class World 
{
	private static Tile[] tiles;
	
	public static int WIDTH;
	public static int HEIGHT;
	public static final int TILE_SIZE = 16;
	
	public World(String path)
	{
		try 
		{
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			
			//Create the tiles array with the map size (in pixels)
			tiles = new Tile[map.getWidth() * map.getHeight()];
			
			//Create the pixels array with the map size (in pixels)
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			
			//Fill the pixels which contains the map
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			
			//Scan each map pixel, detect the color of it and create the respective entity
			for(int xx=0; xx < map.getWidth(); xx++)
			{
				for(int yy = 0; yy < map.getHeight(); yy++)
				{
					int actualPixel  = pixels[xx + (yy * map.getWidth())];
					
					//Floor (Black)
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, FloorTile.TILE_FLOOR);
					
					//Check the pixel color
					if(actualPixel == 0xFF000000)
					{
						//Floor (Black)
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, FloorTile.TILE_FLOOR);
					}
					else if(actualPixel == 0xFFF7F8FF)
					{
						//Wall (White)
						tiles[xx + (yy * WIDTH)] = new WallTile(xx * 16, yy * 16, WallTile.TILE_WALL);
					}
					else if(actualPixel == 0xFF0000FF)
					{
						//Player (Blue)
						Game.player.SetX(xx * 16);
						Game.player.SetY(yy * 16);
 					}
					else if(actualPixel == 0xFFF70000)
					{
						Enemy enemy = new Enemy(xx*16,yy*16,16,16,Entity.ENEMY_EN);
						//Enemy (Red)
						Game.entities.add(enemy);
						Game.enemies.add(enemy);
					}
					else if(actualPixel == 0xFFFFFF00)
					{
						//Potion (Yellow)
						Game.entities.add(new Potion(xx*16,yy*16,16,16,Entity.POTION_EN));
					}
					else if(actualPixel == 0xFF00FF00)
					{
						//Lifepack (Green)
						Game.entities.add(new LifePack(xx*16,yy*16,16,16,Entity.LIFEPACK_EN));
					}
				}
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public static boolean isFree(int xNext, int yNext)
	{
		int x1 = xNext / TILE_SIZE;
		int y1 = yNext / TILE_SIZE;
		
		int x2 = (xNext + TILE_SIZE - 1) / TILE_SIZE;
		int y2 = yNext / TILE_SIZE;
		
		int x3 = xNext / TILE_SIZE;
		int y3 = (yNext + TILE_SIZE - 1) / TILE_SIZE;
		
		int x4 = (xNext + TILE_SIZE - 1) / TILE_SIZE;
		int y4 = (yNext + TILE_SIZE - 1) / TILE_SIZE;
		
		if((tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile) ||
		   (tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile) ||
		   (tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile) ||
		   (tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile))
		{
			return false;
		}
		
		return true;
	}
	
	public void Render(Graphics g)
	{
		//Optimizing the map renderization
		
		//int xStart = Camera.x / 16;
		//int yStart = Camera.y / 16;
		
		//The same operation of above, but more perfomatic
		int xStart = Camera.x >> 4;
		int yStart = Camera.y >> 4;
		
		int xFinal = xStart + (Game.WIDTH >> 4);
		int yFinal = yStart + (Game.HEIGHT >> 4);
		
		//Update the tiles
		//Tiles -> all the static components which belongs to the environment (world)
		for(int xx = xStart; xx <= xFinal; xx++)
		{
			for(int yy = yStart; yy <= yFinal; yy++)
			{
				if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT)
				{
					continue;
				}
				
				Tile tile = tiles[xx + (yy*WIDTH)];
				tile.Render(g);
			}	
		}
	}
}
