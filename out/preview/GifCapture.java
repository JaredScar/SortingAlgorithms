import com.jaredscarito.sortalgorithms.visual.Algorithm;
import com.jaredscarito.sortalgorithms.visual.Frame;
import com.jaredscarito.sortalgorithms.visual.SortingVisualizer;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Writes one PNG per step for every algorithm. Not part of the app.
 */
public class GifCapture {
    public static void main(String[] args) throws Exception {
        final SortingVisualizer[] window = new SortingVisualizer[1];
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                window[0] = new SortingVisualizer();
                window[0].setSize(1200, 820);
                window[0].setLocation(40, 40);
                window[0].setVisible(true);
            }
        });
        Thread.sleep(600);
        final File root = new File("out/preview/gifframes");
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                try {
                    record(window[0], Algorithm.BUBBLE, new File(root, "bubble"));
                    record(window[0], Algorithm.SELECTION, new File(root, "selection"));
                    record(window[0], Algorithm.INSERTION, new File(root, "insertion"));
                    record(window[0], Algorithm.MERGE, new File(root, "merge"));
                    record(window[0], Algorithm.QUICK, new File(root, "quick"));
                    record(window[0], Algorithm.HEAP, new File(root, "heap"));
                    record(window[0], Algorithm.SEQUENTIAL, new File(root, "sequential"));
                    record(window[0], Algorithm.BINARY, new File(root, "binary"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.exit(1);
                }
            }
        });
        window[0].dispose();
        System.exit(0);
    }

    private static void record(SortingVisualizer visualizer, Algorithm algorithm, File dir) throws Exception {
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IllegalStateException("Could not create " + dir);
        }
        Method select = SortingVisualizer.class.getDeclaredMethod("select", Algorithm.class, boolean.class);
        select.setAccessible(true);
        select.invoke(visualizer, algorithm, Boolean.FALSE);
        Field framesField = SortingVisualizer.class.getDeclaredField("frames");
        framesField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<Frame> frames = (List<Frame>) framesField.get(visualizer);
        Method showIndex = SortingVisualizer.class.getDeclaredMethod("showIndex", int.class);
        showIndex.setAccessible(true);
        visualizer.validate();
        for (int i = 0; i < frames.size(); i++) {
            showIndex.invoke(visualizer, Integer.valueOf(i));
            visualizer.validate();
            BufferedImage image = new BufferedImage(visualizer.getWidth(), visualizer.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = image.createGraphics();
            visualizer.paintAll(graphics);
            graphics.dispose();
            ImageIO.write(image, "png", new File(dir, String.format("%04d.png", i)));
        }
        System.out.println(algorithm.displayName + " " + frames.size() + " frames");
    }
}
