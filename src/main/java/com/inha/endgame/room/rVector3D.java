package com.inha.endgame.room;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class rVector3D implements Serializable {
    private static final long serialVersionUID = 1151561654154545L;

    private double x;
    private double y;
    private double z;

    public rVector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}

