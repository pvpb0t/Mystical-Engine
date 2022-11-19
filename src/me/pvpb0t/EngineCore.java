package me.pvpb0t;

import com.google.common.reflect.ClassPath;
import me.pvpb0t.render.font.fontrender.TextMaster;
import me.pvpb0t.render.font.mesh.FontType;
import me.pvpb0t.render.font.mesh.GUIText;
import me.pvpb0t.sound.Sound;
import me.pvpb0t.util.IconUtil;
import me.pvpb0t.util.OsUtil;
import me.pvpb0t.world.Camera;
import me.pvpb0t.world.entity.EntityBase;
import me.pvpb0t.world.Light;
import me.pvpb0t.render.Loader;
import me.pvpb0t.render.tesselator.MainTesselator;
import me.pvpb0t.render.ObjectLoader;
import me.pvpb0t.render.tesselator.EntityTesselator;
import me.pvpb0t.render.model.TexturedModel;
import me.pvpb0t.render.model.raw.Texture;
import me.pvpb0t.render.model.raw.VaoObject;
import me.pvpb0t.util.DebugUtil;
import me.pvpb0t.util.Logger;
import me.pvpb0t.util.math.MotionUtil;
import me.pvpb0t.util.math.RotationUtil;
import me.pvpb0t.world.entity.EntityBaseSP;
import me.pvpb0t.world.entity.EntityManager;
import me.pvpb0t.world.terrain.Terrain;
import me.pvpb0t.world.terrain.TerrainTexture;
import me.pvpb0t.world.terrain.TerrainTextureContainer;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.ImageIOImageData;
import org.newdawn.slick.util.Log;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class EngineCore {

    /*
    @TODO
    -cfg filer med olika inställningar som posX,posY, posZ, Fullscreen (inte svårt)
    -DEBUG MODE
     */

    //Global constants
    public static final String GameTitle = "Mystical Engine";
    public static final Integer version = 1;
    private static final long startTime = System.nanoTime() / 1000000L;
    //width = 1680, height =1050;
    private static final long width = 1280, height =720;
    private static final int FPS = 60;
    private static int tick= 0;
    private static int stuckTick= 0;



    private static VaoObject model;
    private static VaoObject house;
    private static Texture houseTex;
    private static TexturedModel texHouse;
    private static EntityBase houseEntity;


    private static TexturedModel TexturedTree;
    private static List<EntityBase> trees;
    private static List<EntityBase> foundGolds = new ArrayList<>();

    private static TexturedModel TexturedGrass;
    private static List<EntityBase> grass;

    private static TexturedModel TexturedGold;
   // private static EntityBase Gold;

    private static List<EntityBase> gold;

    private static EntityBase StartCastle;
    private static EntityBaseSP player;

    private static List<Terrain> terrain;
    private static List<String> blendmapPath;
    private static List<String> heightmapPath;

    private static Vector3f deltaPos;

    private static boolean fullbright = false;


    //Encapsulated Objects
    private static Loader loader;
    private static Texture texture;
    private static TexturedModel texturedModel;

    //Sounds
    private static Sound theme;
    private static Sound goldSound;
    private static Sound stepSound;
    private static Sound winSound;


    private static FontType fontType;
    private static GUIText text;
    private static Camera camera;
    private static Light light;
    private static MainTesselator tesselator;
    private static boolean debug = false;
    private static boolean hack = false;



    //Main funktionen (Den som körs vid start av program)
    public static void main(String[] args){
        loader = new Loader();

        //Vid start av program så kallas metoden onLaunch()
        onLaunch();

        //Medans displayen inte är nedstängd så uppdaterar jag den i metoden onUpdate
        while (!Display.isCloseRequested()){
            onUpdate();
        }

        //Efter displayen är nedstägd så körs metoden onClose()
        onClose();



    }

    private static void setIcon(){
            if (OsUtil.get() != OsUtil.MAC) {
                try (InputStream inputStream16x = new FileInputStream("res/icon/icon_16x16.png");
                     InputStream inputStream32x = new FileInputStream("res/icon/icon_32x32.png")) {
                    ByteBuffer[] icons = new ByteBuffer[]{IconUtil.readImageToBuffer(inputStream16x), IconUtil.readImageToBuffer(inputStream32x)};
                    Display.setIcon(icons);
                } catch (Exception e) {
                    Logger.print("Couldnt set icon");
                }
            }

    }

    private static void onLaunch(){
        ContextAttribs attribs = new ContextAttribs(3,2).withForwardCompatible(true).withProfileCore(true).withDebug(DebugUtil.runningFromIntelliJ());
        try{
            //Sätter displayens dimensioner
            Display.setDisplayMode(new DisplayMode((int) width, (int) height));
            //Sätter titel på display
            Display.setTitle(GameTitle);
            //Sätter på VSync
            Display.setVSyncEnabled(true);

            setIcon();

            //Skapar displayen
            Display.create();
        } catch (LWJGLException e) {
            throw new RuntimeException(e);
        }
        tesselator = new MainTesselator();
        camera = new Camera();
        camera.setPosition(new Vector3f(1, 10, 1));
        light = new Light(new Vector3f(0,500,20), new Vector3f(1,1,1));

        blendmapPath= new ArrayList<>();
        blendmapPath.add("terrain/blendmap/blendmap0");
        blendmapPath.add("terrain/blendmap/blendmap1");
        blendmapPath.add("terrain/blendmap/blendmap2");
        blendmapPath.add("terrain/blendmap/blendmap3");

        heightmapPath=new ArrayList<>();
        heightmapPath.add("terrain/heightmap/heightmap0");
        heightmapPath.add("terrain/heightmap/heightmap1");
        heightmapPath.add("terrain/heightmap/heightmap2");
        heightmapPath.add("terrain/heightmap/heightmap4");


        terrain = new ArrayList<>();
        for(int i =-2;i<=2;i++){
            for(int z =-2;z<=2;z++){
                TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("terrain/texture/slot0"));
                TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("terrain/texture/ground2"));
                TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("terrain/texture/slot3"));
                TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("terrain/texture/slot4"));
                TerrainTextureContainer textureContainer = new TerrainTextureContainer(backgroundTexture, rTexture, gTexture, bTexture);
                Random rand = new Random();

                TerrainTexture blendMap = new TerrainTexture(loader.loadTexture(blendmapPath.get(rand.nextInt(blendmapPath.size()))));

                terrain.add(new Terrain(z,i, loader,textureContainer, blendMap, heightmapPath.get(rand.nextInt(heightmapPath.size()))));
            }

        }

        house = ObjectLoader.loadObjModel("museum", loader);
        houseTex = new Texture(loader.loadTexture("museumTex"));
        texHouse = new TexturedModel(houseTex, house);
        houseEntity = new EntityBase(texHouse, -30,0,0, new Vector3f(0.1f,0.1f,0.1f),new Vector3f(0,-77,-100), true, false, 10f);



        TexturedGold = new TexturedModel(new Texture(loader.loadTexture("gold")), ObjectLoader.loadObjModel("gold", loader));
        gold = new ArrayList<>();
        for(int i=0;i<40;i++){
            Random random = new Random();
            EntityBase newbox = new EntityBase(TexturedGold,0,0,0, new Vector3f(0.3f,0.3f,0.3f), new Vector3f(random.nextInt(3000)-1000,0,random.nextInt(3000)-1000), true , true,10f);
            newbox.setPosition(new Vector3f(newbox.getPosition().x, EngineCore.getHeightRightTerrain(terrain, newbox),newbox.getPosition().z));
            gold.add(newbox);
        }

        player = new EntityBaseSP(new TexturedModel(new Texture(loader.loadTexture("char")), ObjectLoader.loadObjModel("char", loader)),0,0,0, new Vector3f(1,1,1), new Vector3f(1,20,1 ));
        player.setCamera(camera);

        TexturedTree = new TexturedModel(new Texture(loader.loadTexture("tree")), ObjectLoader.loadObjModel("tree", loader));
        trees = new ArrayList<>();
        for(int i=0; i<600;i++){
            Random random = new Random();
            EntityBase newbox = new EntityBase(TexturedTree,0,0,0, new Vector3f(1,1,1), new Vector3f(random.nextInt(3000)-1000,0,random.nextInt(3000)-1000), true, false,10f );
            newbox.setPosition(new Vector3f(newbox.getPosition().x, EngineCore.getHeightRightTerrain(terrain, newbox),newbox.getPosition().z));
            trees.add(newbox);
        }




        TexturedGrass = new TexturedModel(new Texture(loader.loadTexture("grassTexture")), ObjectLoader.loadObjModel("grassModel", loader));
        grass = new ArrayList<>();
        for(int i=0;i<3000;i++){
            Random random = new Random();
            EntityBase newbox = new EntityBase(TexturedGrass,0,0,0, new Vector3f(2,2,2), new Vector3f(random.nextInt(3000)-1000,0,random.nextInt(3000)-1000),false , true, 0f);
            newbox.setPosition(new Vector3f(newbox.getPosition().x, EngineCore.getHeightRightTerrain(terrain, newbox),newbox.getPosition().z));

            grass.add(newbox);
        }
        theme=new Sound("theme");
        goldSound=new Sound("gold");
        stepSound=new Sound("step");
        winSound=new Sound("win");
        try{
            theme.playSound( true);
            stepSound.playSound(false);
            stepSound.stopSound();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        TextMaster.init(loader);
        fontType= new FontType(loader.loadTexture("font/harrington"), new File("res/texture/font/harrington.fnt"));
         text = new GUIText("Go and find gold!", 5,fontType, new Vector2f(0,0),1f, true);

        text.setColour(0, 0, 0);

        try {
            Class cls = Class.forName("me.pvpb0t.EngineCore");
            ClassPath classPath = ClassPath.from(cls.getClassLoader());
            Set<ClassPath.ClassInfo> classes = classPath.getAllClasses();
            for(ClassPath.ClassInfo clazz : classes){
                if(clazz.getName().contains("pvpb0t")){
                    Logger.print("Loaded native class: "+clazz.toString());
                }
            }
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }

        Logger.print(String.format("Launch sequence took %s milliseconds.", System.nanoTime() / 1000000L - startTime));
    }

    private static void onUpdate(){
        //Uppdaterar displayen
        Display.update();
        //Synkar den med mitt FPS värde
        Display.sync(FPS);
        tick++;
        EntityBase closestEntity = null;




        while (Keyboard.next()){
            if(Keyboard.getEventKey() == Keyboard.KEY_F12){
                if(Keyboard.getEventKeyState()){
                    debug=!debug;
                    fullbright=!fullbright;
                    Logger.print("Debug :" + debug);

                }
            }
            if(Keyboard.getEventKey() == Keyboard.KEY_HOME){
                if(Keyboard.getEventKeyState()){
                    hack = !hack;
                    Logger.print("Hack :" + hack);

                }
            }
        }



        int mouse = Mouse.getDWheel();
        if(mouse != 0){
            if(mouse>0){
                tesselator.addFOV(-3.1f);
            }else{
                tesselator.addFOV(3.1f);

            }
            tesselator = new MainTesselator();
        }

        //RotationUtil.RotateEntityBase(houseEntity, 0,1,0);



        if(fullbright){
            light.setPosition(camera.getPosition());
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_F1)){
            light.setPosition(new Vector3f(1, light.getPosition().y-8 , 1));
        }else if(Keyboard.isKeyDown(Keyboard.KEY_F2)){
            light.setPosition(new Vector3f(1, light.getPosition().y+8 , 1));
        }



        if(!debug) {
            //Change 9 to current height of terrain + 9

            float height = EngineCore.getHeightRightTerrain(terrain, player);

            if(player.getPosition().y<9+height-1){
                player.addY((height-player.getPosition().y+9)/8,0f);
                player.setMotionX(0.2f);
                player.setMotionZ(0.2f);

            }
            if (player.getPosition().y > 9+height) {
                player.Gravity(0.02f, 1);
                player.setIsfalling(true);
            } else {
                player.setMotionY(0);
                player.setIsfalling(false);

            }
        }



        float closest=1000f;
            for(EntityBase entityBase: EntityManager.getEntityBases()) {
                if(entityBase instanceof EntityBaseSP){

                }else {
                    if(closest>player.getDistance(player.getPosition(), entityBase.getPosition())){
                        closest=player.getDistance(player.getPosition(), entityBase.getPosition());
                        closestEntity = entityBase;
                    }
                    if (player.getDistance(player.getPosition(), entityBase.getPosition()) < entityBase.getRadius()) {
                        if(gold.contains(entityBase) && !foundGolds.contains(entityBase)){
                            gold.remove(entityBase);
                            foundGolds.add(entityBase);
                            Logger.print("Picked up gold unit: " + foundGolds.size());
                            try{
                                goldSound.playSound(false);
                                int maxGold= foundGolds.size() + gold.size();
                                text.remove();
                                if(gold.isEmpty()){
                                    winSound.playSound(false);
                                    text = new GUIText("You found all gold, now mystical tree happy!", 5,fontType, new Vector2f(0,0),1f, true);
                                }else{
                                    text = new GUIText("Gold: " + foundGolds.size() +" out of " + maxGold, 5,fontType, new Vector2f(0,0),1f, true);
                                }
                                text.setColour(0, 0, 0);

                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                }

            }
        if(player.isMoved()){
            if(!stepSound.getClip().isActive()){
                try {
                    stepSound.playSound(false);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            player.setMoved(false);
        }


        if(closestEntity!=null) {
            if ((player.getDistance(new Vector3f(player.getPosition().x, 0, player.getPosition().z), closestEntity.getPosition()) < closestEntity.getRadius() / 2) && (!closestEntity.isPassThrough())) {
                stuckTick++;
            } else {
                stuckTick = 0;
            }

            player.move(closestEntity);
        }else{
            player.setPosition(new Vector3f(0, 20 ,0));
        }

        if(!player.isMoved()){
            stepSound.stopSound();
        }



        //Get player location to setBack incase collision
        /*
        List<Entity> possibleCollision;
        if(distance < radius) {
        possibleCollision.add(entity);
        }


        possibleColliosn.clear();
         */
        player.allignCamera();



        camera.setPosition(new Vector3f(player.getPosition().x, debug?player.getPosition().y:player.getPosition().y+3, player.getPosition().z));
        /*if(entityBase.getPosition().x>1.5f){
            entityBase.setPosition(new Vector3f(-1.5f,0,0));

        }



        RotationUtil.RotateEntityBase(entityBase, 0.9f, 0.55f, 0.34f);*/


        if(stuckTick>100){
            stuckTick=0;
            Logger.print("PlayerSP is Stuck -> Resetting Position");
            player.setPosition(new Vector3f(0, 20 ,0));
        }

        GL11.glEnable(GL11.GL_DEPTH_TEST);
       tesselator.processEntityBase(houseEntity);
       if(gold.size() != 0) {
           for (EntityBase goldz : gold) {
               tesselator.processEntityBase(goldz);

           }
       }
       tesselator.processEntityBase(player);

       for(Terrain terrain1 : terrain){
           tesselator.proccessTerrain(terrain1);
       }
        for(EntityBase entityBase1 : trees){
            tesselator.processEntityBase(entityBase1);
        }

        for(EntityBase grass : grass){
            tesselator.processEntityBase(grass);
        }
        if(gold.size()!=0) {
            if (tick % 3 == 0) {
                if (hack) {
                    EntityBase nextgold = gold.get(0);
                    player.setPosition(nextgold.getPosition());
                    player.move(nextgold);
                }
            }
        }

        if(tick % 30 == 0){

            if(debug){
                Logger.print("SplayerPos[" + player.getPosition().toString()+"]");
                Logger.print("SplayerRot[ pitch:" + player.getPitch() + " yaw:" + player.getYaw()+ "]");
                Logger.print("SplayerMotionY: " + player.getMotionY());
                Logger.print("Closets val: " + closest);
                Logger.print("StuckTick val: " + stuckTick);

            }
            //Game logic UPDATE
        }
        tesselator.render(light,camera);
        TextMaster.render();

        deltaPos = player.getPosition();


    }

    public static void onClose(){
        Logger.print("Uninitialized B0TNET");
        Logger.writeToLog();
        //Stänger ner Displayen
        tesselator.flush();
        loader.flushGL();
        Display.destroy();
    }

     public static float getHeightRightTerrain(List<Terrain> terrains, EntityBase entityBase){
         float height =0;
         for(Terrain terrain1: terrains){
             // Logger.print(terrain1.getHeightOffset(player.getPosition().x, player.getPosition().z));
             if(terrain1.getHeightOfTerrain(entityBase.getPosition().x, entityBase.getPosition().z)>height){
                 height=terrain1.getHeightOfTerrain(entityBase.getPosition().x, entityBase.getPosition().z);
             }
         }
     return height;}

}


