package com.mcglonelevi.sudoku;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mcglonelevi.sudoku.entities.Menu;
import com.mcglonelevi.sudoku.entities.SudokuBoard;
import com.mcglonelevi.sudoku.util.SudokuAPI;
import com.mcglonelevi.sudoku.util.SudokuSolver;

public class SudokuGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private SudokuBoard sudokuBoard;
	private OrthographicCamera cam;
	private Viewport viewport;
	private boolean playing = false;
	Menu menu;
	InputListener listener = new InputListener(){
		@Override
		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
			playing = true;
			Gdx.input.setInputProcessor(sudokuBoard);
			return true;
		}
	};

	@Override
	public void create () {
		batch = new SpriteBatch();

		cam = new OrthographicCamera(606, 606);
		viewport = new FitViewport(606, 606, cam);
		cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
		cam.update();

		menu = new Menu(listener, viewport);

		Integer[][] sudokuPuzzle = SudokuAPI.getSudokuBoard();
		if (sudokuPuzzle == null) {
			Gdx.app.exit();
			return;
		}

		SudokuSolver solver = new SudokuSolver(sudokuPuzzle);
		Integer[][] sudokuSolution = solver.solve();
		sudokuBoard = new SudokuBoard(cam, sudokuPuzzle, sudokuSolution, viewport);

		Gdx.input.setInputProcessor(menu.stage);
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void render () {
		if (sudokuBoard.isGameOver()) {
			Gdx.app.exit();
		}

		cam.update();
		batch.setProjectionMatrix(cam.combined);

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();

		if (playing) {
			sudokuBoard.draw(batch);
		} else {
			menu.draw();
		}

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
