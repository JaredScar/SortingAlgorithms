package com.jaredscarito.sortalgorithms.sorts.bubblesort;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Created by user on 1/28/2018.
 */
public class BubbleSort {
    /**
     *
     * @param arr
     * Efficiency: O (n^2)
     */
    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        int[] originalArr = arr.clone();
        int temp;
        System.out.println(Arrays.toString(originalArr));
        System.out.println("You start out with a variable named 'temp' in which will serve the purpose as holding the last value you swapped in the loop before" +
                " the one the program is on.");
        System.out.println("-------------------- Start program --------------------");
        for(int i=0; i < n; i++){
            System.out.println("----------------------");
            System.out.println("Main Loop " + i);
            for(int j=1; j < (n-i); j++){
                if(arr[j-1] > arr[j]){
                    //swap elements
                    temp = arr[j-1];
                    System.out.println("Temp set to --> " + temp);
                    arr[j-1] = arr[j];
                    System.out.println("Set arr[j-i] to --> " + arr[j]);
                    arr[j] = temp;
                    System.out.println("Set arr[j] to temp --> " + arr[j]);
                }
                System.out.println(Arrays.toString(originalArr) + " --> " + Arrays.toString(arr));
                System.out.println("*-*-*-*-*-*-*-*-*-*-*-");
                try {
                    TimeUnit.SECONDS.sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("----------------------");
        }
    }
}
