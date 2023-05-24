package com.example.sensordaten;

public class SensorNode {
    private float x;
    private float y;
    private float z;
    private long date;
    String klassenType;

    SensorNode(float x, float y, float z, long currentDate){
        this.x = x;
        this.y= y;
        this.z = z;
        this.date = currentDate;
    }
    SensorNode(float x, float y, float z, long currentDate, String klassenType){
        this.x = x;
        this.y= y;
        this.z = z;
        this.date = currentDate;
        this.klassenType = klassenType;
    }
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

    public String getDataWDate(String trenner){
        return x + trenner + y + trenner + z + trenner + date;
    }
    public String getData(String trenner){
        return x + trenner + y + trenner + z ;
    }

    public long getDate(){
        return this.date;
    }

    public String getKlassenType(){
        return this.klassenType;
    }
}
