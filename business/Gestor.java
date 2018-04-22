/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import Utils.ChunkTypes;
import domain.Chunk;
import domain.ChunkImage;
import domain.ChunkMosaic;
import file.SaveFile;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import proyecto2progra2.Proyecto2Progra2;

/**
 *
 * @author maikel
 */
public class Gestor {

    private Chunk[][] chunkImage;
    private Chunk[][] chunkMosaic;
    private int size, rowsMosaic, colsMosaic = 0;
    private int rowsImage, colsImage, k, l, i, j;
    private BufferedImage image;
    private boolean rotAccess = false;

    public void available(boolean state) {
        this.rotAccess = state;
    }

    public boolean getAcces() {
        return this.rotAccess;
    }

    public void reinit(TextField tfSize, TextField tfWidth, TextField tfHeight, Canvas image,
            GraphicsContext gcI, GraphicsContext gcM, Canvas canvasMosaic) {

        try {
            if (new File("save.dat").exists()) {
                List<Chunk[][]> list = new SaveFile().recover();
                if (list.get(0) != null) {
                    tfSize.setEditable(false);
                    chunkImage = list.get(0);
                    size = chunkImage[0][0].getSize();
                    rowsImage = chunkImage.length;
                    colsImage = chunkImage[0].length;
                    image.setHeight((rowsImage) * size + ((rowsImage + 1) * 10));
                    image.setWidth((colsImage) * size + ((colsImage + 1) * 10));
                    for (int x = 0; x < rowsImage; x++) {
                        for (int y = 0; y < colsImage; y++) {
                            chunkImage[x][y].draw(gcI);
                        }
                    }
                }
                if (list.get(1) != null) {
                    tfHeight.setEditable(false);
                    tfWidth.setEditable(false);
                    chunkMosaic = list.get(1);
                    rowsMosaic = chunkMosaic.length;
                    colsMosaic = chunkMosaic[0].length;
                    canvasMosaic.setHeight(rowsMosaic * size);
                    canvasMosaic.setWidth(colsMosaic * size);

                    drawGrid(gcM, canvasMosaic);

                    repaintMosaic(gcM);
                }

            }
        } catch (IOException ex) {
            Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void drawGrid(GraphicsContext gcM, Canvas canvasMosaic) {
        //initMosiacChunks();
        if (size > 0 && rowsMosaic>0 && colsMosaic>0) {
            canvasMosaic.setHeight(this.rowsMosaic * size);
            canvasMosaic.setWidth(this.colsMosaic * size);

            for (int x = 0; x <= this.rowsMosaic; x++) {

                gcM.strokeLine(0, x * size, colsMosaic * size, x * size); // rows
            }
            for (int y = 0; y <= this.colsMosaic; y++) {

                gcM.strokeLine(y * size, 0, y * size, size * this.rowsMosaic); // cols
            }
        }

    } // drawGrid

    public byte[] imageToBytes(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }

    public void initMosiacChunks() {
        chunkMosaic = new ChunkMosaic[rowsMosaic][colsMosaic];
        for (int x = 0; x < this.rowsMosaic; x++) {
            for (int y = 0; y < this.colsMosaic; y++) {
                chunkMosaic[x][y] = new ChunkFactory().createChunk(ChunkTypes.mosaic, new byte[0], y, x, size);
            }
        }
    }

    public void imageChuncks(GraphicsContext gc, Canvas canvasImage) {

        gc.clearRect(0, 0, canvasImage.getWidth(), canvasImage.getHeight());
        this.rowsImage = (int) (image.getHeight() / size);
        this.colsImage = (int) (image.getWidth() / size); // determines the chunk width and height
        canvasImage.setHeight((rowsImage) * size + ((rowsImage + 1) * 10));
        canvasImage.setWidth((colsImage) * size + ((colsImage + 1) * 10));
        chunkImage = new ChunkImage[rowsImage][colsImage];
        for (int x = 0; x < this.rowsImage; x++) {
            for (int y = 0; y < this.colsImage; y++) {
                try {
                    //Initialize the image array with image chunks

                    BufferedImage aux = image.getSubimage((y * this.size), (x * this.size), this.size, this.size);
                    chunkImage[x][y] = new ChunkFactory().createChunk(ChunkTypes.image, imageToBytes(aux), y, x, size);
                    chunkImage[x][y].draw(gc);
                    // draws the image chunk
                } // for y
                catch (IOException ex) {
                    Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } // for x
    } // imageChuncks

    public void selectImage(Stage primaryStage, GraphicsContext gc, FileChooser fileChooser,
            Canvas canvasImage) {

        File selectedDirectory = fileChooser.showOpenDialog(primaryStage);
        if (selectedDirectory != null) {
            try {

                this.image = ImageIO.read(selectedDirectory);
                canvasImage.setHeight(image.getHeight());
                canvasImage.setWidth(image.getWidth());
                gc.drawImage(SwingFXUtils.toFXImage(image, null), 0, 0);

            } // if
            catch (IOException ex) {
                Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void repaintMosaic(GraphicsContext gcM) {
        for (int x = 0; x < rowsMosaic; x++) {
            for (int y = 0; y < colsMosaic; y++) {
                try {
                    if (chunkMosaic[x][y].getImageBytes().length != 0) {
                        chunkMosaic[x][y].draw(gcM);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void exportMosaic(Stage primaryStage, GraphicsContext gcM, Canvas canvasMosaic, FileChooser fileChooser) {
        gcM.clearRect(0, 0, colsMosaic * size, rowsMosaic * size);
        repaintMosaic(gcM);
        WritableImage wim = new WritableImage((int) Math.round(canvasMosaic.getWidth()), (int) Math.round(canvasMosaic.getHeight()));
        SnapshotParameters snapshotParameters = new SnapshotParameters();
        snapshotParameters.setFill(Color.TRANSPARENT);
        canvasMosaic.snapshot(snapshotParameters, wim);
        drawGrid(gcM, canvasMosaic);
        repaintMosaic(gcM);
        File file = fileChooser.showSaveDialog(primaryStage);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
        } catch (IOException ex) {
            Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
        }
        //graphicContextMosaic.clearRect(0, 0, colsMosaic * size, rowsMosaic * size);

    }

    public void newProyect(TextField tfSize, TextField tfWidth, TextField tfHeight, Canvas canvasImage,
            GraphicsContext gcI, GraphicsContext gcM, Canvas canvasMosaic) {
        chunkMosaic = null;
        chunkImage = null;
        tfSize.setEditable(true);
        tfHeight.setEditable(true);
        tfWidth.setEditable(true);
        gcI.clearRect(0, 0, canvasImage.getWidth(), canvasImage.getHeight());
        gcM.clearRect(0, 0, canvasMosaic.getWidth(), canvasMosaic.getHeight());
        new SaveFile().newProyect();
    }

    public void selectAChunckImage(int xP, int yP) {
        if (chunkImage[0][0] == null) {
            System.out.println("Ingrese una imagen primero");
        } else {

            for (int x = 0; x < rowsImage; x++) {
                for (int y = 0; y < colsImage; y++) {
                    if (chunkImage[x][y].chunkClicked(xP, yP)) {
                        i = x;
                        j = y;
                        break;
                    }
                }
            }
        }
    }

    public void selectAMosaic(int xP, int yP) {
        for (int x = 0; x < rowsMosaic; x++) {
            for (int y = 0; y < colsMosaic; y++) {
                if (chunkMosaic[x][y].chunkClicked(xP, yP)) {
                    System.out.println(y + " " + x);
                    k = x;
                    l = y;
                    break;
                }
            }
        }
    }

    public void paintInMosaic(int xP, int yP, GraphicsContext gcM) {

        selectAMosaic(xP, yP);
        chunkMosaic[k][l].setImageBytes(chunkImage[i][j].getImageBytes());
        try {
            chunkMosaic[k][l].draw(gcM);
        } catch (IOException ex) {
            Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void save() throws IOException, ClassNotFoundException {
        new SaveFile().save(chunkImage, chunkMosaic);
    }

    public List<Chunk[][]> recover() throws IOException, ClassNotFoundException {
        return new SaveFile().recover();
    }

    public void newProyect() {
        new SaveFile().newProyect();
    }

    public Chunk getMosaicChunk() {
        return this.chunkMosaic[k][l];
    }

    public void setMosaicsParameters(int rows, int cols) {
        this.rowsMosaic = rows / size;
        this.colsMosaic = cols / size;

    }

    public void setSize(int size) {
        this.size = size;
    }
}
