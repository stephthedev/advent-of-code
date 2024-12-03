package com.stephthedev.adventofcode.events2021;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day2 {

    private List<Instruction> ingest(String fileName) throws FileNotFoundException, IOException {
        List<String> movements = Util.parseFileAsStringArray(fileName);
        List<Instruction> instructions = new ArrayList<>();
        for (String movement : movements) {
            String[] commandUnitArr = movement.split(" ");
            String command = commandUnitArr[0];
            int units = Integer.valueOf(commandUnitArr[1]);
            instructions.add(new Instruction(command, units));
        }
        return instructions;
    }

    private long runPartOne(List<Instruction> instructions) {
        int horizontal = 0;
        int depth = 0;
        for (Instruction instruction : instructions) {
            switch(instruction.command) {
                case FORWARD :
                    horizontal += instruction.units;
                    break;
                case DOWN :
                    depth += instruction.units;
                    break;
                case UP :
                    depth -= instruction.units;
                    break;
                default:
                    break;
            }
        }
        return horizontal * depth;
    }

    private long runPartTwo(List<Instruction> instructions) {
        int horizontal = 0;
        int depth = 0;
        int aim = 0;
        for (Instruction instruction : instructions) {
            switch(instruction.command) {
                case FORWARD :
                    horizontal += instruction.units;
                    depth += (aim * instruction.units);
                    break;
                case DOWN :
                    aim += instruction.units;
                    break;
                case UP :
                    aim -= instruction.units;
                    break;
                default:
                    break;
            }
        }
        return horizontal * depth;
    }

    public long[] run(String fileName) throws FileNotFoundException, IOException {
        List<Instruction> instructions = ingest(fileName);
        long part1 = runPartOne(instructions);
        long part2 = runPartTwo(instructions);
        return new long[] {part1, part2};
    }

    public static void main(String[] args) throws IOException {
        Day2 day = new Day2();
        long[] solution = day.run("src/main/resources/day2_input.txt");
        System.out.println(Arrays.toString(solution));
    }

    private enum Command {
        FORWARD, UP, DOWN
    }

    class Instruction {
        Command command;
        long units;

        public Instruction(String command, long units) {
            this.command = Command.valueOf(command.toUpperCase());
            this.units = units;
        }
    }
}
