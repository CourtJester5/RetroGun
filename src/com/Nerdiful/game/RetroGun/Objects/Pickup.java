package com.Nerdiful.game.RetroGun.Objects;

import java.util.Random;

import com.Nerdiful.game.RetroGun.GameLoop.GameUpdate;

public class Pickup
{
	Random random = new Random();
	
	private static float MAX_DURATION = 15;
	public float x, y;
	public float r = 15;
	private float speedX = -2.5f;
	private float speedY = 2.5f;
	private int health = 30;
	private int value = 250;
	private int healthPoints = 0;
	private float duration = 0;
	
	public static enum Pickups {HEALTH};
	private Pickups pickup;
	
	public Pickup(Pickups pickup)
	{
		this.pickup = pickup;
		if(pickup == Pickups.HEALTH)
		{
			healthPoints = 100;
		}
		
		x = (float) ((random.nextFloat() * (GameUpdate.gameW / 2)) + (GameUpdate.gameW / 2)) - r;
		if(random.nextInt() == 0)
		{
			y = -r;
		}
		else
		{
			y = (float) GameUpdate.gameH + r - (float) GameUpdate.HUD_H;
			speedY *= -1;
		}
	}
	
	public void update(float delta)
	{
		x += speedX;
		y += speedY;
		
		duration += delta;
		
		if((x - r < 0 && speedX < 0) || (x + r > GameUpdate.gameW && speedX > 0))
			speedX *= -1;
		if((y - r < 0 && speedY < 0) || (y + r > GameUpdate.gameH - GameUpdate.HUD_H && speedY > 0))
			speedY *= -1;
	}
	
	public void takeDamage(int damage)
	{
		health -= damage;
	}
	
	public int getHealth()
	{
		return health;
	}
	
	public float getDuration()
	{
		return duration;
	}
	
	public int getValue()
	{
		return value;
	}
	
	public float maxDuration()
	{
		return MAX_DURATION;
	}
	
	public float getRadius()
	{
		return r;
	}
	
	public Pickups getType()
	{
		return pickup;
	}
	
	public int getHealthPoints()
	{
		return healthPoints;
	}
}
