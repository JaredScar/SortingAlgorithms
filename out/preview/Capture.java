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

public class Capture {
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
        Thread.sleep(400);
        final File dir = new File("docs/images");
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IllegalStateException("Could not create docs/images");
        }
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                try {
                    shot(window[0], Algorithm.SELECTION, "Swap into place", new File(dir, "selection-sort.png"));
                    shot(window[0], Algorithm.INSERTION, "Swap left", new File(dir, "insertion-sort.png"));
                    shot(window[0], Algorithm.MERGE, "Take from", new File(dir, "merge-sort.png"));
                    shot(window[0], Algorithm.SEQUENTIAL, "Found", new File(dir, "sequential-search.png"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        window[0].dispose();
    }

    private static void shot(SortingVisualizer visualizer, Algorithm algorithm, String titlePart, File file) throws Exception {
        Method select = SortingVisualizer.class.getDeclaredMethod("select", Algorithm.class, boolean.class);
        select.setAccessible(true);
        select.invoke(visualizer, algorithm, Boolean.FALSE);
        Field framesField = SortingVisualizer.class.getDeclaredField("frames");
        framesField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<Frame> frames = (List<Frame>) framesField.get(visualizer);
        int index = 0;
        for (int i = 0; i < frames.size(); i++) {
            if (frames.get(i).title.contains(titlePart)) {
                index = i;
                break;
            }
        }
        Method showIndex = SortingVisualizer.class.getDeclaredMethod("showIndex", int.class);
        showIndex.setAccessible(true);
        showIndex.invoke(visualizer, Integer.valueOf(index));
        visualizer.validate();
        BufferedImage image = new BufferedImage(visualizer.getWidth(), visualizer.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        visualizer.paintAll(graphics);
        graphics.dispose();
        ImageIO.write(image, "png", file);
        System.out.println(algorithm.displayName + " [" + frames.get(index).title + "] -> " + file.getPath());
    }
}
