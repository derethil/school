import java.util.Arrays;

public class GreedySolution {
    private final Graph graph;
    private SupplySolution partialSoln;

    public GreedySolution(Graph graph) {
        this.graph = graph;
        partialSoln = new SupplySolution(graph.vertexCt);
    }

    public void addSupplies(SupplySolution partialSoln, int newSupplyNode) {
        partialSoln.supplies[newSupplyNode] = true;
        partialSoln.covered[newSupplyNode] = true;
        partialSoln.supplyCt++;
        partialSoln.needToCover = partialSoln.needToCover - 1 - wouldCover(newSupplyNode, partialSoln); // Subtract 1 to include supply city in covered cities

        while (graph.G[newSupplyNode].succ.hasNext()) {
            int newCovered = Integer.parseInt(graph.G[newSupplyNode].succ.getNext().toString());
            partialSoln.covered[newCovered] = true;
        }
    }

    public int wouldCover(int newSupplyNode, SupplySolution partialSoln) {
        GraphNode node = graph.G[newSupplyNode];
        int total = node.succCt;

        // Check for already covered cities
        while (node.succ.hasNext()) {
            int potentialCovered = Integer.parseInt(node.succ.getNext().toString());
            if (partialSoln.covered[potentialCovered]) { total--; }
        }
        return total;
    }


    public void findGreedySolution() {
        // Remove duplicates in graph
//        for (int i = 0; i < graph.vertexCt; i++) {
//            graph.G[i].succ.removeDuplicates();
//        }
        while (partialSoln.needToCover > 0) {
            // Find the city with the most connections that has not been covered

            int maxWouldCoverIdx = 0;
            int maxWouldCover = 0;

            for (int nodeIdx = 0; nodeIdx < partialSoln.covered.length; nodeIdx++) {
                int nodeWouldCover = wouldCover(nodeIdx, partialSoln);
                if (nodeWouldCover >= maxWouldCover && !partialSoln.covered[nodeIdx]) {
                    maxWouldCoverIdx = nodeIdx;
                    maxWouldCover = nodeWouldCover;
                }
            }

            addSupplies(partialSoln, maxWouldCoverIdx);
        }
        System.out.println(partialSoln.toString());
    }

}
