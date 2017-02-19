package com.Nerdiful.game.RetroGun.Objects.Projectiles;

import com.Nerdiful.game.RetroGun.Objects.Player;

public class Projectile
{
	public double x, y, r;
	private double deltaX, deltaY, targetX, targetY;
	private double cooldown;
	private double speed;
	private int w, h;
	private int damage;

	public static enum Weapon {BULLET, LASAR, MISSILE};
	private Weapon type;
	private boolean marked = false;
	
	public Projectile(double x, double y, double targetX, double targetY, int w, int h, double speed)
	{
		this.x = x; this.y = y;
		this.w = w; this.h = h;
		this.targetX = targetX; this.targetY = targetY;
		this.speed = speed;
		calcTravel();
	}
	
	public void update()
	{
		x += deltaX;
		y += deltaY;
	}
	
	private void calcTravel()
	{
		deltaX = targetX - x; deltaY = targetY - y;
		r = Math.atan(deltaY / deltaX) * (180 / Math.PI);
		double dist = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
		deltaX = (speed * deltaX) / dist; deltaY = (speed * deltaY) / dist;
		
		//System.out.println(deltaX + ", " + deltaY + ", " + r);
	}
	
	public void mark()
	{
		marked = true;
	}
	
	protected void setWeapon(Weapon type)
	{
		this.type = type;
	}
	
	protected void setCooldown(double cooldown)
	{
		this.cooldown = cooldown;
	}
	
	protected void setDamage(int damage)
	{
		this.damage = damage;
	}
	
	public Weapon getWeapon()
	{
		return type;
	}
	
	public boolean isMarked()
	{
		return marked;
	}
	
	public int getWidth()
	{
		return w;
	}
	
	public int getHeight()
	{
		return h;
	}
	
	public double getRotation()
	{
		return r;
	}
	
	public int getDamage()
	{
		return damage;
	}
	
	public double getTargetX()
	{
		return targetX;
	}
	
	public double getTargetY()
	{
		return targetY;
	}
}
