package com.stephthedev.adventofcode.events2021;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Day5 {

    public List<Line> parseData(String fileName) throws FileNotFoundException, IOException {
        List<String> linesData = Util.parseFileAsStringArray(fileName);
        List<Line> lines = new ArrayList<>(linesData.size());
        for (String line : linesData) {
            String[] coordPairStrArr = line.split("->");
            Coordinate c1 = parseStrAsCoordinate(coordPairStrArr[0]);
            Coordinate c2 = parseStrAsCoordinate(coordPairStrArr[1]);
            lines.add(new Line(c1, c2));

        }
        return lines;
    }

    public void plotHorizontalVerticalLines(List<Line> lines, int[][] graph) {
        //For each line, determine if it's horizontal or vertical
        for (Line line : lines) {
            if (line.isHorizontal()) {
                //If it's horizontal, the y remains the same
                for (int i=line.getXMin(); i<=line.getXMax(); i++) {
                    graph[line.c1.y][i] += 1;
                }
            } else if (line.isVertical()) {
                //If it's vertical, the x remains the same
                for (int i=line.getYMin(); i<=line.getYMax(); i++) {
                    graph[i][line.c1.x] += 1;
                }
            }
        }
    }

    public void plotDiagonalLines(List<Line> lines, int[][] graph) {
        //For each line, determine if it's horizontal or vertical
        for (Line line : lines) {
            if (line.isHorizontal() || line.isVertical()) {
                continue;
            }

            int xRun = line.c2.x - line.c1.x;
            int yRun = line.c2.y - line.c1.y;
            int xSlope = xRun > 0 ? 1 : -1;
            int ySlope = yRun > 0 ? 1 : -1;
            //1. Take the first coordinate
            //2. Increment the x coordinate
            //3. Either increase/decrease the y coordinate (depending on the slope)
            //Coordinate c = null;
            for (int i=0; i<=Math.abs(xRun); i++) {
                int x = (int) (line.c1.x + (i * xSlope));
                int y = (int) (line.c1.y + (i * ySlope));
                graph[x][y] += 1;

				/*//TODO: Remove - debugging
				c = new Coordinate(x, y);
				if (i == 0) {
					if (!line.c1.equals(c)) {
						System.out.println("Expected starting coordinate of " + line.c1 + " but found " + c);
						return;
					}
				} else if (i == absoluteRun) {
					if (!line.c2.equals(c)) {
						System.out.println("Expected ending coordinate of " + line.c2 + " but found " + c);
						return;
					}
				}*/
            }
        }
    }

    int countOverlappingPoints(int[][] graph) {
        int count = 0;
        for (int i=0; i<graph.length; i++) {
            for (int j=0; j<graph[i].length; j++) {
                if (graph[i][j] > 1) {
                    count++;
                }
            }
        }
        return count;
    }

    private Coordinate parseStrAsCoordinate(String pairStr) {
        String[] xyPairArr = pairStr.split(",");
        Coordinate coordinate = new Coordinate();
        coordinate.x = Integer.valueOf(xyPairArr[0].trim());
        coordinate.y = Integer.valueOf(xyPairArr[1].trim());
        return coordinate;
    }

    long[] run(String fileName) throws FileNotFoundException, IOException {
        //Parse the data as lines
        List<Line> lines = parseData(fileName);
        //int dimension = getMaxDimension(lines);
        int dimension = 1000;
        int[][] graph = new int[dimension][dimension];

        //Plot the horizontal and vertical lines
        plotHorizontalVerticalLines(lines, graph);
        long partOne = countOverlappingPoints(graph);

        //Plot the diagonal lines
        plotDiagonalLines(lines, graph);
        long partTwo = countOverlappingPoints(graph);

        return new long[] {partOne, partTwo};
    }

    static class Line {
        Coordinate c1;
        Coordinate c2;

        Line(Coordinate point1, Coordinate point2) {
            this.c1 = point1;
            this.c2 = point2;
        }

        int getXMin() {
            return Math.min(c1.x, c2.x);
        }

        int getXMax() {
            return Math.max(c1.x, c2.x);
        }

        int getYMin() {
            return Math.min(c1.y, c2.y);
        }

        int getYMax() {
            return Math.max(c1.y, c2.y);
        }

        boolean isHorizontal() {
            return c2.y == c1.y;
        }

        boolean isVertical() {
            return c2.x == c1.x;
        }

        @Override
        public String toString() {
            return String.format("(%s) -> (%s)", c1, c2);
        }
    }

    static class Coordinate {
        int x;
        int y;

        Coordinate() {
            x = 0;
            y = 0;
        }
        Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return String.format("(%d, %d)", x, y);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }

            Coordinate c = (Coordinate)o;
            return this.x == c.x && this.y == c.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    public static void main(String[] args) throws IOException {
        Day5 day = new Day5();
        long[] solution = day.run("src/main/resources/day5_input.txt");
        System.out.println(Arrays.toString(solution));
    }
}
