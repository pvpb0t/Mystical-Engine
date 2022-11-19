package me.pvpb0t.util;
/*
https://github.com/MrCrayfish/ModelCreator/tree/b85b0da614ef4948bf8260db9ac845930b170cc4
2022-10-17
 */
public enum OsUtil {

    WINDOWS,
    MAC,
    LINUX,
    SOLARIS,
    UNKNOWN;

    private static final OsUtil OS;

    static
    {
        String name = System.getProperty("os.name").toLowerCase();
        if(name.contains("win"))
        {
            OS = OsUtil.WINDOWS;
        }
        else if(name.contains("mac"))
        {
            OS = OsUtil.MAC;
        }
        else if(name.contains("solaris"))
        {
            OS = OsUtil.SOLARIS;
        }
        else if(name.contains("sunos"))
        {
            OS = OsUtil.SOLARIS;
        }
        else if(name.contains("linux"))
        {
            OS = OsUtil.LINUX;
        }
        else if(name.contains("unix"))
        {
            OS = OsUtil.LINUX;
        }
        else
        {
            OS = OsUtil.UNKNOWN;
        }
    }

    public static OsUtil get()
    {
        return OS;
    }
}
