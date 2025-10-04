package src;
import java.util.*;

public class RandomForest {
    private class EvaluationElement{
        public EvaluationElement(DataPoint dataPoint){
            this.dataPoint=dataPoint;
            predictedGrades=new ArrayList<>();
        }
        private DataPoint dataPoint;
        private List<Double> predictedGrades;
        private double finalPrediction;
    }
    private double MSE;
    private List<RegressionTree> trees;
    private RegressionTree actualTree;
    private List<EvaluationElement>evaluationPoints;
    private List<DataPoint> dataPoints;

    public RandomForest(int numTrees, List<DataPoint> dataPoints,int nodeCandidates) {
        //first thing first I'm going to create a buffered list from the
        //original dataset
        try{
            trees = new ArrayList<>();
            evaluationPoints=new ArrayList<>();
            this.dataPoints=dataPoints;
            createRandomForest(numTrees,nodeCandidates);
            computeMeanGradeForEachPrediction();
            calculateMSE();
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }

    }
    private void createRandomForest(int numTrees,int nodeCandidates){
        for (int i = 0; i < numTrees; i++) {
            actualTree=null;
            List<DataPoint> bootstrappedList=createBootstrappedList(dataPoints);
            //now I need to check which points I didn't consider and store them in another list,
            //we will need them later to check the random forest's accuracy;
            List<DataPoint> outOfBagDataPoints=getOutOfBagDataSet(dataPoints,bootstrappedList);
            actualTree=new RegressionTree(bootstrappedList,nodeCandidates);
            trees.add(actualTree);
            getAndAddGradePrediction(outOfBagDataPoints,actualTree);
        }
    }
    //Method to calculate the mean squared error
    private void calculateMSE(){
        for (EvaluationElement element:evaluationPoints){
            double actualGrade=element.dataPoint.getActualCourseGrade();
            double predictedGrade= element.finalPrediction;
            MSE+=Math.pow(actualGrade-predictedGrade,2);
        }
    }
    private void computeMeanGradeForEachPrediction(){
        for (EvaluationElement element:evaluationPoints){
            double meanGrade=0;
            for (Double grade : element.predictedGrades) {
                meanGrade += grade;
            }
            meanGrade=meanGrade/element.predictedGrades.size();
            element.finalPrediction=meanGrade;
        }
    }
    private void getAndAddGradePrediction(List<DataPoint> outOfBagDataPoints,RegressionTree tree){
        for(DataPoint outOfBag_obj:outOfBagDataPoints){
            boolean isObjectContained=evaluationPoints.stream().
                    anyMatch(evaluationElement -> evaluationElement.dataPoint.getId()==outOfBag_obj.getId());
            if (isObjectContained){
                EvaluationElement element=getElementFromList(outOfBag_obj);
                assert element != null;
                element.predictedGrades.add(tree.predictGrade(outOfBag_obj));
            }else{
                EvaluationElement element=new EvaluationElement(outOfBag_obj);
                element.predictedGrades.add(tree.predictGrade(outOfBag_obj));
                evaluationPoints.add(element);
            }
            tree.predictGrade(outOfBag_obj);
        }
    }
    private EvaluationElement getElementFromList(DataPoint student){
        Optional<EvaluationElement> element=evaluationPoints.stream().filter(evaluationElement ->student.equals(evaluationElement.dataPoint) ).findFirst();
        return element.orElse(null);
    }
    private List<DataPoint> getOutOfBagDataSet(List<DataPoint> originalDataSet,List<DataPoint> bootstrappedList){
        List<DataPoint> outOfBag=new ArrayList<>();
        for (DataPoint dp:originalDataSet){
            if (!bootstrappedList.contains(dp)){
                outOfBag.add(dp);
            }
        }
        return outOfBag;
    }

    private List<DataPoint> createBootstrappedList(List<DataPoint> originalList) {
        List<DataPoint> bootstrappedList = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < originalList.size(); i++) {
            int randomIndex = rand.nextInt(originalList.size()); // Get a random index
            bootstrappedList.add(originalList.get(randomIndex)); // Add the element at the random index to the bootstrapped list
        }
        return bootstrappedList;
    }

    public List<RegressionTree> getTrees() {
        return trees;
    }

    public double getMSE() {
        return MSE;
    }
}