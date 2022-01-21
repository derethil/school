public class OptimalSolution {

    private final Graph graph;

    public OptimalSolution(Graph graph) {
        this.graph = graph;
    }

    public SupplySolution addSupplies(SupplySolution partialSolution, int newSupplyNode) {
        if (!partialSolution.covered[newSupplyNode]) { partialSolution.needToCover--; }
        partialSolution.supplies[newSupplyNode] = true;
        partialSolution.covered[newSupplyNode] = true;
        partialSolution.supplyCt++;

        while (graph.G[newSupplyNode].succ.hasNext()) {
            int newCovered = Integer.parseInt(graph.G[newSupplyNode].succ.getNext().toString());
            if (!partialSolution.covered[newCovered]) { partialSolution.needToCover--; }
            partialSolution.covered[newCovered] = true;
        }

        return partialSolution;
    }

    public SupplySolution findOptimalSolution(int nodeId, int allowedSupplyNodes, SupplySolution partialSolution) {
        if (nodeId >= graph.vertexCt || allowedSupplyNodes < partialSolution.supplyCt) { return partialSolution; }

        SupplySolution didAddSupplies = addSupplies(new SupplySolution(partialSolution), nodeId);

        SupplySolution dintAddSuppliesSol = findOptimalSolution(nodeId + 1, allowedSupplyNodes, partialSolution);
        SupplySolution didAddSuppliesSol = findOptimalSolution(nodeId + 1, allowedSupplyNodes, didAddSupplies);

        if (didAddSuppliesSol.supplyCt + didAddSuppliesSol.needToCover <= dintAddSuppliesSol.supplyCt + dintAddSuppliesSol.needToCover) {
            return didAddSuppliesSol;
        }
        return dintAddSuppliesSol;
    }
}
