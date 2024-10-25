package com.stephthedev.adventofcode.events2023;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day2 {

    private static final String COUNT_COLOR_PATTERN = "([0-9]+) (red|green|blue)";

    private List<GameResult> getCountsByGame(String inputFile) throws IOException {
        List<GameResult> results = new ArrayList<>();

        Pattern countColorPattern = Pattern.compile(COUNT_COLOR_PATTERN);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/" + inputFile)))) {
            String line = reader.readLine();

            while (line != null) {
                int gameId = Integer.parseInt(line.split(":")[0].substring(5).trim());

                List<Subset> subsets = Stream.of(line.substring(line.indexOf(":") + 1).split(";"))
                        .map(subset -> {
                            Map<String, Integer> colorCountMap = new HashMap<>();
                            colorCountMap.put("red", 0);
                            colorCountMap.put("green", 0);
                            colorCountMap.put("blue", 0);

                            List.of(subset.split(","))
                                .forEach(set -> {
                                    Matcher matcher = countColorPattern.matcher(set.trim());
                                    if (matcher.matches()) {
                                        int count = Integer.parseInt(matcher.group(1));
                                        String color = matcher.group(2);
                                        colorCountMap.put(color, count);
                                    }
                                });

                            return new Subset(
                                    colorCountMap.getOrDefault("red", 0),
                                    colorCountMap.getOrDefault("green", 0),
                                    colorCountMap.getOrDefault("blue", 0)
                            );

                        })
                        .toList();

                results.add(new GameResult(gameId, subsets));

                line = reader.readLine();
            }
        }

        return results;
    }

    public void calculate(String inputFile, Map<String, Integer> bagLimitationMap) throws IOException {
        List<GameResult> results = getCountsByGame(inputFile);

        Integer sum = results.stream()
                .filter(result -> result.subsets.stream().allMatch(subset -> subset.redCount <= bagLimitationMap.get("red") &&
                        subset.blueCount <= bagLimitationMap.get("blue") &&
                        subset.greenCount <= bagLimitationMap.get("green"))
                )
                .map(GameResult::gameId)
                .reduce(0, Integer::sum);

        System.out.println("Game Id Sum: " + sum);
    }

    private record Subset(int redCount, int greenCount, int blueCount) {

    }

    private record GameResult(int gameId, List<Subset> subsets) {

    }

    public static void main(String[] args) throws IOException {
        Day2 day2 = new Day2();
        day2.calculate(
                "day2_input.txt",
                Map.of(
                        "red", 12,
                        "green", 13,
                        "blue", 14
                )
        );
    }
}
