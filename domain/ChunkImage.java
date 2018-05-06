package domain;

import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;

public class ChunkImage extends Chunk {

    public ChunkImage(byte[] image, int x, int y, int size) {
        super(image, x, y, size);
    } // constructor

    @Override
    public void draw(GraphicsContext gc) throws IOException {
        ImageView imageView = new ImageView(SwingFXUtils.toFXImage(super.bytesToImage(), null));
        SnapshotParameters snapshot = new SnapshotParameters();
        gc.drawImage(imageView.snapshot(snapshot, null), (x * size) + (1 + x) * 2, (y * size) + (1 + y) * 2, size, size);
    } // draw

    @Override
    public boolean chunkClicked(int xMouse, int yMouse) {
        if ((xMouse >= (x * size) + (1 + x) * 2 && xMouse <= (x * size) + (1 + x) * 2 + this.size)
                && (yMouse >= (y * size) + (1 + y) * 2 && yMouse <= (y * size) + (1 + y) * 2 + this.size)) {
            return true;
        }
        return false;
    } // chunkClicked: retorna true si el chunk fue ckickeado, de lo contrario false

} // fin de la clase
