package me.pvpb0t.util.math;

import me.pvpb0t.world.entity.EntityBase;
import me.pvpb0t.world.Light;
import org.lwjgl.util.vector.Vector3f;

public class MotionUtil {

    public static void moveEntityBase(EntityBase entityBase, Vector3f vector3f){
        Vector3f currentPos = entityBase.getPosition();
        entityBase.setPosition(new Vector3f(currentPos.x+vector3f.x,currentPos.y+vector3f.y, currentPos.z+vector3f.z));
    }

    public static void moveLight(Light light, Vector3f vector3f){
        Vector3f currentPos = light.getPosition();
        light.setPosition(new Vector3f(currentPos.x+vector3f.x,currentPos.y+vector3f.y, currentPos.z+vector3f.z));

    }

}
