package square.window.layers.classes;

import square.events.Dispatcher;
import square.events.Event;
import square.events.types.ClockTickEvent;
import square.events.types.FoodEatenEvent;
import square.events.types.KeyPressedEvent;
import square.events.types.KeyReleasedEvent;
import square.window.Window;
import square.window.layers.interfaces.ClockTickLayer;
import square.window.layers.interfaces.KeyboardControlledLayer;
import square.window.layers.interfaces.Layer;
import square.window.layers.interfaces.Player;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;

import static square.Config.SPRITE_SIZE;

public class Head implements KeyboardControlledLayer, ClockTickLayer, Player {
    private final Window window;
    private final Random r = new Random();
    private int lastX, lastY;
    private int score = 0;
    private Rectangle player;
    private boolean movingUp, movingDown, movingLeft, movingRight, moving;
    private Color color = Color.BLUE;
    private boolean random = false;

    public Head(Window window) {
        this.window = window;
        init();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public void init() {
        stopMovement();
        player = new Rectangle((window.getWidth() / (SPRITE_SIZE * 2)) * SPRITE_SIZE,
                (window.getHeight() / (SPRITE_SIZE * 2)) * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
        score = 0;
    }

    @Override
    public boolean safePosition(Rectangle rectangle) {
        return true;
    }

    @Override
    public boolean onTick(ClockTickEvent e) {
        boolean eaten = false;
        List<Layer> layers = window.getLayers();
        for (int i = 0; i < layers.size(); i++) {
            Layer food = layers.get(i);
            if (food instanceof Food)
                if (((Food) food).getSquare().contains(player)) {
                    window.onEvent(new FoodEatenEvent((Food) food));
                    eaten = true;
                }
        }

        if (random) {
            stopMovement();
            moveRandom();
        } else move();
        return eaten;
    }

    @Override
    public boolean onKeyReleased(KeyReleasedEvent event) {
        return false;
    }

    @Override
    public boolean onKeyPressed(KeyPressedEvent event) {
//        moved = true;
        stopMovement();
        switch (event.getKeyCode()) {
            case KeyEvent.VK_UP:
                moving = movingUp = true;
                break;
            case KeyEvent.VK_DOWN:
                moving = movingDown = true;
                break;
            case KeyEvent.VK_LEFT:
                moving = movingLeft = true;
                break;
            case KeyEvent.VK_RIGHT:
                moving = movingRight = true;
                break;
            case KeyEvent.VK_R:
                random = !random;
                moving = true;
                break;
        }
        return true;
    }

    private void move() {
        lastX = player.x;
        lastY = player.y;
        int lastPossibleX = (window.getWidth() / SPRITE_SIZE - 1) * SPRITE_SIZE;
        int lastPossibleY = (window.getHeight() / SPRITE_SIZE - 1) * SPRITE_SIZE;
        if (movingRight) {
            player.x += SPRITE_SIZE;
            if (player.x > lastPossibleX) {
                player.x = 0;
            }
        }
        if (movingLeft) {
            player.x -= SPRITE_SIZE;
            if (player.x < 0) {
                player.x = lastPossibleX;
            }
        }
        if (movingDown) {
            player.y += SPRITE_SIZE;
            if (player.y > lastPossibleY) {
                player.y = 0;
            }
        }
        if (movingUp) {
            player.y -= SPRITE_SIZE;
            if (player.y < 0) {
                player.y = lastPossibleY;
            }
        }
    }

    private void moveRandom() {
        if (r.nextBoolean()) {
            if (r.nextBoolean()) {
                movingUp = true;
            } else movingDown = true;
        } else if (r.nextBoolean()) {
            movingLeft = true;
        } else movingRight = true;
        move();
    }

    private void stopMovement() {
        moving = movingRight = movingDown = movingLeft = movingUp = false;
    }

    @Override
    public void onRender(Graphics g, float interpolation) {
//        interpolation = 1;
        int diffX = (int) ((player.x - lastX) * interpolation);
        int diffY = (int) ((player.y - lastY) * interpolation);
        g.setColor(color);
        g.fillRect(lastX + diffX, lastY + diffY, player.width, player.height);
    }

    @Override
    public int getRenderX() {
        return player.x;
    }

    @Override
    public int getRenderY() {
        return player.y;
    }

    @Override
    public void onEvent(Event event) {
        Dispatcher dispatcher = new Dispatcher(event);
        dispatcher.dispatch(Event.Type.CLOCK_TICK, event1 -> onTick((ClockTickEvent) event1));
        dispatcher.dispatch(Event.Type.KEY_PRESSED, event1 -> onKeyPressed((KeyPressedEvent) event1));
    }

    @Override
    public Rectangle getBounds() {
        return player;
    }
}
