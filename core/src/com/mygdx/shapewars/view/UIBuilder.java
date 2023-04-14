package com.mygdx.shapewars.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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

        buildTextFieldStyle(skin);
        textField.setSize(width, height);
        textField.setPosition(xPos, yPos);
        stage.addActor(textField);
        
        return textField;
    }

    public TextButton buildButton(String text, float width, float height, float xPos, float yPos) {
        textButton = new TextButton(text, skin);

        buildButtonStyle(skin);
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

    public ImageButton buildImageButtonIP(Texture texture, String ipAddress, float width, float height, float xPos, float yPos) {
        Drawable drawableImage = new TextureRegionDrawable(new TextureRegion(texture));
        ImageButton imageButton = new ImageButton(drawableImage);

        // Create a Label for the ipAddress text
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont(Gdx.files.internal("data/verdana39.fnt"), Gdx.files.internal("data/verdana39.png"), false);
        //labelStyle.font.getData().setScale(1.8f);
        labelStyle.fontColor = Color.valueOf("7ba3b0");

        Label ipAddressLabel = new Label(ipAddress, labelStyle);
        ipAddressLabel.setAlignment(Align.center);
        ipAddressLabel.setSize(width, height); // Set the size of the Label to match the ImageButton
        ipAddressLabel.setPosition((width - ipAddressLabel.getWidth()) / 2, (height - ipAddressLabel.getHeight()) / 2); // Set the position of the text within the ImageButton

        // Add the Label as a child actor to the ImageButton
        imageButton.addActor(ipAddressLabel);

        imageButton.setSize(width, height);
        imageButton.setPosition(xPos, yPos);
        stage.addActor(imageButton);

        return imageButton;
    }


}
