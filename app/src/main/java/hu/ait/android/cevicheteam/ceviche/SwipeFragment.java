package hu.ait.android.cevicheteam.ceviche;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import in.championswimmer.sfg.lib.SimpleFingerGestures;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import pro.alexzaitsev.freepager.library.view.infinite.Constants;
import pro.alexzaitsev.freepager.library.view.infinite.InfiniteHorizontalPagerAdapter;
import pro.alexzaitsev.freepager.library.view.infinite.ViewFactory;

public class SwipeFragment extends android.support.v4.app.Fragment implements ViewFactory{

    public static final String TAG = "Swipe_Fragment";

    private SwipeActivity swipeActivity;
    private SimpleFingerGestures mySfg = new SimpleFingerGestures();

    Transformation transformation = new RoundedCornersTransformation(100, 0);
    String Url;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        ViewPager verticalPager = (ViewPager) inflater.inflate(R.layout.fragment_swipe, container, false);
        verticalPager.setAdapter(new InfiniteHorizontalPagerAdapter(this, 0));
        verticalPager.setCurrentItem(Constants.START_INDEX);


        swipeActivity = ((SwipeActivity) getActivity());
        getUrl();

        return verticalPager;
    }

    private void getUrl() {
        Url = swipeActivity.getIntent().getStringExtra("URL");
    }

    @Override
    public View makeView(int vertical, int horizontal) {
        ImageView ivFood = new ImageView(swipeActivity);
        Picasso.with(swipeActivity).load(Url).resize(800, 800).transform(transformation).centerInside().into(ivFood);
        return ivFood;
    }

}
