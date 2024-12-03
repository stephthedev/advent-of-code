package com.stephthedev.adventofcode.events2023;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Day5 {

    private static final Pattern RANGE_PATTERN = Pattern.compile("(([0-9]+) ([0-9]+) ([0-9]+))");
    private static final Pattern MAP_PATTERN = Pattern.compile("([a-z]+-to-[a-z]+) map:.*");

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
                            .map(matchResult -> {
                                long destination = Long.parseLong(matchResult.group(2));
                                long source = Long.parseLong(matchResult.group(3));
                                long rangeLength = Long.parseLong(matchResult.group(4));
                                return new AlmanacLine(
                                        new Range(destination, destination + rangeLength),
                                        new Range(source, source + rangeLength)
                                );
                            })
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


    public long findLowestLocation(List<Range> seeds, Almanac almanac) {
        Map<Range, List<Range>> locationBySeedMap =  seeds.stream()
            .collect(Collectors.toMap(
                    seed -> seed,
                    seed -> getPossibleDestinations(seed, almanac.seedToSoilRange) //seed-soil
            ))
            .entrySet()
            .stream()
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    seedSoilEntry ->  seedSoilEntry.getValue().stream()
                            .map(soilRange -> getPossibleDestinations(soilRange, almanac.soilToFertilizerRange))
                            .flatMap(Collection::stream)
                            .toList() //seed-fertilizer
            ))
            .entrySet()
            .stream()
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    seedFertilizerEntry -> seedFertilizerEntry.getValue().stream()
                            .map(fertilizerRange -> getPossibleDestinations(fertilizerRange, almanac.fertilizerToWaterRange))
                            .flatMap(Collection::stream)
                            .toList()  //seed-water
            ))
            .entrySet()
            .stream()
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    seedWaterEntry -> seedWaterEntry.getValue().stream()
                            .map(waterRange -> getPossibleDestinations(waterRange, almanac.waterToLightRange))
                            .flatMap(Collection::stream)
                            .toList()  //seed-light
            ))
            .entrySet()
            .stream()
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    seedLightEntry -> seedLightEntry.getValue().stream()
                            .map(lightRange -> getPossibleDestinations(lightRange, almanac.lightToTemperatureRange))
                            .flatMap(Collection::stream)
                            .toList()  //seed-temperature
            ))
            .entrySet()
            .stream()
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    seedTempEntry -> seedTempEntry.getValue().stream()
                            .map(tempRange -> getPossibleDestinations(tempRange, almanac.temperatureToHumidityRange))
                            .flatMap(Collection::stream)
                            .toList()  //seed-humidity
            ))
            .entrySet()
            .stream()
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    seedHumidityEntry -> seedHumidityEntry.getValue().stream()
                            .map(humidityRange -> getPossibleDestinations(humidityRange, almanac.humidityToLocationRange))
                            .flatMap(Collection::stream)
                            .toList()  //seed-location
            ))
        ;

        return locationBySeedMap
                .values()
                .stream()
                .flatMap(Collection::stream)
                .mapToLong(Range::start)
                .min()
                .orElse(Long.MIN_VALUE);

    }

    List<Range> getPossibleDestinations(Range sourceRange, Collection<AlmanacLine> sourceToDestinationRange) {
        @SuppressWarnings("unchecked")
        List<Range> possibleDestinations = (List<Range>) sourceToDestinationRange.stream()
                .map(almanacLine -> {
                    if(almanacLine.sourceRange.containsSubRange(sourceRange)) {
                        long startDelta = Math.abs(sourceRange.start - almanacLine.sourceRange.start);
                        long endDelta = Math.abs(sourceRange.end - almanacLine.sourceRange.start);
                        long possibleDestinationStart = almanacLine.destinationRange.start + startDelta;
                        long possibleDestinationEnd = almanacLine.destinationRange.start + endDelta;
                        if (almanacLine.destinationRange.containsSubRange(possibleDestinationStart, possibleDestinationStart)) {
                            return Optional.of(new Range(possibleDestinationStart, possibleDestinationStart));
                        }
                    }
                    return Optional.empty();
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        return possibleDestinations.isEmpty() ? Collections.singletonList(sourceRange) : possibleDestinations;
    }

    record Range(long start, long end) {
        boolean containsSubRange(long start, long end) {
            return start >= this.start && end <= this.end;
        }

        boolean containsSubRange(Range range) {
            return range.start >= this.start && range.end <= this.end;
        }
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

    record AlmanacLine(Range destinationRange, Range sourceRange) {

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
        Almanac almanac = day5.precompute("scratch.txt");

        //Scratch: 35, Day5: 282277027
        List<Range> exactSeedRanges = almanac.seeds.stream().map(seed -> new Range(seed, seed)).toList();
        long part1Lowest = day5.findLowestLocation(exactSeedRanges, almanac);
        System.out.println("Part 1 Lowest Location: " + part1Lowest);

        //Scratch: 35, Day5:
        List<Range> seedRanges = new ArrayList<>();
        for (int i=0; i<almanac.seeds.size(); i++) {
            if ((i % 2) == 1) {
                long seedStart = almanac.seeds.get(i - 1);
                long rangeLength = almanac.seeds.get(i);
                long seedEnd = (seedStart - 1) + rangeLength;
                seedRanges.add(new Range(seedStart, seedEnd));
            }
        }

        //Scratch: 46
        long part2Lowest = day5.findLowestLocation(seedRanges, almanac);
        System.out.println("Part 2 Lowest Location: " + part2Lowest);
    }
}
