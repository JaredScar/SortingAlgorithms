package com.jaredscarito.sortalgorithms.sorts.quicksort;

import com.jaredscarito.sortalgorithms.visual.Frame;
import com.jaredscarito.sortalgorithms.visual.Trace;

import java.util.List;

/**
 * Quick sort places a pivot in its final position, then sorts the two sides.
 * This version uses the last value in the current range as the pivot.
 */
public final class QuickSort {
    private QuickSort() {
    }

    public static List<Frame> sort(int[] input) {
        int[] arr = input.clone();
        int n = arr.length;
        Trace trace = new Trace(n);
        trace.snap(arr, "Quick Sort",
                "Quick sort picks a pivot and rearranges the range so smaller values are on its left and larger values are on its right. The pivot is then in its final place. This version uses the last value of the range as the pivot, which is slow when the data is already sorted.",
                -1, -1, 0, n - 1);
        sort(arr, 0, n - 1, trace);
        trace.settleAll();
        trace.snap(arr, "Done",
                "Every pivot ended in its final place, so the whole list is sorted. Balanced pivots make this fast. A pivot that is always the smallest or largest value makes it slow.",
                -1, -1, 0, n - 1);
        return trace.frames();
    }

    private static void sort(int[] arr, int low, int high, Trace trace) {
        if (low > high) {
            return;
        }
        if (low == high) {
            trace.settle(low);
            return;
        }
        int pivotIndex = partition(arr, low, high, trace);
        sort(arr, low, pivotIndex - 1, trace);
        sort(arr, pivotIndex + 1, high, trace);
    }

    private static int partition(int[] arr, int low, int high, Trace trace) {
        int pivot = arr[high];
        trace.snap(arr, "Choose a pivot",
                "The pivot is " + pivot + " at index " + high + ". Values less than or equal to it will move to the left side of indexes " + low + " to " + high + ".",
                high, -1, low, high, high);

        int i = low - 1;
        for (int j = low; j < high; j++) {
            trace.comparison();
            if (arr[j] <= pivot) {
                i++;
                if (i != j) {
                    int moving = arr[j];
                    Trace.swap(arr, i, j);
                    trace.move();
                    trace.snap(arr, "Move to the left",
                            moving + " is less than or equal to the pivot " + pivot + ", so it joins the left side.",
                            high, -1, low, high, i);
                } else {
                    trace.snap(arr, "Already on the left",
                            arr[j] + " is less than or equal to the pivot " + pivot + " and is already on the left.",
                            high, -1, low, high, j);
                }
            } else {
                trace.snap(arr, "Stay on the right",
                        arr[j] + " is greater than the pivot " + pivot + ", so it stays on the right for now.",
                        high, -1, low, high, j);
            }
        }

        int finalIndex = i + 1;
        if (finalIndex != high) {
            Trace.swap(arr, finalIndex, high);
            trace.move();
        }
        trace.settle(finalIndex);
        trace.snap(arr, "Pivot placed",
                pivot + " is now at index " + finalIndex + ", and that spot will not change again. Everything to its left is smaller or equal, and everything to its right is larger.",
                finalIndex, -1, low, high);
        return finalIndex;
    }
}
