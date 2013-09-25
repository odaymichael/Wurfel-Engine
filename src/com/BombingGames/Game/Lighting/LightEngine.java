package com.BombingGames.Game.Lighting;

import com.BombingGames.EngineCore.Controller;
import com.BombingGames.EngineCore.Map.Chunk;
import com.BombingGames.EngineCore.Map.Map;
import com.BombingGames.EngineCore.View;
import com.badlogic.gdx.Gdx;
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
    public static final String Version = "1.1.2";
    
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
    private final float k_diff = 100/255f; //the min and max span. value between 0 and 1 empirisch bestimmter Reflexionsfaktor für diffuse Komponente der Reflexion
    private float I_diff0, I_diff1, I_diff2;
    
    //specular light
    private final int n_spec = 12; //  constant factor describing the Oberflächenbeschaffenheit (rau smaller 32, glatt bigger 32, infinity would be a perfect mirror)
    private final float k_specular = 1-k_diff; //empirisch bestimmter reflection factor of mirroring component of reflection. Value "k_diff+kspecular <= 1" therefore 1-k_diff is biggest possible value 
    private float I_spec0, I_spec1, I_spec2;
             
    /**the brightness of each side. The value should be between 0 and 1*/
    private static float I_0, I_1, I_2;
    
    private GlobalLightSource sun;
    private GlobalLightSource moon; 

    /**
     * 
     */
    public LightEngine() {
        sun = new GlobalLightSource(-Controller.getMap().getWorldSpinDirection(), 0, new Color(255, 255, 255, 1), 60);
        moon = new GlobalLightSource(180-Controller.getMap().getWorldSpinDirection(), 0, new Color(0.2f,0.4f,0.8f,1), 45);
    }

    /**
     *
     * @param xPos
     * @param yPos
     */
    public LightEngine(int xPos, int yPos) {
        this();
        this.posX = xPos;
        this.posY = yPos;
    }
    
    
    
    /**
     * 
     * @param delta
     */
    public void update(float delta) {
        sun.update(delta);
        moon.update(delta);
        
        float sunlightBrightness = PseudoGrey.toFloat(sun.getLight());
        float moonlightBrightness = PseudoGrey.toFloat(moon.getLight());
        
        //light intensitiy of enviroment. the normal level. value between 0 and 1
        I_ambient = (ambientBaseLevel + sunlightBrightness + moonlightBrightness) * k_ambient;
                
        //diffusion
        I_diff0 = (float) (sunlightBrightness *  k_diff * Math.cos(((sun.getHeight())*Math.PI)/180) * Math.cos(((sun.getAzimuth()-45)*Math.PI)/180));  
        I_diff0 += moonlightBrightness * k_diff * Math.cos(((moon.getHeight())*Math.PI)/180) * Math.cos(((moon.getAzimuth()-45)*Math.PI)/180);
        
        I_diff1 = (float) (sunlightBrightness * k_diff * Math.cos(((sun.getHeight()-90)*Math.PI)/180));     
        I_diff1 += moonlightBrightness * k_diff * Math.cos(((moon.getHeight()-90)*Math.PI)/180);   
        
        I_diff2 = (float) (sunlightBrightness * k_diff * Math.cos(((sun.getHeight())*Math.PI)/180)*Math.cos(((sun.getAzimuth()-135)*Math.PI)/180));
        I_diff2 += moonlightBrightness  * k_diff * Math.cos(((moon.getHeight())*Math.PI)/180)*Math.cos(((moon.getAzimuth()-135)*Math.PI)/180);
        
        //specular
        
        //it is impossible to get specular light with a GlobalLightSource over the horizon on side 0 and 2. Just left in case i it someday helps somebody.
//        I_spec0 =(int) (
//                        sunlightBrightness
//                        * k_specular
//                        * Math.pow(
//                            Math.sin((sun.getHeight())*Math.PI/180)*Math.sin((sun.getAzimuth())*Math.PI/180)* Math.sqrt(2)/Math.sqrt(3)//y
//                          + Math.sin((sun.getHeight()-75)*Math.PI/180)/Math.sqrt(3)//z
//                        ,n_spec)
//                        *(n_spec+2)/(2*Math.PI)
//                        );

        
        I_spec1 = (float) (
                            sunlightBrightness
                            * k_specular
                            * Math.pow(
                                Math.sin(sun.getHeight()*Math.PI/180)*Math.sin(sun.getAzimuth()*Math.PI/180)/ Math.sqrt(2)//y
                              + Math.sin((sun.getHeight()-90)*Math.PI/180)/ Math.sqrt(2)//z
                            ,n_spec)
                            *(n_spec+2)/(2*Math.PI)
                        );
         I_spec1 +=(float) (
                        moonlightBrightness
                        * k_specular
                        * Math.pow(
                            Math.sin((moon.getHeight())*Math.PI/180)*Math.sin((moon.getAzimuth())*Math.PI/180)/Math.sqrt(2)//y
                          + Math.sin((moon.getHeight()-90)*Math.PI/180)/Math.sqrt(2)//z
                        ,n_spec)
                        *(n_spec+2)/(2*Math.PI)
                        );
         
      //it is impossible to get specular light with a GlobalLightSource over the horizon on side 0 and 2. Just left in case it someday may help somebody.
        //        I_spec2 =(int) (
        //                        sunlightBrightness
        //                        * k_specular
        //                        * Math.pow(
        //                            Math.cos((sun.getHeight() - 35.26) * Math.PI/360)
        //                           *Math.cos((sun.getAzimuth() + 180) * Math.PI/360)
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
     * Get's the brightness of a side.
     * @param side
     * @return a color on the (pseudo) greyscale
     */
    public Color getColorOfSide(int side){
        if (side==0) return getGlobalLight().mul(I_0);
            else if (side==1) return getGlobalLight().mul(I_1);
                else return getGlobalLight().mul(I_2);
    }
    
    /**
     * Returns the sum of every light
     * @return a color with a tone
     */
    public Color getGlobalLight(){
        return sun.getLight().cpy().add(moon.getLight());
    }
    
     /**
     * Calculates the light level based on the sun shining straight from the top
     */
    public static void calcSimpleLight(){
        for (int x=0; x < Map.getBlocksX(); x++){
            for (int y=0; y < Map.getBlocksY(); y++) {
                //find top most renderobject
                int topmost = Chunk.getBlocksZ()-1;//start at top
                while (Controller.getMap().getData(x,y,topmost).isTransparent() && topmost > 0 ){
                    topmost--;
                }
                
                if (topmost>0) {
                    //start at topmost renderobject and go down. Every step make it a bit darker
                    for (int level = topmost; level >= 0; level--){
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
    public GlobalLightSource getSun() {
        return sun;
    }

    /**
     *
     * @return
     */
    public GlobalLightSource getMoon() {
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
            
            //g.setLineWidth(2);
            ShapeRenderer shapeRenderer = view.SHAPE_RENDERER;
            
            //surrounding sphere
            Gdx.gl10.glLineWidth(2);
            shapeRenderer.setColor(Color.BLACK);
            shapeRenderer.begin(ShapeType.Circle);
            shapeRenderer.circle(posX, posY, size);
            shapeRenderer.end();
            
            //cut through
            shapeRenderer.begin(ShapeType.Circle);
            shapeRenderer.translate(posX, posY, 0);
            shapeRenderer.scale(1f, (0.5f), 1f);
            shapeRenderer.circle(0, 0, size);
            shapeRenderer.end();
            shapeRenderer.scale(1f, (2), 1f);
            shapeRenderer.translate(-posX, -posY, 0);
            
            //perfect/correct line
            shapeRenderer.setColor(Color.ORANGE);
            if ((sun.getMaxAngle()/90f-0.5f) != 0) {
                shapeRenderer.begin(ShapeType.Circle);
                shapeRenderer.translate(posX, posY, 0);
                                shapeRenderer.rotate(0, 0, 1, Controller.getMap().getWorldSpinDirection());
                shapeRenderer.scale(1f, (sun.getMaxAngle()/90f-0.5f), 1f);
                shapeRenderer.circle(0, 0, size);
                shapeRenderer.end();
                shapeRenderer.scale(1f, (1/(sun.getMaxAngle()/90f-0.5f)), 1f);
                                shapeRenderer.rotate(0, 0, 1, -Controller.getMap().getWorldSpinDirection());
                shapeRenderer.translate(-posX, -posY, 0);
            } else {
                shapeRenderer.begin(ShapeType.Line);
                shapeRenderer.line(posX-size, posY, posX+size, posY);
                shapeRenderer.end();
            }
            
            //sun position
            //longitude
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.begin(ShapeType.Line);
            shapeRenderer.line(
                posX +(int) (size* Math.sin((sun.getAzimuth()-90)*Math.PI/180)),
                posY +(int) (size/2*Math.cos((sun.getAzimuth()-90)*Math.PI/180)),
                posX,
                posY
            );
            
            //latitude
            shapeRenderer.setColor(Color.MAGENTA);
            shapeRenderer.line(
                posX +(int) (size * Math.sin((sun.getHeight()-90)*Math.PI/180)),
                posY -(int) (size/2*Math.sin((sun.getHeight())*Math.PI/180)),
                posX,
                posY
            );
            
            //long+lat of sun position
            shapeRenderer.setColor(Color.YELLOW);
            shapeRenderer.line(
                posX +(int) ( size*Math.sin((sun.getAzimuth()+90)*Math.PI/180) * Math.sin((sun.getHeight()-90)*Math.PI/180) ),
                posY -(int) ( size/2*Math.sin((sun.getAzimuth())*Math.PI/180) * Math.sin((sun.getHeight()-90)*Math.PI/180)) -(int) (size/2*Math.sin((sun.getHeight())*Math.PI/180)),
                posX,
                posY
             );

            shapeRenderer.setColor(Color.BLUE);
            shapeRenderer.line(
                posX +(int) ( size*Math.sin((moon.getAzimuth()+90)*Math.PI/180) * Math.sin((moon.getHeight()-90)*Math.PI/180) ),
                posY -(int) ( size/2*Math.sin((moon.getAzimuth())*Math.PI/180) * Math.sin((moon.getHeight()-90)*Math.PI/180)) -(int) (size/2*Math.sin((moon.getHeight())*Math.PI/180)),
                posX,
                posY
             );
             shapeRenderer.end();

            view.drawString("Lat: "+sun.getHeight(), 600, 110, Color.WHITE);
            view.drawString("Long: "+sun.getAzimuth(), 600, 100, Color.WHITE);
            view.drawString("PowerSun: "+sun.getPower()*100+"%", 600, 120, Color.WHITE);
            view.drawString("PowerMoon: "+moon.getPower()*100+"%", 600, 130, Color.WHITE);
            view.drawString("LightColor: "+getGlobalLight().toString(), 600, 140, Color.WHITE);
            shapeRenderer.begin(ShapeType.FilledRectangle);
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.filledRect(600, 160, 70, 70);
            shapeRenderer.setColor(getGlobalLight());
            shapeRenderer.filledRect(610, 170, 50, 50);

             //info bars
            
            //left side
            view.drawText(I_ambient+"\n+"+I_diff0+"\n+"+ I_spec0+"\n="+I_0, (int) (I_0*size), 100, Color.WHITE);
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.filledRect(0, 100, I_ambient*size, 10);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.filledRect(I_ambient*size, 100, I_diff0*size, 8);
            shapeRenderer.setColor(Color.BLUE);
            shapeRenderer.filledRect((I_ambient+I_diff0)*size, 100, I_spec0*size, 6);

            //top side
            view.drawText(I_ambient+"\n+"+I_diff1+"\n+"+ I_spec1+"\n="+I_1, (int) (I_1*size), 180, Color.WHITE);
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.filledRect(0, 180, I_ambient*size, 10);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.filledRect(I_ambient*size, 180, I_diff1*size, 8);
            shapeRenderer.setColor(Color.BLUE);
            shapeRenderer.filledRect((I_ambient+I_diff1)*size, 180, I_spec1*size, 6);

            //right side
            view.drawText(I_ambient+"\n+"+I_diff2+"\n+"+ I_spec2+"\n="+I_2, (int) (I_2*size), 260, Color.WHITE);
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.filledRect(0, 260, I_ambient*size, 10);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.filledRect(I_ambient*size, 260, I_diff2*size, 8);
            shapeRenderer.setColor(Color.BLUE);
            shapeRenderer.filledRect((I_ambient+I_diff2)*size, 260, I_spec2*size, 6);
            
            shapeRenderer.end();
        }
    }
}
