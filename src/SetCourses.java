package src;

import java.util.ArrayList;
import java.util.List;

public class SetCourses extends Set {
    private List<Course> coursesList;
    public SetCourses(String n, List<Course> courses) {
        super(n);
        coursesList= new ArrayList<>(courses);
    }
}