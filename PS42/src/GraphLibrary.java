import java.util.*;

/**
 * Graph Library for a Breath First Search and bfs related methods
 * @author David Molina, CS10, PS4, 23S
 */

public class GraphLibrary {

    /**
     * Does a bfs on supplied graph from starting vertex
     * @return graph of points connected to start
     * @param graph graph to do bfs on
     * @param start starting vertex for bfs
     */
    public static <V, E> AdjacencyMapGraph<V, E> bfs(AdjacencyMapGraph<V, E> graph, V start) throws Exception {

        SimpleQueue<V> queue = new SLLQueue<>();
        AdjacencyMapGraph<V, E> pathTree = new AdjacencyMapGraph<>();

        queue.enqueue(start);
        graph.addVisited(start);

        while (!queue.isEmpty()) {
            V vertex = queue.dequeue();
            if (!pathTree.hasVertex(vertex)) {
                pathTree.insertVertex(vertex);
            }

            for (V v : graph.outNeighbors(vertex)) {
                if (!pathTree.isVisited(v)) {
                    pathTree.addVisited(v);
                    pathTree.insertVertex(v);
                    pathTree.insertDirected(v, vertex, graph.getLabel(vertex, v));
                    queue.enqueue(v);
                }
            }
        }
        return pathTree;
    }

//    public static <V, E> AdjacencyMapGraph<V, E> bfs(AdjacencyMapGraph<V, E> graph, V start) throws Exception {
//        Map<V,V> backTrack = new HashMap<V,V>(); //initialize backTrack
//        backTrack.put(start, null); //load start vertex with null parent
//        Set<V> visited = new HashSet<V>(); //Set to track which vertices have already been visited
//        Queue<V> queue = new LinkedList<V>(); //queue to implement BFS
//        AdjacencyMapGraph<V, E> pathTree = new AdjacencyMapGraph<>();
//
//        queue.add(start); //enqueue start vertex
//        visited.add(start); //add start to visited Set
//        while (!queue.isEmpty()) { //loop until no more vertices
//            V u = queue.remove(); //dequeue
//            for (V v : graph.outNeighbors(u)) { //loop over out neighbors
//                if (!visited.contains(v)) { //if neighbor not visited, then neighbor is discovered from this vertex
//                    visited.add(v); //add neighbor to visited Set
//                    queue.add(v); //enqueue neighbor
////                    backTrack.put(v, u); //save that this vertex was discovered from prior vertex
//                    pathTree.insertVertex(v);
//                    pathTree.insertDirected(v, u, graph.getLabel(u, v));
//                }
//            }
//        }
//        return pathTree;
//    }

//        enqueue the start vertex s onto the queue
//        remember that s has been added
//        repeat until we find the goal vertex or the queue is empty:
//            dequeue the next vertex u from the queue
//                (maybe do something while here)
//                for all vertices v that are adjacent to u
//                    if haven't already added v
//                        enqueue v onto the queue
//                        remember that v has been added



    /**
     * Does a bfs on supplied graph from starting vertex
     * @return list of vertices on path
     * @param tree graph path is grabbed from
     * @param vertex vertex path starts at
     */
    public static <V, E> List<V> getPath(AdjacencyMapGraph<V, E> tree, V vertex) {
        List<V> pathList = new ArrayList<>();
        V currentVertex = vertex;

        while (currentVertex != tree.getCenter()) {
            pathList.add(currentVertex);
            currentVertex = tree.returnOutVertex(currentVertex);
        }
        pathList.add(currentVertex);
        return pathList;
    }

    /**
     * Finds all vertices that cannot be reached from center of universe
     * @return Set of vertices not connected to path
     * @param graph original graph
     * @param subgraph subgraph of connected points on original graph
     */
    public static <V, E> Set<V> missingVertices(Graph<V, E> graph, Graph<V, E> subgraph) {
        Set<V> set = new HashSet<>();


        for (V v: graph.vertices()) {
            if (!subgraph.hasVertex(v)) {
                set.add(v);
            }
        }
        return set;
    }

    /**
     * Does a bfs on supplied graph from starting vertex
     * @param tree graph to do bfs on
     * @param root starting vertex for bfs
     */
    public static <V,E> double averageSeperation(AdjacencyMapGraph<V, E> tree, V root) {
        int sum = 0;

        sum += averageSeperationHelper(tree, root, 0);

        return (double) sum / (tree.numVertices() - 1);
    }

    private static <V, E> int averageSeperationHelper(AdjacencyMapGraph<V, E> tree, V root, int level) {
        int sum = 0;

        if (tree.inDegree(root) == 0) {
            return 0;
        } else if (tree.inDegree(root) > 0) {
            level++;
            sum += (level * tree.inDegree(root));

            for (V v: tree.inNeighbors(root)) {
                sum += averageSeperationHelper(tree, v, level);
            }
        }
        return sum;
    }
}
