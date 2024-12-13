package com.stephthedev.adventofcode.events2024;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static java.util.function.Predicate.not;

public class Day7 {

    List<Equation> precompute(String inputFile) throws IOException {
        List<Equation> equations = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/" + inputFile)))) {
            String line = reader.readLine();

            while (line != null) {
                String[] tokens = line.split(":");
                String[] operandsStr = tokens[1].split("\\s+");

                long result = Long.parseLong(tokens[0]);
                List<Long> operands = Arrays.stream(operandsStr)
                        .map(String::trim)
                        .filter(not(String::isEmpty))
                        .map(Long::parseLong)
                        .toList();

                equations.add(new Equation(result, operands));
                line = reader.readLine();
            }
        }

        return equations;
    }

    public long part1(List<Equation> equations) {
        return equations.stream()
                .filter(equation -> isSolvableRecursive(equation, 0, equation.operands.get(0)))
                .map(Equation::result)
                .reduce(0L, Long::sum);
    }

    public void diff(List<Equation> equations) {
        equations.stream()
                .filter(equation -> {
                    boolean nonRecursive = isSolvable(equation);
                    boolean recursive = isSolvableRecursive(equation, 0, equation.operands.get(0));
                    return recursive && !nonRecursive;
                })
                .forEach(System.out::println);
    }

    boolean isSolvable(Equation equation) {
        long resultSoFar = equation.result;
        boolean isDivide = false;
        for (int i=equation.operands.size() - 1; i>=0; i--) {
            isDivide = false;
            long operand = equation.operands.get(i);
            if (resultSoFar % operand == 0) {
                resultSoFar /= operand;
                isDivide = true;
            } else if (resultSoFar >= operand){
                resultSoFar -= operand;
            } else {
                return false;
            }
        }

        if (isDivide && resultSoFar == 1) {
            return true;
        } else return resultSoFar == 0;
    }

    boolean isSolvableRecursive(Equation equation, int index, long currentResult) {
        if (currentResult > equation.result) {
            return false;
        } else if (index == equation.operands.size() - 1) {
            return equation.result == currentResult;
        }

        long operand = equation.operands.get(index + 1);
        if (isSolvableRecursive(equation, index + 1, currentResult + operand)) {
            return true;
        } else return isSolvableRecursive(equation, index + 1, currentResult * operand);
    }

    record Equation(Long result, List<Long> operands) {

    }

    public static void main(String[] args) throws IOException {
        Day7 day7 = new Day7();
        List<Equation> equations = day7.precompute("2024/day7_input.txt");
        long result = day7.part1(equations);
        System.out.println("Part 1: " + result);
    }
}
