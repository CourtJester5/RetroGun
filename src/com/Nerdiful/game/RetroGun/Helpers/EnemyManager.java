package com.Nerdiful.game.RetroGun.Helpers;
 
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
 
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
 
import com.Nerdiful.game.RetroGun.Objects.Player;
import com.Nerdiful.game.RetroGun.Objects.Enemies.*;
import com.Nerdiful.game.RetroGun.Objects.Enemies.Enemy.Enemies;
import com.Nerdiful.game.RetroGun.Objects.Projectiles.Projectile;
 
public class EnemyManager
{
    private List<Enemy> enemies;
    private Random random = new Random();
    private DifficultyManager difficulty;
     
    private double spawnCooldown = 2.5;
    private double cooldown = 0;
     
    int cntr = 0;
     
    public EnemyManager(DifficultyManager difficulty)
    {
        this.difficulty = difficulty;
        enemies = new ArrayList<Enemy>();
    }
     
    public void update(float delta)
    {
        Enemy enemy = null;
        for(int i=0; i<enemies.size(); i++)
        {
            enemy = enemies.get(i);
            enemy.update(delta);
             
            if(outOfBounds(enemy))
                enemies.remove(i);
            if(enemy.getNextShot() == 0 && enemy.getX() > difficulty.getGameUpdate().getGameW() / 2)
            {
                if(enemy.getWeapon() != null)
                {
                    difficulty.addEnemyFire(difficulty.getGameUpdate().getPlayer(), enemy);
                    enemy.setNextShot(delta);
                }
            }
        }
         
        cooldown += delta;
        cntr++;
        //System.out.println(cooldown + ", " + cntr % 45);
        if(cooldown >= spawnCooldown && difficulty.getProgressPercent() < 1)
        {
            spawn();
            cooldown = random.nextDouble() * (spawnCooldown / 4);
        }
    }
     
    private void spawn()
    {
        switch(random.nextInt(3))
        {
            case(0):
            {
                enemies.add(new BasicEnemy());
                break;
            }
            case(1):
            {
                enemies.add(new WaveEnemy());
                break;
            }
            case(2):
            {
                enemies.add(new BlockEnemy());
                break;
            }
            default:
                break;
        }
    }
     
    public void checkCollisions(Player player, ProjectileManager playerFire)
    {
        Enemy enemy;
         
        for(Iterator<Enemy> iterE = enemies.iterator(); iterE.hasNext();)
        {
            enemy = iterE.next();
             
            checkFireCollision(playerFire, enemy);
            checkPlayerCollision(player, enemy);
             
            if(enemy.isMarked())
            {
                if(enemy.getType() == Enemies.BLOCK)
                {
                    BlockEnemy blockEnemy = (BlockEnemy) enemy;
                    BlockEnemy.positions[blockEnemy.getPos()] = false;
                }
                difficulty.getGameUpdate().addScore(enemy.getValue());
                iterE.remove();
                AssetLoader.explosion2.play();
            }
        }
    }
     
    private void checkFireCollision(ProjectileManager playerFire, Enemy enemy)
    {
        Rectangle enemyCollision = new Rectangle((int) enemy.getX() - (enemy.getWidth() / 2), (int) enemy.getY() - (enemy.getHeight() / 2), enemy.getWidth(), enemy.getHeight());
        Rectangle projCollision;
        Projectile proj;
         
        for(Iterator<Projectile> iterP = playerFire.getList().iterator(); iterP.hasNext();)
        {
            proj = iterP.next();
            projCollision = new Rectangle((float) proj.x - (proj.getWidth() / 2), (float) proj.y - (proj.getHeight() / 2),
                    (float) proj.getWidth(), (float) proj.getHeight());
            if(enemyCollision.overlaps(projCollision))
            {
                proj.mark();
                enemy.takeDamage(proj.getDamage());
                if(enemy.getHealth() <= 0)
                    enemy.mark();
                AssetLoader.damage2.play();
            }
        }
    }
     
    private void checkPlayerCollision(Player player, Enemy enemy)
    {
        Vector2[] playerCollision = player.getPoints();
        Vector2[] enemyCollision = enemy.getPoints();
        Rectangle enemyBox = new Rectangle((float) enemy.getX() - (enemy.getWidth() / 2), (float) enemy.getY() - (enemy.getHeight() / 2),
                (float) enemy.getWidth(), (float) enemy.getHeight());
        float enemyRadius = enemyCollision[0].dst(enemyCollision[2]) / 2;
         
        if(player.getCenter().dst((float) enemy.getX(), (float) enemy.getY()) < enemyRadius + player.getRadius())
        {
            for(int i=0; i<playerCollision.length; i++)
            {
                if(enemyBox.contains(playerCollision[i]))
                {
                    player.takeDamage(enemy.getHealth());
                    enemy.mark();
                    if(player.getHealth() > 0)
                        AssetLoader.collision.play();
                    break;
                }
            }
            for(int i=0; i<enemyCollision.length; i++)
            {               
                if(player.contains(enemyCollision[i]))
                {
                    player.takeDamage(enemy.getHealth());
                    enemy.mark();
                    if(player.getHealth() > 0)
                        AssetLoader.collision.play();
                    break;
                }
            }
        }
         
    }
     
    public void clear()
    {
        enemies.clear();
    }
     
    private boolean outOfBounds(Enemy enemy)
    {
        if(enemy.getX() + (enemy.getWidth() / 2) < 0)
            return true;
        else
            return false;
    }
     
    public int numEnemies()
    {
        return enemies.size();
    }
     
    public void draw(Batch sBatch)
    {
        if(enemies.size() > 0)
        {
            Enemy enemy;
            for(int i=0; i<enemies.size(); i++)
            {
                enemy = enemies.get(i);
                switch(enemy.getType())
                {
                    case BASIC:
                    {
                        sBatch.draw(AssetLoader.basicEnemy, (float) (enemy.getX() - (enemy.getWidth() / 2)), (float) (enemy.getY() - (enemy.getHeight() / 2)), enemy.getWidth(), enemy.getHeight());
                        break;
                    }
                    case WAVE:
                    {
                        sBatch.draw(AssetLoader.waveEnemy, (float) (enemy.getX() - (enemy.getWidth() / 2)), (float) (enemy.getY() - (enemy.getHeight() / 2)), enemy.getWidth(), enemy.getHeight());
                        break;
                    }
                    case BLOCK:
                    {
                        sBatch.draw(AssetLoader.blockEnemy, (float) (enemy.getX() - (enemy.getWidth() / 2)), (float) (enemy.getY() - (enemy.getHeight() / 2)), enemy.getWidth(), enemy.getHeight());
                        break;
                    }
                         
                    default:
                        break;
                }
            }
        }
    }
}
