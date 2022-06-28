import DataClasses.GraphicsData;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Main implements Runnable{

    private Thread thread;
    private JFrame window;
    private Canvas canvas;
    private Game game;
    private boolean running = false;

    private BufferStrategy bufferStrategy;
    private BufferedImage backBuffer;

    private GraphicsData graphicsData;

    public Main()
    {
        int width = 320;
        int height = 180;

        backBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        graphicsData = new GraphicsData();
        graphicsData.width = width;
        graphicsData.height = height;
        graphicsData.scale = 3;
        graphicsData.scaledWidth = graphicsData.width * graphicsData.scale;
        graphicsData.scaledHeight = graphicsData.height * graphicsData.scale;
        graphicsData.pixels = ((DataBufferInt)backBuffer.getRaster().getDataBuffer()).getData();

        Dimension size = new Dimension(graphicsData.scaledWidth, graphicsData.scaledHeight);
        canvas  = new Canvas();
        canvas.setPreferredSize(size);

        window = new JFrame();
        window.setResizable(false);
        window.setTitle("Sail Engine");
        window.add(canvas);
        window.pack();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        if(bufferStrategy == null)
        {
            // TODO: Log Error.
            return;
        }

        game = new Game();

        start();
    }

    public synchronized void start()
    {
        thread = new Thread(this, "GameThread");
        thread.start();
    }

    public synchronized void stop()
    {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double nanoSecond = 1000000000.0 / 60.0;
        double delta = 0;

        running = true;
        while(running)
        {
            long currentTime = System.nanoTime();
            delta += (currentTime-lastTime) / nanoSecond;
            lastTime = currentTime;
            while(delta >= 1)
            {
                game.updateAndRender(graphicsData);
                delta--;
            }

            Graphics g = bufferStrategy.getDrawGraphics();
            g.drawImage(backBuffer, 0, 0, graphicsData.scaledWidth, graphicsData.scaledHeight, null);
            g.dispose();
            bufferStrategy.show();
        }
    }

    public static void main(String[] args)
    {
        Main main = new Main();
    }
}
