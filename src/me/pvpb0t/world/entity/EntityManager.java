package me.pvpb0t.world.entity;

import java.util.ArrayList;

public class EntityManager {

    public static ArrayList<EntityBase> getEntityBases() {
        return entityBases;
    }

    public static void addEntities(EntityBase entityBase){
        entityBases.add(entityBase);
    }

    public static void delEntity(EntityBase entityBase){
        entityBases.remove(entityBase);
    }

    public static void flush(){
        entityBases.clear();
    }

    public static void setEntityBases(ArrayList<EntityBase> entityBases) {
        EntityManager.entityBases = entityBases;
    }

    private static ArrayList<EntityBase> entityBases = new ArrayList<>();

}
