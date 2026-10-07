package com.jaredscarito.sortalgorithms.searchs.seqsearch;

import com.jaredscarito.sortalgorithms.visual.Frame;
import com.jaredscarito.sortalgorithms.visual.Trace;

import java.util.List;

/**
 * Sequential search checks each value from left to right until it finds the target.
 */
public final class SeqSearch {
    private SeqSearch() {
    }

    public static List<Frame> search(int[] input, int key) {
        int[] arr = input.clone();
        int n = arr.length;
        Trace trace = new Trace(n);
        trace.snap(arr, "Sequential Search",
                "Sequential search looks for " + key + " one index at a time, starting on the left. The values do not need to be sorted. In the worst case every value is checked, which is O(n).",
                -1, -1, 0, n - 1);

        for (int i = 0; i < n; i++) {
            trace.comparison();
            if (arr[i] == key) {
                String checks = (i + 1) == 1 ? "1 check" : (i + 1) + " checks";
                trace.snap(arr, "Found",
                        arr[i] + " at index " + i + " equals " + key + ". It took " + checks + ".",
                        -1, i, i, n - 1, i);
                return trace.frames();
            }
            trace.snap(arr, "Not a match",
                    arr[i] + " at index " + i + " is not " + key + ". Step one index to the right.",
                    -1, -1, i + 1, n - 1, i);
        }

        trace.snap(arr, "Not found",
                key + " is not in the list. Every value was checked.",
                -1, -1, 0, n - 1);
        return trace.frames();
    }
}
