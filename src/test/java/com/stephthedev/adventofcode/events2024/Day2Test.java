package com.stephthedev.adventofcode.events2024;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

public class Day2Test {

    @ParameterizedTest
    @MethodSource("reports")
    void edgeCases_areValid(Tuple tuple) {
        Day2 day2 = new Day2();
        boolean isSafe = day2.isSafe(tuple.report);
        Assertions.assertFalse(isSafe, "The test should not be safe");

        List<Integer> fixedReport = day2.fixReport(tuple.report);
        System.out.printf("Before: %s, After: %s", tuple.report, fixedReport);
        Assertions.assertEquals(tuple.fixedReport, fixedReport, "Expected a different subset for the fixed report");

        isSafe = day2.isSafe(fixedReport);
        Assertions.assertTrue(isSafe, "The report should be safe now");
    }

    record Tuple(List<Integer> report, List<Integer> fixedReport) {
    }

    private static Stream<Tuple> reports() {
        return Stream.of(
                new Tuple(List.of(48, 46, 47, 49, 51, 54, 56), List.of(46, 47, 49, 51, 54, 56)),
                new Tuple(List.of(1, 1, 2, 3, 4, 5), List.of(1, 2, 3, 4, 5)),
                new Tuple(List.of(1, 2, 3, 4, 5, 5), List.of(1, 2, 3, 4, 5)),
                new Tuple(List.of(5, 1, 2, 3, 4, 5), List.of(1, 2, 3, 4, 5)),
                new Tuple(List.of(1, 4, 3, 2, 1), List.of(4, 3, 2, 1)),
                new Tuple(List.of(1, 6, 7, 8, 9), List.of(6, 7, 8, 9)),
                new Tuple(List.of(1, 2, 3, 4, 3), List.of(1, 2, 3, 4)),
                new Tuple(List.of(9, 8, 7, 6, 7), List.of(9, 8, 7, 6)),
                new Tuple(List.of(7, 10, 8, 10, 11), List.of(7, 8, 10, 11)),
                new Tuple(List.of(11, 8, 10, 8, 7), List.of(11, 10, 8, 7)),
                new Tuple(List.of(29, 28, 27, 25, 26, 25, 22, 20), List.of(29, 28, 27, 26, 25, 22, 20)),

                //Found 5 discrepancies
                new Tuple(List.of(15, 16, 18, 22, 19), List.of(15, 16, 18, 19)),
                new Tuple(List.of(40, 39, 38, 36, 38, 33), List.of(40, 39, 38, 36, 33))
        );
    }
}
