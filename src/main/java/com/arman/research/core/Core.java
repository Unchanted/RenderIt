package com.arman.research.core;

import javax.swing.*;
import java.awt.*;

public abstract class Core {

    public static final int DEFAULT_FONT_SIZE = 24;

    public static final DisplayMode[] MID_RES_MODES = {
            new DisplayMode(800, 600, 16, 0), new DisplayMode(800, 600, 32, 0),
            new DisplayMode(800, 600, 24, 0), new DisplayMode(640, 480, 16, 0),
            new DisplayMode(640, 480, 32, 0), new DisplayMode(640, 480, 24, 0),
            new DisplayMode(1024, 768, 16, 0),
            new DisplayMode(1024, 768, 32, 0),
            new DisplayMode(1024, 768, 24, 0)
    };

    public static final DisplayMode[] LOW_RES_MODES = {
            new DisplayMode(640, 480, 16, 0), new DisplayMode(640, 480, 32, 0),
            new DisplayMode(640, 480, 24, 0), new DisplayMode(800, 600, 16, 0),
            new DisplayMode(800, 600, 32, 0), new DisplayMode(800, 600, 24, 0),
            new DisplayMode(1024, 768, 16, 0),
            new DisplayMode(1024, 768, 32, 0),
            new DisplayMode(1024, 768, 24, 0)
    };

    public static final DisplayMode[] VERY_LOW_RES_MODES = {
            new DisplayMode(320, 240, 16, 0), new DisplayMode(400, 300, 16, 0),
            new DisplayMode(512, 384, 16, 0), new DisplayMode(640, 480, 16, 0),
            new DisplayMode(800, 600, 16, 0)
    };

    private boolean running;
    private ScreenManager screen;
    private int fontSize = DEFAULT_FONT_SIZE;

    public void stop() {
        running = false;
    }

    public void run() {
        try {
            init(VERY_LOW_RES_MODES);
            loop();
        } finally {
            if (screen != null) {
                screen.restoreScreen();
            }
            lazilyExit();
        }
    }

    public void lazilyExit() {
        Thread thread = new Thread() {
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
                System.exit(0);
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    public int getFontSize() {
        return fontSize;
    }

    public ScreenManager getScreen() {
        return screen;
    }

    public void init() {
        init(MID_RES_MODES);
    }

    public void init(DisplayMode[] possibleModes) {
        screen = new ScreenManager();
        DisplayMode displayMode = screen.findFirstCompatibleMode(possibleModes);
        screen.setFullScreen(displayMode);
        Window window = screen.getFullScreenWindow();
        window.setFont(new Font("Dialog", Font.PLAIN, fontSize));
        window.setBackground(Color.blue);
        window.setForeground(Color.white);
        running = true;
    }

    public Image loadImage(String fileName) {
        return new ImageIcon(fileName).getImage();
    }

    public void loop() {
        long startTime = System.currentTimeMillis();
        long currTime = startTime;
        while (running) {
            long elapsedTime = System.currentTimeMillis() - currTime;
            currTime += elapsedTime;
            update(elapsedTime);
            Graphics2D g = screen.getGraphics();
            draw(g);
            g.dispose();
            screen.update();
        }
    }

    public void update(long elapsedTime) {

    }

    public abstract void draw(Graphics2D g);

}