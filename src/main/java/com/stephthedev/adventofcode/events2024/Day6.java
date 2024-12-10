package com.stephthedev.adventofcode.events2024;

import com.stephthedev.adventofcode.utils.FileParserUtil;

import java.io.IOException;

public class Day6 {

    public int part1(char[][] grid) {
        boolean[][] visited = new boolean[grid.length][grid[0].length];

        for (int row = 0; row<grid.length; row++) {
            for (int col = 0; col<grid[row].length; col++) {
                if (grid[row][col] == '^') {
                    moveToNextSpot(grid, new Position(row, col), Direction.NORTH, visited);
                }
            }
        }

        int totalVisited = 0;
        for (int row = 0; row<visited.length; row++) {
            for (int col = 0; col<visited[row].length; col++) {
                totalVisited += visited[row][col] ? 1 : 0;
            }
        }

        return totalVisited;
    }

    private boolean moveToNextSpot(char[][] grid, Position currentPosition, Direction direction, boolean[][] visited) {
        if (currentPosition.row < 0 || currentPosition.row >= grid.length) {
            return false;
        } else if (currentPosition.col < 0 || currentPosition.col >= grid[currentPosition.row].length) {
            return false;
        }

        visited[currentPosition.row][currentPosition.col] = true;
        Position nextPosition = currentPosition.getNextPosition(direction);
        if (isNextPositionBlocked(grid, nextPosition)) {
            nextPosition = currentPosition.getNextPosition(direction.turnRight());
            return moveToNextSpot(grid, nextPosition, direction.turnRight(), visited);
        } else {
            return moveToNextSpot(grid, nextPosition, direction, visited);
        }
    }

    boolean isNextPositionBlocked(char[][] grid, Position nextPosition) {
        if (nextPosition.row < 0 || nextPosition.row >= grid.length) {
            return false;
        } else if (nextPosition.col < 0 || nextPosition.col >= grid[0].length) {
            return false;
        } else return grid[nextPosition.row][nextPosition.col] == '#';
    }

    record Position(int row, int col) {
        Position getNextPosition(Direction direction) {
            int nextRow = row;
            int nextCol = col;

            switch(direction) {
                case NORTH:
                    nextRow -= 1;
                    break;
                case EAST:
                    nextCol += 1;
                    break;
                case SOUTH:
                    nextRow += 1;
                    break;
                case WEST:
                    nextCol -= 1;
                    break;
            }
            return new Position(nextRow, nextCol);
        }
    }

    enum Direction  {
        NORTH, EAST, SOUTH, WEST;

        Direction turnRight() {
            return switch(this) {
                case NORTH -> Direction.EAST;
                case EAST -> Direction.SOUTH;
                case SOUTH -> Direction.WEST;
                case WEST -> Direction.NORTH;
            };
        }
    }

    public static void main(String[] args) throws IOException {
        char[][] grid = FileParserUtil.parseFile("2024/day6_input.txt");
        Day6 day6 = new Day6();

        int distinctGridCount = day6.part1(grid);
        System.out.println("Part 1: " + distinctGridCount);
    }
}
