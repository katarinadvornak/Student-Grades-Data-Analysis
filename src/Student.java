package src;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private int id;
    private double gpa;
    private int passedCourses;
    private ObservableList<StudentGrade> coursesGrades;
    private StudentInfo info;

    public Student(int student_id) {
        id = student_id;
        coursesGrades = FXCollections.observableArrayList();
    }

    public int getId() {
        return id;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public int getPassedCourses() {
        return passedCourses;
    }

    public void setPassedCourses(int passedCourses) {
        this.passedCourses = passedCourses;
    }

    public ObservableList<StudentGrade> getCoursesGrades() {
        return coursesGrades;
    }

    public void setCoursesGrades(ObservableList<StudentGrade> coursesGrades) {
        this.coursesGrades = coursesGrades;
    }

    public void setInfo(StudentInfo info) {
        this.info = info;
    }

    public StudentInfo getInfo() {
        return info;
    }

    public void addGrade(StudentGrade grade) {
        coursesGrades.add(grade);
    }

    public double getGradeForCourse(String courseName) {
        double actual_grade=-1;
        for (StudentGrade grade : coursesGrades) {
            if (grade.getCourseName().equals(courseName)) {
                if(grade.getCourseGrade()!=0){
                    actual_grade= grade.getCourseGrade();
                }
            }
        }
        // Return a default value or handle the case when the course name is not found
        return actual_grade;


    }
}
