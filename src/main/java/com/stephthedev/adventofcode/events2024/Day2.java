package com.stephthedev.adventofcode.events2024;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day2 {

    public List<List<Integer>> precompute(String inputFile) throws IOException {
        List<List<Integer>> reports = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/" + inputFile)))) {
            String line = reader.readLine();

            while (line != null) {
                String[] tokens = line.trim().split("\\s+");
                List<Integer> levels = Arrays.stream(tokens).map(Integer::parseInt).toList();
                reports.add(levels);
                line = reader.readLine();
            }
        }

        return reports;
    }

    public long countSafeReports(List<List<Integer>> reports) {
        return reports.stream()
                .filter(report -> {
                    Boolean isIncreasing = null;
                    for (int i=0; i<report.size(); i++) {
                        int currNumber = report.get(i);
                        if (i + 1 < report.size()) {
                            int nextNumber = report.get(i + 1);
                            if (isIncreasing == null) {
                                isIncreasing = (nextNumber - currNumber) > 0;
                            }
                            int diff = Math.abs(nextNumber - currNumber);
                            if (!(diff >= 1 && diff <= 3)) {
                                return false;
                            } else if (isIncreasing && !(nextNumber > currNumber)) {
                                return false;
                            } else if (!isIncreasing && !(nextNumber < currNumber)) {
                                return false;
                            }
                        }
                    }

                    System.out.printf("Report %s is safe \n", report);
                    return true;
                })
                .count();
    }

    public static void main(String[] args) throws IOException {
        Day2 day2 = new Day2();
        List<List<Integer>> reports = day2.precompute("2024/day2_input.txt");
        long totalSafeReports = day2.countSafeReports(reports);
        System.out.println("Total safe reports: " + totalSafeReports);
    }
}
