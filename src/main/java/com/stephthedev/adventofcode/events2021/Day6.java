package com.stephthedev.adventofcode.events2021;

import com.stephthedev.adventofcode.utils.FileParserUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Day6 {

    private List<Integer> ingest(String fileName) throws IOException {
        List<String> data = FileParserUtil.parseFileAsStringList(fileName);
        String[] school = data.get(0).split(",");
        List<Integer> numbers = new ArrayList<>(school.length);
        for (String fish : school) {
            numbers.add(Integer.parseInt(fish));
        }
        return numbers;
    }

    private long runPartOne(List<Integer> school) {
        return reproduce(school, 80);
    }

    private long runPartTwo(List<Integer> school) {
        return reproduce(school, 256);
    }
    private long reproduce(List<Integer> school, int totalDays) {
        //Put everything in age-count map so we can manipulate the values as we iterate through them
        long[] ageCountArr = new long[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
        for (Integer fish : school) {
            ageCountArr[fish] += 1;
        }

        long totalFishSpawned = school.size();
        for (int d=0; d<totalDays; d++) {
            long dailyFishReproduced = 0;
            for (int fishAge=0; fishAge<ageCountArr.length; fishAge++) {
                if (fishAge == 0) {
                    dailyFishReproduced = ageCountArr[0];
                    ageCountArr[0] = 0;
                } else {
                    ageCountArr[fishAge-1] = ageCountArr[fishAge];
                }
            }

            ageCountArr[6] += dailyFishReproduced;
            ageCountArr[8] = dailyFishReproduced;
            totalFishSpawned += dailyFishReproduced;
        }

        return totalFishSpawned;
    }


    public long[] run(String fileName) throws FileNotFoundException, IOException {
        List<Integer> school = ingest(fileName);
        long part1 = runPartOne(school);
        long part2 = runPartTwo(school);
        return new long[]{part1, part2};
    }

    public static void main(String[] args) throws IOException {
        Day6 day = new Day6();
        long[] solution = day.run("src/main/resources/aoc2021/day6_input.txt");
        System.out.println(Arrays.toString(solution));
    }
}
