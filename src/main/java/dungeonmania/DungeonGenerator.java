package dungeonmania;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.util.Position;

public class DungeonGenerator {
    public static JSONObject generate(int xStart, int yStart, int xEnd, int yEnd) {
        JSONObject dungeon = new JSONObject();

        int xShift = Math.min(xStart, xEnd) - 1;
        int yShift = Math.min(yStart, yEnd) - 1;
        boolean[][] maze = randomisedPrims(xStart - xShift, yStart - yShift, xEnd - xShift, yEnd - yShift);

        JSONArray entities = new JSONArray();
        addWalls(entities, maze, xShift, yShift);
        entities.put(getEntity("player", xStart, yStart));
        entities.put(getEntity("exit", xEnd, yEnd));
        dungeon.put("entities", entities);
        dungeon.put("goal-condition", getGoal());

        return dungeon;
    }

    private static boolean[][] randomisedPrims(int xStart, int yStart, int xEnd, int yEnd) {
        int width = Math.max(xStart, xEnd) + 2;
        int height = Math.max(yStart, yEnd) + 2;
        Random random = new Random();

        boolean[][] maze = new boolean[width][height];
        maze[xStart][yStart] = true;

        List<Position> options = getNeighbours(width, height, xStart, yStart, 2)
            .stream()
            .filter(position -> isWall(maze, position))
            .collect(Collectors.toList());

        while (!options.isEmpty()) {
            Position next = options.remove(random.nextInt(options.size()));

            List<Position> neighbours = getNeighbours(width, height, next, 2)
                .stream()
                .filter(position -> !isWall(maze, position))
                .collect(Collectors.toList());

            if (!neighbours.isEmpty()) {
                Position neighbour = neighbours.get(random.nextInt(neighbours.size()));
                maze[next.getX()][next.getY()] = true;
                maze[(next.getX() + neighbour.getX()) / 2][(next.getY() + neighbour.getY()) / 2] = true;
                maze[neighbour.getX()][neighbour.getY()] = true;
            }

            options.addAll(getNeighbours(width, height, next, 2)
                    .stream()
                    .filter(position -> isWall(maze, position) && !options.contains(position))
                    .collect(Collectors.toList()));
        }

        if (!maze[xEnd][yEnd]) {
            maze[xEnd][yEnd] = true;

            List<Position> neighbours = getNeighbours(width, height, xEnd, yEnd, 1);
            if (neighbours.stream().noneMatch(position -> !isWall(maze, position))) {
                Position neighbour = neighbours.get(random.nextInt(neighbours.size()));
                maze[neighbour.getX()][neighbour.getY()] = true;
            }
        }

        return maze;
    }

    private static List<Position> getNeighbours(int width, int height, int x, int y, int distance) {
        List<Position> neighbours = new ArrayList<>();

        if (x >= distance + 1) {
            neighbours.add(new Position(x - distance, y));
        }
        if (y >= distance + 1) {
            neighbours.add(new Position(x, y - distance));
        }
        if (x < width - (distance + 1)) {
            neighbours.add(new Position(x + distance, y));
        }
        if (y < height - (distance + 1)) {
            neighbours.add(new Position(x, y + distance));
        }

        return neighbours;
    }

    private static List<Position> getNeighbours(int width, int height, Position position, int distance) {
        return getNeighbours(width, height, position.getX(), position.getY(), distance);
    }

    private static boolean isWall(boolean[][] maze, Position position) {
        return !maze[position.getX()][position.getY()];
    }

    private static void addWalls(JSONArray entities, boolean[][] maze, int xShift, int yShift) {
        for (int i = 0; i < maze.length; ++i) {
            for (int j = 0; j < maze[i].length; ++j) {
                if (!maze[i][j]) {
                    entities.put(getEntity("wall", i + xShift, j + yShift));
                }
            }
        }
    }

    private static JSONObject getEntity(String type, int xPosition, int yPosition) {
        JSONObject entity = new JSONObject();
        entity.put("type", type);
        entity.put("x", xPosition);
        entity.put("y", yPosition);
        return entity;
    }

    private static JSONObject getGoal() {
        JSONObject goal = new JSONObject();
        goal.put("goal", "exit");
        return goal;
    }
}
