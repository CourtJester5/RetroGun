package com.Nerdiful.game.RetroGun.Objects.Enemies;
 
import com.Nerdiful.game.RetroGun.Objects.Projectiles.Projectile.Weapon;
import com.Nerdiful.game.RetroGun.GameLoop.GameUpdate;
import com.Nerdiful.game.RetroGun.Helpers.AssetLoader;
 
public class BlockEnemy extends Enemy
{
    private double blockMult = 1;
    private static int blockHealth = 120;
    private static double blockSpeed = 1.0;
    private static double blockCooldown = 0;
    private static int blockW = 10;
    private static int blockH = 55;
    private static int blockValue = 150;
    private static Weapon blockEquipped = null;
    public static boolean positions[] = new boolean[(int) (GameUpdate.gameW / 2) / (blockW + 1)];
     
    private int pos;
    private double targetX;
     
    public BlockEnemy()
    {
        health = blockHealth;
        speed = blockSpeed;
        cooldown = blockCooldown;
        equipped = blockEquipped;
        w = blockW; h = blockH;
        value = blockValue;
        type = Enemies.BLOCK;
        multiplier = blockMult;
         
        x = (int) GameUpdate.gameW + (w / 2);
        y = random.nextDouble() * (GameUpdate.gameH - GameUpdate.HUD_H - h) + (h / 2);
         
        pos = random.nextInt(positions.length);
        while(positions[pos] == true)
            pos = random.nextInt(positions.length);
        positions[pos] = true;
        targetX = (pos * (w + 1)) + (GameUpdate.gameW / 4) + w;
         
        init();
    }
     
    @Override
    public void update(float delta)
    {
         
        if(x > targetX)
        {
            if(x - speed < targetX)
                x = targetX;
            else
                x -= speed;
        }
        else
        {
            if(y >= GameUpdate.gameH - GameUpdate.HUD_H - (h / 2) || y <= 0 + (h / 2))
                speed *= -1;
            y += speed;
        }
         
        super.update(delta);
    }
    public int getPos()
    {
        return pos;
    }
}
