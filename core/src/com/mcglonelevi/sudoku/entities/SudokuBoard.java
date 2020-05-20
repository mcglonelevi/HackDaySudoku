package com.mcglonelevi.sudoku.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SudokuBoard {
    Tile[][] tiles;
    Texture tileTexture;
    int[][] sudokuArray = {
            {
                    4, 6, 1, 8, 3, 9, 5, 2, 7
            },
            {
                    3, 8, 9, 2, 7, 5, 1, 4, 6
            },
            {
                    5, 2, 7, 6, 4, 1, 9, 8, 3
            },
            {
                    2, 5, 8, 1, 6, 7, 3, 9, 4
            },
            {
                    6, 7, 3, 9, 8, 4, 2, 5, 1
            },
            {
                    9, 1, 4, 5, 2, 3, 6, 7, 8
            },
            {
                    7, 4, 5, 3, 9, 6, 8, 1, 2
            },
            {
                    8, 9, 6, 4, 1, 2, 7, 3, 5
            },
            {
                    1, 3, 2, 7, 5, 8, 4, 6, 9
            }
    };

    public SudokuBoard() {
        tiles = new Tile[9][9];
        tileTexture = new Texture(Gdx.files.internal("tile.png"));

        for (int x = 0; x < sudokuArray.length; x++) {
            for (int y = 0; y < sudokuArray.length; y++) {
                tiles[x][y] = new Tile(x, y, sudokuArray[x][y], tileTexture);
            }
        }
    }

    public void draw(SpriteBatch batch) {
        for (int x = 0; x < sudokuArray.length; x++) {
            for (int y = 0; y < sudokuArray.length; y++) {
                tiles[x][y].draw(batch);
            }
        }
    }
}
