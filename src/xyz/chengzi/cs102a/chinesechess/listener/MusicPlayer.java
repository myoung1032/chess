package xyz.chengzi.cs102a.chinesechess.listener;

import javax.sound.sampled.*;
import java.io.File;
import java.lang.reflect.Field;

public class MusicPlayer implements Runnable {
    File file;
    public MusicPlayer(String path){
        file=new File(path);
    }
    boolean loop=true;
    public MusicPlayer(String path,boolean loop){
        this.loop=loop;
        file=new File(path);
    }
    @Override
    public void run() {
        byte[] auBuffer=new byte[1024*128];
        do{
            AudioInputStream audioInputStream=null;
            SourceDataLine sourceDataLine=null;
            try {
                audioInputStream= AudioSystem.getAudioInputStream(file);
                AudioFormat format=audioInputStream.getFormat();
                DataLine.Info info=new DataLine.Info(SourceDataLine.class,format);
                sourceDataLine=(SourceDataLine)AudioSystem.getLine(info);
                sourceDataLine.open(format);
                sourceDataLine.start();
                int count=0;
                while (count!=-1){
                    count=audioInputStream.read(auBuffer,0,auBuffer.length);
                    if (count>=0){
                        sourceDataLine.write(auBuffer,0,count);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                sourceDataLine.drain();
                sourceDataLine.close();
            }
        } while (loop);
    }
}
