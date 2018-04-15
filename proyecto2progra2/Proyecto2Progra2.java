package proyecto2progra2;

import java.io.File;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Proyecto2Progra2 extends Application {

    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private ScrollPane scrollPaneImage, scrollPaneMosaic;
    private Pane pane;
    private Scene scene;
    private Canvas canvasImage, canvasMosaic;
    private final int rows = 4; //You should decide the values for rows and cols variables
    private final int cols = 4;
    private int tamanoMatrizDestino = rows; // PEDIR AL USUARIO
    private final int chunks = rows * cols;
    private int chunkWidth = 0, chunkHeight = 0;

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
                System.out.println(event.getX() + " " + event.getY());
            } // handle
        });

        Button btnSelectImage = new Button("Select an Image");
        Button btnDelete = new Button("Delete");
        btnDelete.relocate(200, 450);
        btnSelectImage.relocate(50, 450);

        // BORRAR
        Button btDrawLines = new Button("Draw");
        btDrawLines.relocate(400, 450);
        // BORRAR

        GraphicsContext gc = this.canvasImage.getGraphicsContext2D();

        btnSelectImage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File selectedDirectory = fileChooser.showOpenDialog(primaryStage);
                if (selectedDirectory != null) {
                    Image image = new Image(selectedDirectory.toURI().toString());
                    canvasImage.setHeight(image.getHeight() + rows * 10.5);
                    canvasImage.setWidth(image.getWidth() + cols * 10.5);
                    imageChuncks(image, gc);
                } // if
            } // handle
        });
        btnDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gc.clearRect(0, 0, canvasImage.getWidth(), canvasImage.getHeight());
            } // handle
        });

        GraphicsContext gc1 = this.canvasMosaic.getGraphicsContext2D();
        btDrawLines.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                drawGrid(gc1);
            }
        });

        this.pane.getChildren().add(this.scrollPaneImage);
        this.pane.getChildren().add(this.scrollPaneMosaic);
        this.pane.getChildren().add(btnSelectImage);
        this.pane.getChildren().add(btnDelete);

        // BORRAR
        this.pane.getChildren().add(btDrawLines);
        // BORRAR

        primaryStage.setTitle("MosaicMaker");
        primaryStage.setScene(this.scene);
        primaryStage.show();
    } // start

    public void imageChuncks(Image image, GraphicsContext gc) {
        this.chunkWidth = (int) image.getWidth() / this.cols; // determines the chunk width and height
        this.chunkHeight = (int) image.getHeight() / this.rows;
        int count = 0;

        PixelReader px = image.getPixelReader();
        Image im[] = new Image[this.chunks];
        for (int x = 0; x < this.rows; x++) {
            for (int y = 0; y < this.cols; y++) {
                //Initialize the image array with image chunks
                WritableImage prueba = new WritableImage(px, (x * this.chunkWidth), (y * this.chunkHeight), this.chunkWidth, this.chunkHeight);
                Image image1 = (Image) prueba;
                im[count] = image1;

                gc.drawImage(im[count], (this.chunkWidth * x) + (1 + x) * 10, (this.chunkHeight * y) + (1 + y) * 10);
                // draws the image chunk
                count++;
            } // for y
        } // for x
    } // imageChuncks

    public void drawGrid(GraphicsContext gc) {
        this.canvasMosaic.setHeight(this.tamanoMatrizDestino*chunkHeight);
        this.canvasMosaic.setWidth(this.tamanoMatrizDestino*chunkWidth);      
        for (int x = 0; x < this.tamanoMatrizDestino; x++) {
            gc.strokeLine(0, x * this.chunkHeight, this.chunkWidth * this.rows, x * this.chunkHeight); // rows
            gc.strokeLine(x * this.chunkWidth, 0, x * this.chunkWidth, this.chunkHeight * this.cols); // cols
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
