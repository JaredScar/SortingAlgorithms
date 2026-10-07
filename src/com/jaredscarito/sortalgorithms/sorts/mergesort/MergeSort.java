package com.jaredscarito.sortalgorithms.sorts.mergesort;

import com.jaredscarito.sortalgorithms.visual.Frame;
import com.jaredscarito.sortalgorithms.visual.Trace;

import java.util.List;

/**
 * Merge sort splits the list until every piece has one value, then merges those pieces in order.
 */
public final class MergeSort {
    private MergeSort() {
    }

    public static List<Frame> sort(int[] input) {
        int[] arr = input.clone();
        int n = arr.length;
        Trace trace = new Trace(n);
        int[] helper = new int[n];
        trace.snap(arr, "Merge Sort",
                "Merge sort splits the list in half until each piece has one value, then merges the pieces back together. It uses a helper array, so it needs extra space. A tie takes the left value, which keeps equal items in their original order.",
                -1, -1, 0, n - 1);
        sort(arr, helper, 0, n - 1, trace);
        trace.settleAll();
        trace.snap(arr, "Done",
                "Every piece has been merged. The work is split evenly, so the running time stays O(n log n) even on data that is already sorted.",
                -1, -1, 0, n - 1);
        return trace.frames();
    }

    private static void sort(int[] arr, int[] helper, int low, int high, Trace trace) {
        if (low >= high) {
            return;
        }
        int mid = low + (high - low) / 2;
        trace.snap(arr, "Split",
                "Split indexes " + low + " to " + high + " into " + low + " to " + mid + " and " + (mid + 1) + " to " + high + ". Sort each half, then merge them.",
                -1, -1, low, high);
        sort(arr, helper, low, mid, trace);
        sort(arr, helper, mid + 1, high, trace);
        merge(arr, helper, low, mid, high, trace);
    }

    private static void merge(int[] arr, int[] helper, int low, int mid, int high, Trace trace) {
        for (int index = low; index <= high; index++) {
            helper[index] = arr[index];
        }
        trace.snap(arr, "Merge two halves",
                "Merge " + low + " to " + mid + " with " + (mid + 1) + " to " + high + ". Compare the front of each half and write the smaller one next.",
                -1, -1, low, high);

        int i = low;
        int j = mid + 1;
        int k = low;
        while (i <= mid && j <= high) {
            trace.comparison();
            if (helper[i] <= helper[j]) {
                int chosen = helper[i];
                arr[k] = chosen;
                trace.move();
                String why = chosen == helper[j]
                        ? chosen + " equals the right value, so the left one is written first. That keeps the sort stable."
                        : chosen + " from the left half is smaller than " + helper[j] + " from the right, so it is written next.";
                trace.snap(arr, "Take from the left", why, -1, -1, low, high, k);
                i++;
            } else {
                int chosen = helper[j];
                arr[k] = chosen;
                trace.move();
                trace.snap(arr, "Take from the right",
                        chosen + " from the right half is smaller than " + helper[i] + " from the left, so it is written next.",
                        -1, -1, low, high, k);
                j++;
            }
            k++;
        }
        while (i <= mid) {
            int value = helper[i];
            arr[k] = value;
            trace.move();
            trace.snap(arr, "Copy the rest of the left",
                    value + " is still waiting on the left, so it is copied into the next open spot. Leftover values on the right are already in place.",
                    -1, -1, low, high, k);
            k++;
            i++;
        }
        trace.snap(arr, "Halves merged",
                "Indexes " + low + " to " + high + " are now in order.",
                -1, -1, low, high);
    }
}
