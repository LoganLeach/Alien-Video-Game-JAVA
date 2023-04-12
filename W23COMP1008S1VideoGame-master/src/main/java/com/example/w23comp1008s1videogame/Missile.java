package com.example.w23comp1008s1videogame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Missile extends Sprite{

    /**
     * We know the missile image, width, height and speed
     * @param posX
     * @param posY
     */
    public Missile(int posX, int posY) {
        super(posX, posY, 40, 20, 7,
                new Image(Missile.class.getResourceAsStream("images/missile.png")));
    }

    private void moveRight()
    {
        posX += speed;

        if (posX>1000)
            setAlive(false);
    }

    public void draw(GraphicsContext gc)
    {
        super.draw(gc);
        moveRight();
    }
}
