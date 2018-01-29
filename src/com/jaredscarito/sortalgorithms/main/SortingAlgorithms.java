package com.jaredscarito.sortalgorithms.main;

import com.jaredscarito.sortalgorithms.searchs.binarysearch.BinarySearch;
import com.jaredscarito.sortalgorithms.searchs.seqsearch.SeqSearch;
import com.jaredscarito.sortalgorithms.sorts.bubblesort.BubbleSort;
import com.jaredscarito.sortalgorithms.sorts.insertionsort.InsertionSort;
import com.jaredscarito.sortalgorithms.sorts.mergesort.MergeSort;
import com.jaredscarito.sortalgorithms.sorts.selectionsort.SelectionSort;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by user on 9/26/2017.
 */
public class SortingAlgorithms {
    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("What are the numbers we should have? (Seperate values with a comma)");
        while(scanner.hasNext()) {
            String[] numSplit = scanner.nextLine().split(",");
            List<Integer> numsList = new ArrayList<>();
            for(String num : numSplit) {
                numsList.add(Integer.parseInt(num));
            }
            int[] arr = numsList.stream().mapToInt(i->i).toArray();
            System.out.println("What sorting algorithm would you like to use?");
            System.out.println("Bubble(0), Selection(1), Insertion(2), Merge(3), Heap(4), Quick(5) or would you like to binary search(6)?");
            switch (scanner.nextInt()) {
                case 0:
                    //Bubble
                    BubbleSort.bubbleSort((arr));
                    break;
                case 1:
                    //Selection
                    SelectionSort.selectionSort(arr);
                    break;
                case 2:
                    //Insertion
                    InsertionSort.insertionSort(arr);
                    break;
                case 3:
                    //Merge
                    MergeSort.sort(arr);
                    break;
                case 4:
                    //Heap
                    break;
                case 5:
                    //Quick
                    break;
                case 6:
                    //Binary Search
                    System.out.print("Which value are we finding the index of?");
                    BinarySearch.binarySearch(arr, scanner.nextInt());
                    break;
                case 7:
                    //Sequential Search
                    System.out.print("Which value are we finding the index of?");
                    SeqSearch.seqSearch(arr, scanner.nextInt());
            }
        }
    }
    /**
     * http://www.java2novice.com/java-sorting-algorithms/
     * Sorts needed:
     * Selection *
     * Insertion *
     * Merge *
     * Heap
     * Quick
     * Bubble *
     *
     * http://www.java2novice.com/java-search-algorithms/
     * Searches needed:
     * Binary Search *
     * Sequential Search *
     */
}
