package com.mcglonelevi.sudoku.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mcglonelevi.sudoku.util.FontGenerator;

import java.awt.*;

public class Menu {
    TextButton playButton;
    public Stage stage;

    public Menu (InputListener listener, Viewport viewport) {
        stage = new Stage(viewport);
        Skin skin = new Skin(Gdx.files.internal("glassy/glassy-ui.json"));
        this.playButton = new TextButton("Play", skin, "small");
        this.playButton.setSize(100, 60);
        this.playButton.setX(Gdx.graphics.getWidth() / 2f, Align.center);
        this.playButton.setY(Gdx.graphics.getWidth() / 2f, Align.center);
        this.playButton.addListener(listener);
        stage.addActor(this.playButton);
    }

    public void draw() {
        stage.act();
        stage.draw();
    }
}
