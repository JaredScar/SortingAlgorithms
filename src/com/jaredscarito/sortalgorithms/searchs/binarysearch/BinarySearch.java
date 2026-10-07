package com.jaredscarito.sortalgorithms.searchs.binarysearch;

import com.jaredscarito.sortalgorithms.visual.Frame;
import com.jaredscarito.sortalgorithms.visual.Trace;

import java.util.Arrays;
import java.util.List;

/**
 * Binary search cuts a sorted list in half on every check.
 */
public final class BinarySearch {
    private BinarySearch() {
    }

    public static List<Frame> search(int[] input, int key) {
        int[] arr = input.clone();
        int n = arr.length;
        Trace trace = new Trace(n);
        if (!isSorted(arr)) {
            trace.snap(arr, "Needs sorted data",
                    "Binary search only works when the values are in order. This list is not sorted, so it is sorted before the search. The steps below use that sorted list.",
                    -1, -1, 0, n - 1);
            Arrays.sort(arr);
            trace.snap(arr, "Sorted and ready",
                    "The list is now sorted. Each step will check the middle and throw away the half that cannot contain " + key + ".",
                    -1, -1, 0, n - 1);
        } else {
            trace.snap(arr, "Binary Search",
                    "This list is already sorted. Low and high mark the range that might still contain " + key + ". The middle of that range is checked next.",
                    -1, -1, 0, n - 1);
        }

        int start = 0;
        int end = n - 1;
        while (start <= end) {
            int mid = start + (end - start) / 2;
            trace.comparison();
            trace.snap(arr, "Check the middle",
                    "Low is " + start + " and high is " + end + ". Mid = " + start + " + (" + end + " - " + start + ") / 2 = " + mid + ", which holds " + arr[mid] + ".",
                    mid, -1, start, end, start, end);
            if (arr[mid] == key) {
                trace.snap(arr, "Found",
                        key + " is at index " + mid + ". Each check threw away half of the remaining values, so this is O(log n).",
                        mid, mid, start, end);
                return trace.frames();
            }
            if (key < arr[mid]) {
                trace.snap(arr, "Discard the right half",
                        key + " is smaller than " + arr[mid] + ", so every index after " + mid + " is too large. High moves to " + (mid - 1) + ".",
                        mid, -1, start, mid - 1);
                end = mid - 1;
            } else {
                trace.snap(arr, "Discard the left half",
                        key + " is larger than " + arr[mid] + ", so every index before " + mid + " is too small. Low moves to " + (mid + 1) + ".",
                        mid, -1, mid + 1, end);
                start = mid + 1;
            }
        }

        trace.snap(arr, "Not found",
                key + " is not in the list. The range became empty, so no index was left to check.",
                -1, -1, 0, n - 1);
        return trace.frames();
    }

    private static boolean isSorted(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < arr[i - 1]) {
                return false;
            }
        }
        return true;
    }
}
