package com.devparadox.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.devparadox.main.Game;

public class UI 
{
	
	public void Render(Graphics g)
	{
		//Max life
		g.setColor(Color.RED);
		g.fillRect(8, 4, 50, 8);
		
		//Actual Life
		g.setColor(Color.GREEN);
		g.fillRect(8, 4, (int)((Game.player.life/Game.player.MAX_LIFE) * 50), 8);
		
		//Text life
		g.setFont(new Font("arial",Font.BOLD,8));
		g.setColor(Color.WHITE);
		g.drawString((int)Game.player.life+"/"+(int)Game.player.MAX_LIFE, 18, 11);
		
		//Max Mana
		g.setColor(Color.RED);
		g.fillRect(Game.WIDTH - 58, 4, 50, 8);
		
		//Actual Mana
		g.setColor(Color.BLUE);
		g.fillRect(Game.WIDTH - 58, 4, (int)((Game.player.mana/Game.player.MAX_Mana) * 50), 8);
		
		//Text life
		g.setFont(new Font("arial",Font.BOLD,8));
		g.setColor(Color.WHITE);
		g.drawString((int)Game.player.mana+"/"+(int)Game.player.MAX_Mana, Game.WIDTH - 48, 11);
	}
}
