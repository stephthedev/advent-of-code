package com.stephthedev.adventofcode.events2023;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Pattern;

public class Day4 {

    private final Pattern NUMBER_PATTERN = Pattern.compile("(\\d+)");

    public List<CardResult> precompute(String inputFile) throws IOException {
        List<CardResult> results = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/" + inputFile)))) {
            String line = reader.readLine();
            while (line != null) {
                int colonIndex = line.indexOf(":");
                String[] card = line.substring(colonIndex + 1).split("\\|");
                List<Integer> winningNumbers = NUMBER_PATTERN.matcher(card[0])
                        .results()
                        .map(matchResult -> Integer.parseInt(matchResult.group()))
                        .toList();

                List<Integer> actualNumbers = NUMBER_PATTERN.matcher(card[1])
                        .results()
                        .map(matchResult -> Integer.parseInt(matchResult.group()))
                        .toList();

                results.add(new CardResult(winningNumbers, actualNumbers));

                line = reader.readLine();
            }
        }

        return results;
    }

    public int calculateMatchingNumbersSum(List<CardResult> cardResults) {
        return cardResults.stream()
                .map(cardResult -> {
                     List<Integer> matchingNumbers = cardResult.actualNumbers
                            .stream()
                            .filter(cardResult.winningNumbers::contains)
                             .toList();
                     int sum = 0;
                     for (int i=0; i<matchingNumbers.size(); i++) {
                         if (i == 0) {
                             sum += 1;
                         } else {
                             sum = sum * 2;
                         }
                     }

                     return sum;
                })
                .reduce(0, Integer::sum);
    }

    record CardResult(Collection<Integer> winningNumbers, Collection<Integer> actualNumbers) {

    }

    public static void main(String[] args) throws IOException {
        Day4 day = new Day4();
        List<CardResult> results = day.precompute(
                "day4_input.txt"
        );
        int result = day.calculateMatchingNumbersSum(results);

        System.out.println("Card Point Value: " + result);
    }
}
