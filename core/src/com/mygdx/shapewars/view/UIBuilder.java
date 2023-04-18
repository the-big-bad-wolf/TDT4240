package com.mygdx.shapewars.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class UIBuilder {
    private final Stage stage;
    private final Skin skin;
    private TextFieldStyle fieldStyle;
    private TextButtonStyle buttonStyle;
    private ImageButton.ImageButtonStyle imageButtonStyle;
    private TextField textField;
    private TextButton textButton;
    private ImageButton imageButton;

    public UIBuilder(Stage stage) {
        this.stage = stage;
        this.skin = new Skin(Gdx.files.internal("data/custom.json"));
    }

    public TextField buildTextField(String text, String styleName, float width, float height, float xPos, float yPos) {
        buildTextFieldStyle(skin, styleName);
        textField = new TextField(text, fieldStyle);
        textField.setSize(width, height);
        textField.setPosition(xPos, yPos);
        textField.setAlignment(Align.center);

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

    public TextButton buildButton(String text, float width, float height, float xPos, float yPos, String styleName) {
        buildButtonStyle(skin, styleName);
        textButton = new TextButton(text, buttonStyle);
        textButton.setSize(width, height);
        textButton.setPosition(xPos, yPos);
        stage.addActor(textButton);

        return textButton;
    }

    private void buildTextFieldStyle(Skin skin, String styleName) {
        fieldStyle = skin.get(styleName, TextFieldStyle.class);
        fieldStyle.font.getData().setScale(1f);
    }

    private void buildButtonStyle(Skin skin, String styleName) {
        buttonStyle = skin.get(styleName, TextButtonStyle.class);
        buttonStyle.font.getData().setScale(1f);
    }

    private void buildImageButtonStyle(Skin skin, Drawable buttonUp) {
        imageButtonStyle = new ImageButton.ImageButtonStyle();
        imageButtonStyle.up = skin.newDrawable(buttonUp);
    }
}
