package com.stephthedev.adventofcode.events2023;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    public int calculateSumOfTotalNumbersMatched(Map<Integer, Integer> winningNumbersByCardMap) {
        return winningNumbersByCardMap.values().stream()
                .map(totalWinningNumbers -> {
                    int sum = 0;
                    for (int i=0; i<totalWinningNumbers; i++) {
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

    public int calculateTotalCardsWon(Map<Integer, Integer> winningNumbersByCard) {
        Map<Integer, Integer> totalCardsWonByCardNumber = winningNumbersByCard.keySet().stream()
                .collect(Collectors.toMap(cardNumber -> cardNumber, cardNumber -> 1));

        for (int cardNumber : winningNumbersByCard.keySet()) {
            totalCardsWonByCardNumber = calculateTotalScratchCardsWon(cardNumber, winningNumbersByCard, totalCardsWonByCardNumber);
        }

        System.out.println("Final: " + totalCardsWonByCardNumber);
        return totalCardsWonByCardNumber.values().stream().reduce(0, Integer::sum);
    }

    private Map<Integer, Integer> calculateWinningNumbersByCard(List<CardResult> cardResults) {
        Map<Integer, Integer> cardResultMap = new HashMap<>();
        for (int i=0; i<cardResults.size(); i++) {
            CardResult cardResult = cardResults.get(i);
            long totalWinningNumbers = cardResult.actualNumbers
                    .stream()
                    .filter(cardResult.winningNumbers::contains)
                    .count();
            cardResultMap.put(i + 1, (int) totalWinningNumbers);
        }
        return cardResultMap;
    }

    private Map<Integer, Integer> calculateTotalScratchCardsWon(int startingCardNumber, Map<Integer, Integer> winningNumbersByCardMap, Map<Integer, Integer> totalCardsWonByCardNumber) {
        int totalWinningNumbers = winningNumbersByCardMap.get(startingCardNumber);
        for (int i=1; i<=totalWinningNumbers; i++) {
            int nextCard = startingCardNumber + i;
            totalCardsWonByCardNumber.compute(nextCard, (k, previousValue) -> previousValue + 1);

            totalCardsWonByCardNumber = calculateTotalScratchCardsWon(nextCard, winningNumbersByCardMap, totalCardsWonByCardNumber);
        }

        return totalCardsWonByCardNumber;
    }

    record CardResult(Collection<Integer> winningNumbers, Collection<Integer> actualNumbers) {

    }

    public static void main(String[] args) throws IOException {
        Day4 day = new Day4();
        List<CardResult> results = day.precompute(
                "aoc2023/day4_input.txt"
        );
        Map<Integer, Integer> winningNumbersByCard = day.calculateWinningNumbersByCard(results);
        int result = day.calculateSumOfTotalNumbersMatched(winningNumbersByCard);

        //Scratch: 13, Day4: 24733
        System.out.println("Card Point Value: " + result);

        //Scratch: 30, Day4:
        int totalCardsWon = day.calculateTotalCardsWon(winningNumbersByCard);
        System.out.println("Total Cards Won: " + totalCardsWon);
    }
}
