package com.jaredscarito.sortalgorithms.visual;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Collects the frames an algorithm wants to show.
 */
public final class Trace {
    private final List<Frame> frames = new ArrayList<Frame>();
    private final boolean[] settled;
    private int comparisons;
    private int moves;

    public Trace(int length) {
        settled = new boolean[length];
    }

    public void comparison() {
        comparisons++;
    }

    public void move() {
        moves++;
    }

    public void settle(int index) {
        if (index >= 0 && index < settled.length) {
            settled[index] = true;
        }
    }

    public void settleAll() {
        Arrays.fill(settled, true);
    }

    public void snap(int[] values, String title, String detail, int special, int found, int low, int high, int... focus) {
        frames.add(new Frame(values, title, detail, focus, special, found, low, high, settled, comparisons, moves));
    }

    public List<Frame> frames() {
        return Collections.unmodifiableList(frames);
    }

    public static void swap(int[] values, int i, int j) {
        int temp = values[i];
        values[i] = values[j];
        values[j] = temp;
    }
}
