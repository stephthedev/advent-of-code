package com.stephthedev.adventofcode.events2023;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Day1 {

    private static Map<String, Integer> STR_NUM_MAP = Map.of(
        "one", 1,
        "two", 2,
        "three", 3,
        "four", 4,
        "five", 5,
        "six", 6,
        "seven", 7,
        "eight", 8,
        "nine", 9
    );

    private List<Integer> findNumbersFromLine(String line) {
        char[] chars = line.toCharArray();
        StringBuilder builder = new StringBuilder();
        List<Integer> numbersFound = new ArrayList<>();
        for (char alphanum : chars) {
            if (Character.isDigit(alphanum)) {
                numbersFound.add(Integer.parseInt(String.valueOf(alphanum)));
                builder = new StringBuilder();
            } else {
                builder.append(alphanum);
                String strFoundSoFar = builder.toString();
                for (String numKey : STR_NUM_MAP.keySet()) {
                    if (strFoundSoFar.contains(numKey)) {
                        numbersFound.add(STR_NUM_MAP.get(numKey));

                        //Hack: Keep the last character because it might be the first letter of the next word
                        char lastChar = builder.charAt(builder.length() - 1);
                        builder = new StringBuilder();
                        builder.append(lastChar);
                    }
                }
            }
        }

        return numbersFound;
    }

    public int calculate(String inputFile) throws IOException {
        int totalSum = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/" + inputFile)))) {
            String line = reader.readLine();

            while (line != null) {
                List<Integer> numbersFound = findNumbersFromLine(line);

                int firstNumberFound = numbersFound.stream()
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("Expected to find a number on line"));

                int lastNumberFound = numbersFound.stream()
                        .reduce((first, second) -> second)
                        .orElseGet(() -> firstNumberFound);

                int twoDigitNumber = Integer.parseInt(String.format("%d%d", firstNumberFound, lastNumberFound));
                System.out.println("Line: " + line + " --- first: " + firstNumberFound + "; last: " + lastNumberFound + " twoDigit: " + twoDigitNumber);

                totalSum += twoDigitNumber;

                line = reader.readLine();
            }
        }

        return totalSum;
    }

    public static void main(String[] args) throws IOException {
        Day1 day1 = new Day1();
        int result = day1.calculate("2023/day1_input.txt");
        System.out.println("Result: " + result);
    }
}
