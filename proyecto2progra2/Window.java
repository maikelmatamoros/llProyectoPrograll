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

public class Window extends Application {

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
        primaryStage.resizableProperty().set(false);
        primaryStage.show();
    } // start

    private void init(Stage primaryStage) {
        this.fileChooserData = new FileChooser();
        this.fileChooserImage = new FileChooser();
        this.fileChooserImage.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Extends", "*.png", "*.jpg", "*.jpeg",
                "*.gif", "*.bmp", "*.wbmp"));
        this.fileChooserData.getExtensionFilters().add(new FileChooser.ExtensionFilter("Dat", "*.dat"));
        this.functionButtonList = new ArrayList<>();
        this.menuBar = new MenuBar();
        Menu menu = new Menu("File");
        MenuItem newProjectItem = new MenuItem("New Project");
        MenuItem openProjectItem = new MenuItem("Open Project");
        MenuItem saveAsProjecttItem = new MenuItem("Save As...");
        MenuItem exportImage = new MenuItem("Export");
        menu.getItems().add(newProjectItem);
        menu.getItems().add(openProjectItem);
        menu.getItems().add(saveAsProjecttItem);
        menu.getItems().add(exportImage);

        newProjectItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (gestor.isSplitted()) {
                    if (!gestor.getConts()) {
                        alert(primaryStage);
                    }
                    gestor.newProyect(canvasImage, graphicContextImage, graphicContextMosaic, canvasMosaic);
                } else {
                    initComponents(primaryStage);
                }
            } // handle
        });

        openProjectItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (gestor.isSplitted()) {
                    if (!gestor.getConts()) {
                        alert(primaryStage);
                    }
                }

                File file = fileChooserData.showOpenDialog(primaryStage);
                if (file != null) {
                    initComponents(primaryStage);
                    gestor.recharge(canvasImage, graphicContextImage, graphicContextMosaic, canvasMosaic, file);
                }
            } // handle
        });

        saveAsProjecttItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (gestor.isSplitted()) {
                    try {
                        gestor.saveProject(fileChooserData, primaryStage);
                    } catch (IOException | ClassNotFoundException ex) {
                        Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
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
        this.pane.setTop(this.menuBar);
        this.scene = new Scene(this.pane, WIDTH, HEIGHT);
        primaryStage.setScene(this.scene);
    } // init: inicia el menú

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
        this.functionButtonList.add(this.btnDraw);
        this.functionButtonList.add(this.btnEraser);
        this.functionButtonList.add(this.btnFlipH);
        this.functionButtonList.add(this.btnFlipV);
        this.functionButtonList.add(this.btnRotateD);
        this.functionButtonList.add(this.btnRotateI);
        this.canvasImage = new Canvas(1000, 1000);
        this.canvasMosaic = new Canvas();
        this.canvasUtilities = new Canvas(1380, 160);
        this.canvasUtilities.relocate(0, 600);
        this.graphicsContextUtilities = this.canvasUtilities.getGraphicsContext2D();
        
        try {
            this.btnDraw.setAvailable();
            drawButtons(this.graphicsContextUtilities);
        } catch (IOException ex) {
            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
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
                            gestor.selectAnImage(primaryStage, graphicContextImage, fileChooserImage, canvasImage);
                        } else if (btDrawMosaic.isClicked((int) e.getX(), (int) e.getY())) {
                            if (gestor.isSplitted() && !gestor.isDefinedMosaic()) {
                                dialogWidthHeigth();
                            }
                        } else if (btnSplit.isClicked((int) e.getX(), (int) e.getY())) {
                            if (gestor.getSize() == 0 && !gestor.isSplitted() && gestor.getImageSelected()) {
                                dialogSize();
                            } else if (gestor.isSplitted() && gestor.getSize() != 0 && gestor.getImageSelected()) {
                                gestor.split(graphicContextImage, canvasImage);
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
                } catch (IOException ex) {
                    Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                }
            } // handle
        }
        );

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if (!gestor.getConts()) {
                    alert(primaryStage);
                }
            }
        }
        );

        this.vBox.getChildren().add(this.scrollPaneImage);
        this.vBox.getChildren().add(this.scrollPaneMosaic);
        this.pane.setCenter(this.vBox);
        this.pane.setBottom(this.canvasUtilities);
    } // initComponents: inicia componentes básicos

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
    } // drawButtons: dibuja el fondo y los botones

    EventHandler<MouseEvent> canvasClickEvent = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            try {
                if (btnRotateD.getState() && event.getSource() == canvasMosaic) {
                    gestor.selectAMosaic((int) event.getX(), (int) event.getY());
                    if (gestor.getMosaicChunk().getImageBytes().length != 0) {
                        ((ChunkMosaic) gestor.getMosaicChunk()).rotate(0);
                        ((ChunkMosaic) gestor.getMosaicChunk()).draw(graphicContextMosaic);
                        gestor.mosaicChanges(0);
                    }
                } else if (btnRotateI.getState() && event.getSource() == canvasMosaic) {
                    gestor.selectAMosaic((int) event.getX(), (int) event.getY());
                    if (gestor.getMosaicChunk().getImageBytes().length != 0) {
                        ((ChunkMosaic) gestor.getMosaicChunk()).rotate(1);
                        ((ChunkMosaic) gestor.getMosaicChunk()).draw(graphicContextMosaic);
                        gestor.mosaicChanges(0);
                    }
                } else if (btnFlipH.getState() && event.getSource() == canvasMosaic) {
                    gestor.selectAMosaic((int) event.getX(), (int) event.getY());
                    if (gestor.getMosaicChunk().getImageBytes().length != 0) {
                        ((ChunkMosaic) gestor.getMosaicChunk()).flipHorizontal(1); // Horizontal
                        gestor.getMosaicChunk().draw(graphicContextMosaic);
                        gestor.mosaicChanges(0);
                    }
                } else if (btnFlipV.getState() && event.getSource() == canvasMosaic) {
                    gestor.selectAMosaic((int) event.getX(), (int) event.getY());
                    if (gestor.getMosaicChunk().getImageBytes().length != 0) {
                        ((ChunkMosaic) gestor.getMosaicChunk()).flipVertical(1);
                        gestor.getMosaicChunk().draw(graphicContextMosaic);
                        gestor.mosaicChanges(0);
                    }
                } else if (event.getSource() == canvasMosaic && btnEraser.getState()) {
                    gestor.selectAMosaic((int) event.getX(), (int) event.getY());
                    gestor.delete(graphicContextMosaic, canvasMosaic);
                    gestor.mosaicChanges(0);
                } else if (event.getSource() == canvasMosaic && btnDraw.getState()) {
                    gestor.paintInMosaic((int) event.getX(), (int) event.getY(), graphicContextMosaic);
                    gestor.mosaicChanges(0);
                } else if (event.getSource() == canvasImage) {
                    if (gestor.isSplitted()) {
                        gestor.selectAChunckImage((int) event.getX(), (int) event.getY());
                    }// else-if
                }
            } catch (IOException ex) {
                Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
            }
        } // handle
    }; // canvasClickEvent

    public void dialogSize() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Size Chunk");
        dialog.setHeaderText("Please select a size beetwen 50 and " + gestor.getSmaller());

        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField size = new TextField();
        size.setPromptText("Size");

        grid.add(new Label("Size"), 0, 0);
        grid.add(size, 1, 0);

        Node loginButton = dialog.getDialogPane().lookupButton(confirmButtonType);
        loginButton.setDisable(true);

        size.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(!(newValue.matches("\\d{1,4}")
                    && Integer.parseInt(newValue) >= 50 && Integer.parseInt(newValue) <= gestor.getSmaller()));
        });

        dialog.getDialogPane().setContent(grid);
        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == confirmButtonType) {
            gestor.setSize(Integer.parseInt(size.getText()));
            gestor.split(graphicContextImage, canvasImage);
            gestor.imageChanges(0);
        }
    } // dialogSize: inicia la ventana emergente que le pide al usuario el tamaño de los chunks

    public void dialogWidthHeigth() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Mosaic Dimention Dialog");
        dialog.setHeaderText("Please write a width and a height between " + gestor.getSize() + " and 1680");

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
            if ((newValue.matches("\\d{1,4}") && Integer.parseInt(newValue) >= gestor.getSize() && Integer.parseInt(newValue) <= 1680)) {
                this.aux1 = true;
            } else {
                this.aux1 = false;
            }
            if (this.aux1 && this.aux2) {
                loginButton.setDisable(false);
            } else {
                loginButton.setDisable(true);
            }
        });
        heigth.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("\\d{1,4}") && Integer.parseInt(newValue) >= this.gestor.getSize() && Integer.parseInt(newValue) <= 1680) {
                this.aux2 = true;
            } else {
                this.aux2 = false;
            }
            if (this.aux1 && this.aux2) {
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
            gestor.initMosiacMatrix();
        }
    } // dialogWidthHeigth: inicia la ventana emergente que le pide al usuario las dimenciones del mosaico

    public void alert(Stage primaryStage) {
        ButtonType confirm = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", confirm, cancel);
        alert.setContentText("Do you want to save before continuing?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == confirm) {
            try {
                gestor.saveProject(fileChooserData, primaryStage);
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    } // alert: despliega una alerta para guardar cambios antes de cerrar la aplicación

    public static void main(String[] args) {
        launch(args);
    } // main

} // fin de la clase
