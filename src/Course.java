package src;

import javax.swing.*;

public class Course {
    private String name;
    private double averageGrade;
    private double mean;
    private double median;
    private double std_deviation;
    private int ngCount;

    public Course(String courseName) {
        name=courseName;
    }
    public double getAverageGrade(){
        return averageGrade;
    }
    public void setAverageGrade(double course_avg){
        averageGrade=course_avg;
    }
    public double getMean(){
        double m=0;
        if (!Double.isNaN(mean)){
            m=mean;
        }
        return m;
    }
    public void setMean(double course_mean){
        mean=course_mean;
    }
    public double getMedian(){
        double m=0;
        if (!Double.isNaN(median)){
            m=median;
        }
        return m;
    }
    public void setMedian(double course_median){
        median=course_median;
    }
    public double getStd_deviation(){
        double std_dev=0;
        if (!Double.isNaN(std_deviation)){
            std_dev=std_deviation;
        }
        return std_dev;
    }
    public void setStd_deviation(double course_std_deviation){
        std_deviation=course_std_deviation;
    }
    public int getNgCount(){
        return ngCount;
    }
    public void setNgCount(int course_ngCount){
        ngCount=course_ngCount;
    }

    public String getName() {
        return name;
    }
}
