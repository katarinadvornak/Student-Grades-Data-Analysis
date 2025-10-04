package src;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;

import java.io.File;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.stream.Collectors;


public class FileDisplayer {

    private static final String EMPTY = "NG";
    public final String CURRENTGRADES = "datasets/CurrentGrades.csv";
    public final String GRADUATEGRADES = "datasets/GraduateGrades.csv";
    public final String STUDENTINFO = "datasets/StudentInfo.csv";
    public final String COURSENAME = "ATE-003";

    private static  final String surunaPath="suruna";
    private static  final String hurniPath="hurni";
    private static  final String lalPath="lal";
    private static  final String voltaPath="volta";

    private Object[][] currentStudentsDataArray;
    private int[] currentStudentsPerCourse;
    private double[] currentStudentsAverageGrade;
    private Object[][] graduateStudentsDataArray;
    private int[] graduateStudentsPerCourse;
    private double[] graduateStudentsAverageGrade;
    private Object[][] studentInfoData;


    //ALEX 27.112.2023
    private ObservableList<Student> currentStudents;
    private ObservableList<Student> graduateStudents;
    private ObservableList<Course> currentStudents_Courses;
    private ObservableList<Course> graduateStudents_Courses;
    private ObservableList<Student> filtered_currentStudents;
    private ObservableList<Course> filtered_currentStudents_Courses;
    private ObservableList<Course> filtered_graduateStudents_Courses;
    private ObservableList<String> selectedCourses;
    private Map<String, String> dictionary_suruna = new HashMap<String, String>();
    private Map<String, String> dictionary_hurni = new HashMap<String, String>();



    public FileDisplayer() {
        try {

            int maxLines = calculateRows(CURRENTGRADES);
            int maxEntries = calculateColumns(CURRENTGRADES);

            int maxLinesGraduated = calculateRows(GRADUATEGRADES);
            int maxEntriesGraduated = calculateColumns(GRADUATEGRADES);

            int maxLinesStudentInfo = calculateRows(STUDENTINFO);
            int maxEntriesStudentInfo = calculateColumns(STUDENTINFO);

            // ALEX 11.10.2023 I tried to make a function that reads the file and
            // in this way we can read both files without repeating the code

            // This array of objects will be composed by:
            // [0] 2D-Array of objects (dataArray)
            // [1] Array of average grades per course
            // [2] Array containing students that didn't take the exam por that course

            // CURRENT STUDENTS 
            Object[] AllData = readDataFromFile(CURRENTGRADES, maxEntries, maxLines);
            currentStudentsDataArray = (Object[][]) AllData[0];
            currentStudentsPerCourse = (int[]) AllData[2];
            currentStudentsAverageGrade = computeAvarage(maxLines, maxEntries, (double[]) AllData[1], currentStudentsPerCourse);


            //GRADUATE STUDENTS 
            Object[] graduateAllData = readDataFromFile(GRADUATEGRADES, maxEntriesGraduated, maxLinesGraduated);
            graduateStudentsDataArray = (Object[][]) graduateAllData[0];
            graduateStudentsAverageGrade = computeAvarage(maxLinesGraduated, maxEntriesGraduated, (double[]) graduateAllData[1], null);
            graduateStudentsPerCourse = (int[]) graduateAllData[2];

            Object[] studentInfoAllData = readDataFromFile(STUDENTINFO, maxEntriesStudentInfo, maxLinesStudentInfo);
            studentInfoData = (Object[][]) studentInfoAllData[0];


            //ALEX 27.11.2023
            //creation of the STUDENT list and COURSE list

            //COURSES
            currentStudents_Courses = createCoursesList(currentStudentsDataArray, true);
            graduateStudents_Courses = FXCollections.observableArrayList(createCoursesList(graduateStudentsDataArray, false));

            //STUDENTS
            currentStudents = createStudentsList(currentStudentsDataArray, studentInfoData);
            graduateStudents = createStudentsList(graduateStudentsDataArray, null);

            
            /*System.out.println("\n\n" + "AVERAGE GRADES\n\n");
            AverageGrades(maxEntriesGraduated, graduateDataArray, graduateAverageGrade, maxLinesGraduated, studentsPerCourse);


            // Calculate and display Correlation for each course
            System.out.println("\n\n" + "CORRELATIONS\n\n");
            calculateAndDisplayStrongestCorrelation(dataArray, maxEntries, studentsPerCourse);
            
            System.out.println("\n\n" + "CUMLAUDE GRADUATE\n\n");
            cumLaude(GRADUATEGRADES,graduateDataArray);
            
            // STATISTICS OLD STUDENTS 
            System.out.println("\n\n" + "STATISTICS\n\n");
            statistics(graduateDataArray, maxEntriesGraduated, maxLinesGraduated, graduatestudentsPerCourse);

            // STATISTICS CURRENT STUDENTS 
            statistics(dataArray, maxEntries, maxLines, studentsPerCourse);

            // graduateThisYear(dataArray,maxLines);
            System.out.println("\n\n" + "PASSING PERCENTAGE\n\n");
            calculatePassingPercentage(maxLines, maxEntries, dataArray, studentsPerCourse);
            */
            // System.out.println("\n\n" + "NG COUNT\n\n");
            //countNG(dataArray,studentsPerCourse,graduateAverageGrade);

            //System.out.println("\n\n" + "DROPOUTS\n\n");    
            //countDropOuts(dataArray);

            //displayInfoCurrentData(studentInfoAllData, graduateDataArray, studentInfoData);

            // TESTING SURUNA AVERAGES 
            //System.out.print(" doot avg " + Dootavg(graduateDataArray, COURSENAME, studentInfoData));
            //System.out.print(" lobi avg " + lobiSuruna(graduateDataArray, COURSENAME, studentInfoData));
            //System.out.print(" nulp avg " + nulpSuruna(graduateDataArray, COURSENAME, studentInfoData));


            // System.out.println("\n\n" + "COMPARISON BETWEEN CURRENT NG AND GRADUATE COURSES \n\n");
            //predictionOnNGCourses(dataArray, graduateDataArray, studentsPerCourse,graduateAverageGrade);


            // System.out.println("\n\n" + "BEST PROPERTY \n\n");

            //studentPrediction(1000243, dataArray , "ATE-203", studentInfoData );
            //displayAverageForCourse(graduateDataArray, maxEntriesStudentInfo, graduateAverageGrade, maxLinesStudentInfo, studentsPerCourse, COURSENAME);

            //differentHurni(graduateDataArray, CURRENTGRADES, studentInfoData);
            //differentLal(graduateDataArray, COURSENAME, studentInfoData);
            //differentStarValues(graduateDataArray, COURSENAME, studentInfoData);
            //differentSurunaValues(graduateDataArray, COURSENAME, studentInfoData);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private ObservableList<Student> createStudentsList(Object[][] dataArray, Object[][] infoDataArray) {
        ObservableList<Student> datalist = FXCollections.observableArrayList();
        for (int i = 1; i < dataArray.length; i++) {
            int std_id = (int) dataArray[i][0];
            Student std = new Student(std_id);
            double sum = 0;
            int passed_courses = 0;
            for (int j = 1; j < dataArray[0].length; j++) {
                if (!dataArray[i][j].toString().equals(EMPTY)) {
                    double grade_data = Double.parseDouble(dataArray[i][j].toString());
                    StudentGrade grade = new StudentGrade(dataArray[0][j].toString(), grade_data);
                    grade.setParent(std);
                    std.addGrade(grade);
                    sum += grade_data;
                    passed_courses += 1;
                } else {
                    StudentGrade grade = new StudentGrade(dataArray[0][j].toString(), 0);
                    grade.setParent(std);
                    std.addGrade(grade);
                }
            }

            //calculate gpa
            double gpa = sum / passed_courses;
            std.setGpa(gpa);

            //set passed courses
            std.setPassedCourses(passed_courses);


            //info array linked
            if (infoDataArray != null) {
                String surunaVal = infoDataArray[i][1].toString();
                String hurniVal = infoDataArray[i][2].toString();
                int lalVal = Integer.parseInt(infoDataArray[i][3].toString());
                String voltaVal = infoDataArray[i][4].toString();
                ;
                StudentInfo info = new StudentInfo(surunaVal, hurniVal, lalVal, voltaVal);
                std.setInfo(info);
            }
            datalist.add(std);
        }
        return datalist;
    }

    private ObservableList<Course> createCoursesList(Object[][] dataArray, Boolean isCurrent) {
        //ALEX 27.11.2023 Here i turned the dataArray in a list of courses objects (with all the necessary values)
        ObservableList<Course> dataCourses = FXCollections.observableArrayList();
        int[] ngCount;
        double[] avg_grade;
        if (isCurrent) {
            ngCount = currentStudentsPerCourse;
            avg_grade = currentStudentsAverageGrade;
        } else {
            ngCount = graduateStudentsPerCourse;
            avg_grade = graduateStudentsAverageGrade;
        }

        double[] coursesMean = mean(dataArray, dataArray[0].length, dataArray.length, ngCount);
        double[] coursesMedian = median(dataArray, dataArray[0].length, dataArray.length, ngCount);
        double[] courseStd_deviation = standardDeviation(dataArray, dataArray[0].length, dataArray.length, ngCount);
        for (int i = 1; i < currentStudentsDataArray[0].length; i++) {

            String courseName = dataArray[0][i].toString();
            Course newCourse = new Course(courseName);
            newCourse.setAverageGrade(avg_grade[i]);
            newCourse.setNgCount(ngCount[i]);
            newCourse.setMean(coursesMean[i - 1]);
            newCourse.setMedian(coursesMedian[i - 1]);
            newCourse.setStd_deviation(courseStd_deviation[i - 1]);
            //statistic values adding
            dataCourses.add(newCourse);
        }
        return dataCourses;
    }

    public static Object[] readDataFromFile(String filepath, int maxEntries, int maxLines) {
        Object[] Alldata = new Object[3];
        try {

            File file = new File(filepath);

            double[] averageGrade = new double[maxEntries];
            Object[][] dataArray = new Object[maxLines][maxEntries];

            Scanner fileScanner = new Scanner(file);
            int linesDone = 0;

            // ALEX 10/10/2023 added an array that for each course, gives me
            // how many sudents took the exam



            //StudentNumber,Suruna Value (string),Hurni Level(string),Lal Count,Volta(mixed "(number) star/stars")

            //ALEX 12/01/2024 I add a counter for the possible values for each property

            ///////////////////////////////////////////////////////////////////////////////////////
            int studentsPerCourse[] = new int[maxEntries];

            while (fileScanner.hasNextLine() && linesDone < maxLines) {
                String line = fileScanner.nextLine();
                linesDone++;

                Scanner lineScanner = new Scanner(line);
                lineScanner.useDelimiter(",");

                int entryIndex = 0;

                while (lineScanner.hasNext() && entryIndex < maxEntries) {
                    if (lineScanner.hasNextInt()) {
                        int i = lineScanner.nextInt();
                        dataArray[linesDone - 1][entryIndex] = i;
                    } else if (lineScanner.hasNextDouble()) {
                        double d = lineScanner.nextDouble();
                        averageGrade[entryIndex] += d;
                        dataArray[linesDone - 1][entryIndex] = d;
                    } else {
                        String s = lineScanner.next();
                        dataArray[linesDone - 1][entryIndex] = s;
                        if (s.contains(EMPTY)) {
                            studentsPerCourse[entryIndex]++;
                        }
                    }
                    entryIndex++;
                }

                lineScanner.close();
            }

            fileScanner.close();

            Alldata[0] = dataArray;
            Alldata[1] = averageGrade;
            Alldata[2] = studentsPerCourse;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Alldata;
    }

    // ALEX 10/10/2023 I try to read the file one tine before the while loop and set
    // the max values for row/colunms
    public static int calculateRows(String filepath) {
        int rowsCount = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            String line;
            while ((line = reader.readLine()) != null) {
                rowsCount += 1;
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rowsCount;
    }

    public static int calculateColumns(String filepath) {
        int columnsCount = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            String line = reader.readLine();
            String[] fields = line.split(",");
            for (String st : fields) {
                columnsCount += 1;
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return columnsCount;
    }

    public static double[] computeAvarage(int linesDone, int maxEntries, double[] averageGrade, int[] studentsPerCourse) {
        // Calculate average difficulties

        for (int i = 0; i < maxEntries; i++) {
            if (studentsPerCourse != null) {
                averageGrade[i] /= ((linesDone - 1) - studentsPerCourse[i]);
            } else {
                averageGrade[i] /= (linesDone - 1);
            }
        }
        return averageGrade;
    }

    // statistics method
    public void statistics(Object[][] dataArray, int[] studentsPerCourse) {
        // create an array with course labels
        String[] courses = new String[dataArray[0].length - 1];

        for (int i = 1; i < dataArray[0].length; i++) {
            courses[i - 1] = dataArray[0][i].toString();
        }

        // calculate the statistics for all courses
        double[] mean = mean(dataArray, dataArray[0].length, dataArray.length, studentsPerCourse);
        double[] median = median(dataArray, dataArray[0].length, dataArray.length, studentsPerCourse);
        double[] standardDeviations = standardDeviation(dataArray, dataArray[0].length, dataArray.length, studentsPerCourse);

        // create a 2D array with course names, mean, median and standard deviation
        Object[][] statisticsArray = new Object[courses.length][4];
        for (int i = 0; i < courses.length; i++) {
            statisticsArray[i][0] = courses[i];
            statisticsArray[i][1] = mean[i];
            statisticsArray[i][2] = median[i];
            statisticsArray[i][3] = standardDeviations[i];
        }

        // TODO: this bellow needs to be deleted after the tests
        // print the 2D array
        for (int i = 0; i < statisticsArray.length; i++) {
            for (int j = 0; j < statisticsArray[0].length; j++) {
                System.out.print(statisticsArray[i][j] + "   ");
            }
            System.out.println();
        }

        // checking simillirites
        for (int i = 0; i < statisticsArray.length; i++) {
            double balance = mean[i] - median[i];
            if (balance > 0.15) {
                System.out.println("course " + courses[i] + " has higher achievers");
            }
            if (balance < -0.15) {
                System.out.println("course " + courses[i] + " has lower achievers");
            } else
                System.out.println("course " + courses[i] + " is balanced");

            // Checking if uniform
            if (standardDeviations[i] <= 1) {
                System.out.println(" this course " + courses[i] + " is uniform");
            }

        }

    }

    public double[] mean(Object[][] dataArray, int maxColumns, int maxLines, int[] studentsPerCourse) {
        double[] mean = new double[maxColumns];
        for (int i = 1; i < maxColumns; i++) {
            double average = 0;
            double sum = 0;

            for (int j = 1; j < maxLines; j++) {
                if (checkArray(dataArray[j][i])) {
                    sum += (double) dataArray[j][i];
                }
            }
            // ALEX 11/10/2023 sum divided by the number of students for each course
            average = sum / ((maxLines - 1) - studentsPerCourse[i]);
            if (average != 0) {
                mean[i - 1] = average;

            } else {
                mean[i - 1] = 0;
            }

        }
        for (int i = 1; i < mean.length; i++) {
            // System.out.println("The mean grade for the course number " + dataArray[0][i]
            // + " is " + mean[i]);
        }
        return mean;

    }

    // compute and print median for all courses
    public double[] median(Object[][] dataArray, int maxColumns, int maxLines, int[] studentsPerCourse) {
        int countCourses = maxColumns - 1;
        double[] medianAr = new double[countCourses];

        for (int j = 1; j < countCourses + 1; j++) {
            // create and array with the values of each column
            double columnData[] = new double[(dataArray.length - 1) - studentsPerCourse[j]];
            int count = 0;
            for (int i = 1; i < maxLines; i++) {
                if (checkArray(dataArray[i][j])) {
                    columnData[count] = (double) dataArray[i][j];
                    count += 1;
                }

            }
            // sort the values in the array
            Arrays.sort(columnData);

            int middle = columnData.length / 2;
            double median;
            if (columnData.length != 0) {
                // check if the number of values in the array is odd or even
                if (columnData.length % 2 == 0) {
                    median = ((columnData[middle - 1]) + columnData[middle]) / 2.0;
                } else {
                    median = columnData[middle];
                }
                medianAr[j - 1] = median;
            } else {
                medianAr[j - 1] = 0;
            }

            // System.out.println("Median for the course " + dataArray[0][j] + "; " +
            // median);
        }
        return medianAr;
    }

    // compute and print the standard deviation
    public double[] standardDeviation(Object[][] dataArray, int maxColumns, int maxLines,
                                      int[] studentsPerCourse) {
        int countCourses = maxColumns - 1;
        double[] standardDevAr = new double[countCourses];

        for (int j = 1; j < countCourses + 1; j++) {
            // create an array with the values of each column
            double[] columnData = new double[maxLines - 1];
            for (int i = 1; i < maxLines; i++) {
                if (checkArray(dataArray[i][j])) {
                    columnData[i - 1] = (double) dataArray[i][j];
                }
            }

            // calculate the mean using an existing method
            double sum = 0;
            for (int i = 0; i < columnData.length; i++) {
                sum += columnData[i];
            }
            double mean = sum / (maxLines - 1);

            // calculate the sum of squared differences from the mean
            double sumOfSqrtDifferences = 0;
            for (int i = 0; i < columnData.length; i++) {
                sumOfSqrtDifferences += Math.pow(columnData[i] - mean, 2);
            }

            // calculate the standard deviation
            double standardDeviation = Math.sqrt(sumOfSqrtDifferences / (maxLines - 1));
            standardDevAr[j - 1] = standardDeviation;

            // System.out.println("Standard deviation of the course " + dataArray[0][j] + ":
            // " + standardDeviation);
        }
        return standardDevAr;
    }

    public boolean checkArray(Object arrayMember) {
        String arraystring = arrayMember.toString();
        boolean isOK = false;
        if (!arraystring.contains("NG")) {
            isOK = true;
        }
        return isOK;
    }

    public ArrayList<Integer> cumLaude(String filepath, Object[][] dataArray) {
        ArrayList<Integer> cumLaudeStudents = new ArrayList<Integer>();
        for (int i = 1; i < dataArray.length; i++) {
            double sum = 0;
            double average = 0;
            boolean grade7 = false;
            double lowergrade = 10;
            int countNG = 0;
            for (int j = 1; j < dataArray[0].length; j++) {
                if (checkArray(dataArray[i][j])) {
                    sum += (double) dataArray[i][j];
                    if ((double) dataArray[i][j] >= 7 && lowergrade >= 7) {
                        grade7 = true;
                    } else {
                        lowergrade = (double) dataArray[i][j];
                    }
                } else {
                    if (dataArray[i][j].toString().contains("NG")) {
                        countNG += 1;
                    }
                }
            }
            average = sum / (dataArray[0].length - 1) - countNG;
            if (average >= 8.0 && grade7 == true) {
                cumLaudeStudents.add(i);
            }
        }
        // Check if there are CumLaude students before printing
        /*if (!cumLaudeStudents.isEmpty()) {
            if (Objects.equals(filepath, GRADUATEGRADES)){
                System.out.println("These students graduated CumLaude: ");
            }else if(Objects.equals(filepath, CURRENTGRADES)){
                System.out.println("These students will probably graduate CumLaude: ");
            }
        } else {
            System.out.println("There are no students who graduated CumLaude");
        }

        for (Integer studentID : cumLaudeStudents) {
            System.out.println("Student ID: " + dataArray[studentID][0].toString());
        }*/
        return cumLaudeStudents;
    }

    public int singleStudentCredits(Object[][] dataArray, int studentIndex) {
        int credits = 0;
        for (int j = 1; j < dataArray[0].length; j++) {
            if (checkArray(dataArray[studentIndex][j])) {
                if ((double) dataArray[studentIndex][j] >= 6) {
                    credits += 6;
                }
            }
        }
        return credits;
    }

    public void AverageGrades(Object[][] dataArray, double[] averageGrade) {
        // // Calculate average difficulties
        // for (int i = 0; i < maxEntries; i++) {
        //     averageGrade[i] /= (linesDone - studentsPerCourse[i]);
        // }


        // Display the average difficulties
        for (int i = 1; i < dataArray[0].length; i++) {
            System.out.println("Average grade for Course " + dataArray[0][i] + ": " + (averageGrade[i]));
        }

        int easiestCourse = 1;
        int hardestCourse = 1;

        for (int i = 2; i < dataArray[0].length; i++) {
            if (averageGrade[i] > averageGrade[easiestCourse]) {
                easiestCourse = i;
            }
            if (averageGrade[i] < averageGrade[hardestCourse]) {
                hardestCourse = i;
            }
        }

        //TODO This bellow needs to be deleted after the test
        System.out.println("Easiest Course: " + dataArray[0][easiestCourse] + " with average grade "
                + (averageGrade[easiestCourse]));
        System.out.println("Hardest Course: " + dataArray[0][hardestCourse] + " with average grade "
                + (averageGrade[hardestCourse]));
    }

    public void calculateAndDisplayStrongestCorrelation(Object[][] dataArray, int[] studentsPerCourse) {
        double strongestCorrelation = 0.0; // between each course
        double lowestCorrelation = 0.0; // Initialize with a value higher than the expected correlations
        int course1Highest = 0;
        int course2Highest = 0;
        int course1Lowest = 0;
        int course2Lowest = 0;
        boolean noCorrelation = true;

        for (int i = 1; i < dataArray[0].length - 1; i++) {
            for (int j = i + 1; j < dataArray[0].length; j++) {
                if (studentsPerCourse[i] != dataArray.length && studentsPerCourse[j] != dataArray.length) {
                    double correlation = calculatePearsonCorrelation(dataArray, i, j);
                    if (Math.abs(correlation) > Math.abs(strongestCorrelation)) {
                        strongestCorrelation = correlation;
                        course1Highest = i;
                        course2Highest = j;
                    } else if (course1Lowest == 0 && course2Lowest == 0) {
                        lowestCorrelation = correlation;
                        course1Lowest = i;
                        course2Lowest = j;
                    }

                    if (Math.abs(correlation) < Math.abs(lowestCorrelation)) {
                        lowestCorrelation = correlation;
                        course1Lowest = i;
                        course2Lowest = j;
                    }

                    // Check if there is any correlation
                    if (correlation != 0.0) {
                        noCorrelation = false;
                    }
                } else {
                    noCorrelation = false;
                }

            }
        }

        //TODO This bellow needs to be deleted after the test
        if (!noCorrelation) {
            System.out.println("Highest correlation is between Course " + dataArray[0][course1Highest] + " and Course "
                    + dataArray[0][course2Highest] + ": " + strongestCorrelation);
            System.out.println("Lowest correlation is between Course " + dataArray[0][course1Lowest] + " and Course " + dataArray[0][course2Lowest]
                    + ": " + lowestCorrelation);
        } else {
            System.out.println("No correlation found between any courses.");
        }
    }

    // NIKITAS 10/10/2023:
    // This is where the calculation for the correlation happens
    public double calculatePearsonCorrelation(Object[][] dataArray, int course1, int course2) {
        double sumXY = 0.0, sumX = 0.0, sumY = 0.0, sumX2 = 0.0, sumY2 = 0.0;
        int count = 0;

        for (Object[] row : dataArray) {
            if (row[course1] instanceof Double && row[course2] instanceof Double) {
                double x = (double) row[course1];
                double y = (double) row[course2];

                sumXY += x * y;
                sumX += x;
                sumY += y;
                sumX2 += x * x;
                sumY2 += y * y;
                count++;
            }
        }

        if (count == 0) {
            return 0.0; // No data for correlation
        }

        double numerator = count * sumXY - sumX * sumY;
        double denominator = Math.sqrt((count * sumX2 - sumX * sumX) * (count * sumY2 - sumY * sumY));

        if (denominator == 0.0) {
            return 0.0; // Avoid division by zero
        }

        return numerator / denominator;
    }

    // create e a method to compute statistics on distribution similarities
    // compute and print the mean grade for all courses

    // ALEX 10/10/2023 this function check if a member of the array
    // contains the "NG"


    public int[][] studentCredits(Object[][] dataArray, int maxLines) {
        int[][] studentCreditsNumber = new int[maxLines][1];
        for (int i = 1; i < maxLines; i++) {
            int credits = singleStudentCredits(dataArray, i);
            studentCreditsNumber[i - 1][0] = (int) dataArray[i][0];
            studentCreditsNumber[i - 1][1] = credits;
        }
        return studentCreditsNumber;
    }


    public void calculatePassingPercentage(Object[][] dataArray, int[] studentsPerCourse) {

        for (int i = 0; i < dataArray[0].length - 1; i++) {
            if (studentsPerCourse[i] == dataArray[0].length - 1) {
                System.out.println("Course " + i + " has no students.");
            } else {
                int courseFailed = 0;
                for (int j = 1; j < dataArray.length; j++) {
                    if (checkArray(dataArray[j][i + 1])) {
                        if ((double) dataArray[j][i + 1] < 6) {
                            courseFailed += 1;
                        }
                    }
                }
                double passingPercentage = ((double) (studentsPerCourse[i] - courseFailed) / dataArray.length) * 100.0;
                System.out.println("Passing percentage for Course " + i + ": " + passingPercentage + "%");
            }
        }
    }

    public void countNG(Object[][] dataArray, int[] studentsPerCourse, double[] graduateAvarage) {
        for (int i = 1; i < studentsPerCourse.length; i++) {
            System.out.println("The course " + dataArray[0][i] + ": " + studentsPerCourse[i] + " NG occurences");
            System.out.println("Avarage grade in the Graduate Grades: " + graduateAvarage[i]);
        }

    }

    private void processStudent(Object[][] dataArray, int studentIndex, int maxEntries) {
        // NIKITAS 11/10/2023: Process each student in the array
        double sum = 0;
        int count = 0;
        int studentCredits = singleStudentCredits(dataArray, studentIndex);
        for (int j = 1; j < maxEntries; j++) {
            if (dataArray[studentIndex][j] instanceof Double) {
                sum += (double) dataArray[studentIndex][j];
                count++;
            }
        }

        double averageGradeValue = (count > 0) ? sum / count : 0;

        if (averageGradeValue >= 6 && studentCredits > 150 && studentCredits < 180) {
            String predictGraduation = predictGraduationStatus(averageGradeValue, dataArray, studentIndex, maxEntries);
            if (predictGraduation.contains("Graduating Soon (Correlated with Course")) {
                System.out.println("Student ID: " + dataArray[studentIndex][0] + " GPA: " +
                        averageGradeValue + " Result: pass" + " Prediction: " +
                        predictGraduation + "\n");
                if (studentCredits == 180) {
                    System.out.println("The student: " + dataArray[studentIndex][0] + " is eligible to graduate\n");
                } else {
                    System.out.println("The student: " + dataArray[studentIndex][0] + " is not eligible to graduate\n");
                }
            }

        }
    }

    private String predictGraduationStatus(double averageGPA, Object[][] dataArray, int studentIndex,
                                           int maxEntries) { // NIKITAS 11/10/2023: Predict graduation status based only on grades
        if (averageGPA >= 6.0) {
            // Check for correlations with other courses
            double strongestCorrelation = 0.0;
            int correlatedCourse = 0;

            for (int j = 1; j < maxEntries; j++) {
                if (j != studentIndex && studentIndex < maxEntries) {
                    double correlation = calculatePearsonCorrelation(dataArray, studentIndex, j);

                    if (Math.abs(correlation) > Math.abs(strongestCorrelation)) {
                        strongestCorrelation = correlation;
                        correlatedCourse = j;
                    }
                }
            }
            // NIKITAS 11/10/2023: checks for correlations between the current student's GPA and other courses.
            if (strongestCorrelation > 0.5) {
                return "Graduating Soon (Correlated with Course " + dataArray[0][correlatedCourse] + ")";
            } // A strong positive correlation with another course might indicate a higher
            // likelihood of success in the program.
        }

        return "Not Graduating Soon";
    }

    // 10.10. katarina
    // method to count how many student dropped out/ failed
    public int countDropOuts(Object[][] dataArray) {
        int count = 0;
        for (int i = 1; i < dataArray.length; i++) {
            Object[] students = dataArray[i]; // get the data for each student
            for (int j = 1; j < students.length; j++) {
                if (students[j] instanceof Double) {
                    double gpa = (Double) students[j];
                    if (gpa < 5.5) {
                        count++;
                        break;
                    }
                }

            }
        }
        System.out.println("The numbers of students that failed the study programme (gpa < 5.5): " + count);
        double numberstudents = (double) dataArray.length - 1;
        double dropOutrate = (count / numberstudents) * 100;
        System.out.println("the dropout rate: " + dropOutrate + "%");
        return count;

    }
    // public  double differentAllSurunaValues(Object[][] dataArray,int courseIndex,Object[] propretyInfoData){
    //     Object subgroup[]= propretyInfoData[0].toString();
    //     double averagenulp=differentSurunaValues((Object[])propretyInfoData[1],courseIndex,subgroup);
    //     double averagedoot=differentSurunaValues((Object[])propretyInfoData[2],courseIndex,subgroup);
    //     double averagelobi=differentSurunaValues((Object[])propretyInfoData[3],courseIndex,subgroup);

    //     double difference = Math.abs(averagenulp - averagedoot - averagelobi);
    //     System.out.println("the difference in average grade between nulp, doot and lobi students is: " + difference);
    //     return difference;
    // }
    public double differentSurunaValues(Object[][] dataArray, String COURSENAME, Object[][] studentInfo) {

        double nulpTotalScore = 0;
        int nulpCount = 0;
        double dootTotalScore = 0;
        int dootCount = 0;
        double lobiTotalScore = 0;
        int lobiCount = 0;

        // Find the index of the course column in gradeInfo array
        int courseIndex = 1;
        for (int i = 1; i < dataArray[0].length; i++) {
            if (dataArray[0][i].equals(COURSENAME)) {
                courseIndex = i;
                break;
            }
        }
        // nulp
        for (int i = 1; i < studentInfo.length; i++) {
            String studentInfonulp = studentInfo[i][1].toString();
            if (studentInfonulp.equalsIgnoreCase("nulp")) {
                if (!dataArray[i][courseIndex].equals("NG")) {
                    nulpTotalScore += (double) dataArray[i][courseIndex];
                    nulpCount++;
                }
            }
        }

        double averagenulp = (double) nulpTotalScore / nulpCount;
        System.out.println("the average grade of nulp students for the course: " + dataArray[0][courseIndex] + " is "
                + averagenulp);

        // doot
        for (int i = 1; i < studentInfo.length; i++) {
            String studentInfodoot = studentInfo[i][1].toString();
            if (studentInfodoot.equalsIgnoreCase("doot")) {
                if (!dataArray[i][courseIndex].equals("NG")) {
                    dootTotalScore += (double) dataArray[i][courseIndex];
                    dootCount++;
                }
            }
        }

        double averagedoot = (double) dootTotalScore / dootCount;
        System.out.println("the average grade of doot students for the course: " + dataArray[0][courseIndex] + " is "
                + averagedoot);

        // lobi
        for (int i = 1; i < studentInfo.length; i++) {
            String studentInfolobi = studentInfo[i][1].toString();
            if (studentInfolobi.equalsIgnoreCase("lobi")) {
                if (!dataArray[i][courseIndex].equals("NG")) {
                    lobiTotalScore += (double) dataArray[i][courseIndex];
                    lobiCount++;
                }
            }
        }

        double averagelobi = (double) lobiTotalScore / lobiCount;
        System.out.println("the average grade of lobi students for the course: " + dataArray[0][courseIndex] + " is "
                + averagelobi);

        double difference1 = Math.abs(averagenulp - averagedoot);
        double difference2 = Math.abs(averagenulp - averagelobi);
        double difference3 = Math.abs(averagelobi - averagedoot);
        System.out.println("the difference in average grade between nulp and doot is : " + difference1);
        System.out.println("the difference in average grade between doot and lobi is : " + difference3);
        System.out.println("the difference in average grade between nulp and lobi is : " + difference2);
        double difference;
        if (difference1 > difference2 && difference1 > difference3) {
            difference = difference1;
        } else if (difference2 > difference1 && difference2 > difference3) {
            difference = difference2;
        } else {
            difference = difference3;
        }
        return difference;

    }

    public double surunaavg(Object[][] dataArray, String courseName, Object[][] studentInfo) {
        double nulpTotalScore = 0;
        int nulpCount = 0;
        double dootTotalScore = 0;
        int dootCount = 0;
        double lobiTotalScore = 0;
        int lobiCount = 0;
        double difference;
        ;

        // Find the index of the course column in gradeInfo array
        int courseIndex = 1;
        for (int i = 1; i < dataArray[0].length; i++) {
            if (dataArray[0][i].equals(COURSENAME)) {
                courseIndex = i;
                break;
            }
        }
        // nulp
        for (int i = 1; i < studentInfo.length; i++) {
            String studentInfonulp = studentInfo[i][1].toString();
            if (studentInfonulp.equalsIgnoreCase("nulp")) {
                if (!dataArray[i][courseIndex].equals("NG")) {
                    nulpTotalScore += (double) dataArray[i][courseIndex];
                    nulpCount++;
                }
            }
        }

        double averagenulp = (double) nulpTotalScore / nulpCount;

        // doot
        for (int i = 1; i < studentInfo.length; i++) {
            String studentInfodoot = studentInfo[i][1].toString();
            if (studentInfodoot.equalsIgnoreCase("doot")) {
                if (!dataArray[i][courseIndex].equals("NG")) {
                    dootTotalScore += (double) dataArray[i][courseIndex];
                    dootCount++;
                }
            }
        }

        double averagedoot = (double) dootTotalScore / dootCount;

        // lobi
        for (int i = 1; i < studentInfo.length; i++) {
            String studentInfolobi = studentInfo[i][1].toString();
            if (studentInfolobi.equalsIgnoreCase("lobi")) {
                if (!dataArray[i][courseIndex].equals("NG")) {
                    lobiTotalScore += (double) dataArray[i][courseIndex];
                    lobiCount++;
                }
            }
        }
        double averagelobi = (double) lobiTotalScore / lobiCount;
        double difference1 = Math.abs(averagenulp - averagedoot);
        double difference2 = Math.abs(averagenulp - averagelobi);
        double difference3 = Math.abs(averagelobi - averagedoot);

        if (difference1 > difference2 && difference1 > difference3) {
            difference = difference1;
            return averagedoot;
        } else if (difference2 > difference1 && difference2 > difference3) {
            difference = difference2;
            if (averagenulp > averagelobi)
                return averagenulp;
            else return averagelobi;
        } else {
            difference = difference3;
            if (averagedoot > averagelobi)
                return averagedoot;
            else return averagelobi;
        }


    }

    public double Dootavg(Object[][] dataArray, String courseName, Object[][] studentInfo) {

        double DootavgTotalScore = 0;
        int DootCount = 0;

        int courseIndex = -1;
        for (int i = 1; i < dataArray[0].length; i++) {
            if (dataArray[0][i].equals(courseName)) {
                courseIndex = i;
                break;
            }
        }

        for (int i = 1; i < studentInfo.length; i++) {
            String studentInfoHurni = studentInfo[i][1].toString();
            if (studentInfoHurni.equalsIgnoreCase("doot")) {
                if (!dataArray[i][courseIndex].equals("NG")) {
                    DootavgTotalScore += (double) dataArray[i][courseIndex];
                    DootCount++;
                }
            }
        }
        double averagedootSuruna = (double) DootavgTotalScore / DootCount;

        return averagedootSuruna;
    }

    public double nulpSuruna(Object[][] dataArray, String courseName, Object[][] studentInfo) {
        double nulpSurunaTotalScore = 0;
        int nulpSurunaCount = 0;

        int courseIndex = -1;
        for (int i = 1; i < dataArray[0].length; i++) {
            if (dataArray[0][i].equals(courseName)) {
                courseIndex = i;
                break;
            }
        }

        for (int i = 1; i < studentInfo.length; i++) {
            String studentInfoSuruna = studentInfo[i][1].toString();
            if (studentInfoSuruna.equalsIgnoreCase("nulp")) {
                if (!dataArray[i][courseIndex].equals("NG")) {
                    nulpSurunaTotalScore += (double) dataArray[i][courseIndex];
                    nulpSurunaCount++;
                }
            }
        }
        double averageNulpSuruna = (double) nulpSurunaTotalScore / nulpSurunaCount;

        return averageNulpSuruna;
    }

    public double lobiSuruna(Object[][] dataArray, String courseName, Object[][] studentInfo) {
        double lobiSurunaTotalScore = 0;
        int lobiSurunaCount = 0;

        int courseIndex = -1;
        for (int i = 1; i < dataArray[0].length; i++) {
            if (dataArray[0][i].equals(courseName)) {
                courseIndex = i;
                break;
            }
        }

        for (int i = 1; i < studentInfo.length; i++) {
            String studentInfoSuruna = studentInfo[i][1].toString();
            if (studentInfoSuruna.equalsIgnoreCase("lobi")) {
                if (!dataArray[i][courseIndex].equals("NG")) {
                    lobiSurunaTotalScore += (double) dataArray[i][courseIndex];
                    lobiSurunaCount++;
                }
            }
        }
        double averageLobiSuruna = (double) lobiSurunaTotalScore / lobiSurunaCount;

        return averageLobiSuruna;
    }


    // 11.10.2023 Katarina
    // methods that compare the grades for different courses based on students'
    // properties
    // method that return a diffrence in everage scores between high and low hurni
    // scores
    public double differentHurni(Object[][] dataArray, String COURSENAME, Object[][] studentInfo) {
        double highHurniTotalScore = 0;
        int highHurniCount = 0;
        double lowHurniTotalScore = 0;
        int lowHurniCount = 0;
        // Find the index of the course column in gradeInfo array
        int courseIndex = 1;
        for (int i = 1; i < dataArray[0].length; i++) {
            if (dataArray[0][i].equals(COURSENAME)) {
                courseIndex = i;
                break;
            }
        }

        // high hurni level
        for (int i = 1; i < studentInfo.length; i++) {
            String studentInfoHurni = studentInfo[i][2].toString();
            if (studentInfoHurni.equalsIgnoreCase("high")) {
                if (!dataArray[i][courseIndex].equals("NG")) {
                    highHurniTotalScore += (double) dataArray[i][courseIndex];
                    highHurniCount++;
                }
            }
        }
        double averageHighHurni = (double) highHurniTotalScore / highHurniCount;
        System.out.println("the average grade of high hurni students for the course: " + dataArray[0][courseIndex]
                + " is " + averageHighHurni);

        // low hurni level
        for (int i = 1; i < studentInfo.length; i++) {
            String studentInfoHurni = studentInfo[i][2].toString();
            if (studentInfoHurni.equalsIgnoreCase("low")) {
                if (!dataArray[i][courseIndex].equals("NG")) {
                    lowHurniTotalScore += (double) dataArray[i][courseIndex];
                    lowHurniCount++;
                }
            }
        }
        double averageLowHurni = (double) lowHurniTotalScore / lowHurniCount;
        System.out.println("the average grade of low hurni students for the course: " + dataArray[0][courseIndex]
                + " is " + averageLowHurni);

        double difference = Math.abs(averageHighHurni - averageLowHurni);
        System.out.println("the difference in average grade between high and low hurni students is: " + difference);
        return difference;
    }

    // method that outputs the difference in averages based on Lal scores
    public double differentLal(Object[][] dataArray, String courseName, Object[][] studentInfo) {
        double highLalTotalScore = 0;
        int highLalCount = 0;
        double lowLalTotalScore = 0;
        int lowLalCount = 0;
        // Find the index of the course column in gradeInfo array
        int courseIndex = 1;
        for (int i = 1; i < dataArray[0].length; i++) {
            if (dataArray[0][i].equals(courseName)) {
                courseIndex = i;
                break;
            }
        }

        // calculate average lal value
        double sumLal = 0;
        for (int i = 1; i < studentInfo.length; i++) {
            int value = (int) studentInfo[i][3];
            sumLal += value;
        }
        double averageLal = sumLal / (studentInfo.length - 1);
        System.out.println("Average lal: " + averageLal);

        // high lal level = above 80
        for (int i = 1; i < studentInfo.length; i++) {
            int studentInfoLal = (int) studentInfo[i][3];
            if (studentInfoLal >= averageLal) {
                if (!dataArray[i][courseIndex].equals("NG")) {
                    highLalTotalScore += (double) dataArray[i][courseIndex];
                    highLalCount++;
                }
            }
        }
        double averageHighLal = (double) highLalTotalScore / highLalCount;
        System.out.println("the average grade of students with Lal score above average for the course: "
                + dataArray[0][courseIndex] + " is " + averageHighLal);

        // low lal level below 80
        for (int i = 1; i < studentInfo.length; i++) {
            int studentInfoLal = (int) studentInfo[i][3];
            if (studentInfoLal < averageLal) {
                if (!dataArray[i][courseIndex].equals("NG")) {
                    lowLalTotalScore += (double) dataArray[i][courseIndex];
                    lowLalCount++;
                }
            }
        }
        double averageLowLal = (double) lowLalTotalScore / lowLalCount;
        System.out.println("the average grade of students with Lal score below average for the course: "
                + dataArray[0][courseIndex] + " is " + averageLowLal);

        double difference = Math.abs(averageHighLal - averageLowLal);
        System.out.println("the difference in average grade between high and low lal students is: " + difference);
        return difference;
    }


    public String findBestProperty(Object[][] dataArray, int maxEntries) {
        double initialVariance = calculateVariance(dataArray, maxEntries);
        double maxVarianceReduction = 0.0;
        String bestProperty = "";

        // Start from 1 because 0 is typically "Student ID"
        for (int i = 1; i < maxEntries; i++) {
            // Specify the properties you want to consider

            String propertyName = dataArray[0][i].toString();
            if (propertyName.equals("Suruna Value") || propertyName.equals("Hurni Level")
                    || propertyName.equals("Lal Count") || propertyName.equals("Volta")) {

                // Calculate variance reduction for each property
                double varianceReduction = calculateVarianceReduction(dataArray, maxEntries, i, initialVariance);

                // Update if the current property has higher variance reduction
                if (varianceReduction > maxVarianceReduction) {
                    maxVarianceReduction = varianceReduction;
                    bestProperty = propertyName; // Update best property
                }
            }
        }

        // Print the best property
        return bestProperty;
    }


    public double calculateVariance(Object[][] dataArray, int maxEntries) {
        // Variables for variance calculation
        double sum = 0.0;
        double sumSquared = 0.0;
        int count = 0;

        // Loop through the data array
        for (int i = 1; i < maxEntries; i++) {
            for (int j = 1; j < dataArray.length; j++) {
                // Check if the element is a numerical value (instanceof Double)
                if (dataArray[j][i] instanceof Double) {
                    double value = (double) dataArray[j][i];
                    // Calculate sum, sum of squares, and increment count
                    sum += value;
                    sumSquared += value * value;
                    count++;
                }
            }
        }

        // Calculate mean
        double mean = sum / count;
        // Calculate variance using the formula
        double variance = (sumSquared / count) - (mean * mean);
        // Return the calculated variance
        return variance;
    }

    public double calculateVarianceReduction(Object[][] dataArray, int maxEntries, int propertyIndex, double initialVariance) {
        // Variables for variance reduction calculation
        double sumBeforeSplit = 0.0;
        double sumSquaredBeforeSplit = 0.0;
        int countBeforeSplit = 0;

        double sumAfterSplit = 0.0;
        double sumSquaredAfterSplit = 0.0;
        int countAfterSplit = 0;

        // Loop through the data array
        for (int j = 1; j < dataArray.length; j++) {
            // Check if the element is a numerical value (instanceof Double)
            if (dataArray[j][propertyIndex] instanceof Double && dataArray[j][propertyIndex].equals("NG")) {
                double value = (double) dataArray[j][propertyIndex];
                // Separate values into groups based on the property
                if (dataArray[j][propertyIndex].equals("NG")) {
                    // Values before the split
                    sumBeforeSplit += value;
                    sumSquaredBeforeSplit += value * value;
                    countBeforeSplit++;
                } else {
                    // Values after the split
                    sumAfterSplit += value;
                    sumSquaredAfterSplit += value * value;
                    countAfterSplit++;
                }
            }
        }
        // Calculate mean and variance for values before the split
        double meanBeforeSplit = sumBeforeSplit / countBeforeSplit;
        double varianceBeforeSplit = (sumSquaredBeforeSplit / countBeforeSplit) - (meanBeforeSplit * meanBeforeSplit);

        // Calculate mean and variance for values after the split
        double meanAfterSplit = sumAfterSplit / countAfterSplit;
        double varianceAfterSplit = (sumSquaredAfterSplit / countAfterSplit) - (meanAfterSplit * meanAfterSplit);
        // Calculate weighted variance reduction
        double weightedVarianceReduction = ((countBeforeSplit * varianceBeforeSplit) + (countAfterSplit * varianceAfterSplit)) / (countBeforeSplit + countAfterSplit);
        // Calculate overall variance reduction
        double varianceReduction = initialVariance - weightedVarianceReduction;

        // Return the calculated variance reduction
        return varianceReduction;
    }

    public void predictFuturePerformance(Object[][] dataArray, int maxEntries) {
        // Call the method to find the best property
        String bestProperty = findBestProperty(dataArray, maxEntries);

        // Print the best property
        System.out.println("The best property is: " + bestProperty);

        // Now you can use the information about the best property to make predictions
        // Assuming the best property is one of Suruna Value, Hurni Level, Lal Count, Volta
        int bestPropertyIndex = -1;
        for (int i = 1; i < maxEntries; i++) {
            if (dataArray[0][i].toString().equals(bestProperty)) {
                bestPropertyIndex = i;
                break;
            }
        }

        // Assuming the best property is a numeric value
        System.out.println("Predictions based on: " + bestProperty);
        // Set a threshold for categorizing students
        double threshold = 6.0;
        for (int j = 1; j < dataArray.length; j++) {
            if (dataArray[j][bestPropertyIndex] instanceof Double) {
                double propertyValue = (double) dataArray[j][bestPropertyIndex];

                String prediction = (propertyValue > threshold) ? "High Performer" : "Low Performer";

                System.out.println("Student ID: " + dataArray[j][0] + ", " + bestProperty + ": " + propertyValue + ", Prediction: " + prediction);
            }
        }
    }

    public void predictionOnNGCourses(Object[][] dataArray, Object[][] graduateDataArray, int[] studentsPerCourse, double[] graduateAverageGrade) {
        //ALEX 11/10/2023 In this function I'll make a prediction on the courses that contins NG
        //based on the graduates grades
        int easiestCourse = 1;
        int hardestCourse = 1;

        for (int i = 2; i < dataArray[0].length; i++) {
            if (graduateAverageGrade[i] > graduateAverageGrade[easiestCourse]) {
                easiestCourse = i;
            }
            if (graduateAverageGrade[i] < graduateAverageGrade[hardestCourse]) {
                hardestCourse = i;
            }
        }
        System.out.println("Easiest Course : " + dataArray[0][easiestCourse] + "\n" + "NG count= " + studentsPerCourse[easiestCourse]);
        System.out.println("Hardest Course : " + dataArray[0][hardestCourse] + "\n" + "NG count= " + studentsPerCourse[hardestCourse]);
    }

    public Object[] getStudentInfoComplete(Object[][] studentInfoArray, Object[][] dataArray) {
        ArrayList<Object[]> AllData = new ArrayList<Object[]>();
        Object[] finalArray = new Object[studentInfoArray[0].length - 1];

        for (int i = 1; i < studentInfoArray[0].length; i++) {
            String propName;
            ArrayList<String> differentProp = new ArrayList<String>();
            ArrayList<String> students = new ArrayList<String>();

            String[] StudentProp = new String[dataArray.length];
            differentProp.add(null);

            //finalArray[0]=studentInfoArray[0][i].toString();

            for (int j = 1; j < studentInfoArray.length; j++) {
                String propvalue = studentInfoArray[j][i].toString();
                if (!differentProp.contains(propvalue)) {
                    differentProp.add(propvalue);
                }
                StudentProp[j] = propvalue;
            }

            Object[] prpObj = new Object[differentProp.size()];
            prpObj[0] = studentInfoArray[0][i];
            for (int k = 0; k < differentProp.size(); k++) {
                ArrayList<Integer> studentPerProp = new ArrayList<Integer>();
                for (int st = 1; st < StudentProp.length; st++) {
                    if (StudentProp[st].equals(differentProp.get(k))) {
                        if (!studentPerProp.isEmpty()) {
                            int student_ID = (int) dataArray[st][0];
                            studentPerProp.add(student_ID);
                        } else {
                            studentPerProp.add(null);
                            int student_ID = (int) dataArray[st][0];
                            studentPerProp.add(student_ID);
                        }
                    }
                }
                if (!studentPerProp.isEmpty()) {
                    Object[] studentsPerPropArray = studentPerProp.toArray();
                    studentsPerPropArray[0] = differentProp.get(k);
                    prpObj[k] = studentsPerPropArray;
                }

            }
            finalArray[i - 1] = prpObj;
        }
        return finalArray;

    }

    public void displayInfoCurrentData(Object[] infoCurrentData, Object[][] dataArray, Object[][] studentInfoData) {
        String courseName = "";
        for (int i = 1; i < dataArray[0].length; i++) {
            courseName = dataArray[0][i].toString();
            System.out.println("\n\n" + "DIFFERENT SURUNA VALUES \n\n");
            differentSurunaValues(dataArray, courseName, studentInfoData);

            System.out.println("\n\n" + "DIFFERENT HURNI VALUES \n\n");
            differentHurni(dataArray, courseName, studentInfoData);

            System.out.println("\n\n" + "DIFFERENT LAL VALUES \n\n");
            differentLal(dataArray, courseName, studentInfoData);

            System.out.println("\n\n" + "DIFFERENT STAR VALUES \n\n");
            differentStarValues(dataArray, courseName, studentInfoData);
        }
    }

    public Object[] readProprety(Object[] infoCurrentData, String proprety) {
        Object[] retArray = new Object[1];
        for (int i = 0; i < infoCurrentData.length; i++) {

            Object[] props = (Object[]) infoCurrentData[i];
            retArray = new Object[props.length];
            if (props[0] == proprety) {
                retArray = props;
            } else {
                for (Object subprop : props) {
                    Object[] subpropArray = (Object[]) subprop;
                    retArray = new Object[props.length];
                    if (subpropArray[0] == proprety) {
                        retArray = subpropArray;
                    }
                }
            }
        }
        return retArray;
    }

    //Katarina
    //calculate the averages for different subgroups based on properties
    public double highHurniAvg(Object[][] dataArray, String courseName, Object[][] studentInfo) {
        double highHurniTotalScore = 0;
        int highHurniCount = 0;

        int courseIndex = -1;
        for (int i = 1; i < dataArray[0].length; i++) {
            if (dataArray[0][i].equals(courseName)) {
                courseIndex = i;
                break;
            }
        }

        for (int i = 1; i < studentInfo.length; i++) {
            String studentInfoHurni = studentInfo[i][2].toString();
            if (studentInfoHurni.equalsIgnoreCase("high")) {
                if (!dataArray[i][courseIndex].equals("NG")) {
                    highHurniTotalScore += (double) dataArray[i][courseIndex];
                    highHurniCount++;
                }
            }
        }
        double averageHighHurni = (double) highHurniTotalScore / highHurniCount;

        return averageHighHurni;
    }

    public double lowHurniAvg(Object[][] dataArray, String courseName, Object[][] studentInfo) {
        double lowHurniTotalScore = 0;
        int lowHurniCount = 0;

        int courseIndex = -1;
        for (int i = 1; i < dataArray[0].length; i++) {
            if (dataArray[0][i].equals(courseName)) {
                courseIndex = i;
                break;
            }
        }

        for (int i = 1; i < studentInfo.length; i++) {
            String studentInfoHurni = studentInfo[i][2].toString();
            if (studentInfoHurni.equalsIgnoreCase("low")) {
                if (!dataArray[i][courseIndex].equals("NG")) {
                    lowHurniTotalScore += (double) dataArray[i][courseIndex];
                    lowHurniCount++;
                }
            }
        }
        double averageLowHurni = (double) lowHurniTotalScore / lowHurniCount;

        return averageLowHurni;
    }

    public double highLalAvg(Object[][] dataArray, String courseName, Object[][] studentInfo) {
        double highLalTotalScore = 0;
        int highLalCount = 0;

        int courseIndex = -1;
        for (int i = 1; i < dataArray[0].length; i++) {
            if (dataArray[0][i].equals(courseName)) {
                courseIndex = i;
                break;
            }
        }

        // calculate average lal value
        double sumLal = 0;
        for (int i = 1; i < studentInfo.length; i++) {
            int value = (int) studentInfo[i][3];
            sumLal += value;
        }
        double averageLal = sumLal / (studentInfo.length - 1);
        // System.out.println(averageLal);

        // high lal level = above average
        for (int i = 1; i < studentInfo.length; i++) {
            int studentInfoLal = (int) studentInfo[i][3];
            if (studentInfoLal >= averageLal) {
                if (!dataArray[i][courseIndex].equals("NG")) {
                    highLalTotalScore += (double) dataArray[i][courseIndex];
                    highLalCount++;
                }
            }
        }
        double averageHighLal = (double) highLalTotalScore / highLalCount;

        return averageHighLal;
    }

    public double lowLalAvg(Object[][] dataArray, String courseName, Object[][] studentInfo) {
        double lowLalTotalScore = 0;
        int lowLalCount = 0;

        int courseIndex = -1;
        for (int i = 1; i < dataArray[0].length; i++) {
            if (dataArray[0][i].equals(courseName)) {
                courseIndex = i;
                break;
            }
        }

        // calculate average lal value
        double sumLal = 0;
        for (int i = 1; i < studentInfo.length; i++) {
            int value = (int) studentInfo[i][3];
            sumLal += value;
        }
        double averageLal = sumLal / (studentInfo.length - 1);
        // System.out.println(averageLal);

        // high lal level = above average
        for (int i = 1; i < studentInfo.length; i++) {
            int studentInfoLal = (int) studentInfo[i][3];
            if (studentInfoLal < averageLal) {
                if (!dataArray[i][courseIndex].equals("NG")) {
                    lowLalTotalScore += (double) dataArray[i][courseIndex];
                    lowLalCount++;
                }
            }
        }
        double averageLowLal = (double) lowLalTotalScore / lowLalCount;

        return averageLowLal;
    }


    // public  double AverageCourse(Object [][] dataArray, String courseName){
    //  int courseIndex = - 1;
    //     for (int i = 1; i < dataArray[0].length; i++) {
    //             if (dataArray[0][i].equals(courseName)) {
    //                 courseIndex = i;
    //               break;
    //           }
    //   }
    //    double sum = 0;
    //     double count = 0;
    //     for(int i = 1;i < dataArray.length; i++){
    //         if(!dataArray[i][courseIndex].equals("NG")){
    //         sum = sum + (double) dataArray[i][courseIndex];
    //        count ++;
    //    }
    //    double average = sum / count;
    //     return average;}


    //find the best property between Hurni and Lal
    public String findBestProperty2(Object[][] dataArray, String courseName, Object[][] studentInfo) {
        double differentLals = differentLal(dataArray, courseName, studentInfo);
        double differentHurnis = differentHurni(dataArray, courseName, studentInfo);
        double differentStars = differentStarValues(dataArray, courseName, studentInfo);
        double differentSaruna = differentSurunaValues(dataArray, courseName, studentInfo);
        String bestProperty;


        if (differentLals > differentHurnis && differentLals > differentStars && differentLals > differentSaruna) {
            bestProperty = "Lal";
        } else if (differentLals < differentHurnis && differentHurnis > differentStars && differentHurnis > differentSaruna) {
            bestProperty = "Hurni";
        } else if (differentLals < differentSaruna && differentHurnis < differentSaruna && differentStars < differentSaruna) {
            bestProperty = "Saruna";
        } else {
            bestProperty = "Stars";
        }
        return bestProperty;

    }

    //predict a grade for a specific student based on his grade
    public void studentPrediction(int studentID, Object[][] dataArray, String courseName, Object[][] studentInfo) {

        int studentIndex = -1;
        for (int i = 1; i < dataArray.length; i++) {
            if ((int) dataArray[i][0] == studentID) {
                studentIndex = i;
                break;
            }
        }
        System.out.print("prediction for student: " + studentID + " in course " + courseName + ":");

        System.out.println();
        String bestProperty = findBestProperty2(dataArray, courseName, studentInfo);
        if (bestProperty.equalsIgnoreCase("Hurni")) {
            String propertyValue = (String) studentInfo[studentIndex][2];
            if (propertyValue.equals("high")) {
                System.out.println("Since Hurni: " + propertyValue + ", then grade: " + lowHurniAvg(dataArray, courseName, studentInfo));
            } else if (propertyValue.equals("low")) {
                System.out.println("Since Hurni: " + propertyValue + ", then grade: " + lowHurniAvg(dataArray, courseName, studentInfo));
            } else {
                bestProperty = "Lal";
                int propertyValue1 = (int) studentInfo[studentIndex][3];
                if (propertyValue1 > 79) {
                    System.out.println("Since Lal: " + propertyValue1 + ", then grade: " + highLalAvg(dataArray, courseName, studentInfo));
                }
                if (propertyValue1 < 79) {
                    System.out.println("Since Lal: " + propertyValue1 + ", then grade: " + lowLalAvg(dataArray, courseName, studentInfo));
                }
            }
        }
        //average Lal = 79
        else if (bestProperty.equalsIgnoreCase("Lal")) {
            int propertyValue = (int) studentInfo[studentIndex][3];
            if (propertyValue > 79) {
                System.out.println("Since Lal: " + propertyValue + ", then grade: " + highLalAvg(dataArray, courseName, studentInfo));
            }
            if (propertyValue < 79) {
                System.out.println("Since Lal: " + propertyValue + ", then grade: " + lowLalAvg(dataArray, courseName, studentInfo));
            }
        }
        // if doot we add, if the other we substract.
        else if (bestProperty.equalsIgnoreCase("Suruna")) {
            String propertyValueS = (String) studentInfo[studentIndex][1];
            if (propertyValueS.equals("doot")) {
                System.out.println("Since Suruna " + propertyValueS + ", then grade: " + surunaavg(dataArray, courseName, studentInfo));
            }

        } else {
            String propertyValue2 = (String) studentInfo[studentIndex][4];
            System.out.println("Since Volta: " + propertyValue2 + ", then grade: " + starAvg(dataArray, courseName, studentInfo, propertyValue2));

        }
    }

    public double differentStarValues(Object[][] dataArray, String courseName, Object[][] studentInfo) {
        double FiveStarValue = 0;
        int FiveStarCount = 0;
        double FourStarValue = 0;
        int FourStarCount = 0;
        double ThreeStarValue = 0;
        int ThreeStarCount = 0;
        double TwoStarValue = 0;
        int TwoStarCount = 0;
        double OneStarValue = 0;
        int OneStarCount = 0;

        int courseIndex = 1;
        for (int i = 1; i < dataArray[0].length; i++) {
            if (dataArray[0][i].equals(courseName)) {
                courseIndex = i;
                break;
            }
        }

        for (int i = 1; i < studentInfo.length; i++) {
            String studentInfoStars = studentInfo[i][4].toString();
            if (studentInfoStars.toString().equalsIgnoreCase("5 stars")) {
                if (!dataArray[i][courseIndex].toString().contains("NG")) {
                    FiveStarValue += (double) dataArray[i][courseIndex];
                    FiveStarCount++;
                }
            }
        }
        double averageFiveStar = (double) FiveStarValue / FiveStarCount;

        //System.out.println("the average grade of 5 star students for the course: " + dataArray[0][courseIndex] + " is " + averageFiveStar);

        for (int i = 1; i < studentInfo.length; i++) {
            String studentInfoStars = studentInfo[i][4].toString();
            if (studentInfoStars.equalsIgnoreCase("4 stars")) {
                if (!dataArray[i][courseIndex].equals("NG")) {
                    FourStarValue += (double) dataArray[i][courseIndex];
                    FourStarCount++;
                }
            }
        }
        double averageFourStar = (double) FourStarValue / FourStarCount;
        //System.out.println("the average grade of 4 star students for the course: " + dataArray[0][courseIndex] + " is " + averageFiveStar);

        for (int i = 1; i < studentInfo.length; i++) {
            String studentInfoStars = studentInfo[i][4].toString();
            if (studentInfoStars.equalsIgnoreCase("3 stars")) {
                if (!dataArray[i][courseIndex].equals("NG")) {
                    ThreeStarValue += (double) dataArray[i][courseIndex];
                    ThreeStarCount++;
                }
            }
        }
        double averageThreeStar = (double) ThreeStarValue / ThreeStarCount;
        //System.out.println("the average grade of 3 star students for the course: " + dataArray[0][courseIndex] + " is " + averageThreeStar);

        for (int i = 1; i < studentInfo.length; i++) {
            String studentInfoStars = studentInfo[i][4].toString();
            if (studentInfoStars.equalsIgnoreCase("2 stars")) {
                if (!dataArray[i][courseIndex].equals("NG")) {
                    TwoStarValue += (double) dataArray[i][courseIndex];
                    TwoStarCount++;
                }
            }
        }

        double averageTwoStar = (double) TwoStarValue / TwoStarCount;
        //System.out.println("the average grade of 2 star students for the course: " + dataArray[0][courseIndex] + " is " + averageTwoStar);

        for (int i = 1; i < studentInfo.length; i++) {
            String studentInfoStars = studentInfo[i][4].toString();
            if (studentInfoStars.equalsIgnoreCase("1 star")) {
                if (!dataArray[i][courseIndex].equals("NG")) {
                    OneStarValue += (double) dataArray[i][courseIndex];
                    OneStarCount++;
                }
            }
        }
        double averageOneStar = (double) OneStarValue / OneStarCount;
        //System.out.println("the average grade of 1 star students for the course: " + dataArray[0][courseIndex] + " is " + averageOneStar);

        double difference1 = Math.abs(averageFiveStar - averageOneStar);
        double difference2 = Math.abs(averageFourStar - averageTwoStar);
        System.out.print("the difference in average grade between 5 and 1 star students is: " + difference1 + "\nthe difference in average grade between 4 and 2 star students is: " + difference2);
        return difference1;
    }

    public double starAvg(Object[][] dataArray, String courseName, Object[][] studentInfo, String stars) {
        double FiveStarValue = 0;
        int FiveStarCount = 0;
        double FourStarValue = 0;
        int FourStarCount = 0;
        double ThreeStarValue = 0;
        int ThreeStarCount = 0;
        double TwoStarValue = 0;
        int TwoStarCount = 0;
        double OneStarValue = 0;
        int OneStarCount = 0;

        int courseIndex = 1;
        for (int i = 1; i < dataArray[0].length; i++) {
            if (dataArray[0][i].equals(courseName)) {
                courseIndex = i;
                break;
            }
        }

        for (int i = 1; i < studentInfo.length; i++) {
            String studentInfoStars = studentInfo[i][4].toString();
            if (studentInfoStars.toString().equalsIgnoreCase("5 stars")) {
                if (!dataArray[i][courseIndex].toString().contains("NG")) {
                    FiveStarValue += (double) dataArray[i][courseIndex];
                    FiveStarCount++;
                }
            }
        }
        double averageFiveStar = (double) FiveStarValue / FiveStarCount;


        for (int i = 1; i < studentInfo.length; i++) {
            String studentInfoStars = studentInfo[i][4].toString();
            if (studentInfoStars.equalsIgnoreCase("4 stars")) {
                if (!dataArray[i][courseIndex].equals("NG")) {
                    FourStarValue += (double) dataArray[i][courseIndex];
                    FourStarCount++;
                }
            }
        }
        double averageFourStar = (double) FourStarValue / FourStarCount;

        for (int i = 1; i < studentInfo.length; i++) {
            String studentInfoStars = studentInfo[i][4].toString();
            if (studentInfoStars.equalsIgnoreCase("3 stars")) {
                if (!dataArray[i][courseIndex].equals("NG")) {
                    ThreeStarValue += (double) dataArray[i][courseIndex];
                    ThreeStarCount++;
                }
            }
        }
        double averageThreeStar = (double) ThreeStarValue / ThreeStarCount;

        for (int i = 1; i < studentInfo.length; i++) {
            String studentInfoStars = studentInfo[i][4].toString();
            if (studentInfoStars.equalsIgnoreCase("2 stars")) {
                if (!dataArray[i][courseIndex].equals("NG")) {
                    TwoStarValue += (double) dataArray[i][courseIndex];
                    TwoStarCount++;
                }
            }
        }

        double averageTwoStar = (double) TwoStarValue / TwoStarCount;

        for (int i = 1; i < studentInfo.length; i++) {
            String studentInfoStars = studentInfo[i][4].toString();
            if (studentInfoStars.equalsIgnoreCase("1 star")) {
                if (!dataArray[i][courseIndex].equals("NG")) {
                    OneStarValue += (double) dataArray[i][courseIndex];
                    OneStarCount++;
                }
            }
        }
        double averageOneStar = (double) OneStarValue / OneStarCount;

        switch (stars) {
            case "5 stars":
                return averageFiveStar;
            case "4 stars":
                return averageFourStar;
            case "3 stars":
                return averageThreeStar;
            case "2 stars":
                return averageTwoStar;
            case "1 star":
                return averageOneStar;

        }
        return 0;
    }


    // Katrina code
    public double nothingHurniAvg(Object[][] dataArray, String courseName, Object[][] studentInfo) {
        double nothingHurniTotalScore = 0;
        int nothingHurniCount = 0;

        int courseIndex = -1;
        for (int i = 1; i < dataArray[0].length; i++) {
            if (dataArray[0][i].equals(courseName)) {
                courseIndex = i;
                break;
            }
        }

        for (int i = 1; i < studentInfo.length; i++) {
            String studentInfoHurni = studentInfo[i][2].toString();
            if (studentInfoHurni.equalsIgnoreCase("nothing")) {
                if (!dataArray[i][courseIndex].equals("NG")) {
                    nothingHurniTotalScore += (double) dataArray[i][courseIndex];
                    nothingHurniCount++;
                }
            }
        }
        double averageNothingHurni = (double) nothingHurniTotalScore / nothingHurniCount;

        return averageNothingHurni;
    }

    public double fullHurniAvg(Object[][] dataArray, String courseName, Object[][] studentInfo) {
        double fullHurniTotalScore = 0;
        int fullHurniCount = 0;

        int courseIndex = -1;
        for (int i = 1; i < dataArray[0].length; i++) {
            if (dataArray[0][i].equals(courseName)) {
                courseIndex = i;
                break;
            }
        }

        for (int i = 1; i < studentInfo.length; i++) {
            String studentInfoHurni = studentInfo[i][2].toString();
            if (studentInfoHurni.equalsIgnoreCase("full")) {
                if (!dataArray[i][courseIndex].equals("NG")) {
                    fullHurniTotalScore += (double) dataArray[i][courseIndex];
                    fullHurniCount++;
                }
            }
        }
        double averageFullHurni = (double) fullHurniTotalScore / fullHurniCount;

        return averageFullHurni;
    }

    public double mediumHurniAvg(Object[][] dataArray, String courseName, Object[][] studentInfo) {
        double mediumHurniTotalScore = 0;
        int mediumHurniCount = 0;

        int courseIndex = -1;
        for (int i = 1; i < dataArray[0].length; i++) {
            if (dataArray[0][i].equals(courseName)) {
                courseIndex = i;
                break;
            }
        }

        for (int i = 1; i < studentInfo.length; i++) {
            String studentInfoHurni = studentInfo[i][2].toString();
            if (studentInfoHurni.equalsIgnoreCase("medium")) {
                if (!dataArray[i][courseIndex].equals("NG")) {
                    mediumHurniTotalScore += (double) dataArray[i][courseIndex];
                    mediumHurniCount++;
                }
            }
        }
        double averageMediumHurni = (double) mediumHurniTotalScore / mediumHurniCount;

        return averageMediumHurni;
    }//KATARINA 31.10.

    public void decisionStumps(int studentID, Object[][] dataArray, String courseName, Object[][] studentInfo) {
        int studentIndex = -1;
        for (int i = 1; i < dataArray.length; i++) {
            if ((int) dataArray[i][0] == studentID) {
                studentIndex = i;
                break;
            }
        }

        //craete decision stumps
        //average based on properties
        String hurni = (String) studentInfo[studentIndex][2];
        double avgHurni;
        switch (hurni) {
            case "high":
                avgHurni = highHurniAvg(dataArray, courseName, studentInfo);
                break;
            case "low":
                avgHurni = lowHurniAvg(dataArray, courseName, studentInfo);
                break;
            case "full":
                avgHurni = fullHurniAvg(dataArray, courseName, studentInfo);
                break;
            case "medium":
                avgHurni = mediumHurniAvg(dataArray, courseName, studentInfo);
                break;
        }

        double lal = (double) studentInfo[studentIndex][3];
        double avgLal;
        if (lal > 79) {
            avgLal = lowLalAvg(dataArray, courseName, studentInfo);
        } else {
            avgLal = highLalAvg(dataArray, courseName, studentInfo);
        }

        String voltaValue = (String) studentInfo[studentIndex][4];
        double avgVolta = starAvg(dataArray, courseName, studentInfo, voltaValue);
    }

    private double displayAverageForCourse(Object[][] dataArray, int maxEntries, double[] averageGrade,
                                           int maxLines, int[] studentsPerCourse, String courseName) {
        int courseIndex = findCourseIndex(dataArray[0], courseName);

        if (courseIndex == -1) {
            System.out.println("Course not found: " + courseName);
            return 0;
        }

        System.out.println("Average for course " + courseName + ": " + averageGrade[courseIndex]);
        return averageGrade[courseIndex];
    }

    // Function to find the index of a course in the dataArray
    private int findCourseIndex(Object[] header, String courseName) {
        for (int i = 1; i < header.length; i++) {
            if (courseName.equals(header[i])) {
                return i;
            }
        }
        return -1; // Course not found
    }


    // NIKITAS 10/10/2023:
    // Compares and displays the strongest correlation


    public double[] getGraduateStudentsAverageGrade() {
        return graduateStudentsAverageGrade;
    }

    public int[] getGraduateStudentsPerCourse() {
        return graduateStudentsPerCourse;
    }

    public Object[][] getStudentInfoData() {
        return studentInfoData;
    }

    public Object[][] getCurrentStudentsDataArray() {
        return currentStudentsDataArray;
    }

    public double[] getCurrentStudentsAverageGrade() {
        return currentStudentsAverageGrade;
    }

    public Object[][] getGraduateStudentsDataArray() {
        return graduateStudentsDataArray;
    }

    public int[] getCurrentStudentsPerCourse() {
        return currentStudentsPerCourse;
    }

    public ObservableList<Student> getCurrentStudents() {
        return currentStudents;
    }

    public ObservableList<Student> getGraduateStudents() {
        return graduateStudents;
    }

    public ObservableList<Course> getCurrentStudents_Courses() {
        return currentStudents_Courses;
    }

    public ObservableList<Course> getGraduateStudents_Courses() {
        return graduateStudents_Courses;
    }
    public void createFilteredCurrentCoursesList(ObservableList<Student> studentsList, ObservableList<String> courses) {
        filtered_currentStudents_Courses = FXCollections.observableArrayList();
        selectedCourses = FXCollections.observableArrayList(courses);
        if (Objects.nonNull(studentsList)) {
            filtered_currentStudents = FXCollections.observableArrayList(studentsList);
            for (String courseName : selectedCourses) {
                Course course_obj = new Course(courseName);
                course_obj.setNgCount(getNGByPropertyAndCourse(courseName));
                course_obj.setAverageGrade(getFilteredGpa(courseName, course_obj.getNgCount()));
                course_obj.setMean(getFilteredGpa(courseName, course_obj.getNgCount()));
                course_obj.setMedian(getFilteredMedian(courseName, courses.size(), course_obj.getNgCount()));
                course_obj.setStd_deviation(getFilteredStdDev(courseName, courses.size(), course_obj.getMean()));
                filtered_currentStudents_Courses.add(course_obj);
            }
        } else {
            for (String courseName : selectedCourses) {
                for (Course c : currentStudents_Courses) {
                    if (c.getName().equals(courseName)) {
                        filtered_currentStudents_Courses.add(c);
                    }
                }
            }
        }

    }

    public void createFilteredGraduateCoursesList(ObservableList<String> courses) {
        filtered_graduateStudents_Courses = FXCollections.observableArrayList();
        selectedCourses = FXCollections.observableArrayList(courses);
        for (String courseName : selectedCourses) {
            for (Course c : currentStudents_Courses) {
                if (c.getName().equals(courseName)) {
                    filtered_graduateStudents_Courses.add(c);
                }
            }
        }
    }

    private double getFilteredGpa(String courseName, int ngCount) {
        double count_gpa = 0;
        double gpa = 0;
        for (Student st : filtered_currentStudents) {
            double grade = 0;
            //I start from here by retriving the grrade for each student and
            //adding it to the count for the avarage grade
            for (StudentGrade grade_obj : st.getCoursesGrades()) {
                if (Objects.nonNull(grade_obj)) {
                    if (grade_obj.getCourseName().equals(courseName) && grade_obj.getCourseGrade() != 0) {
                        grade = grade_obj.getCourseGrade();
                    }
                } else {
                    continue;
                }
            }
            count_gpa += grade;
        }
        gpa = count_gpa / (filtered_currentStudents.size() - ngCount);
        return gpa;
    }

    private double getFilteredMedian(String courseName, int coursesCount, int ngCount) {
        // create and array with the values of each column
        double columnData[] = new double[filtered_currentStudents.size() - ngCount];
        int count = 0;
        for (Student s : filtered_currentStudents) {
            for (StudentGrade grade_obj : s.getCoursesGrades()) {
                if (Objects.nonNull(grade_obj)) {
                    double grade = grade_obj.getCourseGrade();
                    if (grade_obj.getCourseName().equals(courseName) && grade != 0) {
                        columnData[count] = grade;
                        count++;
                    }
                }
            }
        }
        // sort the values in the array
        Arrays.sort(columnData);

        int middle = columnData.length / 2;
        double median;
        if (columnData.length != 0) {
            // check if the number of values in the array is odd or even
            if (columnData.length % 2 == 0) {
                median = ((columnData[middle - 1]) + columnData[middle]) / 2.0;
            } else {
                median = columnData[middle];
            }
        } else {
            median = 0;
        }
        return median;
    }

    private double getFilteredStdDev(String courseName, int coursesCount, double mean) {
        double columnData[] = new double[filtered_currentStudents.size()];
        int j = 0;
        for (Student s : filtered_currentStudents) {
            for (StudentGrade grade_obj : s.getCoursesGrades()) {
                if (Objects.nonNull(grade_obj)) {
                    double grade = grade_obj.getCourseGrade();
                    if (grade_obj.getCourseName().equals(courseName)) {
                        columnData[j] = grade;
                        j++;
                    }
                }
            }
        }
        // calculate the sum of squared differences from the mean
        double sumOfSqrtDifferences = 0;
        for (int i = 0; i < columnData.length; i++) {
            sumOfSqrtDifferences += Math.pow(columnData[i] - mean, 2);
        }
        // calculate the standard deviation
        double standardDeviation = Math.sqrt(sumOfSqrtDifferences / (filtered_currentStudents.size()));
        return standardDeviation;
    }

    public int getNGByPropertyAndCourse(String courseName) {
        int ngCount = 0;
        ObservableList<StudentGrade> gradesGroup = FXCollections.observableArrayList();
        for (Student st : filtered_currentStudents) {

            for (StudentGrade grade_obj : st.getCoursesGrades()) {
                double grade = 0;
                if (Objects.nonNull(grade_obj)) {
                    if (grade_obj.getCourseName().equals(courseName) && grade_obj.getCourseGrade() == 0) {
                        ngCount++;
                    }

                }
            }
        }
        return ngCount;
    }

    public int getFilteredCumLaude() {
        int countNG = 0;
        int cumlaudeCount = 0;
        for (Student st : filtered_currentStudents) {
            double sum = 0;
            double average = 0;
            boolean grade7 = false;
            double lowergrade = 10;
            for (StudentGrade grade_obj : st.getCoursesGrades()) {
                if (Objects.nonNull(grade_obj)) {
                    sum += grade_obj.getCourseGrade();
                    if (grade_obj.getCourseGrade() >= 7 && lowergrade >= 7) {
                        grade7 = true;
                    } else if (grade_obj.getCourseGrade() == 0) {
                        countNG++;
                    } else {
                        lowergrade = grade_obj.getCourseGrade();
                    }
                }
            }
            average = sum / (selectedCourses.size()) - countNG;
            if (average >= 8.0 && grade7) {
                cumlaudeCount++;
            }
        }
        return cumlaudeCount;
    }

    public ObservableList<Student> getFiltered_currentStudents() {
        return filtered_currentStudents;
    }

    public void setFiltered_currentStudents(ObservableList<Student> filtered_currentStudents) {
        this.filtered_currentStudents = filtered_currentStudents;
    }

    public ObservableList<Course> getFiltered_currentStudents_Courses() {
        return filtered_currentStudents_Courses;
    }

    public ObservableList<Course> getFiltered_graduateStudents_Courses() {
        return filtered_graduateStudents_Courses;
    }

    public void setFiltered_currentStudents_Courses(ObservableList<Course> filtered_currentStudents_Courses) {
        this.filtered_currentStudents_Courses = filtered_currentStudents_Courses;
    }

    public static ObservableList<StudentGrade> getGradesForCourse(ObservableList<Student> students, String course) {
        List<StudentGrade> grades=students.stream()
                .flatMap(student -> student.getCoursesGrades().stream())
                .filter(studentGrade -> studentGrade.getCourseName().equals(course))
                .map(studentGrade -> new StudentGrade(String.valueOf(studentGrade.getParent().getId()), studentGrade.getCourseGrade()))
                .collect(Collectors.toList());
        return FXCollections.observableArrayList(grades);

    }
    public List<Student> getCurrentStudentsWithGradesForCourse(String course){
        return currentStudents.stream()
                .filter(student -> student.getGradeForCourse(course) > 0)
                .toList();
    }
    public  ObservableList<Student> getFilteredStudentsList(String selection, String property, String propertyValue, FileDisplayer file_displayer){
        ObservableList<Student> studentsList= FXCollections.observableArrayList();
        if (selection.equals("Current Students")) {
            studentsList=file_displayer.getCurrentStudents();
        } else if(selection.equals("Graduate Students")) {
            studentsList=file_displayer.getCurrentStudents();
        }
        switch (property){
            case(surunaPath):
                return studentsList.stream().filter(student -> student.getInfo().getSurunaValue().equals(propertyValue))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
            case(hurniPath):
                return studentsList.stream().filter(student -> student.getInfo().getHurniLevel().equals(propertyValue))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
            case(lalPath):
                return studentsList.stream().filter(student -> Integer.toString(student.getInfo().getLalCount()).equals(propertyValue))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
            case(voltaPath):
                return studentsList.stream().filter(student -> student.getInfo().getVolta().equals(propertyValue))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
            default:
                return null;
        }
    }
}