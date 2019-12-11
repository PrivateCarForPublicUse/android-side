package com.privatecarforpublic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.privatecarforpublic.R;
import com.privatecarforpublic.model.Car;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CarAdapter extends BaseAdapter{
    private List<Car> mList;
    private Context mContext;

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Car getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public CarAdapter(Context _context, List<Car> _list) {
        this.mContext = _context;
        this.mList=_list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.car_item, parent, false);
            holder.picture = convertView.findViewById(R.id.picture);
            holder.license = convertView.findViewById(R.id.license);
            holder.type = convertView.findViewById(R.id.type);
            holder.star = convertView.findViewById(R.id.star);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        Car car = getItem(position);
        //Picasso.get().load(car.getPicture()).into(holder.picture);
        holder.license.setText(car.getLicense());
        holder.type.setText(car.getType());
        holder.star.setText(car.getStarOfCar()+"");
        return convertView;
    }

    private class ViewHolder {
        private ImageView picture;
        private TextView license;
        private TextView type;
        private TextView star;
    }
}
