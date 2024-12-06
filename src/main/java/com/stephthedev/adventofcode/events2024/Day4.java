package com.stephthedev.adventofcode.events2024;

import com.stephthedev.adventofcode.utils.FileParserUtil;

import java.io.IOException;

public class Day4 {

    public int part1(char[][] board, String word) {
        int count = 0;
        for (Direction direction : Direction.values()) {
            count += searchAndCount(board, word, direction);
        }

        return count;
    }

    private int searchAndCount(char[][] board, String word, Direction direction) {
        boolean[][] visited = new boolean[board.length][board[0].length];

        int count = 0;
        for (int row = 0; row<board.length; row++) {
            for (int col = 0; col<board[row].length; col++) {
                if (board[row][col] == word.charAt(0)) {
                    if (isNextLetterFound(board, row, col, word, 0, visited, direction)) {
                        System.out.printf("Found word at [%d, %d] going %s \n", row, col, direction);
                        count++;
                    }
                }
            }
        }

        return count;
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

    public static void main(String[] args) throws IOException {
        Day4 day4 = new Day4();
        char[][] crossword = FileParserUtil.parseFile("2024/day4_input.txt");
        int xmasOccurenceCount = day4.part1(crossword, "XMAS");
        System.out.println("Part 1: " + xmasOccurenceCount);
    }
}
