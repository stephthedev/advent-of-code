package com.stephthedev.adventofcode.events2024;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

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

        return new LocationList(list1, list2);
    }

    public int findTotalDistance(LocationList locations) {
        List<Integer> list1Sorted = locations.list1.stream().sorted().toList();
        List<Integer> list2Sorted = locations.list2.stream().sorted().toList();

        int totalDistance = 0;
        for (int i=0; i<list1Sorted.size(); i++) {
            int location1 = list1Sorted.get(i);
            int location2 = list2Sorted.get(i);

            totalDistance += Math.abs(location1 - location2);
        }

        return totalDistance;
    }

    record LocationList(List<Integer> list1, List<Integer> list2) {
    }

    public static void main(String[] args) throws IOException {
        Day1 day1 = new Day1();
        LocationList locationList = day1.precompute("2024/day1_input.txt");
        int totalDistance = day1.findTotalDistance(locationList);
        System.out.println("Day 1 Total Distance: " + totalDistance);
    }
}
