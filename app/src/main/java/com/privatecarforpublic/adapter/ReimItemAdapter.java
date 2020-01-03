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
import com.privatecarforpublic.model.Reimburse;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @date:2019/12/7
 * @author:zhongcz
 * @description: 报销管理页面的listview  Item的适配器
 */
public class ReimItemAdapter extends ArrayAdapter<Reimburse> {
    private int resourceId;

    public ReimItemAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<Reimburse> objects) {
        super(context, textViewResourceId, objects);
        this.resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Reimburse reimburse = getItem(position);

        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.img = view.findViewById(R.id.reim_item_headImage);
            viewHolder.time = view.findViewById(R.id.reim_item_time);
            viewHolder.start = view.findViewById(R.id.reim_item_start);
            viewHolder.end = view.findViewById(R.id.reim_item_end);
            viewHolder.isReimburse = view.findViewById(R.id.reim_item_status);

            view.setTag(viewHolder);   //将viewHolder存在view中
        } else {
            view = convertView;  //进行了listView的性能优化，滑动的时候感觉不会那么卡
            viewHolder = (ViewHolder) view.getTag(); //获取
        }

       /* viewHolder.img.setImageResource(reimburse.getImgId());*/
        viewHolder.time.setText(reimburse.getApplyTime());
        viewHolder.start.setText(reimburse.getStart());
        viewHolder.end.setText(reimburse.getEnd());
        String status = null;
        switch (reimburse.getIsReimburse()) {
            case -1:
                status = "报销失败";
                break;
            case 0:
                status = "未报销";
                break;
            case 1:
                status = "已报销";
                break;
            case 2:
                status = "审核中";
                break;
            default:
                status = "报销状态不合理";
                break;
        }
        viewHolder.isReimburse.setText(status);
        return view;
    }

    class ViewHolder {
        private CircleImageView img;
        private TextView time;
        private TextView start;
        private TextView end;
        private TextView isReimburse;
    }
}
