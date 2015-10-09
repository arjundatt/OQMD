package DIC.Test.algoclass;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Arnab Saha
 * Date: 6/5/15
 * Time: 3:21 PM
 */
public class PrimsMST {
    int[][] adjacencyMatrix = {
            {0, 1, 5, 4},
            {1, 0, 2, -1},
            {5, 2, 0, 3},
            {4, -1, 3, 0}};
    int numberOfNodes = 4;
    int numberOfEdges = 10;

    public ArrayList<Edge> getPrimsMST(Integer startNode) {
        ArrayList<Integer> visitedNodes = new ArrayList<Integer>();
        visitedNodes.add(startNode);
        ArrayList<Edge> minSTEdges = new ArrayList<Edge>();
        EdgeHeap edgeHeap = new EdgeHeap(numberOfEdges);
        for (int i = 0; i < numberOfNodes; i++) {
            if (adjacencyMatrix[startNode][i] > 0)
                edgeHeap.insert(new Edge(startNode, i, adjacencyMatrix[startNode][i]));
        }
        while (visitedNodes.size() < numberOfNodes) {
            Edge minEdge = edgeHeap.extractMin();
            if (minEdge != null) {
                if (!visitedNodes.contains(minEdge.endNode)) {
                    minSTEdges.add(minEdge);
                    visitedNodes.add(minEdge.endNode);
                    for (int i = 0; i < numberOfNodes; i++) {
                        if (adjacencyMatrix[minEdge.endNode][i] > 0)
                            edgeHeap.insert(new Edge(minEdge.endNode, i, adjacencyMatrix[minEdge.endNode][i]));
                    }
                }
            }
        }
        return minSTEdges;
    }

    public static void main(String[] args) {
        PrimsMST primsMST = new PrimsMST();
        for (Edge edge : primsMST.getPrimsMST(0)) {
            System.out.println(edge.startNode + " " + edge.endNode + " " + edge.weight);
        }
    }
}
