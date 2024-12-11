package com.stephthedev.adventofcode.events2024;

import com.stephthedev.adventofcode.utils.FileParserUtil;

import java.io.IOException;
import java.util.*;

public class Day6 {

    public List<Tuple> part1(Grid grid) {
        List<Tuple> route = new ArrayList<>();
        for (int row = 0; row<grid.rowSize(); row++) {
            for (int col = 0; col<grid.columnSize(); col++) {
                if (grid.at(row, col) == '^') {
                    Tuple current = new Tuple(new Position(row, col), Direction.NORTH);
                    moveToNextSpot(grid, current, route);
                }
            }
        }
        System.out.println(route);
        return route;
    }

    public long part2(Grid grid, List<Tuple> knownGoodRoute) {
        int count = 0;
        for (int i=0; i<knownGoodRoute.size() - 1; i++) {
            Tuple currentTuple = knownGoodRoute.get(i);
            Tuple tupleToBlock = knownGoodRoute.get(i + 1);

            List<Position> positionsToNow = knownGoodRoute.subList(0, i).stream().map(Tuple::position).toList();
            int lastKnownPosition = positionsToNow.indexOf(tupleToBlock.position);
            if (lastKnownPosition > -1 && lastKnownPosition < i) {
                continue;
            }

            Tuple nextTuple = grid.findNextValidUnblockedLocationRotateOnly(currentTuple);
            //TODO: This doesn't work as expected. Oh well
            if (moveToNextSpot(grid, nextTuple, new ArrayList<>())) {
                count += 1;
            };
        }
        return count;
    }

    private boolean moveToNextSpot(Grid grid, Tuple current, List<Tuple> route) {
        if (current.position.row < 0 || current.position.row >= grid.rowSize()) {
            return false;
        } else if (current.position.col < 0 || current.position.col >= grid.columnSize()) {
            return false;
        } else if (route.contains(current)) {
            return true;
        }

        route.add(current);

        Tuple nextTuple = grid.findNextValidUnblockedLocationForwardOrRotateRight(current);

        return moveToNextSpot(grid, nextTuple, route);
    }

    record Grid(char[][] grid) {

        int rowSize() {
            return grid.length;
        }

        int columnSize() {
            return grid[0].length;
        }

        char at(int row, int col) {
            return grid[row][col];
        }

        boolean isPositionBlocked(Position position) {
            if (position.row < 0 || position.row >= grid.length) {
                return false;
            } else if (position.col < 0 || position.col >= grid[0].length) {
                return false;
            } else return grid[position.row][position.col] == '#';
        }

        Tuple findNextValidUnblockedLocationForwardOrRotateRight(Tuple tuple) {
            Tuple nextTuple = tuple.moveForward();
            if (isPositionBlocked(nextTuple.position)) {
                //Rotate in place until a solution can be found
                for (int i = 0; i<3; i++) {
                    nextTuple = tuple.rotateInPlace(i + 1).moveForward();
                    if (!isPositionBlocked(nextTuple.position)) {
                        break;
                    }

                    if (i == 2) {
                        throw new IllegalStateException("Unable to find a next valid location");
                    }
                }
            }
            return nextTuple;
        }

        Tuple findNextValidUnblockedLocationRotateOnly(Tuple tuple) {
            Tuple nextTuple = null;
            for (int i = 0; i<3; i++) {
                nextTuple = tuple.rotateInPlace(i + 1).moveForward();
                if (!isPositionBlocked(nextTuple.position)) {
                    break;
                }

                if (i == 2) {
                    throw new IllegalStateException("Unable to find a next valid location");
                }
            }
            return nextTuple;
        }
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

    record Tuple(Position position, Direction direction) {

        Tuple rotateInPlace(int numberOfTimes) {
            Direction rotatedDir = direction.rotate(numberOfTimes);
            if (rotatedDir.equals(direction)) {
                throw new IllegalStateException("Rotated in place 360 degrees");
            }
            return new Tuple(position, rotatedDir);
        }

        Tuple moveForward() {
            return new Tuple(
                    position.getNextPosition(direction),
                    direction
            );
        }
    }

    enum Direction  {
        NORTH, EAST, SOUTH, WEST;

        Direction rotate(int numberOfTimes) {
            int mod = (this.ordinal() + numberOfTimes) % 4;
            return Direction.values()[mod];
        }
    }

    record MoveResult(Tuple validMoveLocation, List<Tuple> obstaclesEncountered) {
    }

    public static void main(String[] args) throws IOException {
        Grid grid = new Grid(FileParserUtil.parseFile("2024/day6_input.txt"));
        Day6 day6 = new Day6();

        List<Tuple> course = day6.part1(grid);
        System.out.println("Part 1: " + course.stream().map(Tuple::position).distinct().count());

        long totalLoops = day6.part2(grid, course);
//        Too high: 3239 and 2057 and 1991
        // Not correct: 1654, 1863, 1950
        System.out.println("Part 2: " + totalLoops);

    }
}
