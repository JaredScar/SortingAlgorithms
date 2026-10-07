package com.jaredscarito.sortalgorithms.visual;

public enum Algorithm {
    BUBBLE(
            "Bubble Sort",
            false,
            "Comparing",
            null,
            "Neighbors are compared and swapped until larger values bubble to the end. A pass with no swaps means the list is already sorted.",
            "Time: O(n²) average and worst, O(n) best when it stops early. Space: O(1). Stable: yes.",
            "repeat until no swaps:\n"
                    + "  for each neighbor pair:\n"
                    + "    if left > right:\n"
                    + "      swap them\n"
                    + "  mark the end value finished"),
    SELECTION(
            "Selection Sort",
            false,
            "Comparing",
            "Minimum",
            "Each pass finds the smallest remaining value and swaps it into the next open spot on the left.",
            "Time: O(n²) in every case. Space: O(1). Stable: no. At most one swap per pass.",
            "for each open spot i:\n"
                    + "    min = i\n"
                    + "    for each later index j:\n"
                    + "        if value[j] < value[min]:\n"
                    + "            min = j\n"
                    + "    swap i with min"),
    INSERTION(
            "Insertion Sort",
            false,
            "Comparing",
            "Moving",
            "A sorted group grows on the left. Each new value swaps left until it is in the right spot.",
            "Time: O(n²) average and worst, O(n) best when nearly sorted. Space: O(1). Stable: yes.",
            "for i from 1 to the end:\n"
                    + "    j = i\n"
                    + "    while j > 0 and value[j] < value[j - 1]:\n"
                    + "        swap j left by one\n"
                    + "        j = j - 1"),
    MERGE(
            "Merge Sort",
            false,
            "Writing",
            null,
            "The list is split in half until every piece has one value, then those pieces are merged back together in order.",
            "Time: O(n log n) in every case. Space: O(n) for the helper array. Stable: yes.",
            "sort(low, high):\n"
                    + "    if the range has one value: stop\n"
                    + "    mid = low + (high - low) / 2\n"
                    + "    sort(low, mid)\n"
                    + "    sort(mid + 1, high)\n"
                    + "    merge the halves, smaller front value first"),
    QUICK(
            "Quick Sort",
            false,
            "Comparing",
            "Pivot",
            "A pivot is placed in its final spot, with smaller values on the left and larger values on the right. Each side is then sorted the same way.",
            "Time: O(n log n) average, O(n²) worst. This pivot choice is slow on sorted data. Space: O(log n) calls. Stable: no.",
            "sort(low, high):\n"
                    + "    if the range has one value: stop\n"
                    + "    pivot = value[high]\n"
                    + "    move values <= pivot to the left\n"
                    + "    put the pivot in its final spot\n"
                    + "    sort the left side\n"
                    + "    sort the right side"),
    HEAP(
            "Heap Sort",
            false,
            "Comparing",
            "Parent",
            "The array is treated as a tree. Every parent is made at least as large as its children, then the root is moved to the end.",
            "Time: O(n log n) in every case. Space: O(1). Stable: no.",
            "turn the array into a max heap\n"
                    + "for end from the last index down to 1:\n"
                    + "    swap the root with end\n"
                    + "    mark end as finished\n"
                    + "    sift the root down\n"
                    + "children of i: 2i+1 and 2i+2"),
    SEQUENTIAL(
            "Sequential Search",
            true,
            "Checking",
            null,
            "Check one value at a time from left to right. The list does not need to be sorted. Click a bar to search for that number.",
            "Time: O(n). Space: O(1). Works on unsorted data.",
            "for i from 0 to the end:\n"
                    + "    if value[i] == target:\n"
                    + "        return i\n"
                    + "return not found"),
    BINARY(
            "Binary Search",
            true,
            "End",
            "Middle",
            "On a sorted list, check the middle and throw away the half that cannot hold the target. Click a bar to search for that number.",
            "Time: O(log n). Space: O(1). The data must be sorted.",
            "low = 0, high = last index\n"
                    + "while low <= high:\n"
                    + "    mid = low + (high - low) / 2\n"
                    + "    if value[mid] == target: return mid\n"
                    + "    if target < value[mid]: high = mid - 1\n"
                    + "    else: low = mid + 1\n"
                    + "return not found");

    public final String displayName;
    public final boolean search;
    public final String focusLabel;
    public final String specialLabel;
    public final String summary;
    public final String cost;
    public final String pseudocode;

    Algorithm(String displayName, boolean search, String focusLabel, String specialLabel,
              String summary, String cost, String pseudocode) {
        this.displayName = displayName;
        this.search = search;
        this.focusLabel = focusLabel;
        this.specialLabel = specialLabel;
        this.summary = summary;
        this.cost = cost;
        this.pseudocode = pseudocode;
    }

    public String legendHtml() {
        StringBuilder html = new StringBuilder("<html>");
        html.append(swatch("#e0af68", focusLabel));
        if (specialLabel != null) {
            html.append("&nbsp;&nbsp;&nbsp;");
            html.append(swatch("#bb9af7", specialLabel));
        }
        html.append("&nbsp;&nbsp;&nbsp;");
        if (search) {
            html.append(swatch("#73daca", "Found"));
        } else {
            html.append(swatch("#9ece6a", "Final spot"));
        }
        html.append("&nbsp;&nbsp;&nbsp;");
        html.append(swatch("#565f89", "Outside this step"));
        html.append("</html>");
        return html.toString();
    }

    private static String swatch(String color, String label) {
        return "<span style='color:" + color + "'>&#9632;</span> " + label;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
