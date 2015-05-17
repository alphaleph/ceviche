package hu.ait.android.cevicheteam.ceviche.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jpardogo.listbuddies.lib.adapters.CircularLoopAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hu.ait.android.cevicheteam.ceviche.R;

/**
 * Created by Chau on 5/17/2015.
 */
public class DefaultImageCircularAdapter extends CircularAdapter {

    public static final int NUM_DEFAULT_DRAWABLE = 1;

    public DefaultImageCircularAdapter(Context context, int rowHeight) {
        super(context, rowHeight, null);
    }

    @Override
    protected int getCircularCount() {
        return NUM_DEFAULT_DRAWABLE;
    }

    @Override
    public String getItem(int position) {
        return null;
    }

    static class ViewHolder {
        @InjectView(R.id.image)
        ImageView image;

        public ViewHolder(View convertView) {
            ButterKnife.inject(this, convertView);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(super.getContext()).inflate(R.layout.item_list, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.image.setMinimumHeight(super.getRowHeight());
        holder.image.setImageResource(R.drawable.shrimpy);

        return convertView;
    }
}