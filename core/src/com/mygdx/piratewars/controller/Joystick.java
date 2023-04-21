package com.mygdx.shapewars.controller;

import com.badlogic.gdx.math.Circle;

public class Joystick {
    private final Circle outerCircle;
    private final Circle innerCircle;

    public Joystick(int screenX, int screenY, int outerCircleRadius, int innerCircleRadius) {
        this.outerCircle = new Circle(screenX, screenY, outerCircleRadius);
        this.innerCircle = new Circle(screenX, screenY, innerCircleRadius);
    }

    public Circle getOuterCircle() {
        return outerCircle;
    }

    public Circle getInnerCircle() {
        return innerCircle;
    }
}