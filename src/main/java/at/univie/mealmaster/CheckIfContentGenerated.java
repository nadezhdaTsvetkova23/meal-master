package at.univie.mealmaster;

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class CheckIfContentGenerated {

    private RandomAccessFile raf;

    public CheckIfContentGenerated(){
        try{
            raf = new RandomAccessFile("generatedContent.txt", "rw");
        } catch(Exception e){

        }
    }
    public boolean checkFile(){
        return false;
//        try {
//            raf.seek(0);
//            boolean check = false;
//            try{
//                check = raf.readBoolean();
//            } catch (EOFException e){
//                return false;
//            }
//           return true;
//
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//        return false;
    }
    public void writeTrueToFile(){
        try{
            raf.writeBoolean(true);
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
