/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package file;

import domain.Chunk;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Yerlin Leal
 */
public class SaveFile {

    private String path;

    public SaveFile() {
        this.path = "save.dat";
    } // constructor

    public void save(Chunk[][] matrizChunks, Chunk[][] matrizMosaicChunkses) throws IOException, ClassNotFoundException {
        //System.out.println(matrizChunks[0][0].getImageBytes().length);
        File file = new File(this.path);
        List<Chunk[][]> previous = new ArrayList<>();
        previous.add(matrizChunks);
        previous.add(matrizMosaicChunkses);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
        objectOutputStream.writeUnshared(previous);
        objectOutputStream.close();
    } // save

    public List<Chunk[][]> recover() throws IOException, ClassNotFoundException {
        File myFile = new File(this.path);
        List<Chunk[][]> previous = new ArrayList<>();
        if (myFile.exists()) {
            ObjectInputStream objectInputStream
                    = new ObjectInputStream(
                            new FileInputStream(myFile)
                    );
            Object aux = objectInputStream.readObject();
            previous = (List<Chunk[][]>) aux;
            objectInputStream.close();
        } // if(myFile.exists())
        //System.out.println(previous.get(1)[0][0].getImageBytes().length);
        return previous;
    } // recover

    public void newProyect(){
        File file=new File(path);
        if(file.exists()){
            file.delete();
        }
    }
    
} // fin de la clase
