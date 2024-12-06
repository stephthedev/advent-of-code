package com.stephthedev.adventofcode.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileParserUtil {

    @Deprecated
    public static List<String> parseFileAsStringList(String fileName) throws FileNotFoundException, IOException {
        List<String> lines = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    public static List<String> getLinesFromFile(String inputFile) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(FileParserUtil.class.getResourceAsStream("/" + inputFile)))) {
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
        }
        return lines;
    }

    public static char[][] parseFile(String inputFile) throws IOException {
        List<char[]> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(FileParserUtil.class.getResourceAsStream("/" + inputFile)))) {
            String line = reader.readLine();
            while (line != null) {
                lines.add(line.toCharArray());
                line = reader.readLine();
            }
        }
        char[][] grid = new char[lines.size()][];
        lines.toArray(grid); // fill the array

        return grid;
    }
}
