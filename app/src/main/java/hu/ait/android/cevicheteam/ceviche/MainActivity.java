package hu.ait.android.cevicheteam.ceviche;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;

import org.apache.http.Header;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import hu.ait.android.cevicheteam.ceviche.Fragments.MainFragment;
import hu.ait.android.cevicheteam.ceviche.Fragments.SearchSettingsFragment;

public class MainActivity extends AppCompatActivity implements OnMenuItemClickListener {

    private android.support.v4.app.FragmentManager fragmentManager;
    private DialogFragment mMenuDialogFragment;
    private MainFragment mainFragment;

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    public static final String PREF_NAME = "MySettings";

    private String CX_KEY = "006595710349057423305:js3hz-kiofe";
    private String API_KEY = "AIzaSyBrlBeP70dgFnvl2zddqtRfEkmFzm6WfJY";

    String mCurrentPhotoPath;

    private static int MENU_QUIT = 0;
    private static int MENU_PROFILE = 1;
    private static int MENU_PICTURE = 2;
    private static int MENU_SEARCH_SETTINGS = 3;
    private static int MENU_FAVORITES = 4;

    String imgJson;

    public Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        showFragment(MainFragment.TAG);

        initMenu();
    }

    private void showFragment(String fragmentTag) {
        if (MainFragment.TAG.equals(fragmentTag)) {
            final android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mainFragment = new MainFragment();
            setSearchSettingsURLs();
            fragmentTransaction.replace(R.id.layoutContainer, mainFragment, MainFragment.TAG);

            Handler handler = new Handler(Looper.getMainLooper());
            Runnable setURLS = new Runnable() {
                @Override
                public void run() {
                    fragmentTransaction.commit();
                }
            };
            handler.postDelayed(setURLS, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.drop_down_menu) {
            mMenuDialogFragment.show(fragmentManager, "ContextMenuDialogFragment");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initMenu() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(true);
        menuParams.setAnimationDelay(100);
        // set other settings to meet your needs
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
    }

    private List<MenuObject> getMenuObjects() {
        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject();
        close.setResource(R.drawable.ic_close_black_24dp);

        MenuObject camera = new MenuObject("Take Picture");
        camera.setResource(R.drawable.ic_camera_alt_black_24dp);

        MenuObject profile = new MenuObject("Profile");
        profile.setResource(R.drawable.ic_person_black_24dp);

        MenuObject searchSettings = new MenuObject("Search Settings");
        searchSettings.setResource(R.drawable.ic_edit_black_24dp);

        MenuObject favorites = new MenuObject("Favorites");
        favorites.setResource(R.drawable.ic_grade_black_24dp);

        menuObjects.add(close);
        menuObjects.add(profile);
        menuObjects.add(camera);
        menuObjects.add(searchSettings);
        menuObjects.add(favorites);

        return menuObjects;
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {
        if (position == MENU_PROFILE) {
            startActivity(new Intent(this, EditProfileActivity.class));
        }
        if (position == MENU_PICTURE) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                if (photoFile != null) {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile));
                    startActivityForResult(takePictureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                    galleryAddPic();
                }
            }
        }
        if (position == MENU_SEARCH_SETTINGS) {
            SearchSettingsFragment settings = new SearchSettingsFragment();
            settings.show(getSupportFragmentManager(), SearchSettingsFragment.TAG);
        }
        if (position == MENU_FAVORITES) {
            startFavoritesActivity();
        }
    }

    private void startFavoritesActivity() {
        if (ParseUser.getCurrentUser() != null) {
            startActivity(new Intent(this, FavoritesActivity.class));
        } else {
            Toast.makeText(this, getString(R.string.favorites_open_fail), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (mMenuDialogFragment != null && mMenuDialogFragment.isAdded()) {
            mMenuDialogFragment.dismiss();
        } else{
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_CANCELED:
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                break;
            case RESULT_OK:
                if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                    // TODO: Add Pic reference to SugarORM?
                    //storeUserData(data);
                    //Bitmap photo = (Bitmap) data.getExtras().get("data");
                    Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void startSwipeActivity(String url) {
        Intent i = new Intent(MainActivity.this, SwipeActivity.class);
        String meta = getMetaData(url);

        int min = 0;
        int max = MainFragment.extras.length;
        int rand1 = randInt(min, max);
        int rand2 = randInt(min, max);

        String url1 = MainFragment.extras[rand1];
        String url2 = MainFragment.extras[rand2];

        i.putExtra("URL", url);
        i.putExtra("URL1", url1);
        i.putExtra("URL2", url2);

        i.putExtra("META", meta);
        Log.d("META", meta);
        startActivity(i);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // No call for super(). Bug on API Level > 11.
        // Necessary to keep app from crashing when launching camera
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    public void setSearchSettingsURLs() {
        String settings = getSearchSettings();
        String imgQuery = "https://www.googleapis.com/customsearch/v1?q=" +
                settings + "&cx=" + CX_KEY + "&searchType=image&num=10&key=" + API_KEY;
        new AsyncHttpClient().get(imgQuery, new AsyncHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                imgJson = new String(response);

                mainFragment.imageUrls_left = new String[imgJson.split("link\": \"").length / 2];
                mainFragment.imageUrls_right = new String[imgJson.split("link\": \"").length / 2];


                for (int i = 0; i < imgJson.split("link\": \"").length / 2; i++) {
                    mainFragment.imageUrls_left[i] = imgJson.split("link\": \"")[i + 1].split("\"")[0];
                }
                int place = 0;
                for (int i = mainFragment.imageUrls_left.length; i < imgJson.split("link\": \"").length - 1; i++) {
                    mainFragment.imageUrls_right[place] = imgJson.split("link\": \"")[i + 1].split("\"")[0];
                    place = place + 1;
                }
            }

            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.d("Result", "Failure");
            }
        });

    }

    private String getSearchSettings() {
        SharedPreferences sp = getSharedPreferences(MainActivity.PREF_NAME, MainActivity.MODE_PRIVATE);
        return sp.getString("searchSettings", "");
    }

    public String getMetaData(String url) {
        for (int i = 0; i < imgJson.split("link\": \"").length; i++) {
            if (imgJson.split("link\": \"")[i].split("\"")[0].equals(url)) {
                Log.d("Meta",imgJson.split("snippet\": \"")[i].split("\"")[0]);
                return imgJson.split("snippet\": \"")[i].split("\"")[0];
            }
        }
        return "FOOD";
    }

    public int randInt(int min, int max) {

        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

}
