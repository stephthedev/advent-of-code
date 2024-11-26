package com.stephthedev.adventofcode.events2023;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Day5 {

    private static final Pattern RANGE_PATTERN = Pattern.compile("(([0-9]+) ([0-9]+) ([0-9]+))");
    private static final Pattern MAP_PATTERN = Pattern.compile("([a-z]+-to-[a-z]+) map:.*");
    private static final Pattern SEEDS_PATTERN = Pattern.compile("seeds: [0-9]");

    public Almanac precompute(String inputFile) throws IOException {
        List<Long> seeds = new ArrayList<>();
        List<AlmanacLine> seedToSoilRange = new ArrayList<>();
        List<AlmanacLine> soilToFertilizerRange = new ArrayList<>();
        List<AlmanacLine> fertilizerToWaterRange = new ArrayList<>();
        List<AlmanacLine> waterToLightRange = new ArrayList<>();
        List<AlmanacLine> lightToTemperatureRange = new ArrayList<>();
        List<AlmanacLine> temperatureToHumidityRange = new ArrayList<>();
        List<AlmanacLine> humidityToLocationRange = new ArrayList<>();

        AlmanacMapType almanacMapType = AlmanacMapType.NONE;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/" + inputFile)))) {
            String line = reader.readLine();

            while (line != null) {
                if (line.startsWith("seeds: ")) {
                    seeds = Arrays.stream(line.split(": ")[1].split(" "))
                            .map(Long::parseLong)
                            .toList();
                } else if (line.contains(":")) {
                    almanacMapType = MAP_PATTERN.matcher(line.trim())
                            .results()
                            .findFirst()
                            .map(matchResult -> matchResult.group(1))
                            .map(AlmanacMapType::findByMapping)
                            .orElse(AlmanacMapType.NONE);
                } else if (!line.trim().isEmpty()) {
                    List<AlmanacLine> lines = RANGE_PATTERN.matcher(line)
                            .results()
                            .map(matchResult -> new AlmanacLine(
                                    Long.parseLong(matchResult.group(2)),
                                    Long.parseLong(matchResult.group(3)),
                                    Long.parseLong(matchResult.group(4))
                            ))
                            .toList();

                    switch (almanacMapType) {
                        case SEED_TO_SOIL_MAP -> seedToSoilRange.addAll(lines);
                        case SOIL_TO_FERTILIZER_MAP -> soilToFertilizerRange.addAll(lines);
                        case FERTILIZER_TO_WATER_MAP -> fertilizerToWaterRange.addAll(lines);
                        case WATER_TO_LIGHT_MAP -> waterToLightRange.addAll(lines);
                        case LIGHT_TO_TEMPERATURE_MAP -> lightToTemperatureRange.addAll(lines);
                        case TEMPERATURE_TO_HUMIDITY_MAP -> temperatureToHumidityRange.addAll(lines);
                        case HUMIDITY_TO_LOCATION_MAP -> humidityToLocationRange.addAll(lines);
                    }
                }
                line = reader.readLine();
            }
        };

        return new Almanac(
                seeds,
                seedToSoilRange,
                soilToFertilizerRange,
                fertilizerToWaterRange,
                waterToLightRange,
                lightToTemperatureRange,
                temperatureToHumidityRange,
                humidityToLocationRange
        );
    }

    public long findLowestLocation(Collection<Long> seeds, Almanac almanac) {
        Map<Long, Long> seedToLocationMap = seeds.stream()
                .collect(Collectors.toMap(
                        seed -> seed,
                        seed -> almanac.seedToSoilRange.stream()
                                .map(almanacLine -> almanacLine.getDestinationForSource(seed))
                                .filter(Optional::isPresent)
                                .map(Optional::get)
                                .findFirst()
                                .orElse(seed)  //seed-soil
                ))
                /*.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        seedSoilEntry -> almanac.soilToFertilizerRange.stream()
                                .map(almanacLine -> almanacLine.getDestinationForSource(seedSoilEntry.getValue()))
                                .filter(Optional::isPresent)
                                .map(Optional::get)
                                .findFirst()
                                .orElse(seedSoilEntry.getValue())  //seed-fertilizer
                ))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        seedFertilizerEntry -> almanac.fertilizerToWaterRange.stream()
                                .map(almanacLine -> almanacLine.getDestinationForSource(seedFertilizerEntry.getValue()))
                                .filter(Optional::isPresent)
                                .map(Optional::get)
                                .findFirst()
                                .orElse(seedFertilizerEntry.getValue())  //seed-water
                ))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        seedLightEntry -> almanac.waterToLightRange.stream()
                                .map(almanacLine -> almanacLine.getDestinationForSource(seedLightEntry.getValue()))
                                .filter(Optional::isPresent)
                                .map(Optional::get)
                                .findFirst()
                                .orElse(seedLightEntry.getValue())  //seed-light
                ))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        seedLightEntry -> almanac.lightToTemperatureRange.stream()
                                .map(almanacLine -> almanacLine.getDestinationForSource(seedLightEntry.getValue()))
                                .filter(Optional::isPresent)
                                .map(Optional::get)
                                .findFirst()
                                .orElse(seedLightEntry.getValue())  //seed-temp
                ))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        seedTempEntry -> almanac.temperatureToHumidityRange.stream()
                                .map(almanacLine -> almanacLine.getDestinationForSource(seedTempEntry.getValue()))
                                .filter(Optional::isPresent)
                                .map(Optional::get)
                                .findFirst()
                                .orElse(seedTempEntry.getValue())  //seed-humidity
                ))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        seedHumidityEntry -> almanac.humidityToLocationRange.stream()
                                .map(almanacLine -> almanacLine.getDestinationForSource(seedHumidityEntry.getValue()))
                                .filter(Optional::isPresent)
                                .map(Optional::get)
                                .findFirst()
                                .orElse(seedHumidityEntry.getValue())  //seed-humidity
                ))*/;
        System.out.println("SEED-LOCATION: " + seedToLocationMap);
        return seedToLocationMap.values()
                .stream()
                .mapToLong(v -> v)
                .min()
                .orElse(Long.MIN_VALUE);

    }

    record Almanac(List<Long> seeds,
                   Collection<AlmanacLine> seedToSoilRange,
                   Collection<AlmanacLine> soilToFertilizerRange,
                   Collection<AlmanacLine> fertilizerToWaterRange,
                   Collection<AlmanacLine> waterToLightRange,
                   Collection<AlmanacLine> lightToTemperatureRange,
                   Collection<AlmanacLine> temperatureToHumidityRange,
                   Collection<AlmanacLine> humidityToLocationRange) {
    }

    record AlmanacLine(long destinationRangeStart, long sourceRangeStart, long rangeLength ) {

        Optional<Long> getDestinationForSource(long source) {
            long sourceRangeEnd = sourceRangeStart + rangeLength;
            if (source >= sourceRangeStart && source <= sourceRangeEnd) {
                long delta = source - sourceRangeStart;
                long possibleDestination = destinationRangeStart + delta;
                long destinationRangeEnd = destinationRangeStart + rangeLength;
                if (possibleDestination >= destinationRangeStart && possibleDestination <= destinationRangeEnd) {
                    //System.out.println("Found destination: " + possibleDestination);
                    return Optional.of(possibleDestination);
                }
            }

            return Optional.empty();
        }
    }

    enum AlmanacMapType {
        SEEDS("seeds"),
        SEED_TO_SOIL_MAP("seed-to-soil"),
        SOIL_TO_FERTILIZER_MAP("soil-to-fertilizer"),
        FERTILIZER_TO_WATER_MAP("fertilizer-to-water"),
        WATER_TO_LIGHT_MAP("water-to-light"),
        LIGHT_TO_TEMPERATURE_MAP("light-to-temperature"),
        TEMPERATURE_TO_HUMIDITY_MAP("temperature-to-humidity"),
        HUMIDITY_TO_LOCATION_MAP("humidity-to-location"),
        NONE("none");

        String label;
        AlmanacMapType(String label) {
            this.label = label;
        }

        public static AlmanacMapType findByMapping(String mapping) {
            return Stream.of(AlmanacMapType.values())
                    .filter(value -> value.label.equals(mapping))
                    .findFirst()
                    .orElse(null);
        }
    }

    public static void main(String[] args) throws IOException {
        Day5 day5 = new Day5();
        Almanac almanac = day5.precompute("aoc2023/day5_input.txt");

        long part1Lowest = day5.findLowestLocation(almanac.seeds, almanac);
        //Scratch: 35, Day5: 282277027
        System.out.println("Part 1 Lowest Location: " + part1Lowest);

        long previousSeedStart = 0;
        long maxBatchSize = 1000;
        List<Long> lowestLocations = new ArrayList<>();
        for (int i=0; i<almanac.seeds.size(); i++) {
            if (i % 2 == 0) {
                previousSeedStart = almanac.seeds.get(i);
            } else {
                long range = almanac.seeds.get(i);
                int totalBatches = (int) (range / maxBatchSize);
                
                
                kdk
                
                long lowestLocation = IntStream.rangeClosed(1, totalBatches)
                        .mapToLong(batch -> {
                            long upperRange = previousSeedStart + (batch * maxBatchSize);
                        })
                        .min();
//                long lowestLocation = day5.findLowestLocation(Collections.emptyList(), almanac);
//                lowestLocations.add(lowestLocation);

            }
        }
        long part2Lowest = lowestLocations.stream().mapToLong(v -> v).min().orElse(Long.MIN_VALUE);
        System.out.println("Part 2 Lowest Location: " + part2Lowest);
    }
}
