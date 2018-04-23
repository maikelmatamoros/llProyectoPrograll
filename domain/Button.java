/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package domain;

import java.io.IOException;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 *
 * @author Yerlin Leal
 */
public class Button {
    private String path;
    private int x, y, w, h;

    public Button(String path, int x, int y, int w, int h) {
        this.path = path;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public void draw(GraphicsContext gc) throws IOException {
        gc.drawImage(new Image(path), x, y, w, h);
    }
    
    public boolean isClicked(int xMouse, int yMouse) {
        if ((xMouse >= x && xMouse <= x + w) && (yMouse >= this.y && yMouse <= this.y+h)){
            return true;
        }
        return false;
    }
}
