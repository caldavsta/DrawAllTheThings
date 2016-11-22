package com.onagainapps.sketchallthethings.DrawingManager;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Caleb on 9/24/2016.
 * 
 * A Command could be a BrushStroke, and EraserStroke. In the future it could draw an imported image to the layer.
 * Each command is stored in a stack which is 'pop'ped during an undo operation.
 * Each command is drawn to a layer, one-by-one.
 * While a command is being drawn, it is drawn on-top of Layer's Bitmap.
 * Once a command is completed (like when a user lifts their finger from the screen), all commands in a layer are redrawn.
 */
public abstract class Command {
	public abstract void draw(Canvas canvas);
	public abstract Rect getBounds();
}
