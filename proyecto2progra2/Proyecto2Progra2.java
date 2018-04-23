package proyecto2progra2;

import business.Gestor;
import domain.Button;
import domain.ChunkMosaic;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Proyecto2Progra2 extends Application {

    private final int WIDTH = 1380;
    private final int HEIGHT = 720;
    private ScrollPane scrollPaneImage, scrollPaneMosaic;
    private Pane pane;
    private Scene scene;
    private Canvas canvasImage, canvasMosaic, canvasUtilities;
    private GraphicsContext graphicContextImage, graphicContextMosaic, graphicsContextUtilities;
    private domain.Button btnSelectImage, btDrawMosaic, btnSave, btnNewProyect, btnRotate, btnSplit;
    private FileChooser fileChooser;
    private TextField tfImageChunkSize, tfMosaicCanvasHeight, tfMosaicCanvasWidth;
    private Gestor gestor;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("MosaicMaker");
        this.gestor = new Gestor();
        initComponents(primaryStage);
        primaryStage.show();
    } // start

    private void initComponents(Stage primaryStage) {
        this.fileChooser = new FileChooser();
        this.pane = new Pane();
        this.tfImageChunkSize = new TextField();
        this.tfImageChunkSize.relocate(30, 550);
        this.tfImageChunkSize.resize(100, 30);

        this.tfMosaicCanvasHeight = new TextField();
        this.tfMosaicCanvasHeight.relocate(450, 550);
        this.tfMosaicCanvasHeight.resize(100, 30);
        this.tfMosaicCanvasWidth = new TextField();
        this.tfMosaicCanvasWidth.relocate(600, 550);
        this.tfMosaicCanvasWidth.resize(100, 30);

        this.scene = new Scene(this.pane, WIDTH, HEIGHT);

        this.scrollPaneImage = new ScrollPane();
        this.scrollPaneMosaic = new ScrollPane();

        this.btnSelectImage = new Button("/assets/selectAnImage.png", 20, 10, 180, 120);
        this.btDrawMosaic = new Button("/assets/drawMosaic.png", 690, 10, 180, 120);
        this.btnSplit = new Button("/assets/split.png", 200, 10, 180, 120);
        this.btnRotate = new Button("/assets/rotate.png", 1050, 35, 70, 70);
        this.btnSave = new Button("/assets/save.png", 1150, 35, 70, 70);
        this.btnNewProyect = new Button("/assets/delete.png", 1250, 35, 70, 70);

        this.canvasImage = new Canvas();
        this.canvasMosaic = new Canvas();
        this.canvasUtilities = new Canvas(1380, 320);
        this.canvasUtilities.relocate(0, 400);
        this.graphicsContextUtilities = this.canvasUtilities.getGraphicsContext2D();
        try {
            drawButtons(this.graphicsContextUtilities);
        } catch (IOException ex) {
            Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.scrollPaneImage.setContent(this.canvasImage);
        this.scrollPaneMosaic.setContent(this.canvasMosaic);

        this.scrollPaneImage.setPrefSize(670, 400);
        this.scrollPaneMosaic.setPrefSize(685, 400);

        this.scrollPaneMosaic.relocate(680, 0);

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
                        gestor.selectImage(primaryStage, graphicContextImage, fileChooser, canvasImage);
                    } else if (btnSave.isClicked((int) e.getX(), (int) e.getY())) {
                        gestor.exportMosaic(primaryStage, graphicContextMosaic, canvasMosaic, fileChooser);
                    } else if (btDrawMosaic.isClicked((int) e.getX(), (int) e.getY())) {
                        gestor.setMosaicsParameters(Integer.parseInt(tfMosaicCanvasHeight.getText()),
                                Integer.parseInt(tfMosaicCanvasWidth.getText()));
                        gestor.drawGrid(graphicContextMosaic, canvasMosaic);
                        gestor.initMosiacChunks();
                    } else if (btnNewProyect.isClicked((int) e.getX(), (int) e.getY())) {
                        gestor.newProyect(tfImageChunkSize, tfMosaicCanvasWidth, tfMosaicCanvasHeight, canvasImage, graphicContextImage, graphicContextMosaic, canvasMosaic);
                    } else if (btnRotate.isClicked((int) e.getX(), (int) e.getY())) {
                        gestor.available();
                        if(gestor.isRotateAvaible()){
                            btnRotate.setPath("/assets/rotateTrue.png");
                            try {
                                btnRotate.draw(graphicsContextUtilities);
                            } catch (IOException ex) {
                                Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }else{
                            btnRotate.setPath("/assets/rotate.png");
                            try {
                                btnRotate.draw(graphicsContextUtilities);
                            } catch (IOException ex) {
                                Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } else if (btnSplit.isClicked((int) e.getX(), (int) e.getY())) {
                        if (tfImageChunkSize.getText().equals("")) {
                            System.out.println("Select a size before make a split");
                        } else {
                            gestor.setSize(Integer.parseInt(tfImageChunkSize.getText()));
                            gestor.imageChuncks(graphicContextImage, canvasImage);
                        } // else
                    } // else-if
                } // if
            } // handle
        });

        this.scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.C) {
//                    gestor.available(false);
                }
            }
        });

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                try {
                    ButtonType confirm = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
                    ButtonType cancel = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", confirm, cancel);
                    alert.setTitle("Confirm");
                    alert.setContentText("Do you wanna save?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == confirm) {
                        gestor.save();
                    }

                } catch (IOException | ClassNotFoundException ex) {
                    Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        primaryStage.setOnShown(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                gestor.reinit(tfImageChunkSize, tfMosaicCanvasWidth, tfMosaicCanvasHeight, canvasImage, graphicContextImage, graphicContextMosaic, canvasMosaic);
            }
        });
    
        this.pane.getChildren().add(this.scrollPaneImage);
        this.pane.getChildren().add(this.scrollPaneMosaic);
        this.pane.getChildren().add(this.canvasUtilities);
        this.pane.getChildren().add(this.tfImageChunkSize);
        this.pane.getChildren().add(this.tfMosaicCanvasHeight);
        this.pane.getChildren().add(this.tfMosaicCanvasWidth);
        primaryStage.setScene(this.scene);
    } // initComponents

    private void drawButtons(GraphicsContext g) throws IOException {
        g.drawImage(new Image("/assets/background.png"), 0, 0, 1380, 320);
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
            if (gestor.getAcces() && event.getSource() == canvasMosaic) {
                gestor.selectAMosaic((int) event.getX(), (int) event.getY());
                if (gestor.getMosaicChunk().getImageBytes().length != 0) {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        ((ChunkMosaic) gestor.getMosaicChunk()).rotate(1);
                    } else if (event.getButton() == MouseButton.SECONDARY) {
                        ((ChunkMosaic) gestor.getMosaicChunk()).rotate(0);
                    }
                    try {
                        ((ChunkMosaic) gestor.getMosaicChunk()).draw(graphicContextMosaic);
                    } catch (IOException ex) {
                        Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } // if
            } else if (event.getSource() == canvasImage) {
                gestor.selectAChunckImage((int) event.getX(), (int) event.getY());
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

    public static void main(String[] args) {
        launch(args);
    } // main

} // fin de la clase
