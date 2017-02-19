package com.Nerdiful.game.RetroGun.Helpers;
 
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
 
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
 
import com.Nerdiful.game.RetroGun.GameLoop.GameUpdate;
import com.Nerdiful.game.RetroGun.Objects.Projectiles.Projectile;
 
public class ProjectileManager
{
    private List<Projectile> projectiles;
    private double gameW, gameH;
     
    public ProjectileManager(GameUpdate update)
    {
        projectiles = new ArrayList<Projectile>();
        gameW = update.getGameW(); gameH = update.getGameH();
    }
     
    public void update()
    {
        Projectile proj;
        for(Iterator<Projectile> iter = projectiles.iterator(); iter.hasNext();)
        {
            proj = iter.next();
            proj.update();
             
            if(outOfBounds(proj) || proj.isMarked())
                iter.remove();
        }
    }
     
    public void clear()
    {
        projectiles.clear();
    }
     
    private boolean outOfBounds(Projectile proj)
    {
        double x = proj.x; double y = proj.y;
         
        if(x < 0 || y < 0 || x > gameW || y > gameH)
            return true;
        else
            return false;
    }
     
    public void addProj(Projectile newProj)
    {
        projectiles.add(newProj);
    }
     
    public List<Projectile> getList()
    {
        return projectiles;
    }
     
    public void draw(Batch sBatch)
    {
        if(projectiles.size() > 0)
        {
            Projectile proj;
            Sprite projSprite;
             
            for(int i=0; i<projectiles.size(); i++)
            {
                proj = projectiles.get(i);
                projSprite = new Sprite(AssetLoader.bullet);
                projSprite.setBounds((float) (proj.x - (proj.getWidth() / 2)), (float) (proj.y - (proj.getHeight() / 2)), proj.getWidth(), proj.getHeight());
                projSprite.setOrigin((float) proj.getWidth() / 2, (float) proj.getHeight() / 2);
                projSprite.rotate((float) proj.r);
                projSprite.draw(sBatch);
                 
                //drawTrajectory(proj, projSprite, sBatch);
            }
        }
    }
     
    private void drawTrajectory(Projectile proj, Sprite projSprite, Batch sBatch)
    {
        ShapeRenderer shapes = new ShapeRenderer();
        shapes.setProjectionMatrix(sBatch.getProjectionMatrix());
        shapes.setAutoShapeType(true);
        sBatch.end();
         
        shapes.begin(ShapeType.Line); shapes.setColor(Color.RED);
        shapes.line((float) proj.x, (float) proj.y, (float) proj.getTargetX(), (float) proj.getTargetY());
        shapes.set(ShapeType.Filled);
        shapes.circle((float) proj.x, (float) proj.y, 2f);
 
        sBatch.setProjectionMatrix(shapes.getProjectionMatrix());
        shapes.end();
        shapes.dispose();
        sBatch.begin();
    }
}
