import com.jaredscarito.sortalgorithms.visual.SortingVisualizer;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class Shot {
    public static void main(String[] args) throws Exception {
        final SortingVisualizer[] window = new SortingVisualizer[1];
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                window[0] = new SortingVisualizer();
                window[0].setVisible(true);
            }
        });
        Thread.sleep(1600);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                try {
                    SortingVisualizer visualizer = window[0];
                    BufferedImage image = new BufferedImage(
                            visualizer.getWidth(), visualizer.getHeight(), BufferedImage.TYPE_INT_RGB);
                    Graphics2D graphics = image.createGraphics();
                    visualizer.paintAll(graphics);
                    graphics.dispose();
                    ImageIO.write(image, "png", new File("out/preview/window.png"));
                    System.out.println("SHOT " + visualizer.getWidth() + "x" + visualizer.getHeight());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
