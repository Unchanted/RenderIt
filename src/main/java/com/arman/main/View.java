package com.arman.main;

import com.arman.geom.Polygon3D;
import com.arman.geom.Vector3D;
import com.arman.models.Mesh;
import com.arman.renderers.BasicRenderer;
import com.arman.renderers.BoundingBox;
import com.arman.renderers.Renderer;
import com.arman.renderers.ScanLineRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.IOException;

public class View extends Canvas implements Runnable {

    private boolean running;
    private BufferStrategy bs;
    private BoundingBox bounds;

    private Renderer renderer;

    public View() {
        this.bounds = new BoundingBox(440, 330);
        this.running = false;
    }

    public static void main(String[] args) {
        View view = new View();
        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(view, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        view.start();
    }

    public int getWidth() {
        return this.bounds.width;
    }

    public int getHeight() {
        return this.bounds.height;
    }

    public int getTop() {
        return this.bounds.y;
    }

    public int getLeft() {
        return this.bounds.x;
    }

    @Override
    public BoundingBox getBounds() {
        return bounds;
    }

    public void start() {
        createBufferStrategy(3);
        this.bs = getBufferStrategy();
        this.renderer = new ScanLineRenderer(new Camera(), this);
        this.running = true;
        Thread thread = new Thread(this);
        thread.start();
    }

    public Dimension getPreferredSize() {
        return new Dimension(440, 330);
    }


    public float getDistanceToCamera() {
        return (this.bounds.width / 2) / (float) Math.tan(0.5 * Math.PI / 2);
    }

    @Override
    public void run() {
        double time;
        double lastTime = System.nanoTime() / 1000000000.0;
        double passed;
        double delta = 0;
        double rate = 0;
        int frames = 0;
        int fps = 0;
        while (running) {
            boolean render = false;
            time = System.nanoTime() / 1000000000.0;
            passed = time - lastTime;
            Time.deltaTime = passed;
            Time.time += Time.deltaTime;
            lastTime = time;
            delta += passed;
            rate += passed;
            double tickSpeed = 1.0 / 60.0;
            while (delta >= tickSpeed) {
                delta -= tickSpeed;
                render = true;
                update(tickSpeed);
                if (rate >= 1.0) {
                    rate = 0;
                    fps = frames;
                    Time.fps = fps;
                    frames = 0;
                }
            }
            if (render) {
                draw();
                frames++;
                Time.frameCount++;
            } else {
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void update(double tickSpeed) {

    }

    public void paint(Graphics g) {
        super.paint(g);
    }

    public void draw() {
        Graphics2D g = (Graphics2D) this.bs.getDrawGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(Color.WHITE);
        g.drawString("FPS: " + Time.fps, 10, 20);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        int z = 5;
        this.renderer.draw(g, new Polygon3D(new Vector3D(100, 100, z), new Vector3D(100, 200, z), new Vector3D(200, 200, z), new Vector3D(200, 100, z), new Vector3D(150, 150, z)));
        try {
            Mesh mesh = new MeshLoader().load("./gear.obj");
            mesh.add(new Vector3D(100, 100, 0));
            this.renderer.draw(g, mesh);
        } catch (IOException e) {
            e.printStackTrace();
        }
        g.dispose();
        this.bs.show();
    }

    public float viewXToScreenX(float x) {
        return x + this.bounds.x + this.bounds.width / 2;
    }

    public float viewYToScreenY(float y) {
        return -y + this.bounds.y + this.bounds.height / 2;
    }

    public float screenXToViewX(float x) {
        return x - this.bounds.x - this.bounds.width / 2;
    }

    public float screenYToViewY(float y) {
        return -y + this.bounds.y + this.bounds.height / 2;
    }

    public void project(Vector3D v) {
        float distanceToCamera = getDistanceToCamera();
        v.setX(distanceToCamera * v.getX() / v.getZ());
        v.setY(distanceToCamera * v.getY() / v.getZ());
        v.setX(viewXToScreenX(v.getX()));
        v.setY(viewYToScreenY(v.getY()));
    }

}
