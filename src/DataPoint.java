package src;

public class DataPoint {
    private int id;
    private String hurni;
    private int lal;
    private String volta;
    private String suruna;

    private double actualGrade;


    DataPoint(Student student, String courseName) {
        this.id=student.getId();
        this.hurni = student.getInfo().getHurniLevel();
        this.lal = student.getInfo().getLalCount();
        this.volta = student.getInfo().getVolta();
        this.suruna = student.getInfo().getSurunaValue();
        this.actualGrade = student.getGradeForCourse(courseName);
    }

    public double getActualCourseGrade(){
        return actualGrade;
    }

    public String getHurni() {
        return hurni;
    }

    public void setHurni(String hurni) {
        this.hurni = hurni;
    }

    public int getLal() {
        return lal;
    }

    public String getVolta() {
        return volta;
    }

    public String getSuruna() {
        return suruna;
    }
    public int getVoltaCount(){
        return DataPoint.convertVolta(volta);
    }
    public int getSurunaCount() {
        return DataPoint.convertSuruna(suruna);
    }
    public int getHurniCount(){
        return DataPoint.convertHurni(hurni);
    }

    public int getId() {
        return id;
    }

    public int getValue(String property){
        switch (property){
            case("suruna"):
                return getSurunaCount();
            case("hurni"):
                return getHurniCount();
            case("lal"):
                return getLal();
            case("volta"):
                return getVoltaCount();
            default:
                return -1;
        }
    }
    public String getStringValue(String property){
        switch (property){
            case("suruna"):
                return getSuruna();
            case("hurni"):
                return getHurni();
            case("lal"):
                return String.valueOf(getLal());
            case("volta"):
                return getVolta();
            default:
                return "";
        }
    }
    public static int convertValue(String property,String value){
        switch (property){
            case("suruna"):
                return convertSuruna(value);
            case("hurni"):
                return convertHurni(value);
            case("lal"):
                return convertLal(value);
            case("volta"):
                return convertVolta(value);
            default:
                return -1;
        }
    }
    public static int convertSuruna(String value){
        switch (value) {
            case ("nulp"):
                return 0;
            case ("doot"):
                return 1;
            case ("lobi"):
                return 2;
        }
        return -1;
    }
    public static int convertHurni(String value){
        switch (value) {
            case ("nothing"):
                return 0;
            case ("low"):
                return 1;
            case ("medium"):
                return 2;
            case ("full"):
                return 3;
            case ("high"):
                return 4;
        }
        return -1;
    }
    public static int convertLal(String value){
        return Integer.parseInt(value);
    }
    public static int convertVolta(String value){
        if(value.contains("stars")){
            return Integer.parseInt(value.replace(" stars",""))-1;
        }else{
            return Integer.parseInt(value.replace(" star",""))-1;
        }
    }

}