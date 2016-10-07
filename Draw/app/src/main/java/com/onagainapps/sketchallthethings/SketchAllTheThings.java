package com.onagainapps.sketchallthethings;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.onagainapps.sketchallthethings.Tools.Eraser;
import com.onagainapps.sketchallthethings.Tools.PaintBrush;
import com.onagainapps.sketchallthethings.Tools.Tool;

import java.util.ArrayList;

/**
 * Created by Caleb on 9/24/2016.
 */
public class SketchAllTheThings {
private static final String TAG = SketchAllTheThings.class.getSimpleName();
	
	private static final int DEFAULT_COLOR = Color.BLACK;
	
	private PaintBrush paintBrush;
	private Eraser eraser;

    private static SketchAllTheThings instance = null;

    public static SketchAllTheThings getInstance(){
        if (instance == null){
            instance = new SketchAllTheThings();
        }
        return instance;
    }
	

	//defaults

	public final int DEFAULT_BACKGROUND_COLOR = Color.TRANSPARENT;
	public final Bitmap.Config DEFAULT_BITMAP_CONFIG = Bitmap.Config.ARGB_8888;

	//current settings
	private Tool currentTool;
	private int currentColor;
	
	private Paint canvasBorderPaint;
	
	private ArrayList<Tool> tools;


	public SketchAllTheThings(){
		
		//setup the Paint that draws the border around the drawing
		canvasBorderPaint = new Paint();
		canvasBorderPaint.setColor(Color.BLACK);
		canvasBorderPaint.setStyle(Paint.Style.STROKE);
		canvasBorderPaint.setStrokeWidth(2.0f);
		
		setCurrentColor(DEFAULT_COLOR);
		
		setupTools();
		setCurrentTool(paintBrush);
	}
	
	private void setupTools(){
		this.tools = new ArrayList<>();
		
		//create the tools
		paintBrush = new PaintBrush();
		eraser = new Eraser();
		
		//add tools to belt
		tools.add(0, paintBrush);
		tools.add(1, eraser);
		
	}
	
	public void setCurrentColor(int color){
		Log.d("caleb " + TAG, "Color set: " + color);
		currentColor = color;
	}

	public int getColor(){
		return currentColor;
	}
	
	public Tool getCurrentTool() {
		return currentTool;
	}
	
	public void setCurrentTool(Tool currentTool) {
		this.currentTool = currentTool;
	}
	
	public Paint getCanvasBorderPaint() {
		return canvasBorderPaint;
	}
	
	public PaintBrush getPaintBrush () { return paintBrush; }
	
	public ArrayList<Tool> getTools() {
		return tools;
	}
}
