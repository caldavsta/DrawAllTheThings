package com.onagainapps.sketchallthethings.DrawingManager;

import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.onagainapps.sketchallthethings.DrawingView;
import com.onagainapps.sketchallthethings.SketchAllTheThings;
import com.onagainapps.sketchallthethings.Tools.PaintBrush;
import com.onagainapps.sketchallthethings.Tools.Tool;

/**
 * Created by Caleb on 9/24/2016.
 */
public class DrawingInputManager implements View.OnTouchListener {
	private static final String TAG = "DrawingInputMgr";
	private int singlePointerId = -1;
	private int multiPointerId = -1;
	
	
	private MotionEvent.PointerCoords pointerLastFrame;
	
	
	//Drawing stuff
	private DrawingView drawingView;
	
	//Canvas ToolType stuff
	private float lastFrameX;
	private float lastFrameY;
	private float lastFrameDistance;
	private float cursorDistanceFactor = 1.0f;
	
	private float canvasTranslateX = 0.0f;
	private float canvasTranslateY = 0.0f;
	private float canvasScaleFactor = 1.0f;
	
	public DrawingInputManager(DrawingView drawingView) {
		this.drawingView = drawingView;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		//Log.d("caleb " + TAG,"ToolType: " + SketchAllTheThings.getInstance().getCurrentTool() + " Event: " + actionToString(MotionEventCompat.getActionMasked(event)));
		
		boolean result = false;
		switch (SketchAllTheThings.getInstance().getCurrentTool().getToolType()){
			case Tool.PAINT_BRUSH:
				result = handleDrawingToolInput(v, event);
				break;
			case Tool.ZOOM_AND_PAN:
				result = handleCanvasInput(v, event);
				break;
		}
		
		return result;
		
	}
	
	private boolean handleCanvasInput(View v, MotionEvent event){
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				singlePointerId = event.getPointerId(0);
				lastFrameX = event.getX();
				lastFrameY = event.getY();
				Log.d("caleb " + TAG,"start pan");
				
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				if (event.getPointerCount() == 2){
					multiPointerId = event.getPointerId(event.getPointerCount());
					lastFrameDistance = distanceBetweenPoints(event.getAxisValue(MotionEvent.AXIS_X, 0), event.getAxisValue(MotionEvent.AXIS_Y, 0), event.getAxisValue(MotionEvent.AXIS_X, 1), event.getAxisValue(MotionEvent.AXIS_Y, 1));
					//zoom started
					Log.d("caleb " + TAG,"Zoom started");
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if (event.getPointerCount() == 2){
					//zoom event continues
					float distanceBetweenCursors = distanceBetweenPoints(event.getAxisValue(MotionEvent.AXIS_X, 0), event.getAxisValue(MotionEvent.AXIS_Y, 0), event.getAxisValue(MotionEvent.AXIS_X, 1), event.getAxisValue(MotionEvent.AXIS_Y, 1));
					adjustScale(distanceBetweenCursors - lastFrameDistance);
					
					Log.d("caleb " + TAG,"Zoom Continues. Scale Factor: " + canvasScaleFactor + " Distance between cursors: " + distanceBetweenCursors);
					
					lastFrameDistance = distanceBetweenCursors;

				} else if (event.getPointerCount() == 1){
					// pan
					float panX = lastFrameX - event.getX();
					float panY = lastFrameY - event.getY();
							
					adjustTranslate(panX, panY);
					
					//drawingView.getCanvasMatrix().postTranslate(panX, panY);
					lastFrameY = event.getY();
					lastFrameX = event.getX();
					Log.d("caleb " + TAG,"continue pan of " + panX + " x " + panY);
					drawingView.postInvalidate();
				}
				break;
			case MotionEvent.ACTION_UP:
				singlePointerId = -1;
				Log.d("caleb " + TAG,"end pan");
				break;
			case MotionEvent.ACTION_POINTER_UP:
				if (event.getPointerCount() < 3){
					multiPointerId = -1;
					Log.d("caleb " + TAG,"Zoom Complete. Scale Factor: " + canvasScaleFactor);
				}
				break;
		}
		return true;
	}
	
	private boolean handleDrawingToolInput(View v, MotionEvent event){
		switch (event.getAction()) {
			
			case MotionEvent.ACTION_DOWN:
				singlePointerId = event.getPointerId(0);

				break;

			case MotionEvent.ACTION_UP:
				singlePointerId = -1;
				if (drawingView.getCurrentDrawing().getCurrentCommand() != null) {
					drawingView.getCurrentDrawing().finishAddingCommand();
				}
				
				break;
			
			case MotionEvent.ACTION_MOVE:
				if (event.getPointerId(0) == singlePointerId && event.getPointerCount() == 1) {
					Command existingCommand = drawingView.getCurrentDrawing().getCurrentCommand();
					
					float translatedX = event.getX()- (int)canvasTranslateX;
					float translatedY = event.getY() - (int)canvasTranslateY;
					
					if (existingCommand == null) {
						//create command
						Point point = new Point((int)translatedX, (int)translatedY);
						Command newCommand = new BrushStroke(point);
						
						//create brushstroke
						BrushStroke commandAsBrushStroke = (BrushStroke) newCommand;
						commandAsBrushStroke.setBrushColor(SketchAllTheThings.getInstance().getColor());
						commandAsBrushStroke.setBrushSize(SketchAllTheThings.getInstance().getPaintBrush().getBrushSize());
						commandAsBrushStroke.addPointToLine(point);
						
						//inform drawingview that command has started
						drawingView.getCurrentDrawing().startAddingCommand(newCommand);
						existingCommand = newCommand;
					}
					if (existingCommand.getClass() == BrushStroke.class) {
						BrushStroke brushStroke = (BrushStroke) existingCommand;
						//Log.d("caleb " + TAG, event.getX() + " x " + event.getY());
						
						Point point = new Point((int)translatedX, (int)translatedY);
						brushStroke.addPointToLine(point);
						
						v.postInvalidate();
					}
				} 
				break;
			
			case MotionEvent.ACTION_CANCEL:
				drawingView.getCurrentDrawing().cancelAddingCommand();
				
				break;
		}
		return true;
	}
	
	// Given an action int, returns a string description
	public static String actionToString(int action) {
		switch (action) {
			
			case MotionEvent.ACTION_DOWN:
				return "Down";
			case MotionEvent.ACTION_MOVE:
				return "Move";
			case MotionEvent.ACTION_POINTER_DOWN:
				return "Pointer Down";
			case MotionEvent.ACTION_UP:
				return "Up";
			case MotionEvent.ACTION_POINTER_UP:
				return "Pointer Up";
			case MotionEvent.ACTION_OUTSIDE:
				return "Outside";
			case MotionEvent.ACTION_CANCEL:
				return "Cancel";
		}
		return "";
	}
	
	private static float distanceBetweenPoints(float x1, float y1, float x2, float y2){
		float result = (float) Math.hypot(x1-x2, y1-y2);
		return result;
	}
	
	public void adjustTranslate(float x, float y){
		this.canvasTranslateX -= x;
		this.canvasTranslateY -= y;
		
		drawingView.setTranslate(canvasTranslateX, canvasTranslateY);
	}
	
	public void adjustScale(float newDistance){
		canvasScaleFactor += newDistance;
		
		//drawingView.setScale(canvasScaleFactor);
	}
	
}
