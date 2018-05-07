package business;

import domain.Button;
import domain.Chunk;
import domain.ChunkImage;
import domain.ChunkMosaic;
import file.SaveFile;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import proyecto2progra2.Window;

public class Gestor {

    private Chunk[][] chunkImage;
    private Chunk[][] chunkMosaic;
    private int size, rowsMosaic, colsMosaic = 0;
    private int rowsImage, colsImage, k, l, i, j;
    private int contImageChanges, contMosaicChanges = 0;
    private BufferedImage image;

    public void imageChanges(int action) {
        if (action == 0) {
            this.contImageChanges++;
        } else {
            this.contImageChanges = 0;
        }
    } // imageChanges: contabiliza cambios en el canvasImage 

    public void mosaicChanges(int action) {
        if (action == 0) {
            this.contMosaicChanges++;
        } else {
            this.contMosaicChanges = 0;
        }
    } // mosaicChanges: contaviliza los cambios del canvasMosaic

    public boolean getConts() {
        if (this.contImageChanges == 0 && this.contMosaicChanges == 0) {
            return true;
        } else {
            return false;
        }
    } // getConts: retorna true sino hay cambios, de lo contrario retorna false

    public void setState(ArrayList<Button> list, Button button) throws IOException {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getState()) {
                list.get(i).setAvailable();
            }
        } // for
        button.setAvailable();
    } // setState: cambia el estado de los botones, activa el botón seleccionado y desactiva los demás

    public void recharge(Canvas canvasImage, GraphicsContext gcI, GraphicsContext gcM, Canvas canvasMosaic, File file) {
        try {
            if (file.exists()) {
                List<Chunk[][]> list = new SaveFile().recover(file); // cargo lista con matrices guardadas
                if (list.get(0) != null) { // si la matriz de la imagen no es null repinta la matriz de imágenes
                    this.chunkImage = list.get(0);
                    this.size = this.chunkImage[0][0].getSize();
                    this.rowsImage = this.chunkImage.length;
                    this.colsImage = this.chunkImage[0].length;
                    canvasImage.setHeight((this.rowsImage) * this.size + ((this.rowsImage + 1) * 5));
                    canvasImage.setWidth((this.colsImage) * this.size + ((this.colsImage + 1) * 5));
                    for (int x = 0; x < this.rowsImage; x++) {
                        for (int y = 0; y < this.colsImage; y++) {
                            this.chunkImage[x][y].draw(gcI);
                        } // for y
                    } // for x
                } // 
                if (list.get(1) != null) { // si la matriz del mosaico no es null
                    this.chunkMosaic = list.get(1); // si la matriz del mosaico no es null repunta el mosaico guardado
                    this.rowsMosaic = this.chunkMosaic.length;
                    this.colsMosaic = this.chunkMosaic[0].length;
                    canvasMosaic.setHeight(this.rowsMosaic * this.size);
                    canvasMosaic.setWidth(this.colsMosaic * this.size);
                    drawGrid(gcM, canvasMosaic);
                    repaintMosaic(gcM);
                } // if (list.get(1) != null)
            } // if (new File("saveProject.dat").exists())
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
        } // try-catch
    } // recharge: repinta imagen y mosaico guardado

    public void drawGrid(GraphicsContext gcM, Canvas canvasMosaic) {
        if (this.size > 0 && this.rowsMosaic > 0 && this.colsMosaic > 0) {
            canvasMosaic.setHeight(this.rowsMosaic * this.size);
            canvasMosaic.setWidth(this.colsMosaic * this.size);
            for (int x = 0; x <= this.rowsMosaic; x++) {
                gcM.strokeLine(0, x * this.size, this.colsMosaic * this.size, x * this.size); // rows
            } // for x
            for (int y = 0; y <= this.colsMosaic; y++) {
                gcM.strokeLine(y * this.size, 0, y * this.size, this.size * this.rowsMosaic); // cols
            } // for y
        } // if (size > 0 && rowsMosaic > 0 && colsMosaic > 0)
    } // drawGrid: dibuja las lineas del mosaico

    public byte[] imageToBytes(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    } // imageToBytes: retorna arreglo de bytes de un BufferedImage

    public void initMosiacMatrix() {
        this.chunkMosaic = new ChunkMosaic[this.rowsMosaic][this.colsMosaic];
        for (int x = 0; x < this.rowsMosaic; x++) {
            for (int y = 0; y < this.colsMosaic; y++) {
                this.chunkMosaic[x][y] = new ChunkMosaic( new byte[0], y, x, this.size);
            } // for y
        } // for x
    } // initMosiacMatrix: inicia la matriz del mosaico en null

    public void split(GraphicsContext gc, Canvas canvasImage) {
        gc.clearRect(0, 0, canvasImage.getWidth(), canvasImage.getHeight());
        this.rowsImage = (int) (this.image.getHeight() / this.size);
        this.colsImage = (int) (this.image.getWidth() / this.size);
        canvasImage.setHeight((this.rowsImage) * this.size + ((this.rowsImage + 1) * 2));
        canvasImage.setWidth((this.colsImage) * this.size + ((this.colsImage + 1) * 2));
        this.chunkImage = new ChunkImage[this.rowsImage][this.colsImage];
        for (int x = 0; x < this.rowsImage; x++) {
            for (int y = 0; y < this.colsImage; y++) {
                try {
                    //Inicia la matriz de imágenes con chunk
                    BufferedImage aux = this.image.getSubimage((y * this.size), (x * this.size), this.size, this.size);
                    this.chunkImage[x][y] = new ChunkImage(imageToBytes(aux), y, x, this.size);
                    this.chunkImage[x][y].draw(gc);
                } catch (IOException ex) {
                    Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                }
            } // for y
        } // for x
    } // split: divide la imagen cargada en chunks

    public void selectAnImage(Stage primaryStage, GraphicsContext gc, FileChooser fileChooser, Canvas canvasImage) {
        File selectedDirectory = fileChooser.showOpenDialog(primaryStage);
        if (selectedDirectory != null) {
            try {
                BufferedImage aux = ImageIO.read(selectedDirectory);
                if (this.size != 0) {
                    if (aux.getHeight() >= this.size && aux.getWidth() >= this.size) {
                        gc.clearRect(0, 0, canvasImage.getWidth(), canvasImage.getHeight());
                        this.image = aux;
                        canvasImage.setHeight(this.image.getHeight());
                        canvasImage.setWidth(this.image.getWidth());
                        gc.drawImage(SwingFXUtils.toFXImage(this.image, null), 0, 0);
                    }
                } else {
                    gc.clearRect(0, 0, canvasImage.getWidth(), canvasImage.getHeight());
                    this.image = aux;
                    canvasImage.setHeight(this.image.getHeight());
                    canvasImage.setWidth(this.image.getWidth());
                    gc.drawImage(SwingFXUtils.toFXImage(this.image, null), 0, 0);
                }
            } catch (IOException ex) {
                Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
            }
        } // if (selectedDirectory != null)
    } // selectAnImage: selecciona una imagen con un fileChooser

    public void repaintMosaic(GraphicsContext gcM) {
        for (int x = 0; x < this.rowsMosaic; x++) {
            for (int y = 0; y < this.colsMosaic; y++) {
                try {
                    if (this.chunkMosaic[x][y].getImageBytes().length != 0) {
                        this.chunkMosaic[x][y].draw(gcM);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                }
            } // for y
        } // for x
    } // repaintMosaic: pinta en el canvas del mosaico los chunks no nulos

    public void exportMosaic(Stage primaryStage, GraphicsContext gcM, Canvas canvasMosaic, FileChooser fileChooser) {
        if (this.chunkMosaic != null) {
            gcM.clearRect(0, 0, this.colsMosaic * this.size, this.rowsMosaic * this.size);
            repaintMosaic(gcM);
            WritableImage wim = new WritableImage((int) Math.round(canvasMosaic.getWidth()), (int) Math.round(canvasMosaic.getHeight()));
            SnapshotParameters snapshotParameters = new SnapshotParameters();
            snapshotParameters.setFill(Color.TRANSPARENT);
            canvasMosaic.snapshot(snapshotParameters, wim);
            drawGrid(gcM, canvasMosaic);
            repaintMosaic(gcM);
            String path = fileChooser.showSaveDialog(primaryStage).getPath() + ".png";
            File file = new File(path);
            if (path != null) {
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
                } catch (IOException ex) {
                    Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                }
            } // if
        } // if (chunkMosaic != null)
    } // exportMosaic: exporta el mosaico en formato png

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
    } // newProyect: reinicia todo

    public void selectAChunckImage(int xP, int yP) {
        for (int x = 0; x < this.rowsImage; x++) {
            for (int y = 0; y < this.colsImage; y++) {
                if (this.chunkImage[x][y].chunkClicked(xP, yP)) {
                    this.i = x;
                    this.j = y;
                    break;
                }
            } // for y
        } // for x
    } // selectAChunckImage: guarda en la variable i, j la posición del cunk seleccionado en la matriz de la imagen

    public void selectAMosaic(int xP, int yP) {
        for (int x = 0; x < this.rowsMosaic; x++) {
            for (int y = 0; y < this.colsMosaic; y++) {
                if (this.chunkMosaic[x][y].chunkClicked(xP, yP)) {
                    this.k = x;
                    this.l = y;
                    break;
                }
            } // for y
        } // for x
    } // selectAMosaic: guarda en la variable k, l la posición del cunk seleccionado en la matriz del mosaico

    public void paintInMosaic(int xP, int yP, GraphicsContext gcM) {
        selectAMosaic(xP, yP);
        ((ChunkMosaic) this.chunkMosaic[this.k][this.l]).setInitialValues();
        this.chunkMosaic[this.k][this.l].setImageBytes(this.chunkImage[this.i][this.j].getImageBytes());
        try {
            this.chunkMosaic[this.k][this.l].draw(gcM);
        } catch (IOException ex) {
            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
        }
    } // paintInMosaic: pinta la imagen del chunk seleccionado del canvasImage en el chunk del canvasMosaic clickeado

    public void saveProject(FileChooser fileChooser, Stage stage) throws IOException, ClassNotFoundException {
        if (this.chunkImage != null) {
            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                new SaveFile().save(this.chunkImage, this.chunkMosaic, file.getAbsolutePath());
                imageChanges(1);
                mosaicChanges(1);
            }
        }
    } // saveProject: guarda el proyecto

    public boolean getImageSelected() {
        if (this.image != null) {
            return true;
        } else {
            return false;
        }
    } // getImageSelected: retorna true si hay una imagen cargada en el canvasImage

    public Chunk getMosaicChunk() {
        return this.chunkMosaic[k][l];
    } // getMosaicChunk: retorna el chunk del canvasMosaic seleccionado

    public void setMosaicsParameters(int heigth, int width) {
        this.rowsMosaic = heigth / this.size;
        this.colsMosaic = width / this.size;
    } // setMosaicsParameters: da valor a las variables globales del tamaño de las matrices

    public void setSize(int size) {
        this.size = size;
    } // setSize: da valor de los chunk a la variable global size 

    public int getSize() {
        return this.size;
    } // getSize: retorna el valor del size de los chunks

    public boolean isDefinedMosaic() {
        if (this.chunkMosaic != null) {
            return true;
        } else {
            return false;
        }
    } // isDefinedMosaic: retorna true si el mosaico tiene algún chunk

    public int getSmaller() {
        if (this.image.getHeight() > this.image.getWidth()) {
            return this.image.getWidth();
        } else {
            return this.image.getHeight();
        }
    } // getSmaller: retorna menor valor entre las dimenciones de la imagen

    public boolean isSplitted() {
        if (this.chunkImage != null) {
            return true;
        } else {
            return false;
        }
    } // isSplitted: retorna true si ya se hizo split, de lo contrario retona false

    public void delete(GraphicsContext graphicContextMosaic, Canvas canvasMosaic) {
        graphicContextMosaic.clearRect(0, 0, canvasMosaic.getWidth(), canvasMosaic.getHeight());
        ((ChunkMosaic) getMosaicChunk()).setImageBytes(new byte[0]);
        drawGrid(graphicContextMosaic, canvasMosaic);
        repaintMosaic(graphicContextMosaic);
    } // delete: borra el chunk del mosaico seleccionado

} // fin de la clase

// Patrón de diseño: Facade