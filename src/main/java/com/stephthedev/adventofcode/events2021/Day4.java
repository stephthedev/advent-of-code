package com.stephthedev.adventofcode.events2021;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day4 {

    private BingoData getData(String fileName) throws FileNotFoundException, IOException {
        List<String> lines = Util.parseFileAsStringArray(fileName);

        //1. Construct an int[] of the called numbers from the first line
        String[] calledNumbersStrArr = lines.get(0).trim().split(",");
        int[] calledNumbersArr = new int[calledNumbersStrArr.length];
        for (int i=0; i<calledNumbersStrArr.length; i++) {
            int num = Integer.valueOf(calledNumbersStrArr[i]);
            calledNumbersArr[i] = num;
        }


        //1. Construct the bingo card
        List<BingoCard> bingoCards = new ArrayList<BingoCard>();
        int[][] bingoCard = new int[5][5];
        int row = 0;
        for (int i=2; i<lines.size(); i++ ) {
            String line = lines.get(i).trim().replaceAll("\\s+", " ");	//Ignore padding and consolidate multiple spaces into one
            if (line.length() > 0) {
                String[] numbersArr = line.split(" ");
                for (int j=0; j<numbersArr.length; j++) {
                    bingoCard[row][j] = Integer.valueOf(numbersArr[j]);
                }
                row++;
            } else {
                //Add the bingo card to a list
                bingoCards.add(new BingoCard(bingoCard));
                //Reset
                bingoCard = new int[5][5];
                row = 0;
            }
        }
        bingoCards.add(new BingoCard(bingoCard));

        return new BingoData(calledNumbersArr, bingoCards);
    }

    private List<Long> computeWinningBoard(BingoData bingoData) {
        List<Long> bingos = new ArrayList<>();
        //For each bingo number
        for (int calledNumber : bingoData.calledNumbers) {

            //Mark it on the card
            for (int i=0; i<bingoData.bingoCards.size(); i++) {
                if (!bingoData.bingoCards.get(i).isBingo()) {
                    bingoData.bingoCards.get(i).maybeMarkNumber(calledNumber);

                    //If bingo, compute the final score, which is:
                    //bingo number * unmarked numbers sum
                    if (bingoData.bingoCards.get(i).isBingo()) {
                        long finalScore = calledNumber * bingoData.bingoCards.get(i).getUnmarkedNumbersSum();
                        bingos.add(finalScore);
                    }
                }
            }
        }
        return bingos;
    }

    public static class BingoNumber {
        final int bingoNumber;
        boolean isMarked;

        BingoNumber(int bingoNumber) {
            this.bingoNumber = bingoNumber;
            this.isMarked = false;
        }

        void mark() {
            isMarked = true;
        }
    }

    public static class BingoCard {

        private BingoNumber[][] bingoNumbers;
        boolean isBingo;

        public BingoCard(int[][] numbers) {
            this.bingoNumbers = new BingoNumber[numbers.length][numbers[0].length];
            this.isBingo = false;
            for (int i=0; i<numbers.length; i++) {
                for (int j=0; j<numbers[i].length; j++) {
                    bingoNumbers[i][j] = new BingoNumber(numbers[i][j]);
                }
            }
        }

        boolean maybeMarkNumber(int number) {
            boolean isFound = false;
            for (int row=0; row<bingoNumbers.length; row++) {
                for (int col=0; col<bingoNumbers[row].length; col++) {
                    if (bingoNumbers[row][col].bingoNumber == number) {
                        bingoNumbers[row][col].isMarked = true;
                        isFound = true;
                    }
                }
            }
            return isFound;
        }

        boolean isBingo() {
            //Check if any rows are a bingo
            for (int row=0; row<bingoNumbers.length; row++) {
                boolean isBingoRow = true;
                for (int col=0; col<bingoNumbers[row].length; col++) {
                    isBingoRow &= bingoNumbers[row][col].isMarked;
                }
                if (isBingoRow) {
                    isBingo = true;
                    return true;
                }
            }

            //Check if any columns are a bingo
            for (int col=0; col<bingoNumbers[0].length; col++) {
                boolean isBingoColumn = true;
                for (int row=0; row<bingoNumbers.length; row++) {
                    isBingoColumn &= bingoNumbers[row][col].isMarked;
                }
                if (isBingoColumn) {
                    isBingo = false;
                    return true;
                }
            }

            return false;
        }

        int getUnmarkedNumbersSum() {
            int sum = 0;
            for (int row=0; row<bingoNumbers.length; row++) {
                for (int col=0; col<bingoNumbers[row].length; col++) {
                    if (bingoNumbers[row][col].isMarked == false) {
                        sum += bingoNumbers[row][col].bingoNumber;
                    }
                }
            }
            return sum;
        }

    }

    public static class BingoData {
        final List<BingoCard> bingoCards;
        final int[] calledNumbers;

        public BingoData(int[] calledNumbers, List<BingoCard> bingoCards) {
            this.calledNumbers = calledNumbers;
            this.bingoCards = bingoCards;
        }
    }

    public long[] run(String fileName) throws FileNotFoundException, IOException {
        BingoData data = getData(fileName);
        List<Long> results = computeWinningBoard(data);
        return new long[] {results.get(0), results.get(results.size() - 1)};
    }

    public static void main(String[] args) throws IOException {
        Day4 day = new Day4();
        long[] solution = day.run("src/main/resources/aoc2021/day4_input.txt");
        System.out.println(Arrays.toString(solution));
    }
}
