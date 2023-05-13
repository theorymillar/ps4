import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class GameInterfaceDriver {
    public static void main(String[] args) throws Exception {
        GameInterface baconGame = new GameInterface();

        AdjacencyMapGraph<String, Set<String>> mainGraph = baconGame.getGraph();
        AdjacencyMapGraph<String, Set<String>> pathTree = baconGame.getPathTree();
        boolean endGame = false;
        String center = "Kevin Bacon";

        while (!endGame) {
            Scanner input = new Scanner(System.in);
            System.out.println("Insert command here: ");
            String line = input.nextLine();
            String[] command = line.split(",");

            if (command[0].equals("1") && command.length > 1) {
                center = command[1];
                System.out.println(mainGraph);
                pathTree = GraphLibrary.bfs(mainGraph, center);
                pathTree.setCenter(center);
                mainGraph.setCenter(center);
                System.out.println(center + " is now the center of the universe");
                System.out.println(pathTree);
            } else if (command[0].equals("2") && command.length > 1) {
                List<String> path = GraphLibrary.getPath(pathTree, command[1]);
                System.out.println(path);

                for (int i = path.size() - 1; i >= 1; i--) {
                    System.out.println(path.get(i) + " appeared in " + mainGraph.getLabel(path.get(i), path.get(i-1)) + " with " + path.get(i-1));
                }
            } else if (command[0].equals("3")) {
                System.out.println("The number of actors connected to center is " + (pathTree.numVertices()-1));
            } else if (command[0].equals("4")) {
                double averageSeparation = GraphLibrary.averageSeperation(pathTree, mainGraph.getCenter());
                System.out.println(mainGraph.getCenter() + " has average connectedness to other actors of " + averageSeparation);
            } else if (command[0].equals("5")) {
                System.out.println(mainGraph.getCenter() + " has co-starred with " + mainGraph.outDegree(mainGraph.getCenter()) + " actors");
            } else if (command[0].equals("6")) {
                Set<String> set = GraphLibrary.missingVertices(mainGraph, pathTree);
                String res = "";

                for (String actor: set) {
                    res += actor + ",";
                }
                System.out.println("The actors with no connection to " + mainGraph.getCenter() + " are" + res);
            } else if (command[0].equals("7")) {
                System.out.println(GameInterface.findBestBacon(mainGraph));
            } else if (command[0].equals("8")) {
                System.out.println(GameInterface.findClosestSocialCircle(mainGraph));
            } else if (command[0].equals("9")) {
                endGame = true;
                System.out.println("Thank for playing the game!");
            } else {
                System.out.println("Invalid Command");
            }
        }
    }
}
