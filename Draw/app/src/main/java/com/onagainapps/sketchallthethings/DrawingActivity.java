package com.onagainapps.sketchallthethings;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
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
import java.io.FileOutputStream;

public class DrawingActivity extends AppCompatActivity implements DrawerLayout.DrawerListener {
	private static final String TAG = DrawingActivity.class.getSimpleName();
	
	
	public DrawingView drawingView;
	
	private ListView layerListView;
	private DrawerLayout drawerLayout;
	private ExpandableListView toolListView;
	
	private ActionBar actionBar;
	
	private ColorPicker colorPicker;
	
	
	LayerArrayAdapter layerArrayAdapter;
	ToolArrayAdapter toolArrayAdapter;
	
	
	int mStackLevel = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		setContentView(R.layout.activity_drawing);
		 
		drawingView = (DrawingView) findViewById(R.id.drawing_canvas);
		
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerLayout.addDrawerListener(this);
		drawerLayout.setOnDragListener(new View.OnDragListener() {
			@Override
			public boolean onDrag(View v, DragEvent event) {
				return true;
			}
		});
		
		//Layer Drawer
		layerListView = (ListView) findViewById(R.id.layer_drawer);
		
		Button newLayerButton = (Button) findViewById(R.id.layer_newlayer);
		newLayerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				drawingView.getCurrentDrawing().createNewLayer();
				layerArrayAdapter.notifyDataSetChanged();
			}
		});
		
		//Tool Drawer
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
		
		//Tool Drawer Color Picker
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
			}
		});
		
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		int count = menu.size();
		for (final Tool thisTool : SketchAllTheThings.getInstance().getTools()){
			int thisToolId = Tool.TOOL_PREFIX  + thisTool.getToolType();
			MenuItem thisMenuItem = menu.add(5,thisToolId, count, thisTool.getDisplayName());
			thisMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			thisMenuItem.setIcon(thisTool.getIconDrawable());
			thisMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					SketchAllTheThings.getInstance().setCurrentTool(thisTool);
					Toast.makeText(getBaseContext(), thisTool.getDisplayName() + " selected.", Toast.LENGTH_SHORT);
					return true;
				}
			});
			count++;
		}
		
		
		getMenuInflater().inflate(R.menu.menu_drawingcanvas, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
			case R.id.action_undo:
				drawingView.getCurrentDrawing().undo();
				break;
			
			case R.id.action_redo:
				drawingView.getCurrentDrawing().redo();
				break;
			
			case R.id.action_share_image:
				shareBitmap(drawingView.getCurrentDrawing().getDrawingAsBitmap(), "MyFile");
				break;
			
			case R.id.action_new_drawing:
				Drawing drawing = Drawing.getNewDefaultDrawing(drawingView);
				drawingView.setCurrentDrawing(drawing);
				setupLayerAdapter();
				layerArrayAdapter.setDrawing(drawing);
				break;
			
			default:
				
				
				
				break;

		}
		return super.onOptionsItemSelected(item);
	}
	
	public void setupLayerAdapter(){
		layerArrayAdapter = drawingView.getCurrentDrawing().getLayerArrayAdapterForLayerList(this, drawingView);
		layerListView.setAdapter(layerArrayAdapter);
		layerArrayAdapter.setDrawing(drawingView.getCurrentDrawing());
	}
	
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
		
	}
	
	@Override
	public void onDrawerOpened(View drawerView) {
		drawerView.bringToFront();
		
		Log.d("caleb " + TAG,"Drawer Opened");
	}
	
	@Override
	public void onDrawerClosed(View drawerView) {
		colorPicker.setOldCenterColor(SketchAllTheThings.getInstance().getColor());
		colorPicker.postInvalidate();
	}

	@Override
	public void onDrawerStateChanged(int newState) {
		
	}
}
