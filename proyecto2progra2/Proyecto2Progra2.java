package proyecto2progra2;

import domain.Chunk;
import domain.MosaicChunk;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class Proyecto2Progra2 extends Application {

    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private ScrollPane scrollPaneImage, scrollPaneMosaic;
    private Pane pane;
    private Scene scene;
    private Canvas canvasImage, canvasMosaic;
    private final int rows = 3; //You should decide the values for rows and cols variables
    private final int cols = 3;
    private int tamanoMatrizDestino = rows; // PEDIR AL USUARIO
    private final int chunks = rows * cols;
    private int i,j;
    private int chunkWidth = 0, chunkHeight = 0;
    private Chunk chunksVec[][];
    private MosaicChunk mosaicVec[][];
    @Override
    public void start(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        this.pane = new Pane();

        this.scene = new Scene(this.pane, WIDTH, HEIGHT);

        this.scrollPaneImage = new ScrollPane();
        this.scrollPaneMosaic = new ScrollPane();

        this.canvasImage = new Canvas();
        this.canvasMosaic = new Canvas(450, 400);

        this.scrollPaneImage.setContent(this.canvasImage);
        this.scrollPaneMosaic.setContent(this.canvasMosaic);

        this.scrollPaneImage.setPrefSize(300, 400);
        this.scrollPaneMosaic.setPrefSize(450, 400);

        this.scrollPaneMosaic.relocate(350, 0);

        this.scrollPaneImage.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        this.scrollPaneImage.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        this.scrollPaneMosaic.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        this.scrollPaneMosaic.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        this.canvasImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (chunksVec[0][0] == null) {
                    System.out.println("Ingrese una imagen primero");
                } else {
                    
                    for (int x = 0; x < rows; x++) {
                        for (int y = 0; y < cols; y++) {
                            if(chunksVec[x][y].mousePresionado((int) event.getX(), (int) event.getY())){
                                i=x;
                                j=y;
                                break;
                            }
                        }
                    }
                }
                //System.out.println(event.getX() + " " + event.getY());

            } // handle
        }
        );
        GraphicsContext gcM=this.canvasMosaic.getGraphicsContext2D();
        this.canvasMosaic.setOnMouseClicked(new EventHandler<MouseEvent>() {
            int k;
            int l;
            @Override
            public void handle(MouseEvent event) {
                for (int x=0; x < rows; x++) {
                        for (int y = 0; y < cols; y++) {
                            if(mosaicVec[x][y].mousePresionado((int) event.getX(), (int) event.getY())){
                                k=x;
                                l=y;
                                break;
                            }
                        }
                    }
                mosaicVec[k][l].setImage(chunksVec[i][j].getImage());
                mosaicVec[k][l].draw(gcM);;
            }
        });

        Button btnSelectImage = new Button("Select an Image");
        Button btnDelete = new Button("Delete");
        Button save=new Button("Save");
        
        save.relocate(300, 450);
        btnDelete.relocate(
                200, 450);
        btnSelectImage.relocate(
                50, 450);

        // BORRAR
        Button btDrawLines = new Button("Draw");

        btDrawLines.relocate(
                400, 450);
        // BORRAR

        GraphicsContext gc = this.canvasImage.getGraphicsContext2D();

        btnSelectImage.setOnAction(
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event
            ) {
                File selectedDirectory = fileChooser.showOpenDialog(primaryStage);
                if (selectedDirectory != null) {
                    Image image = new Image(selectedDirectory.toURI().toString());
                    canvasImage.setHeight(image.getHeight() + rows * 10.5);
                    canvasImage.setWidth(image.getWidth() + cols * 10.5);
                    imageChuncks(image, gc);
                } // if
            } // handle
        }
        );
        btnDelete.setOnAction(
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event
            ) {
                gc.clearRect(0, 0, canvasImage.getWidth(), canvasImage.getHeight());
            } // handle
        }
        );

        GraphicsContext gc1 = this.canvasMosaic.getGraphicsContext2D();

        btDrawLines.setOnAction(
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event
            ) {
                drawGrid(gc1);
            }
        }
        );

        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                WritableImage wim=new WritableImage((int)Math.round(canvasMosaic.getWidth()), (int)Math.round(canvasMosaic.getHeight()));
                SnapshotParameters snapshotParameters=new SnapshotParameters();
                snapshotParameters.setTransform(Transform.scale(8, 8));
                canvasMosaic.snapshot(null, wim);
                File file=new File("Canvas Screenshot");
                try {
                    
                    ImageIO.write(SwingFXUtils.fromFXImage(wim, null),"png",file);
                } catch (IOException ex) {
                    Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        this.pane.getChildren()
                .add(this.scrollPaneImage);

        this.pane.getChildren()
                .add(this.scrollPaneMosaic);

        this.pane.getChildren()
                .add(btnSelectImage);

        this.pane.getChildren()
                .add(btnDelete);

        // BORRAR
        this.pane.getChildren()
                .add(btDrawLines);
        // BORRAR
        this.pane.getChildren().add(save);

        primaryStage.setTitle(
                "MosaicMaker");
        primaryStage.setScene(
                this.scene);
        primaryStage.show();
    } // start

    public void imageChuncks(Image image, GraphicsContext gc) {
        this.chunkWidth = (int) image.getWidth() / this.cols; // determines the chunk width and height
        this.chunkHeight = (int) image.getHeight() / this.rows;

        PixelReader px = image.getPixelReader();
        chunksVec = new Chunk[rows][cols];
        for (int x = 0; x < this.rows; x++) {
            for (int y = 0; y < this.cols; y++) {
                //Initialize the image array with image chunks
                WritableImage writableImage = new WritableImage(px, (x * this.chunkWidth), (y * this.chunkHeight), this.chunkWidth, this.chunkHeight);
                Image aux = (Image) writableImage;
                chunksVec[x][y] = new Chunk(aux, x, y, chunkWidth, chunkHeight);
                chunksVec[x][y].draw(gc);
                // draws the image chunk

            } // for y
        } // for x
    } // imageChuncks

    public void initMosiacChunks() {
        this.mosaicVec=new MosaicChunk[rows][cols];
        for (int x = 0; x < this.rows; x++) {
            for (int y = 0; y < this.cols; y++) {
                this.mosaicVec[x][y]=new MosaicChunk(x, y, size);
            }
        }
    }
    private int size=100;
    public void drawGrid(GraphicsContext gc) {
        initMosiacChunks();
        this.canvasMosaic.setHeight(this.rows * size);
        this.canvasMosaic.setWidth(this.cols * size);

        for (int x = 0; x <= this.rows; x++) {

            gc.strokeLine(0, x * size, cols * size, x * size); // rows
        }
        for (int y = 0; y <= this.cols; y++) {

            gc.strokeLine(y * size, 0, y * size, size * this.rows); // cols
        }
    } // drawGrid

    public static void main(String[] args) {
        launch(args);
    } // main

} // fin de la clase

/* Revisar: al cargar una imagen relativamente pequeña no pinta bien la 
cuadrícula, falla el calculo
Hacer el repaint o bloquear el dibujo de la cuadrícula despues de haber elegido el tamaño
VIDEO: https://www.youtube.com/watch?v=hIRuMY4_9zs
 */
