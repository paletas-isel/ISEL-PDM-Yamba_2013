package pt.isel.pdm.yamba.views;

import pt.isel.pdm.yambapdm.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Timeline extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timeline, menu);
        return true;
    }
    
}
