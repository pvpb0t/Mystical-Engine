package me.pvpb0t.util.math;

import me.pvpb0t.world.entity.EntityBase;

public class RotationUtil {

    public static void RotateEntityBase(EntityBase entityBase,float addX, float addY, float addZ){
        entityBase.setRotationX(entityBase.getRotationX()+addX);
        entityBase.setRotationY(entityBase.getRotationY()+addY);
        entityBase.setRotationZ(entityBase.getRotationZ()+addZ);

    }

}
