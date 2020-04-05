package com.mendix.merger.merge.minheap;

import java.util.Comparator;

public class HeapNodeComparator implements Comparator<HeapNode> {

    @Override
    public int compare(HeapNode node1, HeapNode node2) {
        return node1.getWord().compareTo(node2.getWord());
    }
}
