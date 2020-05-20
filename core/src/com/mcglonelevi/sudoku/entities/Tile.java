package com.mcglonelevi.sudoku.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

public class Tile {
    public static final int TILE_SIZE = 64;
    public static final int TILE_SPACING = 3;
    public static final int BOX_SPACING = 3;

    int x;
    int y;
    Integer number;
    Texture tileTexture;
    Vector2 drawPosition;
    Vector2 textDrawPosition;
    BitmapFont font;
    boolean selectable;

    public Tile(int x, int y, Integer number, Texture tileTexture, BitmapFont font) {
        this.x = x;
        this.y = y;
        this.number = number;
        this.tileTexture = tileTexture;
        this.drawPosition = calculateDrawPosition(x, y);
        this.textDrawPosition = new Vector2(this.drawPosition.x + 32, this.drawPosition.y + 36);
        this.font = font;
        this.selectable = number == null;
    }

    public Vector2 calculateDrawPosition(int x, int y) {
        return new Vector2(
            x * TILE_SIZE + x * TILE_SPACING + (x / 3) * BOX_SPACING,
            y * TILE_SIZE + y * TILE_SPACING + (y / 3) * BOX_SPACING
        );
    }

    public boolean isSelectable() {
        return selectable;
    }

    public boolean isInDrawBounds(int x, int y) {
        return x > drawPosition.x && x < drawPosition.x + TILE_SIZE && y > drawPosition.y && y < drawPosition.y + TILE_SIZE;
    }

    public void draw(SpriteBatch batch, boolean selected) {
        if (selected) {
            batch.setColor(Color.YELLOW);
        }
        batch.draw(tileTexture, drawPosition.x, drawPosition.y);
        if (selected) {
            batch.setColor(Color.WHITE);
        }

        if (number != null) {
            font.draw(batch, String.valueOf(this.number), this.textDrawPosition.x, this.textDrawPosition.y, 0, Align.center, false);
        }
    }
}
