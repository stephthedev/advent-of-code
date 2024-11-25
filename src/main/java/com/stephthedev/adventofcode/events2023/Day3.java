package com.stephthedev.adventofcode.events2023;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day3 {

    private final Pattern NUMBER_PATTERN = Pattern.compile("(\\d+)");

    private final Pattern SYMBOL_PATTERN = Pattern.compile("([^\\d\\.]+)");

    public List<LineResult> precompute(String inputFile) throws IOException {
        List<LineResult> lineResults = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/" + inputFile)))) {
            String line = reader.readLine();
            while (line != null) {
                Map<Integer, Set<Integer>> partNumberIndicesMap = NUMBER_PATTERN.matcher(line)
                        .results()
                        .collect(Collectors.toMap(
                            MatchResult::start,
                            matchResult -> Integer.parseInt(matchResult.group())
                        ))
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getValue,
                                entry -> {
                                    Set<Integer> indices = new HashSet<>();
                                    int partNumberLength = Integer.toString(entry.getValue()).length();
                                    for (int i=0; i<partNumberLength; i++) {
                                        indices.add(entry.getKey() + i);
                                    }
                                    return indices;
                                },
                                (first, second) -> {
                                    first.addAll(second);
                                    return first;
                                }
                        ));

                Map<Integer, String> indexSymbolMap = SYMBOL_PATTERN.matcher(line)
                        .results()
                        .collect(Collectors.toMap(
                                MatchResult::start,
                                MatchResult::group
                        ));

                lineResults.add(new LineResult(
                        line.length(),
                        partNumberIndicesMap,
                        indexSymbolMap
                ));

                line = reader.readLine();
            }
        }

        return lineResults;
    }

    public Result sumRelevantPartNumbers(List<LineResult> lineResults) {
        int totalSumOfPartNumbers = 0;
        int totalSumOfGears = 0;
        for (int i=0; i<lineResults.size(); i++) {
            LineResult currentLine = lineResults.get(i);
            LineResult previousLine = (i - 1 >= 0) ? lineResults.get(i - 1) : null;
            LineResult nextLine = (i + 1 < lineResults.size()) ? lineResults.get(i + 1) : null;

            totalSumOfPartNumbers += findSumOfValidPartNumbersForLine(
                Optional.ofNullable(previousLine),
                currentLine,
                Optional.ofNullable(nextLine)
            );

            totalSumOfGears += findSumOfValidGears(
                Optional.ofNullable(previousLine),
                currentLine,
                Optional.ofNullable(nextLine)
            );
        }

        return new Result(
                totalSumOfPartNumbers,
                totalSumOfGears
        );
    }

    private int findSumOfValidGears(Optional<LineResult> previousLine,
                                   LineResult currentLine,
                                   Optional<LineResult> nextLine) {
        return currentLine.indexSymbolMap.entrySet().stream()
            .filter(entrySet -> entrySet.getValue().equals("*"))
            .map(Map.Entry::getKey)
            .map(gearIndex -> Stream.of(
                                    previousLine
                                            .map(lineResult -> lineResult.findPartNumberInRange(gearIndex))
                                            .orElse(null),
                                    currentLine.findPartNumberInRange(gearIndex),
                                    nextLine
                                            .map(lineResult -> lineResult.findPartNumberInRange(gearIndex))
                                            .orElse(null)
                            )
                            .filter(Objects::nonNull)
                            .flatMap(Collection::stream)
                            .collect(Collectors.toSet()))
            .filter(numbersInRange -> numbersInRange.size() == 2)
            .map(matches -> matches.stream().reduce(1, (a, b) -> a * b))
            .reduce(0, Integer::sum);
    }

    private int findSumOfValidPartNumbersForLine(Optional<LineResult> previousLine, LineResult currentLine, Optional<LineResult> nextLine) {
        return currentLine.partIndicesMap.entrySet().stream()
                .filter(entry -> entry.getValue().stream().anyMatch(indexNumber -> {
                    if (currentLine.hasSymbolInRange(indexNumber)) {
                        //Check first for numbers adjacent to symbols on the current line
                        return true;
                    } else if (previousLine.map(lineResult -> lineResult.hasSymbolInRange(indexNumber)).orElse(false)) {
                        //Next, check for numbers adjacent to symbols on the previous line
                        return true;
                    } else {
                        return nextLine.map(lineResult -> lineResult.hasSymbolInRange(indexNumber)).orElse(false);
                    }
                }))
                .map(Map.Entry::getKey)
                .reduce(0, Integer::sum);
    }

    record LineResult(int lineSize,
                      Map<Integer, Set<Integer>> partIndicesMap,
                      Map<Integer, String> indexSymbolMap) {

        public boolean hasSymbolInRange(int index) {
            return IntStream.rangeClosed(index - 1, index + 1)
                    .anyMatch(indexSymbolMap::containsKey);
        }

        public Collection<Integer> findPartNumberInRange(int index) {
            return partIndicesMap.entrySet().stream()
                    .filter(entrySet -> IntStream.rangeClosed(index - 1, index + 1)
                            .anyMatch(computedIndex -> entrySet.getValue().contains(computedIndex)))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toSet());
        }
    }

    record Result(int part1, int part2) {
    }

    public static void main(String[] args) throws IOException {
        Day3 day3 = new Day3();
        List<LineResult> lineResults = day3.precompute(
                "aoc2023/day3_input.txt"
        );
        Result result = day3.sumRelevantPartNumbers(lineResults);
        //Scratch: 4361, 467835
        //Day 3: 520019, 75519888
        System.out.println("SUM part numbers: " + result.part1);
        System.out.println("SUM gears: " + result.part2);
    }
}
