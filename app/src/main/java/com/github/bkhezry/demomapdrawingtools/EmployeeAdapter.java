package com.github.bkhezry.demomapdrawingtools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import static rx.plugins.RxJavaHooks.clear;

public class EmployeeAdapter extends ArrayAdapter<Employee> {

    Context context;
    int resource, textViewResourceId;
    List<Employee> items, tempItems, suggestions;

    public EmployeeAdapter(Context context, int resource, int textViewResourceId, List<Employee> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<Employee>(items); // this makes the difference.
        suggestions = new ArrayList<Employee>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_item, parent, false);
        }
        Employee employee = items.get(position);
        if (employee != null) {
            TextView lblName = (TextView) view.findViewById(R.id.lbl_name);
            ImageView usericon = (ImageView) view.findViewById(R.id.usericon);
            if (lblName != null) {
                lblName.setText(employee.getlastName());
                Glide.with(context).load(employee.getImg())
                        .centerCrop()
                        .dontAnimate()
                        .thumbnail(0.5f)
                        .placeholder(R.drawable.image_placeholder)
                        .into(usericon);
            }

        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((Employee) resultValue).getlastName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Employee employee : tempItems) {
                    if (employee.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(employee);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<Employee> filterList = (ArrayList<Employee>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Employee employee : filterList) {
                    add(employee);
                    notifyDataSetChanged();
                }
            }
        }
    };
}