package com.jaredscarito.sortalgorithms.sorts.mergesort;

import java.util.Arrays;

/**
 * Created by user on 1/28/2018.
 */
public class MergeSort {
    private static int[] numbers;
    private static int[] helper;

    private static int number;

    public static void sort(int[] values) {
        System.out.println("-------------------- Start sort() --------------------");
        System.out.println("The sort() method is ran");
        numbers = values;
        System.out.println("int[] numbers is set to " + Arrays.toString(values));
        number = values.length;
        System.out.println("int number is set to " + values.length);
        helper = new int[number];
        System.out.println("int[] helper is set to " + Arrays.toString(new int[number]));
        mergesort(0, number - 1);
        System.out.println("We run mergeSort(0, (number - 1))");
        System.out.println("-------------------- End sort() --------------------");
    }

    private static void mergesort(int low, int high) {
        // check if low is smaller than high, if not then the array is sorted
        System.out.println("-------------------- Start mergesort() --------------------");
        System.out.println("We check if int low < int high");
        if (low < high) {
            System.out.println("It is");
            // Get the index of the element which is in the middle
            int middle = low + (high - low) / 2;
            System.out.println("Get the index of the element which is in middle with --> low + (high-low) / 2 = " + middle);
            // Sort the left side of the array
            mergesort(low, middle);
            System.out.println("We then run mergeSort(low, middle) which is using recursion to sort left side of array");
            // Sort the right side of the array
            mergesort(middle + 1, high);
            System.out.println("We then run mergeSort(middle + 1, high) which is using recursion to sort right side of array");
            // Combine them both
            merge(low, middle, high);
            System.out.println("We run merge(low, middle, high) to combine them both together");
        }
        System.out.println("-------------------- End mergesort() --------------------");
    }

    private static void merge(int low, int middle, int high) {
        System.out.println("-------------------- Start merge() --------------------");
        // Copy both parts into the helper array
        for (int i = low; i <= high; i++) {
            System.out.println("helper[i] = numbers[i]");
            System.out.println(helper[i] + " = " + numbers[i]);
            helper[i] = numbers[i];
        }

        int i = low;
        System.out.println("int i = " + i);
        int j = middle + 1;
        System.out.println("int j = " + j);
        int k = low;
        System.out.println("int k = " + k);
        // Copy the smallest values from either the left or the right side back
        // to the original array
        System.out.println("Copy the smallest values from either the left side or right side back to original array");
        while (i <= middle && j <= high) {
            if (helper[i] <= helper[j]) {
                System.out.println("- Left side -");
                System.out.println("helper[i] is <= helper[j]");
                System.out.println(helper[i] + " <= " + helper[j]);
                System.out.println("numbers[k] = helper[i]");
                System.out.println(numbers[k] + " = " + helper[i]);
                numbers[k] = helper[i];
                i++;
            } else {
                System.out.println("- Right side -");
                System.out.println("helper[i] is not <= helper[j]");
                System.out.println("numbers[k] = helper[j]");
                System.out.println(numbers[k] + " = " + helper[j]);
                numbers[k] = helper[j];
                j++;
            }
            k++;
        }
        // Copy the rest of the left side of the array into the target array
        System.out.println("Copy rest of left side of the array into target array:");
        while (i <= middle) {
            System.out.println("numbers[k] = helper[i]");
            System.out.println(numbers[k] + " = " + helper[i]);
            numbers[k] = helper[i];
            k++;
            i++;
        }
        // Since we are sorting in-place any leftover elements from the right side
        // are already at the right position.
        System.out.println("-------------------- End merge() --------------------");
    }
}
