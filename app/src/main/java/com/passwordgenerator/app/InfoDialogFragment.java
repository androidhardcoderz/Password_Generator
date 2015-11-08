package com.passwordgenerator.app;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Scott on 11/6/2015.
 */
public class InfoDialogFragment extends DialogFragment {

    @Bind(R.id.shopwTipsCheckBox) CheckBox showTips;
    @Bind(R.id.enableShakeCheckBox)
    CheckBox enableShake;

    private final String TAG = "InfoDIalogFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //custom style(appcompact)
       setStyle(DialogFragment.STYLE_NO_TITLE, android.support.v7.appcompat.R.style.Theme_AppCompat_Light_Dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //inflate view for this fragment
        View rootView = inflater.inflate(R.layout.info_dialog, container,
                false);

        ButterKnife.bind(this,rootView); //butterknife view interjection lib

        //set default/saved states
        showTips.setChecked(UserPreferences.getShowTips(getActivity()));
        enableShake.setChecked(UserPreferences.getShake(getActivity()));

        showTips.setOnCheckedChangeListener(new ChangePreferences());
        enableShake.setOnCheckedChangeListener(new ChangePreferences());

        getDialog().setCancelable(true); //allow cancelable by user

        return rootView;
    }

    class ChangePreferences implements CompoundButton.OnCheckedChangeListener{

        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param buttonView The compound button view whose state has changed.
         * @param isChecked  The new checked state of buttonView.
         */
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(buttonView.getId() == showTips.getId()){
                //show tips was changed!
                UserPreferences.setShowTips(getActivity(),buttonView.isChecked());
                Log.i(TAG,buttonView.getId() + " WAS CHANGED TO " + buttonView.isChecked());
            }else if(buttonView.getId() == enableShake.getId()){
                //shake was changed!
                UserPreferences.setShake(getActivity(), buttonView.isChecked());
                Log.i(TAG, buttonView.getId() + " WAS CHANGED TO " + buttonView.isChecked());
            }
        }
    }
}
