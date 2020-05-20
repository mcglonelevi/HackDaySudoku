package com.mcglonelevi.sudoku.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SudokuAPI {
    public static Integer[][] getSudokuBoard() {
        String requestJson = requestBoard();

        if (requestJson == null) {
            Gdx.app.exit();
            return null;
        } else {
            JsonReader jsonReader = new JsonReader();
            JsonValue jsonValue = jsonReader.parse(requestJson);
            JsonValue squares = jsonValue.get("squares");
            Integer[][] intArray = new Integer[9][9];

            for (int i = 0; i < squares.size; i++) {
                JsonValue square = squares.get(i);

                intArray[square.getInt("x")][square.getInt("y")] = square.getInt("value");
            }

            return intArray;
        }
    }

    private static String requestBoard() {
        HttpURLConnection con = null;
        try {
            URL url = new URL("http://www.cs.utep.edu/cheon/ws/sudoku/new/?size=9&level=2");
            con = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } catch (IOException e) {
            //e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }

        return null;
    }
}
