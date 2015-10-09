package DIC.Test.algoclass;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Arnab Saha
 * Date: 5/30/15
 * Time: 1:53 PM
 */
public class DijkstraShortestPath {
    int n = 8;
    int e = 20;
    int[][] adjacencyMatrix = {
            {0, 2, 3, 0, 0, 0, 0, 0},
            {0, 0, 0, 5, 10, 0, 0, 0},
            {0, 0, 0, 0, 0, 4, 8, 0},
            {0, 0, 0, 0, 3, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 1},
            {0, 0, 0, 0, 0, 0, 0, 8},
            {0, 0, 0, 0, 0, 0, 0, 0}};


    public int[] findShortestPaths(int startNode) {
        ArrayList<Integer> visitedNodes = new ArrayList<Integer>();
        visitedNodes.add(startNode);
        int[] shortestPathDistances = new int[n];
        shortestPathDistances[startNode] = 0;
        EdgeHeap edgeHeap = new EdgeHeap(e);
        for (int i = 0; i < n; i++) {
            if (adjacencyMatrix[startNode][i] > 0)
                edgeHeap.insert(new Edge(startNode, i, adjacencyMatrix[startNode][i]));
        }
        while (visitedNodes.size() < n) {
            Edge minEdge = edgeHeap.extractMin();
            if (minEdge == null)
                return shortestPathDistances;
            if (!visitedNodes.contains(minEdge.endNode)) {
                visitedNodes.add(minEdge.endNode);
                shortestPathDistances[minEdge.endNode] = shortestPathDistances[minEdge.startNode] + minEdge.weight;
                for (int i = 0; i < n; i++) {
                    if (adjacencyMatrix[minEdge.endNode][i] > 0)
                        edgeHeap.insert(new Edge(minEdge.endNode, i, adjacencyMatrix[minEdge.endNode][i]));
                }
            }
        }
        return shortestPathDistances;
    }

    public static void main(String[] args) {
        DijkstraShortestPath shortestPath = new DijkstraShortestPath();
        int[] shortestPaths = shortestPath.findShortestPaths(0);
        for (int i = 0; i < shortestPaths.length; i++) {
            System.out.println(i + " = " + shortestPaths[i]);
        }
    }
}

class Edge implements Comparable<Edge> {
    int startNode;
    int endNode;
    int weight;

    Edge(int startNode, int endNode, int weight) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.weight = weight;
    }

    @Override
    public int compareTo(Edge edge) {
        if (weight < edge.weight)
            return -1;
        else if (weight == edge.weight)
            return 0;
        else return 1;
    }
}
