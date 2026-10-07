package com.jaredscarito.sortalgorithms.sorts.bubblesort;

import com.jaredscarito.sortalgorithms.visual.Frame;
import com.jaredscarito.sortalgorithms.visual.Trace;

import java.util.List;

/**
 * Bubble sort walks neighboring pairs and swaps any that are out of order.
 * After each pass, the next largest value is in its final place.
 */
public final class BubbleSort {
    private BubbleSort() {
    }

    public static List<Frame> sort(int[] input) {
        int[] arr = input.clone();
        int n = arr.length;
        Trace trace = new Trace(n);
        trace.snap(arr, "Bubble Sort",
                "Bubble sort compares neighbors and swaps them when the left one is larger. Large values bubble to the end. If a whole pass makes no swaps, the list is already sorted and the algorithm stops.",
                -1, -1, 0, n - 1);

        boolean finishedEarly = false;
        for (int i = 0; i < n - 1 && !finishedEarly; i++) {
            boolean swapped = false;
            for (int j = 1; j < n - i; j++) {
                int left = arr[j - 1];
                int right = arr[j];
                trace.comparison();
                if (left > right) {
                    Trace.swap(arr, j - 1, j);
                    trace.move();
                    swapped = true;
                    trace.snap(arr, "Swap",
                            left + " is greater than " + right + ", so the neighbors swap.",
                            -1, -1, 0, n - 1 - i, j - 1, j);
                } else {
                    trace.snap(arr, "In order",
                            left + " is already less than or equal to " + right + ", so they stay put.",
                            -1, -1, 0, n - 1 - i, j - 1, j);
                }
            }
            trace.settle(n - 1 - i);
            trace.snap(arr, "End of pass " + (i + 1),
                    "Pass " + (i + 1) + " is done. " + arr[n - 1 - i] + " is in its final place.",
                    -1, -1, 0, n - i - 2);
            if (!swapped) {
                finishedEarly = true;
            }
        }

        trace.settleAll();
        if (finishedEarly) {
            trace.snap(arr, "Early stop",
                    "A pass made no swaps, so every remaining value is already in order. Bubble sort can stop before finishing all of its passes.",
                    -1, -1, 0, n - 1);
        } else {
            trace.snap(arr, "Done",
                    "Every pass placed the next largest value at the end. The list is sorted.",
                    -1, -1, 0, n - 1);
        }
        return trace.frames();
    }
}
