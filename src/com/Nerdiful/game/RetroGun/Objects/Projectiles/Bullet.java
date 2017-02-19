package com.Nerdiful.game.RetroGun.Objects.Projectiles;

import com.Nerdiful.game.RetroGun.Helpers.AssetLoader;

public class Bullet extends Projectile
{
	private static double bulletSpeed = 16.0;
	public static double cooldown = .2875;
	private static int damage = 15;
	private static int w = 24;
	private static int h = 6;
	
	public Bullet(double projX, double projY, double targetX, double targetY)
	{
		super(projX, projY, targetX, targetY, w, h, bulletSpeed);
		setWeapon(Weapon.LASAR);
		setCooldown(cooldown);
		setDamage(damage);
		
		AssetLoader.shoot.play();
	}
	
	public Bullet(double multiplier, double projX, double projY, double targetX, double targetY)
	{
		super(projX, projY, targetX, targetY, w, h, bulletSpeed * multiplier);
		setWeapon(Weapon.LASAR);
		setCooldown(cooldown);
		setDamage((int) (damage * multiplier));
		
		AssetLoader.shoot.play();
	}
}
