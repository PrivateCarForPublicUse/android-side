package com.privatecarforpublic.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.privatecarforpublic.R;
import com.privatecarforpublic.model.MyTravels;
import java.util.List;

/**
 * @date:2019/12/16
 * @author:zhongcz
 */
public class MyTravelsAdapter extends ArrayAdapter<MyTravels> {
    private int resourceId;

    public MyTravelsAdapter(@NonNull Context context, int resource, @NonNull List<MyTravels> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MyTravels myTravels = getItem(position);

        View view;
        ViewHolder viewHolder;
        if (null == convertView) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.date = view.findViewById(R.id.time);
            viewHolder.start = view.findViewById(R.id.start_address);
            viewHolder.end = view.findViewById(R.id.end_address);

            view.setTag(viewHolder);   //将viewHolder存在view中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.date.setText(myTravels.getCarStartTime().toString());
        viewHolder.start.setText(myTravels.getOrigin());
        viewHolder.end.setText(myTravels.getDestination());
        return view;
    }

    class ViewHolder {
        private TextView date;
        private TextView start;
        private TextView end;
    }
}
