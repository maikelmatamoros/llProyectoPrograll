/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package file;

import domain.Chunk;
import domain.MosaicChunk;
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

    public void save(Chunk[][] matrizChunks, MosaicChunk[][] matrizMosaicChunkses) throws IOException, ClassNotFoundException {
        File file = new File(this.path);
        List<ArrayList> previous = new ArrayList<ArrayList>();
        ArrayList<Chunk[][]> chunksAL = new ArrayList<>();
        ArrayList<MosaicChunk[][]> mosaicChunksAL = new ArrayList<>();
        chunksAL.add(matrizChunks);
        mosaicChunksAL.add(matrizMosaicChunkses);
        previous.add(chunksAL);
        previous.add(mosaicChunksAL);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
        objectOutputStream.writeUnshared(previous);
        objectOutputStream.close();
    } // save

    public List<ArrayList> recover() throws IOException, ClassNotFoundException {
        File myFile = new File(this.path);
        List<ArrayList> previous = new ArrayList<>();
        if (myFile.exists()) {
            ObjectInputStream objectInputStream
                    = new ObjectInputStream(
                            new FileInputStream(myFile)
                    );
            Object aux = objectInputStream.readObject();
            previous = (List<ArrayList>) aux;
            objectInputStream.close();
        } // if(myFile.exists())
        return previous;
    } // recover

} // fin de la clase
