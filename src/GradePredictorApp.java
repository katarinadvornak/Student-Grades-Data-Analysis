package src;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.*;

public class GradePredictorApp extends Application {
    private static final int treesNumber= 100;
    private static final int featuresNumber= 4;
    private FileDisplayer displayer;
    private Stage parentStage;
    private TextField studentIdField;
    private ListView<String> missingCoursesList;
    private ListView<String> predictionList;
    private TextField debugTextField;
    public GradePredictorApp(FileDisplayer displayer){
        this.displayer=displayer;
    }
    @Override
    public void start(Stage primaryStage) {
        // Root pane for our layout
        GridPane root = new GridPane();
        root.setPadding(new Insets(10));
        root.setHgap(10);
        root.setVgap(10);

        // TextField for student ID
        studentIdField = new TextField();
        studentIdField.setPromptText("STUDENT_ID");
        GridPane.setConstraints(studentIdField, 0, 0, 2, 1);

        // ListView for missing courses
        missingCoursesList = new ListView<>();
        GridPane.setConstraints(missingCoursesList, 0, 1);
        missingCoursesList.setMaxHeight(Double.MAX_VALUE);  // Set the maximum height to Double.MAX_VALUE
        // ListView for predictions
        predictionList = new ListView<>();
        GridPane.setConstraints(predictionList, 1, 1);
        predictionList.setMaxHeight(Double.MAX_VALUE);  // Set the maximum height to Double.MAX_VALUE

        // Button to trigger prediction (Assuming you have a method to handle this)
        Button predictButton = new Button("Predict");
        predictButton.setOnAction(this::predictionButtonPushEvent);
        GridPane.setConstraints(predictButton, 0, 2, 1, 1);

        // Add all components to the root pane
        root.getChildren().addAll(studentIdField, missingCoursesList, predictionList, predictButton);

        // Button for tree debugging (visible only in debug mode)
        Button treeDebugButton = new Button("Tree Debug");
        treeDebugButton.setOnAction(this::treeDebugButtonPushEvent);
        GridPane.setConstraints(treeDebugButton, 0, 3, 1, 1);

        // TextField next to the treeDebugButton
        debugTextField = new TextField();
        debugTextField.setPromptText("COURSE NAME");
        GridPane.setConstraints(debugTextField, 1, 3, 1, 1);


        Separator separator = new Separator(Orientation.HORIZONTAL);
        GridPane.setConstraints(separator, 0, 2, 2, 2); // Adjust the constraints as needed
        root.getChildren().add(separator);


        // Add both the Tree Debug button and TextField under the second ListView
        root.getChildren().addAll(treeDebugButton, debugTextField);

        // Set the scene and stage
        Scene scene = new Scene(root, 500, 400);
        primaryStage.setTitle("Grade Predictor");
        primaryStage.setScene(scene);
        primaryStage.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((obs, oldWindow, newWindow) -> {
                    if (newWindow != null) {
                        // Window is shown
                        ScrollBar scrollBar1 = (ScrollBar) missingCoursesList.lookup(".scroll-bar:vertical");
                        ScrollBar scrollBar2 = (ScrollBar) predictionList.lookup(".scroll-bar:vertical");
                        if (scrollBar1 != null && scrollBar2 != null) {
                            scrollBar1.valueProperty().bindBidirectional(scrollBar2.valueProperty());
                        }
                    }
                });
            }
        });
        primaryStage.show();
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(event -> {
            // Handle the close request here
            parentStage.show();

            // If you want to consume the event and prevent the window from closing, uncomment the following line:
            // event.consume();
        });
        syncScrollBars();

    }
    private void treeDebugButtonPushEvent(ActionEvent event){
        try {
            Course course=new Course(debugTextField.getText());
            List<Student> studentsWithGrade=displayer.getCurrentStudentsWithGradesForCourse(course.getName());;
            //RandomForest forest=new RandomForest(100,course,studentsWithGrade);
            //System.out.println("done");
            RegressionTree tree=new RegressionTree(Utils.convertStudentsToDataPoints(studentsWithGrade,course),2);
            TreeVisualization treeDisplayer=new TreeVisualization(tree.getRoot());
            Stage treeStage=new Stage();
            treeDisplayer.start(treeStage);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    private void predictionButtonPushEvent(ActionEvent event){
        int selectedId=Integer.parseInt(studentIdField.getText());
        Student selectedStudent = null;
        for (Student st:displayer.getCurrentStudents()){
            if(st.getId()==selectedId){
                selectedStudent=st;
            }
        }
        /////

        //RegressionTree tree=new RegressionTree(dataPoints,4);
        assert selectedStudent != null;
        //double grade=tree.predictGrade(StudentUtils.convertStudentToDataPoint(selectedStudent,course));
        /////
        predictMissingCourses(selectedStudent);
        syncScrollBars();
        //now I will construct the random forest based on a data set without the selected student

        //RandomForest forest=new RandomForest(200,,newStudentList)
    }
    private void predictMissingCourses(Student selectedStudent){
        List<Course> coursesList=displayer.getCurrentStudents_Courses();
        List<Course> missingCourses= new ArrayList<>();
        List<Prediction> predictions = new ArrayList<>();
        try{
            //Now i will create an HasMap that contains possible grades from 1 to 10 as key
            //values and the count of predictions for that specific grade
            for (int i=0;i<coursesList.size();i++){
                StudentGrade grade=selectedStudent.getCoursesGrades().get(i);
                if(grade.getCourseGrade()==0){
                    missingCourses.add(coursesList.get(i));
                }
            }
            for (Course missingCourse:missingCourses){
                List<Student> studentsWithGrade=displayer.getCurrentStudentsWithGradesForCourse(missingCourse.getName());
                if(!studentsWithGrade.isEmpty()){

                    //I remove the student from the list to avoid overfit
                    //first thing first I create a new datapoint of the student based on this course
                    DataPoint studentDataPoint= Utils.convertStudentToDataPoint(selectedStudent,missingCourse);
                    List<DataPoint> dataPoints= Utils.convertStudentsToDataPoints(studentsWithGrade,missingCourse);

                    //now I will take the best random forest for each course
                    RandomForest forest=getBestForest(treesNumber,dataPoints,featuresNumber);
                    HashMap<Double,Integer> gradesDictionary=getGradesDictionary(forest, studentDataPoint);
                    Map.Entry<Double,Integer> bestEntry = getBestEntry(gradesDictionary);
                    assert bestEntry != null;
                    predictions.add(new Prediction(missingCourse.getName(),bestEntry.getKey()));
                }else{
                    double predictionOnGraduates=getPredictionOnCourseWithoutGrade(missingCourse);
                    predictions.add(new Prediction(missingCourse.getName(),predictionOnGraduates));
                }
            }
            populateListView(missingCourses,predictions);
        }catch (Exception ex){
            System.out.println(ex.getLocalizedMessage());
        }
    }
    private void populateListView(List<Course> missingCourses,List<Prediction> predictions){
        ObservableList<String> missingCoursesNames= FXCollections.observableArrayList();
        ObservableList<String> predictionGradesString= FXCollections.observableArrayList();
        for(int i=0;i<missingCourses.size();i++){
            double grade=predictions.get(i).getPredictedGrade();
            double roundedGrade=Utils.customRound(grade);
            missingCoursesNames.add(missingCourses.get(i).getName());
            predictionGradesString.add(String.valueOf(roundedGrade));
        }
        missingCoursesList.setItems(missingCoursesNames);
        predictionList.setItems(predictionGradesString);
    }
    private Map.Entry<Double,Integer> getBestEntry(HashMap<Double,Integer> gradesDictionary){
        Map.Entry<Double,Integer> bestEntry = null;
        for (Map.Entry<Double,Integer> entry:gradesDictionary.entrySet()){
            if(Objects.nonNull(bestEntry)){
                if(entry.getValue()>bestEntry.getValue()){
                    bestEntry=entry;
                }
            }else{
                bestEntry=entry;
            }
        }
        return bestEntry;
    }
    private HashMap<Double,Integer> getGradesDictionary(RandomForest forest,DataPoint studentDataPoint){
        HashMap<Double,Integer> gradesDictionary=new HashMap<>();
        for(RegressionTree tree:forest.getTrees()){
            double predictedGrade= tree.predictGrade(studentDataPoint);
            if(gradesDictionary.containsKey(predictedGrade)){
                int count=gradesDictionary.get(predictedGrade);
                gradesDictionary.replace(predictedGrade,count,count+1);
            }else{
                gradesDictionary.put(predictedGrade,1);
            }
        }
        return  gradesDictionary;
    }
    private RandomForest getBestForest(int treesNumber,List<DataPoint> dataPoints,int featuresCount){
        /*i will take into account 3 kind of forests
          -one considering the square of the features (2)
          -one considering 1 value more (3)
          -one considering 2 values more (4)
        */
        List<RandomForest> forests=new ArrayList<>();
        int firstForestFeatCount=(int) Math.sqrt(featuresCount);
        for(int i=0;i<3;i++){
            int thisForestFeatCount=firstForestFeatCount+i;
            forests.add(new RandomForest(treesNumber,dataPoints,thisForestFeatCount));
        }
        forests.sort(Comparator.comparingDouble(RandomForest::getMSE));
        return forests.getFirst();
    }

    private double getPredictionOnCourseWithoutGrade(Course course){
        double predictedGrade=0;
        List<Course> courses_grad=new ArrayList<>(displayer.getGraduateStudents_Courses());
        Optional<Course> thisCourse=courses_grad.stream().
                filter(c->c.getName().equals(course.getName())).
                findFirst();
        return thisCourse.map(Course::getAverageGrade).orElse(predictedGrade);
    }
    private void syncScrollBars(){
        // ScrollBar synchronization
        ScrollBar listView1ScrollBar = getListViewScrollBar(missingCoursesList);
        ScrollBar listView2ScrollBar = getListViewScrollBar(predictionList);

        listView1ScrollBar.valueProperty().addListener((obs, oldValue, newValue) ->
                listView2ScrollBar.setValue(newValue.doubleValue()));

        listView2ScrollBar.valueProperty().addListener((obs, oldValue, newValue) ->
                listView1ScrollBar.setValue(newValue.doubleValue()));

    }
    private ScrollBar getListViewScrollBar(ListView<?> listView) {
        ScrollBar scrollbar = null;
        for (javafx.scene.Node node : listView.lookupAll(".scroll-bar")) {
            if (node instanceof ScrollBar) {
                scrollbar = (ScrollBar) node;
                if (scrollbar.getOrientation() == javafx.geometry.Orientation.VERTICAL) {
                    break;
                }
            }
        }
        return scrollbar;
    }

    public void setParentStage(Stage parentStage) {
        this.parentStage = parentStage;
    }
    public static void main(String[] args) {
        launch(args);
    }
}
