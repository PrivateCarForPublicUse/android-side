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

import org.w3c.dom.Text;

import java.util.List;

public class CarAdapter extends BaseAdapter{
    private static final String MILE = "公里";
    private List<Car> mList;
    private Context mContext;
    //用来判别绑定的  _item.xml
    private int pageKind;

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

    public CarAdapter(Context _context, List<Car> _list,int pageKind) {
        this.mContext = _context;
        this.mList=_list;
        this.pageKind = pageKind;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            if(pageKind == 1) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.car_item, parent, false);
                holder.picture = convertView.findViewById(R.id.picture);
                holder.license = convertView.findViewById(R.id.license);
                holder.type = convertView.findViewById(R.id.type);
                holder.star = convertView.findViewById(R.id.star);
            }
            else if(pageKind == 2){
                convertView = LayoutInflater.from(mContext).inflate(R.layout.my_car_item, parent, false);
                holder.picture = convertView.findViewById(R.id.my_cars_img);
                holder.mileage = convertView.findViewById(R.id.my_cars_mileage);
                holder.license = convertView.findViewById(R.id.my_cars_licence);
                holder.star = convertView.findViewById(R.id.my_cars_star1);
            }
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        Car car = getItem(position);
        //Picasso.get().load(car.getPicture()).into(holder.picture);
        if(pageKind == 1){
            holder.license.setText(car.getLicense());
            holder.type.setText(car.getType());
            holder.star.setText(car.getStarOfCar()+"");
        }
        else if(pageKind == 2){
            holder.license.setText(car.getLicense());
            //`holder.brand.setText(car.getBrand());
            holder.mileage.setText(car.getMileage()+MILE);
            holder.star.setText(car.getStarOfCar()+"");
        }

        return convertView;
    }

    private class ViewHolder {
        private ImageView picture;
        private TextView mileage;
        private TextView brand;
        private TextView license;
        private TextView type;
        private TextView star;
    }
}
