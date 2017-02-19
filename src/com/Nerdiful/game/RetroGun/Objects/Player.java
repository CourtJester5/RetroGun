package com.Nerdiful.game.RetroGun.Objects;

import java.util.Iterator;

import com.Nerdiful.game.RetroGun.GameLoop.GameUpdate;
import com.Nerdiful.game.RetroGun.Helpers.AssetLoader;
import com.Nerdiful.game.RetroGun.Helpers.ProjectileManager;
import com.Nerdiful.game.RetroGun.Objects.Projectiles.Bullet;
import com.Nerdiful.game.RetroGun.Objects.Projectiles.Projectile;
import com.Nerdiful.game.RetroGun.Objects.Projectiles.Projectile.Weapon;
import com.badlogic.gdx.math.Vector2;

public class Player
{
	private static final int MAX_HEALTH = 150;
	private static final int MAX_SHIELD = 50;
	
	private double gameW, gameH;
	
	private int w = 40, h = 32;
	private Vector2 a, b, c, circumcenter;
	private double radius;

	private double cooldown, nextShot;
	private int speed = 20;
	private int health;
	
	private int numWeapons = 1, numDefenses = 1;
	
	public Weapon equipped;
	
	public Player(double gW, double gH)
	{
		this.gameW = gW; this.gameH = gH;
		
		a = new Vector2((float) gameW / 4, (float) gameH / 2);
		b = new Vector2(a.x - w, a.y - (h / 2));
		c = new Vector2(a.x - w, a.y + (h / 2));
		calcCircumcenter();
		
		setWeapon(Weapon.BULLET);
		nextShot = 0;
		health = MAX_HEALTH;
	}
	
	public void checkCollisions(ProjectileManager enemyFire)
	{
		Projectile proj;
		
		for(Iterator<Projectile> iterP = enemyFire.getList().iterator(); iterP.hasNext();)
		{
			proj = iterP.next();
			double projRadius = Math.sqrt(Math.pow(proj.getWidth() / 2, 2.0) + Math.pow(proj.getHeight() / 2, 2));

			if(Math.sqrt(Math.pow(proj.x - circumcenter.x, 2) + Math.pow(proj.y - circumcenter.y, 2)) <= radius + projRadius)
			{
				if(contains(new Vector2((float) proj.x, (float) proj.y)))
				{
					takeDamage(proj.getDamage());
					proj.mark();
					AssetLoader.damage.play();
				}
			}
		}
	}
	
	public boolean contains(Vector2 proj)
	{
		Vector2 BP = new Vector2(proj.x - b.x, proj.y - b.y);
		Vector2 BA = new Vector2(a.x - b.x, a.y - b.y);
		Vector2 BC = new Vector2(c.x - b.x, c.y - b.y);
		
		float dotBABA = BA.dot(BA);
		float dotBABC = BA.dot(BC);
		float dotBCBC = BC.dot(BC);
		float dotBABP = BA.dot(BP);
		float dotBCBP = BC.dot(BP);
		float invDenom = 1 / (dotBABA * dotBCBC - (float) Math.pow(dotBABC, 2));
		
		float u = ((dotBCBC * dotBABP) - (dotBABC * dotBCBP)) * invDenom;
		float v = ((dotBABA * dotBCBP) - (dotBABC * dotBABP)) * invDenom;
		
		if((u >= 0) && (v >= 0) && (u + v < 1))
			return true;
		return false;
	}
	
	public void takeDamage(int damage)
	{
		health -= damage;
	}
	
	private void calcCircumcenter()
	{
		circumcenter = new Vector2();
		Vector2 ABmid = new Vector2((a.x + b.x) / 2, (a.y + b.y) / 2);
		Vector2 BCmid = new Vector2((b.x + c.x) / 2, (b.y + c.y) / 2);
		double ABm = (b.y - a.y) / (b.x - a.x);
		
		circumcenter.x = (float) (ABmid.x - ABm * (BCmid.y - ABmid.y));
		circumcenter.y = BCmid.y;
		radius = a.x - circumcenter.x;
	}
	
	public void moveToY(double y)
	{		
		if(Math.abs(a.y - y) < speed)
			a.y = (float) y;
		else if(a.y < y)
			a.y += speed;
		else if(a.y > y)
			a.y -= speed;
		
		if(a.y < 0)
			a.y = 0;
		else if(a.y > gameH - GameUpdate.HUD_H)
			a.y = (float) (gameH - GameUpdate.HUD_H);
		
		b.x = a.x - w; b.y = (a.y - (h / 2));
		c.x = a.x - w; c.y = (a.y + (h / 2));
		calcCircumcenter();
	}
	
	public void setWeapon(Weapon toEquip)
	{
		equipped = toEquip;
		switch(toEquip)
		{
			case BULLET:
				cooldown = Bullet.cooldown;
				break;
			case LASAR:
				break;
			case MISSILE:
				break;
			default:
				break;
				
		}
	}
	
	public void reset()
	{
		a.y = (float) gameH / 2;
		b.x = a.x - w; b.y = (a.y - (h / 2));
		c.x = a.x - w; c.y = (a.y + (h / 2));
		calcCircumcenter();
		
		nextShot = 0;
		health = MAX_HEALTH;
	}
	
	public void setNextShot(double nextShot)
	{
		this.nextShot = nextShot;
	}
	
	public void addHealth(int healthPoints)
	{
		if(health < MAX_HEALTH)
		{
			health += healthPoints;
			if(health > MAX_HEALTH)
				health = MAX_HEALTH;
		}
		else
		{
			health += healthPoints;
			if(health > MAX_HEALTH + MAX_SHIELD)
				health = MAX_HEALTH + MAX_SHIELD;
		}
	}
	
	public int getMaxHealth()
	{
		return MAX_HEALTH;
	}
	
	public int getMaxShield()
	{
		return MAX_SHIELD;
	}
	
	public int getHealth()
	{
		return health;
	}
	
	public int getNumWeapons()
	{
		return numWeapons;
	}
	
	public int getNumDefenses()
	{
		return numDefenses;
	}
	
	public Weapon getWeapon()
	{
		return equipped;
	}
	
	public double getCooldown()
	{
		return cooldown;
	}
	
	public double nextShot()
	{
		return nextShot;
	}
	
	public Vector2[] getPoints()
	{
		return new Vector2[] {a, b, c};
	}
	
	public Vector2 getCenter()
	{
		return circumcenter;
	}
	
	public double getRadius()
	{
		return radius;
	}
	
	public int getWidth()
	{
		return w;
	}
	
	public int getHeight()
	{
		return h;
	}
}
