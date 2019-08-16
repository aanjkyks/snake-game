package square.window.layers.classes;

import square.events.Dispatcher;
import square.events.Event;
import square.events.types.*;
import square.window.Window;
import square.window.layers.interfaces.ClockTickLayer;
import square.window.layers.interfaces.KeyboardControlledLayer;
import square.window.layers.interfaces.Layer;
import square.window.layers.interfaces.Player;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static square.Config.SPRITE_SIZE;

public class Snake implements KeyboardControlledLayer, ClockTickLayer, Player {
    private final Window window;
    private final Random r = new Random();
    private int snakeLength = 6;
    private int lastX, lastY;
    private int score = 0;
    private Rectangle player;
    private boolean moving, pressed;
    private MovementDirection direction = MovementDirection.NONE;
    private Color color = Color.BLUE;
    private boolean random = false;
    private LinkedList<Rectangle> tail = new LinkedList<>();

    public Snake(Window window) {
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
        return rectangle.x != player.x && rectangle.y != player.y;
    }

    @Override
    public boolean onTick(ClockTickEvent e) {
        boolean eaten = pressed = false;
        List<Layer> layers = window.getLayers();
        for (int i = 0; i < layers.size(); i++) {
            Layer food = layers.get(i);
            if (food instanceof Food)
                if (((Food) food).getSquare().contains(player)) {
                    window.onEvent(new FoodEatenEvent((Food) food));
                    snakeLength++;
                    eaten = true;
                }
        }

        if (random) {
            moveRandom();
        } else move();
        return eaten;
    }

    @Override
    public boolean onKeyReleased(KeyReleasedEvent event) {
        return false;
    }

    private void checkEatingTail() {
        for (int i = 0; i < tail.size() - 1; i++) {
            Rectangle rectangle = tail.get(i);
            if (rectangle.contains(player))
                window.onEvent(new GameOverEvent());
        }
    }

    @Override
    public boolean onKeyPressed(KeyPressedEvent event) {
        if (!pressed) {
            switch (event.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if (direction.getOpposite() == MovementDirection.UP)
                        break;
                    moving = true;
                    direction = MovementDirection.UP;
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction.getOpposite() == MovementDirection.DOWN)
                        break;
                    moving = true;
                    direction = MovementDirection.DOWN;
                    break;
                case KeyEvent.VK_LEFT:
                    if (direction.getOpposite() == MovementDirection.LEFT)
                        break;
                    moving = true;
                    direction = MovementDirection.LEFT;
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction.getOpposite() == MovementDirection.RIGHT)
                        break;
                    moving = true;
                    direction = MovementDirection.RIGHT;
                    break;
                case KeyEvent.VK_R:
                    random = !random;
                    moving = true;
                    break;
            }
            pressed = true;
        }
        return false;
    }

    private void move() {
        lastX = player.x;
        lastY = player.y;
        if (moving) {
            tail.addFirst(new Rectangle(lastX, lastY, SPRITE_SIZE, SPRITE_SIZE));

            direction.move(this, window);
        }

        while (tail.size() > snakeLength)
            tail.removeLast();
        checkEatingTail();
    }

    private void moveRandom() {
        if (r.nextBoolean()) {
            direction = r.nextBoolean() ?
                    direction.getOpposite() == MovementDirection.UP ? direction : MovementDirection.UP :
                    direction.getOpposite() == MovementDirection.DOWN ? direction : MovementDirection.DOWN;
        } else
            direction = r.nextBoolean() ?
                    direction.getOpposite() == MovementDirection.LEFT ? direction : MovementDirection.LEFT :
                    direction.getOpposite() == MovementDirection.RIGHT ? direction : MovementDirection.RIGHT;
        move();
    }

    private void stopMovement() {
        moving = pressed = false;
    }

    @Override
    public void onRender(Graphics g, float interpolation) {
//        interpolation = 1;
        float localInterpolation = interpolation;
        int diffX = player.x - lastX;
        int diffY = player.y - lastY;
        if (Math.abs(diffX) > SPRITE_SIZE || Math.abs(diffY) > SPRITE_SIZE)
            localInterpolation = 1;
        g.setColor(color);
        g.fillRect((int) (lastX + diffX * localInterpolation), (int) (lastY + diffY * localInterpolation), player.width,
                player.height);
        for (int i = 0; i < tail.size(); i++) {
            Rectangle rectangle = tail.get(i);
            if (i == tail.size() - 1 && i > 0) {
                float tailInterpolation = interpolation;
                Rectangle prev = tail.get(i - 1);
                if (tail.size() < snakeLength) tailInterpolation = 0;
                int diffTailX = prev.x - rectangle.x;
                int diffTailY = prev.y - rectangle.y;
                if (Math.abs(diffTailX) > SPRITE_SIZE || Math.abs(diffTailY) > SPRITE_SIZE)
                    tailInterpolation = 1;
                g.fillRect((int) (rectangle.x + diffTailX * tailInterpolation),
                        (int) (rectangle.y + diffTailY * tailInterpolation), rectangle.width, rectangle.height);
                return;
            }
            g.fillRect(rectangle.x, rectangle.y, SPRITE_SIZE, SPRITE_SIZE);
        }

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

    public enum MovementDirection {
        UP {
            @Override
            void move(Player player, Window window) {
                int lastPossibleY = (window.getHeight() / SPRITE_SIZE - 1) * SPRITE_SIZE;
                player.getBounds().y -= SPRITE_SIZE;
                if (player.getBounds().y < 0)
                    player.getBounds().y = lastPossibleY;
            }

            @Override
            public MovementDirection getOpposite() {
                return DOWN;
            }
        }, DOWN {
            @Override
            public MovementDirection getOpposite() {
                return UP;
            }

            @Override
            void move(Player player, Window window) {
                int lastPossibleY = (window.getHeight() / SPRITE_SIZE - 1) * SPRITE_SIZE;
                player.getBounds().y += SPRITE_SIZE;
                if (player.getBounds().y > lastPossibleY)
                    player.getBounds().y = 0;
            }
        }, LEFT {
            @Override
            void move(Player player, Window window) {
                int lastPossibleX = (window.getWidth() / SPRITE_SIZE - 1) * SPRITE_SIZE;
                player.getBounds().x -= SPRITE_SIZE;
                if (player.getBounds().x < 0) {
                    player.getBounds().x = lastPossibleX;
                }
            }

            @Override
            public MovementDirection getOpposite() {
                return RIGHT;
            }
        }, RIGHT {
            @Override
            void move(Player player, Window window) {
                int lastPossibleX = (window.getWidth() / SPRITE_SIZE - 1) * SPRITE_SIZE;
                player.getBounds().x += SPRITE_SIZE;
                if (player.getBounds().x > lastPossibleX)
                    player.getBounds().x = 0;
            }

            @Override
            public MovementDirection getOpposite() {
                return LEFT;
            }
        }, NONE {
            @Override
            void move(Player player, Window window) {
            }

            @Override
            public MovementDirection getOpposite() {
                return NONE;
            }
        };

        void move(Player player, Window window) {
            throw new RuntimeException("Something went wrong");
        }

        public MovementDirection getOpposite() {
            throw new RuntimeException("Something went wrong");
        }

    }
}
