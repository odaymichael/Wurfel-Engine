package com.BombingGames.Game.Lighting;

import com.BombingGames.EngineCore.Chunk;
import com.BombingGames.EngineCore.Map;
import com.BombingGames.EngineCore.View;
import com.badlogic.gdx.graphics.Color;


/**
 *This Light engine calculates phong shading for three normals over the day.
 * @author Benedikt Vogler
 */
public class LightEngine {
    /**
     * The Version of the light engine.
     */
    public static final String Version = "1.0.2";
    
    private boolean renderData = false;
    private int posX = 250;
    private int posY = 250;
    private final int size = 500;
    
    //ambient light
    private final int ambientBaseLevel = 40;//value 0-255
    private float k_ambient = 0.32f;//material constant, value 0-1
    private int I_ambient;//ambient light
    
    //diffuse light
    private final int k_diff = 60; //the min and max span. value between 0 and 255 empirisch bestimmter Reflexionsfaktor für diffuse Komponente der Reflexion
    private int I_diff0;
    private int I_diff1;
    private int I_diff2;
    
    //specular light
    private int n_spec = 8; // ... konstanter Faktor zur Beschreibung der Oberflächenbeschaffenheit (rau kleiner 32, glatt größer 32,  wäre ein perfekter Spiegel)
    private int k_specular = 8; //empirisch bestimmter Reflexionsfaktor für spiegelnde Komponente der Reflexion
    private int I_spec0;
    private int I_spec1;
    private int I_spec2;
                
    private static int I_0;
    private static int I_1;
    private static int I_2;
    
    private Sun sun;
    private Moon moon; 

    /**
     * 
     */
    public LightEngine() {
        sun = new Sun();
        moon = new Moon();
    }

    public LightEngine(int xPos, int yPos) {
        sun = new Sun();
        moon = new Moon();
        this.posX = xPos;
        this.posY = yPos;
    }
    
    
    
    /**
     * 
     * @param delta
     */
    public void update(int delta) {
        sun.update(delta);
        moon.update(delta);
        
        
        //light intensitiy of enviroment. the normal level. value between 0 and 255
        int I_a = (int) (ambientBaseLevel + sun.getBrightness()*255 + moon.getBrightness()*255);
        I_ambient = (int) (I_a * k_ambient);
                
        //diffusion
        I_diff0 = (int) (sun.getBrightness() *  k_diff * Math.cos(((sun.getLatPos())*Math.PI)/180) * Math.cos(((sun.getLongPos()-45)*Math.PI)/180));  
        I_diff0 +=(int) (moon.getBrightness() * k_diff * Math.cos(((moon.getLatPos())*Math.PI)/180) * Math.cos(((moon.getLongPos()-45)*Math.PI)/180));
        
        I_diff1 = (int) (sun.getBrightness() * k_diff * Math.cos(((sun.getLatPos()-90)*Math.PI)/180));     
        I_diff1 += (int) (moon.getBrightness() * k_diff * Math.cos(((moon.getLatPos()-90)*Math.PI)/180));   
        
        I_diff2 = (int) (sun.getBrightness() * k_diff * Math.cos(((sun.getLatPos())*Math.PI)/180)*Math.cos(((sun.getLongPos()-135)*Math.PI)/180));
        I_diff2 += (int) (moon.getBrightness()  * k_diff * Math.cos(((moon.getLatPos())*Math.PI)/180)*Math.cos(((moon.getLongPos()-135)*Math.PI)/180));
        
        //specular
        
        //it is impossible to get specular with a GLS over the horizon on side 0 and 2. Just left here if it someday helps.
//        I_spec0 =(int) (
//                        sun.getBrightness()
//                        * k_specular
//                        * Math.pow(
//                            Math.sin((sun.getLatPos())*Math.PI/180)*Math.sin((sun.getLongPos())*Math.PI/180)* Math.sqrt(2)/Math.sqrt(3)//y
//                          + Math.sin((sun.getLatPos()-75)*Math.PI/180)/Math.sqrt(3)//z
//                        ,n_spec)
//                        *(n_spec+2)/(2*Math.PI)
//                        );

        
        I_spec1 =(int) (
                        sun.getBrightness()
                        * k_specular
                        * Math.pow(
                            Math.sin(sun.getLatPos()*Math.PI/180)*Math.sin(sun.getLongPos()*Math.PI/180)/ Math.sqrt(2)//y
                          + Math.sin((sun.getLatPos()-90)*Math.PI/180)/ Math.sqrt(2)//z
                        ,n_spec)
                        *(n_spec+2)/(2*Math.PI)
                        );
         I_spec1 +=(int) (
                        moon.getBrightness()
                        * k_specular
                        * Math.pow(
                            Math.sin((moon.getLatPos())*Math.PI/180)*Math.sin((moon.getLongPos())*Math.PI/180)/Math.sqrt(2)//y
                          + Math.sin((moon.getLatPos()-90)*Math.PI/180)/Math.sqrt(2)//z
                        ,n_spec)
                        *(n_spec+2)/(2*Math.PI)
                        );
      //it is impossible to get specular with a GLS over the horizon on side 0 and 2. Just left here if it someday helps.
        //        I_spec2 =(int) (
        //                        sun.getBrightness()
        //                        * k_specular
        //                        * Math.pow(
        //                            Math.cos((sun.getLatPos() - 35.26) * Math.PI/360)
        //                           *Math.cos((sun.getLongPos() + 180) * Math.PI/360)
        //                        ,n_spec)
        //                        *(n_spec+2)/(2*Math.PI)
        //                        );   
        
        I_0 = (int) (I_ambient + I_diff0 + I_spec0);
        I_1 = (int) (I_ambient + I_diff1 + I_spec1);
        I_2 = (int) (I_ambient + I_diff2 + I_spec2);
    }
    
    /**
     * Returns the average brightness.
     * @return
     */
    public static int getBrightness() {
        return (I_0+I_1+I_2)/3;
    }
        
    /**
     * 
     * @param side
     * @return
     */
    public static int getBrightness(int side){
        if (side==0) return I_0;
            else if (side==1) return I_1;
                else return I_2;
    }
    
    /**
     * Returns the global lightcolor.
     * @return
     */
    public Color getLightColor(){
        Color tmp = sun.getColor().cpy().mul(sun.getBrightness());
        tmp.add(moon.getColor().cpy().mul(moon.getBrightness()));
        tmp.a = 1;
        return tmp;
    }
    
     /**
     * Calculates the light level based on the sun shining straight from the top
     */
    public static void calcSimpleLight(){
        for (int x=0; x < Map.getBlocksX(); x++){
            for (int y=0; y < Map.getBlocksY(); y++) {
                
                //find top most renderobject
                int topmost = Chunk.getBlocksZ()-1;
                while (Map.getData(x,y,topmost).isTransparent() && topmost > 0 ){
                    topmost--;
                }
                
                if (topmost>0) {
                    //start at topmost renderobject and go down. Every step make it a bit darker
                    for (int level = topmost; level > -1; level--){
                        Map.getData(x,y,level).setLightlevel(63 + 64 * level / topmost);
                    }
                }
            }
        }         
    }

    /**
     *
     * @return
     */
    public Sun getSun() {
        return sun;
    }

    /**
     *
     * @return
     */
    public Moon getMoon() {
        return moon;
    }

    /**
     *
     * @return
     */
    public boolean getRenderPosition() {
        return renderData;
    }

    /**
     *
     * @param renderData
     */
    public void RenderData(boolean renderPosition) {
        this.renderData = renderPosition;
    }
    
    
    
    /**
     *
     * @param g
     */
    public void render(View view){
        if (renderData) {
            //sun position
            //g.setLineWidth(2);

            //longitude
            //g.setColor(Color.red);
//            g.drawLine(
//                posX +(int) (size* Math.sin((sun.getLongPos()-90)*Math.PI/180)),
//                posY +(int) (size/2*Math.cos((sun.getLongPos()-90)*Math.PI/180)),
//                posX,
//                posY
//            );
//
//            //latitude
//            g.setColor(Color.magenta);
//            g.drawLine(
//                posX +(int) (size * Math.sin((sun.getLatPos()-90)*Math.PI/180)),
//                posY -(int) (size/2*Math.sin((sun.getLatPos())*Math.PI/180)),
//                posX,
//                posY
//            );
//
//            //long+lat of sun position
//            g.setColor(Color.yellow);
//            g.drawLine(
//                posX +(int) ( size*Math.sin((sun.getLongPos()+90)*Math.PI/180) * Math.sin((sun.getLatPos()-90)*Math.PI/180) ),
//                posY -(int) ( size/2*Math.sin((sun.getLongPos())*Math.PI/180) * Math.sin((sun.getLatPos()-90)*Math.PI/180)) -(int) (size/2*Math.sin((sun.getLatPos())*Math.PI/180)),
//                posX,
//                posY
//             );
//            
//            g.setColor(Color.blue);
//            g.drawLine(
//                posX +(int) ( size*Math.sin((moon.getLongPos()+90)*Math.PI/180) * Math.sin((moon.getLatPos()-90)*Math.PI/180) ),
//                posY -(int) ( size/2*Math.sin((moon.getLongPos())*Math.PI/180) * Math.sin((moon.getLatPos()-90)*Math.PI/180)) -(int) (size/2*Math.sin((moon.getLatPos())*Math.PI/180)),
//                posX,
//                posY
//             );
//
//            g.setColor(Color.white);
//            g.drawString("Lat: "+sun.getLatPos(), 400, 110);
//            g.drawString("Long: "+sun.getLongPos(), 400, 100);
//            g.drawString("BrightnessSun: "+sun.getBrightness(), 400, 120);
//            g.drawString("BrightnessMoon: "+moon.getBrightness(), 400, 130);
//            g.drawString("Long: "+getLightColor().toString(), 400, 140);
//            
//             //info bars
//        
//            //left side
//            g.setColor(Color.white);
//            g.drawString("Left:"+I_ambient+"+"+I_diff0+"+"+ I_spec0+"="+I_0, I_0, 100);
//            g.setColor(Color.green);
//            g.fillRect(0, 100, I_ambient, 10);
//            g.setColor(Color.red);
//            g.fillRect(I_ambient, 100, I_diff0, 8);
//            g.setColor(Color.blue);
//            g.fillRect(I_ambient+I_diff0, 100, I_spec0, 6);
//
//            //top side
//            g.setColor(Color.white);
//            g.drawString("Top:"+I_ambient+"+"+I_diff1+"+"+ I_spec1+"="+I_1, I_1, 120);
//            g.setColor(Color.green);
//            g.fillRect(0, 120, I_ambient, 10);
//            g.setColor(Color.red);
//            g.fillRect(I_ambient, 120, I_diff1, 8);
//            g.setColor(Color.blue);
//            g.fillRect(I_ambient+I_diff1, 120, I_spec1, 6);
//
//            //right side
//            g.setColor(Color.white);
//            g.drawString("Right:"+I_ambient+"+"+I_diff2+"+"+ I_spec2+"="+I_2, I_2, 140);
//            g.setColor(Color.green);
//            g.fillRect(0, 140, I_ambient, 10);
//            g.setColor(Color.red);
//            g.fillRect(I_ambient, 140, I_diff2, 8);
//            g.setColor(Color.blue);
//            g.fillRect(I_ambient+I_diff2, 140, I_spec2, 6);
        }
    }
}
