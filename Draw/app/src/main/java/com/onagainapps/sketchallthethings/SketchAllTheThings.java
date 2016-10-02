package com.onagainapps.sketchallthethings;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.onagainapps.sketchallthethings.DrawingManager.Command;

import java.util.Stack;

/**
 * Created by Caleb on 9/24/2016.
 */
public class SketchAllTheThings {
private static final String TAG = SketchAllTheThings.class.getSimpleName();

	public static class Tool {
		public static final int BRUSH = 0;
		public static final int CANVAS = 1;
	}
	

    private static SketchAllTheThings instance = null;

    public static SketchAllTheThings getInstance(){
        if (instance == null){
            instance = new SketchAllTheThings();
        }
        return instance;
    }
	

	//defaults
	private final int DEFAULT_COLOR = Color.BLACK;
	private final int DEFAULT_BRUSH_SIZE = 12;
	public final int MAX_BRUSH_SIZE = 100;
	public final int DEFAULT_BACKGROUND_COLOR = Color.TRANSPARENT;
	public final Bitmap.Config DEFAULT_BITMAP_CONFIG = Bitmap.Config.ARGB_8888;

	//current settings
	private int currentTool;
	private int currentColor;
	private int currentBrushSize;
	
	private Paint canvasBorderPaint;


	public SketchAllTheThings(){
		currentTool = Tool.CANVAS;
		currentColor = DEFAULT_COLOR;
        currentBrushSize = DEFAULT_BRUSH_SIZE;
		
		canvasBorderPaint = new Paint();
		canvasBorderPaint.setColor(Color.BLACK);
		canvasBorderPaint.setStyle(Paint.Style.STROKE);
		canvasBorderPaint.setStrokeWidth(2.0f);


	}

	

	public void setCurrentColor(int color){
		Log.d("caleb " + TAG, "Color set: " + color);
		currentColor = color;
	}

	public int getColor(){
		return currentColor;
	}
	public int getCurrentBrushSize() {
		return currentBrushSize;
	}

	public void setCurrentBrushSize(int currentBrushSize) {
		this.currentBrushSize = currentBrushSize;
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
}
