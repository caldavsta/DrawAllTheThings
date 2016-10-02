package com.onagainapps.sketchallthethings;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.onagainapps.sketchallthethings.DrawingManager.Command;
import com.onagainapps.sketchallthethings.DrawingManager.Drawing;
import com.onagainapps.sketchallthethings.DrawingManager.DrawingInputManager;
import com.onagainapps.sketchallthethings.DrawingManager.Layer;

/**
 * Created by Caleb on 9/24/2016.
 */
public class DrawingView extends View {
	private static final String TAG = DrawingView.class.getSimpleName();
	
	public static class FitType {
		public static final int FREE_SIZE = 0;
		public static final int FIT_HEIGHT = 1;
	}
	
	private DrawingInputManager drawingInputManager;
	private Drawing currentDrawing;
	private int fitType;
	
	private Matrix canvasMatrix;
	
	private float translateX;
	private float translateY;
	private float scaleFactor = 1.0f;
	
	public boolean drawingCurrently;
	
	public DrawingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		
		drawingCurrently = false;
		
		canvasMatrix = new Matrix();
		
		Log.d("caleb " + TAG, "DrawingView created by " + context.getClass().toString());
	}
	
	public void onDraw(Canvas canvas) {
		if (!this.isInEditMode() && currentDrawing != null) {
			//adjust canvas
			switch (fitType) {
				case FitType.FIT_HEIGHT:
					float scaleFactor = (float) canvas.getHeight() / (float) getCurrentDrawing().getHeight();
					//Log.d("caleb " + TAG,"FitHeight. CanvasHeight: " + canvas.getHeight() + " DrawingHeight: " + getCurrentDrawing().getHeight() + " scaleFactor: " + scaleFactor);
					canvas.scale(scaleFactor, scaleFactor);
					
					break;
			}
			canvas.save();
			canvas.translate(translateX, translateY);
			canvas.scale(scaleFactor, scaleFactor);
			
			canvas.drawRect(-1, -1, getCurrentDrawing().getWidth()+1, getCurrentDrawing().getHeight()+1, SketchAllTheThings.getInstance().getCanvasBorderPaint());
			getCurrentDrawing().onDraw(canvas);
			
			canvas.restore();
			
			
		} else {
			drawPlaceholder(canvas);
		}
	}
	
	
	private void drawPlaceholder(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.FILL);
		canvas.drawPaint(paint);
		
		paint.setColor(Color.BLACK);
		paint.setTextSize(20);
		canvas.drawText("No current drawing.", 10, 25, paint);
	}
	
	public Drawing getCurrentDrawing() {
		return currentDrawing;
	}
	
	public void setCurrentDrawing(Drawing currentDrawing) {
		this.currentDrawing = currentDrawing;
		drawingInputManager = new DrawingInputManager(this);
		this.setOnTouchListener(drawingInputManager);
		invalidate();
	}
	
	public void setTranslate(float x, float y){
		this.translateX = x;
		this.translateY = y;
	}
	
	public void setScale(float scaleFactor){
		this.scaleFactor = scaleFactor;
	}
	
	public Matrix getCanvasMatrix() {
		return canvasMatrix;
	}
	
	public void setFitType(int fitType) {
		this.fitType = fitType;
		postInvalidate();
	}
}