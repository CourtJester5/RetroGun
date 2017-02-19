package com.Nerdiful.game.RetroGun.Helpers;
 
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
 
import com.Nerdiful.game.RetroGun.Objects.Pickup;
import com.Nerdiful.game.RetroGun.Objects.Player;
import com.Nerdiful.game.RetroGun.Objects.Projectiles.Projectile;
 
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
 
public class PickupManager
{
    private DifficultyManager difficulty;
    private List<Pickup> pickups;
     
    private final double MULT = 1.125;
    private int cooldown = 5000;
    private int spawn = 0;
     
    public PickupManager(DifficultyManager difficulty)
    {
        this.difficulty = difficulty;
        pickups = new ArrayList<Pickup>();
         
        if(difficulty.getGameUpdate().getScore() >= cooldown)
        {
            while(difficulty.getGameUpdate().getScore() / cooldown > spawn)
            {
                spawn++;
                cooldown *= MULT;
            }
        }
    }
     
    public void update(float delta, int score)
    {
        Pickup pickup;
        Projectile proj;
        Player player = difficulty.getGameUpdate().getPlayer();
        ProjectileManager playerFire = difficulty.getGameUpdate().getPlayerFire();
         
        for(Iterator<Pickup> iter = pickups.iterator(); iter.hasNext();)
        {
            pickup = iter.next();
            pickup.update(delta);
             
            if(pickup.getHealth() <= 0)
            {
                if(pickup.getHealth() <= 0)
                {
                    switch(pickup.getType())
                    {
                        case HEALTH:
                        {
                            AssetLoader.health.play();
                            player.addHealth(pickup.getHealthPoints());
                            difficulty.getGameUpdate().addScore(pickup.getValue());
                        }
                    }
                }
                iter.remove();
            }
            else if(pickup.getDuration() >= pickup.maxDuration())
                iter.remove();
             
            //Check collisions with player
            if(player.getCenter().dst(new Vector2(pickup.x, pickup.y)) < player.getRadius() + pickup.r)
            {
                if(collidesPlayer(pickup, player))
                {
                    switch(pickup.getType())
                    {
                        case HEALTH:
                        {
                            AssetLoader.health.play();
                            player.addHealth(pickup.getHealthPoints());
                            difficulty.getGameUpdate().addScore(pickup.getValue());
                            pickup.takeDamage(pickup.getHealth());
                            iter.remove();
                        }
                    }
                }
            }
             
            //Check collisions with player fire
            for(Iterator<Projectile> iterP = playerFire.getList().iterator(); iterP.hasNext();)
            {
                proj = iterP.next();
                if(Math.pow(pickup.x - proj.x, 2) + Math.pow(pickup.y - proj.y, 2) < Math.pow(pickup.r, 2))
                {
                    switch(pickup.getType())
                    {
                        case HEALTH:
                        {
                            AssetLoader.damage2.play();
                            proj.mark();
                            pickup.takeDamage(proj.getDamage());
                        }
                    }
                }
            }
        }
         
        if(score / cooldown > spawn)
        {
            pickups.add(new Pickup(Pickup.Pickups.HEALTH));
            cooldown *= MULT;
            spawn++;
        }
    }
     
    private boolean collidesPlayer(Pickup pickup, Player player)
    {
        Vector2 center = new Vector2(pickup.x, pickup.y);
        Vector2[] points = player.getPoints();
        Vector2 hyp, adj;
        float k;
         
        for(int i=0; i<points.length; i++)
        {
            //Check if any player points intersect circle
            if(points[i].dst(center) < pickup.r)
                return true;
             
            hyp = new Vector2(points[i].x - pickup.x, points[i].y - pickup.y);
            adj = new Vector2(points[i].x - points[(i+1)%3].x, points[i].y - points[(i+1)%3].y);
            k = hyp.dot(adj);
             
            if(k > 0)
            {
                k /= hyp.len();
                 
                if(k < hyp.len())
                {
                    if(hyp.dot(hyp) - (k * k) <= pickup.r * pickup.r)
                        return true;
                }
            }
        }
        return false;
    }
     
    public void draw(Batch batch)
    {
        if(pickups.size() > 0)
        {
            Pickup pickup;
            for(int i=0; i<pickups.size(); i++)
            {
                pickup = pickups.get(i);
                switch(pickup.getType())
                {
                    case HEALTH:
                    {
                        batch.draw(AssetLoader.pickupHealth, pickup.x - pickup.r, pickup.y - pickup.r, 2 * pickup.r, 2 * pickup.r);
                        break;
                    }
                    default:
                        break;
                }
            }
        }
    }
}
