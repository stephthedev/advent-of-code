package com.stephthedev.adventofcode.events2024;

import com.stephthedev.adventofcode.utils.FileParserUtil;

import java.io.IOException;

public class Day6 {

    public int part1(char[][] grid) {
        boolean[][] visited = new boolean[grid.length][grid[0].length];

        for (int row = 0; row<grid.length; row++) {
            for (int col = 0; col<grid[row].length; col++) {
                if (grid[row][col] == '^') {
                    moveToNextSpot(grid, row, col, Direction.NORTH, visited);
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

    private boolean moveToNextSpot(char[][] grid, int row, int col, Direction direction, boolean[][] visited) {
        if (row < 0 || row >= grid.length) {
            return false;
        } else if (col < 0 || col >= grid[row].length) {
            return false;
        }

        System.out.printf("(%d, %d) => %s \n", row, col, grid[row][col]);
        visited[row][col] = true;
        Direction nextDirection = getNextDirection(grid, row, col, direction);
        if (nextDirection == Direction.NORTH) {
            return moveToNextSpot(grid, row - 1, col, nextDirection, visited);
        } else if (nextDirection == Direction.EAST) {
            return moveToNextSpot(grid, row, col + 1, nextDirection, visited);
        } else if (nextDirection == Direction.SOUTH) {
            return moveToNextSpot(grid, row + 1, col, nextDirection, visited);
        } else if (nextDirection == Direction.WEST) {
            return moveToNextSpot(grid, row, col - 1, nextDirection, visited);
        } else {
            throw new IllegalStateException("This shouldn't happen");
        }
    }

    Direction getNextDirection(char[][] grid, int row, int col, Direction direction) {
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

        if (nextRow < 0 || nextRow >= grid.length) {
            return direction;
        } else if (nextCol < 0 || nextCol >= grid[0].length) {
            return direction;
        } else if (grid[nextRow][nextCol] == '#') {
            return switch(direction) {
                case NORTH -> Direction.EAST;
                case EAST -> Direction.SOUTH;
                case SOUTH -> Direction.WEST;
                case WEST -> Direction.NORTH;
            };
        }

        return direction;
    }

    enum Direction  {
        NORTH, EAST, SOUTH, WEST
    }

    public static void main(String[] args) throws IOException {
        char[][] grid = FileParserUtil.parseFile("2024/day6_input.txt");
        Day6 day6 = new Day6();

        int distinctGridCount = day6.part1(grid);
        System.out.println("GRID: " + distinctGridCount);
    }
}
