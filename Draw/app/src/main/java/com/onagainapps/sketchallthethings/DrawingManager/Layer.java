package com.onagainapps.sketchallthethings.DrawingManager;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.onagainapps.sketchallthethings.SketchAllTheThings;

import java.util.Stack;

/**
 * Created by Caleb on 9/29/2016.
 */

public class Layer {
	private static final String TAG = Layer.class.getSimpleName();
	
	private Stack<Command> undoStack;
	private Stack<Command> redoStack;
	
	private String name = "UNNAME";

	private int backgroundColor;
	
	private boolean isVisible;
	private boolean isEditable;
	
	private int width;
	private int height;
	
	private Bitmap bitmap;
	private Canvas canvas;
	
	public Layer(int width, int height, String name){
		this.width = width;
		this.height = height;
		
		this.name = name;
		
		isVisible = true;
		undoStack = new Stack<>();
		redoStack = new Stack<>();
		
		backgroundColor = SketchAllTheThings.getInstance().DEFAULT_BACKGROUND_COLOR;
		
		bitmap = Bitmap.createBitmap(width, height, SketchAllTheThings.getInstance().DEFAULT_BITMAP_CONFIG);
		bitmap.eraseColor(backgroundColor);
		
		canvas = new Canvas(bitmap);
	}
	
	//PUBLIC METHODS
	
	public void onDraw(Canvas canvas, Paint paint){
			canvas.drawBitmap(bitmap, 0,0, paint);
		
	}
	
	public void redrawAllCommands(){
		bitmap.eraseColor(backgroundColor);
		for(Command command : undoStack){
			command.draw(canvas);
		}
	}
	
	public void addCommandAndDrawToBitmap(Command command){
		undoStack.push(command);
		drawCommandToBitmap(command);
	}
	
	private void drawCommandToBitmap(Command command){
		command.draw(canvas);
	}
	
	public Rect getBounds(){
		int left = 0;
		int top = 0;
		int right = 0;
		int bottom = 0;
		for (Command thisCommand : undoStack){
			if (thisCommand.getBounds().left > left) left = thisCommand.getBounds().left;
			if (thisCommand.getBounds().top > top) top = thisCommand.getBounds().top;
			if (thisCommand.getBounds().right > right) right = thisCommand.getBounds().right;
			if (thisCommand.getBounds().bottom > bottom) bottom = thisCommand.getBounds().bottom;
		}
		return new Rect(left, top, right, bottom);
		// TODO make it search redoStack too
	}
	
	public boolean undoLastAction() {
		if (undoStack.size() > 0){
			redoStack.push(undoStack.pop());
			redrawAllCommands();
			return true;
		} else {
			return false;
		}
	}
	
	public boolean redoLastAction(){
		if (redoStack.size() > 0){
			Command redoCommand = redoStack.pop();
			addCommandAndDrawToBitmap(redoCommand);
			return true;
		} else {
			return false;
		}
	}
	
	public void clearRedoStack(){
		redoStack.clear();
	}
	
	//GETTERS AND SETTERS
	
	public boolean isVisible() {
		return isVisible;
	}
	
	public void setVisible(boolean visible) {
		isVisible = visible;
	}
	
	public Stack<Command> getUndoStack() {
		return undoStack;
	}
	
	public int getBackgroundColor() {
		return backgroundColor;
	}
	
	public void setBackgroundColorAndRedraw(int backgroundColor) {
		this.backgroundColor = backgroundColor;
		redrawAllCommands();
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public String toString(){
		return "Layer " + name + " with " + undoStack.size() + " commands.";
	}
	
	public String getName() {
		return name;
	}
	
	public Bitmap getBitmap() {
		return bitmap;
	}
}
