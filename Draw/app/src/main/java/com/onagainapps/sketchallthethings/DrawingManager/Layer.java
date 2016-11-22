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

/**
 * Created by Caleb on 9/29/2016.
 * 
 * Layer handles the Bitmap where Commands are drawn to. It also gives information about how much room Commands *actually* occupy within the layer (see getBounds() )
 */
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
	
	/**
	 * Instantiates a new Layer. Usually matches the dimensions of the Drawing but doesn't need to. When image-importing is implemented, each image can be imported to its own layer
	 *
	 * @param width  the width
	 * @param height the height
	 * @param name   the name. not implemented
	 */
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

	/**
	 * Passes the Canvas, which came from Drawing.onDraw(), to the currentCommand so that it can draw it's information to the canvas
	 *
	 * @param canvas the canvas originating from DrawingActivity.onDraw()
	 */
	public void onDraw(Canvas canvas){
		canvas.drawBitmap(bitmap, 0,0, paint);
		if (currentCommand != null) {
			currentCommand.draw(canvas);
		}
	}
	
	/**
	 * Start adding a new command. 
	 * Is called, for example, when the user starts a brush stroke.
	 *
	 * @param command the command
	 */
	public void startAddingCommand(Command command) {
		setCurrentCommand(command);
	}
	
	/**
	 * Cancels the current command. 
	 * Is called, for example, if Android determines that the user meant to swipe a drawer into view after originally determining that the user wished to begin a brush stroke.
	 * Without this method, there would be accidental lines drawn each time the user swiped in one of the drawers
	 */
	public void cancelAddingCommand(){
		setCurrentCommand(null);
	}
	
	/**
	 * Finishes adding a new command. 
	 * Is called, for example, when the user lifts their finger after drawing a brush stroke.
	 * Once the command is finished, each command in the layer is redrawn from the beginning.
	 * Clears the redo-stack
	 * todo commands don't need to be redrawn every time
	 */
	public void finishAddingCommand() {
		addCommandAndDrawToBitmap(getCurrentCommand());
		clearRedoStack();
		Log.d("caleb " + TAG,"Command Finished: " + getCurrentCommand().toString());
		setCurrentCommand(null);
		redrawAllCommands();
		
		// -----------------------------drawingView.postInvalidate();
	}
	
	/**
	 * Add command and draw to bitmap.
	 *
	 * @param command the command
	 */
	public void addCommandAndDrawToBitmap(Command command){
		undoStack.push(command);
		drawCommandToBitmap(command);
	}
	
	private void drawCommandToBitmap(Command command){
		command.draw(canvas);
	}
	
	/**
	 * Erase the layer, then redraw all commands.
	 */
	public void redrawAllCommands(){
		bitmap.eraseColor(Color.TRANSPARENT);
		canvas.drawColor(backgroundColor);
		for(Command command : undoStack){
			command.draw(canvas);
		}
	}
	
	/**
	 * Gets the *actual* bounds of the layer.
	 * For example: if the layer is 50x50 but there is only a 5x5 box in the middle of the layer, this would return x=5,y=5
	 * This is designed for LayerView so that it only shows what has been drawn instead of the whole layer
	 *
	 * @return a Rect which is the size of the actual content of the layer. rarely = the layer's dimensions
	 */
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
	
	/**
	 * Undo last action.
	 *
	 * @return whether an action(Command) was undone
	 */
	public boolean undoLastAction() {
		if (undoStack.size() > 0){
			redoStack.push(undoStack.pop());
			redrawAllCommands();
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Redo last action.
	 *
	 * @return whether the action(Command) was redone
	 */
	public boolean redoLastAction(){
		if (redoStack.size() > 0){
			Command redoCommand = redoStack.pop();
			addCommandAndDrawToBitmap(redoCommand);
			return true;
		} else {
			return false;
		}
	}
	
	
	/**
	 * Clear redo stack.
	 * Called every time a new Command is completed.
	 */
	public void clearRedoStack(){
		redoStack.clear();
	}

	/**
	 * Is visible boolean.
	 *
	 * @return the boolean
	 */
	public boolean isVisible() {
		return isVisible;
	}
	
	/**
	 * Sets visible.
	 *
	 * @param visible the visible
	 */
	public void setVisible(boolean visible) {
		isVisible = visible;
	}
	
	/**
	 * Gets undo stack.
	 *
	 * @return the undo stack
	 */
	public Stack<Command> getUndoStack() {
		return undoStack;
	}
	
	/**
	 * Gets background color.
	 *
	 * @return the background color
	 */
	public int getBackgroundColor() {
		return backgroundColor;
	}
	
	/**
	 * Sets background color then redraws the layer so that it reflects the new selection
	 *
	 * @param backgroundColor the background color
	 */
	public void setBackgroundColorAndRedraw(int backgroundColor) {
		this.backgroundColor = backgroundColor;
		redrawAllCommands();
	}
	
	/**
	 * Gets width.
	 *
	 * @return the width of the layer
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Gets height.
	 *
	 * @return the height of the layer
	 */
	public int getHeight() {
		return height;
	}
	
	public String toString(){
		return "Layer " + name + " with " + undoStack.size() + " commands.";
	}
	
	/**
	 * Gets name.
	 *
	 * @return the name of the layer
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets bitmap.
	 *
	 * @return the bitmap of the layer
	 */
	public Bitmap getBitmap() {
		return bitmap;
	}
	
	/**
	 * Gets current command.
	 *
	 * @return the current command if one is being performed, null otherwise
	 */
	public Command getCurrentCommand() {
		return currentCommand;
	}
	
	/**
	 * Sets current command.
	 *
	 * @param currentCommand the command that the user has begun
	 */
	public void setCurrentCommand(Command currentCommand) {
		this.currentCommand = currentCommand;
	}
}
