package com.stephthedev.adventofcode.events2024;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

public class Day3 {

    private static Pattern MULTIPLY_PATTERN = Pattern.compile("mul\\(([0-9]+),([0-9]+)\\)");
    private static Pattern SWITCH_PATTERN = Pattern.compile("do(n't)?\\(\\)?");

    List<Map<Integer, Token>> precompute(String inputFile) throws IOException {
        List<Map<Integer, Token>> allTokensByIndex = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/" + inputFile)))) {
            String line = reader.readLine();

            while (line != null) {
                Map<Integer, Instruction> instructionByIndex = MULTIPLY_PATTERN.matcher(line).results()
                    .collect(Collectors.toMap(
                            MatchResult::start,
                            matchResult -> {
                                int left = Integer.parseInt(matchResult.group(1));
                                int right = Integer.parseInt(matchResult.group(2));
                                return new Instruction(left, right);
                            }
                ));

                Map<Integer, Switch> switchByIndex = SWITCH_PATTERN.matcher(line).results()
                        .collect(Collectors.toMap(
                           MatchResult::start,
                           matchResult -> {
                               if (matchResult.group().startsWith("don't")) {
                                   return Switch.DONT;
                               } else {
                                   return Switch.DO;
                               }
                           }
                        ));

                Map<Integer, Token> tokenByIndex =  Stream.of(switchByIndex.keySet(), instructionByIndex.keySet())
                    .flatMap(Collection::stream)
                    .collect(Collectors.toMap(
                        Function.identity(),
                        index -> {
                            if (instructionByIndex.containsKey(index)) {
                                return new Token(instructionByIndex.get(index), null);
                            } else {
                                return new Token(null, switchByIndex.get(index));
                            }
                        }
                    ));

                allTokensByIndex.add(tokenByIndex);
                line = reader.readLine();
            }
        }

        return allTokensByIndex;
    }

    public int sumAllMultiplications(List<Map<Integer, Token>> results) {
        return results.stream()
                .map(tokenByIndexMap -> tokenByIndexMap.keySet()
                        .stream()
                        .map(tokenByIndexMap::get)
                        .filter(Token::isInstruction)
                        .map(Token::instruction)
                        .map(Instruction::multiply)
                        .toList()
                )
                .flatMap(Collection::stream)
                .reduce(0, Integer::sum);
    }

    public int sumOnlyEnabledInstructions(List<Map<Integer, Token>> results) {
        AtomicReference<Switch> prevSwitch = new AtomicReference<>(Switch.DO);
        return results.stream()
                .map(tokenByIndex -> {
                    List<Integer> sortedIndices = tokenByIndex.keySet().stream().sorted().toList();
                    List<Instruction> enabledInstructions = new ArrayList<>();
                    for (int i = 0; i< sortedIndices.size(); i++) {;
                        int currIndex = sortedIndices.get(i);
                        Token result = tokenByIndex.get(currIndex);
                        if (result.isSwitch()) {
                            prevSwitch.set(result.conditionalSwitch);
                        } else if (result.isInstruction() && prevSwitch.get() == Switch.DO) {
                            enabledInstructions.add(result.instruction());
                        }
                    }
                    return enabledInstructions;
                })
                .flatMap(Collection::stream)
                .map(Instruction::multiply)
                .reduce(0, Integer::sum);

    }

    record Instruction (int left, int right) {
        int multiply() {
            return left * right;
        }
    }

    enum Switch { DO, DONT }

    record Token(Instruction instruction, Switch conditionalSwitch) {

        boolean isSwitch() {
            return conditionalSwitch != null;
        }

        boolean isInstruction() {
            return instruction != null;
        }
    }

    public static void main(String[] args) throws IOException {
        Day3 day3 = new Day3();
        //Day 3: 181345830
        List<Map<Integer, Token>> results = day3.precompute("2024/day3_input.txt");

        Map<String, List<Switch>> switches = results.stream()
                .map(Map::values)
                .flatMap(Collection::stream)
                .filter(Token::isSwitch)
                .map(Token::conditionalSwitch)
                .collect(groupingBy(Switch::name));

        int part1Result = day3.sumAllMultiplications(results);
        System.out.println("Part 1: " + part1Result);

        //Day 3: (Not 107307267 (too high))
        int part2Result = day3.sumOnlyEnabledInstructions(results);
        System.out.println("Part 2: " + part2Result);
    }
}
