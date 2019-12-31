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
import com.privatecarforpublic.model.MyTravelsUtil;

import java.util.List;

import static com.privatecarforpublic.util.Constants.*;

/**
 * @date:2019/12/16
 * @author:zhongcz
 */
public class MyTravelsAdapter extends ArrayAdapter<MyTravelsUtil> {
    private int resourceId;

    public MyTravelsAdapter(@NonNull Context context, int resource, @NonNull List<MyTravelsUtil> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MyTravelsUtil myTravels = getItem(position);

        View view;
        ViewHolder viewHolder;
        if (null == convertView) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.date = view.findViewById(R.id.time);
            viewHolder.start = view.findViewById(R.id.start_address);
            viewHolder.end = view.findViewById(R.id.end_address);
            viewHolder.status = view.findViewById(R.id.status);

            view.setTag(viewHolder);   //将viewHolder存在view中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.date.setText(myTravels.getCarStartTime());
        viewHolder.start.setText(myTravels.getOrigin());
        viewHolder.end.setText(myTravels.getDestination());
        String STATUS = null;
        int CASE = myTravels.getStatus();
        switch (CASE) {
            case -1:
                STATUS = REVIEW_NOT_PASSED;
                break;
            case 0:
                STATUS = NOT_REVIEW;
                break;
            case 1:
                STATUS = REVIEW_PASSED;
                break;
            case 2:
                STATUS = ON_ROAD;
                break;
            case 3:
                STATUS = FINISHED;
                break;
            case 4:
                STATUS = CANCELED;
                break;
            default:
                STATUS = "未赋值";
        }
        viewHolder.status.setText(STATUS);
        return view;
    }

    class ViewHolder {
        private TextView date;
        private TextView start;
        private TextView end;
        private TextView status;
    }
}
