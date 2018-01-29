package com.jaredscarito.sortalgorithms.sorts.insertionsort;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Created by user on 1/28/2018.
 */
public class InsertionSort {
    /**
     *
     * @param input
     * Efficiency: O (n^2)
     */
    public static void insertionSort(int[] input) {
        System.out.println("You start out with a variable named 'temp' in which will serve the purpose to swap the two positions in the array");
        int temp;
        int[] originalArr = input.clone();
        System.out.println("-------------------- Start program --------------------");
        for (int i = 1; i < input.length; i++) {
            System.out.println("----------------------");
            System.out.println("Looped - " + i);
            for(int j = i ; j > 0 ; j--){
                System.out.println("---");
                System.out.println("Goes through the loop backwards and checks if the current input is smaller than any of the lower indexes");
                if(input[j] < input[j-1]){
                    System.out.print(input[j] + " is lower than " + input[j-1] + " so we have to swap there positions");
                    temp = input[j];
                    input[j] = input[j-1];
                    input[j-1] = temp;
                    System.out.println(Arrays.toString(originalArr) + " --> " + Arrays.toString(input));
                    System.out.println("*-*-*-*-*-*-*-*-*-*-*-");
                }
                System.out.println("---");
            }
            System.out.println("----------------------");
            try {
                TimeUnit.SECONDS.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(input);
    }
}
