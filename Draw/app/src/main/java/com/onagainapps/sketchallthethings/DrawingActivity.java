package com.onagainapps.sketchallthethings;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.onagainapps.sketchallthethings.DrawingManager.Drawing;
import com.onagainapps.sketchallthethings.DrawingManager.LayerArrayAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class DrawingActivity extends AppCompatActivity {
	private static final String TAG = DrawingActivity.class.getSimpleName();
	
	private BrushSettingsFragment brushSettingsFragment;
	
	public DrawingView drawingView;
	
	private ListView layerListView;
	private DrawerLayout drawerLayout;
	private ListView toolListView;
	
	LayerArrayAdapter adapter;
	
	
	int mStackLevel = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		setContentView(R.layout.activity_drawing);
		
		drawingView = (DrawingView) findViewById(R.id.drawing_canvas);
		
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		
		//Layer Drawer
		layerListView = (ListView) findViewById(R.id.layer_drawer);
		
		Button newLayerButton = (Button) findViewById(R.id.layer_newlayer);
		newLayerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				drawingView.getCurrentDrawing().createNewLayer();
				adapter.notifyDataSetChanged();
			}
		});
		
		//Tool Drawer
		toolListView = (ListView) findViewById(R.id.tool_drawer);
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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
			
			case R.id.action_brush_settings:
				showDialog();
				break;
			
			case R.id.action_share_image:
				shareBitmap(drawingView.getCurrentDrawing().getDrawingAsBitmap(), "MyFile");
				break;
			
			case R.id.action_new_drawing:
				Drawing drawing = Drawing.getNewDefaultDrawing(drawingView);
				drawingView.setCurrentDrawing(drawing);
				setupLayerAdapter();
				adapter.setDrawing(drawing);
				break;
			
			case R.id.action_watch:
				if (SketchAllTheThings.getInstance().getCurrentTool() == SketchAllTheThings.Tool.BRUSH){
					SketchAllTheThings.getInstance().setCurrentTool(SketchAllTheThings.Tool.CANVAS);
				} else {
					
					SketchAllTheThings.getInstance().setCurrentTool(SketchAllTheThings.Tool.BRUSH);
				}
				
				
				
				
				//SketchAllTheThings.getInstance().tempCommandStack = drawingView.getDrawingInputManager().getCommandList();
				//Intent i = new Intent(this, WatchingActivity.class);
				//startActivity(i);
				break;
			
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void setupLayerAdapter(){
		adapter = drawingView.getCurrentDrawing().getLayerArrayAdapterForLayerList(this, drawingView);
		layerListView.setAdapter(adapter);
		adapter.setDrawing(drawingView.getCurrentDrawing());
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
	
	void showDialog() {
		mStackLevel++;
		
		// DialogFragment.show() will take care of adding the fragment
		// in a transaction.  We also want to remove any currently showing
		// dialog, so make our own transaction and take care of that here.
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		
		// Create and show the dialog
		if (brushSettingsFragment == null) {
			brushSettingsFragment = BrushSettingsFragment.newInstance();
		}
		brushSettingsFragment.show(ft, "dialog");
	}
	
}
