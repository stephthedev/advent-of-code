package com.stephthedev.adventofcode.events2024;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

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
                .filter(this::isSafe)
                .count();
    }

    public long countFixableSafeReports(List<List<Integer>> reports) {
        return reports.stream()
                .map(this::fixReport)
                .filter(this::isSafe)
                .count();
    }

    boolean isSafe(List<Integer> report) {
        for (int i=1; i<report.size(); i++) {
            if ((i+1) < report.size()) {
                int left = report.get(i - 1);
                int middle = report.get(i);
                int right = report.get(i + 1);

                if ((left < middle && isWithinTolerance(middle - left))
                        && (middle < right && isWithinTolerance(right - middle))) {
                    //TODO:
                } else if ((left > middle && isWithinTolerance(left - middle))
                        && (middle > right && isWithinTolerance(middle - right))) {
                    //TODO
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    List<Integer> fixReport(List<Integer> report) {
        int ascendingCount = 0, descendingCount = 0;

        for (int i=1; i<report.size(); i++) {
            int prev = report.get(i - 1);
            int curr = report.get(i);
            if (prev < curr) {
                ascendingCount++;
            } else if (prev > curr) {
                descendingCount++;
            }
        }

        boolean isAscending = ascendingCount > descendingCount;

        Stack<Integer> fixedReport = new Stack<>();
        boolean hasOmissions = false;
        for (int i=1; i<report.size(); i++) {
            int prev = report.get(i - 1);
            int curr = report.get(i);
            int prevCurrDiff = isAscending ? curr - prev : prev - curr;

            if (prevCurrDiff == 0) {
                //Omit duplicates
                hasOmissions = true;
            } else if (isWithinTolerance(prevCurrDiff)) {
                fixedReport.add(prev);
                hasOmissions = true;
            } else if (isAscending && prevCurrDiff < 0) {
                if (!fixedReport.isEmpty() && fixedReport.peek() < prev) {
                    fixedReport.add(prev);
                    hasOmissions = true;
                }
            } else if (!isAscending && prevCurrDiff > 0) {
                if (!fixedReport.isEmpty() && fixedReport.peek() > prev) {
                    fixedReport.add(prev);
                    hasOmissions = true;
                }
            }

            if (i == report.size() - 1) {
                int lastSaved = fixedReport.peek();
                int lastCurrDiff = isAscending ? curr - lastSaved : lastSaved - curr;
                int lastPrevDiff = isAscending ? prev - lastSaved : lastSaved - prev;
                if (isWithinTolerance(lastCurrDiff)) {
                    fixedReport.add(curr);
                } else if (fixedReport.peek() != prev && isWithinTolerance(lastPrevDiff)) {
                    fixedReport.add(prev);
                }
            }
        }

        return fixedReport;
    }


    public boolean isWithinTolerance(int diff) {
        return diff >= 1 && diff <= 3;
    }

    public static void main(String[] args) throws IOException {
        Day2 day2 = new Day2();
        List<List<Integer>> reports = day2.precompute("2024/day2_input.txt");
        long totalSafeReports = day2.countSafeReports(reports);
        System.out.println("Total safe reports: " + totalSafeReports);

        List<Integer> sample = List.of(49, 47, 44, 46, 41);
        System.out.println("Before: " + sample);
        System.out.println("After: " + day2.fixReport(sample));
        long totalSafeFixedReports = day2.countFixableSafeReports(reports);
        System.out.println("Total fixed safe reports: " + totalSafeFixedReports);
    }
}
