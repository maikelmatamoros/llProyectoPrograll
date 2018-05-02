package proyecto2progra2;

import business.Gestor;
import domain.Button;
import domain.ChunkMosaic;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Proyecto2Progra2 extends Application {

    private final int WIDTH = 1360;
    private final int HEIGHT = 720;
    private ScrollPane scrollPaneImage, scrollPaneMosaic;
    private BorderPane pane;
    private HBox vBox;
    private Scene scene;
    private Canvas canvasImage, canvasMosaic, canvasUtilities;
    private GraphicsContext graphicContextImage, graphicContextMosaic, graphicsContextUtilities;
    private domain.Button btnSelectImage, btDrawMosaic, btnEraser, btnRotateD, btnRotateI, btnSplit, btnFlipH, btnFlipV, btnDraw;
    private FileChooser fileChooserData, fileChooserImage;
    private Gestor gestor;
    private boolean aux1 = false;
    private boolean aux2 = false;
    private MenuBar menuBar;
    private ArrayList<Button> functionButtonList;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("MosaicMaker");
        this.gestor = new Gestor();
        init(primaryStage);
        //initComponents(primaryStage);
        primaryStage.resizableProperty().set(false);
        primaryStage.show();
    } // start

    private void init(Stage primaryStage) {
        this.fileChooserData = new FileChooser();
        this.fileChooserImage = new FileChooser();
        fileChooserImage.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Extends", "*.png", "*.jpg", "*.jpeg",
                "*.gif", "*.bmp", "*.wbmp"));
        fileChooserData.getExtensionFilters().add(new FileChooser.ExtensionFilter("Dat", "*.dat"));
        this.functionButtonList = new ArrayList<>();
        this.menuBar = new MenuBar();
        Menu menu = new Menu("File");
        MenuItem newProyectItem = new MenuItem("New Proyect");
        MenuItem openProyectItem = new MenuItem("Open Proyect");
        MenuItem saveAsProyectItem = new MenuItem("Save As...");
        MenuItem exportImage = new MenuItem("Export");
        menu.getItems().add(newProyectItem);
        menu.getItems().add(openProyectItem);
        menu.getItems().add(saveAsProyectItem);
        menu.getItems().add(exportImage);

        newProyectItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (gestor.getImage()) {
                    if (alert(primaryStage)) {
                        try {
                            gestor.save(fileChooserData, primaryStage);
                        } catch (IOException ex) {
                            Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    gestor.newProyect(canvasImage, graphicContextImage, graphicContextMosaic, canvasMosaic);
                } else {
                    initComponents(primaryStage);
                }
            }
        });

        openProyectItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (gestor.getImage()) {
                    if (alert(primaryStage)) {
                        try {
                            gestor.save(fileChooserData, primaryStage);
                        } catch (IOException | ClassNotFoundException ex) {
                            Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                }

                File file = fileChooserData.showOpenDialog(primaryStage);
                if (file != null) {
                    initComponents(primaryStage);
                    gestor.reinit(canvasImage, graphicContextImage, graphicContextMosaic, canvasMosaic, file);
                }

            }
        });

        saveAsProyectItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (gestor.getImage()) {
                    try {
                        gestor.save(fileChooserData, primaryStage);
                    } catch (IOException | ClassNotFoundException ex) {
                        Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        });

        exportImage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gestor.exportMosaic(primaryStage, graphicContextMosaic, canvasMosaic, fileChooserData);
            }
        });

        this.menuBar.getMenus().add(menu);
        this.pane = new BorderPane();
        this.pane.setTop(menuBar);
        this.scene = new Scene(this.pane, WIDTH, HEIGHT);
        primaryStage.setScene(this.scene);
    } // init

    private void initComponents(Stage primaryStage) {
        this.vBox = new HBox();

        this.scrollPaneImage = new ScrollPane();
        this.scrollPaneMosaic = new ScrollPane();

        this.btnSelectImage = new Button(20, 35, 170, 50, "selectAnImage");
        this.btnSplit = new Button(200, 35, 170, 50, "split");
        this.btDrawMosaic = new Button(380, 35, 170, 50, "drawMosaic");

        this.btnDraw = new Button(700, 25, 70, 70, "draw");
        this.btnEraser = new Button(800, 25, 70, 70, "eraser");
        this.btnRotateI = new Button(900, 25, 70, 70, "rotateI");
        this.btnRotateD = new Button(1000, 25, 70, 70, "rotateD");
        this.btnFlipH = new Button(1100, 25, 70, 70, "flipH");
        this.btnFlipV = new Button(1200, 25, 70, 70, "flipV");
        this.functionButtonList.add(btnDraw);
        this.functionButtonList.add(btnEraser);
        this.functionButtonList.add(btnFlipH);
        this.functionButtonList.add(btnFlipV);
        this.functionButtonList.add(btnRotateD);
        this.functionButtonList.add(btnRotateI);
        this.canvasImage = new Canvas(1000, 1000);
        this.canvasMosaic = new Canvas();
        this.canvasUtilities = new Canvas(1380, 160);
        this.canvasUtilities.relocate(0, 600);
        this.graphicsContextUtilities = this.canvasUtilities.getGraphicsContext2D();
        try {
            this.btnDraw.setAvailable();
        } catch (IOException ex) {
            Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            drawButtons(this.graphicsContextUtilities);
        } catch (IOException ex) {
            Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.scrollPaneImage.setContent(this.canvasImage);
        this.scrollPaneMosaic.setContent(this.canvasMosaic);

        this.scrollPaneImage.setPrefSize(680, 600);
        this.scrollPaneMosaic.setPrefSize(680, 600);
        this.scrollPaneImage.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        this.scrollPaneImage.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        this.scrollPaneMosaic.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        this.scrollPaneMosaic.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        this.canvasImage.setOnMouseClicked(this.canvasClickEvent);
        this.canvasMosaic.setOnMouseClicked(this.canvasClickEvent);

        this.graphicContextMosaic = this.canvasMosaic.getGraphicsContext2D();
        this.graphicContextImage = this.canvasImage.getGraphicsContext2D();

        this.canvasUtilities.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                try {
                    if (e.getSource() == canvasUtilities) {
                        if (btnSelectImage.isClicked((int) e.getX(), (int) e.getY())) {
                            gestor.selectImage(primaryStage, graphicContextImage, fileChooserImage, canvasImage);
                        } else if (btDrawMosaic.isClicked((int) e.getX(), (int) e.getY())) {
                            if (!gestor.isDefinedValue()) {
                                dialogWidthHeigth();
                            }
                        } else if (btnSplit.isClicked((int) e.getX(), (int) e.getY())) {
                            if (gestor.getSize() == 0 && !gestor.getImage()) {
                                dialogSize();
                            } else if (gestor.getImage() && gestor.getSize() != 0) {
                                gestor.imageChuncks(graphicContextImage, canvasImage);
                            }
                        } else if (btnEraser.isClicked((int) e.getX(), (int) e.getY())) {

                            gestor.setState(functionButtonList, btnEraser);
                            drawButtons(graphicsContextUtilities);
                        } else if (btnRotateD.isClicked((int) e.getX(), (int) e.getY())) {

                            gestor.setState(functionButtonList, btnRotateD);
                            drawButtons(graphicsContextUtilities);
                        } else if (btnRotateI.isClicked((int) e.getX(), (int) e.getY())) {

                            gestor.setState(functionButtonList, btnRotateI);
                            drawButtons(graphicsContextUtilities);
                        } else if (btnDraw.isClicked((int) e.getX(), (int) e.getY())) {

                            gestor.setState(functionButtonList, btnDraw);
                            drawButtons(graphicsContextUtilities);
                        } else if (btnFlipH.isClicked((int) e.getX(), (int) e.getY())) {

                            gestor.setState(functionButtonList, btnFlipH);
                            drawButtons(graphicsContextUtilities);
                        } else if (btnFlipV.isClicked((int) e.getX(), (int) e.getY())) {

                            gestor.setState(functionButtonList, btnFlipV);
                            drawButtons(graphicsContextUtilities);
                        } // else-if
                    } // if
                    // handle
                } catch (IOException ex) {
                    Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        );

        primaryStage.setOnCloseRequest(
                new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event
            ) {
                ButtonType confirm = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
                ButtonType cancel = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", confirm, cancel);
                alert.setContentText("Do you wanna charge a proyect?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == confirm) {
                    try {
                        gestor.save(fileChooserData, primaryStage);
                    } catch (IOException ex) {
                        Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        );

        vBox.getChildren()
                .add(this.scrollPaneImage);
        vBox.getChildren()
                .add(this.scrollPaneMosaic);

        this.pane.setCenter(
                this.vBox);

        this.pane.setBottom(
                this.canvasUtilities);

    } // initComponents

    private void drawButtons(GraphicsContext g) throws IOException {
        g.clearRect(0, 0, 1380, 160);
        g.drawImage(new Image("/assets/background.png"), 0, 0, 1380, 160);
        this.btnSelectImage.draw(g);
        this.btnSplit.draw(g);
        this.btDrawMosaic.draw(g);
        this.btnEraser.draw(g);
        this.btnRotateI.draw(g);
        this.btnRotateD.draw(g);
        this.btnFlipH.draw(g);
        this.btnFlipV.draw(g);
        this.btnDraw.draw(g);
    } // drawButtons

    //                  
    EventHandler<MouseEvent> canvasClickEvent = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            try {
                if (btnRotateD.getState() && event.getSource() == canvasMosaic) {
                    gestor.selectAMosaic((int) event.getX(), (int) event.getY());
                    if (gestor.getMosaicChunk().getImageBytes().length != 0) {
                        ((ChunkMosaic) gestor.getMosaicChunk()).rotate(0);
                        ((ChunkMosaic) gestor.getMosaicChunk()).draw(graphicContextMosaic);
                    }

                } else if (btnRotateI.getState() && event.getSource() == canvasMosaic) {
                    gestor.selectAMosaic((int) event.getX(), (int) event.getY());
                    if (gestor.getMosaicChunk().getImageBytes().length != 0) {
                        ((ChunkMosaic) gestor.getMosaicChunk()).rotate(1);
                        ((ChunkMosaic) gestor.getMosaicChunk()).draw(graphicContextMosaic);
                    }
                } else if (btnFlipH.getState() && event.getSource() == canvasMosaic) {
                    gestor.selectAMosaic((int) event.getX(), (int) event.getY());
                    if (gestor.getMosaicChunk().getImageBytes().length != 0) {
                        ((ChunkMosaic) gestor.getMosaicChunk()).flipHorizontal(1); // Horizontal
                        gestor.getMosaicChunk().draw(graphicContextMosaic);
                    }
                } else if (btnFlipV.getState() && event.getSource() == canvasMosaic) {
                    gestor.selectAMosaic((int) event.getX(), (int) event.getY());
                    if (gestor.getMosaicChunk().getImageBytes().length != 0) {
                        ((ChunkMosaic) gestor.getMosaicChunk()).flipVertical(1);
                        gestor.getMosaicChunk().draw(graphicContextMosaic);
                    }
                } else if (event.getSource() == canvasMosaic && btnEraser.getState()) {
                    gestor.selectAMosaic((int) event.getX(), (int) event.getY());
                    gestor.delete(graphicContextMosaic, canvasMosaic);
                } else if (event.getSource() == canvasMosaic && btnDraw.getState()) {
                    gestor.paintInMosaic((int) event.getX(), (int) event.getY(), graphicContextMosaic);
                } else if (event.getSource() == canvasImage) {
                    if (gestor.getImage()) {
                        gestor.selectAChunckImage((int) event.getX(), (int) event.getY());
                    }// else-if
                } // handle

            } catch (IOException ex) {
                Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }; // canvasClickEvent

    public void dialogSize() {
        // Create the custom dialog.
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Size Dialog");
        dialog.setHeaderText("Please select a size beetwen 50 and " + gestor.getSmaller());

        // Set the button types.
        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField size = new TextField();
        size.setPromptText("Size");

        grid.add(new Label("Size"), 0, 0);
        grid.add(size, 1, 0);

        // Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(confirmButtonType);
        loginButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        size.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(!(newValue.matches("\\d{1,4}")
                    && Integer.parseInt(newValue) >= 50 && Integer.parseInt(newValue) <= gestor.getSmaller()));
        });

        dialog.getDialogPane().setContent(grid);
        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == confirmButtonType) {
            gestor.setSize(Integer.parseInt(size.getText()));
            gestor.imageChuncks(graphicContextImage, canvasImage);
        }
    } // dialogSize

    public void dialogWidthHeigth() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Mosaic dimention Dialog");
        dialog.setHeaderText("please write a width and a height between " + gestor.getSize() + " and 1680");

        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField width = new TextField();
        width.setPromptText("Width");
        TextField heigth = new TextField();
        heigth.setPromptText("Heigth");

        grid.add(new Label("Width:"), 0, 0);
        grid.add(width, 1, 0);
        grid.add(new Label("Heigth:"), 0, 1);
        grid.add(heigth, 1, 1);

        Node loginButton = dialog.getDialogPane().lookupButton(confirmButtonType);
        loginButton.setDisable(true);

        width.textProperty().addListener((observable, oldValue, newValue) -> {
            if ((newValue.matches("\\d{1,4}") && Integer.parseInt(newValue) > gestor.getSize() && Integer.parseInt(newValue) <= 1680)) {
                aux1 = true;
            } else {
                aux1 = false;
            }
            if (aux1 && aux2) {
                loginButton.setDisable(false);
            } else {
                loginButton.setDisable(true);
            }
        });
        heigth.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("\\d{1,4}") && Integer.parseInt(newValue) > gestor.getSize() && Integer.parseInt(newValue) <= 1680) {
                aux2 = true;
            } else {
                aux2 = false;
            }
            if (aux1 && aux2) {
                loginButton.setDisable(false);
            } else {
                loginButton.setDisable(true);
            }
        });
        dialog.getDialogPane().setContent(grid);
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == confirmButtonType) {
            gestor.setMosaicsParameters(Integer.parseInt(heigth.getText()), Integer.parseInt(width.getText()));
            gestor.drawGrid(graphicContextMosaic, canvasMosaic);
            gestor.initMosiacChunks();
        }
    } // dialogWidthHeigth

    public boolean alert(Stage primaryStage) {
        ButtonType confirm = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", confirm, cancel);
        alert.setContentText("Do you want to save before continuing?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == confirm) {
            return true;
        } else {
            return false;
        }
    } // alert

    public static void main(String[] args) {
        launch(args);
    } // main

} // fin de la clase
