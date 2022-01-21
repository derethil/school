import java.util.Arrays;

public class GreedySolution {
    private final Graph graph;
    private SupplySolution partialSoln;

    public GreedySolution(Graph graph) {
        this.graph = graph;
        partialSoln = new SupplySolution(graph.vertexCt);
    }

    public void addSupplies(SupplySolution partialSoln, int newSupplyNode) {
        if (!partialSoln.covered[newSupplyNode]) {
            partialSoln.needToCover--;
        }
        partialSoln.supplies[newSupplyNode] = true;
        partialSoln.covered[newSupplyNode] = true;
        partialSoln.supplyCt++;

        while (graph.G[newSupplyNode].succ.hasNext()) {
            int newCovered = Integer.parseInt(graph.G[newSupplyNode].succ.getNext().toString());
            if (!partialSoln.covered[newCovered]) { partialSoln.needToCover--; }
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


    public SupplySolution findGreedySolution() {
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
        return partialSoln;
    }

}
