package com.stephthedev.adventofcode.events2021;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day9 {

    private int[][] ingest(String fileName) throws IOException {
        List<String> lines = Util.parseFileAsStringArray(fileName);
        int[][] lavaFlow = new int[lines.size()][lines.get(0).length()];
        int count = 0;
        for (String line : lines) {
            for (int i=0; i<line.length(); i++) {
                //TODO: WHy isn't this working with a split on a number regex?
                int value = Integer.valueOf(line.charAt(i) + "");
                lavaFlow[count][i] = value;
            }
            count++;
        }

        return lavaFlow;
    }

    Part1Result findLowPoints(int[][] lavaFlows) {
        List<int[]> lowPoints = new ArrayList<>();

        long risk = 0;
        for (int i=0; i<lavaFlows.length; i++) {
            for (int j=0; j<lavaFlows[i].length; j++) {
                int curr = lavaFlows[i][j];
                int left = j-1 >= 0 ? lavaFlows[i][j-1] : -1;
                int right = j+1 < lavaFlows[i].length ? lavaFlows[i][j+1] : -1;
                int above = i-1 >= 0 ? lavaFlows[i-1][j] : -1;
                int below = i+1 < lavaFlows.length ? lavaFlows[i+1][j] : -1;

                if (((above == -1) || above > curr) && ((right == -1) || right > curr) &&
                        ((below == -1) || below > curr) && ((left == -1) || left > curr)) {
                    risk += (1 + curr);
                    lowPoints.add(new int[]{i, j});
                }
            }
        }

        Part1Result result = new Part1Result();
        result.lowPoints = lowPoints;
        result.risk = risk;
        return result;
    }

    long findThreeLargestBasins(List<int[]> lowPoints) {
        return -1;
    }

    long[] run(String fileName) throws FileNotFoundException, IOException {
        int[][] lavaFlows = ingest(fileName);
        Part1Result result = findLowPoints(lavaFlows);
        return new long[]{result.risk, -1};
    }

    private class Part1Result {
        public List<int[]> lowPoints;
        public long risk;
    }

    public static void main(String[] args) throws IOException {
        Day9 day = new Day9();
        long[] solution = day.run("src/main/resources/aoc2021/day9_input.txt");
        System.out.println(Arrays.toString(solution));
    }
}
