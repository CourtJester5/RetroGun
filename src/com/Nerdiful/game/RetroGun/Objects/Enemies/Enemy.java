package com.Nerdiful.game.RetroGun.Objects.Enemies;

import java.util.Random;

import com.Nerdiful.game.RetroGun.Objects.Projectiles.Projectile.Weapon;

import com.badlogic.gdx.math.Vector2;

public class Enemy
{
	Random random = new Random();
	
	protected double x, y;
	protected double speed;
	protected int health;
	protected double multiplier = 1.0;
	protected int w, h;
	protected int value;
	protected Weapon equipped;
	protected double cooldown;
	private double nextShot;
	private boolean marked = false;
	private Vector2 a, b, c, d;
	
	public enum Enemies {BASIC, WAVE, BLOCK};
	protected Enemies type;
	
	protected void init()
	{
		a = new Vector2((float) x + (w / 2), (float) y - (h / 2));
		b = new Vector2((float) x - (w / 2), (float) y - (h / 2));
		c = new Vector2((float) x - (w / 2), (float) y + (h / 2));
		d = new Vector2((float) x + (w / 2), (float) y + (h / 2));
		
		//nextShot = random.nextDouble() * cooldown;
		nextShot = 0;
	}
	
	public void update(float delta)
	{
		a = new Vector2((float) x + (w / 2), (float) y - (h / 2));
		b = new Vector2((float) x - (w / 2), (float) y - (h / 2));
		c = new Vector2((float) x - (w / 2), (float) y + (h / 2));
		d = new Vector2((float) x + (w / 2), (float) y + (h / 2));
		
		if(equipped != null)
		{
			if(nextShot > cooldown)
				nextShot = 0;
			if(nextShot != 0)
				nextShot += delta;
		}
	}
	
	public void setNextShot(double nextShot)
	{
		this.nextShot = nextShot;
	}
	
	public void takeDamage(int damage)
	{
		health -= damage;
	}
	
	public void mark()
	{
		marked = true;
	}
	
	public boolean isMarked()
	{
		return marked;
	}
	
	public Enemies getType()
	{
		return type;
	}
	
	public Weapon getWeapon()
	{
		return equipped;
	}
	
	public int getHealth()
	{
		return health;
	}
	
	public double getMultiplier()
	{
		return multiplier;
	}
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public Vector2[] getPoints()
	{
		return new Vector2[] {a, b, c, d};
	}
	
	public int getWidth()
	{
		return w;
	}
	
	public int getHeight()
	{
		return h;
	}
	
	public int getValue()
	{
		return value;
	}

	public double getNextShot()
	{
		return nextShot;
	}
	
	public double getCooldown()
	{
		return cooldown;
	}
}
