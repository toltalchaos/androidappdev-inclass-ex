package com.example.helloworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    SharedPreferences prefs;
    Button buttonPost, buttonView;
    EditText editText;
    int defaultColor;
    View mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //register global prefs
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //register and set bg color
        mainView = findViewById(R.id.linear_layout_main);
        String bgColorKey = getResources().getString(R.string.preference_main_bg_key);
        String bgColor = prefs.getString(bgColorKey, "#cecece");
        mainView.setBackgroundColor(Color.parseColor(bgColor));
        prefs.registerOnSharedPreferenceChangeListener(this);
        //register button
        Button submitButton = findViewById(R.id.submit_button);
        //pipe to method
        submitButton.setOnClickListener(this);
        //register
        Button launchButton = findViewById(R.id.launch_button);
        launchButton.setOnClickListener(this);

        //allow internet access on main thread - permitall on main thread available (bad practice)
        if (android.os.Build.VERSION.SDK_INT > 9){
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    @Override
    public void onClick(View view) {
        //register edit text field
        EditText editText = findViewById(R.id.edit_text_message);
        //get data
        String data = editText.getText().toString();
        switch (view.getId()){
            case R.id.submit_button:

                //using http to post to mySQL server ->
                postToChatter(data);



                //Toast => popup
                Toast.makeText(this, "You Entered: " + data, Toast.LENGTH_LONG).show();
                break;
            case R.id.launch_button:
                //create new intent to start an activity - new instance of class
                Intent intent = new Intent(this, RecieveActivity.class);

                 Bundle bundle = new Bundle();
                 bundle.putString("PREFIX", "you typed: ");
                 bundle.putString("DATA", data);

                 //load intent with bundle extras
                 intent.putExtras(bundle);

                this.startActivity(intent);
                break;

        }



    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        String bgColor = prefs.getString(key, "#cecece");
        mainView.setBackgroundColor(Color.parseColor(bgColor));
    }
    private void postToChatter(String data) {
        //grabbing user and pass
        String userNameKey = getResources().getString(R.string.preferences_username_key);
        String userName = prefs.getString(userNameKey, "unknown/defaultvalue");
        try {
            HttpClient client = new DefaultHttpClient();
            //create new post request
            HttpPost post = new HttpPost("http://www.youcode.ca/JitterServlet");
            List<NameValuePair> paramters = new ArrayList<NameValuePair>();
            //load request with keyvalues
            paramters.add(new BasicNameValuePair("DATA", data));
            paramters.add(new BasicNameValuePair("LOGIN_NAME", userName));
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(paramters);
            post.setEntity(formEntity);
            //execute request
            client.execute(post);


        }
        catch (Exception exception){
            Toast.makeText(this, exception.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_view_chatter: {
                //register edit text field
                EditText editText = findViewById(R.id.edit_text_message);
                //get data
                String data = editText.getText().toString();

                Bundle bundle = new Bundle();
                bundle.putString("PREFIX", "");
                bundle.putString("DATA", data);

                Intent intent = new Intent(this, RecieveActivity.class);
                //load intent with bundle extras
                intent.putExtras(bundle);

                startActivity(intent);
                break;
            }
            case R.id.menu_item_chatter_listview: {
                Intent intent = new Intent(this, ChatterListViewActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.menu_item_custom_listview: {
                Intent intent = new Intent(this, CustomListViewActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.menu_item_color_spinner: {
                Intent intent = new Intent(this, ColorSpinnerActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.menu_item_object_spinner: {
                Intent intent = new Intent(this, ObjectSpinnerActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.menu_item_preference: {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            }
        }
        return true;
    }


}