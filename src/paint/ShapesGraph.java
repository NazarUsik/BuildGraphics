package paint;

import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

class ShapesGraph implements Serializable {
    private static transient int NUMBER_COLUMNS = 5;
    private double xMin = -1;
    private double xMax = 1;
    private Function functionFx;
    private Function functionGx;
    private transient Function differenceFunction;
    private transient GraphBuilder builder;
    private String nameProject;
    private transient ArrayList<NumberTextField> pointsX_FromFx;
    private transient ArrayList<NumberTextField> pointsY_FromFx;
    private transient ArrayList<NumberTextField> pointsX_FromGx;
    private transient ArrayList<NumberTextField> pointsY_FromGx;

    private ShapesGraph(ShapesGraph shapesGraph) {
        this.nameProject = shapesGraph.nameProject;
        this.functionFx = new Function(shapesGraph.functionFx);
        this.functionGx = new Function(shapesGraph.functionGx);
        this.differenceFunction = new Function();
        this.xMax = shapesGraph.xMax;
        this.xMin = shapesGraph.xMin;
    }

    public ShapesGraph() {

    }

    void start(Stage primaryStage) {
        primaryStage.setResizable(false);
        primaryStage.setTitle("Functions");

        HBox root = new HBox();

        VBox vBox = new VBox(2);
        vBox.setMaxSize(400, 550);
        vBox.setMinSize(400, 550);
        root.getChildren().

                add(vBox);

        Pane pane = new Pane();
        root.getChildren().add(pane);

        builder = new GraphBuilder(pane);

        // Дуже важливо будувати графік після визначення розмірів панелі:
        Scene scene = new Scene(root, 1000, 550);
        vBox.getChildren().add(createMenuBar(primaryStage, root));
        vBox.getChildren().add(createTableFunction(primaryStage));
        vBox.getChildren().add(createHelpButton(primaryStage));

        builder.drawGraph(this.xMin, this.xMax);
        primaryStage.setScene(scene);
        primaryStage.show();
        if (nameProject == null) {
            TextField namePrj = new TextField();
            NumberTextField setNumberColumns = new NumberTextField();

            Button okButton = new Button("Ok");

            VBox secondaryLayout = new VBox(10);
            HBox hBox = new HBox();
            BorderPane borderPane = new BorderPane();
            borderPane.setCenter(okButton);
            hBox.getChildren().addAll(new Label("Enter name Project: "), namePrj);
            secondaryLayout.getChildren().add(hBox);
            hBox = new HBox();
            hBox.getChildren().addAll(new Label("Enter number of columns: "), setNumberColumns);
            secondaryLayout.getChildren().addAll(hBox, borderPane);

            Scene secondScene = new Scene(secondaryLayout, 230, 150);

            // New window (Stage)
            Stage newWindow = new Stage();
            newWindow.setTitle("Second Stage");
            newWindow.setScene(secondScene);

            // Specifies the modality for new window.
            newWindow.initModality(Modality.WINDOW_MODAL);

            // Specifies the owner Window (parent) for new window
            newWindow.initOwner(primaryStage);

            // Set position of second window, related to primary window.
            newWindow.setX(primaryStage.getX() + 200);
            newWindow.setY(primaryStage.getY() + 100);

            newWindow.show();


            okButton.setOnAction(actionEvent -> {
                try {
                    this.nameProject = namePrj.getText();
                    NUMBER_COLUMNS = Integer.parseInt(setNumberColumns.getText());
                    this.start(primaryStage);
                    newWindow.close();
                } catch (Exception ex) {
                    StackPane secondary = new StackPane();
                    secondary.getChildren().add(new Label("Number of columns must fill!"));

                    Scene sc = new Scene(secondary, 230, 100);

                    // New window (Stage)
                    Stage newWindow1 = new Stage();
                    newWindow1.setTitle("Second Stage");
                    newWindow1.setScene(sc);

                    // Specifies the modality for new window.
                    newWindow1.initModality(Modality.WINDOW_MODAL);

                    // Specifies the owner Window (parent) for new window
                    newWindow1.initOwner(primaryStage);

                    // Set position of second window, related to primary window.
                    newWindow1.setX(primaryStage.getX() + 200);
                    newWindow1.setY(primaryStage.getY() + 100);

                    newWindow1.show();
                }
            });
        }
    }

    private VBox createHelpButton(Stage primaryStage) {
        VBox vBox = new VBox();
        //_________________________________________Create help button___________________________________________________
        NumberTextField textFieldToXMin = new NumberTextField();
        NumberTextField textFieldToXMax = new NumberTextField();
        Button defineLimitsButton = new Button("Define Limits");
        defineLimitsButton.setOnAction(actionEvent -> {
            try {
                if(!textFieldToXMax.getText().equals(textFieldToXMin.getText())) {
                    this.xMax = Double.parseDouble(textFieldToXMax.getText());
                    this.xMin = Double.parseDouble(textFieldToXMin.getText());
                    builder.drawGraph(this.xMin, this.xMax);
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText(null);
                    alert.setContentText("xMax != xMin!");
                    alert.showAndWait();
                }
            } catch (Exception ex) {
                StackPane secondaryLayout = new StackPane();
                secondaryLayout.getChildren().add(new Label("Fill define limits!"));

                Scene secondScene = new Scene(secondaryLayout, 230, 100);

                // New window (Stage)
                Stage newWindow = new Stage();
                newWindow.setTitle("Second Stage");
                newWindow.setScene(secondScene);

                // Specifies the modality for new window.
                newWindow.initModality(Modality.WINDOW_MODAL);

                // Specifies the owner Window (parent) for new window
                newWindow.initOwner(primaryStage);

                // Set position of second window, related to primary window.
                newWindow.setX(primaryStage.getX() + 200);
                newWindow.setY(primaryStage.getY() + 100);

                newWindow.show();
            }
        });

        HBox hBox = new HBox();
        VBox tmpVBox = new VBox();
        HBox tmpHBox = new HBox();
        tmpHBox.getChildren().addAll(new Label("From: "), textFieldToXMin, new Label("To: "), textFieldToXMax);
        tmpVBox.getChildren().addAll(tmpHBox);
        tmpHBox = new HBox();
        tmpHBox.getChildren().addAll(new Label("           "), defineLimitsButton);
        hBox.getChildren().add(tmpHBox);
        tmpVBox.getChildren().addAll(tmpHBox);
        hBox.getChildren().add(tmpVBox);
        tmpHBox = new HBox();
        Button diffFuncButton = new Button("Build f(x) - g(x)");
        Label maxLabel = new Label("Max: y = 0;\n         x = 0;");
        diffFuncButton.setOnAction(actionEvent -> {
            try {
                Function.setDomainOfDefinition(this.functionFx.getArrayOfPoints(), this.functionGx.getArrayOfPoints());
                this.functionFx.Interpolate();
                this.functionGx.Interpolate();
                this.differenceFunction = new Function(new Equation(this.functionFx.getLagrangeFunc(), this.functionGx.getLagrangeFunc()).getFuncDifference());
                builder.addFunction(this.differenceFunction, Color.PURPLE, 3);
                builder.drawGraph(this.xMin, this.xMax);
                differenceFunction.writeMaxInXML("D:\\Programming\\JavaPrograms\\kursach\\Max.xml");
                try {
                    Point tmpPoint = new Point(differenceFunction.findMaxToDichotomy());
                    maxLabel.setText("Max: y = " + (float) (tmpPoint.getY()) + ";\n         x = " + (float) (tmpPoint.getX()) + ";");
                } catch (NullPointerException ex) {
                    maxLabel.setText("Max: y = 0;\n         x = 0;");

                }
            } catch (Exception ex) {
                StackPane secondaryLayout = new StackPane();
                secondaryLayout.getChildren().add(new Label("Set 2 functions first!"));

                Scene secondScene = new Scene(secondaryLayout, 230, 100);

                // New window (Stage)
                Stage newWindow = new Stage();
                newWindow.setTitle("Second Stage");
                newWindow.setScene(secondScene);

                // Specifies the modality for new window.
                newWindow.initModality(Modality.WINDOW_MODAL);

                // Specifies the owner Window (parent) for new window
                newWindow.initOwner(primaryStage);

                // Set position of second window, related to primary window.
                newWindow.setX(primaryStage.getX() + 200);
                newWindow.setY(primaryStage.getY() + 100);

                newWindow.show();
            }
        });
        Separator separator = new Separator(Orientation.VERTICAL);
        separator.setMinSize(20, 70);
        tmpHBox.getChildren().addAll(separator, diffFuncButton, maxLabel);

        hBox.getChildren().add(tmpHBox);
        vBox.getChildren().add(hBox);


        Button updateGraphButton = new Button("Update Graph");
        Button clearGraphButton = new Button("Clear Graph");
        updateGraphButton.setOnAction(actionEvent -> builder.drawGraph(this.xMin, this.xMax));
        clearGraphButton.setOnAction(actionEvent -> {
            builder.clearFunctions();
            builder.drawGraph(this.xMin, this.xMax);
        });
        tmpHBox = new HBox(10);
        tmpHBox.getChildren().addAll(updateGraphButton, clearGraphButton);
        separator = new Separator();
        separator.setMinHeight(30);
        vBox.getChildren().addAll(separator, tmpHBox);
        //______________________________________________________________________________________________________________
        return vBox;
    }

    private VBox createTableFunction(Stage primaryStage) {
        VBox vBox = new VBox();
        //_____________________________________Table f(x)_______________________________________________________________
        GridPane tableFx = new GridPane();
        tableFx.setMaxSize(400, 50);
        tableFx.setMinSize(400, 50);
        tableFx.addRow(0);
        tableFx.addRow(1);
        tableFx.addColumn(0, new Label("x:"), new Label("y:"));
        pointsX_FromFx = new ArrayList<>();
        pointsY_FromFx = new ArrayList<>();

        for (int i = 1; i <= NUMBER_COLUMNS; i++) {
            pointsX_FromFx.add(new NumberTextField());
            pointsY_FromFx.add(new NumberTextField());
            tableFx.addColumn(i, pointsX_FromFx.get(i - 1), pointsY_FromFx.get(i - 1));
        }
        Button createFunctionButtonFx = new Button("Create");
        createFunctionButtonFx.setOnAction(actionEvent -> {
            try {
                ArrayList<Point> points = new ArrayList<>();
                for (int i = 0; i < pointsX_FromFx.size(); i++) {
                    points.add(new Point(Double.valueOf(pointsX_FromFx.get(i).getText()), Double.valueOf(pointsY_FromFx.get(i).getText())));
                }
                this.functionFx = new Function(points);
                builder.addFunction(this.functionFx, Color.BLUE, 2);
                builder.drawGraph(this.xMin, this.xMax);
            } catch (Exception ex) {
                StackPane secondaryLayout = new StackPane();
                secondaryLayout.getChildren().add(new Label("The table must be completely filled!"));

                Scene secondScene = new Scene(secondaryLayout, 230, 100);

                // New window (Stage)
                Stage newWindow = new Stage();
                newWindow.setTitle("Second Stage");
                newWindow.setScene(secondScene);

                // Specifies the modality for new window.
                newWindow.initModality(Modality.WINDOW_MODAL);

                // Specifies the owner Window (parent) for new window
                newWindow.initOwner(primaryStage);

                // Set position of second window, related to primary window.
                newWindow.setX(primaryStage.getX() + 200);
                newWindow.setY(primaryStage.getY() + 100);

                newWindow.show();
            }
        });

        Button clearFunctionButtonFx = new Button("Clear");
        clearFunctionButtonFx.setOnAction(actionEvent -> {
            for (int i = 0; i < pointsX_FromFx.size(); i++) {
                pointsX_FromFx.get(i).setText("");
                pointsY_FromFx.get(i).setText("");
            }
        });

        Button saveFunctionTableFxButton = new Button("Save function");
        TextField nameFileFx = new TextField("Fx");
        saveFunctionTableFxButton.setOnAction(actionEvent -> {
            ArrayList<Point> points = new ArrayList<>();
            for (int i = 0; i < pointsX_FromFx.size(); i++) {
                points.add(new Point(Double.valueOf(pointsX_FromFx.get(i).getText()), Double.valueOf(pointsY_FromFx.get(i).getText())));
            }
            Function saveFunction = new Function(points);
            saveFunction.writeFuncInXML("D:\\Programming\\JavaPrograms\\kursach\\XLM_Graph\\" + nameFileFx.getText() + ".xml");
        });

        Button openFunctionTableFxButton = new Button("Open function");
        final FileChooser fileChooserTableFx = new FileChooser();
        configuringFileChooserFunc(fileChooserTableFx);
        openFunctionTableFxButton.setOnAction(event -> {
            File file = fileChooserTableFx.showOpenDialog(primaryStage);
            if (file != null) {

                Function function = new Function(file.toString());
                ArrayList<Point> points = new ArrayList<>(function.getArrayOfPoints());
                //numberColumnOfTable = points.size();
                for (int i = 0; i < pointsX_FromFx.size(); i++) {
                    double tmp = (i-(pointsX_FromFx.size()/2));
                    pointsX_FromFx.get(i).setText(String.valueOf(tmp));
                    pointsY_FromFx.get(i).setText(String.valueOf(function.applyAsDouble(tmp)));
                }
                this.functionFx = new Function(points);
                builder.addFunction(functionFx, Color.BLUE, 2);
                builder.drawGraph(this.xMin, this.xMax);
            }
        });

        Label label = new Label("");
        label.setMinSize(400, 23);
        Separator separatorFx = new Separator();
        separatorFx.setMinHeight(1);
        HBox tmpFx = new HBox();
        tmpFx.setMinSize(400, 5);
        tmpFx.getChildren().addAll(new Label("    "), createFunctionButtonFx, new Label("   "), clearFunctionButtonFx, new Label("   "), saveFunctionTableFxButton, new Label("   "), openFunctionTableFxButton);

        //_____________________________________Table g(x)_______________________________________________________________

        GridPane tableGx = new GridPane();
        tableGx.setMaxSize(400, 50);
        tableGx.setMinSize(400, 50);
        tableGx.addRow(0);
        tableGx.addRow(1);
        tableGx.addColumn(0, new Label("x:"), new Label("y:"));
        pointsX_FromGx = new ArrayList<>();
        pointsY_FromGx = new ArrayList<>();


        for (int i = 1; i <= NUMBER_COLUMNS; i++) {
            pointsX_FromGx.add(new NumberTextField());
            pointsY_FromGx.add(new NumberTextField());
            tableGx.addColumn(i, pointsX_FromGx.get(i - 1), pointsY_FromGx.get(i - 1));
        }
        Button createFunctionButtonGx = new Button("Create");
        createFunctionButtonGx.setOnAction(actionEvent -> {
            try {
                ArrayList<Point> points = new ArrayList<>();
                for (int i = 0; i < pointsX_FromGx.size(); i++) {
                    points.add(new Point(Double.valueOf(pointsX_FromGx.get(i).getText()), Double.valueOf(pointsY_FromGx.get(i).getText())));
                }
                this.functionGx = new Function(points);
                builder.addFunction(this.functionGx, Color.RED, 2);
                builder.drawGraph(this.xMin, this.xMax);
            } catch (Exception ex) {
                StackPane secondaryLayout = new StackPane();
                secondaryLayout.getChildren().add(new Label("The table must be completely filled!"));

                Scene secondScene = new Scene(secondaryLayout, 230, 100);

                // New window (Stage)
                Stage newWindow = new Stage();
                newWindow.setTitle("Second Stage");
                newWindow.setScene(secondScene);

                // Specifies the modality for new window.
                newWindow.initModality(Modality.WINDOW_MODAL);

                // Specifies the owner Window (parent) for new window
                newWindow.initOwner(primaryStage);

                // Set position of second window, related to primary window.
                newWindow.setX(primaryStage.getX() + 200);
                newWindow.setY(primaryStage.getY() + 100);

                newWindow.show();
            }
        });

        Button clearFunctionButtonGx = new Button("Clear");
        clearFunctionButtonGx.setOnAction(actionEvent -> {
            for (int i = 0; i < pointsX_FromGx.size(); i++) {
                pointsX_FromGx.get(i).setText("");
                pointsY_FromGx.get(i).setText("");
            }
        });


        Button saveFunctionTableGxButton = new Button("Save function");
        TextField nameFileGx = new TextField("Gx");
        saveFunctionTableGxButton.setOnAction(event -> {
            ArrayList<Point> points = new ArrayList<>();
            for (int i = 0; i < pointsX_FromGx.size(); i++) {
                points.add(new Point(Double.valueOf(pointsX_FromGx.get(i).getText()), Double.valueOf(pointsY_FromGx.get(i).getText())));
            }
            Function saveFunction = new Function(points);
            saveFunction.writeFuncInXML("D:\\Programming\\JavaPrograms\\kursach\\XLM_Graph\\" + nameFileGx.getText() + ".xml");
        });

        Button openFunctionTableGxButton = new Button("Open function");
        final FileChooser fileChooserTableGx = new FileChooser();
        configuringFileChooserFunc(fileChooserTableGx);
        openFunctionTableGxButton.setOnAction(event -> {
            File file = fileChooserTableGx.showOpenDialog(primaryStage);
            if (file != null) {
                Function function = new Function(file.toString());
                ArrayList<Point> points = new ArrayList<>(function.getArrayOfPoints());
                //numberColumnOfTable = points.size();
                for (int i = 0; i < pointsX_FromGx.size(); i++) {
                    double tmp = (i-(pointsX_FromFx.size()/2));
                    pointsX_FromGx.get(i).setText(String.valueOf(tmp));
                    pointsY_FromGx.get(i).setText(String.valueOf(function.applyAsDouble(tmp)));
                }
                this.functionGx = new Function(points);
                builder.addFunction(functionGx, Color.RED, 2);
                builder.drawGraph(this.xMin, this.xMax);
            }
        });

        Label labelGx = new Label("");
        labelGx.setMinSize(400, 23);
        Separator separatorGx = new Separator();
        separatorGx.setMinHeight(1);
        HBox tmpGx = new HBox();
        tmpGx.setMinSize(400, 5);
        tmpGx.getChildren().addAll(new Label("    "), createFunctionButtonGx, new Label("   "), clearFunctionButtonGx, new Label("   "), saveFunctionTableGxButton, new Label("   "), openFunctionTableGxButton);

        Separator separator = new Separator();
        separator.setMinHeight(50);

        HBox toNameBoxFx = new HBox();
        toNameBoxFx.getChildren().addAll(new Label("Enter name function: "), nameFileFx);

        HBox toNameBoxGx = new HBox();
        toNameBoxGx.getChildren().addAll(new Label("Enter name function: "), nameFileGx);

        vBox.getChildren().addAll(new Label("Enter first function (Blue color)"), toNameBoxFx, tableFx, label, separatorFx, tmpFx);
        vBox.getChildren().add(separator);
        vBox.getChildren().addAll(new Label("Enter second function (Red color)"), toNameBoxGx, tableGx, labelGx, separatorGx, tmpGx);
        separator = new Separator();
        separator.setMinHeight(30);
        vBox.getChildren().add(separator);
        //______________________________________________________________________________________________________________
        return vBox;
    }

    private MenuBar createMenuBar(Stage primaryStage, Pane root) {
        MenuBar menuBar = new MenuBar();


        // Create menus
        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        Menu aboutMenu = new Menu("About");

        MenuItem aboutItem = new MenuItem("Help");
        aboutItem.setOnAction(event -> {
            try {
                File file = new File("src\\HelpFiles\\Main.html");
                Desktop.getDesktop().open(file);
            }catch(IOException ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("File");
                alert.setHeaderText(null);
                alert.setContentText("File not found!");
                alert.showAndWait();
            }
        });
        aboutMenu.getItems().add(aboutItem);

        // Create MenuItems
        MenuItem newItem = new MenuItem("New");
        newItem.setOnAction(event -> {
            TextField namePrj = new TextField();
            NumberTextField setNumberColumns = new NumberTextField();

            Button okButton = new Button("Ok");

            VBox secondaryLayout = new VBox(10);
            HBox hBox = new HBox();
            BorderPane borderPane = new BorderPane();
            borderPane.setCenter(okButton);
            hBox.getChildren().addAll(new Label("Enter name Project: "), namePrj);
            secondaryLayout.getChildren().add(hBox);
            hBox = new HBox();
            hBox.getChildren().addAll(new Label("Enter number of columns: "), setNumberColumns);
            secondaryLayout.getChildren().addAll(hBox, borderPane);

            Scene secondScene = new Scene(secondaryLayout, 230, 150);

            // New window (Stage)
            Stage newWindow = new Stage();
            newWindow.setTitle("Second Stage");
            newWindow.setScene(secondScene);

            // Specifies the modality for new window.
            newWindow.initModality(Modality.WINDOW_MODAL);

            // Specifies the owner Window (parent) for new window
            newWindow.initOwner(primaryStage);

            // Set position of second window, related to primary window.
            newWindow.setX(primaryStage.getX() + 200);
            newWindow.setY(primaryStage.getY() + 100);

            newWindow.show();

            okButton.setOnAction(actionEvent -> {
                this.nameProject = namePrj.getText();
                NUMBER_COLUMNS = Integer.parseInt(setNumberColumns.getText());
                this.start(primaryStage);
                newWindow.close();
            });


        });

        MenuItem saveItem = new MenuItem("Save");
        saveItem.setOnAction(event -> {
            try {
                if (nameProject == null)
                    nameProject = "New Project";
                ObjectOutputStream encoder = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream("D:\\Programming\\JavaPrograms\\kursach\\XML_Save\\" + nameProject + ".zip")));
                encoder.writeObject(this);
                encoder.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        MenuItem openProjectItem = new MenuItem("Open Project");
        final FileChooser fileChooserTableFx = new FileChooser();
        configuringFileChooserPrj(fileChooserTableFx);
        openProjectItem.setOnAction(event -> {
            File file = fileChooserTableFx.showOpenDialog(primaryStage);
            if (file != null) {
                try {
                    ObjectInputStream decoder = new ObjectInputStream(new GZIPInputStream(new FileInputStream(file)));
                    ShapesGraph shapesGraph = new ShapesGraph((ShapesGraph) decoder.readObject());
                    this.nameProject = shapesGraph.nameProject;
                    this.functionFx = new Function(shapesGraph.functionFx);
                    this.functionGx = new Function(shapesGraph.functionGx);
                    this.xMax = shapesGraph.xMax;
                    this.xMin = shapesGraph.xMin;
                    for (int i = 0; i < pointsX_FromFx.size(); i++) {
                        double tmp = (i-(pointsX_FromFx.size()/2));
                        pointsX_FromFx.get(i).setText(String.valueOf(tmp));
                        pointsY_FromFx.get(i).setText(String.valueOf(functionFx.applyAsDouble(tmp)));
                    }
                    for (int i = 0; i < pointsX_FromFx.size(); i++) {
                        double tmp = (i-(pointsX_FromFx.size()/2));
                        pointsX_FromGx.get(i).setText(String.valueOf(tmp));
                        pointsY_FromGx.get(i).setText(String.valueOf(functionGx.applyAsDouble(tmp)));
                    }
                    builder.addFunction(functionFx, Color.BLUE, 2);
                    builder.addFunction(functionGx, Color.RED, 2);
                    //builder.drawGraph(this.xMin, this.xMax);
                    Function.setDomainOfDefinition(this.functionFx.getArrayOfPoints(), this.functionGx.getArrayOfPoints());
                    this.functionFx.Interpolate();
                    this.functionGx.Interpolate();
                    this.differenceFunction = new Function(new Equation(this.functionFx.getLagrangeFunc(), this.functionGx.getLagrangeFunc()).getFuncDifference());
                    builder.addFunction(differenceFunction, Color.PURPLE, 4);
                    builder.drawGraph(this.xMin, this.xMax);

                } catch (ClassNotFoundException | IOException | NullPointerException e) {
                    System.out.println("error");
                    e.printStackTrace();
                }
            }
        });

        MenuItem createReport = new MenuItem("Create report");
        createReport.setOnAction(event -> {
            try {
                WritableImage image = root.snapshot(null, new WritableImage(1000, 550));
                ReportUtils.saveFiles(new File(nameProject + " Report.pdf"), image, nameProject);
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Close the report first, then generate!");
                alert.showAndWait();
            }
        });

        // SeparatorMenuItem.
        SeparatorMenuItem separatorToFile = new SeparatorMenuItem();

        // Set Accelerator for Exit MenuItem. When user click on the Exit item.
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setAccelerator(KeyCombination.keyCombination("Alt+F4"));
        exitItem.setOnAction(event -> System.exit(0));

        SeparatorMenuItem separatorToEdit = new SeparatorMenuItem();

        MenuItem updateGraph = new MenuItem("Update");
        updateGraph.setOnAction(actionEvent -> builder.drawGraph(this.xMin, this.xMax));

        MenuItem clearGraph = new MenuItem("Clear");
        clearGraph.setOnAction(actionEvent -> {
            builder.clearFunctions();
            builder.drawGraph(this.xMin, this.xMax);
        });

        // Add menuItems to the Menus
        fileMenu.getItems().addAll(newItem, saveItem, openProjectItem, createReport, separatorToFile, exitItem);
        editMenu.getItems().addAll(separatorToEdit, updateGraph, clearGraph);

        // Add Menus to the MenuBar
        menuBar.getMenus().addAll(fileMenu, editMenu, aboutMenu);

        // Add event to the MenuBar

        return menuBar;
    }

    private void configuringFileChooserFunc(FileChooser fileChooser) {
        // Set title for FileChooser
        fileChooser.setTitle("Select Graph");
        // Set Initial Directory
        fileChooser.setInitialDirectory(new File("D:\\Programming\\JavaPrograms\\kursach\\XLM_Graph"));
        // Add Extension Filters
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML", "*.xml"));
    }

    private void configuringFileChooserPrj(FileChooser fileChooser) {
        // Set title for FileChooser
        fileChooser.setTitle("Select project");
        // Set Initial Directory
        fileChooser.setInitialDirectory(new File("D:\\Programming\\JavaPrograms\\kursach\\XML_Save"));
        // Add Extension Filters
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Zip", "*.zip"));
    }

}