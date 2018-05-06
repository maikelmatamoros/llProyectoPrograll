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

public class SaveFile {

    public SaveFile() {
    } // constructor

    public void save(Chunk[][] matrixChunks, Chunk[][] matrixMosaicChunks, String path) throws IOException, ClassNotFoundException {
        File file;
        if (new File(path).exists()) {
            file = new File(path);
        } else {
            file = new File(path + ".dat");
        }
        List<Chunk[][]> previous = new ArrayList<>();
        previous.add(matrixChunks);
        previous.add(matrixMosaicChunks);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
        objectOutputStream.writeUnshared(previous);
        objectOutputStream.close();
    } // save: guarda en archivo .dat las matrices del canvasImage y del mosaicImage

    public List<Chunk[][]> recover(File file) throws IOException, ClassNotFoundException {
        List<Chunk[][]> previous = new ArrayList<>();
        if (file.exists()) {
            ObjectInputStream objectInputStream
                    = new ObjectInputStream(
                            new FileInputStream(file)
                    );
            Object aux = objectInputStream.readObject();
            previous = (List<Chunk[][]>) aux;
            objectInputStream.close();
        } // if(myFile.exists())
        return previous;
    } // recover: retorna lista con las matrices de las 

} // fin de la clase
