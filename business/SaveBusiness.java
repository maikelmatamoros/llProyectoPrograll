/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import domain.Chunk;
import file.SaveFile;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author maikel
 */
public class SaveBusiness {
    private SaveFile save;
    
    public SaveBusiness(){
        this.save=new SaveFile();
    }
    
    public void save(Chunk[][] matrizChunks, Chunk[][] matrizMosaicChunkses) throws IOException, ClassNotFoundException {
        this.save.save(matrizChunks, matrizMosaicChunkses);
    }
    public List<Chunk[][]> recover() throws IOException, ClassNotFoundException {
        return this.save.recover();
    }
    
    public void newProyect(){
        this.save.newProyect();
    }
}
