package com.jaredscarito.sortalgorithms.sorts.selectionsort;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Created by user on 1/28/2018.
 */
public class SelectionSort {
    /**
     *
     * @param arr
     * Efficiency: O (n^2)
     */
    public static void selectionSort(int[] arr) {
        int[] originalArr = arr.clone();
        System.out.println("You don't start out with any variables in this one!");
        System.out.println("-------------------- Start program --------------------");
        for (int i = 0; i < arr.length - 1; i++)
        {
            System.out.println("----------------------");
            System.out.println("Main Loop " + i);
            int index = i;
            System.out.println("Sets index to current loop index. index = i; --> Index " + i);
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[index]) {
                    index = j;
                    //Finds lowest value
                    System.out.println("---");
                    System.out.print("Sets lowest value index to index of --> " + String.valueOf(arr[j]) + " which has index " + j);
                    System.out.println("---");
                }
            }
            int smallerNumber = arr[index];
            //Swaps smallest number to current index of loop
            arr[index] = arr[i];
            System.out.println("Swaps current index with smallest number's index: " + arr[index] + " <--> " + arr[i]);
            arr[i] = smallerNumber;
            System.out.println(Arrays.toString(originalArr) + " --> " + Arrays.toString(arr));
            System.out.println("----------------------");
            try {
                TimeUnit.SECONDS.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
