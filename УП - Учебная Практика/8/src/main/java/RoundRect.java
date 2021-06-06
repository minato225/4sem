/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

/**
 *
 * @author Рома
 */
public class RoundRect extends Canvas{
    private int width;
    private int height;
    private Color color;

    private final int arcWidth = 30;
    private final int arcHeight = 30;

    private int x = 50;
    private int y = 50;
    

    public RoundRect(int width, int height, Color color) {
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public void setWidth(int width){
        this.width=width;
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
    public Color getColor(){
        return color;
    }
    public void setHeight(int Height){
        this.height=Height;
    }
    public void setColor(Color clr){
        this.color=clr;
    }
    public RoundRect() {
        width = 300;
        height = 200;
        color = new Color(250, 26, 57);
    }

    public void setStartPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void paint(Graphics g) {        
        Graphics2D g2 = (Graphics2D) g;

        g2.setPaint(Color.RED);
        g2.draw(new RoundRectangle2D.Double(x, y, width, height, arcWidth, arcHeight));
        g2.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
    }
}
