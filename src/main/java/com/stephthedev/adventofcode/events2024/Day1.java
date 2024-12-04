package com.stephthedev.adventofcode.events2024;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class Day1 {

    public LocationList precompute(String inputFile) throws IOException {
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/" + inputFile)))) {
            String line = reader.readLine();
            while (line != null) {
                String[] tokens = line.trim().split("\\s+");
                list1.add(Integer.parseInt(tokens[0].trim()));
                list2.add(Integer.parseInt(tokens[1].trim()));
                line = reader.readLine();
            }
        }

        Collections.sort(list1);
        Collections.sort(list2);

        return new LocationList(list1, list2);
    }

    public int findTotalDistance(LocationList locations) {
        int totalDistance = 0;
        for (int i=0; i<locations.list1.size(); i++) {
            int location1 = locations.list1.get(i);
            int location2 = locations.list2.get(i);

            totalDistance += Math.abs(location1 - location2);
        }

        return totalDistance;
    }

    public long findSimilarityScore(LocationList locations) {
        Map<Integer, Long> locationCountById = locations.list2.stream()
                .collect(groupingBy(Function.identity(), counting()));

        return locations.list1.stream()
                .map(locationId -> locationId * locationCountById.getOrDefault(locationId, 0L))
                .reduce(0L, Long::sum);
    }

    record LocationList(List<Integer> list1, List<Integer> list2) {
    }

    public static void main(String[] args) throws IOException {
        Day1 day1 = new Day1();
        LocationList locationList = day1.precompute("2024/day1_input.txt");
        int totalDistance = day1.findTotalDistance(locationList);
        System.out.println("Day 1 Total Distance: " + totalDistance);

        long similarityScore = day1.findSimilarityScore(locationList);
        System.out.println("Day 2 Similarity Score: " + similarityScore);
    }
}
