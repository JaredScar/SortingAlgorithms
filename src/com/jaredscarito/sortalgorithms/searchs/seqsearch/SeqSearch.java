package com.jaredscarito.sortalgorithms.searchs.seqsearch;

/**
 * Created by user on 1/28/2018.
 */
public class SeqSearch {
    /**
     *
     * @param arr
     * @param key
     * Efficiency: O (N)
     */
    public static void seqSearch(int[] arr, int key) {
        int i = 0;
        for(int arrVal : arr) {
            if(arrVal == key) {
                System.out.print("FOUND AT INDEX - " + i + " -");
                return;
            }
            i++;
        }
    }
}
