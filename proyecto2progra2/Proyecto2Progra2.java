package proyecto2progra2;

import business.SaveBusiness;
import domain.Chunk;
import file.SaveFile;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.imageio.ImageIO;

public class Proyecto2Progra2 extends Application {

    private final int WIDTH = 1380;
    private final int HEIGHT = 720;
    private ScrollPane scrollPaneImage, scrollPaneMosaic;
    private Pane pane;
    private Scene scene;
    private Canvas canvasImage, canvasMosaic, canvas;
    private int rowsImage, rowsMosaic; //You should decide the values for rows and cols variables
    private int colsImage, colsMosaic;
    private int i, j;
    private Chunk chunksVec[][];
    private Chunk mosaicVec[][];
    private int size;
    private GraphicsContext graphicContextImage, graphicContextMosaic;
    private int k, l;
    private Button btnSelectImage, btDrawLines, btnSave, btnNewProyect, btnRotate;
    private FileChooser fileChooser;
    private TextField tfImageChunkSize, tfMosaicCanvasHeight, tfMosaicCanvasWidth;
    private boolean rotAccess;
    private SaveBusiness saveBusiness;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("MosaicMaker");
        initComponents(primaryStage);
        primaryStage.show();
    } // start

    private void initComponents(Stage primaryStage) {
        this.saveBusiness = new SaveBusiness();
        this.fileChooser = new FileChooser();
        this.pane = new Pane();
        rotAccess = false;
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

        btnSave.relocate(300, 450);
        btnSelectImage.relocate(50, 450);
        btDrawLines.relocate(400, 450);
        btnNewProyect.relocate(600, 600);
        btnRotate.relocate(500, 500);

        btnSelectImage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectAndSplitImage(primaryStage, graphicContextImage);
            }
        });
        btDrawLines.setOnAction(buttonsEvents);
        btnNewProyect.setOnAction(buttonsEvents);
        btnRotate.setOnAction(buttonsEvents);
        btnSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                saveMosaic(primaryStage);
            }
        });

        this.pane.getChildren().add(this.scrollPaneImage);
        this.pane.getChildren().add(this.scrollPaneMosaic);

        this.pane.getChildren().add(btnSelectImage);
        this.pane.getChildren().add(btDrawLines);
        this.pane.getChildren().add(btnSave);
        this.pane.getChildren().add(btnNewProyect);
        this.pane.getChildren().add(btnRotate);

        this.pane.getChildren().add(this.tfImageChunkSize);
        this.pane.getChildren().add(this.tfMosaicCanvasHeight);
        this.pane.getChildren().add(this.tfMosaicCanvasWidth);

        this.scene.setOnKeyPressed(keyEvent);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                try {
                    saveBusiness.save(chunksVec, mosaicVec);
                } catch (IOException | ClassNotFoundException ex) {
                    Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        primaryStage.setOnShown(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                reinit();
            }
        });

        primaryStage.setScene(this.scene);

    }

    EventHandler<MouseEvent> canvasClickEvent = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (rotAccess) {
                selectAMosaic((int) event.getX(), (int) event.getY());
                if (mosaicVec[k][l].getImageBytes().length != 0) {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        mosaicVec[k][l].rotate(0);
                    } else if (event.getButton() == MouseButton.SECONDARY) {
                        mosaicVec[k][l].rotate(1);
                    }
                    try {
                        mosaicVec[k][l].draw(graphicContextMosaic, 1);
                    } catch (IOException ex) {
                        Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            } else if (event.getSource() == canvasImage) {
                selectAChunck((int) event.getX(), (int) event.getY());
            } else if (event.getSource() == canvasMosaic && event.getButton() == MouseButton.PRIMARY) {
                paintInMosaic((int) event.getX(), (int) event.getY());
            } else if (event.getSource() == canvasMosaic && event.getButton() == MouseButton.SECONDARY) {
                selectAMosaic((int) event.getX(), (int) event.getY());
                graphicContextMosaic.clearRect(l * size, k * size, size, size);
                mosaicVec[k][l].setImageBytes(new byte[0]);
                drawGrid(graphicContextMosaic);
                repaintMosaic();
            }
        }
    };

    EventHandler<KeyEvent> keyEvent = (event) -> {
        if (event.getCode() == KeyCode.C) {
            this.rotAccess = false;
        }
    };

    private void reinit() {
        try {
            if (new File("save.dat").exists()) {
                List<Chunk[][]> list = this.saveBusiness.recover();
                if (list.get(0) != null) {
                    tfImageChunkSize.setEditable(false);
                    chunksVec = list.get(0);
                    size = chunksVec[0][0].getSize();
                    rowsImage = chunksVec.length;
                    colsImage = chunksVec[0].length;
                    canvasImage.setHeight((rowsImage) * size + ((rowsImage + 1) * 10));
                    canvasImage.setWidth((colsImage) * size + ((colsImage + 1) * 10));
                    for (int x = 0; x < rowsImage; x++) {
                        for (int y = 0; y < colsImage; y++) {
                            chunksVec[x][y].draw(graphicContextImage, 0);
                        }
                    }
                }
                if (list.get(1) != null) {
                    tfMosaicCanvasHeight.setEditable(false);
                    tfMosaicCanvasWidth.setEditable(false);
                    mosaicVec = list.get(1);
                    rowsMosaic = mosaicVec.length;
                    colsMosaic = mosaicVec[0].length;
                    canvasMosaic.setHeight(rowsMosaic * size);
                    canvasMosaic.setWidth(colsMosaic * size);

                    drawGrid(graphicContextMosaic);

                    repaintMosaic();
                }

            }
        } catch (IOException ex) {
            Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void selectAChunck(int xP, int yP) {
        if (chunksVec[0][0] == null) {
            System.out.println("Ingrese una imagen primero");
        } else {

            for (int x = 0; x < rowsImage; x++) {
                for (int y = 0; y < colsImage; y++) {
                    if (chunksVec[x][y].chunckImageClicked(xP, yP)) {
                        i = x;
                        j = y;
                        break;
                    }
                }
            }
        }
    }

    private void selectAMosaic(int xP, int yP) {
        for (int x = 0; x < rowsMosaic; x++) {
            for (int y = 0; y < colsMosaic; y++) {
                if (mosaicVec[x][y].chunckMosaicClicked(xP, yP)) {
                    System.out.println(y + " " + x);
                    k = x;
                    l = y;
                    break;
                }
            }
        }
    }

    private void paintInMosaic(int xP, int yP) {

        selectAMosaic(xP, yP);
        mosaicVec[k][l].setImageBytes(chunksVec[i][j].getImageBytes());
        try {
            mosaicVec[k][l].draw(graphicContextMosaic, 1);
        } catch (IOException ex) {
            Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void newProyect() {
        this.mosaicVec = null;
        this.chunksVec = null;
        this.tfImageChunkSize.setEditable(true);
        this.tfMosaicCanvasHeight.setEditable(true);
        this.tfMosaicCanvasWidth.setEditable(true);
        this.graphicContextImage.clearRect(0, 0, this.canvasImage.getWidth(), this.canvasImage.getHeight());
        this.graphicContextMosaic.clearRect(0, 0, this.canvasMosaic.getWidth(), this.canvasMosaic.getHeight());
        SaveFile save = new SaveFile();
        save.newProyect();

    }

    EventHandler<ActionEvent> buttonsEvents = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (event.getSource() == btDrawLines) {
                rowsMosaic = Integer.parseInt(tfMosaicCanvasHeight.getText()) / size;
                colsMosaic = Integer.parseInt(tfMosaicCanvasWidth.getText()) / size;
                drawGrid(graphicContextMosaic);
                initMosiacChunks();
            } else if (event.getSource() == btnNewProyect) {
                newProyect();
            } else if (event.getSource() == btnRotate) {
                rotAccess = true;
            }
        }
    };

    private void selectAndSplitImage(Stage primaryStage, GraphicsContext gc) {
        if (this.tfImageChunkSize.isEditable()) {
            this.size = Integer.parseInt(this.tfImageChunkSize.getText());
        }
        File selectedDirectory = fileChooser.showOpenDialog(primaryStage);
        if (selectedDirectory != null) {
            try {
                BufferedImage image = ImageIO.read(selectedDirectory);
                imageChuncks(image, gc);
            } // if
            catch (IOException ex) {
                Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void repaintMosaic() {
        for (int x = 0; x < rowsMosaic; x++) {
            for (int y = 0; y < colsMosaic; y++) {
                try {
                    if (mosaicVec[x][y].getImageBytes().length != 0) {
                        mosaicVec[x][y].draw(graphicContextMosaic, 1);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void saveMosaic(Stage primaryStage) {
        graphicContextMosaic.clearRect(0, 0, colsMosaic * size, rowsMosaic * size);
        repaintMosaic();
        WritableImage wim = new WritableImage((int) Math.round(canvasMosaic.getWidth()), (int) Math.round(canvasMosaic.getHeight()));
        SnapshotParameters snapshotParameters = new SnapshotParameters();
        snapshotParameters.setFill(Color.TRANSPARENT);
        canvasMosaic.snapshot(snapshotParameters, wim);
        drawGrid(graphicContextMosaic);
        repaintMosaic();
        File file = this.fileChooser.showSaveDialog(primaryStage);
        try {

            ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
        } catch (IOException ex) {
            Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
        }
        //graphicContextMosaic.clearRect(0, 0, colsMosaic * size, rowsMosaic * size);

    }

    public void imageChuncks(BufferedImage image, GraphicsContext gc) {

        gc.clearRect(0, 0, canvasImage.getWidth(), canvasImage.getHeight());
        this.rowsImage = (int) (image.getHeight() / size);
        this.colsImage = (int) (image.getWidth() / size); // determines the chunk width and height
        System.out.println(colsImage);
        this.canvasImage.setHeight((rowsImage) * size + ((rowsImage + 1) * 10));
        this.canvasImage.setWidth((colsImage) * size + ((colsImage + 1) * 10));
        chunksVec = new Chunk[rowsImage][colsImage];
        for (int x = 0; x < this.rowsImage; x++) {
            for (int y = 0; y < this.colsImage; y++) {
                try {
                    //Initialize the image array with image chunks

                    BufferedImage aux = image.getSubimage((y * this.size), (x * this.size), this.size, this.size);
                    chunksVec[x][y] = new Chunk(imageToBytes(aux), y, x, size);
                    chunksVec[x][y].draw(gc, 0);
                    // draws the image chunk
                } // for y
                catch (IOException ex) {
                    Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        } // for x
    } // imageChuncks

    public void initMosiacChunks() {
        this.mosaicVec = new Chunk[rowsMosaic][colsMosaic];
        for (int x = 0; x < this.rowsMosaic; x++) {
            for (int y = 0; y < this.colsMosaic; y++) {
                this.mosaicVec[x][y] = new Chunk(new byte[0], y, x, size);
            }
        }
    }

    public void drawGrid(GraphicsContext gc) {
        //initMosiacChunks();
        this.canvasMosaic.setHeight(this.rowsMosaic * size);
        this.canvasMosaic.setWidth(this.colsMosaic * size);

        for (int x = 0; x <= this.rowsMosaic; x++) {

            gc.strokeLine(0, x * size, colsMosaic * size, x * size); // rows
        }
        for (int y = 0; y <= this.colsMosaic; y++) {

            gc.strokeLine(y * size, 0, y * size, size * this.rowsMosaic); // cols
        }
    } // drawGrid

    public byte[] imageToBytes(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }

    public static void main(String[] args) {
        launch(args);
    } // main

} // fin de la clase
