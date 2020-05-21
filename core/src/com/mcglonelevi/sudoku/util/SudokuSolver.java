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

    public static void main(String[] args) {
        SudokuSolver solver = new SudokuSolver(new Integer[][] {
            {null, null, null, null, 4, 1, null, 7, 5},
            {null, null, 7, null, null, 8, 1, null, 3},
            {null, 1, null, null, null, null, null, null, null},

            {null, 5, 9, null, 1, null, null, 4, null},
            {null, null, 4, 5, null, null, 2, null, 1},
            {null, 2, null, null, null, 4, 9, 5, null},

            {null, null, 5, 6, null, 3, null, 1, null},
            {3, null, null, 4, null, null, null, null, null},
            {null, null, null, null, 9, 5, 8, null, null},
        });

        solver.solve();
        solver.debug();
    }

    public Integer[][] solve() {
        populateSets();
        calculateSets();

        boolean shouldRun = true;

        while (shouldRun) {
            shouldRun = performRowColumnBoxImplicitSearch();
        }

        return this.board;
    }

    public Set<Integer> calculatePossibilitiesForCell(int x, int y) {
        Set<Integer> rowSet = rowSets.get(x);
        Set<Integer> columnSet = columnSets.get(y);
        Set<Integer> boxSet = boxSets.get(determineBox(x, y));

        Set<Integer> cellSet = new HashSet<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        cellSet.removeAll(rowSet);
        cellSet.removeAll(columnSet);
        cellSet.removeAll(boxSet);

        return cellSet;
    }

    boolean performRowColumnBoxImplicitSearch() {
        boolean modified = false;

        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if (this.board[x][y] != null) {
                    continue;
                }

                Set<Integer> cellSet = calculatePossibilitiesForCell(x, y);

                // In some lookups, the box, column, row scan will be good enough
                if (cellSet.size() == 1) {
                    Integer number = (Integer) cellSet.toArray()[0];
                    this.board[x][y] = number;
                    rowSets.get(x).add(number);
                    columnSets.get(y).add(number);
                    boxSets.get(determineBox(x, y)).add(number);
                    modified = true;
                }
                else {
                    cellSet.removeAll(getIntersectionSetForOtherEmptyCellsInBox(x, y));

                    if (cellSet.size() == 1) {
                        Integer number = (Integer) cellSet.toArray()[0];
                        this.board[x][y] = number;
                        rowSets.get(x).add(number);
                        columnSets.get(y).add(number);
                        boxSets.get(determineBox(x, y)).add(number);
                        modified = true;
                    }
                }
            }
        }

        return modified;
    }

     Set<Integer> getIntersectionSetForOtherEmptyCellsInBox(int x, int y) {
        int boxX = x / 3;
        int boxY = y / 3;

        Set<Integer> combinedSet = new HashSet<>();

         System.out.println("SCAN GROUPING FOR " + x + ", " + y);
         System.out.println("Box X: " + boxX);
         System.out.println("Box Y: " + boxY);
        for (int scanX = boxX * 3; scanX < boxX * 3 + 3; scanX++) {
            for (int scanY = boxY * 3; scanY < boxY * 3 + 3; scanY++) {
                System.out.println(scanX + ", " + scanY);
                if ((scanX == x && scanY == y) || (board[x][y] != null)) {
                    continue;
                }

                combinedSet.addAll(calculatePossibilitiesForCell(scanX, scanY));
            }
        }

        return combinedSet;
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
        System.out.println(Arrays.deepToString(this.board).replaceAll("], ", "\n"));
    }
}
