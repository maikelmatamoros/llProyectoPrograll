/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto2progra2;

import java.io.File;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author maikel
 */
public class Proyecto2Progra2 extends Application {

    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private ScrollPane scrollPane;
    private Pane pane;
    private Scene scene;
    private Canvas canvas;
    private final int rows = 30; //You should decide the values for rows and cols variables
    private final int cols = 30;
    private final int chunks = rows * cols;

    @Override
    public void start(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        this.pane = new Pane();
        this.scene = new Scene(this.pane, WIDTH, HEIGHT);
        this.scrollPane=new ScrollPane();
        this.canvas = new Canvas();
        this.scrollPane.setContent(this.canvas);
        this.scrollPane.setPrefSize(400, 400);
        scrollPane.setPrefSize(300, 300);
         // Always show vertical scroll bar
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        
        // Horizontal scroll bar is only displayed when needed
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        
        this.canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(event.getX()+" "+ event.getY());
            }
        });
        
        Button btn = new Button();
        Button btn1 = new Button("Borrar");
        
        btn1.relocate(200, 400);
        btn.relocate(400, 400);
        
        GraphicsContext gc = this.canvas.getGraphicsContext2D();
        
        btn.setText("Select an Image");

        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                
                File selectedDirectory = fileChooser.showOpenDialog(primaryStage);
                if (selectedDirectory != null) {
                    Image image = new Image(selectedDirectory.toURI().toString());
                    canvas.setHeight(image.getHeight()+rows*10.5);
                    canvas.setWidth(image.getWidth()+cols*10.5);
                    imageChuncks(image, gc);
                }

            }
        });
        btn1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            }
        });

        this.pane.getChildren().add(this.scrollPane);
        this.pane.getChildren().add(btn);
        this.pane.getChildren().add(btn1);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void imageChuncks(Image image, GraphicsContext gc) {
        int chunkWidth = (int) image.getWidth() / cols; // determines the chunk width and height
        int chunkHeight = (int) image.getHeight() / rows;
        int count = 0;

        PixelReader px = image.getPixelReader();
        Image im[] = new Image[chunks];
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                //Initialize the image array with image chunks
                
                WritableImage prueba = new WritableImage(px, (x * chunkWidth), (y * chunkHeight), chunkWidth, chunkHeight);
                Image image1 = (Image) prueba;
                im[count] = image1;
                
                gc.drawImage(im[count], (chunkWidth * x) + (1 + x) * 10, (chunkHeight * y) + (1 + y) * 10);
                // draws the image chunk
                count++;
            }
        }
        
    }
    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
