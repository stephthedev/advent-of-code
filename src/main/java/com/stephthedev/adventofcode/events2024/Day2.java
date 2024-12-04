package com.stephthedev.adventofcode.events2024;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.IntStream;

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

    public long countFixableSafeReports(boolean isHacky, List<List<Integer>> reports) {
        return reports.stream()
                .map(report -> isHacky ? fixReportHacky(report) : fixReport(report))
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

    public void compareNonHackyVsHackySolution(List<List<Integer>> reports) {
        for (List<Integer> report : reports) {
            List<Integer> fixedReport = fixReport(report);
            List<Integer> hackyReport = fixReportHacky(report);

            if (!isSafe(fixedReport) && isSafe(hackyReport)) {
                if (!fixedReport.equals(hackyReport)) {
                    System.out.println("Discrepancy found for report: " + report + ". Expected " + fixedReport + " but was " + hackyReport);
                }
            }
        }
    }

    List<Integer> fixReportHacky(List<Integer> report) {
        List<List<Integer>> reportsWithOneElementRemoved = IntStream.range(0, report.size())
                .mapToObj(index -> {
                    List<Integer> dupe = new ArrayList<>(report);
                    dupe.remove(index);
                    return dupe;
                })
                .toList();

        return reportsWithOneElementRemoved.stream()
                .filter(this::isSafe)
                .findFirst()
                .orElse(report);
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
        for (int i=1; i<report.size(); i++) {
            int prev = report.get(i - 1);
            int curr = report.get(i);
            int prevCurrDiff = isAscending ? curr - prev : prev - curr;

            if (isWithinTolerance(prevCurrDiff)) {
                fixedReport.add(prev);
            } else if (isAscending) {
                //The numbers suddenly decreased, check if the next number is increasing as well, if so, omit adding prev
                if (prevCurrDiff < 0 && i < report.size() - 1) {
                    int next = report.get(i + 1);
                    if (!isWithinTolerance(next - curr)) {
                        fixedReport.add(prev);
                    }
                } else if (prevCurrDiff > 3) {
                    //A wide jump was encountered, check to see if the previous saved value is enough to preserve prev
                    if (!fixedReport.isEmpty()) {
                        int last = fixedReport.peek();
                        if (isWithinTolerance(prev - last)) {
                            fixedReport.add(prev);
                        }
                    }
                }
            } else {
                //The numbers suddenly decreased, check if the next number is decreasing as well and maybe use that instead
                if (prevCurrDiff > 0 && i < report.size() - 1) {
                    int next = report.get(i + 1);
                    if (!isWithinTolerance(curr - next)) {
                        fixedReport.add(prev);
                    }
                } else if (prevCurrDiff > 3) {
                    //A wide jump was encountered, check to see if the previous saved value is enough to preserve prev
                    if (!fixedReport.isEmpty()) {
                        int last = fixedReport.peek();
                        if (isWithinTolerance(last - prev)) {
                            fixedReport.add(prev);
                        }
                    }
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

        if (report.size() - fixedReport.size() > 1) {
            return report;
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

        long totalSafeFixedReports = day2.countFixableSafeReports(false, reports);
        System.out.println("Total fixed safe reports: " + totalSafeFixedReports);

        long hackySolution = day2.countFixableSafeReports(true, reports);
        System.out.println("(Hacky) Total fixed safe reports: " + hackySolution);

        day2.compareNonHackyVsHackySolution(reports);
    }
}
