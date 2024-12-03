package com.stephthedev.adventofcode.events2021;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * https://adventofcode.com/2021/day/7
 */
public class Day7 {

    private Map<Integer, Integer> summationMemo = new HashMap<>();

    public List<Integer> ingest(String fileName) throws FileNotFoundException, IOException {
        List<String> linesData = Util.parseFileAsStringArray(fileName);
        String[] numbersArr = linesData.get(0).split(",");
        List<Integer> numbers = new ArrayList<>(numbersArr.length);
        for (String numberStr : numbersArr) {
            int number = Integer.valueOf(numberStr);
            numbers.add(number);
        }
        return numbers;
    }

    //Calculate fuel consumption - constant
    int runPartOne(List<Integer> numbers) {
        //1. Group the crabs by horizontal distance
        Map<Integer, Integer> horizontalCountMap = new HashMap<>();
        for (int number : numbers) {
            if (horizontalCountMap.containsKey(number)) {
                horizontalCountMap.put(number, horizontalCountMap.get(number) + 1);
            } else {
                horizontalCountMap.put(number, 1);
            }
        }

        //2. Compare to any distance between 0 and the max crab horizontal position
        int totalFuelUsed = Integer.MAX_VALUE;
        int maxPosition = Collections.max(horizontalCountMap.keySet());
        for (int i=0; i<maxPosition; i++) {
            int fuelUsed = 0;
            for (Integer horizontal : horizontalCountMap.keySet()) {
                fuelUsed += Math.abs((horizontal - i) * horizontalCountMap.get(horizontal));
            }
            totalFuelUsed = Math.min(fuelUsed, totalFuelUsed);
        }

        return totalFuelUsed;
    }

    //Calculate fuel consumption - constant
    int runPartTwo(List<Integer> numbers) {
        //1. Group the crabs by horizontal distance
        Map<Integer, Integer> horizontalCountMap = new HashMap<>();
        for (int number : numbers) {
            if (horizontalCountMap.containsKey(number)) {
                horizontalCountMap.put(number, horizontalCountMap.get(number) + 1);
            } else {
                horizontalCountMap.put(number, 1);
            }
        }

        //2. Compare to any distance between 0 and the max crab horizontal position
        int totalFuelUsed = Integer.MAX_VALUE;
        int maxPosition = Collections.max(horizontalCountMap.keySet());
        for (int i=0; i<maxPosition; i++) {
            int fuelUsed = 0;
            for (Integer horizontal : horizontalCountMap.keySet()) {
                int distance = Math.abs(horizontal - i);
                fuelUsed += summation(distance) * horizontalCountMap.get(horizontal);
            }
            totalFuelUsed = Math.min(fuelUsed, totalFuelUsed);
        }

        return totalFuelUsed;
    }

    private int summation(int number) {
        if (number == 0 ) {
            return 0;
        } else if (number == 1) {
            return 1;
        }
        else if (!summationMemo.containsKey(number)) {
            int sum = 0;
            for (int i=1; i<=number; i++) {
                sum += i;
            }
            summationMemo.put(number, sum);
        }
        return summationMemo.get(number);
    }

    public long[] run(String fileName) throws FileNotFoundException, IOException {
        List<Integer> school = ingest(fileName);
        long part1 = runPartOne(school);
        long part2 = runPartTwo(school);
        return new long[]{part1, part2};
    }

    public static void main(String[] args) throws IOException {
        Day7 day = new Day7();
        long[] solution = day.run("src/main/resources/day7_input.txt");
        System.out.println(Arrays.toString(solution));
    }
}
