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

    public void save(Chunk[][] matrizChunks, Chunk[][] matrizMosaicChunkses,String path) throws IOException, ClassNotFoundException {
        File file;
        if(new File(path).exists()){
            file = new File(path);
        }else{
            file=new File(path+".dat");
        }
        List<Chunk[][]> previous = new ArrayList<>();
        previous.add(matrizChunks);
        previous.add(matrizMosaicChunkses);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
        objectOutputStream.writeUnshared(previous);
        objectOutputStream.close();
    } // save

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
    } // recover

} // fin de la clase
