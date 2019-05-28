package com.onagainapps.sketchallthethings;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.onagainapps.sketchallthethings.DrawingManager.Command;
import com.onagainapps.sketchallthethings.DrawingManager.Drawing;
import com.onagainapps.sketchallthethings.DrawingManager.DrawingInputManager;
import com.onagainapps.sketchallthethings.DrawingManager.Layer;


public class DrawingView extends View {
	private static final String TAG = DrawingView.class.getSimpleName();
	
	
	public static class FitType {
		
		public static final int FREE_SIZE = 0;
		
		public static final int FIT_HEIGHT = 1;
	}
	
	private DrawingInputManager drawingInputManager;
	private Drawing currentDrawing; // the Drawing. Only one-at-a-time.
	private int fitType; // how the image should scale on the screen
	
	// parameters for scaling/translating the image on-screen.
	private Matrix canvasMatrix; // controls how the canvas is scaled in the DrawingView.
	private float translateX;
	private float translateY;
	private float scaleFactor = 1.0f;
	
	//the dimensions of the drawing. are set when drawing is first displayed.
	private int width = 0; 
	private int height = 0;

	
	public DrawingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		canvasMatrix = new Matrix();
		
		Log.d("caleb " + TAG, "DrawingView created by " + context.getClass().toString());
	}
	
	
	public void onDraw(Canvas canvas) {
		
		//sets the Drawing's dimensions if they need to be changed.
		if (canvas.getHeight() != this.height || canvas.getWidth() != this.width){
			this.width = canvas.getWidth();
			this.height = canvas.getHeight();
			Log.d("caleb " + TAG,"DrawingView size changed: " + width + " x " + height);
		}

		// Setup the canvas for drawing
		if (!this.isInEditMode() && currentDrawing != null) {
			//adjust canvas
			switch (fitType) {
				case FitType.FIT_HEIGHT:
					float scaleFactor = (float) canvas.getHeight() / (float) getCurrentDrawing().getHeight();
					//Log.d("caleb " + TAG,"FitHeight. CanvasHeight: " + canvas.getHeight() + " DrawingHeight: " + getCurrentDrawing().getHeight() + " scaleFactor: " + scaleFactor);
					canvas.scale(scaleFactor, scaleFactor);
					
					break;
			}
			
			// begin to perform scaling and translation operations on the drawing.
			canvas.save();
			canvas.translate(translateX, translateY);
			canvas.scale(scaleFactor, scaleFactor);
			
			// draw the 'border' of the image (when user zooms out)
			canvas.drawRect(-1, -1, getCurrentDrawing().getWidth()+1, getCurrentDrawing().getHeight()+1, SketchAllTheThings.getInstance().getCanvasBorderPaint());
			
			//pass the canvas to the Drawing so that it can perform the actual drawing operations.
			getCurrentDrawing().onDraw(canvas);
			
			// complete the scaling/translation options
			canvas.restore();

		} else {
			// show the placeholder while in edit mode or when there is no currently open drawing
			drawPlaceholder(canvas);
		}
	}
	
	
	private void drawPlaceholder(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.FILL);
		canvas.drawPaint(paint);
		
		paint.setColor(Color.BLACK);
		int TEXT_SIZE = 40;
		int TEXT_SPACING = 2;
		int PARAGRAPH_SPACING = 30;
		int LINE_WRAP_INDENT = 20;
		float y = canvas.getHeight()/8;
		float x = 25;
		paint.setTextSize(TEXT_SIZE);
		canvas.drawText("• To begin: Menu -> New Drawing", x, y , paint);
		y+=TEXT_SIZE + TEXT_SPACING + PARAGRAPH_SPACING;
		canvas.drawText("• Swipe from left edge of screen to ", x, y , paint);
		y+=TEXT_SIZE + TEXT_SPACING;
		canvas.drawText("change color and see tools", x + LINE_WRAP_INDENT, y , paint);
		y+=TEXT_SIZE + TEXT_SPACING + PARAGRAPH_SPACING;
		canvas.drawText("• Swipe from right edge of screen to", x, y , paint);
		y+=TEXT_SIZE + TEXT_SPACING;
		canvas.drawText("see layers", x + LINE_WRAP_INDENT, y , paint);
		y+=TEXT_SIZE + TEXT_SPACING + PARAGRAPH_SPACING;
		canvas.drawText("• To delete a layer, long press\n", x, y , paint);
		y+=TEXT_SIZE + TEXT_SPACING + PARAGRAPH_SPACING;
		canvas.drawText("• To edit a layer, press the pencil icon", x, y , paint);

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
	
	
	public int getCanvasWidth() {
		return width;
	}
	
	
	public int getCanvasHeight() {
		return height;
	}
	
	
	public void setFitType(int fitType) {
		this.fitType = fitType;
		postInvalidate();
	}
}