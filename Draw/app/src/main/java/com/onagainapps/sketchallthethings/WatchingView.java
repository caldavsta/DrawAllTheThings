package com.onagainapps.sketchallthethings;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.onagainapps.sketchallthethings.DrawingManager.BrushStroke;
import com.onagainapps.sketchallthethings.DrawingManager.Command;
import com.onagainapps.sketchallthethings.DrawingManager.SequentialCommandReader;

/**
 * Created by Caleb on 9/24/2016.
 */
public class WatchingView extends View {
	private static final String TAG = WatchingView.class.getSimpleName();

	private SequentialCommandReader sequentialCommandReader;

	private Bitmap bitmapBuffer;

	private Canvas bufferCanvas;

	public boolean bufferUpdated;

	private int backgroundColor;

	private Paint paintDrawCommand;

	public WatchingView(Context context, AttributeSet attrs) {
		super(context, attrs);

		backgroundColor = SketchAllTheThings.getInstance().DEFAULT_BACKGROUND_COLOR;

		int width = context.getResources().getDisplayMetrics().widthPixels;
		int height = context.getResources().getDisplayMetrics().heightPixels;

		bitmapBuffer = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		bitmapBuffer.eraseColor(backgroundColor);
		bufferCanvas = new Canvas(bitmapBuffer);

		paintDrawCommand = new Paint();

		setupPaint(paintDrawCommand);


		bufferUpdated = false;

		Log.d("caleb " + TAG, "WatchingView created");
	}


	public void onDraw(Canvas canvas) {
		if (!this.isInEditMode()) {
			if (!sequentialCommandReader.getCommandStack().empty()) {
				canvas.drawBitmap(bitmapBuffer, 0, 0, paintDrawCommand);


				Command command = sequentialCommandReader.getCommandStack().lastElement();
				//if (command.drawingCurrently){
				//	drawCommandToCanvas(command, canvas);
				//}

			}

		} else {
			Paint paint = new Paint();
			paint.setColor(Color.WHITE);
			paint.setStyle(Paint.Style.FILL);
			canvas.drawPaint(paint);

			paint.setColor(Color.BLACK);
			paint.setTextSize(20);
			canvas.drawText("Some Text", 10, 25, paint);
		}
	}

	private void drawCommandToCanvas(Command command, Canvas canvas){
		if (command.getClass() == BrushStroke.class) {
			BrushStroke brushStroke = (BrushStroke) command;
			Point lastPoint = null;

			//perform brush stroke
			paintDrawCommand.setColor(brushStroke.getBrushColor());
			paintDrawCommand.setStrokeWidth(brushStroke.getBrushSize());

			for (Point point : brushStroke.getPointList()) {
				if (brushStroke.getPointList().size() == 1) {
					canvas.drawCircle(point.x, point.y, brushStroke.getBrushSize()/2, paintDrawCommand);
				}
				//canvas.drawPoint(point.x, point.y, paintDrawCommand);
				//canvas.drawCircle(point.x,point.y,10, paintDrawCommand);
				if (lastPoint != null) {
					canvas.drawLine(lastPoint.x, lastPoint.y, point.x, point.y, paintDrawCommand);
				}
				lastPoint = point;
			}

		}
	}

	private void setupPaint(Paint paint){
		paint.setDither(true);                    // set the dither to true
		paint.setStyle(Paint.Style.STROKE);       // set to STOKE
		paint.setStrokeJoin(Paint.Join.ROUND);    // set the join to round you want
		paint.setStrokeCap(Paint.Cap.ROUND);      // set the paint cap to round too
		//paintDrawCommand.setPathEffect(new CornerPathEffect(10) );   // set the path effect when they join.
		paint.setAntiAlias(true);
	}

	private void drawCommandToBuffer(Command command){
		drawCommandToCanvas(command, bufferCanvas);
	}



}