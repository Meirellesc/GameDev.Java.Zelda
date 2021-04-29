package com.devparadox.world;

public class Camera 
{
	public static int x;
	public static int y;
	
	public static int Clamp(int actualPos, int minPos, int maxPos)
	{
		if(actualPos < minPos)
		{
			actualPos = minPos;
		}
		
		if(actualPos > maxPos)
		{
			actualPos = maxPos;
		}
		
		return actualPos;
	}
}
