package com.mygdx.game.Entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Constants;

/**
 * Created by Demo on 02/08/2017.
 */

public class Ball{

    public static final int RADIUS = 15;

    private float m_X;
    private float m_Y;
    private float m_VelX;
    private float m_VelY;

    public void draw(ShapeRenderer sr)
    {
        sr.setColor(Color.RED);
        sr.circle(m_X,m_Y,RADIUS);
    }

    public float getX()
    {
        return m_X;
    }

    public float getY()
    {
        return m_Y;
    }

    public float getVelocityX()
    {
        return m_VelX;
    }

    public float getVelocityY()
    {
        return m_VelY;
    }

    public void set(float x,float y, float velX, float velY)
    {
        m_X = x;
        m_Y = y;
        m_VelX = velX;
        m_VelY = velY;
    }

    public void update(float deltaTime)
    {
        m_X += m_VelX * deltaTime;
        m_Y += m_VelY * deltaTime;

        //Bounce on wall left/right
        if( m_X > Constants.GAME_WIDTH)
        {
            m_X = Constants.GAME_WIDTH;
            m_VelX = -m_VelX;
        }
        else if( m_X < 0)
        {
            m_X = 0;
            m_VelX = -m_VelX;
        }
    }

}
