package proyecto2progra2;

import business.Gestor;

import domain.ChunkMosaic;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
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
    private Canvas canvasImage, canvasMosaic;
    private GraphicsContext graphicContextImage, graphicContextMosaic;
    private Button btnSelectImage, btDrawLines, btnSave, btnNewProyect, btnRotate, btnSplit;
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

        this.canvasImage = new Canvas();
        this.canvasMosaic = new Canvas();

        this.scrollPaneImage.setContent(this.canvasImage);
        this.scrollPaneMosaic.setContent(this.canvasMosaic);

        this.scrollPaneImage.setPrefSize(670, 400);
        this.scrollPaneMosaic.setPrefSize(685, 400);

        this.scrollPaneMosaic.relocate(680, 0);

        this.scrollPaneImage.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        this.scrollPaneImage.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        this.scrollPaneMosaic.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        this.scrollPaneMosaic.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        this.canvasImage.setOnMouseClicked(canvasClickEvent);
        this.canvasMosaic.setOnMouseClicked(canvasClickEvent);

        this.graphicContextMosaic = this.canvasMosaic.getGraphicsContext2D();
        this.graphicContextImage = this.canvasImage.getGraphicsContext2D();

        btnSelectImage = new Button("Select an Image");
        btnSave = new Button("Save");
        btDrawLines = new Button("Draw Mosaic");
        btnNewProyect = new Button("New Proyect");
        btnRotate = new Button("Rotate");
        btnSplit = new Button("Split");

        btnSave.relocate(300, 450);
        btnSelectImage.relocate(50, 450);
        btDrawLines.relocate(400, 450);
        btnNewProyect.relocate(600, 600);
        btnRotate.relocate(500, 500);
        btnSplit.relocate(130, 450);

        btnSelectImage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gestor.selectImage(primaryStage, graphicContextImage, fileChooser, canvasImage);
            }
        });
        btDrawLines.setOnAction(buttonsEvents);
        btnNewProyect.setOnAction(buttonsEvents);
        btnRotate.setOnAction(buttonsEvents);
        btnSplit.setOnAction(buttonsEvents);
        btnSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gestor.exportMosaic(primaryStage, graphicContextMosaic, canvasMosaic, fileChooser);
            }
        });

        this.pane.getChildren().add(this.scrollPaneImage);
        this.pane.getChildren().add(this.scrollPaneMosaic);

        this.pane.getChildren().add(btnSelectImage);
        this.pane.getChildren().add(btDrawLines);
        this.pane.getChildren().add(btnSave);
        this.pane.getChildren().add(btnNewProyect);
        this.pane.getChildren().add(btnRotate);
        this.pane.getChildren().add(btnSplit);

        this.pane.getChildren().add(this.tfImageChunkSize);
        this.pane.getChildren().add(this.tfMosaicCanvasHeight);
        this.pane.getChildren().add(this.tfMosaicCanvasWidth);

        this.scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.C) {
                    gestor.available(false);
                }
            }
        });
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                try {
                    gestor.save();
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

        primaryStage.setScene(this.scene);

    }

    EventHandler<MouseEvent> canvasClickEvent = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (gestor.getAcces()) {
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
                }
            } else if (event.getSource() == canvasImage) {
                gestor.selectAChunckImage((int) event.getX(), (int) event.getY());
            } else if (event.getSource() == canvasMosaic && event.getButton() == MouseButton.PRIMARY) {
                gestor.paintInMosaic((int) event.getX(), (int) event.getY(),graphicContextMosaic);
            } else if (event.getSource() == canvasMosaic && event.getButton() == MouseButton.SECONDARY) {
                gestor.selectAMosaic((int) event.getX(), (int) event.getY());
                graphicContextMosaic.clearRect(0, 0, canvasMosaic.getWidth(),canvasMosaic.getHeight());
                ((ChunkMosaic) gestor.getMosaicChunk()).setImageBytes(new byte[0]);
                gestor.drawGrid(graphicContextMosaic,canvasMosaic);
                gestor.repaintMosaic(graphicContextMosaic);
            }
        }
    };

    EventHandler<ActionEvent> buttonsEvents = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (event.getSource() == btDrawLines) {
                gestor.setMosaicsParameters(Integer.parseInt(tfMosaicCanvasHeight.getText()),
                        Integer.parseInt(tfMosaicCanvasWidth.getText()));
                gestor.drawGrid(graphicContextMosaic,canvasMosaic);
                gestor.initMosiacChunks();
            } else if (event.getSource() == btnNewProyect) {
                gestor.newProyect();
            } else if (event.getSource() == btnRotate) {
                gestor.available(true);
            } else if (event.getSource() == btnSplit) {
                if (tfImageChunkSize.getText().equals("")) {
                    System.out.println("Select a size before make a split");
                } else {
                    gestor.setSize( Integer.parseInt(tfImageChunkSize.getText()));
                    gestor.imageChuncks(graphicContextImage, canvasImage);
                }

            }
        }
    };



public static void main(String[] args) {
        launch(args);
    } // main

} // fin de la clase
