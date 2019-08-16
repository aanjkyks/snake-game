package square.window;

import square.events.Dispatcher;
import square.events.Event;
import square.events.types.*;
import square.objects.HighScores;
import square.window.layers.classes.*;
import square.window.layers.interfaces.Layer;
import square.window.layers.interfaces.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static square.Config.*;

public class Window extends Canvas {
    private int currentFoods = 0;
    private BufferStrategy bs;
    private String title;
    private JFrame frame;
    private List<Layer> layers = new LinkedList<>();
    private boolean running = false;
    private Player player;
    private boolean gameOver = false;
    private HighScores scores = new HighScores();

    public Window(String title, int width, int height) {
        setPreferredSize(new Dimension(width, height));
        this.title = title;
        init(title);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                onEvent(new KeyPressedEvent(e.getKeyCode()));
            }

            @Override
            public void keyReleased(KeyEvent e) {
                onEvent(new KeyReleasedEvent(e.getKeyCode()));
            }
        });
        start();
    }

    public static int getUPDATES() {
        return UPDATES;
    }

    public HighScores getScores() {
        return scores;
    }

    public Player getPlayer() {
        return player;
    }

    private void setPlayer(Player player) {
        this.player = player;
        layers.add(player);
    }

    public List<Layer> getLayers() {
        return layers;
    }

    private void init(String name) { //initializing a window
        frame = new JFrame(name);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
        restart();
    }

    private void start() { //trigger for ClockTickEvent
        running = true;

        new Thread(() -> {
            long timer = System.currentTimeMillis();
            double updateTimeout = 1_000_000_000.0 / UPDATES;
            double renderTimeout = 1_000_000_000.0 / (Math.max(FRAME_LIMIT, UPDATES));
            long nextRender;
            long nextUpdate = nextRender = System.nanoTime();
            int updates = 0;
            int frames = 0;
            float interpolation;
            while (running) {
                long now = System.nanoTime();
                if (now >= nextUpdate) {
                    update();
                    nextUpdate += updateTimeout;
                    updates++;
                }
                interpolation = (float) (now + updateTimeout - nextUpdate) / (float) updateTimeout;

                if (now >= nextRender) {
                    render(interpolation);
                    nextRender += renderTimeout;
                    frames++;
                }
                if (System.currentTimeMillis() - timer > 1000) {
                    timer += 1000;
                    frame.setTitle(title + " | Updates: " + updates + " Frames: " + frames + " Score : " + player.getScore());
                    updates = 0;
                    frames = 0;
                }
            }
        }).start();
    }

    private void render(float interpolation) {
        if (bs == null) {
            createBufferStrategy(3);
            bs = getBufferStrategy();
        }
        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        onRender(g, interpolation);
        g.dispose();
        bs.show();
    }

    private void update() {
        if (gameOver)
            return;

        while (currentFoods < FOODS) {
            layers.add(0, new Food(this));
            currentFoods++;
        }
        onEvent(new ClockTickEvent());
    }

    private void onRender(Graphics g, float interpolation) {
        for (int i = 0; i < layers.size(); i++) {
            Layer layer = layers.get(i);
            layer.onRender(g, interpolation);
        }
    }

    public void onEvent(Event event) {
        Dispatcher dispatcher = new Dispatcher(event);
        dispatcher.dispatch(Event.Type.GAME_OVER, event1 -> gameOver());
        dispatcher.dispatch(Event.Type.FOOD_EATEN, event1 -> handleFood((FoodEatenEvent) event1));
        dispatcher.dispatch(Event.Type.NEW_GAME, event1 -> restart());
        for (int i = layers.size() - 1; i >= 0; i--) {
            if (i > layers.size() - 1) // layers get cleared on Game Over, i stays and tries to call onEvent on now
                return;                // deleted layer
            layers.get(i).onEvent(event);
        }
    }

    private boolean gameOver() {
        gameOver = true;
        if (scores.newHiScore(player.getScore()))
            layers.add(new HighGameOver(this));
        else layers.add(new GameOverScreen(this));
        layers.add(new ScoreBoard(this));
        return true;
    }

    private boolean restart() {
        layers = new LinkedList<>();
        layers = Collections.synchronizedList(layers);
        Player player = new Snake(this);
        setPlayer(player);
        layers.add(new CurrentScore(player, this));
        gameOver = false;
        currentFoods = 0;
        return true;
    }

    public void addLayer(Layer layer) {
        layers.add(layer);
    }

    private boolean handleFood(FoodEatenEvent event) {
        if (event.getFood() instanceof Poison)
            onEvent(new GameOverEvent());
        else
            currentFoods--;
        if (player.getScore() % POINTS_TO_SPAWN_POISON == 0)
            layers.add(0, new Poison(this));
        return false;
    }
}
