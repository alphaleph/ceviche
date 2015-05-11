package hu.ait.android.cevicheteam.ceviche;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.net.URL;

import hu.ait.android.cevicheteam.ceviche.Utils.ScaleToFitWidthHeightTransform;


public class SwipeFragment extends android.support.v4.app.Fragment {

    public static final String TAG = "Swipe_Fragment";

    private ImageView ivFood;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_swipe, container, false);
        ivFood = (ImageView) rootView.findViewById(R.id.imageView);
        SwipeActivity swipeActivity = ((SwipeActivity) getActivity());


        String Url = swipeActivity.getIntent().getStringExtra("URL");
        Picasso.with(swipeActivity).load(Url).resize(800, 800).centerInside().into(ivFood);
        return rootView;
    }
}
