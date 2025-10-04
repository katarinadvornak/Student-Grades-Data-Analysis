package src;

import java.util.ArrayList;
import java.util.List;

public class SetStudents extends Set {
    private List<Student> studentsList;
    public SetStudents(String n,List<Student> students) {
        super(n);
        studentsList=new ArrayList<>(students);
    }
}
