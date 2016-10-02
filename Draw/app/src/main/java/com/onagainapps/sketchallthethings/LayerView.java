package com.onagainapps.sketchallthethings;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.onagainapps.sketchallthethings.DrawingManager.Layer;

/**
 * Created by Caleb on 9/30/2016.
 */

public class LayerView extends View {
	private Layer layer;
	private Paint paint;
	private Matrix matrix;
	public LayerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.paint = new Paint();
		this.matrix = new Matrix();
	}
	
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (!this.isInEditMode()) {
			//adjust canvas
			float scaleFactor = (float) canvas.getHeight() / (float) layer.getHeight();
			//Log.d("caleb " + TAG,"FitHeight. CanvasHeight: " + canvas.getHeight() + " DrawingHeight: " + getCurrentDrawing().getHeight() + " scaleFactor: " + scaleFactor);
			matrix.setScale(scaleFactor, scaleFactor);
			canvas.drawBitmap(layer.getBitmap(), matrix, paint);
			drawPlaceholder(canvas, layer.getName());
		} else {
			drawPlaceholder(canvas, "NO LAYER");
		}
	}
	
	
	private void drawPlaceholder(Canvas canvas, String text) {
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setTextSize(20);
		canvas.drawText(text, 10, 25, paint);
	}
	
	public void setLayer(Layer layer) {
		this.layer = layer;
	}
}
