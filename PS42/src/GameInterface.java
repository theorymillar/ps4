import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class GameInterface {

    private Map<String, String> MovieIDName;
    private Map<String, String> ActorIDName;
    private Map<String, HashSet<String>> MovieActorMap;
    private AdjacencyMapGraph<String, Set<String>> graph;
    private boolean start = true;

    public Map<String, String> getMovieIDName() {
        return MovieIDName;
    }

    public void setMovieIDName(Map<String, String> movieIDName) {
        MovieIDName = movieIDName;
    }

    public Map<String, String> getActorIDName() {
        return ActorIDName;
    }

    public void setActorIDName(Map<String, String> actorIDName) {
        ActorIDName = actorIDName;
    }

    public Map<String, HashSet<String>> getMovieActorMap() {
        return MovieActorMap;
    }

    public void setMovieActorMap(Map<String, HashSet<String>> movieActorMap) {
        MovieActorMap = movieActorMap;
    }

    public AdjacencyMapGraph<String, Set<String>> getGraph() {
        return graph;
    }

    public void setGraph(AdjacencyMapGraph<String, Set<String>> graph) {
        this.graph = graph;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public AdjacencyMapGraph<String, Set<String>> getPathTree() {
        return pathTree;
    }

    public void setPathTree(AdjacencyMapGraph<String, Set<String>> pathTree) {
        this.pathTree = pathTree;
    }

    private AdjacencyMapGraph<String, Set<String>> pathTree;

    public GameInterface() throws Exception {
        MovieIDName = null;
        ActorIDName = null;
        MovieActorMap = null;
        graph = null;
        pathTree = null;
        playGame();
    }

    private  Map<String, String> loadFileIntoMap(String fileName) throws Exception {
        Map<String, String> map = new HashMap<>();
        try {
            BufferedReader input = new BufferedReader(new FileReader(fileName));
            String line;

            while ((line = input.readLine()) != null) {
                String[] lineList = line.split("\\|");
                map.put(lineList[0], lineList[1]);
            }
            input.close();
        } catch (IOException e) {
            System.out.println(e);
        }
        return map;
    }

    public static ArrayList<String> findBestBacon(AdjacencyMapGraph<String, Set<String>> graph) {
        Map<String, Integer> map = new HashMap<>();
        ArrayList<String> bestBaconPath = new ArrayList<>();
        for (String vertex: graph.vertices()) {
            map.put(vertex, graph.outDegree(vertex));
            bestBaconPath.add(vertex);
        }
        bestBaconPath.sort((String v1, String v2) -> Integer.compare(map.get(v1), map.get(v2)));
        return bestBaconPath;
    }

    public static ArrayList<String> findClosestSocialCircle(AdjacencyMapGraph<String, Set<String>> graph) throws Exception {
        ArrayList<String> socialCircleOrder = new ArrayList<>();
        Map<String, Double> socialCircleValue = new HashMap<>();
        for (String vertex: graph.vertices()) {
            double valueOfSocialCircle = GraphLibrary.averageSeperation(GraphLibrary.bfs(graph, vertex), vertex);
            socialCircleValue.put(vertex, valueOfSocialCircle);
            socialCircleOrder.add(vertex);
        }
        socialCircleOrder.sort((String v1, String v2) -> Double.compare(socialCircleValue.get(v1), socialCircleValue.get(v2)));
        return socialCircleOrder;
    }

    private  Map<String,HashSet<String>> loadMovieActorMap(String fileName, Map<String,String> movieIdName,Map<String,String> actorIdName) throws Exception {
        BufferedReader input = new BufferedReader(new FileReader(fileName));
        String line;
        Map<String, HashSet<String>> map = new HashMap<>();

        while ((line = input.readLine()) != null) {
            String[] lineList = line.split("\\|");

            if (!map.containsKey(movieIdName.get(lineList[0]))) {
                map.put(movieIdName.get(lineList[0]), new HashSet<>());
            }
            if (map.containsKey(movieIdName.get(lineList[0]))) {
                String actorName = ActorIDName.get(lineList[1]);
                HashSet<String> hashSet = map.get(movieIdName.get(lineList[0]));
                hashSet.add(actorName);
                map.put(movieIdName.get(lineList[0]), hashSet);
            }
        }
        input.close();;
        return map;
    }

    private  AdjacencyMapGraph<String, Set<String>> makeGraph(Map<String, HashSet<String>> movieActorMap) {
        AdjacencyMapGraph<String, Set<String>> graph = new AdjacencyMapGraph<>();

        for (String movie: movieActorMap.keySet()) {

            for (String name1: movieActorMap.get(movie)) {
                if (!graph.hasVertex(name1)) {
                    graph.insertVertex(name1);
                }

                for (String name2: movieActorMap.get(movie)) {
                    if (!name1.equals(name2)) {
                        if (!graph.hasVertex(name2)) {
                            graph.insertVertex(name2);
                        }

                        if (!graph.hasEdge(name1, name2)) {
                            graph.insertDirected(name1, name2, new HashSet<>());
                        }
                    }

                    if (graph.hasEdge(name1, name2)) {
//                        Set<String> movieList = graph.getLabel(name1, name2);
//                        movieList.add(movie);
                        graph.insertUndirected(name1, name2, graph.getLabel(name1, name2));
                    }
                }
            }
        }
        return graph;
    }

    public void playGame() throws Exception {
        System.out.println("Hello welcome to the Bacon Game!");
        System.out.println("If your input requires inputting a name, separate command and input with comma");
        System.out.println("Here are the commands:");
        System.out.println("1: change center of the universe to a valid actor");
        System.out.println("2: find the shortest path to an actor from the center of the universe");
        System.out.println("3: find the number of actors connected to the actor at the center of the universe");
        System.out.println("4: find the average connectedness of actors to the actor at the center of the universe");
        System.out.println("5: find the number of actors the actor at center of universe co-stared with");
        System.out.println("6: name the actors who are not connected at all to center of the universe");
        System.out.println("7: sort actors based on popularity");
        System.out.println("8: sort actors based on how the averageSeparation of their inner circle");
        System.out.println("9: quit the game");

        System.out.println("The actor at the center of the universe is now Kevin Bacon");

//        MovieIDName = loadFileIntoMap("Inputs/movies.txt");
        MovieIDName = loadFileIntoMap("Inputs/moviesTest.txt");

        // System.out.println(" MovieIDName " + MovieIDName);
//        ActorIDName = loadFileIntoMap("Inputs/actors.txt");
        ActorIDName = loadFileIntoMap("Inputs/actorsTest.txt");

        // System.out.println("ActorIdName " + ActorIDName);
//        MovieActorMap = loadMovieActorMap("Inputs/movie-actors.txt", MovieIDName, ActorIDName);
        MovieActorMap = loadMovieActorMap("Inputs/movie-actorsTest.txt", MovieIDName, ActorIDName);

        //  System.out.println("MovieActorMap " + MovieActorMap);

        graph = makeGraph(MovieActorMap);
        // System.out.println("Graph: " + graph);
        graph.setCenter("Kevin Bacon");
        pathTree = GraphLibrary.bfs(graph, graph.getCenter());
        pathTree.setCenter("Kevin Bacon");
    }
}
