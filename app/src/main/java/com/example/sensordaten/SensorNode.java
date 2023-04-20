package com.example.sensordaten;

public class SensorNode {
    private float x;
    private float y;
    private float z;

    SensorNode(float x, float y, float z){
        this.x = x;
        this.y= y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getMAG(){
        return (float) Math.sqrt(x*x + y*y + z*z);
    }

    public String getData(String trenner){
        return (Math.rint(x * 100) / 100) + trenner + (Math.rint(y * 100) / 100) + trenner + (Math.rint(z * 100) / 100) + "\n";
    }

}
