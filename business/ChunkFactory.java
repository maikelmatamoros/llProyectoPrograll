/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import Utils.ChunkTypes;
import domain.Chunk;
import domain.ChunkImage;
import domain.ChunkMosaic;

/**
 *
 * @author maikel
 */
public class ChunkFactory {
    public Chunk createChunk(ChunkTypes type, byte[] image,int x,int y,int size){
        switch(type){
            case image:
                return new ChunkImage(image, x, y, size);
            case mosaic:
                return new ChunkMosaic(image, x, y, size);
            default: return null;
        }
    }
}
