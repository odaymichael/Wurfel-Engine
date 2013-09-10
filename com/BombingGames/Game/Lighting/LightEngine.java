package com.BombingGames.Game.Lighting;

import com.BombingGames.EngineCore.Controller;
import com.BombingGames.EngineCore.Map.Chunk;
import com.BombingGames.EngineCore.Map.Map;
import com.BombingGames.EngineCore.View;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;


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
    //diagramm data
    private int posX = 250;
    private int posY = 250;
    private final int size = 500;
    
    //ambient light
    private final float ambientBaseLevel = .5f;//value 0-1
    private float k_ambient = 0.32f;//material constant, value 0-1
    private float I_ambient;//ambient light
    
    //diffuse light
    private final float k_diff = 60/255f; //the min and max span. value between 0 and 1 empirisch bestimmter Reflexionsfaktor für diffuse Komponente der Reflexion
    private float I_diff0, I_diff1, I_diff2;
    
    //specular light
    private final int n_spec = 8; // ... konstanter Faktor zur Beschreibung der Oberflächenbeschaffenheit (rau kleiner 32, glatt größer 32,  infinity wäre ein perfekter Spiegel)
    private final int k_specular = 8; //empirisch bestimmter Reflexionsfaktor für spiegelnde Komponente der Reflexion
    private float I_spec0, I_spec1, I_spec2;
             
    /**the brightness of each side. The value should be between 0 and 1*/
    private static float I_0, I_1, I_2;
    
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
        float I_a = ambientBaseLevel + sun.getBrightness() + moon.getBrightness();
        I_ambient = I_a * k_ambient;
                
        //diffusion
        I_diff0 = (float) (sun.getBrightness() *  k_diff * Math.cos(((sun.getLatPos())*Math.PI)/180) * Math.cos(((sun.getLongPos()-45)*Math.PI)/180));  
        I_diff0 += moon.getBrightness() * k_diff * Math.cos(((moon.getLatPos())*Math.PI)/180) * Math.cos(((moon.getLongPos()-45)*Math.PI)/180);
        
        I_diff1 = (float) (sun.getBrightness() * k_diff * Math.cos(((sun.getLatPos()-90)*Math.PI)/180));     
        I_diff1 += moon.getBrightness() * k_diff * Math.cos(((moon.getLatPos()-90)*Math.PI)/180);   
        
        I_diff2 = (float) (sun.getBrightness() * k_diff * Math.cos(((sun.getLatPos())*Math.PI)/180)*Math.cos(((sun.getLongPos()-135)*Math.PI)/180));
        I_diff2 += moon.getBrightness()  * k_diff * Math.cos(((moon.getLatPos())*Math.PI)/180)*Math.cos(((moon.getLongPos()-135)*Math.PI)/180);
        
        //specular
        
        //it is impossible to get specular light with a GlobalLightSource over the horizon on side 0 and 2. Just left in case i it someday helps somebody.
//        I_spec0 =(int) (
//                        sun.getBrightness()
//                        * k_specular
//                        * Math.pow(
//                            Math.sin((sun.getLatPos())*Math.PI/180)*Math.sin((sun.getLongPos())*Math.PI/180)* Math.sqrt(2)/Math.sqrt(3)//y
//                          + Math.sin((sun.getLatPos()-75)*Math.PI/180)/Math.sqrt(3)//z
//                        ,n_spec)
//                        *(n_spec+2)/(2*Math.PI)
//                        );

        
        I_spec1 = (float) (
                            sun.getBrightness()
                            * k_specular
                            * Math.pow(
                                Math.sin(sun.getLatPos()*Math.PI/180)*Math.sin(sun.getLongPos()*Math.PI/180)/ Math.sqrt(2)//y
                              + Math.sin((sun.getLatPos()-90)*Math.PI/180)/ Math.sqrt(2)//z
                            ,n_spec)
                            *(n_spec+2)/(2*Math.PI)
                        );
         I_spec1 +=(float) (
                        moon.getBrightness()
                        * k_specular
                        * Math.pow(
                            Math.sin((moon.getLatPos())*Math.PI/180)*Math.sin((moon.getLongPos())*Math.PI/180)/Math.sqrt(2)//y
                          + Math.sin((moon.getLatPos()-90)*Math.PI/180)/Math.sqrt(2)//z
                        ,n_spec)
                        *(n_spec+2)/(2*Math.PI)
                        );
      //it is impossible to get specular light with a GlobalLightSource over the horizon on side 0 and 2. Just left in case i it someday helps somebody.
        //        I_spec2 =(int) (
        //                        sun.getBrightness()
        //                        * k_specular
        //                        * Math.pow(
        //                            Math.cos((sun.getLatPos() - 35.26) * Math.PI/360)
        //                           *Math.cos((sun.getLongPos() + 180) * Math.PI/360)
        //                        ,n_spec)
        //                        *(n_spec+2)/(2*Math.PI)
        //                        );   
        
        I_0 = I_ambient + I_diff0 + I_spec0;
        I_1 = I_ambient + I_diff1 + I_spec1;
        I_2 = I_ambient + I_diff2 + I_spec2;
    }
    
    /**
     * Returns the average brightness.
     * @return
     */
    public static float getBrightness() {
        return (I_0+I_1+I_2)/3f;
    }
        
    /**
     * 
     * @param side
     * @return
     */
    public static float getBrightness(int side){
        if (side==0) return I_0;
            else if (side==1) return I_1;
                else return I_2;
    }
    
    /**
     * Returns the global light color.
     * @return
     */
    public Color getLightColor(){
        Color tmp = sun.getLight().cpy();
        tmp.add(moon.getLight());
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
                while (Controller.getMap().getData(x,y,topmost).isTransparent() && topmost > 0 ){
                    topmost--;
                }
                
                if (topmost>0) {
                    //start at topmost renderobject and go down. Every step make it a bit darker
                    for (int level = topmost; level > -1; level--){
                        Controller.getMap().getData(x,y,level).setLightlevel(.25f + .25f*level / (float) topmost);
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
    public boolean isRenderingData() {
        return renderData;
    }

    /**
     *Should diagrams be rendered showing the data of the LE.
     * @param render
     */
    public void RenderData(boolean render) {
        this.renderData = render;
    }
    
    
    
    /**
     *Shows the data of the light engine in diagramms.
     * @param view 
     */
    public void render(View view){
        if (renderData) {
            
            //sun position
            //g.setLineWidth(2);
            ShapeRenderer shapeRenderer = view.getShapeRenderer();
            //longitude
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.begin(ShapeType.Line);
            shapeRenderer.line(
                posX +(int) (size* Math.sin((sun.getLongPos()-90)*Math.PI/180)),
                posY +(int) (size/2*Math.cos((sun.getLongPos()-90)*Math.PI/180)),
                posX,
                posY
            );
            
            //latitude
            shapeRenderer.setColor(Color.MAGENTA);
            shapeRenderer.line(
                posX +(int) (size * Math.sin((sun.getLatPos()-90)*Math.PI/180)),
                posY -(int) (size/2*Math.sin((sun.getLatPos())*Math.PI/180)),
                posX,
                posY
            );
            
            //long+lat of sun position
            shapeRenderer.setColor(Color.YELLOW);
            shapeRenderer.line(
                posX +(int) ( size*Math.sin((sun.getLongPos()+90)*Math.PI/180) * Math.sin((sun.getLatPos()-90)*Math.PI/180) ),
                posY -(int) ( size/2*Math.sin((sun.getLongPos())*Math.PI/180) * Math.sin((sun.getLatPos()-90)*Math.PI/180)) -(int) (size/2*Math.sin((sun.getLatPos())*Math.PI/180)),
                posX,
                posY
             );

            shapeRenderer.setColor(Color.BLUE);
            shapeRenderer.line(
                posX +(int) ( size*Math.sin((moon.getLongPos()+90)*Math.PI/180) * Math.sin((moon.getLatPos()-90)*Math.PI/180) ),
                posY -(int) ( size/2*Math.sin((moon.getLongPos())*Math.PI/180) * Math.sin((moon.getLatPos()-90)*Math.PI/180)) -(int) (size/2*Math.sin((moon.getLatPos())*Math.PI/180)),
                posX,
                posY
             );
             shapeRenderer.end();

            view.drawString("Lat: "+sun.getLatPos(), 600, 110, Color.WHITE);
            view.drawString("Long: "+sun.getLongPos(), 600, 100, Color.WHITE);
            view.drawString("BrightnessSun: "+sun.getBrightness(), 600, 120, Color.WHITE);
            view.drawString("BrightnessMoon: "+moon.getBrightness(), 600, 130, Color.WHITE);
            view.drawString("LightColor: "+getLightColor().toString(), 600, 140, Color.WHITE);

             //info bars

            shapeRenderer.begin(ShapeType.FilledRectangle);
            
            //left side
            view.drawText("Left:"+I_ambient+"\n+"+I_diff0+"\n+"+ I_spec0+"\n="+I_0, (int) (I_0*size), 100, Color.WHITE);
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.filledRect(0, 100, I_ambient*size, 10);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.filledRect(I_ambient*size, 100, I_diff0*size, 8);
            shapeRenderer.setColor(Color.BLUE);
            shapeRenderer.filledRect((I_ambient+I_diff0)*size, 100, I_spec0*size, 6);

            //top side
            view.drawText("Top:"+I_ambient+"\n+"+I_diff1+"\n+"+ I_spec1+"\n="+I_1, (int) (I_1*size), 140, Color.WHITE);
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.filledRect(0, 140, I_ambient*size, 10);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.filledRect(I_ambient*size, 140, I_diff1*size, 8);
            shapeRenderer.setColor(Color.BLUE);
            shapeRenderer.filledRect((I_ambient+I_diff1)*size, 140, I_spec1*size, 6);

            //right side
            view.drawText("Right:"+I_ambient+"\n+"+I_diff2+"\n+"+ I_spec2+"\n="+I_2, (int) (I_2*size), 180, Color.WHITE);
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.filledRect(0, 180, I_ambient*size, 10);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.filledRect(I_ambient*size, 180, I_diff2*size, 8);
            shapeRenderer.setColor(Color.BLUE);
            shapeRenderer.filledRect((I_ambient+I_diff2)*size, 180, I_spec2*size, 6);
            
            shapeRenderer.end();
        }
    }
}
