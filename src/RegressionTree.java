package src;

import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Random;


public class RegressionTree {

    private static  final String surunaPath="suruna";
    private static  final String hurniPath="hurni";
    private static  final String lalPath="lal";
    private static  final String voltaPath="volta";
    private Course course;
    private TreeNode root;
    private ObservableList<String> studentInfoStringList;
    private List<DataPoint> initialDataSet;
    public RegressionTree(List<DataPoint> dataPoints,int nodeCandidates){
        //
        /////////////////////////////REGRESSION TREE ALGORITHM//////////////////////////////////////

        //ALEX 11/01/2024 before we start to build the tree we need to know the order of the features
        //and decide which one will be the root node, which one will be the second etc...
        studentInfoStringList= FXCollections.observableArrayList(surunaPath,hurniPath,lalPath,voltaPath);
        initialDataSet=new ArrayList<>(dataPoints);
        //Let's take a look to squared residuals for each property, i will store them in 4 variables so i can
        //compare them later (i will create a method from the code bellow)

        root = buildTree(dataPoints,nodeCandidates,studentInfoStringList);
        pruneTree(root);

        /////////////////////////////////////////////////////////////////////

    }



  /*  public static void main(String[] args) {

        // Create an instance of FileDisplayer
        FileDisplayer fileDisplayer = new FileDisplayer();

        // Access the currentStudents list from FileDisplayer class
        ObservableList<Student> students = fileDisplayer.getCurrentStudents();

        // Shuffle the original list truly randomly
        Collections.shuffle(students);

        //create an object of a course
        Course course = new Course("LDE-009");

        // Split the data into training and validation sets (80-20 split)
        int splitIndex = (int) (0.8 * students.size());
        List<Student> trainingStudents = students.subList(0, splitIndex);
        List<Student> validationStudents = students.subList(splitIndex, students.size());

        // Convert students to data points
        List<DataPoint> trainingData = convertStudentsToDataPoints(trainingStudents, course);
        List<DataPoint> validationData = convertStudentsToDataPoints(validationStudents, course);

        /////////////////////////////REGRESSION TREE ALGORITHM//////////////////////////////////////

        //ALEX 11/01/2024 before we start to build the tree we need to know the order of the features
        //and decide which one will be the root node, which one will be the second etc...

        ObservableList<String> studentInfoStringList= FXCollections.observableArrayList(surunaPath,hurniPath,lalPath,voltaPath);

        //Let's take a look to squared residuals for each property, i will store them in 4 variables so i can
        //compare them later (i will create a method from the code bellow)

        /////////////////////////////////////////////////////////////////////





        // Build the regression tree using the training set
        TreeNode root = buildTree(trainingData);

        // Validate the regression tree on the validation set
        double totalSquaredError = 0.0;
        double sizeValidationSet = 0;

        for (DataPoint validationPoint : validationData) {
            double predictedGPA = predictGPA(root, validationPoint);
            double actualGPA = validationPoint.getActualCourseGrade();
            if(actualGPA != -1) {
                // Print predicted and actual GPA for each validation point
                System.out.println("Predicted GPA: " + predictedGPA + ", Actual GPA: " + actualGPA);

                // Calculate squared error
                double squaredError = Math.pow(predictedGPA - actualGPA, 2);
                totalSquaredError += squaredError;
                sizeValidationSet++;
            }
        }

        // Calculate Mean Squared Error (MSE)
        double mse = totalSquaredError / sizeValidationSet;
        System.out.println("Mean Squared Error on Validation Set: " + mse);

        // Example: Predict GPA for a new data point
        Student newStudent = new Student(1000243);
        DataPoint newDataPoint = convertStudentToDataPoint(newStudent, course);
        double predictedGPA = predictGPA(root, newDataPoint);
        System.out.println("Predicted GPA: " + predictedGPA);


    }*/
    private ResidualsResult calculateSurunaResidual(List<DataPoint> trainingData){
        //Since the tree needs to be binary we need to consider the suruna values as numbers
        //we assume that nulp=0 doot=2 and lobi=3

        double bestResidual=0;
        String surunaVal="";
        double[] residuals= calculateStringResidual(surunaPath,trainingData);

        for(int i=0;i<getSubNodesCount(surunaPath);i++){
            double res=residuals[i];
            if (i==0){
                bestResidual=res;
                surunaVal=getSurunaOptions().get(i);
            }else{
                if(res<bestResidual){
                    bestResidual=res;
                    surunaVal=getSurunaOptions().get(i);
                }
            }
        }
        return new ResidualsResult(surunaPath,surunaVal,bestResidual);

    }
    private ResidualsResult calculateHurniResidual(List<DataPoint> trainingData){
        //Since the tree needs to be binary we need to consider the suruna values as numbers
        //we assume that nothing=0 low=1 and medium=2 full=3 high=4

        //I will create the return value as a list, the first member is the best value an
        double bestResidual=0;
        String hurniVal="";
        double[] residuals= calculateStringResidual(hurniPath,trainingData);

        for(int i=0;i<getSubNodesCount(hurniPath);i++){
            double res=residuals[i];
            if (i==0){
                bestResidual=res;
                hurniVal=getHurniOptions().get(i);
            }else{
                if(res<bestResidual){
                    bestResidual=res;
                    hurniVal=getHurniOptions().get(i);
                }
            }
        }
        return new ResidualsResult(hurniPath,hurniVal,bestResidual);
    }
    private ResidualsResult calculateLalResidual(List<DataPoint> trainingData){
        List<DataPoint> sortedData=sortList(trainingData,lalPath);

        double bestResidual=0;
        int lalVal=0;
        for(int i=1;i<sortedData.size()+1;i++){
            DataPoint dp=sortedData.get(i-1);
            double res= calculateResidual(lalPath,i,sortedData);;
            if (i==1){
                bestResidual=res;
                lalVal=dp.getLal();
            }else{
                if(res<bestResidual){
                    bestResidual=res;
                    lalVal=dp.getLal();
                }
            }
        }
        return new ResidualsResult(lalPath,String.valueOf(lalVal),bestResidual);
    }
    private ResidualsResult calculateVoltaResidual(List<DataPoint> trainingData){
        List<DataPoint> sortedData=sortList(trainingData,voltaPath);

        double bestResidual=0;
        String voltaVal="";


        double[] residuals= calculateStringResidual(voltaPath,sortedData);

        for(int i=0;i<getSubNodesCount(voltaPath);i++){
            double res=residuals[i];
            if (i==0){
                bestResidual=res;
                voltaVal=getVoltaOptions().get(i);
            }else{
                if(res<bestResidual){
                    bestResidual=res;
                    voltaVal=getVoltaOptions().get(i);
                }
            }
        }
        return new ResidualsResult(voltaPath,voltaVal,bestResidual);
    }
    private double calculateResidual(String propertyPath, int threshold, List<DataPoint> data){
        double residual=0;
        //we need to calculate the average grade until the threshold and the average
        //of the elements after the threshold
        //for each student I need to pick the grade

        double avgGradeBeforeThreshold=0;
        double avgGradeAfterThreshold=0;


        int studentsBeforeThreshold=0;
        int studentsAfterThreshold=0;

        for(int i=0;i<data.size();i++){
            DataPoint dp=data.get(i);
            if (dp.getValue(propertyPath)<=data.get(threshold-1).getLal()){
                avgGradeBeforeThreshold+=dp.getActualCourseGrade();
                studentsBeforeThreshold++;
            }else{
                avgGradeAfterThreshold+=dp.getActualCourseGrade();
                studentsAfterThreshold++;
            }
        }
        avgGradeBeforeThreshold= avgGradeBeforeThreshold/studentsBeforeThreshold;
        avgGradeAfterThreshold= avgGradeAfterThreshold/studentsAfterThreshold;

        for(int i=0;i<data.size();i++){
            DataPoint dp=data.get(i);
            double grade=dp.getActualCourseGrade();
            if (i<threshold){
                residual+=Math.pow((grade-avgGradeBeforeThreshold),2);
            }else{
                residual+=Math.pow((grade-avgGradeAfterThreshold),2);
            }

        }
        return residual;
    }
    private double[] calculateStringResidual(String propertyPath, List<DataPoint> data){
        double[] residuals=new double[getSubNodesCount(propertyPath)];
        //for each student I need to pick the grade
        double[] averageGradeArray= new double[getSubNodesCount(propertyPath)];
        int[] studentsCountArray= new int[getSubNodesCount(propertyPath)];


        for(DataPoint dp:data){
            for(int i=0;i<getSubNodesCount(propertyPath);i++){
                if(dp.getValue(propertyPath)==i){
                    averageGradeArray[i]+=dp.getActualCourseGrade();
                }
            }
        }
        for (int i=0;i<getSubNodesCount(propertyPath);i++){
            averageGradeArray[i]=averageGradeArray[i]/studentsCountArray[i];
        }

        for(DataPoint dp:data){
            double grade=dp.getActualCourseGrade();
            for (int i=0;i<getSubNodesCount(propertyPath);i++){
                if(dp.getValue(propertyPath)==i){
                    residuals[i]+=Math.pow((grade-averageGradeArray[i]),2);
                }
            }
        }
        return  residuals;
    }

    private List<DataPoint> sortList(List<DataPoint> dataList,String property){
        List<DataPoint> newDataList = new ArrayList<>(dataList);
        newDataList.sort(new Comparator<DataPoint>() {
            @Override
            public int compare(DataPoint dp1, DataPoint dp2) {
                return Integer.compare(dp1.getValue(property), dp2.getValue(property));
            }
        });
        return newDataList;
    }


    static class SplitResult {
        String attribute;
        Object value;
        double averageGrade;
        Map<Object, Double> categoricalValues;
        List<String> propertiesLeft;

        SplitResult(String attribute, Object value,List<String> propertiesLeft) {
            this.propertiesLeft=new ArrayList<>(propertiesLeft);
            this.attribute = attribute;
            this.value = value;
        }
    }
    private class ResidualsResult{
        private String propertyName;
        private String propertyValue;
        private double residualValue;

        public  ResidualsResult(String propertyName,String propertyValue,double residualValue){
            this.propertyName=propertyName;
            this.propertyValue=propertyValue;
            this.residualValue=residualValue;
        }
        public String getPropertyName() {
            return propertyName;
        }

        public String getPropertyValue() {
            return propertyValue;
        }

        public double getSumOfResiduals() {
            return residualValue;
        }

    }

    /*private static double predictGPA(TreeNode node, DataPoint dataPoint) {
        if (node.dataPoints != null) {
            // Leaf node, return mean GPA
            return node.meanValue;
        }

        if (isNumericAttribute(node.splitAttribute)) {
            // Numeric split
            double numericValue = (double) node.splitValue;
            if (dataPoint.lal < numericValue) {
                return predictGPA(node.left, dataPoint);
            } else {
                return predictGPA(node.right, dataPoint);
            }
        } else {
            // Categorical split
            String categoricalValue = (String) node.splitValue;
            if (dataPoint.suruna.equals(categoricalValue)) {
                return predictGPA(node.left, dataPoint);
            } else {
                return predictGPA(node.right, dataPoint);
            }
        }
    }*/

    private TreeNode buildTree(List<DataPoint> data,int nodeCandidates,List<String> propertiesLeft) {
        if (shouldStopSplitting(data,propertiesLeft)) {
            // Create a leaf node with the mean GPA as the predicted value
            return new TreeNode(data, calculateMeanGPA(data),true);
        }

        // Find the best split based on the minimum variance

        SplitResult bestSplit = findBestSplit(data,nodeCandidates,propertiesLeft);


        // Create an internal node with the best split
        TreeNode node = new TreeNode(data,bestSplit.attribute, bestSplit.value);

        // Recursively build the left and right subtrees

        //I'm not creating always 2 subtrees anymore, but it will be one branch per property value
        //(when my property is a string), the two subtrees will be only for the numeric property
        List<List<DataPoint>> list_nodesData=new ArrayList<>(new ArrayList<>());
        for(int i=0;i<getSubNodesCount(bestSplit.attribute);i++){
            List<DataPoint> filteredData=filterData(data, bestSplit, i);
            list_nodesData.add(filteredData);
        }
        for(int j=0;j<getSubNodesCount(bestSplit.attribute);j++){
            if(!list_nodesData.get(j).isEmpty()){
                node.nodes.add(buildTree(list_nodesData.get(j),nodeCandidates,bestSplit.propertiesLeft));
            }else{
                //if there is no data for this point i will keep as leaf node the mean value
                //of the parent node
                node.nodes.add(new TreeNode(data, calculateMeanGPA(data), true));
            }
        }

        return node;
    }


    private boolean shouldStopSplitting(List<DataPoint> data,List<String> leftProperties) {
        // Stop splitting if the number of data points in the node is less than or equal to 20
        return data.size() <= 20 || leftProperties.isEmpty();
    }

    private static double calculateMeanGPA(List<DataPoint> data) {
        double sum = 0;
        for (DataPoint dp : data) {
            sum += dp.getActualCourseGrade();
        }
        return sum / data.size();
    }

    private SplitResult findBestSplit(List<DataPoint> data,int nodeCandidates,List<String> propertiesLeft) {
        Random rand = new Random();
        List<String> propListCopy=new ArrayList<>(propertiesLeft);

        //i'm doing a shuffle on the copied list containing the possible properties, then
        //based on the nodeCandidates i will select that number of elements then i will calculate the
        //residual of them

        Collections.shuffle(propListCopy);
        List<ResidualsResult> propertiesOrder=new ArrayList<>();
        if (propertiesLeft.size()>nodeCandidates){
            for(String prop:propListCopy.subList(0,nodeCandidates)){
                propertiesOrder.add(getPropertyResidual(prop,data));
            }
        }else{
            for(String prop:propListCopy){
                propertiesOrder.add(getPropertyResidual(prop,data));
            }
        }

        //Alex 11/01/2024 i will try to do the decision for the best splits here
        propertiesOrder.sort(Comparator.comparing(ResidualsResult::getSumOfResiduals));

        ResidualsResult bestResult=propertiesOrder.getFirst();

        propertiesLeft.remove(bestResult.propertyName);
        return new SplitResult(bestResult.propertyName,bestResult.propertyValue,propertiesLeft);

    }
    private ResidualsResult getPropertyResidual(String propertyName, List<DataPoint> data){
        ResidualsResult result=null;
        switch (propertyName){
            case(surunaPath):
                result=calculateSurunaResidual(data);
                break;
            case(hurniPath):
                result=calculateHurniResidual(data);
                break;
            case(lalPath):
                result=calculateLalResidual(data);
                break;
            case(voltaPath):
                result=calculateVoltaResidual(data);
                break;
        }
        return result;
    }

    private List<DataPoint> filterData(List<DataPoint> data, SplitResult split, int nodeIndex) {
        List<DataPoint> filteredData=new ArrayList<>();
        if(split.attribute.equals(lalPath)){
            filteredData=filterNumericData(data,split,nodeIndex);
        }else {
            filteredData=filterStringData(data,split,nodeIndex);
        }
        return filteredData;
    }
    private List<DataPoint> filterNumericData(List<DataPoint> data, SplitResult split, int nodeIndex){
        List<DataPoint> filteredData=new ArrayList<>();
        for(DataPoint dp:data){
            int propertyValue=(int) dp.getValue(split.attribute);
            int split_propertyValue= Integer.parseInt(split.value.toString());
            if((propertyValue<=split_propertyValue && nodeIndex==0)||(propertyValue>split_propertyValue && nodeIndex==1)){
                filteredData.add(dp);
            }
        }
        return filteredData;
    }
    private List<DataPoint> filterStringData(List<DataPoint> data, SplitResult split, int nodeIndex){
        List<DataPoint> filteredData=new ArrayList<>();
        for(DataPoint dp:data){
            int propertyValue=dp.getValue(split.attribute);
            if((propertyValue==nodeIndex)){
                filteredData.add(dp);
            }
        }
        return filteredData;
    }
    private int getSubNodesCount(String propertyName){
        switch (propertyName){
            case (surunaPath):
                return getSurunaOptions().size();
            case (hurniPath):
                return getHurniOptions().size();
            case (voltaPath):
                return getVoltaOptions().size();
            case (lalPath):
                return 2;
        }
        return 0;
    }
    public double predictGrade(DataPoint selectedStudent){
        TreeNode predictionNode=givePredictionNode(root,selectedStudent);
        assert predictionNode != null;
        return predictionNode.meanValue;
        //assert predictionNode != null;
        //return customRound(predictionNode.meanValue);
    }
    private TreeNode givePredictionNode(TreeNode rootNode, DataPoint dataPoint) {
        // Get the property value from the DataPoint
        int dataPointPropertyValue = dataPoint.getValue(rootNode.splitAttribute);
        if(rootNode.isLeaf){
            return rootNode;
        }
        for(int i=0;i<rootNode.nodes.size();i++){
            TreeNode node =rootNode.nodes.get(i);
            if(!Utils.isNumericProperty(rootNode.splitAttribute)){
                if (dataPointPropertyValue == i) {
                    // If it's a leaf node, we've found our prediction node
                    if (node.isLeaf) {
                        return node; // Return the leaf node
                    } else {
                        // Otherwise, continue searching in the subtree
                        return givePredictionNode(node, dataPoint); // Recursive call
                    }
                }
            }else{
                if (dataPointPropertyValue <= DataPoint.convertValue(rootNode.splitAttribute,rootNode.splitValue.toString())&&i==0) {
                    // in this case the node is the first one
                    if (node.isLeaf) {
                        return node; // Return the leaf node
                    } else {
                        // Otherwise, continue searching in the subtree
                        return givePredictionNode(node, dataPoint); // Recursive call
                    }
                }else if (dataPointPropertyValue > DataPoint.convertValue(rootNode.splitAttribute,rootNode.splitValue.toString())&&i==1){
                    if (node.isLeaf) {
                        return node; // Return the leaf node
                    } else {
                        // Otherwise, continue searching in the subtree
                        return givePredictionNode(node, dataPoint); // Recursive call
                    }
                }
            }
        }
        // If no matching node is found, return null or throw an exception if that's unexpected
        return null;
    }
    private List<String> getSurunaOptions(){
        return Arrays.asList("nulp","doot","lobi");
    }
    private List<String> getHurniOptions(){
        return Arrays.asList("high","low","medium","full","nothing");
    }
    private List<String> getVoltaOptions(){
        return Arrays.asList("1 star","2 stars","3 stars","4 stars","5 stars");
    }
    public TreeNode getRoot(){
        return root;
    }
    public void pruneTree(TreeNode node) {
        if (node == null || node.isLeaf) {
            return;
        }

        // Recursively prune child nodes first
        for(TreeNode childNode : node.nodes) {
            pruneTree(childNode);
        }

        // Check if pruning this node reduces the cost
        if (shouldPrune(node)) {
            node.nodes.clear(); // Remove child nodes
            node.isLeaf = true; // Convert this node to a leaf
            node.meanValue = calculateMeanGPA(node.dataPoints); // Set mean value for the leaf node
            System.out.println("oooo");
        }
    }

    private boolean shouldPrune(TreeNode node) {
        double costIfPruned = calculateCost(node.dataPoints);
        double costIfNotPruned = 0;

        for (TreeNode child : node.nodes) {
            costIfNotPruned += calculateCost(child.dataPoints);
        }

        return costIfPruned < costIfNotPruned;
    }

    private double calculateCost(List<DataPoint> dataPoints) {
        double mean = calculateMeanGPA(dataPoints);
        double cost = 0;
        for (DataPoint dp : dataPoints) {
            cost += Math.pow(dp.getActualCourseGrade() - mean, 2);
        }
        return cost;
    }

}