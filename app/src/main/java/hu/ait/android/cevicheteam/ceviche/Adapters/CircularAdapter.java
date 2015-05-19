package hu.ait.android.cevicheteam.ceviche.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jpardogo.listbuddies.lib.adapters.CircularLoopAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import hu.ait.android.cevicheteam.ceviche.R;
import hu.ait.android.cevicheteam.ceviche.Utils.ScaleToFitWidthHeightTransform;

public class CircularAdapter extends CircularLoopAdapter {

    private static final String TAG = CircularAdapter.class.getSimpleName();

    private List<String> mItems = new ArrayList<>();

    private Context mContext;
    private int mRowHeight;

    public CircularAdapter(Context context, int rowHeight, List<String> imagesUrl) {
        mContext = context;
        mRowHeight = rowHeight;
        mItems = imagesUrl;
    }

    protected Context getContext() {
        return mContext;
    }

    protected int getRowHeight() {
        return mRowHeight;
    }

    @Override
    protected int getCircularCount() {
        return mItems.size();
    }

    @Override
    public String getItem(int position) {
        return mItems.get(getCircularPosition(position));
    }

    static class ViewHolder {
        ImageView image;

        public ViewHolder(View convertView) {
            image = (ImageView) convertView.findViewById(R.id.image);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.image.setMinimumHeight(mRowHeight);

        Picasso.with(mContext).load(getItem(position)).transform(new ScaleToFitWidthHeightTransform(mRowHeight, true)).into(holder.image);

        return convertView;
    }
}