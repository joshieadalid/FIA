package agentes;

import java.util.*;

import static agentes.AgentFunctions.typeEmpty;
import static agentes.AgentFunctions.typeSpacecraft;

public class AStar {
    private final int[][] matrix;
    private final Position start;
    private final Position end;

    public AStar(int[][] matrix, Position start, Position end) {
        this.matrix = matrix;
        this.start = start;
        this.end = end;
    }

    public static void printPath(List<Position> path) {
        if (path != null) {
            System.out.println("Path found:");
            for (Position position : path) {
                System.out.println(position);
            }
        } else {
            System.out.println("Path not found");
        }
    }

    public static void main(String[] args) {
        int[][] matrix = {
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0}};
        Position startPosition = new Position(0, 0);
        Position endPosition = new Position(11, 11);
        AStar resolver = new AStar(matrix, startPosition, endPosition);
        List<Position> path = resolver.findPath();
        printPath(path);
    }

    public List<Position> findPath() {
        PriorityQueue<Node> open = new PriorityQueue<>();
        Map<Position, Node> closed = new HashMap<>();

        Node startNode = new Node(null, start, start.manhattan(end));
        open.add(startNode);

        while (!open.isEmpty()) {
            Node current = open.poll();

            if (current.position().equals(end)) {
                // Se encontró la solución
                return buildPath(current);
            }

            List<Node> neighbors = getNeighbors(current);

            for (Node neighbor : neighbors) {
                if (!closed.containsKey(neighbor.position())) {
                    open.add(neighbor);
                    closed.put(neighbor.position(), neighbor);
                }
            }
        }

        // No se encontró la solución
        return null;
    }

    private List<Node> getNeighbors(Node node) {
        Position pos = node.position();
        List<Node> neighbors = new ArrayList<>(4);
        int[][] dirMatrix = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] dir : dirMatrix) {
            Position nextPos = pos.plus(new Position(dir[0], dir[1]));
            boolean isTypeEmpty = AgentFunctions.isType(matrix, nextPos, typeEmpty);
            boolean isTypeSpacecraft = AgentFunctions.isType(matrix, nextPos, typeSpacecraft);
            if (isTypeEmpty || isTypeSpacecraft) {
                Node neighbor = new Node(node, nextPos, nextPos.manhattan(end));
                neighbors.add(neighbor);
            }
        }

        return neighbors;
    }


    private List<Position> buildPath(Node endNode) {
        List<Position> path = new ArrayList<>();
        Node current = endNode;

        while (current != null) {
            path.add(0, current.position());
            current = current.parent();
        }

        return path;
    }

}
