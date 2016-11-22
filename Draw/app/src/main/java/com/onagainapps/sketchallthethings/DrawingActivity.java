package com.onagainapps.sketchallthethings;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;
import com.onagainapps.sketchallthethings.DrawingManager.Drawing;
import com.onagainapps.sketchallthethings.DrawingManager.LayerArrayAdapter;
import com.onagainapps.sketchallthethings.Tools.Tool;
import com.onagainapps.sketchallthethings.Tools.ToolArrayAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

/*
SketchAllTheThings is a drawing app for Android. It contains several
	 functions which can be extrapolated upon. For instance, layered drawings,
	 Views which display individual layers or the entire Drawing.
    Copyright (C) 2016  Caleb Stamper

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * DrawingActivity is the main activity in the app. It is a DrawerLayout with a left and right drawer.
 * Left Drawer: Has a color-picker (https://github.com/LarsWerkman/HoloColorPicker), and lists the available tools, which get inflated into an expandable listview. Each listview item contains a set of properties for the tool (size is the only one so-far)
 * Right Drawer: Lists all layers in the current Drawing. Drawing is performed on only one layer at a time. Layers are drawn in DrawingView starting at the bottom of the layer list and ending at the top. The layers can be reorganized, deleted, and hidden independently.
 * The AppBar is where tools are chosen. The Undo and Redo buttons are here. The top bar also displays the currently selected color.
 * The DrawingView is the main part of this activity. It is where the current drawing is shown and is the view that a user interacts with.
 */
public class DrawingActivity extends AppCompatActivity implements DrawerLayout.DrawerListener {
	private static final String TAG = DrawingActivity.class.getSimpleName();
	private static final int REQUEST_WRITE_STORAGE = 112;
	
	/**
	 * The Drawing view.
	 * This is where the drawing appears.
	 */
	public DrawingView drawingView;
	private ListView layerListView;
	private DrawerLayout drawerLayout;
	private ExpandableListView toolListView;
	private ColorPicker colorPicker;
	
	
	/**
	 * The Layer array adapter. For separating the drawing's layers into separate views. (the right Drawer)
	 */
	LayerArrayAdapter layerArrayAdapter;
	
	/**
	 * The Tool array adapter. For separating all tools and their properties into views. (left Drawer, under color-picker)
	 */
	ToolArrayAdapter toolArrayAdapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		setContentView(R.layout.activity_drawing);
		 
		//setup local vars
		drawingView = (DrawingView) findViewById(R.id.drawing_canvas);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		layerListView = (ListView) findViewById(R.id.layer_drawer);
		
		//setup the activity's layout
		drawerLayout.addDrawerListener(this);
		
		//Setup the Layer Drawer
		Button newLayerButton = (Button) findViewById(R.id.layer_newlayer);
		newLayerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				drawingView.getCurrentDrawing().createNewLayer();
				layerArrayAdapter.notifyDataSetChanged();
			}
		});
		
		//Setup the Tool Drawer
		toolListView = (ExpandableListView) findViewById(R.id.toolbelt_listview);
		toolArrayAdapter = new ToolArrayAdapter(this, SketchAllTheThings.getInstance().getTools());
		toolListView.setAdapter(toolArrayAdapter);
		toolListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				Log.d("caleb " + TAG,"Got that!");
				toolListView.expandGroup(groupPosition);
				return true;
			}
		});
		
		//Setup tool drawer's color picker
		colorPicker = (ColorPicker) findViewById(R.id.color_picker);
		SVBar svBar = (SVBar) findViewById(R.id.color_svbar);
		OpacityBar opacityBar = (OpacityBar) findViewById(R.id.color_opacitybar);
		SaturationBar saturationBar = (SaturationBar) findViewById(R.id.color_saturationbar);
		ValueBar valueBar = (ValueBar) findViewById(R.id.color_valuebar);
		
		colorPicker.addSVBar(svBar);
		colorPicker.addOpacityBar(opacityBar);
		colorPicker.addSaturationBar(saturationBar);
		colorPicker.addValueBar(valueBar);
		colorPicker.setColor(SketchAllTheThings.getInstance().getColor());
		
		colorPicker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
			@Override
			public void onColorChanged(int color) {
				SketchAllTheThings.getInstance().setCurrentColor(color);
				supportInvalidateOptionsMenu();
			}
		});

	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	/**
	 * Used in newer android version for runtime permissions verification. 
	 * @deprecated Only a temporary feature for testing.
	 * @param requestCode the permission to test for
	 * @param permissions 
	 * @param grantResults
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case REQUEST_WRITE_STORAGE: {
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					//reload my activity with permission granted or use the features what required the permission
				} else {
					Toast.makeText(this, "You have chosen not to grant write permission. Your images will not be saved.", Toast.LENGTH_LONG).show();
				}
			}
		}
	}
		
		@Override
	protected void onStop() {
		super.onStop();
			
			//The Activity is stopping. Save it to storage so it won't get lost.
		if (drawingView.getCurrentDrawing() != null){
			if (isExternalStorageWritable()){
				storeImage(drawingView.getCurrentDrawing().getDrawingAsBitmap());
			}
		}

	}
	
	private void checkIfWritePermissions(){
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED) {
			// Check Permissions Now
			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
					REQUEST_WRITE_STORAGE);
		}
	}
	
	/**
	 * Is external storage writable boolean.
	 *
	 * @return the boolean
	 */
	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Gets file for saving.
	 *
	 * @return the file for saving
	 */
	public File getFileForSaving() {
		// Get the directory for the app's private pictures directory.
		Calendar rightNow = Calendar.getInstance();
		File mediaFile = null;
		File rootsd = Environment.getExternalStorageDirectory();
		File file = new File(rootsd.getPath() + rootsd.separator + "DCIM" + rootsd.separator, getResources().getString(R.string.app_name));
		
		if (!file.exists()){
			if (file.mkdir()){
				Log.d("caleb " + TAG,"Directory created");
			} else {
				Log.d("caleb " + TAG,"Cant create directory. Canwrite? " + file.canWrite() + " isdirectory?" + file.isDirectory());
			}
		}
		mediaFile = new File(file.getPath() + File.separator +
				"Sketch " + rightNow.getTimeInMillis() + ".jpg");

		
		return mediaFile;
	}
	
	/**
	 * Saves the current drawing to Storage.
	 * @deprecated only a temporary feature for testing
	 * @param image the Bitmap of the current drawing
	 */
	private void storeImage(Bitmap image) {
		File pictureFile = getFileForSaving();
		if (pictureFile == null) {
			Log.d(TAG,
					"Error creating media file, check storage permissions: ");// e.getMessage());
			return;
		}
		try {
			FileOutputStream fos = new FileOutputStream(pictureFile);
			image.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.close();
		} catch (FileNotFoundException e) {
			Log.d(TAG, "File not found: " + e.getMessage());
		} catch (IOException e) {
			Log.d(TAG, "Error accessing file: " + e.getMessage());
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		addToolsToAppBar(menu);
		
		getMenuInflater().inflate(R.menu.menu_drawingcanvas, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		
		//Sets the AppBar's icons' background colors to indicate which tool and color are currently selected.
		// todo make icons look neater
		for (int i = 0; i < menu.size(); i++){
			if (menu.getItem(i).getTitle().equals(SketchAllTheThings.getInstance().getCurrentTool().getDisplayName())){
				menu.getItem(i).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.DARKEN);
			} else {
				if (menu.getItem(i).getIcon() != null){//if it has an icon
						menu.getItem(i).getIcon().clearColorFilter();
				}
			}
			if (menu.getItem(i).getItemId() == R.id.action_color_picker){
				menu.getItem(i).getIcon().setColorFilter(SketchAllTheThings.getInstance().getColor(), PorterDuff.Mode.DST_ATOP);
			}
		}
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		supportInvalidateOptionsMenu(); 
		
		//handle the menu selection. Undo, Redo, Share, New Drawing
		switch (item.getItemId()) {
			case R.id.action_undo:
				if (drawingView.getCurrentDrawing() != null){ 
					drawingView.getCurrentDrawing().undo();
				}
				break;
			
			case R.id.action_redo:
				if (drawingView.getCurrentDrawing() != null) {
					drawingView.getCurrentDrawing().redo();
				}
				break;
			
			case R.id.action_share_image:
				if (drawingView.getCurrentDrawing() != null) {
					shareBitmap(drawingView.getCurrentDrawing().getDrawingAsBitmap(), "MyFile");
				}
				break;
			
			case R.id.action_new_drawing:
				startNewDrawing();
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	/**
	 * Inflates each tool (and it's parameters) into the tool list in the left drawer. For example: Inflate the Brush tool and it's 'size' parameter, which shows as a seekbar.
	 * @param appBar the menu which tools get inflated to
	 */
	private void addToolsToAppBar(Menu appBar){
		int count = appBar.size();
		for (final Tool thisTool : SketchAllTheThings.getInstance().getTools()){
			int thisToolId = Tool.TOOL_PREFIX  + thisTool.getToolType();
			MenuItem thisMenuItem = appBar.add(5,thisToolId, count, thisTool.getDisplayName());
			thisMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			thisMenuItem.setTitle(thisTool.getDisplayName());
			thisMenuItem.setIcon(thisTool.getIconDrawable());
			thisMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					SketchAllTheThings.getInstance().setCurrentTool(thisTool);
					
					supportInvalidateOptionsMenu();
					Log.d("caleb " + TAG,thisTool.getDisplayName() + " selected.");
					return true;
				}
			});
			count++;
		}
	}
	
	/**
	 * Begins a new drawing which is sized to fit the DrawingView's dimensions
	 */
	private void startNewDrawing(){
		checkIfWritePermissions();
		Drawing drawing = Drawing.getNewDefaultDrawing(drawingView);
		drawingView.setCurrentDrawing(drawing);
		setupLayerAdapter();
		layerArrayAdapter.setDrawing(drawing);
	}
	
	/**
	 * sets up the layer adapter, which is attached to the list in the right drawer
	 */
	public void setupLayerAdapter(){
		layerArrayAdapter = drawingView.getCurrentDrawing().getLayerArrayAdapterForLayerList(this, drawingView);
		layerListView.setAdapter(layerArrayAdapter);
		layerArrayAdapter.setDrawing(drawingView.getCurrentDrawing());
	}
	
	/**
	 * Brings up the share dialog and sends the current drawing's Bitmap as image/png
	 * @param bitmap the Bitmap of the current Drawing
	 * @param fileName the name of the file which will be saved
	 */
	private void shareBitmap(Bitmap bitmap, String fileName) {
		try {
			File file = new File(this.getCacheDir(), fileName + ".png");
			FileOutputStream fOut = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			fOut.flush();
			fOut.close();
			file.setReadable(true, false);
			final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
			intent.setType("image/png");
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void onDrawerSlide(View drawerView, float slideOffset) {
		drawerView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("caleb " + TAG," " + v.getId());
			}
		});
	}
	
	@Override
	public void onDrawerOpened(View drawerView) {
		drawerView.bringToFront();
		
		Log.d("caleb " + TAG,"Drawer Opened");
	}
	
	@Override
	public void onDrawerClosed(View drawerView) {
		colorPicker.setOldCenterColor(SketchAllTheThings.getInstance().getColor()); // make sure the color picker's color is set to the user's current tool color
		colorPicker.postInvalidate(); //refresh the colorPicker
	}
	
	/**
	 * Only necessary for implementing DrawerListener. Does nothing.
	 * @param newState
	 */
	@Override
	public void onDrawerStateChanged(int newState) {
		
	}
	

}
