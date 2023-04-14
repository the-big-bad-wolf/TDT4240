package com.mygdx.shapewars.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class UIBuilder {
    private final Stage stage;
    private final Skin skin;
    private TextFieldStyle fieldStyle;
    private TextButtonStyle buttonStyle;
    private TextField textField;
    private TextButton textButton;

    public UIBuilder(Stage stage) {
        this.stage = stage;
        this.skin = new Skin(Gdx.files.internal("data/uiskin.json"));
    }

    public TextField buildTextField(String text, float width, float height, float xPos, float yPos) {
        textField = new TextField(text, skin);

        buildTextFieldStyle(skin);
        textField.setSize(width, height);
        textField.setPosition(xPos, yPos);
        stage.addActor(textField);

        return textField;
    }

    public Label buildTextLabel(String text, float width, float height, float xPos, float yPos) {
        Label label = new Label(text, skin);
        label.setWidth(width);
        label.setHeight(height);
        label.setPosition(xPos, yPos);
        stage.addActor(label);
        return label;
    }

    public TextButton buildButton(String text, float width, float height, float xPos, float yPos) {
        textButton = new TextButton(text, skin);

        buildButtonStyle(skin);
        textButton.setSize(width, height);
        textButton.setPosition(xPos, yPos);
        stage.addActor(textButton);

        return textButton;
    }

    private void buildTextFieldStyle(Skin skin) {
        fieldStyle = new TextFieldStyle();
        fieldStyle.font = skin.getFont("default-font");
        fieldStyle.font.getData().setScale(3f);
        fieldStyle.background = skin.getDrawable("default-scroll");
        fieldStyle.cursor = skin.getDrawable("default-round");
    }

    private void buildButtonStyle(Skin skin) {
        buttonStyle = new TextButtonStyle();
        buttonStyle.font = skin.getFont("default-font");
        buttonStyle.font.getData().setScale(3f);
        buttonStyle.up = skin.getDrawable("default-round");
        buttonStyle.down = skin.getDrawable("default-round-down");
    }
}
