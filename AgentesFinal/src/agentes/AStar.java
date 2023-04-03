package agentes;

import java.util.*;

public class AStar {
    private static List<Node> neighbors(int[][] matrix, Node current, Position end) {
        List<Direction> directions = Arrays.asList(new Direction(-1, 0), new Direction(1, 0), new Direction(0, -1), new Direction(0, 1));
        directions = directions.stream().filter(dir -> AgentFunctions.isType(matrix, current.position().plus(dir), AgentFunctions.typeEmpty)).toList();
        List<Node> nodeList = new ArrayList<>();

        for (Direction i : directions) {
            Position nextPosition = current.position().plus(i);
            nodeList.add(new Node(current, nextPosition, nextPosition.manhattan(end)));
        }
        return nodeList;
    }

    private static void openNodes(PriorityQueue<Node> open, Map<Position, Node> closed, List<Node> toAdd) {
        for (Node i : toAdd) {
            if (!closed.containsKey(i.position())) {
                open.add(i);
            }
        }
    }

    private static void closeBestNode(PriorityQueue<Node> open, Map<Position, Node> closed, int[][] matrix, Position endPosition) {
        Node best = open.poll();
        if (best != null) {
            closed.put(best.position(), best); // Poner en cerrados
            openNodes(open, closed, neighbors(matrix, best, endPosition)); // Agrega a lista sus hijos
        } else {
            System.out.println("?");
        }
    }

    public static void main(String[] args) {
        Position startPosition = new Position(1, 1);
        Position endPosition = new Position(3, 3);

        PriorityQueue<Node> open = new PriorityQueue<>();
        Map<Position, Node> closed = new HashMap<>();

        int[][] matrix = {{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 0, 0}};

        Node start = new Node(null, startPosition, startPosition.manhattan(endPosition));

        open.add(start);
        closeBestNode(open,closed,matrix,endPosition);
        closeBestNode(open,closed,matrix,endPosition);
        closeBestNode(open,closed,matrix,endPosition);

        System.out.println("Abiertos: ");
        for (Node i : open) {
            System.out.println(i + ", ");
        }
        System.out.println("Cerrados: ");
        for (Node i : closed.values()) {
            System.out.println(i + ", ");
        }
    }
}