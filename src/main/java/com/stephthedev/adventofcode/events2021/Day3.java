package com.stephthedev.adventofcode.events2021;

import com.stephthedev.adventofcode.utils.FileParserUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day3 {

    private enum BitCriteriaRating {
        OXYGEN, C02
    }

    //Given a list of binary numbers, process the gamma rate and epsilon rate.
    private int processPowerConsumption(List<String> binaryNumbers) {
        int length = binaryNumbers.get(0).length();

        //Note: To process either the gamma rate or epsilon rate, it's only necessary to
        // count the frequency of 0s OR 1s (because it's a binary number)
        // So, we only only keep track of the number of 1s and infer the number of 0s
        // How? To get the number of 0s, we do: total - (1 frequency) to get a 0 frequency

        //Keep a map of index position and number of ones it accumulates
        Map<Integer, Integer> gammaRateOnlyOnesMap = new HashMap<>(length);
        for (int i=0; i<length; i++) {
            gammaRateOnlyOnesMap.put(i, 0);
        }

        //For each index, increase the count for the number of times a 1 was found
        for (String number : binaryNumbers) {
            for (int i=0; i<number.length(); i++) {
                char digit = number.charAt(i);
                if (digit == '1') {
                    int prevTotal = gammaRateOnlyOnesMap.get(i) + 1;
                    gammaRateOnlyOnesMap.put(i, prevTotal);
                }
            }
        }

        int half = binaryNumbers.size() / 2;
        StringBuilder gammaRateBuilder = new StringBuilder();
        StringBuilder epsilonRateBuilder = new StringBuilder();
        for (int i=0; i<length; i++) {
            int onesTotal = gammaRateOnlyOnesMap.get(i);
            int highestDigit = -1;
            int lowestDigit = -1;

            //If the total number of ones is larger than half the list of numbers (500), it's rate is higher
            if (onesTotal > half) {
                highestDigit = 1;
                lowestDigit = 0;	//We can then infer that 0 is lower
            } else {
                highestDigit = 0;
                lowestDigit = 1;	//We can then infer this
            }
            gammaRateBuilder = gammaRateBuilder.append(highestDigit);
            epsilonRateBuilder = epsilonRateBuilder.append(lowestDigit);
        }
        int gammaRate = binaryStringToDecimal(gammaRateBuilder);
        int epsilonRate = binaryStringToDecimal(epsilonRateBuilder);
        return gammaRate * epsilonRate;
    }

    private int processLifeSupportRating(List<String> binaryNumbers) {
        int oxygenGeneratorRating = processBitCriteria(binaryNumbers, BitCriteriaRating.OXYGEN);
        int co2ScrubberRating = processBitCriteria(binaryNumbers, BitCriteriaRating.C02);
        return oxygenGeneratorRating * co2ScrubberRating;
    }

    private int processBitCriteria(List<String> binaryNumbersAsStrings, BitCriteriaRating rating) {
        int strLength = binaryNumbersAsStrings.get(0).length();
        List<String> numbers = new ArrayList<>();
        numbers.addAll(binaryNumbersAsStrings);
        for (int i=0; i<strLength; i++) {
            numbers = filterArray(numbers, i, rating);
            if (numbers.size() == 1) {
                break;
            }
        }
        return binaryStringToDecimal(numbers.get(0));
    }

    //Filters an array according to the bit criteria
    private List<String> filterArray(List<String> binaryNumbers, int index, BitCriteriaRating rating) {
        long zeroesCount = binaryNumbers.stream().filter(s -> s.charAt(index) == '0').count();
        long onesCount = binaryNumbers.stream().filter(s -> s.charAt(index) == '1').count();
        char c = '1';
        if (rating == BitCriteriaRating.OXYGEN) {
            c = zeroesCount > onesCount ? '0' : '1';
        } else if (rating == BitCriteriaRating.C02) {
            c = zeroesCount <= onesCount ? '0' : '1';
        }
        char oneOrZero = c;
        return binaryNumbers.stream().filter( s -> s.charAt(index) == oneOrZero).collect(Collectors.toList());
    }

    private int binaryStringToDecimal(StringBuilder builder) {
        return binaryStringToDecimal(builder.toString());
    }

    private int binaryStringToDecimal(String binaryStr) {
        int number = Integer.parseInt(binaryStr, 2);
        //System.out.println(String.format("Binary: %s, Number: %d", binaryStr, number));
        return number;
    }

    public long[] run(String fileName) throws FileNotFoundException, IOException {
        List<String> binaryNumbers = FileParserUtil.parseFileAsStringList(fileName);
        int part1 = processPowerConsumption(binaryNumbers);
        int part2 = processLifeSupportRating(binaryNumbers);
        return new long[] {part1, part2};
    }

    public static void main(String[] args) throws IOException {
        Day3 day = new Day3();
        long[] solution = day.run("src/main/resources/aoc2021/day3_input.txt");
        System.out.println(Arrays.toString(solution));
    }
}
