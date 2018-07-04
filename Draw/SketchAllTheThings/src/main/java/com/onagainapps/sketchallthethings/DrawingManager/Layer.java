package com.onagainapps.sketchallthethings.DrawingManager;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.widget.Button;

import com.onagainapps.sketchallthethings.SketchAllTheThings;

import java.util.Stack;


public class Layer {
	private static final String TAG = Layer.class.getSimpleName();
	
	private Stack<Command> undoStack;
	private Stack<Command> redoStack;
	
	private String name = "UNNAMED";// layer names not implemented
	
	private Command currentCommand;// the Command, if the user is currently interacting with the Drawing

	private int backgroundColor;// defaults to transparent. will be user-editable at release
	
	private boolean isVisible;// whether or not to draw the layer
	
	//a Layer's dimensions usually match the Drawing's dimension but may not.
	private int width;
	private int height;
	
	private Bitmap bitmap;// where each Command draws pixels to
	private Canvas canvas;// a canvas to keep previous Commands (so that not every Command need be redrawn each time a new one is added)
	private Paint paint;// the properties of how bitmap is drawn to canvas (is not related to brush stroke color etc.)
	
	
	public Layer(int width, int height, String name){
		this.width = width;
		this.height = height;
		
		this.name = name;
		
		isVisible = true;
		
		undoStack = new Stack<>();
		redoStack = new Stack<>();
		
		backgroundColor = SketchAllTheThings.getInstance().DEFAULT_BACKGROUND_COLOR;
		
		bitmap = Bitmap.createBitmap(width, height, SketchAllTheThings.getInstance().DEFAULT_BITMAP_CONFIG);
		bitmap.eraseColor(backgroundColor);// turns every pixel in the Bitmap to the background color.
		
		canvas = new Canvas(bitmap);

		paint = new Paint();
	}

	
	public void onDraw(Canvas canvas){
		canvas.drawBitmap(bitmap, 0,0, paint);
		if (currentCommand != null) {
			currentCommand.draw(canvas);
		}
	}
	
	
	public void startAddingCommand(Command command) {
		setCurrentCommand(command);
	}
	
	
	public void cancelAddingCommand(){
		setCurrentCommand(null);
	}
	
	
	public void finishAddingCommand() {
		addCommandAndDrawToBitmap(getCurrentCommand());
		clearRedoStack();
		Log.d("caleb " + TAG,"Command Finished: " + getCurrentCommand().toString());
		setCurrentCommand(null);
		redrawAllCommands();
		
		// -----------------------------drawingView.postInvalidate();
	}
	
	
	public void addCommandAndDrawToBitmap(Command command){
		undoStack.push(command);
		drawCommandToBitmap(command);
	}
	
	private void drawCommandToBitmap(Command command){
		command.draw(canvas);
	}
	
	
	public void redrawAllCommands(){
		bitmap.eraseColor(Color.TRANSPARENT);
		canvas.drawColor(backgroundColor);
		for(Command command : undoStack){
			command.draw(canvas);
		}
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
	
	
	public Command getCurrentCommand() {
		return currentCommand;
	}
	
	
	public void setCurrentCommand(Command currentCommand) {
		this.currentCommand = currentCommand;
	}
}
