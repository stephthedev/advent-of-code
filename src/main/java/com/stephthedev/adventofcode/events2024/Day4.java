package com.stephthedev.adventofcode.events2024;

import com.stephthedev.adventofcode.utils.FileParserUtil;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day4 {

    private static final String XMAS = "XMAS";
    private static final String MAS = "MAS";

    public int part1(char[][] board) {
        List<Coordinate> coordinates = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            coordinates.addAll(searchAndCount(board, XMAS, direction));
        }
        return coordinates.size();
    }

    public long part2(char[][] board) {
        Set<Direction> validDirections = Set.of(Direction.NORTH_EAST, Direction.SOUTH_EAST, Direction.SOUTH_WEST, Direction.NORTH_WEST);

        List<Coordinate> middleLetterCoordinates = new ArrayList<>();

        for (Direction direction : validDirections) {
            searchAndCount(board, MAS, direction)
                    .stream()
                    .map(coordinate -> switch(direction) {
                        case NORTH, SOUTH, EAST, WEST -> null;

                        case NORTH_EAST -> new Coordinate(
                                coordinate.row - 1,
                                coordinate.col + 1
                        );
                        case SOUTH_EAST -> new Coordinate(
                                coordinate.row + 1,
                                coordinate.col + 1
                        );
                        case SOUTH_WEST -> new Coordinate(
                                coordinate.row + 1,
                                coordinate.col - 1
                        );
                        case NORTH_WEST -> new Coordinate(
                                coordinate.row - 1,
                                coordinate.col - 1
                        );
                    })
                    .forEach(middleLetterCoordinates::add);
        }

        long total = middleLetterCoordinates.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .values()
                .stream()
                .filter(count -> count > 1)
                .reduce(0L, Long::sum);

        //TODO: I think this is doubled up counting all the duplicate As, we need to do divide to get the actual number of crosses
        return total / 2L;
    }

    private List<Coordinate> searchAndCount(char[][] board, String word, Direction direction) {
        boolean[][] visited = new boolean[board.length][board[0].length];

        List<Coordinate> startingCoordinates = new ArrayList<>();
        for (int row = 0; row<board.length; row++) {
            for (int col = 0; col<board[row].length; col++) {
                if (board[row][col] == word.charAt(0)) {
                    if (isNextLetterFound(board, row, col, word, 0, visited, direction)) {
                        startingCoordinates.add(new Coordinate(row, col));
                    }
                }
            }
        }

        return startingCoordinates;
    }

    private boolean isNextLetterFound(char[][] board,
                                      int row,
                                      int col,
                                      String word,
                                      int currLetterIndex,
                                      boolean[][] visited,
                                      Direction direction
                                      ) {
        if (currLetterIndex == word.length()) {
            //We made it this far, go ahead and stop
            return true;
        }

        if (row >= board.length || row < 0) {
            return false;
        }

        if (col >= board[0].length || col < 0) {
            return false;
        }

        if (board[row][col] != word.charAt(currLetterIndex)) {
            return false;
        }

        if (visited[row][col]) {
            return false;
        }

        visited[row][col] = true;

        //Search: North, East, South, West, North East, South East, South West, North West
        boolean isFound = switch (direction) {
            case NORTH -> isNextLetterFound(board, row - 1, col, word, currLetterIndex + 1, visited, direction);
            case EAST -> isNextLetterFound(board, row, col + 1, word, currLetterIndex + 1, visited, direction);
            case SOUTH -> isNextLetterFound(board, row + 1, col, word, currLetterIndex + 1, visited, direction);
            case WEST -> isNextLetterFound(board, row, col - 1, word, currLetterIndex + 1, visited, direction);
            case NORTH_EAST -> isNextLetterFound(board, row - 1, col + 1, word, currLetterIndex + 1, visited, direction);
            case SOUTH_EAST -> isNextLetterFound(board, row + 1, col + 1, word, currLetterIndex + 1, visited, direction);
            case SOUTH_WEST -> isNextLetterFound(board, row + 1, col - 1, word, currLetterIndex + 1, visited, direction);
            case NORTH_WEST -> isNextLetterFound(board, row - 1, col - 1, word, currLetterIndex + 1, visited, direction);
        };

        if (isFound) {
            return true;
        }

        visited[row][col] = false;
        return false;
    }

    enum Direction {
        NORTH,
        SOUTH,
        EAST,
        WEST,
        NORTH_EAST,
        SOUTH_EAST,
        SOUTH_WEST,
        NORTH_WEST
    }

    record Coordinate(int row, int col) {
    }

    public static void main(String[] args) throws IOException {
        Day4 day4 = new Day4();
        char[][] crossword = FileParserUtil.parseFile("2024/day4_input.txt");
        int xmasOccurenceCount = day4.part1(crossword);
        System.out.println("Part 1: " + xmasOccurenceCount);

        long part2 = day4.part2(crossword);
        System.out.println("Part 2: " + part2);
    }
}
