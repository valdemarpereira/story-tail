package com.valdemar.tellatale.adapter;

import java.util.List;

import com.valdemar.tellatale.R;
import com.valdemar.tellatale.common.TaleApplicationContext;
import com.valdemar.tellatale.model.Tail;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TaleTailsAdapter extends ArrayAdapter<Tail>
{
	private LayoutInflater mInflater = null;
	private Resources resources;
	private ViewHolder holder = null;
	private final List<Tail> values;
	private TaleApplicationContext myContext;

	public TaleTailsAdapter(TaleApplicationContext context,List<Tail> values)
	{
		super(context, R.layout.tail_single_item_layout, values);
		this.mInflater = LayoutInflater.from(context);
		this.values = values;
		resources = context.getResources();
		myContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{

		Tail tail = values.get(position);

		if (tail == null)
			return null;

		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.tail_single_item_layout, null);
			holder = new ViewHolder();

			holder.story = (TextView) convertView.findViewById(R.id.tail_story);
			holder.date = (TextView) convertView.findViewById(R.id.tail_date);
			holder.user = (TextView) convertView.findViewById(R.id.tail_user);
			holder.place = (TextView) convertView.findViewById(R.id.tail_place);

			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		holder.story.setTypeface(myContext.getStoryFont());
		holder.story.setText(tail.getStory());
		holder.date.setText(resources.getString(R.string.tail_date, tail.getFormatedDate()));
		holder.user.setText(resources.getString(R.string.tail_user, tail.getCreatedby()));
		holder.place.setText(tail.getCityAndPlace());


		return convertView;
	}

	static class ViewHolder
	{
		TextView story;
		TextView date;
		TextView user;
		TextView place;
	}
}