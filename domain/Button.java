package domain;

import java.io.IOException;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Button {

    private String path;
    private int x, y, w, h;

    public Button(String path, int x, int y, int w, int h) {
        this.path = path;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    } // constructor
    
    public void setPath(String path) {
        this.path = path;
    } // setPath

    public void draw(GraphicsContext gc) throws IOException {
        gc.drawImage(new Image(path), x, y, w, h);
    } // draw

    public boolean isClicked(int xMouse, int yMouse) {
        if ((xMouse >= x && xMouse <= x + w) && (yMouse >= this.y && yMouse <= this.y + h)) {
            return true;
        }
        return false;
    } // isClicked

} // fin de la clase
