package com.stephthedev.adventofcode.events2023;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day3 {

    private final Pattern NUMBER_PATTERN = Pattern.compile("(\\d+)");

    private final Pattern SYMBOL_PATTERN = Pattern.compile("([^\\d\\.]+)");

    public int sumRelevantPartNumbers(List<LineResult> lineResults) {
        int sum = 0;
        for (int i=0; i<lineResults.size(); i++) {
            LineResult currentLine = lineResults.get(i);
            LineResult previousLine = (i - 1 > 0) ? lineResults.get(i - 1) : null;
            LineResult nextLine = (i + 1 < lineResults.size()) ? lineResults.get(i + 1) : null;

            List<Integer> matchingNumbers = currentLine.indexNumberMap.keySet().stream()
                    .filter(startingIndexOfNumber -> {
                        int leftIndexToCheck = Math.max(startingIndexOfNumber - 1, 0);
                        int rightIndexToCheck = Math.min(startingIndexOfNumber + currentLine.indexNumberMap.get(startingIndexOfNumber).length(), 141);

                        if (currentLine.hasSymbolExactlyAt(leftIndexToCheck, rightIndexToCheck)) {
                            //Check first for numbers adjacent to symbols on the current line
                            return true;
                        } else //Next, check for numbers adjacent to symbols on the previous line
                            if (previousLine != null && previousLine.hasSymbolInRange(leftIndexToCheck, rightIndexToCheck)) {
                            //Next, check for numbers adjacent to symbols on the previous line
                            return true;
                        } else return nextLine != null && nextLine.hasSymbolInRange(leftIndexToCheck, rightIndexToCheck);
                    })
                    .map(currentLine.indexNumberMap::get)
                    .map(Integer::parseInt)
                    .toList();

            sum += matchingNumbers.stream().reduce(0, Integer::sum);
        }

        return sum;
    }

    public List<LineResult> precompute(String inputFile) throws IOException {
        List<LineResult> lineResults = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/" + inputFile)))) {
            String line = reader.readLine();

            while (line != null) {
                Map<Integer, String> indexNumberMap = NUMBER_PATTERN.matcher(line)
                        .results()
                        .collect(Collectors.toMap(
                                MatchResult::start,
                                MatchResult::group
                        ));

                Map<Integer, String> indexSymbolMap = SYMBOL_PATTERN.matcher(line)
                        .results()
                        .collect(Collectors.toMap(
                                MatchResult::start,
                                MatchResult::group
                        ));

                lineResults.add(new LineResult(indexNumberMap, indexSymbolMap));

                line = reader.readLine();
            }
        }

        return lineResults;
    }

    record LineResult(Map<Integer, String> indexNumberMap, Map<Integer, String> indexSymbolMap) {

        public boolean hasSymbolExactlyAt(int leftIndex, int rightIndex) {
            return indexSymbolMap.containsKey(leftIndex) || indexSymbolMap.containsKey(rightIndex);
        }

        public boolean hasSymbolInRange(int leftIndex, int rightIndex) {
            return IntStream.rangeClosed(leftIndex, rightIndex)
                    .anyMatch(indexSymbolMap::containsKey);
        }
    }

    public static void main(String[] args) throws IOException {
        Day3 day3 = new Day3();
        List<LineResult> lineResults = day3.precompute(
                "day3_input.txt"
        );
        System.out.println("SUM: " + day3.sumRelevantPartNumbers(lineResults));
    }
}
