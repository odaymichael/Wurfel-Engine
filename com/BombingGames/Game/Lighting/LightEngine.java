package com.BombingGames.Game.Lighting;

import org.newdawn.slick.Color;

/**
 *This Light engine calculates phong shading for three normals over the day.
 * @author Benedikt Vogler
 */
public class LightEngine {
    /**
     * The Version of the light engine.
     */
    public static final String Version = "0.1.2";
    
    //ambient light
    private final int ambientBaseLevel = 40;//value 0-255
    private float k_ambient = 0.32f;//material constant, value 0-1
    private int I_ambient;//ambient light
    
    //diffuse light
    private final int k_diff = 48; //the min and max span. value between 0 and 255 empirisch bestimmter Reflexionsfaktor für diffuse Komponente der Reflexion
    private int I_diff0;
    private int I_diff1;
    private int I_diff2;
    
    //specular light
    private int n_spec = 8; // ... konstanter Faktor zur Beschreibung der Oberflächenbeschaffenheit (rau kleiner 32, glatt größer 32,  wäre ein perfekter Spiegel)
    private int k_specular = 25; //empirisch bestimmter Reflexionsfaktor für spiegelnde Komponente der Reflexion
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
    
    
    
    /**
     * 
     * @param delta
     */
    public void update(int delta) {
        sun.update(delta);
        moon.update(delta);
        
        
        int I_a; //light intensitiy of enviroment. the normal level. value between 0 and 255
        I_a = (int) (ambientBaseLevel + sun.getBrightness()*255 + moon.getBrightness()*255);
        I_ambient = (int) (I_a * k_ambient);
                
        //diffusion
        I_diff0 = (int) (sun.getBrightness() * k_diff * Math.cos(((sun.getLatPos()-90)*Math.PI)/180) * Math.cos(((sun.getLongPos()-45)*Math.PI)/180));  
        I_diff0 +=(int) (moon.getBrightness() * k_diff * Math.cos(((moon.getLatPos()-90)*Math.PI)/180) * Math.cos(((moon.getLongPos()-45)*Math.PI)/180));
        
        I_diff1 = (int) (sun.getBrightness() * k_diff * Math.cos(((sun.getLatPos())*Math.PI)/180));     
        I_diff1 += (int) (moon.getBrightness() * k_diff * Math.cos(((moon.getLatPos())*Math.PI)/180));   
        
        I_diff2 = (int) (sun.getBrightness() * k_diff * Math.cos(((sun.getLatPos()-90)*Math.PI)/180)*Math.cos(((sun.getLongPos()-135)*Math.PI)/180));
        I_diff2 += (int) (moon.getBrightness() * k_diff * Math.cos(((moon.getLatPos()-90)*Math.PI)/180)*Math.cos(((moon.getLongPos()-135)*Math.PI)/180));
        
        //specular
        I_spec0 =(int) (
                        sun.getBrightness()
                        * k_specular
                        * Math.pow(
                            Math.cos((sun.getLatPos() - 125.26) * Math.PI/360)
                           *Math.cos((sun.getLongPos()) * Math.PI/360)
                        ,n_spec)
                        *(n_spec+2)/(2*Math.PI)
                        );
        I_spec0 +=(int) (
                        moon.getBrightness()
                        * k_specular
                        * Math.pow(
                            Math.cos((moon.getLatPos() - 125.26) * Math.PI/360)
                           *Math.cos((moon.getLongPos()) * Math.PI/360)
                        ,n_spec)
                        *(n_spec+2)/(2*Math.PI)
                        );
        
        I_spec1 =(int) (
                        sun.getBrightness()
                        * k_specular
                        * Math.pow(
                            Math.cos((sun.getLatPos() + 45) * Math.PI/360)
                           *Math.cos((sun.getLongPos() + 90) * Math.PI/360)
                        ,n_spec)
                        *(n_spec+2)/(2*Math.PI)
                        );
        I_spec1 +=(int) (
                        moon.getBrightness()
                        * k_specular
                        * Math.pow(
                            Math.cos((moon.getLatPos() + 45) * Math.PI/360)
                           *Math.cos((moon.getLongPos() + 90) * Math.PI/360)
                        ,n_spec)
                        *(n_spec+2)/(2*Math.PI)
                        );
        I_spec2 =(int) (
                        sun.getBrightness()
                        * k_specular
                        * Math.pow(
                            Math.cos((sun.getLatPos() - 125.26) * Math.PI/360)
                           *Math.cos((sun.getLongPos() + 180) * Math.PI/360)
                        ,n_spec)
                        *(n_spec+2)/(2*Math.PI)
                        );  
         I_spec2 +=(int) (
                        moon.getBrightness()
                        * k_specular
                        * Math.pow(
                            Math.cos((moon.getLatPos() - 125.26) * Math.PI/360)
                           *Math.cos((moon.getLongPos() + 180) * Math.PI/360)
                        ,n_spec)
                        *(n_spec+2)/(2*Math.PI)
                        );  
        
        I_0 = (int) (I_ambient + I_diff0 + I_spec0);
        I_1 = (int) (I_ambient + I_diff1 + I_spec1);
        I_2 = (int) (I_ambient + I_diff2 + I_spec2);
    }
    
    /**
     * 
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
        return sun.getColor();
    }
}
