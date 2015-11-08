package com.passwordgenerator.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.appbrain.AppBrain;

public class MainActivity extends AppCompatActivity {

    public ImageView getInfoImageView() {
        return infoImageView;
    }

    public void setInfoImageView(ImageView infoImageView) {
        this.infoImageView = infoImageView;
    }

    private ImageView infoImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        infoImageView = (ImageView) findViewById(R.id.infoImageView);
        infoImageView.setOnClickListener(new InfoClicker());

        AppBrain.init(this);

    }

    class InfoClicker implements View.OnClickListener{

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            InfoDialogFragment infoFragment = new InfoDialogFragment();
            infoFragment.show(getSupportFragmentManager(),"TAGGER");
        }
    }

}
