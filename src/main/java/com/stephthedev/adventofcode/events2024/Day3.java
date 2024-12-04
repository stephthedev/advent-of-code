package com.stephthedev.adventofcode.events2024;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Day3 {

    private static Pattern MULTIPLY_PATTERN = Pattern.compile("mul\\(([0-9]+),([0-9]+)\\)");

    List<Instruction> precompute(String inputFile) throws IOException {
        List<Instruction> instructions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/" + inputFile)))) {
            String line = reader.readLine();

            while (line != null) {
                instructions.addAll
                    (MULTIPLY_PATTERN.matcher(line).results()
                    .map(matchResult -> {
                        int left = Integer.parseInt(matchResult.group(1));
                        int right = Integer.parseInt(matchResult.group(2));
                        return new Instruction(left, right);
                    })
                    .toList()
                );
                line = reader.readLine();
            }
        }

        return instructions;
    }

    public int sumAllMultiplications(List<Instruction> instructions) {
        return instructions.stream().map(Instruction::multiply).reduce(0, Integer::sum);
    }

    record Instruction (int left, int right) {
        int multiply() {
            return left * right;
        }
    }

    public static void main(String[] args) throws IOException {
        Day3 day3 = new Day3();
        List<Instruction> instructions = day3.precompute("2024/day3_input.txt");

        int part1Result = day3.sumAllMultiplications(instructions);
        System.out.println(part1Result);
    }
}
