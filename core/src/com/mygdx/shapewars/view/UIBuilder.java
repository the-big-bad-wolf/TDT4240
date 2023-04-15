package com.mygdx.shapewars.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
    private TextField textField;
    private TextButton textButton;

    public UIBuilder(Stage stage) {
        this.stage = stage;
        this.skin = new Skin(Gdx.files.internal("data/uiskin.json"));
    }

    public TextField buildTextField(String text, float width, float height, float xPos, float yPos) {
        textField = new TextField(text, skin);
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = new BitmapFont(Gdx.files.internal("data/verdana39.fnt"));

        buildTextFieldStyle(skin);
        textField.setSize(width, height);
        textField.setPosition(xPos, yPos);
        textField.setColor(Color.valueOf("7ba3b0"));
        // textField.setStyle(textFieldStyle);

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

    /*
     * public TextField buildTextField(String text, float width, float height, float
     * xPos, float yPos, Drawable backgroundDrawable) {
     * TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
     * 
     * // Set up the font for the text field
     * BitmapFont font = new BitmapFont(Gdx.files.internal("data/verdana39.fnt"));
     * textFieldStyle.font = font;
     * 
     * // Set up the background image for the text field
     * textFieldStyle.background = backgroundDrawable;
     * 
     * // Create the text field with the text and style
     * TextField textField = new TextField(text, textFieldStyle);
     * 
     * // Set the size and position of the text field
     * textField.setSize(width, height);
     * textField.setPosition(xPos, yPos);
     * 
     * // Add the text field to your stage or UI table
     * stage.addActor(textField);
     * 
     * return textField;
     * }
     */

    public TextButton buildButton(String text, float width, float height, float xPos, float yPos) {
        textButton = new TextButton(text, skin);
        buildButtonStyle(skin);
        textButton.setSize(width, height);
        textButton.setPosition(xPos, yPos);
        stage.addActor(textButton);

        return textButton;
    }

    public TextButton buildInputButton(String text, float width, float height, float xPos, float yPos,
            TextureRegionDrawable background) {
        TextButton textButton = new TextButton(text, skin);

        TextButton.TextButtonStyle textButtonStyle = textButton.getStyle();

        // Set the background image for the text button
        textButtonStyle.up = background;

        // Set the background image for the text button when pressed
        textButtonStyle.down = background; // You can use a different image for pressed state if desired

        textButton.setSize(width, height);
        textButton.setPosition(xPos, yPos);
        stage.addActor(textButton);

        return textButton;
    }

    public ImageButton buildImageButton(Texture texture, float width, float height, float xPos, float yPos) {
        Drawable drawableImage = new TextureRegionDrawable(new TextureRegion(texture));
        ImageButton imageButton = new ImageButton(drawableImage);

        imageButton.setSize(width, height);
        imageButton.setPosition(xPos, yPos);
        stage.addActor(imageButton);

        return imageButton;
    }

    private void buildTextFieldStyle(Skin skin) {
        fieldStyle = new TextFieldStyle();
        fieldStyle.font = skin.getFont("default-font");
        fieldStyle.font.getData().setScale(1f);
        fieldStyle.background = skin.getDrawable("default-scroll");
        fieldStyle.cursor = skin.getDrawable("default-round");
    }

    private void buildButtonStyle(Skin skin) {
        buttonStyle = new TextButtonStyle();
        buttonStyle.font = skin.getFont("default-font");
        buttonStyle.font.getData().setScale(1f);
        buttonStyle.up = skin.getDrawable("default-round");
        buttonStyle.down = skin.getDrawable("default-round-down");
    }

    public ImageButton buildImageButtonWithText(Texture texture, String text, float width, float height, float xPos,
            float yPos) {
        Drawable drawableImage = new TextureRegionDrawable(new TextureRegion(texture));
        ImageButton imageButton = new ImageButton(drawableImage);

        // Create a Label for the text
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont(Gdx.files.internal("data/verdana39.fnt"),
                Gdx.files.internal("data/verdana39.png"), false);
        // labelStyle.font.getData().setScale(1.8f);
        labelStyle.fontColor = Color.valueOf("7ba3b0");

        Label ipAddressLabel = new Label(text, labelStyle);
        ipAddressLabel.setAlignment(Align.center);
        ipAddressLabel.setSize(width, height); // Set the size of the Label to match the ImageButton
        ipAddressLabel.setPosition((width - ipAddressLabel.getWidth()) / 2, (height - ipAddressLabel.getHeight()) / 2); // Set
                                                                                                                        // the
                                                                                                                        // position
                                                                                                                        // of
                                                                                                                        // the
                                                                                                                        // text
                                                                                                                        // within
                                                                                                                        // the
                                                                                                                        // ImageButton

        // Add the Label as a child actor to the ImageButton
        imageButton.addActor(ipAddressLabel);

        imageButton.setSize(width, height);
        imageButton.setPosition(xPos, yPos);
        stage.addActor(imageButton);

        return imageButton;
    }
}
