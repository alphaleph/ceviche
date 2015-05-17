package hu.ait.android.cevicheteam.ceviche.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.jpardogo.listbuddies.lib.views.ListBuddiesLayout;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hu.ait.android.cevicheteam.ceviche.Adapters.CircularAdapter;
import hu.ait.android.cevicheteam.ceviche.Adapters.DefaultImageCircularAdapter;
import hu.ait.android.cevicheteam.ceviche.R;

/**
 * Created by Chau on 5/17/2015.
 */
public class FavoritesFragment extends Fragment implements ListBuddiesLayout.OnBuddyItemClickListener {

    public static final String TAG = "Favorites_Fragment";

    // TODO: Get Personal Ceviche Pictures (SugarORM?)
    private List<String> personalPicUris = new ArrayList<>(Arrays.asList(
            "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcQpIpz3DAfFl5vIem3qP7Eutkc8_XfyiZs_ymF61nnw6_nQoqPA"
    ));

    private List<String> favoritePicUris;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorites, null);

        CircularAdapter adapter = new CircularAdapter(getActivity(), getResources().getDimensionPixelSize(R.dimen.item_height_small), personalPicUris);

        ListBuddiesLayout listBuddies = (ListBuddiesLayout) rootView.findViewById(R.id.listbuddies);
        CircularAdapter adapter2 = setFavoritesAdapter();

        listBuddies.setAdapters(adapter, adapter2);
        listBuddies.setOnItemClickListener(this);
        return rootView;
    }

    private CircularAdapter setFavoritesAdapter() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        favoritePicUris = currentUser.getList(getString(R.string.parse_favorite_photos));

        if ( favoritePicUris != null && (!favoritePicUris.isEmpty()) ) {
            return new CircularAdapter(getActivity(), getResources().getDimensionPixelSize(R.dimen.item_height_tall), favoritePicUris);
        } else {
            Toast.makeText(this.getActivity(), this.getActivity().getString(R.string.favorites_populate_fail), Toast.LENGTH_LONG).show();
            return new DefaultImageCircularAdapter(getActivity(), getResources().getDimensionPixelSize(R.dimen.item_height_tall));
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBuddyItemClicked(AdapterView<?> adapterView, View view, int buddy, int position, long id) {
        String Url;
        if (buddy == 0) {
            Url = personalPicUris.get(position);
        } else {
            Url = favoritePicUris.get(position);
        }

        //TODO: Delete Dialog Fragment
    }
}
