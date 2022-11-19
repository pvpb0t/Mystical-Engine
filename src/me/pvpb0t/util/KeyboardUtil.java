package me.pvpb0t.util;

import org.lwjgl.input.Keyboard;
/*
https://github.com/MrCrayfish/ModelCreator/tree/b85b0da614ef4948bf8260db9ac845930b170cc4
2022-10-17
 */
public class KeyboardUtil {

    public static boolean isCTRLDown(){

            if(OsUtil.get() == OsUtil.MAC)
            {
                return Keyboard.isKeyDown(Keyboard.KEY_LMETA) || Keyboard.isKeyDown(Keyboard.KEY_RMETA);
            }
            else
            {
                return Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
            }

    }

}
