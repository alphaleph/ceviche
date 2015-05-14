package hu.ait.android.cevicheteam.ceviche;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
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
import java.util.Date;
import java.util.List;

import hu.ait.android.cevicheteam.ceviche.Fragments.MainFragment;
import hu.ait.android.cevicheteam.ceviche.Fragments.SearchSettingsFragment;


public class MainActivity extends AppCompatActivity implements OnMenuItemClickListener {

    private android.support.v4.app.FragmentManager fragmentManager;
    private DialogFragment mMenuDialogFragment;

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    public static final String PREF_NAME = "MySettings";

    String mCurrentPhotoPath;

    private static int MENU_QUIT = 0;
    private static int MENU_PROFILE = 1;
    private static int MENU_PICTURE = 2;
    private static int MENU_SEARCH_SETTINGS = 3;
    private static int MENU_FAVORITES = 4;

    private String CX_KEY = "006595710349057423305:js3hz-kiofe";
    private String API_KEY = "AIzaSyBrlBeP70dgFnvl2zddqtRfEkmFzm6WfJY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        showFragment(MainFragment.TAG);

        //setSearchSettingsURLs();
        initMenu();
    }

    private void showFragment(String fragmentTag) {
        if (MainFragment.TAG.equals(fragmentTag)) {
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            MainFragment mainFragment = new MainFragment();
            fragmentTransaction.replace(R.id.layoutContainer, mainFragment, MainFragment.TAG);
            fragmentTransaction.commit();
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
        List<MenuObject> menuObjects = new ArrayList<MenuObject>();

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
        // Todo
        if (position == MENU_PROFILE) {
            // Edit Profile Activity
            startActivity(new Intent(this, EditProfileActivity.class));
        }
        if (position == MENU_PICTURE) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
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
            // Favorites Activity
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
                    //storeUserData(data);
                    //Bitmap photo = (Bitmap) data.getExtras().get("data");
                    Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                }
        }
    }

    public void startSwipeActivity(String url) {
        Intent i = new Intent(MainActivity.this, SwipeActivity.class);
        i.putExtra("URL", url);
        Log.d("url", url);
        startActivity(i);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
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
                settings + "&cx=" + CX_KEY + "&searchType=image&key=" + API_KEY;
        new AsyncHttpClient().get(imgQuery, new AsyncHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String imgJson = new String(response);
                Log.d("JSON", imgJson);
                String firstImage = imgJson.split("link\": \"")[1].split("\"")[0];
                //Todo update the image urls array, move this function?
                //MainFragment.imageUrls_left.add(firstImage);
                Log.d("First Image", firstImage);

                //Picasso.with(getApplicationContext()).load(firstImage).into((ImageView) findViewById(R.id.imageView));
            }
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {}
        });

    }

    private String getSearchSettings() {
        SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return sp.getString("searchSettings", "");
    }

}
