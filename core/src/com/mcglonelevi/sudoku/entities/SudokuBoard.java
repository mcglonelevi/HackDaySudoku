package com.mcglonelevi.sudoku.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mcglonelevi.sudoku.util.FontGenerator;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class SudokuBoard implements InputProcessor {
    Tile[][] tiles;
    Texture tileTexture;
    Vector2 selectedTile = null;
    OrthographicCamera cam;
    Pattern keyRegex = Pattern.compile("[1-9]");
    BitmapFont font;
    ParticleEffect effect;
    private boolean hasWon = false;
    Integer[][] sudokuArray;
    Viewport viewport;
    Sound poofSound;
    Sound popSound;

    public SudokuBoard(OrthographicCamera cam, Integer[][] sudokuArray, Viewport viewport) {
        this.viewport = viewport;
        this.sudokuArray = sudokuArray;
        this.poofSound = Gdx.audio.newSound(Gdx.files.internal("poof.mp3"));
        this.popSound = Gdx.audio.newSound(Gdx.files.internal("pop.mp3"));

        this.effect = new ParticleEffect();
        effect.load(Gdx.files.internal("smoke.pfx"), Gdx.files.internal(""));

        this.font = FontGenerator.generateFont(Gdx.files.internal("hemi-head-semi-bold.ttf"));

        this.cam = cam;
        tiles = new Tile[9][9];
        tileTexture = new Texture(Gdx.files.internal("tile.png"));

        for (int x = 0; x < sudokuArray.length; x++) {
            for (int y = 0; y < sudokuArray[x].length; y++) {
                tiles[x][y] = new Tile(x, y, sudokuArray[x][y], tileTexture, font);
            }
        }
    }

    public void draw(SpriteBatch batch) {
        for (int x = 0; x < sudokuArray.length; x++) {
            for (int y = 0; y < sudokuArray[x].length; y++) {
                tiles[x][y].draw(batch, this.selectedTile != null && this.selectedTile.x == x && this.selectedTile.y == y);
            }
        }

        effect.draw(batch, Gdx.graphics.getDeltaTime());
    }

    public boolean isGameOver() {
        return hasWon;
    }

    private boolean isValid() {
        return validateRows() && validateColumns() && validateBoxes();
    }

    private boolean validateRows() {
        for (int x = 0; x < tiles.length; x++) {
            Set<Integer> rowSet = new HashSet<>();

            for (int y = 0; y < tiles[x].length; y++) {
                if (tiles[x][y].number == null) {
                    return false;
                }

                rowSet.add(tiles[x][y].number);
            }

            if (rowSet.size() != 9) {
                return false;
            }
        }

        return true;
    }

    private boolean validateColumns() {
        for (int y = 0; y < tiles.length; y++) {
            Set<Integer> columnSet = new HashSet<>();

            for (int x = 0; x < tiles[y].length; x++) {
                if (tiles[x][y].number == null) {
                    return false;
                }

                columnSet.add(tiles[x][y].number);
            }

            if (columnSet.size() != 9) {
                return false;
            }
        }

        return true;
    }

    private boolean validateBoxes() {
        for (int xBox = 0; xBox < 3; xBox++) {
            for (int yBox = 0; yBox < 3; yBox++) {
                int xOffset = xBox * 3;
                int yOffset = yBox * 3;

                Set<Integer> boxSet = new HashSet<>();

                for (int x = xOffset; x < xOffset + 3; x++) {
                    for (int y = yOffset; y < yOffset + 3; y++) {
                        if (tiles[x][y].number == null) {
                            return false;
                        }

                        boxSet.add(tiles[x][y].number);
                    }
                }

                if (boxSet.size() != 9) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (selectedTile != null) {
            Tile tile = tiles[(int) selectedTile.x][(int) selectedTile.y];

            if (keycode >= Input.Keys.NUM_1 && keycode <= Input.Keys.NUM_9) {
                String keyString = Input.Keys.toString(keycode);
                if (keyRegex.matcher(keyString).matches()) {
                    tile.number = Integer.parseInt(keyString);
                    popSound.play(1.0f);
                    selectedTile = null;
                    updateWonState();

                    return true;
                }
            } else if (keycode == Input.Keys.BACKSPACE) {
                tile.number = null;

                drawPoof(tile);

                selectedTile = null;

                return true;
            }
        }

        return false;
    }

    void drawPoof(Tile tile) {
        this.poofSound.play(1.0f);
        effect.setPosition(tile.textDrawPosition.x, tile.textDrawPosition.y - 4);
        effect.start();
    }

    void updateWonState() {
        hasWon = isValid();
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        String characterString = String.valueOf(character);
        if (selectedTile != null && keyRegex.matcher(characterString).matches()) {
            tiles[(int) selectedTile.x][(int) selectedTile.y].number = Integer.parseInt(characterString);
            selectedTile = null;
            return true;
        }

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 unprojectedCoordinates = this.viewport.unproject(new Vector3(screenX, screenY, 0));

        boolean tileSelected = false;

        outerloop:
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[x].length; y++) {
                if (tiles[x][y].isSelectable() && tiles[x][y].isInDrawBounds((int) unprojectedCoordinates.x, (int) unprojectedCoordinates.y)) {
                    if (this.selectedTile != null && this.selectedTile.x == x && this.selectedTile.y == y) {
                        this.selectedTile = null;
                    } else {
                        this.selectedTile = new Vector2(x, y);
                    }
                    tileSelected = true;
                    break outerloop;
                }
            }
        }

        if (!tileSelected) {
            this.selectedTile = null;
        }

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
