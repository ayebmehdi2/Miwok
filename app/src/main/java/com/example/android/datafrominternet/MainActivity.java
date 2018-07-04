package com.example.android.datafrominternet;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import com.example.android.datafrominternet.utilities.NetworkUtils;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private EditText ed;

    private TextView urlt;

    private TextView jsont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ed = findViewById(R.id.et_search_box);

        urlt = findViewById(R.id.tv_url_display);
        jsont = findViewById(R.id.tv_github_search_results_json);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_search) {
            urlt.setText(String.valueOf(NetworkUtils.buildUrl(ed.getText().toString())));
            getLoaderManager().initLoader(0, null, this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<String> onCreateLoader(int i, Bundle bundle) {
        return new NetworkUtils.AsyncGit(MainActivity.this, ed.getText().toString());
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String s) {
        if (s != null || !s.equals("")){
            jsont.setText(s);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}