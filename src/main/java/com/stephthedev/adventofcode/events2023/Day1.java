package com.stephthedev.adventofcode.events2023;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Day1 {

    public int calculate(String inputFile) throws IOException {
        int totalSum = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/" + inputFile)))) {
            String line = reader.readLine();

            while (line != null) {
                List<Character> numbers = line.chars()
                        .mapToObj(i -> (char) i)
                        .filter(Character::isDigit)
                        .toList();

                int firstNumberFound = numbers.stream()
                        .findFirst()
                        .map(num -> Integer.parseInt(String.valueOf(num)))
                        .orElseThrow(() -> new IllegalStateException("Expected to find a number on line"));

                int lastNumberFound = numbers.stream()
                        .reduce((first, second) -> second)
                        .map(num -> Integer.parseInt(String.valueOf(num)))
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
        int result = day1.calculate("day1_input.txt");
        System.out.println("Result: " + result);
    }
}
