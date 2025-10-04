package src;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TreeNode {
    String splitAttribute;
    Object splitValue;
    double meanValue;
    List<DataPoint> dataPoints;
    List<TreeNode> nodes;
    boolean isLeaf;

    TreeNode(List<DataPoint> dataPoints, double meanValue, boolean isLeaf) {
        this.dataPoints = dataPoints;
        this.meanValue = meanValue;
        this.isLeaf=isLeaf;
    }

    TreeNode(List<DataPoint> dataPoints,String splitAttribute, Object splitValue) {
        this.nodes=new ArrayList<>();
        this.dataPoints=dataPoints;
        this.splitAttribute = splitAttribute;
        this.splitValue = splitValue;
    }
}