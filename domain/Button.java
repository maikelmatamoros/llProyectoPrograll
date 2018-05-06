package domain;

import java.io.IOException;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Button {

    private int x, y, width, heigth;
    private boolean state;
    private String name;

    public Button(int x, int y, int width, int heigth, String name) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.heigth = heigth;
        this.state = false;
        this.name = name;
    } // constructor

    public void setAvailable() throws IOException {
        this.state = !this.state;
    } // setAvailable: habilita y desabilita el botón

    public void draw(GraphicsContext gc) throws IOException {
        if (this.state) {
            gc.drawImage(new Image("/assets/" + name + "True.png"), x, y, width, heigth);
        } else {
            gc.drawImage(new Image("/assets/" + name + ".png"), x, y, width, heigth);
        }
    } // draw: dibuja los botones con la apariencia de seleccionado o no

    public boolean getState() {
        return this.state;
    } // getState: retorna el estado del botón

    public boolean isClicked(int xMouse, int yMouse) {
        if ((xMouse >= this.x && xMouse <= this.x + this.width) && (yMouse >= this.y && yMouse <= this.y + this.heigth)) {
            return true;
        }
        return false;
    } // isClicked: retorna true si el botón fue clickeado y false si no

} // fin de la clase
