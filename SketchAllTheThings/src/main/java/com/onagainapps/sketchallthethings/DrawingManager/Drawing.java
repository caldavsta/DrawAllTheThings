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


public class Drawing {
	private static final String TAG = Drawing.class.getSimpleName();
	
	private ArrayList<Layer> layerList; //layers are drawn starting at the last element. layerList.last will be under all other layers
	private Layer layerBeingEdited; // the layer where input is directed to

	private DrawingView drawingView; 
	
	//Drawing's size is independent of the screen size.
	private int height;
	private int width;
	
	
	public Drawing(int width, int height, DrawingView drawingView) {
		this.drawingView = drawingView;
		this.width = width;
		this.height = height;
		
		Log.d("caleb " + TAG, "Drawing Created: " + width + " x " + height);
		
		layerList = new ArrayList<>();
	}
	
	//PRIVATE METHODS
	
	
	private Bitmap getBitmapForThisDrawing(){
		return Bitmap.createBitmap(width, height, SketchAllTheThings.getInstance().DEFAULT_BITMAP_CONFIG);
	}
	
	
	public void onDraw(Canvas canvas) {
		for (int i = layerList.size()-1; i >=0; i--) {
			if (layerList.get(i).isVisible()) {
				layerList.get(i).onDraw(canvas);
			}
		}
	}
	
	
	public Bitmap getDrawingAsBitmap(){
		Bitmap result = getBitmapForThisDrawing();
		Canvas tempCanvas = new Canvas(result);
		onDraw(tempCanvas);
		return result;
	}
	
	
	public void undo(){
		if (getLayerBeingEdited().undoLastAction()){
			Log.d("caleb " + TAG,"Undo Successful");
			drawingView.postInvalidate();
		}
	}
	
	
	public void redo(){
		if (getLayerBeingEdited().redoLastAction() ){
			Log.d("caleb " + TAG,"Redo Successful");
			drawingView.postInvalidate();
		} 
	}
	
	
	public Layer createNewLayer() {
		Layer newLayer = new Layer(getWidth(), getHeight(), "Layer " + layerList.size());
		layerList.add(0, newLayer);
		Log.d("caleb " + TAG,"Layer created: " + newLayer);
		layerBeingEdited = newLayer;
		return newLayer;
	}
	
	
	public void deleteLayer(Layer layer){
		layerList.remove(layer);
	}
	
	
	public Command getCurrentCommand() {
		return getLayerBeingEdited().getCurrentCommand();
	}
	
	
	public LayerArrayAdapter getLayerArrayAdapterForLayerList(Context context, DrawingView drawingView){
		return new LayerArrayAdapter(context, layerList, drawingView);
	}
	
	
	public void moveLayerUp(Layer layerToMove){
		if (isLayerAtTop(layerToMove)){
			return;
		}
		int currentIndex = layerList.indexOf(layerToMove);
		int indexToMoveTo = currentIndex + 1;
		
		Collections.swap(layerList, currentIndex, indexToMoveTo);
		drawingView.postInvalidate();
	}
	
	
	public void moveLayerDown(Layer layerToMove){ 
		if (isLayerAtBottom(layerToMove)){
			return;
		}
		int currentIndex = layerList.indexOf(layerToMove);
		int indexToMoveTo = currentIndex - 1;
		
		Collections.swap(layerList, currentIndex, indexToMoveTo);
		drawingView.postInvalidate();
	}
	
	
	public boolean isLayerAtTop(Layer layer){ //is layer drawn first
		return (layerList.get(layerList.size()-1) == layer);
	}
	
	
	public boolean isLayerAtBottom(Layer layer){//is layer drawn last
		return (layerList.get(0) == layer);
	}
	
	
	public Layer getLayerBeingEdited() {
		if (layerBeingEdited == null) {
			layerBeingEdited = layerList.get(0);
		}
		return layerBeingEdited;
	}
	
	
	public int getWidth() {
		return width;
	}
	
	
	public int getHeight() {
		return height;
	}
	
	
	public void setLayerBeingEdited(Layer layerBeingEdited) {
		Log.d("caleb " + TAG,"Layer being edited changed to: " + layerBeingEdited.getName());
		this.layerBeingEdited = layerBeingEdited;
	}
	
	
	
	public static Drawing getNewDefaultDrawing(DrawingView drawingView){
		Drawing result = new Drawing(drawingView.getCanvasWidth(), drawingView.getCanvasHeight(), drawingView);
		result.createNewLayer().setBackgroundColorAndRedraw(Color.WHITE);
		result.setLayerBeingEdited(result.createNewLayer());
		return result;
	}
}
