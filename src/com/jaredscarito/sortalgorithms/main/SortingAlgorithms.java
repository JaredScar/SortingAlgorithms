package com.jaredscarito.sortalgorithms.main;

import com.jaredscarito.sortalgorithms.visual.SortingVisualizer;

import javax.swing.SwingUtilities;

/**
 * Opens the visualizer used to learn sorting and searching one step at a time.
 */
public class SortingAlgorithms {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SortingVisualizer().setVisible(true);
            }
        });
    }
}
