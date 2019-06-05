package com.skydt.weightcontrol.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.skydt.weightcontrol.R;
import com.skydt.weightcontrol.models.Day;

import java.util.List;
import java.util.Locale;

public class DayAdapter extends ArrayAdapter
{
    private final int layoutResource;
    private final LayoutInflater layoutInflater;
    private List<Day> days;

    public DayAdapter(Context context, int resource, List<Day> days)
    {
        super(context, resource);
        this.layoutInflater = LayoutInflater.from(context);
        this.layoutResource = resource;
        this.days = days;
    }

    @Override
    public int getCount()
    {
        return days.size();
    }

    @Override
    public Object getItem(int position)
    {
        return days.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder;
        if (convertView == null)
        {
            convertView = layoutInflater.inflate(layoutResource, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Day day = days.get(position);
        viewHolder.tvDate.setText(day.getDateAsDanishDisplayText());
        viewHolder.tvGoalWeight.setText(String.format(Locale.getDefault(), "%.1f", day.getGoalWeight()));
        viewHolder.tvGoalWeight.append(" kg");

        if (day.getLike())
        {
            viewHolder.tvDate.setTextColor(Color.parseColor("#53C557"));
            viewHolder.tvGoalWeight.setTextColor(Color.parseColor("#53C557"));
        }

        return convertView;
    }

    private class ViewHolder
    {
        final TextView tvDate;
        final TextView tvGoalWeight;

        ViewHolder(View v)
        {
            this.tvDate = v.findViewById(R.id.tvDate);
            this.tvGoalWeight = v.findViewById(R.id.tvGWeight);
        }
    }
}
