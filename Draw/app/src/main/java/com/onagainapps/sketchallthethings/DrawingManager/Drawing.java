package com.onagainapps.sketchallthethings.DrawingManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.onagainapps.sketchallthethings.DrawingView;
import com.onagainapps.sketchallthethings.SketchAllTheThings;

import java.util.ArrayList;
import java.util.Collections;

import static android.R.attr.translateX;
import static android.R.attr.translateY;

/**
 * Created by Caleb on 9/28/2016.
 * 
 * Drawing has a list of Layers, handles layer-drawing order, can give a nicely-sized blank drawing to start with (custom sized not implemented yet)
 * No actual drawing takes place. The drawing takes place in each Layer.
 */
public class Drawing {
	private static final String TAG = Drawing.class.getSimpleName();
	
	private ArrayList<Layer> layerList; //layers are drawn starting at the last element. layerList.last will be under all other layers
	private Layer layerBeingEdited; // the layer where input is directed to

	private DrawingView drawingView; 
	
	//Drawing's size is independent of the screen size.
	private int height;
	private int width;
	
	/**
	 * Instantiates a new Drawing.
	 *
	 * @param width       the width
	 * @param height      the height
	 * @param drawingView the drawing view where Drawing is displayed and from where input is received.
	 */
	public Drawing(int width, int height, DrawingView drawingView) {
		this.drawingView = drawingView;
		this.width = width;
		this.height = height;
		
		Log.d("caleb " + TAG, "Drawing Created: " + width + " x " + height);
		
		layerList = new ArrayList<>();
	}
	
	//PRIVATE METHODS
	
	/**
	 * 
	 * @return the Bitmap which can be shared or saved, etc
	 */
	private Bitmap getBitmapForThisDrawing(){
		return Bitmap.createBitmap(width, height, SketchAllTheThings.getInstance().DEFAULT_BITMAP_CONFIG);
	}
	
	/**
	 * Iterates through layers. If the layer is 'visible' it is drawn to the canvas.
	 *
	 * @param canvas the canvas which is passed from DrawingView.onDraw()
	 */
	public void onDraw(Canvas canvas) {
		for (int i = layerList.size()-1; i >=0; i--) {
			if (layerList.get(i).isVisible()) {
				layerList.get(i).onDraw(canvas);
			}
		}
	}
	
	/**
	 * Get drawing as bitmap bitmap.
	 *
	 * @return the bitmap
	 */
	public Bitmap getDrawingAsBitmap(){
		Bitmap result = getBitmapForThisDrawing();
		Canvas tempCanvas = new Canvas(result);
		onDraw(tempCanvas);
		return result;
	}
	
	/**
	 * Undoes the most recent action in the currently-being-edited layer
	 */
	public void undo(){
		if (getLayerBeingEdited().undoLastAction()){
			Log.d("caleb " + TAG,"Undo Successful");
			drawingView.postInvalidate();
		}
	}
	
	/**
	 * Redoes the most recent action in the currently-being-edited layer
	 */
	public void redo(){
		if (getLayerBeingEdited().redoLastAction() ){
			Log.d("caleb " + TAG,"Redo Successful");
			drawingView.postInvalidate();
		} 
	}
	
	/**
	 * Creates a new layer, adds the layer to the top of the drawing, and makes it the currently-being-edited layer
	 *
	 * @return the layer
	 */
	public Layer createNewLayer() {
		Layer newLayer = new Layer(getWidth(), getHeight(), "Layer " + layerList.size());
		layerList.add(0, newLayer);
		Log.d("caleb " + TAG,"Layer created: " + newLayer);
		layerBeingEdited = newLayer;
		return newLayer;
	}
	
	/**
	 * Delete a layer.
	 *
	 * @param layer the layer to delete
	 */
	public void deleteLayer(Layer layer){
		layerList.remove(layer);
	}
	
	/**
	 *
	 * @return the current command if an action is taking place, null otherwise
	 */
	public Command getCurrentCommand() {
		return getLayerBeingEdited().getCurrentCommand();
	}
	
	/**
	 * Get layer array adapter for layer list layer array adapter.
	 *
	 * @param context     the context
	 * @param drawingView the drawing view
	 * @return the layer array adapter
	 */
	public LayerArrayAdapter getLayerArrayAdapterForLayerList(Context context, DrawingView drawingView){
		return new LayerArrayAdapter(context, layerList, drawingView);
	}
	
	/**
	 * Move layer up so that it is drawn further on-top
	 *
	 * @param layerToMove the layer to move
	 */
	public void moveLayerUp(Layer layerToMove){
		if (isLayerAtTop(layerToMove)){
			return;
		}
		int currentIndex = layerList.indexOf(layerToMove);
		int indexToMoveTo = currentIndex + 1;
		
		Collections.swap(layerList, currentIndex, indexToMoveTo);
		drawingView.postInvalidate();
	}
	
	/**
	 * Move layer down so that it is drawn further below.
	 *
	 * @param layerToMove the layer to move
	 */
	public void moveLayerDown(Layer layerToMove){ 
		if (isLayerAtBottom(layerToMove)){
			return;
		}
		int currentIndex = layerList.indexOf(layerToMove);
		int indexToMoveTo = currentIndex - 1;
		
		Collections.swap(layerList, currentIndex, indexToMoveTo);
		drawingView.postInvalidate();
	}
	
	/**
	 * Is layer at top boolean.
	 *
	 * @param layer the layer
	 * @return whether the layer is at the top of the list
	 */
	public boolean isLayerAtTop(Layer layer){ //is layer drawn first
		return (layerList.get(layerList.size()-1) == layer);
	}
	
	/**
	 * Is layer at bottom boolean.
	 *
	 * @param layer the layer
	 * @return whether layer is at the bottom of the list
	 */
	public boolean isLayerAtBottom(Layer layer){//is layer drawn last
		return (layerList.get(0) == layer);
	}
	
	/**
	 * Gets layer being edited (the layer that input is currently directed to)
	 *
	 * @return the layer being edited
	 */
	public Layer getLayerBeingEdited() {
		if (layerBeingEdited == null) {
			layerBeingEdited = layerList.get(0);
		}
		return layerBeingEdited;
	}
	
	/**
	 * Get width of Drawing.
	 *
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Get height of Drawing.
	 *
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Sets layer being edited.
	 *
	 * @param layerBeingEdited the layer being edited
	 */
	public void setLayerBeingEdited(Layer layerBeingEdited) {
		Log.d("caleb " + TAG,"Layer being edited changed to: " + layerBeingEdited.getName());
		this.layerBeingEdited = layerBeingEdited;
	}
	
	
	/**
	 * Instantiates a Drawing which is sized to fit perfectly into the current screen. Will be very large on high-dpi displays.
	 *
	 * @param drawingView the drawing view which Drawing will be created to fit.
	 * @return the new drawing
	 */
	public static Drawing getNewDefaultDrawing(DrawingView drawingView){
		Drawing result = new Drawing(drawingView.getCanvasWidth(), drawingView.getCanvasHeight(), drawingView);
		result.createNewLayer().setBackgroundColorAndRedraw(Color.WHITE);
		result.setLayerBeingEdited(result.createNewLayer());
		return result;
	}
}
