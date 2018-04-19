package proyecto2progra2;

import domain.Chunk;
import file.SaveFile;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
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
import javafx.scene.image.WritableImage;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Transform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.imageio.ImageIO;

public class Proyecto2Progra2 extends Application {

    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private ScrollPane scrollPaneImage, scrollPaneMosaic;
    private Pane pane;
    private Scene scene;
    private Canvas canvasImage, canvasMosaic;
    private int rows; //You should decide the values for rows and cols variables
    private int cols;
    private int i, j;
    private Chunk chunksVec[][];
    private Chunk mosaicVec[][];

    @Override
    public void start(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        this.pane = new Pane();
        size = 100;
        this.scene = new Scene(this.pane, WIDTH, HEIGHT);

        this.scrollPaneImage = new ScrollPane();
        this.scrollPaneMosaic = new ScrollPane();

        this.canvasImage = new Canvas();
        this.canvasMosaic = new Canvas();

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
                            if (chunksVec[x][y].chunckImageClicked((int) event.getX(), (int) event.getY())) {
                                i = x;
                                j = y;
                                break;
                            }
                        }
                    }
                }
                //System.out.println(event.getX() + " " + event.getY());

            } // handle
        }
        );
        GraphicsContext gcM = this.canvasMosaic.getGraphicsContext2D();
        this.canvasMosaic.setOnMouseClicked(new EventHandler<MouseEvent>() {
            int k;
            int l;

            @Override
            public void handle(MouseEvent event) {
                for (int x = 0; x < rows; x++) {
                    for (int y = 0; y < cols; y++) {
                        if (mosaicVec[x][y].chunckMosaicClicked((int) event.getX(), (int) event.getY())) {
                            k = x;
                            l = y;
                            break;
                        }
                    }
                }
                mosaicVec[k][l].setImageBytes(chunksVec[i][j].getImageBytes());
                try {
                    mosaicVec[k][l].draw(gcM, 1);
                } catch (IOException ex) {
                    Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        Button btnSelectImage = new Button("Select an Image");
        Button btnDelete = new Button("Delete");
        Button save = new Button("Save");

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
        pane.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                System.out.println("Dejó");
            }
        });
        GraphicsContext gc = this.canvasImage.getGraphicsContext2D();

        btnSelectImage.setOnAction(
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event
            ) {
                File selectedDirectory = fileChooser.showOpenDialog(primaryStage);
                if (selectedDirectory != null) {
                    try {
                        System.out.println(selectedDirectory.getPath());
                        BufferedImage image = ImageIO.read(selectedDirectory);
                        canvasImage.setHeight(image.getHeight() + rows * 10.5);
                        canvasImage.setWidth(image.getWidth() + cols * 10.5);
                        imageChuncks(image, gc);
                    } // if
                    catch (IOException ex) {
                        Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
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

        btDrawLines.setOnAction(
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event
            ) {
                drawGrid(gcM);
            }
        }
        );

        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                WritableImage wim = new WritableImage((int) Math.round(canvasMosaic.getWidth()), (int) Math.round(canvasMosaic.getHeight()));
                SnapshotParameters snapshotParameters = new SnapshotParameters();
                snapshotParameters.setTransform(Transform.scale(8, 8));
                canvasMosaic.snapshot(null, wim);
                File file = new File("Canvas Screenshot");
                try {

                    ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
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

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                SaveFile save = new SaveFile();
                try {
                    save.save(chunksVec, mosaicVec);
                } catch (IOException ex) {
                    Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        primaryStage.setOnShown(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {

                try {
                    SaveFile save = new SaveFile();

                    chunksVec = save.recover().get(0);
                    rows = chunksVec.length;
                    cols = chunksVec[0].length;
                    canvasImage.setHeight((rows) * size + ((rows + 1) * 10));
                    canvasImage.setWidth((cols) * size + ((cols + 1) * 10));
                    canvasMosaic.setHeight(rows * size);
                    canvasMosaic.setWidth(cols * size);

                    mosaicVec = save.recover().get(1);
                    System.err.println(mosaicVec.length);
                    drawGrid(gcM);
                    for (int x = 0; x < rows; x++) {
                        for (int y = 0; y < cols; y++) {
                            chunksVec[x][y].draw(gc, 0);
                            if(mosaicVec[x][y].getImageBytes()!=null){
                                mosaicVec[x][y].draw(gcM, 1);
                            }
                            

                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        primaryStage.setTitle(
                "MosaicMaker");
        primaryStage.setScene(
                this.scene);
        primaryStage.show();
    } // start
    private int size;

    public void drawAfterClose() {

    }

    public void imageChuncks(BufferedImage image, GraphicsContext gc) {

        this.rows = (int) (image.getHeight() / 100);
        this.cols = (int) (image.getWidth() / 100); // determines the chunk width and height
        this.canvasImage.setHeight((rows) * size + ((rows + 1) * 10));
        this.canvasImage.setWidth((cols) * size + ((cols + 1) * 10));
        System.out.println(rows + " " + cols);

        chunksVec = new Chunk[rows][cols];
        for (int x = 0; x < this.rows; x++) {
            for (int y = 0; y < this.cols; y++) {
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
        this.mosaicVec = new Chunk[rows][cols];
        for (int x = 0; x < this.rows; x++) {
            for (int y = 0; y < this.cols; y++) {
                this.mosaicVec[x][y] = new Chunk(null, x, y, size);
            }
        }
    }

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

    public byte[] imageToBytes(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }

    public static void main(String[] args) {
        launch(args);
    } // main

} // fin de la clase
