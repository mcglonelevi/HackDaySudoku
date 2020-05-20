package com.mcglonelevi.sudoku;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mcglonelevi.sudoku.entities.SudokuBoard;
import com.mcglonelevi.sudoku.entities.Tile;

public class SudokuGame extends ApplicationAdapter {
	SpriteBatch batch;
	SudokuBoard sudokuBoard;

	@Override
	public void create () {
		batch = new SpriteBatch();
		sudokuBoard = new SudokuBoard();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();

		sudokuBoard.draw(batch);

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
