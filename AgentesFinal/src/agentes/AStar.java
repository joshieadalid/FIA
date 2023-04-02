package agentes;
import java.util.*;

import static agentes.AgentFunctions.isType;
import static agentes.AgentFunctions.tipoVacio;

public class AStar {
    private final int[][] matrix;
    private final Position goal;

    public AStar(int[][] matrix, Position goal) {
        this.matrix = matrix;
        this.goal = goal;
    }

    private double heuristic(Position pos1, Position pos2) {
        // distancia euclidiana
        return Math.sqrt(Math.pow(pos1.i() - pos2.i(), 2) + Math.pow(pos1.j() - pos2.j(), 2));
    }

    private List<Position> getNeighbors(Position pos) {
        List<Position> neighbors = new ArrayList<>();

        Position up = new Position(pos.i() - 1, pos.j());
        Position down = new Position(pos.i() + 1, pos.j());
        Position left = new Position(pos.i(), pos.j() - 1);
        Position right = new Position(pos.i(), pos.j() + 1);

        if (isType(matrix,up,tipoVacio)) {
            neighbors.add(up);
        }

        if (isType(matrix,down,tipoVacio)) {
            neighbors.add(down);
        }

        if (isType(matrix,left,tipoVacio)) {
            neighbors.add(left);
        }

        if (isType(matrix,right,tipoVacio)) {
            neighbors.add(right);
        }

        return neighbors;
    }

    private List<Position> findPath(Position start) {
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(Node::distance));
        Map<Position, Double> gScore = new HashMap<>();
        Map<Position, Double> fScore = new HashMap<>();
        Map<Position, Position> cameFrom = new HashMap<>();

        gScore.put(start, 0.0);
        fScore.put(start, heuristic(start, goal));

        openSet.add(new Node(start, fScore.get(start)));

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (matrix[current.position().i()][current.position().j()] == 3) {
                // se ha encontrado el nodo objetivo, reconstruir el camino y devolverlo
                return reconstructPath(cameFrom, current.position());
            }

            for (Position neighbor : getNeighbors(current.position())) {
                double tentativeGScore = gScore.getOrDefault(current.position(), Double.POSITIVE_INFINITY) + 1.0;

                if (tentativeGScore < gScore.getOrDefault(neighbor, Double.POSITIVE_INFINITY)) {
                    cameFrom.put(neighbor, current.position());
                    gScore.put(neighbor, tentativeGScore);
                    fScore.put(neighbor, tentativeGScore + heuristic(neighbor, goal));

                    if (!openSet.contains(new Node(neighbor, fScore.get(neighbor)))) {
                        openSet.add(new Node(neighbor,fScore.get(neighbor)));
                    }
                }
            }
        }

        // no se encontró un camino
        return null;
    }

    private static List<Position> reconstructPath(Map<Position, Position> cameFrom, Position current) {
        List<Position> path = new ArrayList<>();
        path.add(current);

        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            path.add(0, current);
        }

        return path;
    }

    public static void main(String[] args) {
        int[][] matrix = {
                {0, 0, 0, 0, 0, 0, 0},
                {0, 1, 1, 1, 1, 1, 0},
                {0, 1, 0, 0, 0, 1, 0},
                {0, 1, 0, 1, 0, 1, 0},
                {0, 1, 0, 0, 0, 1, 0},
                {0, 1, 1, 1, 1, 1, 0},
                {0, 0, 0, 0, 0, 0, 0}
        };

        Position start = new Position(0, 0);
        Position goal = new Position(6, 6);

        AStar pathfinder = new AStar(matrix, goal);
        List<Position> path = pathfinder.findPath(start);

        if (path != null) {
            for (Position pos : path) {
                System.out.println(pos);
            }
        } else {
            System.out.println("No se encontró un camino");
        }
    }
}
