package com.stephthedev.adventofcode.events2024;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Day6Test {

    @Test
    void test1() {
        Day6 day6 = new Day6();
        Day6.Grid grid = toGrid(
"""
.....
..#..
..^#.
.....
""");
        List<Day6.Tuple> route = day6.part1(grid);
        List<Day6.Tuple> expectedRoute = List.of(
                new Day6.Tuple(new Day6.Position(2, 2), Day6.Direction.NORTH),
                new Day6.Tuple(new Day6.Position(3, 2), Day6.Direction.SOUTH)
        );
        Assertions.assertEquals(expectedRoute, route);
    }

    @Test
    void test2() {
        Day6 day6 = new Day6();
        Day6.Grid grid = toGrid("""
.##..
....#
.....
.^.#.
.....
""");
        List<Day6.Tuple> route = day6.part1(grid);
        long result = day6.part2(grid, route);
        Assertions.assertEquals(1L, result);
    }

    @Test
    void test3() {
        Day6 day6 = new Day6();
        Day6.Grid grid = toGrid("""
.#..#.
......
#.....

""");
        List<Day6.Tuple> route = day6.part1(grid);
        long result = day6.part2(grid, route);
        Assertions.assertEquals(1L, result);
    }


    Day6.Grid toGrid(String result) {
        String[] lines = result.split("\n");
        char[][] grid = new char[lines.length][lines[0].trim().length()];
        for (int i=0; i<lines.length; i++) {
            String line = lines[i].trim();
            grid[i] = line.toCharArray();
        }
        return new Day6.Grid(grid);
    }
}
