package src;

import javafx.collections.ObservableList;

public class StudentInfo {
    private String surunaValue;
    private String hurniLevel;
    private int lalCount;
    private String volta;
    public StudentInfo(String suruna, String hurni,int lal, String vol){
        surunaValue=suruna;
        hurniLevel=hurni;
        lalCount=lal;
        volta=vol;
    }

    public String getSurunaValue() {
        return surunaValue;
    }

    public String getHurniLevel() {
        return hurniLevel;
    }

    public int getLalCount() {
        return lalCount;
    }

    public String getVolta() {
        return volta;
    }
    public int getVoltaCount(){
        if(volta.contains("stars")){
            return Integer.parseInt(volta.replace(" stars",""));
        }else{
            return Integer.parseInt(volta.replace(" star",""));
        }
    }
}
