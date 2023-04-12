package com.example.w23comp1008s1videogame;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;

public class GameBoardController {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button startButton;

    //This variable is used to hold the keys that the game player is pressing
    private HashSet<KeyCode> activeKeys;

    @FXML
    void startGame(ActionEvent event) {
        startButton.setVisible(false);

        //store the keys pressed in the activeKeys set
        activeKeys = new HashSet<>();
        anchorPane.getScene().setOnKeyPressed(keyPressed -> activeKeys.add(keyPressed.getCode()));
        anchorPane.getScene().setOnKeyReleased(keyReleased -> activeKeys.remove(keyReleased.getCode()));

        Canvas canvas = new Canvas(1000,800);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        //get Image objects
        Image background = new Image(getClass().getResourceAsStream("images/space.png"));

        //Create a Sprite that we can draw on our canvas
        Ship ship = new Ship(300,500);
        ArrayList<Alien> aliens = new ArrayList<>();

        //Random Number Generator (rng)
        SecureRandom rng = new SecureRandom();

        for (int i=1; i<=5; i++)
            aliens.add(new Alien(rng.nextInt(700,1000), rng.nextInt(0,750)));

        ArrayList<Explosion> explosions = new ArrayList<>();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gc.drawImage(background,0,0,canvas.getWidth(),canvas.getHeight());
                userMovesShip(ship);


                if (ship.isAlive())
                    ship.draw(gc);
                else if (explosions.size()==0  && ship.getMissilesReleased().size()==0)
                    stop();

                aliens.removeIf(alien -> !alien.isAlive());
                explosions.removeIf(explosion -> !explosion.isAlive());

                //your goal is to add logic such that any existing explosions can finish before the animation stops

                if (aliens.size()==0)
                {
                    //user has destroyed all the aliens, send a message and stop the game
                    finalMessage(gc, "You saved the universe");
                    if (explosions.size()==0  && ship.getMissilesReleased().size()==0)
                        stop();
                }

                //draw each of the explosions
                for(Explosion explosion : explosions)
                    explosion.draw(gc);

                for(Alien alien : aliens)
                {
                    alien.draw(gc);

                    if (alien.collidesWith(ship) && ship.isAlive())
                    {
                        explosions.add(new Explosion(ship.getPosX(), ship.getPosY()));
                        ship.setAlive(false);
                        alien.setAlive(false);
                        finalMessage(gc, "The Aliens got you - nice try!");
                        stop();

//                        startButton.setVisible(true);
//                        anchorPane.getChildren().remove(canvas);
                    }

                    //did any of the missiles hit the alien?
                    for (Missile missile : ship.getMissilesReleased())
                    {
                        if (missile.collidesWith(alien))
                        {
                            explosions.add(new Explosion(alien.getPosX(), alien.getPosY()));
                            missile.setAlive(false);
                            alien.setAlive(false);
                        }
                    }
                }
            }
        };
        timer.start();
        anchorPane.getChildren().add(canvas);
    }

    private void finalMessage(GraphicsContext gc, String message)
    {
        Font font = Font.font("Arial", FontWeight.NORMAL,32);
        gc.setFont(font);
        gc.setFill(Color.WHITE);
        gc.fillText(message, 250,350);
    }

    private void userMovesShip(Ship ship)
    {
        if (activeKeys.contains(KeyCode.UP))
            ship.moveUp();
        if (activeKeys.contains(KeyCode.DOWN))
            ship.moveDown();
        if (activeKeys.contains(KeyCode.LEFT))
            ship.moveLeft();
        if (activeKeys.contains(KeyCode.RIGHT))
            ship.moveRight();
        if (activeKeys.contains(KeyCode.SPACE))
            ship.shootMissile();
    }
}
