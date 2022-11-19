package me.pvpb0t.sound;

import me.pvpb0t.util.Logger;

import javax.sound.sampled.*;
import java.io.File;

public class Sound {
    public Clip getClip() {
        return clip;
    }

    private Clip clip=null;
    private String file;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Sound(String file) {
        this.file = file;
        clip = null;
    }

    //https://stackoverflow.com/questions/29600213/java-lwjgl-ogg-background-music 2022-10-27
    public void playSound(boolean loop) throws Exception{
        if (clip != null && clip.isOpen()) clip.close();
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("res/sound/" + file + ".wav").getAbsoluteFile());
        clip = AudioSystem.getClip();

        clip.open(audioInputStream);
        FloatControl gainControl =
                (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(0f);

        Logger.print("Sound Channel: "+clip.getFrameLength() + " | " + clip.getFramePosition());
        clip.start();

        if(loop){
            clip.loop(100000);
        }
    }

    public void stopSound(){
        if(clip!=null){
            clip.stop();
        }
    }

}
