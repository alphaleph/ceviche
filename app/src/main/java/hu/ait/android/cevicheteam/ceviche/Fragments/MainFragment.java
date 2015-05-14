package hu.ait.android.cevicheteam.ceviche.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.jpardogo.listbuddies.lib.views.ListBuddiesLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hu.ait.android.cevicheteam.ceviche.Adapters.CircularAdapter;
import hu.ait.android.cevicheteam.ceviche.MainActivity;
import hu.ait.android.cevicheteam.ceviche.R;


public class MainFragment extends android.support.v4.app.Fragment implements ListBuddiesLayout.OnBuddyItemClickListener {

    public static final String TAG = "Home_Fragment";

    public final static String[] imageUrls_left = new String[]{
            "https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcSlzm9ECd0-C4V5MdsyFH5F8TAXE4S2amI1pAaFfd7csFLQ2AD-4Q",
            "http://www.applebees.com/~/media/2012_menu_images/Menu%20C/579x441/Desserts/Desserts_Brownie_Bite.jpg",
            "http://davidduncanhouse.com/wp/wp-content/uploads/2014/07/Chocolate-mousse-cake-with-strawberry.jpg",
            "https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcSLQ0O_VUN_XcJItRcnzw4uznDFI6QHeXTWfNgICsK2y-9tEv-I",
            "https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcQLhaxZOImFClMw-o3IJTciOJ14W_xl5GV0JK5ZcsZ6Gt1009iRJQ",
            "https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcSDcGiolZ6G2Wt_rLmks-35vpiNiZ2TwwuABDSR5oO1Nxao_GBbrA",
    };

    public final static String[] imageUrls_right = new String[]{
            "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcRsU6aV8k6ujehgHZUoWpREFCVVyAX-fRSi6w1bjSfQCZdNnRx3",
            "http://www.bettycrocker.com/~/media/Images/Bettys%20Dish/Headers/Recipe-Category-Pages/Meal-Type/DinnerRecipes_header.jpg",
            "https://www.captaincook.com.au/assets/images-syd-870x580/157/sg-food4_large.jpg",
            "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcQ0s0EHN1CMrXXN_3NblAzft5Fz13JRjVz1gb2-U5mmz-zzZuVquQ",
            "https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcSBl-sWQgdKcnlTAynFJbgRug0taSj6Gzq-BYLXKnsach81W1-DyA"
    };

    private List<String> mImagesLeft = new ArrayList<>();
    private List<String> mImagesRight = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        mImagesLeft.addAll(Arrays.asList(imageUrls_left));
        mImagesRight.addAll(Arrays.asList(imageUrls_right));

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ListBuddiesLayout listBuddies = (ListBuddiesLayout) rootView.findViewById(R.id.listbuddies);
        CircularAdapter adapter = new CircularAdapter(getActivity(), getResources().getDimensionPixelSize(R.dimen.item_height_small), mImagesLeft);
        CircularAdapter adapter2 = new CircularAdapter(getActivity(), getResources().getDimensionPixelSize(R.dimen.item_height_tall), mImagesRight);

        listBuddies.setAdapters(adapter, adapter2);
        listBuddies.setOnItemClickListener(this);
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBuddyItemClicked(AdapterView<?> adapterView, View view, int buddy, int position, long id) {
        String Url;
        if (buddy == 0) {
            Url = imageUrls_left[position];
        } else {
            Url = imageUrls_right[position];
        }

        ((MainActivity) getActivity()).startSwipeActivity(Url);
    }
}
