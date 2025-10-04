package src;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class CoursesAnalyst extends Application{
    private static final String LABEL_STYLE = "-fx-font-size: 14px; -fx-text-fill: #333333;";
    private static final String LIST_VIEW_STYLE = "-fx-border-color: #cccccc; -fx-border-radius: 5px;";
    private static final String BUTTON_STYLE = "-fx-background-color: #5cb85c; -fx-text-fill: white; -fx-border-radius: 5px;";
    private static final String PANE_STYLE = "-fx-background-color: #f0f0f0; -fx-padding: 10px;";
    private static  final String ngPath="ng_count";
    private static  final String cumlaudePath="cumlaude";
    private static  final String dropOutsPath="dropouts";
    private static  final String statisticsPath ="statistics";
    private static  final String meanPath ="mean";
    private static  final String medianPath ="median";
    private static  final String std_devPath ="std_deviation";
    private static  final String lineChartPath="lineChart";
    private static  final String barChartPath="barChart";
    private static  final String pieChartPath="pieChart";
    private static  final String areaChartPath="areaChart";
    private static  final String scatterChartPath="scatterChart";
    private static  final String surunaPath="suruna";
    private static  final String hurniPath="hurni";
    private static  final String lalPath="lal";
    private static  final String voltaPath="volta";
    private FileDisplayer displayer;
    private BarChart<String, Number> barChart;
    private PieChart pieChart;
    private LineChart<String, Number> lineChart;
    private AreaChart<String, Number> areaChart;
    private ScatterChart<String, Number> scatterChart;
    private StackedBarChart<String,Number> stackedBarChart;
    private StackedAreaChart<String,Number> stackedAreaChart;
    private StackPane graphContainer;
    private ComboBox<ComboBoxElement> cbGraphType;
    private ComboBox<ComboBoxElement> cbResult;
    private ComboBox<ComboBoxElement> cbProperty;
    private ListView<Set> listviewDataSet;
    private ListView<String> listViewProperties;
    private ListView<ListCheckboxElement> listViewCourses;
    private CheckBox checkboxSelectAll;
    private ObservableList<String> selectedCoursesList;
    private CheckBox applyFiltersCheckbox;
    private  Pane filtersPane1;
    private  Pane filtersPane2;
    private Stage stage2;
    private Stage parentStage;
    public CoursesAnalyst(FileDisplayer displayer){
        this.displayer=displayer;
    }
    @Override
    public void start(Stage primaryStage) {
        createMainMenu(primaryStage);
        createGraphWindowLayout();
        primaryStage.setOnCloseRequest(event -> {
            // Handle the close request here
            parentStage.show();

            // If you want to consume the event and prevent the window from closing, uncomment the following line:
            // event.consume();
        });

    }
    private void createMainMenu(Stage primaryStage){
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(537.0, 450.0);

        HBox centerHBox = new HBox();
        centerHBox.setPrefSize(540.0, 425.0);

        // Pane 1 in HBox
        Pane pane1 = createFirstPane();

        // Pane 2 in HBox
        Pane pane2 = createSecondPane();

        centerHBox.getChildren().addAll(pane1, pane2);
        borderPane.setCenter(centerHBox);

        // ... [scene setup and stage display]
        Scene scene = new Scene(borderPane, 537, 450);
        primaryStage.setTitle("Data visualiser");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    private Pane createFirstPane() {
        Pane pane = new Pane();
        pane.setPrefSize(261.0, 406.0);
        pane.setStyle("-fx-border-color: black;");

        // Nested Pane 1
        Pane nestedPane1 = new Pane();
        nestedPane1.setLayoutX(14.0);
        nestedPane1.setLayoutY(34.0);
        nestedPane1.setPrefSize(233.0, 123.0);
        nestedPane1.setStyle("-fx-background-color: white;");

        // Components in Nested Pane 1
        Label label1 = new Label("Select dataset");
        label1.setLayoutX(14.0);
        label1.setLayoutY(14.0);

        listviewDataSet = new ListView<>();
        listviewDataSet.setLayoutX(14.0);
        listviewDataSet.setLayoutY(35.0);
        listviewDataSet.setPrefSize(212.0, 74.0);
        setListviewDataSet();
        nestedPane1.getChildren().addAll(label1, listviewDataSet);


        // Nested Pane 3
        Pane nestedPane3 = new Pane();
        nestedPane3.setLayoutX(14.0);
        nestedPane3.setLayoutY(177.0);
        nestedPane3.setPrefSize(233.0, 123.0);
        nestedPane3.setStyle("-fx-background-color: white;");


        // Components in Nested Pane 3
        Label label3 = new Label("Select result to visualize");
        label3.setLayoutX(14.0);
        label3.setLayoutY(14.0);

        createNewCbResult();
        cbResult.setOnMouseClicked(this::populateCbResult);


        nestedPane3.getChildren().addAll(label3, cbResult);

        // Label outside nested panes
        Label mainLabel = new Label("Data selection");
        mainLabel.setLayoutX(14.0);
        mainLabel.setLayoutY(6.0);
        mainLabel.setStyle(LABEL_STYLE);


        nestedPane1.setStyle(PANE_STYLE);
        nestedPane3.setStyle(PANE_STYLE);
        label1.setStyle(LABEL_STYLE);
        label3.setStyle(LABEL_STYLE);
        listviewDataSet.setStyle(LIST_VIEW_STYLE);
        cbResult.setStyle(LIST_VIEW_STYLE);

        // New Pane for Graph Type, positioned after nestedPane3
        Pane graphTypePane = new Pane();
        graphTypePane.setLayoutX(nestedPane3.getLayoutX());
        graphTypePane.setLayoutY(nestedPane3.getLayoutY() + nestedPane3.getPrefHeight() + 10); // Positioned just below nestedPane3
        graphTypePane.setPrefSize(nestedPane3.getPrefWidth(), nestedPane3.getPrefHeight());
        graphTypePane.setStyle(nestedPane3.getStyle());

        // Components in Graph Type Pane
        Label graphTypeLabel = new Label("Graph type");
        graphTypeLabel.setLayoutX(14.0); // Same as in nestedPane3
        graphTypeLabel.setLayoutY(14.0); // Same as in nestedPane3
        graphTypeLabel.setStyle(LABEL_STYLE);

        cbGraphType = new ComboBox<>();
        cbGraphType.setLayoutX(14.0); // Adjust if needed
        cbGraphType.setLayoutY(49.0); // Adjust if needed, same as comboBox in nestedPane3
        cbGraphType.setPrefSize(207.0, 26.0); // Same as comboBox in nestedPane3
        cbGraphType.setStyle(LIST_VIEW_STYLE);
        cbGraphType.getStylesheets().add("src/css/combo-size.css");
        cbGraphType.setOnMouseClicked(this::populateCbGraph);


        graphTypePane.getChildren().addAll(graphTypeLabel, cbGraphType);

        // Add all nested panes and the new graphTypePane to the main pane
        pane.getChildren().addAll(nestedPane1, nestedPane3, mainLabel, graphTypePane);

        return pane;

    }

    private Pane createSecondPane() {
        Pane pane = new Pane();
        pane.setPrefSize(281.0, 442.0);
        pane.setStyle("-fx-border-color: black;");

        // Nested Pane 1 in Second Pane
        filtersPane1 = new Pane();
        filtersPane1.setLayoutX(25.0);
        filtersPane1.setLayoutY(31.0);
        filtersPane1.setPrefSize(229.0, 178.0);
        filtersPane1.setStyle("-fx-background-color: white;");
        filtersPane1.setDisable(true);


        // Components in Nested Pane 1
        cbProperty = new ComboBox<>();
        cbProperty.setLayoutX(15.0);
        cbProperty.setLayoutY(43.0);
        cbProperty.setPrefSize(190.0, 26.0);
        createCbProperty();

        Label label1 = new Label("Select property");
        label1.setLayoutX(15.0);
        label1.setLayoutY(14.0);

        listViewProperties = new ListView<>();
        listViewProperties.setLayoutX(14.0);
        listViewProperties.setLayoutY(103.0);
        listViewProperties.setPrefSize(193.0, 61.0);

        Label label2 = new Label("Select property value");
        label2.setLayoutX(14.0);
        label2.setLayoutY(81.0);

        filtersPane1.getChildren().addAll(cbProperty, label1, listViewProperties, label2);

        // Nested Pane 2 in Second Pane
        filtersPane2 = new Pane();
        filtersPane2.setLayoutX(25.0);
        filtersPane2.setLayoutY(218.0);
        filtersPane2.setPrefSize(229.0, 167.0);
        filtersPane2.setStyle("-fx-background-color: white;");
        //filtersPane2.setDisable(true);


        // Components in Nested Pane 2
        Label label3 = new Label("Select course(s)");
        label3.setLayoutX(14.0);
        label3.setLayoutY(14.0);


        createListviewCourses();

        checkboxSelectAll = new CheckBox("Select all");
        checkboxSelectAll.setLayoutX(14.0);
        checkboxSelectAll.setLayoutY(130.0);
        checkboxSelectAll.setOnAction(this::selectAllCourses);

        filtersPane2.getChildren().addAll(label3, listViewCourses, checkboxSelectAll);

        // Button outside nested panes
        Button plotButton = new Button("Plot graph");
        plotButton.setLayoutX(174.0);
        plotButton.setLayoutY(394.0);
        plotButton.setOnAction(this::createGraphDisplayer);

        // Label outside nested panes
        applyFiltersCheckbox = new CheckBox("Apply filters");
        applyFiltersCheckbox.setLayoutX(26.0);
        applyFiltersCheckbox.setLayoutY(6.0);
        applyFiltersCheckbox.setStyle(LABEL_STYLE);
        applyFiltersCheckbox.setDisable(true);
        applyFiltersCheckbox.setOnAction(this::applyFiltersSelection);

        filtersPane1.setStyle(PANE_STYLE);
        filtersPane2.setStyle(PANE_STYLE);
        label1.setStyle(LABEL_STYLE);
        label2.setStyle(LABEL_STYLE);
        label3.setStyle(LABEL_STYLE);
        listViewProperties.setStyle(LIST_VIEW_STYLE);
        listViewCourses.setStyle(LIST_VIEW_STYLE);
        cbProperty.setStyle(LIST_VIEW_STYLE);
        plotButton.setStyle(BUTTON_STYLE);

        // Add all nested panes, button, and label to the main pane
        pane.getChildren().addAll(applyFiltersCheckbox, filtersPane1, filtersPane2, plotButton);

        return pane;
    }
    private void selectAllCourses(ActionEvent e){
        boolean selectionVal= checkboxSelectAll.isSelected();
        for(ListCheckboxElement listCell:listViewCourses.getItems()){
            listCell.setSelected(selectionVal);
        }
        listViewCourses.refresh();
    }
    private void applyFiltersSelection(ActionEvent e){
        if (applyFiltersCheckbox.isSelected()){
            disableFiltersPanes(false);
        }else{
            disableFiltersPanes(true);
        }
    }
    private void disableFiltersPanes(boolean disable){
        filtersPane1.setDisable(disable);
        //filtersPane2.setDisable(disable);
    }
    private void createNewCbResult(){
        cbResult = new ComboBox<>();
        cbResult.setLayoutX(14.0);
        cbResult.setLayoutY(49.0);
        cbResult.setPrefSize(207.0, 26.0);
        cbResult.getStylesheets().add("src/css/combo-size.css");
    }
    private void populateCbResult(MouseEvent e){
        ComboBoxElement ng_el=new ComboBoxElement("NG count",ngPath);
        ComboBoxElement cumlaude_el=new ComboBoxElement("Cum laude students",cumlaudePath);
        ComboBoxElement dropOuts_el=new ComboBoxElement("Drop Outs",dropOutsPath);
        ComboBoxElement mean_el=new ComboBoxElement("GPA (Mean)", meanPath);
        ComboBoxElement median_el=new ComboBoxElement("Median", medianPath);
        ComboBoxElement std_dev_el=new ComboBoxElement("Standard deviation", std_devPath);
        ComboBoxElement statistics_el=new ComboBoxElement("Statistics", statisticsPath);
        ObservableList<ComboBoxElement> items=FXCollections.observableArrayList(ng_el, cumlaude_el,dropOuts_el,mean_el,median_el,std_dev_el,statistics_el);
        cbResult.requestLayout();
        cbResult.getItems().clear();
        if(listviewDataSet.getSelectionModel().getSelectedItem().getName().equals("Graduate Students")){
            items.remove(ng_el);
        }
        cbResult.setItems(items);

        cbResult.setCellFactory(param -> new ListCell<ComboBoxElement>() {
            @Override
            protected void updateItem(ComboBoxElement ce, boolean empty) {
                super.updateItem(ce, empty);
                setText(empty?null:ce.getDisplayName());
            }
        });
        cbResult.setButtonCell(new ListCell<ComboBoxElement>() {
            @Override
            protected void updateItem(ComboBoxElement ce, boolean empty){
                super.updateItem(ce, empty);
                setText(empty? null:ce.getDisplayName());
            }
        });
        cbResult.setOnAction(this::enableCheckBoxFiltersBasedOnsSelection);
        cbResult.hide();
        cbResult.show();


    }
    private void enableCheckBoxFiltersBasedOnsSelection(ActionEvent e){
        if (cbResult.getSelectionModel().getSelectedItem().getId().equals(cumlaudePath)||cbResult.getSelectionModel().getSelectedItem().getId().equals(dropOutsPath)){
            applyFiltersCheckbox.setDisable(true);
            filtersPane1.setDisable(true);
            filtersPane2.setDisable(true);
        }else{
            applyFiltersCheckbox.setDisable(false);
            filtersPane2.setDisable(false);
        }
    }
    private void populateCbGraph(MouseEvent e){
        ComboBoxElement line_el=new ComboBoxElement("Line chart",lineChartPath);
        ComboBoxElement bar_el=new ComboBoxElement("Bar chart",barChartPath);
        ComboBoxElement pie_el=new ComboBoxElement("Pie chart",pieChartPath);
        ComboBoxElement area_el=new ComboBoxElement("Area chart",areaChartPath);
        ComboBoxElement scatter_el=new ComboBoxElement("Scatter chart",scatterChartPath);
        ObservableList<ComboBoxElement> items= FXCollections.observableArrayList(line_el,bar_el,pie_el,area_el,scatter_el);
        cbGraphType.requestLayout();
        cbGraphType.getItems().clear();
        if (cbResult.getSelectionModel().getSelectedItem().getId().equals(statisticsPath)) {
            items.removeAll(pie_el, scatter_el);
        }
        cbGraphType.setItems(items);
        cbGraphType.setCellFactory(param -> new ListCell<ComboBoxElement>() {
            @Override
            protected void updateItem(ComboBoxElement ce, boolean empty) {
                super.updateItem(ce, empty);

                setText(empty?null:ce.getDisplayName());
            }
        });
        cbGraphType.setButtonCell(new ListCell<ComboBoxElement>() {
            @Override
            protected void updateItem(ComboBoxElement ce, boolean empty){
                super.updateItem(ce, empty);
                setText(empty? null:ce.getDisplayName());
            }
        });
        cbGraphType.hide();
        cbGraphType.show();

    }
    private void createCbProperty(){
        ComboBoxElement suruna_el=new ComboBoxElement("Suruna Value",surunaPath);
        ComboBoxElement hurni_el=new ComboBoxElement("Hurni Level",hurniPath);
        ComboBoxElement lal_el=new ComboBoxElement("Lal Count",lalPath);
        ComboBoxElement volta_el=new ComboBoxElement("Volta",voltaPath);
        ObservableList<ComboBoxElement> items=FXCollections.observableArrayList(suruna_el,hurni_el,lal_el,volta_el);
        cbProperty.setCellFactory(param -> new ListCell<ComboBoxElement>() {
            @Override
            protected void updateItem(ComboBoxElement ce, boolean empty) {
                super.updateItem(ce, empty);

                setText(empty?null:ce.getDisplayName());
            }
        });
        cbProperty.setButtonCell(new ListCell<ComboBoxElement>() {
            @Override
            protected void updateItem(ComboBoxElement ce, boolean empty){
                super.updateItem(ce, empty);
                setText(empty? null:ce.getDisplayName());
            }
        });
        cbProperty.setItems(items);
        cbProperty.setOnAction(this::propertySelection);

    }
    private void setListviewDataSet() {
        SetStudents currentStudents_set = new SetStudents("Current Students", displayer.getCurrentStudents());
        SetStudents graduateStudents_set = new SetStudents("Graduate Students", displayer.getGraduateStudents());
        ObservableList<Set> items = FXCollections.observableArrayList(currentStudents_set, graduateStudents_set);
        listviewDataSet.setItems(items);
        listviewDataSet.setCellFactory(param -> new ListCell<Set>() {
            @Override
            protected void updateItem(Set st, boolean empty) {
                super.updateItem(st, empty);

                if (empty || st == null) {
                    setText(null);
                } else {
                    setText(st.getName());
                }
            }
        });
        listviewDataSet.setOnMouseClicked(this::enableFilters);
    }
    private void enableFilters(MouseEvent e){
        if(Objects.nonNull(listviewDataSet.getSelectionModel().getSelectedItem())){
            if (listviewDataSet.getSelectionModel().getSelectedItem().getName().equals("Current Students")){
                applyFiltersCheckbox.setDisable(false);
            }else if((listviewDataSet.getSelectionModel().getSelectedItem().getName().equals("Graduate Students"))){
                applyFiltersCheckbox.setSelected(false);
                applyFiltersCheckbox.setDisable(true);
                disableFiltersPanes(true);
            }
        }
    }
    private void createGraphDisplayer(ActionEvent e){
        try{
            for (Node child:
                    graphContainer.getChildren()) {
                child.setVisible(false);

            }



            selectedCoursesList=FXCollections.observableArrayList();
            for(ListCheckboxElement cbx:listViewCourses.getItems()){
                if (cbx.selectedProperty().get()){
                    selectedCoursesList.add(cbx.getName());
                }
            }
            if (isPropertySelected()){
                String selectedProp=cbProperty.getSelectionModel().getSelectedItem().getId();
                String selectedPropValue=listViewProperties.getSelectionModel().getSelectedItem();
                String dataSetSelection=listviewDataSet.getSelectionModel().getSelectedItem().getName();
                ObservableList<Student>studentsWithProp=displayer.getFilteredStudentsList(dataSetSelection,selectedProp,selectedPropValue,displayer);

                displayer.createFilteredCurrentCoursesList(studentsWithProp,selectedCoursesList);

            }else{
                ObservableList<Course> newCourseList=FXCollections.observableArrayList();
                if(listviewDataSet.getSelectionModel().getSelectedItem().getName().equals("Current Students")){
                    displayer.createFilteredCurrentCoursesList(null,selectedCoursesList);
                }else if(listviewDataSet.getSelectionModel().getSelectedItem().getName().equals("Graduate Students")){
                    displayer.createFilteredGraduateCoursesList(selectedCoursesList);
                }
            }
            switch (cbResult.getSelectionModel().getSelectedItem().getId()){
                case (ngPath):
                    createGeneralNG(cbGraphType.getSelectionModel().getSelectedItem().getId());
                    break;
                case(cumlaudePath):
                    createGeneralCumLaude(cbGraphType.getSelectionModel().getSelectedItem().getId());
                    break;
                case(dropOutsPath):
                    createGeneralDropOut(cbGraphType.getSelectionModel().getSelectedItem().getId());
                    break;
                case(meanPath):
                    createGeneralMean(cbGraphType.getSelectionModel().getSelectedItem().getId());
                    break;
                case(medianPath):
                    createGeneralMedian(cbGraphType.getSelectionModel().getSelectedItem().getId());
                    break;
                case(std_devPath):
                    createGeneralStandardDeviation(cbGraphType.getSelectionModel().getSelectedItem().getId());
                    break;
                case (statisticsPath):
                    createGeneralStatistics(cbGraphType.getSelectionModel().getSelectedItem().getId());
                    break;

            }
            stage2.show();
        }catch (Exception ex){
            showAlert();
        }


    }
    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Information Dialog");
        alert.setContentText("Be careful, you do not select all the necessary values\nCheck all the listboxes/checboxes and run again");

        alert.showAndWait();
    }
    private void createGraphWindowLayout(){
        // MenuBar at the top
        stage2=new Stage();
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        menuBar.getMenus().add(fileMenu);



        // StackPane with charts on the right
        graphContainer = new StackPane();
        graphContainer.setPrefWidth(800);
        lineChart = new LineChart<>(new CategoryAxis(), new NumberAxis());
        lineChart.setId("lineChart");
        lineChart.setVisible(false);
        lineChart.setAnimated(false);
        barChart = new BarChart<>(new CategoryAxis(), new NumberAxis());
        barChart.setId("barChart");
        barChart.setVisible(false);
        barChart.setAnimated(false);
        pieChart = new PieChart();
        pieChart.setId("pieChart");
        pieChart.setVisible(false);
        pieChart.setAnimated(false);
        areaChart=new AreaChart<>(new CategoryAxis(), new NumberAxis());
        areaChart.setId("areaChart");
        areaChart.setVisible(false);
        areaChart.setAnimated(false);
        scatterChart=new ScatterChart<>(new CategoryAxis(), new NumberAxis());
        scatterChart.setId("scatterChart");
        scatterChart.setVisible(false);
        scatterChart.setAnimated(false);
        stackedBarChart=new StackedBarChart<>(new CategoryAxis(), new NumberAxis());
        stackedBarChart.setId("stackedBarChart");
        stackedBarChart.setVisible(false);
        stackedBarChart.setAnimated(false);
        stackedAreaChart=new StackedAreaChart<>(new CategoryAxis(), new NumberAxis());
        stackedAreaChart.setId("stackedAreaChart");
        stackedAreaChart.setVisible(false);
        stackedAreaChart.setAnimated(false);
        graphContainer.getChildren().addAll(lineChart, barChart, pieChart,areaChart,scatterChart,stackedBarChart,stackedAreaChart);


        // Root layout
        BorderPane root = new BorderPane();
        root.setCenter(graphContainer);

        Scene scene = new Scene(root, 1000, 600);
        stage2.setScene(scene);
        stage2.setTitle("Visualizer");
    }
    private void createGeneralNG(String char_id){

        switch (char_id){
            case(barChartPath):
                createBarChartNG(getSeriesNG());
                break;
            case(lineChartPath):
                createLineChartNG(getSeriesNG());
                break;
            case(pieChartPath):
                createPieChartNG();
                break;
            case(areaChartPath):
                createAreaChartNG(getSeriesNG());
                break;
            case(scatterChartPath):
                createScatterChartNG(getSeriesNG());
                break;
        }



    }
    private XYChart.Series<String, Number> getSeriesNG(){
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        ObservableList<Course> coursesList=FXCollections.observableArrayList();

        series.setName("NG Count");
        coursesList= displayer.getFiltered_currentStudents_Courses();

        for (Course course : coursesList) {
            XYChart.Data<String, Number> data = new XYChart.Data<>(course.getName(), course.getNgCount());
            series.getData().add(data);
        }
        return series;
    }
    private void createBarChartNG(XYChart.Series<String,Number> series) {
        // Initialize the BarChart
        barChart.getData().clear();
        barChart.getXAxis().setLabel("Courses");
        barChart.getYAxis().setLabel("Number of NG");
        ((NumberAxis) barChart.getYAxis()).setTickUnit(1);

        barChart.getData().add(series);
        barChart.setVisible(true);
        barChart.requestLayout();
    }
    private void createPieChartNG() {
        // Initialize the PieChart
        pieChart.getData().clear();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        // Add data for each course manually
        for (Course course:
                displayer.getCurrentStudents_Courses()) {
            String txt=course.getName()+"\n"+"("+ course.getNgCount() +" NGs)";
            pieChartData.add(new PieChart.Data(txt,course.getNgCount()));
        }

        pieChart.setData(pieChartData);
        pieChart.setVisible(true);
    }
    private void createLineChartNG(XYChart.Series<String, Number> series){
        lineChart.getData().clear();
        lineChart.getData().add(series);
        lineChart.setVisible(true);
        lineChart.requestLayout();
    }
    private void createAreaChartNG(XYChart.Series<String, Number> series){
        areaChart.getData().clear();
        areaChart.getData().add(series);
        areaChart.setVisible(true);
        areaChart.requestLayout();
    }
    private void createScatterChartNG(XYChart.Series<String, Number> series){
        scatterChart.getData().clear();
        scatterChart.getData().add(series);
        scatterChart.setVisible(true);
        scatterChart.requestLayout();
    }
    private void createGeneralCumLaude(String char_id){
        switch (char_id){
            case(barChartPath):
                createCumLaudeBarChart();
                break;
            case(lineChartPath):
                createCumLaudeLineChart();
                break;
            case(pieChartPath):
                createCumlaudePieChart();
                break;
            case(areaChartPath):
                createCumLaudeAreaChart();
                break;
            case(scatterChartPath):
                createCumLaudeScatterChart();
                break;
        }

    }
    private void createCumlaudePieChart() {
        int cumLaudeCount = 0;
        int nonCumLaudeCount=0;
        if(listviewDataSet.getSelectionModel().getSelectedItem().getName().equals("Graduate Students")){
            cumLaudeCount=displayer.cumLaude(displayer.GRADUATEGRADES, displayer.getGraduateStudentsDataArray()).size();
            nonCumLaudeCount=displayer.getGraduateStudents().size()-cumLaudeCount;
        } else if (listviewDataSet.getSelectionModel().getSelectedItem().getName().equals("Current Students")){
            if (isPropertySelected()){
                cumLaudeCount=displayer.getFilteredCumLaude();
                nonCumLaudeCount=displayer.getFiltered_currentStudents().size()-cumLaudeCount;
            }else{
                cumLaudeCount=displayer.cumLaude(displayer.CURRENTGRADES, displayer.getCurrentStudentsDataArray()).size();
                nonCumLaudeCount=displayer.getCurrentStudents().size()-cumLaudeCount;
            }

        }
        pieChart.getData().clear();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Cum Laude", cumLaudeCount),
                new PieChart.Data("Non Cum Laude", nonCumLaudeCount)
        );
        pieChart.setData(pieChartData);
        pieChart.setVisible(true);
    }
    private void createCumLaudeBarChart(){
        int cumLaudeCount = 0;
        int nonCumLaudeCount=0;
        if(listviewDataSet.getSelectionModel().getSelectedItem().getName().equals("Graduate Students")){
            cumLaudeCount=displayer.cumLaude(displayer.GRADUATEGRADES, displayer.getGraduateStudentsDataArray()).size();
            nonCumLaudeCount=displayer.getGraduateStudents().size()-cumLaudeCount;
        } else if (listviewDataSet.getSelectionModel().getSelectedItem().getName().equals("Current Students")){
            if (isPropertySelected()){
                cumLaudeCount=displayer.getFilteredCumLaude();
                nonCumLaudeCount=displayer.getFiltered_currentStudents().size()-cumLaudeCount;
            }else{
                cumLaudeCount=displayer.cumLaude(displayer.CURRENTGRADES, displayer.getCurrentStudentsDataArray()).size();
                nonCumLaudeCount=displayer.getCurrentStudents().size()-cumLaudeCount;
            }

        }
        barChart.getData().clear();
        XYChart.Series<String,Number> series = new XYChart.Series<>();
        series.setName("Cum Laude students count");

        XYChart.Data<String, Number> data1 = new XYChart.Data<>("Cum Laude", cumLaudeCount);
        XYChart.Data<String, Number> data2 = new XYChart.Data<>("Not Cum Lude", nonCumLaudeCount);
        series.getData().add(data1);
        series.getData().add(data2);
        barChart.getData().add(series);
        barChart.setVisible(true);
        barChart.requestLayout();

    }
    private void createCumLaudeLineChart(){
        int cumLaudeCount = 0;
        int nonCumLaudeCount=0;
        if(listviewDataSet.getSelectionModel().getSelectedItem().getName().equals("Graduate Students")){
            cumLaudeCount=displayer.cumLaude(displayer.GRADUATEGRADES, displayer.getGraduateStudentsDataArray()).size();
            nonCumLaudeCount=displayer.getGraduateStudents().size()-cumLaudeCount;
        } else if (listviewDataSet.getSelectionModel().getSelectedItem().getName().equals("Current Students")){
            if (isPropertySelected()){
                cumLaudeCount=displayer.getFilteredCumLaude();
                nonCumLaudeCount=displayer.getFiltered_currentStudents().size()-cumLaudeCount;
            }else{
                cumLaudeCount=displayer.cumLaude(displayer.CURRENTGRADES, displayer.getCurrentStudentsDataArray()).size();
                nonCumLaudeCount=displayer.getCurrentStudents().size()-cumLaudeCount;
            }

        }
        lineChart.getData().clear();
        XYChart.Series<String,Number> series = new XYChart.Series<>();
        series.setName("Cum Laude students count");

        XYChart.Data<String, Number> data1 = new XYChart.Data<>("Cum Laude", cumLaudeCount);
        XYChart.Data<String, Number> data2 = new XYChart.Data<>("Not Cum Lude", nonCumLaudeCount);
        series.getData().add(data1);
        series.getData().add(data2);
        lineChart.getData().add(series);
        lineChart.setVisible(true);
        lineChart.requestLayout();

    }
    private void createCumLaudeAreaChart(){
        int cumLaudeCount = 0;
        int nonCumLaudeCount=0;
        if(listviewDataSet.getSelectionModel().getSelectedItem().getName().equals("Graduate Students")){
            cumLaudeCount=displayer.cumLaude(displayer.GRADUATEGRADES, displayer.getGraduateStudentsDataArray()).size();
            nonCumLaudeCount=displayer.getGraduateStudents().size()-cumLaudeCount;
        } else if (listviewDataSet.getSelectionModel().getSelectedItem().getName().equals("Current Students")){
            if (isPropertySelected()){
                cumLaudeCount=displayer.getFilteredCumLaude();
                nonCumLaudeCount=displayer.getFiltered_currentStudents().size()-cumLaudeCount;
            }else{
                cumLaudeCount=displayer.cumLaude(displayer.CURRENTGRADES, displayer.getCurrentStudentsDataArray()).size();
                nonCumLaudeCount=displayer.getCurrentStudents().size()-cumLaudeCount;
            }

        }
        areaChart.getData().clear();
        XYChart.Series<String,Number> series = new XYChart.Series<>();
        series.setName("Cum Laude students count");

        XYChart.Data<String, Number> data1 = new XYChart.Data<>("Cum Laude", cumLaudeCount);
        XYChart.Data<String, Number> data2 = new XYChart.Data<>("Not Cum Lude", nonCumLaudeCount);
        series.getData().add(data1);
        series.getData().add(data2);
        areaChart.getData().add(series);
        areaChart.setVisible(true);
        areaChart.requestLayout();

    }
    private void createCumLaudeScatterChart(){
        int cumLaudeCount = 0;
        int nonCumLaudeCount=0;
        if(listviewDataSet.getSelectionModel().getSelectedItem().getName().equals("Graduate Students")){
            cumLaudeCount=displayer.cumLaude(displayer.GRADUATEGRADES, displayer.getGraduateStudentsDataArray()).size();
            nonCumLaudeCount=displayer.getGraduateStudents().size()-cumLaudeCount;
        } else if (listviewDataSet.getSelectionModel().getSelectedItem().getName().equals("Current Students")){
            if (isPropertySelected()){
                cumLaudeCount=displayer.getFilteredCumLaude();
                nonCumLaudeCount=displayer.getFiltered_currentStudents().size()-cumLaudeCount;
            }else{
                cumLaudeCount=displayer.cumLaude(displayer.CURRENTGRADES, displayer.getCurrentStudentsDataArray()).size();
                nonCumLaudeCount=displayer.getCurrentStudents().size()-cumLaudeCount;
            }

        }
        scatterChart.getData().clear();
        XYChart.Series<String,Number> series = new XYChart.Series<>();
        series.setName("Cum Laude students count");

        XYChart.Data<String, Number> data1 = new XYChart.Data<>("Cum Laude", cumLaudeCount);
        XYChart.Data<String, Number> data2 = new XYChart.Data<>("Not Cum Lude", nonCumLaudeCount);
        series.getData().add(data1);
        series.getData().add(data2);
        scatterChart.getData().add(series);
        scatterChart.setVisible(true);
        scatterChart.requestLayout();

    }
    private void createGeneralDropOut(String char_id){
        switch (char_id){
            case(barChartPath):
                createDropOutBarChart();
                break;
            case(lineChartPath):
                createDropOutLineChart();
                break;
            case(pieChartPath):
                createDropOutPieChart();
                break;
            case(areaChartPath):
                createDropOutAreaChart();
                break;
            case(scatterChartPath):
                createDropOutScatterChart();
                break;
        }
    }
    private void createDropOutBarChart(){
        barChart.getData().clear();
        XYChart.Series<String,Number> series=getSeriesDropOut(listviewDataSet.getSelectionModel().getSelectedItem());
        barChart.getData().add(series);
        barChart.setVisible(true);
    }
    private void createDropOutLineChart(){
        lineChart.getData().clear();
        XYChart.Series<String,Number> series=getSeriesDropOut(listviewDataSet.getSelectionModel().getSelectedItem());
        lineChart.getData().add(series);
        lineChart.setVisible(true);
    }
    private void createDropOutPieChart(){
        pieChart.getData().clear();
        XYChart.Series<String,Number> series=getSeriesDropOut(listviewDataSet.getSelectionModel().getSelectedItem());
        ObservableList<PieChart.Data> pieChartData=FXCollections.observableArrayList();
        for(XYChart.Data<String,Number> data:series.getData()){
            String stringVal=data.getXValue();
            int numbVal=(int)data.getYValue();
            PieChart.Data dt=new PieChart.Data(stringVal,numbVal);
            pieChartData.add(dt);
        }
        pieChart.setData(pieChartData);
        pieChart.setVisible(true);
    }
    private void createDropOutAreaChart(){
        areaChart.getData().clear();
        XYChart.Series<String,Number> series=getSeriesDropOut(listviewDataSet.getSelectionModel().getSelectedItem());
        areaChart.getData().add(series);
        areaChart.setVisible(true);
    }
    private void createDropOutScatterChart(){
        scatterChart.getData().clear();
        XYChart.Series<String,Number> series=getSeriesDropOut(listviewDataSet.getSelectionModel().getSelectedItem());
        scatterChart.getData().add(series);
        scatterChart.setVisible(true);
    }
    private XYChart.Series<String,Number> getSeriesDropOut(Set set){
        int dropOutCount=0;
        int noDropOut=0;
        if (set.getName().equals("Current Students")) {
            dropOutCount= displayer.countDropOuts(displayer.getCurrentStudentsDataArray());
            noDropOut=displayer.getGraduateStudents().size()-dropOutCount;
        } else if(set.getName().equals("Graduate Students")) {
            dropOutCount=displayer.countDropOuts(displayer.getGraduateStudentsDataArray());
            noDropOut=displayer.getCurrentStudents().size()-dropOutCount;
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Drop Out rate");
        XYChart.Data<String,Number> droppedOut=new XYChart.Data<>("Dropped out",dropOutCount);
        XYChart.Data<String,Number> notDroppedOut=new XYChart.Data<>("Do not drop out",noDropOut);
        series.getData().add(droppedOut);
        series.getData().add(notDroppedOut);
        return  series;
    }
    private void createGeneralStatistics(String char_id){

        switch (char_id){
            case(barChartPath):
                createStatisticsStackedBarChart();
                break;
            case(lineChartPath):
                createStatisticsLineChart();
                break;
            case(pieChartPath):
                break;
            case(areaChartPath):
                createStatisticsStackedAreaChart();
                break;
            case(scatterChartPath):
                createStatisticsScatterChart();
                break;
        }
    }
    private void createStatisticsStackedBarChart(){
        stackedBarChart.getData().clear();
        stackedBarChart.getXAxis().setLabel("Courses");
        stackedBarChart.setVisible(true);
        CategoryAxis stackedBarGraphXAxis=(CategoryAxis) stackedBarChart.getXAxis();

        String[] courseNames = displayer.getCurrentStudents_Courses().stream().map(Course::getName).toArray(String[]::new);
        stackedBarGraphXAxis.setTickLabelRotation(90);
        stackedBarChart.getData().clear();
        ObservableList<XYChart.Series<String,Number>> series=getSeriesStatistics(listviewDataSet.getSelectionModel().getSelectedItem(),true);
        for(XYChart.Series<String,Number> s:series){

            stackedBarChart.getData().add(s);

        }
        stackedBarChart.setVisible(true);
    }
    private void createStatisticsLineChart(){
        lineChart.getData().clear();
        lineChart.getXAxis().setLabel("Courses");
        ObservableList<XYChart.Series<String,Number>> series=getSeriesStatistics(listviewDataSet.getSelectionModel().getSelectedItem(),false);
        lineChart.getData().addAll(series);
        lineChart.setVisible(true);
    }
    private void createStatisticsStackedAreaChart(){
        stackedAreaChart.getData().clear();
        stackedAreaChart.getXAxis().setLabel("Courses");
        NumberAxis yaxis=(NumberAxis) stackedAreaChart.getYAxis();
        yaxis.setLowerBound(0);
        yaxis.setTickUnit(0.5);
        yaxis.setAutoRanging(false);
        yaxis.setUpperBound(15);

        stackedAreaChart.requestLayout();
        ObservableList<XYChart.Series<String,Number>> series=getSeriesStatistics(listviewDataSet.getSelectionModel().getSelectedItem(),true);
        for(XYChart.Series<String,Number> s:series){
            stackedAreaChart.getData().add(s);
        }
        stackedAreaChart.setVisible(true);
    }
    private void createStatisticsScatterChart(){
        scatterChart.getData().clear();
        scatterChart.getXAxis().setLabel("Courses");
        ObservableList<XYChart.Series<String,Number>> series=getSeriesStatistics(listviewDataSet.getSelectionModel().getSelectedItem(),true);
        for(XYChart.Series<String,Number> s:series){
            scatterChart.getData().add(s);
        }
        scatterChart.setVisible(true);
    }
    private ObservableList<XYChart.Series<String,Number>> getSeriesStatistics(Set set, boolean isStack){
        ObservableList<Course> coursesList = null;
        ObservableList<XYChart.Series<String,Number>> seriesList=FXCollections.observableArrayList();
        try {
            if(set.getName().equals("Current Students")){
                coursesList= displayer.getFiltered_currentStudents_Courses();
            }else if(set.getName().equals("Graduate Students")){
                coursesList=displayer.getGraduateStudents_Courses();
            }
            assert coursesList != null;
            XYChart.Series<String,Number> seriesMean=new XYChart.Series<>();
            XYChart.Series<String,Number> seriesMedian=new XYChart.Series<>();
            XYChart.Series<String,Number> seriesStdDeviation=new XYChart.Series<>();
            seriesMean.setName("Mean");
            seriesMedian.setName("Median");
            seriesStdDeviation.setName("Standard deviation");

            for(Course course:coursesList){
                if(isStack){
                    seriesStdDeviation.getData().add(new XYChart.Data<>(course.getName(), course.getStd_deviation()));
                    seriesMean.getData().add(new XYChart.Data<>(course.getName(), course.getMean()-course.getStd_deviation()));
                    seriesMedian.getData().add(new XYChart.Data<>(course.getName(), course.getMedian()-(course.getMean()-course.getStd_deviation())));

                }else {
                    seriesStdDeviation.getData().add(new XYChart.Data<>(course.getName(), course.getStd_deviation()));
                    seriesMean.getData().add(new XYChart.Data<>(course.getName(), course.getMean()));
                    seriesMedian.getData().add(new XYChart.Data<>(course.getName(), course.getMedian()));

                }

            }
            seriesList.add(seriesStdDeviation);
            seriesList.add(seriesMean);
            seriesList.add(seriesMedian);

        }catch (Exception e){
            return seriesList=null;
        }

        return seriesList;
    }

    private void createGeneralMean(String char_id){
        switch (char_id){
            case(barChartPath):
                createBarChartMean();
                break;
            case(lineChartPath):
                createLineChartMean();
                break;
            case(pieChartPath):
                createPieChartMean();
                break;
            case(areaChartPath):
                createAreaChartMean();
                break;
            case(scatterChartPath):
                createScatterChartMean();
                break;
        }
    }
    private void createGeneralMedian(String char_id){
        switch (char_id){
            case(barChartPath):
                createBarChartMedian();
                break;
            case(lineChartPath):
                createLineChartMedian();
                break;
            case(pieChartPath):
                createPieChartMedian();
                break;
            case(areaChartPath):
                createAreaChartMedian();
                break;
            case(scatterChartPath):
                createScatterChartMedian();
                break;
        }
    }
    private void createGeneralStandardDeviation(String char_id){
        switch (char_id){
            case(barChartPath):
                createBarChartStdDev();
                break;
            case(lineChartPath):
                createLineChartStdDev();
                break;
            case(pieChartPath):
                createPieChartStdDev();
                break;
            case(areaChartPath):
                createAreaChartStdDev();
                createStatisticsStackedAreaChart();
                break;
            case(scatterChartPath):
                createScatterChartStdDev();
                break;
        }
    }
    private void createBarChartMean(){
        barChart.getData().clear();
        barChart.getXAxis().setLabel("Courses");
        barChart.getYAxis().setLabel("Mean value");
        XYChart.Series<String, Number> series=getSeriesMean(listviewDataSet.getSelectionModel().getSelectedItem());
        barChart.getData().add(series);
        barChart.setVisible(true);
    }
    private void createBarChartMedian(){
        barChart.getData().clear();
        barChart.getXAxis().setLabel("Courses");
        barChart.getYAxis().setLabel("Median value");
        XYChart.Series<String, Number> series=getSeriesMedian(listviewDataSet.getSelectionModel().getSelectedItem());
        barChart.getData().add(series);
        barChart.setVisible(true);
    }
    private void createBarChartStdDev(){
        barChart.getData().clear();
        barChart.getXAxis().setLabel("Courses");
        barChart.getYAxis().setLabel("Standard Deviation value");
        XYChart.Series<String, Number> series=getSeriesStandardDeviation(listviewDataSet.getSelectionModel().getSelectedItem());
        barChart.getData().add(series);
        barChart.setVisible(true);
    }
    private void createLineChartMean(){
        lineChart.getData().clear();
        lineChart.getXAxis().setLabel("Courses");
        lineChart.getYAxis().setLabel("Mean value");
        XYChart.Series<String, Number> series=getSeriesMean(listviewDataSet.getSelectionModel().getSelectedItem());
        lineChart.getData().add(series);
        lineChart.setVisible(true);
    }
    private void createLineChartMedian(){
        lineChart.getData().clear();
        lineChart.getXAxis().setLabel("Courses");
        lineChart.getYAxis().setLabel("Median value");
        XYChart.Series<String, Number> series=getSeriesMedian(listviewDataSet.getSelectionModel().getSelectedItem());
        lineChart.getData().add(series);
        lineChart.setVisible(true);
    }
    private void createLineChartStdDev(){
        lineChart.getData().clear();
        lineChart.getXAxis().setLabel("Courses");
        lineChart.getYAxis().setLabel("Standard Deviation value");
        XYChart.Series<String, Number> series=getSeriesStandardDeviation(listviewDataSet.getSelectionModel().getSelectedItem());
        lineChart.getData().add(series);
        lineChart.setVisible(true);
    }
    private void createPieChartMean(){
        pieChart.getData().clear();
        XYChart.Series<String, Number> series=getSeriesMean(listviewDataSet.getSelectionModel().getSelectedItem());
        ObservableList<PieChart.Data> pieChartDataList=FXCollections.observableArrayList();
        for(XYChart.Data<String,Number> dt:series.getData()){
            double yvalue=(double) dt.getYValue();
            pieChartDataList.add(new PieChart.Data(dt.getXValue(),yvalue));
        }
        barChart.getData().add(series);
        barChart.setVisible(true);
    }
    private void createPieChartMedian(){
        pieChart.getData().clear();
        XYChart.Series<String, Number> series=getSeriesMedian(listviewDataSet.getSelectionModel().getSelectedItem());
        ObservableList<PieChart.Data> pieChartDataList=FXCollections.observableArrayList();
        for(XYChart.Data<String,Number> dt:series.getData()){
            double yvalue=(double) dt.getYValue();
            pieChartDataList.add(new PieChart.Data(dt.getXValue(),yvalue));
        }
        pieChart.setData(pieChartDataList);
        pieChart.setVisible(true);
    }
    private void createPieChartStdDev(){
        pieChart.getData().clear();
        XYChart.Series<String, Number> series=getSeriesStandardDeviation(listviewDataSet.getSelectionModel().getSelectedItem());
        ObservableList<PieChart.Data> pieChartDataList=FXCollections.observableArrayList();
        for(XYChart.Data<String,Number> dt:series.getData()){
            double yvalue=(double) dt.getYValue();
            pieChartDataList.add(new PieChart.Data(dt.getXValue(),yvalue));
        }
        lineChart.getData().add(series);
        lineChart.setVisible(true);
    }

    private void createAreaChartMean(){
        areaChart.getData().clear();
        areaChart.getXAxis().setLabel("Courses");
        areaChart.getYAxis().setLabel("Mean value");
        XYChart.Series<String, Number> series=getSeriesMean(listviewDataSet.getSelectionModel().getSelectedItem());
        areaChart.getData().add(series);
        areaChart.setVisible(true);
    }
    private void createAreaChartMedian(){
        areaChart.getData().clear();
        areaChart.getXAxis().setLabel("Courses");
        areaChart.getYAxis().setLabel("Median value");
        XYChart.Series<String, Number> series=getSeriesMedian(listviewDataSet.getSelectionModel().getSelectedItem());
        areaChart.getData().add(series);
        areaChart.setVisible(true);
    }
    private void createAreaChartStdDev(){
        areaChart.getData().clear();
        areaChart.getXAxis().setLabel("Courses");
        areaChart.getYAxis().setLabel("Standard Deviation value");
        XYChart.Series<String, Number> series=getSeriesStandardDeviation(listviewDataSet.getSelectionModel().getSelectedItem());
        areaChart.getData().add(series);
        areaChart.setVisible(true);
    }
    private void createScatterChartMean(){
        scatterChart.getData().clear();
        scatterChart.getXAxis().setLabel("Courses");
        scatterChart.getYAxis().setLabel("Mean value");
        XYChart.Series<String, Number> series=getSeriesMean(listviewDataSet.getSelectionModel().getSelectedItem());
        scatterChart.getData().add(series);
        scatterChart.setVisible(true);
    }
    private void createScatterChartMedian(){
        scatterChart.getData().clear();
        scatterChart.getXAxis().setLabel("Courses");
        scatterChart.getYAxis().setLabel("Median value");
        XYChart.Series<String, Number> series=getSeriesMedian(listviewDataSet.getSelectionModel().getSelectedItem());
        scatterChart.getData().add(series);
        scatterChart.setVisible(true);
    }
    private void createScatterChartStdDev(){
        scatterChart.getData().clear();
        scatterChart.getXAxis().setLabel("Courses");
        scatterChart.getYAxis().setLabel("Standard Deviation value");
        XYChart.Series<String, Number> series=getSeriesStandardDeviation(listviewDataSet.getSelectionModel().getSelectedItem());
        scatterChart.getData().add(series);
        scatterChart.setVisible(true);
    }
    private XYChart.Series<String, Number> getSeriesMean(Set set){
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        List<Course> coursesList=null;
        series.setName("GPA value");
        if (set.getName().equals("Current Students")) {
            coursesList=displayer.getFiltered_currentStudents_Courses();
/*
            if (isPropertySelected()){
                coursesList= displayer.getFiltered_currentStudents_Courses();
            }else{
                coursesList=displayer.getFiltered_graduateStudents_Courses();
            }*/
        } else if(set.getName().equals("Graduate Students")) {
            coursesList=displayer.getFiltered_graduateStudents_Courses();
        }
        for(Course course: coursesList){
            series.getData().add(new XYChart.Data<>(course.getName(),course.getMean()));
        }
        return series;

    }
    private XYChart.Series<String, Number> getSeriesMedian(Set set){
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        List<Course> coursesList=null;
        series.setName("Median value");
        if (set.getName().equals("Current Students")) {
            coursesList=displayer.getFiltered_currentStudents_Courses();
            /*if (isPropertySelected()){
                coursesList= displayer.getFiltered_currentStudents_Courses();
            }else{
                coursesList=displayer.getFiltered_graduateStudents_Courses();
            }*/
        } else if(set.getName().equals("Graduate Students")) {
            coursesList=displayer.getFiltered_graduateStudents_Courses();
        }
        for(Course course: coursesList){
            series.getData().add(new XYChart.Data<>(course.getName(),course.getMedian()));
        }
        return series;

    }
    private XYChart.Series<String, Number> getSeriesStandardDeviation(Set set){
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        List<Course> coursesList=null;
        series.setName("Standard deviation value");
        if (set.getName().equals("Current Students")) {
            coursesList=displayer.getFiltered_currentStudents_Courses();
        } else if(set.getName().equals("Graduate Students")) {
            coursesList=displayer.getFiltered_graduateStudents_Courses();
        }
        for(Course course: coursesList){
            series.getData().add(new XYChart.Data<>(course.getName(),course.getStd_deviation()));
        }
        return series;

    }
    //Katarina's method for gpa chart renamed
    private XYChart.Series<String, Number> getSeriesGpa(Set set) {

        //Create new series
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        // Get the appropriate data array based on the selected data set
        Object[][] selectedData;
        double[] meanGPA = new double[0];

        // Calculate the mean (average) GPA using the mean method
        if (set.getName().equals("Current Students")) {
            selectedData = displayer.getCurrentStudentsDataArray();
            meanGPA = displayer.getCurrentStudentsAverageGrade();
        } else if(set.getName().equals("Graduate Students")) {
            selectedData = displayer.getGraduateStudentsDataArray();
            meanGPA = displayer.getGraduateStudentsAverageGrade();
        }

        // Get the course names
        String[] courseNames = displayer.getCurrentStudents_Courses().stream().map(Course::getName).toArray(String[]::new);

        // Debug print statements

        // Recreate the entire series
        for (int i = 0, j= 1; i < courseNames.length; i++, j++) {
            String course = courseNames[i];
            double gpa = meanGPA[j];
            updateData(series, course, gpa);
        }

        // Debug print statement
        System.out.println("Updated Chart Data: " + series.getData());

        return series;
        // Update the chart



    }
    private void updateData(XYChart.Series<String, Number> series, String category, double value) {
        // Check if the category already exists in the series
        XYChart.Data<String, Number> existingData = findDataByCategory(series, category);

        if (!Double.isNaN(value)) {
            if (existingData != null) {
                // If the category exists, update the value
                existingData.setYValue(value);
            } else {
                // If the category doesn't exist, add a new data point
                series.getData().add(new XYChart.Data<>(category, value));
            }
        }
    }
    private XYChart.Data<String, Number> findDataByCategory(XYChart.Series<String, Number> series, String category) {
        for (XYChart.Data<String, Number> data : series.getData()) {
            if (data.getXValue().equals(category)) {
                return data;
            }
        }
        return null;
    }
    private boolean isPropertySelected(){
        if (applyFiltersCheckbox.isSelected()){
            if(Objects.nonNull(cbProperty.getSelectionModel().getSelectedItem())){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }

    }

    private void propertySelection(ActionEvent e){

        switch (cbProperty.getSelectionModel().getSelectedItem().getId()){
            case(surunaPath):
                showSuruna();
                break;
            case(hurniPath):
                showHurni();
                break;
            case(lalPath):
                showLal();
                break;
            case(voltaPath):
                showVolta();
                break;
            default:
                break;
        }
    }
    private void showSuruna(){
        ObservableList<String> surunaOptions=FXCollections.observableArrayList("nulp","doot","lobi");
        listViewProperties.setItems(surunaOptions);
    }
    private void showHurni(){
        ObservableList<String> hurniOptions=FXCollections.observableArrayList("high","low","medium","full","nothing");
        listViewProperties.setItems(hurniOptions);
    }
    private void showLal(){
        ObservableList<String> lalOptions=FXCollections.observableArrayList();
        for (int i=59;i<=100;i++){
            lalOptions.add(Integer.toString(i));
        }
        listViewProperties.setItems(lalOptions);
    }
    private void showVolta(){
        ObservableList<String> voltaOptions=FXCollections.observableArrayList("1 star","2 stars","3 stars","4 stars","5 stars");
        listViewProperties.setItems(voltaOptions);
    }
    public FileDisplayer getDisplayer(){
        return displayer;
    }
    private void createListviewCourses(){
        Map<String, BooleanProperty> itemCheckedMap = new HashMap<>();
        String[] courseNames = displayer.getCurrentStudents_Courses().stream().map(Course::getName).toArray(String[]::new);
        ObservableList<String> items = FXCollections.observableArrayList(courseNames);


        for (String item : items) {
            itemCheckedMap.put(item, new SimpleBooleanProperty(false));
        }
        selectedCoursesList=FXCollections.observableArrayList();
        ObservableList<ListCheckboxElement> coursesCbxList=FXCollections.observableArrayList();
        for(String cName:courseNames){
            coursesCbxList.add(new ListCheckboxElement(cName));
        }


        listViewCourses = new ListView<>(coursesCbxList);
        listViewCourses.setLayoutX(14.0);
        listViewCourses.setLayoutY(38.0);
        listViewCourses.setPrefSize(193.0, 92.0);

        listViewCourses.setCellFactory(lv -> new ListCell<ListCheckboxElement>() {
            private final CheckBox checkBox;
            {
                // Initialize the CheckBox
                checkBox = new CheckBox();
                checkBox.setOnAction(e -> {
                    ListCheckboxElement item = getItem();
                    if (item != null) {
                        item.setSelected(checkBox.isSelected());
                    }
                });
            }
            @Override
            public void updateItem(ListCheckboxElement item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    checkBox.setText(item.getName());
                    checkBox.setSelected(item.isSelected());
                    setGraphic(checkBox);
                }
            }

        });

    }

    public void setParentStage(Stage parentStage) {
        this.parentStage = parentStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
