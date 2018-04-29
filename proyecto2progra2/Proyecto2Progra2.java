package proyecto2progra2;

import business.Gestor;
import domain.Button;
import domain.ChunkMosaic;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.imageio.ImageIO;

public class Proyecto2Progra2 extends Application {

    private final int WIDTH = 1360;
    private final int HEIGHT = 720;
    private ScrollPane scrollPaneImage, scrollPaneMosaic;
    private Pane pane;
    private Scene scene;
    private Canvas canvasImage, canvasMosaic, canvasUtilities;
    private GraphicsContext graphicContextImage, graphicContextMosaic, graphicsContextUtilities;
    private domain.Button btnSelectImage, btDrawMosaic, btnSave, btnNewProyect, btnRotate, btnSplit;
    private FileChooser fileChooser1,fileChooser2;
    private Gestor gestor;
    private boolean aux1 = false;
    private boolean aux2 = false;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("MosaicMaker");
        this.gestor = new Gestor();
        initComponents(primaryStage);
        primaryStage.resizableProperty().set(false);
        primaryStage.show();
    } // start

    private void initComponents(Stage primaryStage) {
        this.fileChooser1 = new FileChooser();
        this.fileChooser2 = new FileChooser();
        this.pane = new Pane();
        this.scene = new Scene(this.pane, WIDTH, HEIGHT);

        this.scrollPaneImage = new ScrollPane();
        this.scrollPaneMosaic = new ScrollPane();

        this.btnSelectImage = new Button("/assets/selectAnImage.png", 20, 35, 170, 50);
        this.btDrawMosaic = new Button("/assets/drawMosaic.png", 690, 35, 170, 50);
        this.btnSplit = new Button("/assets/split.png", 200, 35, 170, 50);
        this.btnRotate = new Button("/assets/rotate.png", 1050, 25, 70, 70);
        this.btnSave = new Button("/assets/save.png", 1150, 25, 70, 70);
        this.btnNewProyect = new Button("/assets/delete.png", 1250, 25, 70, 70);

        this.canvasImage = new Canvas(1000, 1000);
        this.canvasMosaic = new Canvas();
        this.canvasUtilities = new Canvas(1380, 160);
        this.canvasUtilities.relocate(0, 600);
        this.graphicsContextUtilities = this.canvasUtilities.getGraphicsContext2D();
        try {
            drawButtons(this.graphicsContextUtilities);
        } catch (IOException ex) {
            Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
        }

//        this.canvasImage.focusTraversableProperty().set(true);
//        this.scrollPaneImage.focusTraversableProperty().set(true);
//        this.scrollPaneImage.setFocusTraversable(true);
        this.scrollPaneImage.setContent(this.canvasImage);
        this.scrollPaneMosaic.setContent(this.canvasMosaic);

        this.scrollPaneImage.setPrefSize(670, 600);
        this.scrollPaneMosaic.setPrefSize(685, 600);

        this.scrollPaneMosaic.relocate(674, 0);

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
                if (e.getSource() == canvasUtilities) {
                    if (btnSelectImage.isClicked((int) e.getX(), (int) e.getY())) {
                        gestor.selectImage(primaryStage, graphicContextImage, fileChooser2, canvasImage);
                    } else if (btnSave.isClicked((int) e.getX(), (int) e.getY())) {
                        gestor.exportMosaic(primaryStage, graphicContextMosaic, canvasMosaic, fileChooser1);
                    } else if (btDrawMosaic.isClicked((int) e.getX(), (int) e.getY())) {
                        if (!gestor.isDefinedValue()) {
                            dialogWidthHeigth();
                        }
                    } else if (btnNewProyect.isClicked((int) e.getX(), (int) e.getY())) {
                        gestor.newProyect(canvasImage, graphicContextImage, graphicContextMosaic, canvasMosaic);
                    } else if (btnRotate.isClicked((int) e.getX(), (int) e.getY())) {
                        gestor.available();
                        if (gestor.getRotAccess()) {
                            btnRotate.setPath("/assets/rotateTrue.png");
                            try {
                                btnRotate.draw(graphicsContextUtilities);
                            } catch (IOException ex) {
                                Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            btnRotate.setPath("/assets/rotate.png");
                            try {
                                btnRotate.draw(graphicsContextUtilities);
                            } catch (IOException ex) {
                                Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } else if (btnSplit.isClicked((int) e.getX(), (int) e.getY())) {
                        if (gestor.getSize() == 0 && !gestor.getImage()) {
                            dialogSize();
                        } else if (gestor.getImage() && gestor.getSize() != 0) {
                            gestor.imageChuncks(graphicContextImage, canvasImage);
                        }

                    } // else-if
                } // if
            } // handle
        });

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                ButtonType confirm = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
                ButtonType cancel = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", confirm, cancel);
                alert.setContentText("Do you wanna charge a proyect?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == confirm) {
                    try {
                        gestor.save(fileChooser1, primaryStage);
                    } catch (IOException ex) {
                        Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        primaryStage.setOnShown(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                ButtonType confirm = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
                ButtonType cancel = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", confirm, cancel);
                alert.setContentText("Do you wanna charge a proyect?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == confirm) {
                    fileChooser1.getExtensionFilters().add(new FileChooser.ExtensionFilter("Dat", "*.dat"));
                    File file = fileChooser1.showOpenDialog(primaryStage);
                    if (file != null) {
                        gestor.reinit(canvasImage, graphicContextImage, graphicContextMosaic, canvasMosaic, file);
                    }

                }

            }
        });

        this.pane.getChildren().add(this.scrollPaneImage);
        this.pane.getChildren().add(this.scrollPaneMosaic);
        this.pane.getChildren().add(this.canvasUtilities);

        primaryStage.setScene(this.scene);
    } // initComponents

    private void drawButtons(GraphicsContext g) throws IOException {
        g.drawImage(new Image("/assets/background.png"), 0, 0, 1380, 120);
        this.btnSelectImage.draw(g);
        this.btDrawMosaic.draw(g);
        this.btnSplit.draw(g);
        this.btnSave.draw(g);
        this.btnNewProyect.draw(g);
        this.btnRotate.draw(g);
    } // drawButtons

    EventHandler<MouseEvent> canvasClickEvent = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (gestor.getRotAccess() && event.getSource() == canvasMosaic) {
                gestor.selectAMosaic((int) event.getX(), (int) event.getY());
                if (gestor.getMosaicChunk().getImageBytes().length != 0) {
                    if (event.getButton() == MouseButton.PRIMARY) {
//                        ((ChunkMosaic) gestor.getMosaicChunk()).rotate(1);
                        ((ChunkMosaic) gestor.getMosaicChunk()).flipHorizontal(1); // Horizontal
                        //                  ((ChunkMosaic) gestor.getMosaicChunk()).flipVertical(1);
                    } else if (event.getButton() == MouseButton.SECONDARY) {
//                        ((ChunkMosaic) gestor.getMosaicChunk()).rotate(0);
                    }
                    try {
                        ((ChunkMosaic) gestor.getMosaicChunk()).draw(graphicContextMosaic);
                    } catch (IOException ex) {
                        Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } // if
            } else if (event.getSource() == canvasImage) {
                if (gestor.getImage()) {
                    gestor.selectAChunckImage((int) event.getX(), (int) event.getY());
                }

            } else if (event.getSource() == canvasMosaic && event.getButton() == MouseButton.PRIMARY) {
                gestor.paintInMosaic((int) event.getX(), (int) event.getY(), graphicContextMosaic);
            } else if (event.getSource() == canvasMosaic && event.getButton() == MouseButton.SECONDARY) {
                gestor.selectAMosaic((int) event.getX(), (int) event.getY());
                graphicContextMosaic.clearRect(0, 0, canvasMosaic.getWidth(), canvasMosaic.getHeight());
                ((ChunkMosaic) gestor.getMosaicChunk()).setImageBytes(new byte[0]);
                gestor.drawGrid(graphicContextMosaic, canvasMosaic);
                gestor.repaintMosaic(graphicContextMosaic);
            } // else-if
        } // handle
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

    public static void main(String[] args) {
        launch(args);
    } // main

} // fin de la clase
