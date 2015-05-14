package hu.ait.android.cevicheteam.ceviche;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.dd.CircularProgressButton;

import java.util.Calendar;


public class SearchSettingsFragment extends DialogFragment {
    public static final String TAG = "Search_Settings_Fragment";

    private CircularProgressButton btnSave;
    private EditText etSearch;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(
                new ContextThemeWrapper(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog));

        LayoutInflater i = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = i.inflate(R.layout.fragment_search_settings, null);


        etSearch = (EditText) view.findViewById(R.id.search_preferences);
        SharedPreferences sp = getActivity().getSharedPreferences(MainActivity.PREF_NAME, MainActivity.MODE_PRIVATE);
        String s = sp.getString("searchSettings","");
        if (s != null && !s.equals("")) {
            etSearch.setText(s);
        }


        btnSave = (CircularProgressButton) view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int progress = 0; progress <= 100; progress++) {
                    btnSave.setProgress(progress);
                }

                if (!etSearch.getText().toString().equals("")) {
                    SharedPreferences sp = getActivity().getSharedPreferences(MainActivity.PREF_NAME, MainActivity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("searchSettings", etSearch.getText().toString());
                    editor.commit();
                }

            }
        });

        builder.setView(view);
        builder.setTitle("Search Settings");
        return builder.create();
    }


}
