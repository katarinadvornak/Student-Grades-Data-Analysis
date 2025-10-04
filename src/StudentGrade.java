package src;

public class StudentGrade {
    private Student parent;
    private String courseName;
    private double courseGrade;

    public StudentGrade(String id, double grade){
        courseName=id;
        courseGrade=grade;
    }

    public double getCourseGrade() {
        return courseGrade;
    }

    public String getCourseName() {
        return courseName;
    }

    public Student getParent() {
        return parent;
    }

    public void setParent(Student parent) {
        this.parent = parent;
    }
}
