

package org.example.sudokugame_javafx;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SudokuLogic {
    static int[][] sudoku = new int[9][9];
    private static int[][] answer = new int[9][9];
    static int[][] sudokubak = new int[9][9];

    // Static method to read Sudoku
    public static void readSudoku() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("sudoku.txt"));
        String line;
        int row = 0;
        while ((line = reader.readLine()) != null && row < 9) {
            String[] numbers = line.trim().split(" ");
            for (int col = 0; col < 9 && col < numbers.length; col++) {
                int num = Integer.parseInt(numbers[col]);
                sudoku[row][col] = num;
                sudokubak[row][col] = num;
            }
            row++;
        }
        reader.close();
    }

    public static void resetSudoku() {
        // Copy values from the backup array to the Sudoku array
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sudoku[i][j] = sudokubak[i][j];
            }
        }
    }
    // Static method to get the value at a specific position in the Sudoku
    public static int getSudokuValue(int row, int col) {
        return sudoku[row][col];
    }



    // Static method to read the answer
    public static void readAnswer() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("answer.txt"));
        String line;
        int row = 0;
        while ((line = reader.readLine()) != null && row < 9) {
            String[] numbers = line.trim().split(" ");
            for (int col = 0; col < 9 && col < numbers.length; col++) {
                answer[row][col] = Integer.parseInt(numbers[col]);
            }
            row++;
        }
        reader.close();
    }

    public static int readAnswerValue(int row, int col) throws IOException {
        return answer[row][col];
    }

    // Static method to check if the Sudoku is correct
    public static boolean checkSudoku() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (sudoku[i][j] != answer[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void setSudokuValue(int row, int col, int value) {
        sudoku[row][col] = value;
    }


}
