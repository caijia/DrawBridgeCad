package com.caijia.drawbridgecad;

import java.io.Serializable;

public class BridgeParams implements Serializable {

    private float length;
    private float width;
    private float diBan;
    private float fuBan;
    private float yiYuanBan;
    private int zuoDun;
    private int youDun;
    private int dunShu;
    private String direction;
    private String unit;

    public BridgeParams() {
        length = -1;
        width = -1;
        diBan = -1;
        fuBan = -1;
        yiYuanBan = -1;
        zuoDun = -1;
        youDun = -1;
        dunShu = -1;
        unit = Constants.UNIT_M;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getDiBan() {
        return diBan;
    }

    public void setDiBan(float diBan) {
        this.diBan = diBan;
    }

    public float getFuBan() {
        return fuBan;
    }

    public void setFuBan(float fuBan) {
        this.fuBan = fuBan;
    }

    public float getYiYuanBan() {
        return yiYuanBan;
    }

    public void setYiYuanBan(float yiYuanBan) {
        this.yiYuanBan = yiYuanBan;
    }

    public int getZuoDun() {
        return zuoDun;
    }

    public void setZuoDun(int zuoDun) {
        this.zuoDun = zuoDun;
    }

    public int getYouDun() {
        return youDun;
    }

    public void setYouDun(int youDun) {
        this.youDun = youDun;
    }

    public int getDunShu() {
        return dunShu;
    }

    public void setDunShu(int dunShu) {
        this.dunShu = dunShu;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
