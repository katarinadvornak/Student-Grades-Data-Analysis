package src;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Utils {

    private static  final String surunaPath="suruna";
    private static  final String hurniPath="hurni";
    private static  final String lalPath="lal";
    private static  final String voltaPath="volta";
    public static List<DataPoint> convertStudentsToDataPoints(List<Student> students, Course course) {
        List<DataPoint> dataPoints = new ArrayList<>();
        for (Student student : students) {
            dataPoints.add(convertStudentToDataPoint(student, course));
        }
        return dataPoints;
    }

    public static DataPoint convertStudentToDataPoint(Student student, Course course) {
        return new DataPoint(student,course.getName());
    }

    public static double customRound(double number) {
        if (number % 1 >= 0.5) {
            return Math.ceil(number);
        } else {
            return Math.floor(number);
        }
    }
    public static boolean isNumericProperty(String propertyPath){
        return propertyPath.equals(lalPath);
    }


}
