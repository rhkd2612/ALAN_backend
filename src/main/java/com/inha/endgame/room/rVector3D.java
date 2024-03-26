package com.inha.endgame.room;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class rVector3D implements Serializable {
    private static final long serialVersionUID = 1151561654154545L;

    private float x;
    private float y;
    private float z;

    public rVector3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}

