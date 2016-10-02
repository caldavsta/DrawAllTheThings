package com.onagainapps.sketchallthethings.DrawingManager;


import android.util.Log;

import java.util.Stack;

/**
 * Created by Caleb on 9/25/2016.
 */

public class SequentialCommandReader {
private static final String TAG = SequentialCommandReader.class.getSimpleName();
	
	private Stack<Command> commandStack;

	public SequentialCommandReader(Stack<Command> commandStack){
		if (commandStack == null){
			Log.d("caleb " + TAG,"commandStack passed to constructor is null!");
		} else{
			Log.d("caleb " + TAG,"SequentialCommandReader created");
		}
		this.commandStack = commandStack;
	}

	public Stack<Command> getCommandStack(){
		return commandStack;
	}
}
