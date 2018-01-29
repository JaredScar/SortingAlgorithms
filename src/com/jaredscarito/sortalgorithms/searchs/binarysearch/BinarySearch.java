package com.jaredscarito.sortalgorithms.searchs.binarysearch;

import java.util.concurrent.TimeUnit;

/**
 * Created by user on 1/28/2018.
 */
public class BinarySearch {
    /**
     *
     * @param inputArr
     * @param key
     * Efficiency:  O (lg n)
     */
    public static void binarySearch(int[] inputArr, int key) {
        System.out.println("Binary Search requires an already sorted list to work. The variables it starts off with are " +
                "the start of the array (0) and the end (args.length-1).");
        int start = 0;
        int end = inputArr.length - 1;
        System.out.println("-------------------- Start program --------------------");
        while (start <= end) {
            System.out.println("----------------------");
            System.out.println("Looped - " + start);
            int mid = (start + end) / 2;
            System.out.println("The mid is set to (start + (args.length-1)) --> " + mid);
            if (key == inputArr[mid]) {
                System.out.println("The key's index has been found and is --> " + mid);
            }
            if (key < inputArr[mid]) {
                System.out.println("The key is smaller than the current index, we subtract 1 from mid.");
                end = mid - 1;
            } else {
                start = mid + 1;
                System.out.println("The key is larger than the current index, we add 1 to mid.");
            }
            try {
                TimeUnit.SECONDS.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("----------------------");
        }
        System.out.println("The value you were looking for was not found within the search!");
    }
}
