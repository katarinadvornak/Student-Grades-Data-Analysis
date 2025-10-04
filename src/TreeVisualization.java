package src;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.util.*;

public class TreeVisualization extends Application {
    private double verticalGap = 80; // Vertical gap between levels
    private Map<Integer, Double> levelWidthMap = new HashMap<>();
    private TreeNode root;
    private Course selectedCourse;

    public TreeVisualization(TreeNode root) {
        this.root = root;
        calculateLevelWidth(root, 0,10);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane rootPane = new Pane();

        double maxWidth = levelWidthMap.values().stream().max(Double::compare).orElse(Double.valueOf(0));
        double initialHorizontalGap = 25*maxWidth;

        BackgroundFill backgroundFill = new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY);
        Background background = new Background(backgroundFill);
        rootPane.setBackground(background);

        visualizeTree(rootPane, root, 400, 0,initialHorizontalGap,0); // Start from the center of the pane

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(rootPane, screenBounds.getWidth(), screenBounds.getHeight());
        scene.setFill(Color.WHITE);
        primaryStage.setTitle("Tree Visualization");
        primaryStage.setScene(scene);

        // Panning
        rootPane.setOnMousePressed(pressEvent -> {
            // Store the initial press positions
            final double[] initialX = {pressEvent.getSceneX()};
            final double[] initialY = {pressEvent.getSceneY()};

            rootPane.setOnMouseDragged(dragEvent -> {
                // Calculate the delta in x and y directions
                double deltaX = dragEvent.getSceneX() - initialX[0];
                double deltaY = dragEvent.getSceneY() - initialY[0];

                // Update the translation based on the delta
                rootPane.setTranslateX(rootPane.getTranslateX() + deltaX);
                rootPane.setTranslateY(rootPane.getTranslateY() + deltaY);

                // Update the initial press positions for the next drag event
                initialX[0] = dragEvent.getSceneX();
                initialY[0] = dragEvent.getSceneY();
            });
        });

        // Zooming
        rootPane.setOnScroll(scrollEvent -> {
            double zoomFactor = 1.05;
            double delta_Y = scrollEvent.getDeltaY();

            // Calculate the mouse position relative to the rootPane
            double mouseX = scrollEvent.getX();
            double mouseY = scrollEvent.getY();

            // Adjust the zoom factor based on the mouse position
            if (delta_Y < 0) {
                zoomFactor = 1 / zoomFactor;
            }

            // Save the current scale
            double currentScaleX = rootPane.getScaleX();
            double currentScaleY = rootPane.getScaleY();

            // Apply scaling
            rootPane.setScaleX(currentScaleX * zoomFactor);
            rootPane.setScaleY(currentScaleY * zoomFactor);

            // Adjust the translation to keep the mouse position fixed after zooming
            double deltaX = (rootPane.getScaleX() - currentScaleX) * mouseX;
            double deltaY = (rootPane.getScaleY() - currentScaleY) * mouseY;

            rootPane.setTranslateX(rootPane.getTranslateX() - deltaX);
            rootPane.setTranslateY(rootPane.getTranslateY() - deltaY);
        });



        primaryStage.show();

    }

    private void visualizeTree(Pane pane, TreeNode node, double x, double y, double horizontalGap, int level) {
        if (node == null) return;

        double nodeSize = Math.max(20, 40 - level * 5); // Reduce size with depth to fit more nodes
        Circle circle = new Circle(x, y, nodeSize);
        circle.setRadius(nodeSize);
        Text label = createLabelForNode(node, x, y - nodeSize - 4); // Adjust label above the node

        pane.getChildren().addAll(circle, label);

        if (node.nodes != null && !node.nodes.isEmpty()) {
            // Increase horizontal gap for nodes with more children
            double nextLevelGap = horizontalGap / Math.sqrt(node.nodes.size());
            double totalWidth = (node.nodes.size() - 1) * nextLevelGap;
            double startX = x - totalWidth / 2;

            for (int i = 0; i < node.nodes.size(); i++) {
                TreeNode childNode = node.nodes.get(i);
                double childX = startX + i * nextLevelGap;
                double childY = y + verticalGap + nodeSize * 2; // Increase vertical gap

                // Adjust line start and end points to avoid overlapping with nodes and labels
                double lineStartY = y + nodeSize;
                double lineEndY = childY - nodeSize;
                Line line = new Line(x, lineStartY, childX, lineEndY);
                pane.getChildren().add(line);

                // Recursive call to visualize child nodes
                visualizeTree(pane, childNode, childX, childY, nextLevelGap / 2, level + 1);
            }
        }
    }


    private Text createLabelForNode(TreeNode node, double x, double y) {
        // Further styling of the label, such as setting the font size, color, etc.
        return new Text(x, y, getNodeLabel(node));
    }

    private String getNodeLabel(TreeNode node) {
        String label="";
        if (node.isLeaf){
            label=String.valueOf(Utils.customRound(node.meanValue));
        }else{
            if (!Utils.isNumericProperty(node.splitAttribute)){
                label=node.splitAttribute + ": " + node.splitValue;
            }else{
                label=node.splitAttribute + "> " + node.splitValue;
            }
        }
        return label;
    }


    private void calculateLevelWidth(TreeNode node, int level, double nodeWidth) {
        if (node == null) return;

        // Assume each node will require its width plus some buffer space
        double requiredWidth = node.nodes != null ? node.nodes.size() * nodeWidth : 0;

        // Update the levelWidthMap with the maximum required width at this level
        levelWidthMap.put(level, Math.max(levelWidthMap.getOrDefault(level, (double) 0), requiredWidth));

        // Recursively call for children with increased node width for deeper levels
        if (node.nodes != null) {
            for (TreeNode child : node.nodes) {
                calculateLevelWidth(child, level + 1, nodeWidth * 1.5);
            }
        }
    }

    private double calculateInitialHorizontalGap(double maxWidth) {
        // Adjust this formula as needed for your application's requirements
        return Math.max(700 / Math.pow(2, maxWidth - 1), 20);
    }

    public static void main(String[] args) {
        launch(args);
    }
}