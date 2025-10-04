package src;

public class Prediction {
    private String courseName;
    private double predictedGrade;
    public Prediction(String courseName,double predictedGrade){
        this.courseName=courseName;
        this.predictedGrade=predictedGrade;
    }

    public String getCourseName() {
        return courseName;
    }

    public double getPredictedGrade() {
        return predictedGrade;
    }
}
