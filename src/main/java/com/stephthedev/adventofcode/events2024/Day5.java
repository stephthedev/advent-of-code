package com.stephthedev.adventofcode.events2024;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

public class Day5 {

    public PuzzleInput precompute(String inputFile) throws IOException {
        List<Rule> rules = new ArrayList<>();
        List<List<Integer>> pageUpdates = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/" + inputFile)))) {
            String line = reader.readLine();

            while (line != null) {
                if (line.contains("|")) {
                    String[] tokens = line.split("\\|");
                    rules.add(new Rule(
                            Integer.parseInt(tokens[0]),
                            Integer.parseInt(tokens[1])
                    ));
                } else if (line.contains(",")) {
                    String[] tokens = line.split(",");
                    pageUpdates.add(Stream.of(tokens).map(Integer::parseInt).toList());
                }
                line = reader.readLine();
            }
        }

        return new PuzzleInput(rules, pageUpdates);
    }

    public Result sum(PuzzleInput puzzleInput) {
        int part1Sum = 0;
        int part2Sum = 0;
        for (List<Integer> pageUpdate : puzzleInput.pageUpdates) {
            List<Rule> filteredRuleSet = puzzleInput.rules.stream()
                    .filter(rule -> pageUpdate.contains(rule.left) && pageUpdate.contains(rule.right))
                    .toList();

            Optional<Integer> middlePageNumber = getMiddlePageNumber(
                pageUpdate,
                puzzleInput.rules.stream()
                        .filter(rule -> pageUpdate.contains(rule.left) && pageUpdate.contains(rule.right))
                        .toList()
            );

            if (middlePageNumber.isEmpty()) {
                List<Integer> pageUpdateSorted = pageUpdate.stream()
                        .sorted(new RuleComparator(filteredRuleSet).reversed())
                        .toList();
                part2Sum += getMiddlePageNumber(pageUpdateSorted, filteredRuleSet).orElseThrow();
            } else {
                part1Sum += middlePageNumber.get();
            }
        }

        return new Result(part1Sum, part2Sum);
    }

    private Optional<Integer> getMiddlePageNumber(List<Integer> pageUpdate, List<Rule> rules) {
        if (pageUpdate.size() % 2 == 0) {
            throw new UnsupportedOperationException("Expected pageupdate to have an odd amount of numbers");
        }

       List<Rule> filteredRuleSet = rules.stream()
            .filter(rule -> pageUpdate.contains(rule.left) && pageUpdate.contains(rule.right))
            .toList();

        List<Integer> expectedPageOrder = pageUpdate.stream().sorted(new RuleComparator(filteredRuleSet).reversed()).toList();

        Map<Integer, Integer> indexByPageNumber = new HashMap<>();
        for (int i=0; i<expectedPageOrder.size(); i++) {
            indexByPageNumber.put(expectedPageOrder.get(i), i);
        }

        int prevIndex = indexByPageNumber.get(pageUpdate.get(0));
        for (int i=1; i<pageUpdate.size(); i++) {
            int pageNumber = pageUpdate.get(i);
            if (indexByPageNumber.get(pageNumber) < prevIndex) {
                return Optional.empty();
            }
            prevIndex = indexByPageNumber.get(pageNumber);
        }

        int middleIndex = (int) ((double) (pageUpdate.size() / 2));
        return Optional.of(pageUpdate.get(middleIndex));
    }

    record Rule(int left, int right) {
    }

    record PuzzleInput(List<Rule> rules, List<List<Integer>> pageUpdates) {
    }

    record Result(int part1, int part2) {

    }

    class RuleComparator implements Comparator<Integer> {

        private Map<Integer, Set<Integer>> rules;

        RuleComparator(List<Rule> specificRules) {
            this.rules = specificRules.stream()
                    .collect(groupingBy(Rule::left))
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> entry.getValue().stream().map(Rule::right).collect(Collectors.toSet())
                    ));
        }

        @Override
        public int compare(Integer page1, Integer page2) {
            if (page1.equals(page2)) {
                return 0;
            }

            if (!rules.containsKey(page1)) {
                return -1;
            } else if (!rules.containsKey(page2)) {
                return 1;
            }

            if (rules.get(page1).contains(page2)) {
                return 1;
            } else if (rules.get(page2).contains(page1)) {
                return -1;
            }

            if (!rules.containsKey(page1)) {
                return -1;
            } else if (!rules.containsKey(page2)) {
                return 1;
            }
            return 0;
        }
    }

    public static void main(String[] args) throws IOException {
        Day5 day = new Day5();
        PuzzleInput puzzleInput = day.precompute("2024/day5_input.txt");
        Result result = day.sum(puzzleInput);
        System.out.println("PART 1: " + result.part1);
        System.out.println("PART 2: " + result.part2);
    }
}
