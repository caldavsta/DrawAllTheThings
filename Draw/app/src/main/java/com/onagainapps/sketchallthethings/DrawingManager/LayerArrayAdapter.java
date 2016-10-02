package com.onagainapps.sketchallthethings.DrawingManager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.onagainapps.sketchallthethings.DrawingView;
import com.onagainapps.sketchallthethings.LayerView;
import com.onagainapps.sketchallthethings.R;

import java.util.ArrayList;

/**
 * Created by Caleb on 9/29/2016.
 */

public class LayerArrayAdapter extends ArrayAdapter<Layer> {
	private static final String TAG = LayerArrayAdapter.class.getSimpleName();
	
	private Drawing drawing;
	private ArrayList<Layer> dataSet;
	private DrawingView drawingView;
	
	private static class ViewHolder {
		Button visibilityButton;
		Button editingButton;
		Button moveUpButton;
		Button moveDownButton;
		LayerView layerView;
		Layer layer;
	}
	
	public LayerArrayAdapter(Context context, ArrayList<Layer> layers, DrawingView drawingView) {
		super(context, R.layout.item_layer, layers);
		dataSet = layers;
		drawing = drawingView.getCurrentDrawing();
		this.drawingView = drawingView;
	}
	
	@NonNull
	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {
		final ViewHolder viewHolder;
		
		if (convertView == null){
			viewHolder = new ViewHolder();
			
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.item_layer, parent, false);
			
			viewHolder.layerView = (LayerView) convertView.findViewById(R.id.layerview_layerView);
			viewHolder.visibilityButton = (Button) convertView.findViewById(R.id.layerview_togglevisible);
			viewHolder.editingButton = (Button) convertView.findViewById(R.id.layerView_toggleediting);
			viewHolder.moveDownButton = (Button) convertView.findViewById(R.id.layerView_movedown);
			viewHolder.moveUpButton = (Button) convertView.findViewById(R.id.layerView_moveup);
			
			convertView.setTag(viewHolder);
			
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.layer = dataSet.get(position);
		viewHolder.layerView.postInvalidate();
		viewHolder.layerView.setLayer(viewHolder.layer);
		
		
		
		viewHolder.visibilityButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewHolder.layer.setVisible(!viewHolder.layer.isVisible());
				notifyDataSetChanged();
				drawingView.postInvalidate();
			}
		});
		
		viewHolder.editingButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				drawing.setLayerBeingEdited(viewHolder.layer);
				notifyDataSetChanged();
			}
		});
		
		viewHolder.moveUpButton.setVisibility(drawing.isLayerAtBottom(viewHolder.layer)? View.INVISIBLE : View.VISIBLE);
		viewHolder.moveUpButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				drawing.moveLayerDown(viewHolder.layer);
				notifyDataSetChanged();
			}
		});
		
		
		viewHolder.moveDownButton.setVisibility(drawing.isLayerAtTop(viewHolder.layer)? View.INVISIBLE : View.VISIBLE);
		viewHolder.moveDownButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				drawing.moveLayerUp(viewHolder.layer);
				notifyDataSetChanged();
			}
		});
		
		viewHolder.layerView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Log.d("caleb " + TAG,"Layer: " + viewHolder.layerView.getWidth() + " x " + viewHolder.layerView.getHeight() + " Layer: " + layer.getWidth() + " x " + layer.getHeight());
				//TODO make a rename dialog when layer is clicked
			}
		});
		viewHolder.visibilityButton.setText((viewHolder.layer.isVisible()? "✓":"X"));
		viewHolder.editingButton.setText((viewHolder.layer == drawing.getLayerBeingEdited())? "✓":"");
		//viewHolder.layerView.setDrawingFromLayer(layer);
		
		
		return convertView;
		
		
	}
	
	public void setDrawing(Drawing drawing) {
		this.drawing = drawing;
	}
	
	public void setDrawingView(DrawingView drawingView) {
		this.drawingView = drawingView;
	}
}
