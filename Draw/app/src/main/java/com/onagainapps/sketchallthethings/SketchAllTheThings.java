package com.onagainapps.sketchallthethings;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.onagainapps.sketchallthethings.Tools.PaintBrush;
import com.onagainapps.sketchallthethings.Tools.Tool;

import java.util.ArrayList;

/**
 * Created by Caleb on 9/24/2016.
 */
public class SketchAllTheThings {
private static final String TAG = SketchAllTheThings.class.getSimpleName();

	public static class ToolType {//LEGACY
		public static final int BRUSH = 0;
		public static final int CANVAS = 1;
	}
	
	
	private PaintBrush paintBrush;

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
	private int currentTool;
	private int currentColor;
	
	private Paint canvasBorderPaint;
	
	private ArrayList<Tool> tools;


	public SketchAllTheThings(){
		currentTool = ToolType.CANVAS;
		
		canvasBorderPaint = new Paint();
		canvasBorderPaint.setColor(Color.BLACK);
		canvasBorderPaint.setStyle(Paint.Style.STROKE);
		canvasBorderPaint.setStrokeWidth(2.0f);
		
		
		setupTools();
	}
	
	private void setupTools(){
		this.tools = new ArrayList<>();
		paintBrush = new PaintBrush();
		tools.add(0, paintBrush);
	}
	
	public void setCurrentColor(int color){
		Log.d("caleb " + TAG, "Color set: " + color);
		currentColor = color;
	}

	public int getColor(){
		return currentColor;
	}
	
	public int getCurrentTool() {
		return currentTool;
	}
	
	public void setCurrentTool(int currentTool) {
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
