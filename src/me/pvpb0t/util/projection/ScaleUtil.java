package me.pvpb0t.util.projection;

import me.pvpb0t.world.entity.EntityBase;
import org.lwjgl.util.vector.Vector3f;

public class ScaleUtil {

    public static void Scale(EntityBase entityBase, Vector3f vector3f){
        entityBase.setScale(new Vector3f(entityBase.getScale().x + vector3f.x, entityBase.getScale().y+ vector3f.y, entityBase.getScale().z+ vector3f.z));
    }

}
