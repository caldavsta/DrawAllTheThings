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
 * 
 * LayerView is the View which shows just the Commands that are in one layer. It is shown in the right drawer.
 */
public class LayerView extends View {
	private Layer layer;
	private Paint paint;
	private Matrix matrix;
	
	/**
	 * Instantiates a new Layer view.
	 *
	 * @param context the context
	 * @param attrs   the attrs
	 */
	public LayerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.paint = new Paint();
		this.matrix = new Matrix();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if (!this.isInEditMode()) {
			// adjust the canvas so that the layer fits neatly into LayerView
			float scaleFactor = (float) canvas.getHeight() / (float) layer.getHeight();
			matrix.setScale(scaleFactor, scaleFactor);
			
			// perform the actual drawing of the layer
			canvas.drawBitmap(layer.getBitmap(), matrix, paint);
			
			// draw the label (should be layer's name but not implemented)
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
	
	/**
	 * Sets layer.
	 *
	 * @param layer the layer
	 */
	public void setLayer(Layer layer) {
		this.layer = layer;
	}
}
