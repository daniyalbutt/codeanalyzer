
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.ConstraintsBase;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainMethod extends Application{
	
	int totalJavaFiles = 0;
	int totalNumberOfLines = 0;
	int totalNumberOfWhiteLines = 0;
	int totalNumberOfCommentLines = 0;
	int totalSourceCodeLine = 0;
	int totalPackageCount = 0;
	String javaPathSelected = null;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	 public void start(final Stage primaryStage) throws IOException {
		
        primaryStage.setTitle("Code Analyzer");
        
        Text headingText = new Text("Welcome To Code Analyzer");
        headingText.setStyle("-fx-font: 24 arial;");
        GridPane.setHalignment(headingText,HPos.CENTER);
        
        GridPane UpperGrid = new GridPane();
        UpperGrid.setHgap(10);
        UpperGrid.setVgap(10);
        UpperGrid.setPadding(new Insets(10, 10, 0, 10));
        final DirectoryChooser fileChooser = new DirectoryChooser();
        Button button = new Button("Select File");
        GridPane.setHalignment(button,HPos.RIGHT);
        
        final Text fileText = new Text("No File is Selected");
        fileText.setUnderline(true);
        
        Button AnalyzerButton = new Button("Start Analyzing");
        AnalyzerButton.setAlignment(Pos.CENTER);
        
        Button ProjectStructure = new Button("Project Structure");
        ProjectStructure.setAlignment(Pos.TOP_RIGHT);
        GridPane.setHalignment(ProjectStructure,HPos.RIGHT);
        
        ProjectStructure.setOnAction(new EventHandler<ActionEvent>() {
			
			public void handle(ActionEvent arg0) {
				if(javaPathSelected != null){
					TreeItem<Path> treeItem = new TreeItem<Path>(Paths.get(javaPathSelected));
				    treeItem.setExpanded(true);
				    try {
						createTree( treeItem);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				    treeItem.getChildren().sort( Comparator.comparing( new Function<TreeItem<Path>, String>() {
			            @Override
			            public String apply(TreeItem<Path> t) {
			                return t.getValue().toString().toLowerCase();
			            }
			        }));
				    TreeView<Path> treeView = new TreeView<Path>(treeItem);
			        StackPane root = new StackPane();
			        root.getChildren().add(treeView);
			        primaryStage.setTitle("Folder Tree View Example");
			        primaryStage.show();
			        Stage newWindow = new Stage();
			        newWindow.setScene(new Scene(root, 400, 400));
			        newWindow.initOwner(primaryStage);
			        newWindow.show();
				}else{
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error Box");
					alert.setHeaderText(null);
					alert.setContentText("Please Select the Path of the Project!");
					alert.showAndWait();
				}
			}
		});
        
        UpperGrid.add(headingText, 0, 0, 2, 1);
        UpperGrid.add(button,0 ,1);
        UpperGrid.add(fileText,1 , 1);
        UpperGrid.add(AnalyzerButton, 0, 2, 2, 1);
        UpperGrid.add(ProjectStructure, 1, 2, 2, 1);
        
        GridPane.setHalignment(AnalyzerButton,HPos.CENTER);
        
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(60);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(60);
        UpperGrid.getColumnConstraints().addAll(col1,col2);
       	
        button.setOnAction(new EventHandler<ActionEvent>() {
       	 
           public void handle(ActionEvent event) {
        	   
        	   File selectedDirectory = fileChooser.showDialog(primaryStage);
	           fileText.setText(selectedDirectory.toString());
	           javaPathSelected = selectedDirectory.toString();
	           
           }
           
       });
        
        GridPane MainGrid = new GridPane();
        MainGrid.setAlignment(Pos.CENTER);
        MainGrid.setPadding(new Insets(10, 10, 10, 10));
        MainGrid.setVgap(10);
        MainGrid.setHgap(10);
        
        final Text LowerHeading = new Text("Code Analysis");
        LowerHeading.setStyle("-fx-font: 22 arial;");
        GridPane.setHalignment(LowerHeading,HPos.CENTER);
        
        Text TFiles = new Text("Total Java Files :");
        Text TFilesResponse = new Text(Integer.toString(totalJavaFiles));
        GridPane.setHalignment(TFiles,HPos.RIGHT);
        
        Text TLinesOfCode = new Text("Total Number Of Lines :");
        Text TLinesOfCodeResponse = new Text(Integer.toString(totalNumberOfLines));
        GridPane.setHalignment(TLinesOfCode,HPos.RIGHT);
        
        Text TLinesOfSourceCode = new Text("Total Number Of Source Code Lines :");
        Text TLinesOfSourceCodeResponse = new Text(Integer.toString(totalSourceCodeLine));
        GridPane.setHalignment(TLinesOfSourceCode,HPos.RIGHT);
        
        Text TWhiteSpaces = new Text("Total Whitespaces Lines :");
        Text TWhiteSpacesResponse = new Text(Integer.toString(totalNumberOfWhiteLines));
        GridPane.setHalignment(TWhiteSpaces,HPos.RIGHT);

        Text TComments = new Text("Total Comments :");
        Text TCommentsResponse = new Text(Integer.toString(totalNumberOfCommentLines));
        GridPane.setHalignment(TComments,HPos.RIGHT);
        
        Text TPakage = new Text("Total Pakages :");
        Text TPakageResponse = new Text(Integer.toString(totalPackageCount));
        GridPane.setHalignment(TPakage,HPos.RIGHT);
        
        final GridPane LowerGrid = new GridPane();
        LowerGrid.setAlignment(Pos.CENTER);
        LowerGrid.setPadding(new Insets(10, 10, 10, 10));
        LowerGrid.setVgap(10);
        LowerGrid.setHgap(10);
        
        LowerGrid.add(LowerHeading, 0, 0, 2, 1);
        LowerGrid.add(TFiles, 0, 1);
        LowerGrid.add(TFilesResponse, 1, 1);
        LowerGrid.add(TLinesOfCode, 0, 2);
        LowerGrid.add(TLinesOfCodeResponse, 1, 2);
        LowerGrid.add(TLinesOfSourceCode, 0, 3);
        LowerGrid.add(TLinesOfSourceCodeResponse, 1, 3);
        LowerGrid.add(TWhiteSpaces, 0, 4);
        LowerGrid.add(TWhiteSpacesResponse, 1,4);
        LowerGrid.add(TComments, 0, 5);
        LowerGrid.add(TCommentsResponse, 1,5);
        LowerGrid.add(TPakage, 0, 6);
        LowerGrid.add(TPakageResponse, 1, 6);
        
        AnalyzerButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(javaPathSelected != null){
					CodeAnalyzer codeAnlyzer = new CodeAnalyzer();
		           	try {
						codeAnlyzer.countFiles(javaPathSelected);
						totalJavaFiles = codeAnlyzer.getCount();
			           	totalNumberOfLines = codeAnlyzer.getTotalLines();
			           	totalNumberOfWhiteLines = codeAnlyzer.getTotalWhiteSpaceLines();
			           	totalNumberOfCommentLines= codeAnlyzer.getTotalCommentLines();
			           	totalSourceCodeLine = codeAnlyzer.getTotalSourceCodeLines();
			           	totalPackageCount = codeAnlyzer.removeTheSameElement();
			           	showTheGridPane(primaryStage);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error Box");
					alert.setHeaderText(null);
					alert.setContentText("Please Select the Path of the Project!");
					alert.showAndWait();
				}
			}
		});
        
        ColumnConstraints LowerCol1 = new ColumnConstraints();
        LowerCol1.setPercentWidth(50);
        ColumnConstraints LowerCol2 = new ColumnConstraints();
        LowerCol2.setPercentWidth(50);
        LowerGrid.getColumnConstraints().addAll(LowerCol1,LowerCol2);
        
        GridPane GraphGrid = new GridPane();
        GraphGrid.setAlignment(Pos.BOTTOM_CENTER);
        GraphGrid.setPadding(new Insets(10, 10, 10, 10));
        GraphGrid.setVgap(10);
        GraphGrid.setHgap(10);
        GraphGrid.setGridLinesVisible(true);
        
        //Defining X Axis
        CategoryAxis xAxis = new CategoryAxis(); 
        xAxis.setLabel("Code Analyzing");
        		
        //Defining Y Axis
        NumberAxis yAxis = new NumberAxis(); 
        yAxis.setLabel("Code Visualization");
        
        //Creating the Bar chart 
        BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis, yAxis);  
        barChart.setTitle("Visualization");
        
        //Prepare XYChart.Series objects by setting data        
        XYChart.Series<String, Number> series1 = new XYChart.Series(); 
        series1.setName("Files"); 
        series1.getData().add(new XYChart.Data("", totalJavaFiles));

        XYChart.Series<String, Number> series2 = new XYChart.Series(); 
        series2.setName("Total Lines"); 
        series2.getData().add(new XYChart.Data("", totalNumberOfLines)); 
        
        XYChart.Series<String, Number> series3 = new XYChart.Series(); 
        series3.setName("Total Source Code"); 
        series3.getData().add(new XYChart.Data("", totalSourceCodeLine)); 

        XYChart.Series<String, Number> series4 = new XYChart.Series(); 
        series4.setName("Whitespaces Lines"); 
        series4.getData().add(new XYChart.Data("", totalNumberOfWhiteLines)); 
        
        XYChart.Series<String, Number> series5 = new XYChart.Series(); 
        series5.setName("Comment Lines"); 
        series5.getData().add(new XYChart.Data("", totalNumberOfCommentLines)); 
        
        XYChart.Series<String, Number> series6 = new XYChart.Series(); 
        series6.setName("Total Pakages"); 
        series6.getData().add(new XYChart.Data("", totalPackageCount));
        
        //Setting the data to bar chart
        barChart.getData().addAll(series1, series2, series3, series4, series5, series6);
        
        //Creating Group Object
        Group root = new Group(barChart);
        GridPane.setHalignment(root,HPos.CENTER);
        
        MainGrid.add(UpperGrid,0 ,0);
        MainGrid.add(LowerGrid,0 ,1);
        MainGrid.add(root,0 ,2);
        
        Scene scene = new Scene(MainGrid, 600, 760);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
	
	public void showTheGridPane(final Stage primaryStage){
		primaryStage.setTitle("Code Analyzer");
        
        Text headingText = new Text("Welcome To Code Analyzer");
        headingText.setStyle("-fx-font: 24 arial;");
        GridPane.setHalignment(headingText,HPos.CENTER);
        
        GridPane UpperGrid = new GridPane();
        UpperGrid.setHgap(10);
        UpperGrid.setVgap(10);
        UpperGrid.setPadding(new Insets(10, 10, 0, 10));
        final DirectoryChooser fileChooser = new DirectoryChooser();
        Button button = new Button("Select File");
        GridPane.setHalignment(button,HPos.RIGHT);
        
        final Text fileText = new Text(javaPathSelected);
        fileText.setUnderline(true);
        
        Button AnalyzerButton = new Button("Start Analyzing");
        AnalyzerButton.setAlignment(Pos.CENTER);
        
        Button ProjectStructure = new Button("Project Structure");
        ProjectStructure.setAlignment(Pos.TOP_RIGHT);
        GridPane.setHalignment(ProjectStructure,HPos.RIGHT);
        
ProjectStructure.setOnAction(new EventHandler<ActionEvent>() {
			
			public void handle(ActionEvent arg0) {
				if(javaPathSelected != null){
					TreeItem<Path> treeItem = new TreeItem<Path>(Paths.get(javaPathSelected));
				    treeItem.setExpanded(true);
				    try {
						createTree( treeItem);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				    treeItem.getChildren().sort( Comparator.comparing( new Function<TreeItem<Path>, String>() {
			            @Override
			            public String apply(TreeItem<Path> t) {
			                return t.getValue().toString().toLowerCase();
			            }
			        }));
				    TreeView<Path> treeView = new TreeView<Path>(treeItem);
			        StackPane root = new StackPane();
			        root.getChildren().add(treeView);
			        primaryStage.setTitle("Folder Tree View Example");
			        primaryStage.show();
			        Stage newWindow = new Stage();
			        newWindow.setScene(new Scene(root, 400, 400));
			        newWindow.initOwner(primaryStage);
			        newWindow.show();
				}else{
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error Box");
					alert.setHeaderText(null);
					alert.setContentText("Please Select the Path of the Project!");
					alert.showAndWait();
				}
			}
		});

        UpperGrid.add(headingText, 0, 0, 2, 1);
        UpperGrid.add(button,0 ,1);
        UpperGrid.add(fileText,1 , 1);
        UpperGrid.add(AnalyzerButton, 0, 2, 2, 1);
        UpperGrid.add(ProjectStructure, 1, 2, 2, 1);
        
        GridPane.setHalignment(AnalyzerButton,HPos.CENTER);
        
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(60);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(60);
        UpperGrid.getColumnConstraints().addAll(col1,col2);
       	
        button.setOnAction(new EventHandler<ActionEvent>() {
       	 
           public void handle(ActionEvent event) {
        	   
        	   File selectedDirectory = fileChooser.showDialog(primaryStage);
	           fileText.setText(selectedDirectory.toString());
	           javaPathSelected = selectedDirectory.toString();
	           
           }
           
       });
        
        GridPane MainGrid = new GridPane();
        MainGrid.setAlignment(Pos.CENTER);
        MainGrid.setPadding(new Insets(10, 10, 10, 10));
        MainGrid.setVgap(10);
        MainGrid.setHgap(10);
        
        final Text LowerHeading = new Text("Code Analysis");
        LowerHeading.setStyle("-fx-font: 22 arial;");
        GridPane.setHalignment(LowerHeading,HPos.CENTER);
        
        Text TFiles = new Text("Total Java Files :");
        Text TFilesResponse = new Text(Integer.toString(totalJavaFiles));
        GridPane.setHalignment(TFiles,HPos.RIGHT);
        
        Text TLinesOfCode = new Text("Total Number Of Lines :");
        Text TLinesOfCodeResponse = new Text(Integer.toString(totalNumberOfLines));
        GridPane.setHalignment(TLinesOfCode,HPos.RIGHT);
        
        Text TLinesOfSourceCode = new Text("Total Number Of Source Code Lines :");
        Text TLinesOfSourceCodeResponse = new Text(Integer.toString(totalSourceCodeLine));
        GridPane.setHalignment(TLinesOfSourceCode,HPos.RIGHT);
        
        Text TWhiteSpaces = new Text("Total Whitespaces Lines :");
        Text TWhiteSpacesResponse = new Text(Integer.toString(totalNumberOfWhiteLines));
        GridPane.setHalignment(TWhiteSpaces,HPos.RIGHT);

        Text TComments = new Text("Total Comments :");
        Text TCommentsResponse = new Text(Integer.toString(totalNumberOfCommentLines));
        GridPane.setHalignment(TComments,HPos.RIGHT);
        
        Text TPakage = new Text("Total Pakage :");
        Text TPakageResponse = new Text(Integer.toString(totalPackageCount));
        GridPane.setHalignment(TPakage,HPos.RIGHT);
        
        final GridPane LowerGrid = new GridPane();
        LowerGrid.setAlignment(Pos.CENTER);
        LowerGrid.setPadding(new Insets(10, 10, 10, 10));
        LowerGrid.setVgap(10);
        LowerGrid.setHgap(10);
        
        LowerGrid.add(LowerHeading, 0, 0, 2, 1);
        LowerGrid.add(TFiles, 0, 1);
        LowerGrid.add(TFilesResponse, 1, 1);
        LowerGrid.add(TLinesOfCode, 0, 2);
        LowerGrid.add(TLinesOfCodeResponse, 1, 2);
        LowerGrid.add(TLinesOfSourceCode, 0, 3);
        LowerGrid.add(TLinesOfSourceCodeResponse, 1, 3);
        LowerGrid.add(TWhiteSpaces, 0, 4);
        LowerGrid.add(TWhiteSpacesResponse, 1,4);
        LowerGrid.add(TComments, 0, 5);
        LowerGrid.add(TCommentsResponse, 1,5);
        LowerGrid.add(TPakage, 0, 6);
        LowerGrid.add(TPakageResponse, 1,6);
        
        AnalyzerButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(javaPathSelected != null){
					CodeAnalyzer codeAnlyzer = new CodeAnalyzer();
		           	try {
						codeAnlyzer.countFiles(javaPathSelected);
						totalJavaFiles = codeAnlyzer.getCount();
			           	totalNumberOfLines = codeAnlyzer.getTotalLines();
			           	totalNumberOfWhiteLines = codeAnlyzer.getTotalWhiteSpaceLines();
			           	totalNumberOfCommentLines= codeAnlyzer.getTotalCommentLines();
			           	totalSourceCodeLine = codeAnlyzer.getTotalSourceCodeLines();
			           	totalPackageCount = codeAnlyzer.removeTheSameElement();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
        
        ColumnConstraints LowerCol1 = new ColumnConstraints();
        LowerCol1.setPercentWidth(50);
        ColumnConstraints LowerCol2 = new ColumnConstraints();
        LowerCol2.setPercentWidth(50);
        LowerGrid.getColumnConstraints().addAll(LowerCol1,LowerCol2);
        
        GridPane GraphGrid = new GridPane();
        GraphGrid.setAlignment(Pos.BOTTOM_CENTER);
        GraphGrid.setPadding(new Insets(10, 10, 10, 10));
        GraphGrid.setVgap(10);
        GraphGrid.setHgap(10);
        GraphGrid.setGridLinesVisible(true);
        
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Code Analyzing");
        		
        //Defining Y Axis
        NumberAxis yAxis = new NumberAxis(); 
        yAxis.setLabel("Code Visualization");
        
        //Creating the Bar chart 
        BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis, yAxis);  
        barChart.setTitle("Visualization");
        
        //Prepare XYChart.Series objects by setting data        
        XYChart.Series<String, Number> series1 = new XYChart.Series(); 
        series1.setName("Files"); 
        series1.getData().add(new XYChart.Data("", totalJavaFiles));

        XYChart.Series<String, Number> series2 = new XYChart.Series(); 
        series2.setName("Total Lines"); 
        series2.getData().add(new XYChart.Data("", totalNumberOfLines)); 
        
        XYChart.Series<String, Number> series3 = new XYChart.Series(); 
        series3.setName("Total Source Code"); 
        series3.getData().add(new XYChart.Data("", totalSourceCodeLine)); 

        XYChart.Series<String, Number> series4 = new XYChart.Series(); 
        series4.setName("Whitespaces Lines"); 
        series4.getData().add(new XYChart.Data("", totalNumberOfWhiteLines)); 
        
        XYChart.Series<String, Number> series5 = new XYChart.Series(); 
        series5.setName("Comment Lines"); 
        series5.getData().add(new XYChart.Data("", totalNumberOfCommentLines)); 
        
        XYChart.Series<String, Number> series6 = new XYChart.Series(); 
        series6.setName("Total Pakages"); 
        series6.getData().add(new XYChart.Data("", totalPackageCount));
        
        //Setting the data to bar chart
        barChart.getData().addAll(series1, series2, series3, series4, series5, series6);
        
        //Creating Group Object
        Group root = new Group(barChart);
        GridPane.setHalignment(root,HPos.CENTER);
        
        MainGrid.add(UpperGrid,0 ,0);
        MainGrid.add(LowerGrid,0 ,1);
        MainGrid.add(root,0 ,2);
        
        Scene scene = new Scene(MainGrid, 600, 760);
        primaryStage.setScene(scene);
        primaryStage.show();
	}
	
	public static void createTree(TreeItem<Path> rootItem) throws IOException {

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(rootItem.getValue())) {

            for (Path path : directoryStream) {

                TreeItem<Path> newItem = new TreeItem<Path>(path);
                newItem.setExpanded(true);

                rootItem.getChildren().add(newItem);

                if (Files.isDirectory(path)) {
                    createTree(newItem);
                }
            }
        }
    }
	
}
