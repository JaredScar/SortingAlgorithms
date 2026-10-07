package com.jaredscarito.sortalgorithms.sorts.selectionsort;

import com.jaredscarito.sortalgorithms.visual.Frame;
import com.jaredscarito.sortalgorithms.visual.Trace;

import java.util.List;

/**
 * Selection sort repeatedly chooses the smallest remaining value and places it next.
 */
public final class SelectionSort {
    private SelectionSort() {
    }

    public static List<Frame> sort(int[] input) {
        int[] arr = input.clone();
        int n = arr.length;
        Trace trace = new Trace(n);
        trace.snap(arr, "Selection Sort",
                "Selection sort scans the unsorted part for the smallest value, then swaps that value into the next open spot on the left. That spot is finished.",
                -1, -1, 0, n - 1);

        for (int i = 0; i < n - 1; i++) {
            int min = i;
            trace.snap(arr, "Start a pass",
                    "Search from index " + i + " to the end. The smallest candidate so far is " + arr[min] + " at index " + min + ".",
                    min, -1, i, n - 1);
            for (int j = i + 1; j < n; j++) {
                trace.comparison();
                if (arr[j] < arr[min]) {
                    min = j;
                    trace.snap(arr, "New minimum",
                            arr[min] + " at index " + min + " is the smallest value found so far.",
                            min, -1, i, n - 1, j);
                } else {
                    trace.snap(arr, "Keep the minimum",
                            arr[j] + " is not smaller than " + arr[min] + ". The minimum stays at index " + min + ".",
                            min, -1, i, n - 1, j);
                }
            }
            if (min != i) {
                int placed = arr[min];
                int displaced = arr[i];
                Trace.swap(arr, i, min);
                trace.move();
                trace.settle(i);
                trace.snap(arr, "Swap into place",
                        placed + " is the smallest remaining value, so it swaps with " + displaced + ". Index " + i + " is finished.",
                        -1, -1, i + 1, n - 1);
            } else {
                trace.settle(i);
                trace.snap(arr, "Already in place",
                        arr[i] + " is already the smallest remaining value, so index " + i + " is finished without a swap.",
                        -1, -1, i + 1, n - 1);
            }
        }

        trace.settleAll();
        trace.snap(arr, "Done",
                "Each spot received the minimum of what was left. Selection sort always scans the rest of the list, even when the values are already sorted.",
                -1, -1, 0, n - 1);
        return trace.frames();
    }
}
