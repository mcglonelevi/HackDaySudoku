package com.mcglonelevi.sudoku.util;

import java.util.*;

public class SudokuSolver {
    Integer[][] board;
    List<Set<Integer>> columnSets = new ArrayList<>();
    List<Set<Integer>> rowSets = new ArrayList<>();
    List<Set<Integer>> boxSets = new ArrayList<>();

    public SudokuSolver(Integer[][] board) {
        this.board = new Integer[9][9];

        // clone to local state
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                this.board[x][y] = board[x][y];
            }
        }
    }

    public static int determineBox(int x, int y) {
        return (x / 3) + (y / 3) * 3;
    }

    public Integer[][] solve() {
        populateSets();
        calculateSets();

        boolean shouldRun = true;

        while (shouldRun) {
            shouldRun = performRowColumnBoxSearch();
        }

        return this.board;
    }

    boolean performRowColumnBoxSearch() {
        boolean modified = false;

        for (int x = 0; x < 9; x++) {
            Set<Integer> rowSet = rowSets.get(x);

            for (int y = 0; y < 9; y++) {
                if (this.board[x][y] != null) {
                    continue;
                }

                Set<Integer> columnSet = columnSets.get(y);
                Set<Integer> boxSet = boxSets.get(determineBox(x, y));
                Set<Integer> cellSet = new HashSet<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));

                cellSet.removeAll(rowSet);
                cellSet.removeAll(columnSet);
                cellSet.removeAll(boxSet);

                if (cellSet.size() == 1) {
                    int number = (int) cellSet.toArray()[0];
                    this.board[x][y] = number;
                    rowSet.add(number);
                    columnSet.add(number);
                    boxSet.add(number);
                    modified = true;
                }
            }
        }

        return modified;
    }

    void populateSets() {
        for (int i = 0; i < 9; i++) {
            rowSets.add(new HashSet<Integer>());
            columnSets.add(new HashSet<Integer>());
            boxSets.add(new HashSet<Integer>());
        }
    }

    void calculateSets() {
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                Integer currentCell = board[x][y];
                if (currentCell != null) {
                    rowSets.get(x).add(currentCell);
                    columnSets.get(y).add(currentCell);
                    boxSets.get(determineBox(x, y)).add(currentCell);
                }
            }
        }
    }

    public void debug() {
        System.out.println(columnSets);
        System.out.println(rowSets);
        System.out.println(boxSets);
    }
}
