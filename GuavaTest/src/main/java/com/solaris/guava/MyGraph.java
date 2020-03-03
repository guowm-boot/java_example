package com.solaris.guava;

import com.google.common.graph.*;

public class MyGraph {
    public static void main(String[] args) {
        testGraph();

    }

    private static void testGraph() {
        MutableGraph<Integer> graph= GraphBuilder.undirected()
                .build();
        graph.putEdge(1, 2);
        graph.putEdge(1, 2);
        graph.putEdge(2, 3);
        graph.addNode(2);
        System.out.println(graph);
    }
}
