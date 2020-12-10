import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Scanner;

public class FlowGraph {
    int vertexCt;  // Number of vertices in the graph.
    GraphNode[] graph;  // Adjacency list for graph.
    GraphNode[] resGraph; // Residual graph
    String graphName;  //The file from which the graph was created.
    int maxFlowFromSource;
    int maxFlowIntoSink;

    int totalFlows = 0;


    public FlowGraph() {
        this.vertexCt = 0;
        this.graphName = "";
        this.maxFlowFromSource = 0;
        this.maxFlowIntoSink = 0;
    }

    /**
     * create a graph with vertexCt nodes
     * @param vertexCt
     */
    public FlowGraph(int vertexCt) {
        this.vertexCt = vertexCt;
        graph = new GraphNode[vertexCt];
        for (int i = 0; i < vertexCt; i++) {
            graph[i] = new GraphNode(i);
        }
        this.maxFlowFromSource = 0;
        this.maxFlowIntoSink = 0;
    }

    public int getVertexCt() {
        return vertexCt;
    }

    public int getMaxFlowFromSource() {
        return maxFlowFromSource;
    }

    public int getMaxFlowIntoSink() {
        return maxFlowIntoSink;
    }

    /**
     * @param source
     * @param destination
     * @param cap         capacity of edge
     * @param cost        cost of edge
     * @return create an edge from source to destination with capacation
     */
    public boolean addEdge(int source, int destination, int cap, int cost) {
        System.out.println("addEdge " + source + "->" + destination + "(" + cap + ", " + cost + ")");
        if (source < 0 || source >= vertexCt) return false;
        if (destination < 0 || destination >= vertexCt) return false;
        //add edge
        graph[source].addEdge(source, destination, cap, cost);
        return true;
    }

    /**
     * @param source
     * @param destination
     * @return return the capacity between source and destination
     */
    public int getCapacity(int source, int destination) {
        return graph[source].getCapacity(destination);
    }

    /**
     * @param source
     * @param destination
     * @return the cost of the edge from source to destination
     */
    public int getCost(int source, int destination) {
        return graph[source].getCost(destination);
    }

    /**
     * @return string representing the graph
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("The Graph " + graphName + " \n");
        sb.append("Total input " + maxFlowIntoSink + " :  Total output " + maxFlowFromSource + "\n");

        for (int i = 0; i < vertexCt; i++) {
            sb.append(graph[i].toString());
        }
        return sb.toString();
    }

    /**
     * Builds a graph from filename.  It automatically inserts backward edges needed for mincost max flow.
     * @param filename
     */
    public void makeGraph(String filename) {
        try {
            graphName = filename;
            Scanner reader = new Scanner(new File("resources/" + filename));
            vertexCt = reader.nextInt();
            graph = new GraphNode[vertexCt];
            resGraph = new GraphNode[vertexCt];
            for (int i = 0; i < vertexCt; i++) {
                graph[i] = new GraphNode(i);
                resGraph[i] = new GraphNode(i);
            }
            while (reader.hasNextInt()) {
                int v1 = reader.nextInt();
                int v2 = reader.nextInt();
                int cap = reader.nextInt();
                int cost = reader.nextInt();
                graph[v1].addEdge(v1, v2, cap, cost);
                graph[v2].addEdge(v2, v1, 0, -cost);
                resGraph[v1].addEdge(v1, v2, cap, cost);
                resGraph[v2].addEdge(v2, v1, 0, -cost);
                if (v1 == 0) maxFlowFromSource += cap;
                if (v2 == vertexCt - 1) maxFlowIntoSink += cap;
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean findFlowPath() {
        PriorityQueue<Integer> queue = new PriorityQueue<>();

        for (GraphNode node: graph) {
            node.distance = Integer.MAX_VALUE;
            node.prevNode = -1;
        }

        graph[0].distance = 0;
        queue.add(graph[0].nodeID);

        while (!queue.isEmpty()) {
            GraphNode node = graph[queue.remove()];
            for (EdgeInfo edge: node.succ) {
                GraphNode succNode = graph[edge.to];
                if (node.distance + edge.cost < succNode.distance && resGraph[edge.from].getCapacity(edge.to) > 0) {
                    succNode.distance = node.distance + edge.cost;
                    succNode.prevNode = node.nodeID;
                    queue.add(succNode.nodeID);
                }
            }
        }
        return graph[vertexCt -1].prevNode >= 0;
    }

    private int getBottleneck() {
       return getBottleneck(vertexCt -1);
    }

    private int getBottleneck(int nodeID) {
        int prevNodeID = graph[nodeID].prevNode;
        if (prevNodeID == -1) return Integer.MAX_VALUE;

        EdgeInfo currResEdge = resGraph[prevNodeID].getEdge(nodeID);
        EdgeInfo currEdge = graph[prevNodeID].getEdge(nodeID);

        int bottleneck = currResEdge.capacity;
        int prevBottleneck = getBottleneck(prevNodeID);
        return Math.min(prevBottleneck, bottleneck);
    }

    private ArrayList<EdgeInfo> getShortestPathEdges() { return getShortestPathEdges(vertexCt - 1, new ArrayList<EdgeInfo>()); }

    private ArrayList<EdgeInfo> getShortestPathEdges(int nodeID, ArrayList<EdgeInfo> edges) {
        if (nodeID == -1) return edges;
        GraphNode node = graph[nodeID];
        if (node.prevNode == -1) return edges;

        edges.add(0, graph[node.prevNode].getEdge(nodeID));
        return getShortestPathEdges(node.prevNode, edges);
    }

    private void increaseFlow() { increaseFlow(vertexCt - 1, getBottleneck()); }

    private void increaseFlow(int nodeID, int flow) {
        if (nodeID == 0) return;
        int prevNodeID = graph[nodeID].prevNode;

        resGraph[prevNodeID].getEdge(nodeID).capacity -= flow;

        increaseFlow(prevNodeID, flow);
    }

    private int getTotalCost(ArrayList<EdgeInfo> edges) {
        int totalCost = 0;
        for (EdgeInfo edge: edges) {
            int currFlow = edge.capacity - (resGraph[edge.from].getEdge(edge.to).capacity);
            totalCost += edge.cost * currFlow;
        }
        return totalCost;
    }

    public void minCostMaxFlow() {
        ArrayList<EdgeInfo> totalEdges = new ArrayList<>();

        while (findFlowPath()) {
            ArrayList<EdgeInfo> currPathEdges = getShortestPathEdges();

            StringBuilder currPathString = new StringBuilder();
            currPathString.append(currPathEdges.get(0).from);

            for (EdgeInfo edge: currPathEdges) {
                currPathString.append(" ");
                currPathString.append(edge.to);
                if (!totalEdges.contains(edge)) totalEdges.add(edge);
            }

            System.out.printf("found flow %s: %s\n", getBottleneck(), currPathString);

            increaseFlow();
            totalFlows++;
        }

        System.out.println("\n" + graphName + " Max Flow SPACE " + maxFlowIntoSink + " assigned " + totalFlows);

        for (EdgeInfo edge: totalEdges) {
            if (edge.from != 0 && edge.to != (vertexCt -1)) {
                System.out.printf("Edge (%s, %s) assigned %s(%s)\n", edge.from, edge.to, edge.capacity, edge.cost);
            }
        }

        System.out.println("TotalCost = " + getTotalCost(totalEdges));
    }

    public static void main(String[] args) {
//        String[] files = {"group1.txt", "group1.txt", "group4.txt", "group5.txt", "group6.txt", "group7.txt", "group8.txt"};
        String[] files = {"group0.txt"};

        for (String filename: files) {
            FlowGraph graph1 = new FlowGraph();
            graph1.makeGraph(filename);

//            System.out.println(graph1.toString());

            graph1.minCostMaxFlow();

        }
    }
}