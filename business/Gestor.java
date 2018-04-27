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
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import proyecto2progra2.Proyecto2Progra2;

public class Gestor {

    private Chunk[][] chunkImage;
    private Chunk[][] chunkMosaic;
    private int size, rowsMosaic, colsMosaic = 0;
    private int rowsImage, colsImage, k, l, i, j;
    private BufferedImage image;
    private boolean rotAccess = false;

    public void available() {
        this.rotAccess = !rotAccess;
    } // available

    public boolean getRotAccess() {
        return this.rotAccess;
    } // getRotAccess

    public void reinit(Canvas image, GraphicsContext gcI, GraphicsContext gcM, Canvas canvasMosaic) {
        try {
            if (new File("save.dat").exists()) {
                List<Chunk[][]> list = new SaveFile().recover();
                if (list.get(0) != null) {

                    this.chunkImage = list.get(0);
                    this.size = this.chunkImage[0][0].getSize();
                    this.rowsImage = this.chunkImage.length;
                    this.colsImage = this.chunkImage[0].length;
                    image.setHeight((this.rowsImage) * this.size + ((this.rowsImage + 1) * 10));
                    image.setWidth((this.colsImage) * this.size + ((this.colsImage + 1) * 10));
                    for (int x = 0; x < this.rowsImage; x++) {
                        for (int y = 0; y < this.colsImage; y++) {
                            this.chunkImage[x][y].draw(gcI);
                        } // for y
                    } // for x
                } // if (list.get(0) != null)
                if (list.get(1) != null) {

                    this.chunkMosaic = list.get(1);
                    this.rowsMosaic = this.chunkMosaic.length;
                    this.colsMosaic = this.chunkMosaic[0].length;
                    canvasMosaic.setHeight(this.rowsMosaic * this.size);
                    canvasMosaic.setWidth(this.colsMosaic * this.size);
                    drawGrid(gcM, canvasMosaic);
                    repaintMosaic(gcM);
                } // if (list.get(1) != null)
            } // if (new File("save.dat").exists())
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
        } // try-catch
    } // reinit

    public void drawGrid(GraphicsContext gcM, Canvas canvasMosaic) {
        //initMosiacChunks();
        if (this.size > 0 && this.rowsMosaic > 0 && this.colsMosaic > 0) {
            canvasMosaic.setHeight(this.rowsMosaic * this.size);
            canvasMosaic.setWidth(this.colsMosaic * this.size);
            for (int x = 0; x <= this.rowsMosaic; x++) {
                gcM.strokeLine(0, x * this.size, this.colsMosaic * this.size, x * this.size); // rows
            } // for x
            for (int y = 0; y <= this.colsMosaic; y++) {
                gcM.strokeLine(y * this.size, 0, y * size, size * this.rowsMosaic); // cols
            } // for y
        } // if (size > 0 && rowsMosaic > 0 && colsMosaic > 0)
    } // drawGrid

    public byte[] imageToBytes(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    } // imageToBytes

    public void initMosiacChunks() {
        this.chunkMosaic = new ChunkMosaic[this.rowsMosaic][this.colsMosaic];
        for (int x = 0; x < this.rowsMosaic; x++) {
            for (int y = 0; y < this.colsMosaic; y++) {
                this.chunkMosaic[x][y] = new ChunkFactory().createChunk(ChunkTypes.mosaic, new byte[0], y, x, this.size);
            } // for y
        } // for x
    } // initMosiacChunks

    public void imageChuncks(GraphicsContext gc, Canvas canvasImage) {
        gc.clearRect(0, 0, canvasImage.getWidth(), canvasImage.getHeight());
        this.rowsImage = (int) (this.image.getHeight() / this.size);
        this.colsImage = (int) (this.image.getWidth() / this.size); // determines the chunk width and height
        canvasImage.setHeight((this.rowsImage) * this.size + ((this.rowsImage + 1) * 10));
        canvasImage.setWidth((this.colsImage) * this.size + ((this.colsImage + 1) * 10));
        this.chunkImage = new ChunkImage[this.rowsImage][this.colsImage];
        for (int x = 0; x < this.rowsImage; x++) {
            for (int y = 0; y < this.colsImage; y++) {
                try {
                    //Initialize the image array with image chunks
                    BufferedImage aux = image.getSubimage((y * this.size), (x * this.size), this.size, this.size);
                    this.chunkImage[x][y] = new ChunkFactory().createChunk(ChunkTypes.image, imageToBytes(aux), y, x, this.size);
                    this.chunkImage[x][y].draw(gc);
                    // draws the image chunk
                } // for y
                catch (IOException ex) {
                    Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } // for x
    } // imageChuncks

    public void selectImage(Stage primaryStage, GraphicsContext gc, FileChooser fileChooser, Canvas canvasImage) {
        File selectedDirectory = fileChooser.showOpenDialog(primaryStage);
        if (selectedDirectory != null) {
            try {
                this.image = ImageIO.read(selectedDirectory);
                canvasImage.setHeight(this.image.getHeight());
                canvasImage.setWidth(this.image.getWidth());
                gc.drawImage(SwingFXUtils.toFXImage(this.image, null), 0, 0);

            } // if
            catch (IOException ex) {
                Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
            }
        } // if (selectedDirectory != null)
    } // selectImage

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
            } // for y
        } // for x
    } // repaintMosaic

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
    } // exportMosaic

    public void newProyect(Canvas canvasImage, GraphicsContext gcI, GraphicsContext gcM, Canvas canvasMosaic) {
        this.chunkMosaic = null;
        this.chunkImage = null;
        this.size = 0;
        this.rowsImage = 0;
        this.colsImage = 0;
        this.colsMosaic = 0;
        this.rowsMosaic = 0;
        this.image = null;
        gcI.clearRect(0, 0, canvasImage.getWidth(), canvasImage.getHeight());
        gcM.clearRect(0, 0, canvasMosaic.getWidth(), canvasMosaic.getHeight());
        new SaveFile().newProyect();
    } // newProyect

    public void selectAChunckImage(int xP, int yP) {
        for (int x = 0; x < rowsImage; x++) {
            for (int y = 0; y < colsImage; y++) {
                if (chunkImage[x][y].chunkClicked(xP, yP)) {
                    i = x;
                    j = y;
                    break;
                }
            } // for y
        } // for x

    } // selectAChunckImage

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
    } // selectAMosaic

    public void paintInMosaic(int xP, int yP, GraphicsContext gcM) {
        selectAMosaic(xP, yP);
        ((ChunkMosaic) chunkMosaic[k][l]).setRotation();
        chunkMosaic[k][l].setImageBytes(chunkImage[i][j].getImageBytes());
        try {
            chunkMosaic[k][l].draw(gcM);
        } catch (IOException ex) {
            Logger.getLogger(Proyecto2Progra2.class.getName()).log(Level.SEVERE, null, ex);
        }
    } // paintInMosaic

    public void save() throws IOException, ClassNotFoundException {
        new SaveFile().save(chunkImage, chunkMosaic);
    } // save

    public List<Chunk[][]> recover() throws IOException, ClassNotFoundException {
        return new SaveFile().recover();
    } // recover

    public Chunk getMosaicChunk() {
        return this.chunkMosaic[k][l];
    } // getMosaicChunk

    public void setMosaicsParameters(int rows, int cols) {
        this.rowsMosaic = rows / size;
        this.colsMosaic = cols / size;
    } // setMosaicsParameters

    public void setSize(int size) {
        this.size = size;
    } // setSize

    public int getSize() {
        return this.size;
    } // getSize

    public boolean isDefinedValue() {
        if (rowsMosaic != 0 && colsMosaic != 0) {
            return true;
        } else {
            return false;
        }
    } // isDefinedValue

    public int getSmaller() {
        if (this.image.getHeight() > this.image.getWidth()) {
            return this.image.getWidth();
        } else {
            return this.image.getHeight();
        }
    } // getSmaller

    public boolean getImage() {
        if (this.image != null) {
            return true;
        } else {
            return false;
        }
    } // getImage

} // fin de la clase
