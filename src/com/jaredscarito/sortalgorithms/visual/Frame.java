package com.jaredscarito.sortalgorithms.visual;

/**
 * One moment in an algorithm, saved so the learner can move forward and backward.
 */
public final class Frame {
    public final int[] values;
    public final String title;
    public final String detail;
    public final int[] focus;
    public final int special;
    public final int found;
    public final int rangeLow;
    public final int rangeHigh;
    public final boolean[] settled;
    public final int comparisons;
    public final int moves;

    public Frame(int[] values, String title, String detail, int[] focus, int special, int found,
                 int rangeLow, int rangeHigh, boolean[] settled, int comparisons, int moves) {
        this.values = values.clone();
        this.title = title;
        this.detail = detail;
        this.focus = focus == null ? new int[0] : focus.clone();
        this.special = special;
        this.found = found;
        this.rangeLow = rangeLow;
        this.rangeHigh = rangeHigh;
        this.settled = settled.clone();
        this.comparisons = comparisons;
        this.moves = moves;
    }

    public boolean isFocus(int index) {
        for (int focused : focus) {
            if (focused == index) {
                return true;
            }
        }
        return false;
    }

    public boolean isSettled(int index) {
        return index >= 0 && index < settled.length && settled[index];
    }
}
