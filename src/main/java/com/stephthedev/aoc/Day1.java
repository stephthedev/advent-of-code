package com.stephthedev.aoc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day1 {

    private List<Long> ingest(String fileName) throws FileNotFoundException, IOException {
        List<String> lines = Util.parseFileAsStringArray(fileName);
        if (lines == null || lines.size() == 0) {
            System.err.println("Failed to read any lines");
        }
        return lines.stream().map(s -> Long.parseLong(s)).collect(Collectors.toList());
    }

    private int countDepthIncreases(List<Long> depths) {
        long prevDepth = depths.get(0);
        int count = 0;
        for (int i=1; i<depths.size(); i++) {
            if (depths.get(i) > prevDepth) {
                count++;
            }
            prevDepth = depths.get(i);
        }
        return count;
    }

    private int count3WindowDepthIncreases(List<Long> depths) {
        int count = 0;
        long prevDepth = depths.get(0) + depths.get(1) + depths.get(2);
        for (int i=1; i<depths.size(); i++) {
            if (i < depths.size() - 2) {
                long newDepth = depths.get(i) + depths.get(i+1) + depths.get(i+2);
                if (newDepth > prevDepth) {
                    count++;
                }
                prevDepth = newDepth;
            }
        }
        return count;
    }

    public long[] run(String fileName) throws IOException {
        List<Long> dataset = ingest(fileName);
        int part1 = countDepthIncreases(dataset);
        int part2 = count3WindowDepthIncreases(dataset);
        return new long[]{part1, part2};
    }

    public static void main(String[] args) throws IOException {
        Day1 day = new Day1();
        long[] solution = day.run("src/main/resources/day1_input.txt");
        System.out.println(Arrays.toString(solution));
    }
}
