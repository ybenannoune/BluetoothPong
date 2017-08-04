package com.mygdx.game.Entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Constants;

/**
 * Created by Demo on 02/08/2017.
 */

public class Paddle
{
    public static final int HEIGHT = 15;
    public static final int WIDTH = 150;

    private float m_X;
    private float m_Y;

    public Paddle(int x, int y)
    {
        m_X = x;
        m_Y = y;
    }

    public void draw(ShapeRenderer sr)
    {
        sr.setColor(Color.WHITE);
        sr.rect(m_X, m_Y, WIDTH, HEIGHT);
    }

    public float getX()
    {
        return m_X;
    }

    public float getY()
    {
        return m_Y;
    }

    public void set(float x )
    {
        m_X = x;
    }

    public void move(float x)
    {
        m_X += x;

        if(m_X < 0)
            m_X = 0;

        if(m_X + WIDTH > Constants.GAME_WIDTH)
        {
            m_X = Constants.GAME_WIDTH - WIDTH;
        }
    }
}