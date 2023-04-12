package com.example.w23comp1008s1videogame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Alien extends Sprite {

    public Alien(int posX, int posY) {
        super(posX, posY, 60, 60, 4,
                new Image(Alien.class.getResourceAsStream("images/alien.png")));
    }

    private void moveLeft()
    {
        posX -= speed;

        if (posX<0)
            posX=1000;
    }

    public void draw(GraphicsContext gc)
    {
        if (this.isAlive())
        {
            super.draw(gc);
            moveLeft();
        }
    }
}
