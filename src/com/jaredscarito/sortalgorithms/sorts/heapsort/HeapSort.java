package com.jaredscarito.sortalgorithms.sorts.heapsort;

import com.jaredscarito.sortalgorithms.visual.Frame;
import com.jaredscarito.sortalgorithms.visual.Trace;

import java.util.List;

/**
 * Heap sort turns the array into a max heap, then repeatedly removes the largest value.
 * For a node at index i, the children are at 2i + 1 and 2i + 2.
 */
public final class HeapSort {
    private HeapSort() {
    }

    public static List<Frame> sort(int[] input) {
        int[] arr = input.clone();
        int n = arr.length;
        Trace trace = new Trace(n);
        trace.snap(arr, "Heap Sort",
                "Heap sort reads the array as a tree. The value at index i is a parent. Its children are at 2i + 1 and 2i + 2. A max heap says every parent is at least as large as its children, so the largest value sits at index 0.",
                -1, -1, 0, n - 1);

        for (int i = n / 2 - 1; i >= 0; i--) {
            sift(arr, n, i, trace, "Build the heap");
        }
        if (n > 0) {
            trace.snap(arr, "Heap ready",
                    "The array is a max heap. Index 0 holds the largest value. The tree under the bars shows the same array: parents above, children below.",
                    0, -1, 0, n - 1);
        }

        for (int end = n - 1; end > 0; end--) {
            int largest = arr[0];
            Trace.swap(arr, 0, end);
            trace.move();
            trace.settle(end);
            trace.snap(arr, "Remove the root",
                    largest + " was the largest value still in the heap, so it moves to index " + end + " and stays there. The hole at the root has to be repaired.",
                    -1, -1, 0, end - 1, end);
            sift(arr, end, 0, trace, "Repair the heap");
        }

        trace.settleAll();
        trace.snap(arr, "Done",
                "The heap is empty. Each removed root was the next largest value, so they sit in order from left to right. This always takes O(n log n) time and needs no extra array.",
                -1, -1, 0, n - 1);
        return trace.frames();
    }

    private static void sift(int[] arr, int heapSize, int start, Trace trace, String phase) {
        int i = start;
        while (true) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int largest = i;
            if (left < heapSize) {
                trace.comparison();
                if (arr[left] > arr[largest]) {
                    largest = left;
                }
            }
            if (right < heapSize) {
                trace.comparison();
                if (arr[right] > arr[largest]) {
                    largest = right;
                }
            }
            if (largest == i) {
                if (left < heapSize) {
                    String children = right < heapSize
                            ? "its children " + arr[left] + " and " + arr[right]
                            : "its child " + arr[left];
                    int[] focus = right < heapSize ? new int[]{left, right} : new int[]{left};
                    trace.snap(arr, phase,
                            "Parent " + arr[i] + " at index " + i + " is at least as large as " + children + ", so this branch is a valid heap.",
                            i, -1, 0, heapSize - 1, focus);
                }
                return;
            }

            int parentIndex = i;
            int childIndex = largest;
            int parentValue = arr[parentIndex];
            int childValue = arr[childIndex];
            Trace.swap(arr, parentIndex, childIndex);
            trace.move();
            trace.snap(arr, phase,
                    childValue + " at index " + childIndex + " is larger than its parent " + parentValue + " at index " + parentIndex + ", so they swap. The parent that moved down is checked against its new children.",
                    parentIndex, -1, 0, heapSize - 1, childIndex);
            i = childIndex;
        }
    }
}
