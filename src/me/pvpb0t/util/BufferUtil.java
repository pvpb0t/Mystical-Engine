package me.pvpb0t.util;

import org.lwjgl.BufferUtils;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class BufferUtil {

    public static FloatBuffer dataToFloatBuffer(float[] data){
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(data.length);
        floatBuffer.put(data);
        floatBuffer.flip();
        return floatBuffer;
    }
    public static IntBuffer dataToIntBuffer(int[] data){
        IntBuffer intBuffer = BufferUtils.createIntBuffer(data.length);
        intBuffer.put(data);
        intBuffer.flip();
        return intBuffer;
    }

    public static DoubleBuffer dataToDoubleBuffer(double[] data){
        DoubleBuffer doubleBuffer = BufferUtils.createDoubleBuffer(data.length);
        doubleBuffer.put(data);
        doubleBuffer.flip();
        return doubleBuffer;
    }


}
