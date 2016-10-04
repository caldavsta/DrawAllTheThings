package com.onagainapps.sketchallthethings.Tools;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import com.onagainapps.sketchallthethings.DrawingActivity;
import com.onagainapps.sketchallthethings.DrawingManager.Layer;
import com.onagainapps.sketchallthethings.LayerView;
import com.onagainapps.sketchallthethings.R;
import com.onagainapps.sketchallthethings.SketchAllTheThings;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Caleb on 10/2/2016.
 */

public class ToolArrayAdapter extends BaseExpandableListAdapter {
	private static final String TAG = ToolArrayAdapter.class.getSimpleName();
	
	private ArrayList<Tool> toolArrayList;
	DrawingActivity drawingActivity;
	
	private static class GroupViewHolder {
		TextView titleTextView;
	}
	
	private static class ChildViewHolder {
		ViewGroup toolLayout;
	}
	
	public ToolArrayAdapter(Context context, ArrayList<Tool> toolArrayList){
		this.toolArrayList = toolArrayList;
		drawingActivity = (DrawingActivity) context;
		
	}
	
	
	
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		final GroupViewHolder groupViewHolder;
		Log.d("caleb " + TAG,"Getting there!");
		if (convertView == null){
			groupViewHolder = new GroupViewHolder();
			
			LayoutInflater inflater = LayoutInflater.from(drawingActivity);	
			convertView = inflater.inflate(R.layout.tool_group_view, parent, false);
			
			groupViewHolder.titleTextView = (TextView) convertView.findViewById(R.id.tool_groupTextView);
			
			convertView.setTag(groupViewHolder);
		} else {
			groupViewHolder = (GroupViewHolder) convertView.getTag();
		}
		
		groupViewHolder.titleTextView.setText(toolArrayList.get(groupPosition).getDisplayName());
		
		return convertView;
	}
	
	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		View result = null;
		switch (toolArrayList.get(groupPosition).getToolType()){
			case Tool.PAINT_BRUSH:
				PaintBrush paintBrush = (PaintBrush) toolArrayList.get(groupPosition);
				result = paintBrush.getPaintBrushDrawer().processView(convertView, drawingActivity, parent);
				break;
			case Tool.ERASER:
				Eraser eraser = (Eraser) toolArrayList.get(groupPosition);
				result = eraser.getEraserDrawer().processView(convertView, drawingActivity, parent);
				break;
			default:
				Log.d("caleb " + TAG,"This is a big problem!");
				break;
		}
		
		return result;
	}
	
	
	
	@Override
	public int getGroupCount() {
		return toolArrayList.size();
	}
	
	@Override
	public int getChildrenCount(int groupPosition) {
		return 1;
	}
	
	@Override
	public Object getGroup(int groupPosition) {
		return toolArrayList.get(groupPosition);
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return toolArrayList.get(groupPosition);
	}
	
	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}
	
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}
	
	@Override
	public boolean hasStableIds() {
		return false;
	}
	
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
}
