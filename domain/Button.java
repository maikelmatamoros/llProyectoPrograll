package domain;

import java.io.IOException;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Button {

    private String path;
    private int x, y, w, h;
    private boolean state;
    String name;
    
    public Button(String path, int x, int y, int w, int h,String name) {
        this.path = path;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.state=false;
        this.name=name;
    } // constructor
    
    
    public void setAvailable(GraphicsContext gc) throws IOException{
        this.state=!this.state;
        draw(gc);
    }

    public void draw(GraphicsContext gc) throws IOException {
        
        if(state){
            System.out.println("/assets/"+name+"True.png");
            gc.drawImage(new Image("/assets/"+name+"True.png"), x, y, w, h);
        }else{
            System.out.println("/assets/"+name+".png");
            gc.drawImage(new Image("/assets/"+name+".png"), x, y, w, h);
        }
        
    } // draw
    
    public boolean getState(){
        return this.state;
    }

    public boolean isClicked(int xMouse, int yMouse) {
        if ((xMouse >= x && xMouse <= x + w) && (yMouse >= this.y && yMouse <= this.y + h)) {
            return true;
        }
        return false;
    } // isClicked

} // fin de la clase
