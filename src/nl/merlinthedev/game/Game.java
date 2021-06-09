package nl.merlinthedev.game;

import nl.merlinthedev.game.entities.Player;
import nl.merlinthedev.game.gfx.Colors;
import nl.merlinthedev.game.gfx.Screen;
import nl.merlinthedev.game.gfx.SpriteSheet;
import nl.merlinthedev.game.level.Level;
import nl.merlinthedev.game.music.musicLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Game extends Canvas implements Runnable {

    public static final int WIDTH = 160;
    public static final int HEIGHT = WIDTH / 12 * 9;
    public static final int SCALE = 7;
    public static final String NAME = "Artic Exploration";
    private static String filepath;


    private JFrame frame;

    public boolean running = false;
    public int tickCount = 0;

    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT,
            BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    private int[] colors = new int[6 * 6 * 6];

    private Screen screen;
    public InputHandler input;
    public Level level;
    public Player player;


    public Game() {
        setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

        frame = new JFrame(NAME);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(this, BorderLayout.CENTER);
        frame.pack();

        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    public void init() {
        int index = 0;
        for (int r = 0; r < 6; r++) {
            for (int g = 0; g < 6; g++) {
                for (int b = 0; b < 6; b++) {
                    int rr = (r * 255 / 5);
                    int gg = (g * 255 / 5);
                    int bb = (b * 255 / 5);

                    colors[index++] = rr << 16 | gg << 8 | bb;
                }
            }
        }

        screen = new Screen(WIDTH, HEIGHT, new SpriteSheet("/SpriteSheet.png"));
        input = new InputHandler(this);
        level = new Level("/levels/water_level.png");
        player = new Player(level, 0, 0, input);
        level.addEntity(player);

        filepath = "res/music.wav";
        musicLoader musicObject = new musicLoader();
        musicObject.playMusic(filepath);


    }

    public synchronized void start() {
        running = true;
        new Thread(this).start();
    }

    public synchronized void stop() {
        running = false;
    }

    public void run() {
        this.requestFocus();
        long lastTime = System.nanoTime();
        long lastTimer = System.currentTimeMillis();
        double nsPerTick = 1000000000 / 60;
        double delta = 0;
        int ticks = 0;
        int frames = 0;

        init();

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;
            boolean shouldRender = true;

            while (delta >= 1) {
                ticks++;
                tick();
                delta -= 1;
                shouldRender = true;
            }
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (shouldRender) {
                frames++;
                render();
            }

            if (System.currentTimeMillis() - lastTimer >= 1000) {
                lastTimer += 1000;
                System.out.println(ticks + " ticks , " + frames
                        + " frames per second");
                frames = 0;
                ticks = 0;
            }
        }
    }


    public void tick() {
        tickCount++;
        level.tick();
    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        int xOffset = player.x - (screen.width / 2);
        int yOffset = player.y - (screen.height / 2);

        level.renderTiles(screen, xOffset, yOffset);
        level.renderEntities(screen);

        for (int x = 0; x < level.width; x++) {
            int color = Colors.get(-1, -1, -1, -1);
            if (x % 10 == 0 && x != 0) {
                color = Colors.get(-1, -1, -1, -1);
            }

        }


        for (int y = 0; y < screen.height; y++) {
            for (int x = 0; x < screen.width; x++) {
                int ColorCode = screen.pixels[x + y * screen.width];
                if (ColorCode < 255) {
                    pixels[x + y * WIDTH] = colors[ColorCode];

                }
            }
        }

        Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        g.dispose();
        bs.show();
    }

    public static void main(String[] args) {
        new Game().start();
    }

}