package domain;

import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;

public class ChunkMosaic extends Chunk {

    private int rotation;
    private int horizontal, vertical, negativoHorizontal, negativoVertical;

    public ChunkMosaic(byte[] image, int x, int y, int size) {
        super(image, x, y, size);
        this.rotation = 0;
        this.horizontal = 0;
        this.vertical = 0;
        this.negativoHorizontal = 1;
        this.negativoVertical = 1;
    } // constructor

    public void rotate(int click) {
        if (click == 0) {
            if (rotation < 360) {
                rotation += 90;
            } else {
                rotation = rotation * 0 + 90;
            }
        } else {
            if (rotation > 0) {
                rotation -= 90;
            } else {
                rotation = 360 - 90;
            }
        }
    } // rotate

    public void setRotation() { // Cambiar nombre al metodo
        this.rotation = 0;
        flipHorizontal(0);
        flipVertical(0);
    } // setRotation

    public void flipHorizontal(int click) {
        if (click == 1) { // derecha
            if (horizontal == size) {
                horizontal = 0;
                negativoHorizontal = 1;
            } else {
                horizontal = size;
                negativoHorizontal = -1;
            }
        } else { // izquierda
            horizontal = 0;
            negativoHorizontal = 1;
        }
        
    } // flip

    public void flipVertical(int click) {
        if (click == 1) { // abajo
            if(vertical == size){
                vertical = 0;
                negativoVertical = 1;
            }else{
                vertical = size;
                negativoVertical = -1;
            }
        } else { // arriba
            vertical = 0;
            negativoVertical = 1;
        }
    } // flip

    @Override
    public void draw(GraphicsContext gc) throws IOException {
        ImageView imageView = new ImageView(SwingFXUtils.toFXImage(bytesToImage(), null));
        imageView.setRotate(imageView.getRotate() + rotation);
        SnapshotParameters snapshot = new SnapshotParameters();
        gc.drawImage(imageView.snapshot(snapshot, null), x * size + horizontal, y * size + vertical, size * negativoHorizontal, size * negativoVertical);
    } // draw

    @Override
    public boolean chunkClicked(int xMouse, int yMouse) {
        if ((xMouse >= this.x * size && xMouse <= this.x * size + size)
                && (yMouse >= this.y * size && yMouse <= this.y * size + size)) {
            return true;
        }
        return false;
    } // chunkClicked

} // fin de la clase
