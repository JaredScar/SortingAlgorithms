package com.jaredscarito.sortalgorithms.sorts.insertionsort;

import com.jaredscarito.sortalgorithms.visual.Frame;
import com.jaredscarito.sortalgorithms.visual.Trace;

import java.util.List;

/**
 * Insertion sort grows a sorted group on the left by sliding each new value into place.
 */
public final class InsertionSort {
    private InsertionSort() {
    }

    public static List<Frame> sort(int[] input) {
        int[] arr = input.clone();
        int n = arr.length;
        Trace trace = new Trace(n);
        trace.snap(arr, "Insertion Sort",
                "Insertion sort keeps a sorted group on the left. It takes the next value and swaps it left until that group is in order again. Equal values are left alone, so the sort is stable.",
                -1, -1, 0, n - 1);

        for (int i = 1; i < n; i++) {
            int j = i;
            trace.snap(arr, "Take the next value",
                    "Insert " + arr[i] + " into the sorted group covering indexes 0 to " + (i - 1) + ".",
                    i, -1, 0, i, i);
            while (j > 0) {
                trace.comparison();
                if (arr[j] >= arr[j - 1]) {
                    trace.snap(arr, "In order",
                            arr[j] + " is greater than or equal to " + arr[j - 1] + ", so it stops here. Indexes 0 to " + i + " are in order.",
                            j, -1, 0, i, j - 1, j);
                    break;
                }
                int moving = arr[j];
                int other = arr[j - 1];
                Trace.swap(arr, j, j - 1);
                trace.move();
                j--;
                trace.snap(arr, "Swap left",
                        moving + " is smaller than " + other + ", so it swaps one step to the left.",
                        j, -1, 0, i, j, j + 1);
            }
        }

        trace.settleAll();
        trace.snap(arr, "Done",
                "Every value has been inserted into the growing group. On a list that is already sorted, each value stops after one comparison, which is why the best case is O(n).",
                -1, -1, 0, n - 1);
        return trace.frames();
    }
}
