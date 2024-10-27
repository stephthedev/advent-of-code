package com.stephthedev.adventofcode.events2023;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Day3 {

    public int calculate(String inputFile) throws IOException {
        int totalSum = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/" + inputFile)))) {
            String line = reader.readLine();

            while (line != null) {
                line = reader.readLine();
            }
        }

        return totalSum;
    }

    public static void main(String[] args) throws IOException {
        Day3 day3 = new Day3();
        day3.calculate(
                "day3_input.txt"
        );
    }
}
