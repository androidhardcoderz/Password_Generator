package com.passwordgenerator.app;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private Button lengthButton,generate;
    private TextView prevention,generatedPassword;
    private CheckBox symbols;
    private CheckBox numbers;
    private CheckBox lowercase;
    private CheckBox uppercase;
    private CheckBox similiar;
    private CheckBox ambiguous;
    private CheckBox copy;

    RelativeLayout shakeLayout;

    boolean loaded;

    public CheckBox getSave() {
        return save;
    }

    public void setSave(CheckBox save) {
        this.save = save;
    }

    public CheckBox getCopy() {
        return copy;
    }

    public void setCopy(CheckBox copy) {
        this.copy = copy;
    }

    private CheckBox save;
    private ArrayList<CheckBox> options = new ArrayList<>();
    private OptionsList optionsList;
    View coordinatorLayoutView;

    public MainActivityFragment() {

    }


    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity
    private boolean passwordIsGenerated;
    Fragment main,info;

    private final SensorEventListener mSensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter

            if(passwordIsGenerated && UserPreferences.getShake(getActivity()) == true) {
                if (mAccel > 12) {
                    MediaPlayer mp = MediaPlayer.create(getActivity(),R.raw.dice_shake);
                    mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {

                                    getGeneratedPassword().setText(new Password().shuffleString(getGeneratedPassword().getText().toString()));
                                    mp.release();

                                    if(getCopy().isChecked())
                                        copyToClipboard(getGeneratedPassword());
                                }
                            });
                        }
                    });

                }
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };


    @Override
    public void onResume() {
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }

    /**
     * Called when the Fragment is no longer resumed.  This is generally
     * tied to {@linkActivity#onPause() Activity.onPause} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        optionsList = new OptionsList();

        loaded = false;

         /* do this in onCreate */
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loaded = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);

        coordinatorLayoutView = view.findViewById(R.id.view);

        shakeLayout = (RelativeLayout) view.findViewById(R.id.shakeLayout);
        shakeLayout.setVisibility(View.INVISIBLE);

        setLengthButton((Button) view.findViewById(R.id.lengthButton));
        getLengthButton().setOnClickListener(new LengthClicker());

        setGenerate((Button) view.findViewById(R.id.generateButton));
        getGenerate().setOnClickListener(new GeneratePasswordClicker());

        setGeneratedPassword((TextView) view.findViewById(R.id.generatedPasswordTextView));


        setPrevention((TextView) view.findViewById(R.id.preventionTextView));
        getPrevention().setText(BulletTextUtil.makeBulletListFromStringArrayResource(2, getActivity(), R.array.prevention));

        setSymbols((CheckBox) view.findViewById(R.id.symbolsCB));
        setNumbers((CheckBox) view.findViewById(R.id.numbersCB));
        setLowercase((CheckBox) view.findViewById(R.id.lowercaseCB));
        setUppercase((CheckBox) view.findViewById(R.id.uppercaseCB));
        setSimiliar((CheckBox) view.findViewById(R.id.similarCB));
        setAmbiguous((CheckBox) view.findViewById(R.id.ambiguousCB));

        setCopy((CheckBox) view.findViewById(R.id.copyCB));
        setSave((CheckBox) view.findViewById(R.id.saveCB));

        addCheckBoxes();

        //set listeners for each checkbox!
        for(CheckBox cb: options){
            cb.setOnCheckedChangeListener(new CheckChanged());
        }

        getSimiliar().setOnCheckedChangeListener(new CheckChanged());
        getAmbiguous().setOnCheckedChangeListener(new CheckChanged());
        getCopy().setOnCheckedChangeListener(new CheckChanged());
        getSave().setOnCheckedChangeListener(new CheckChanged());

        loadFromSettings();
        return view;
    }

    class CheckChanged implements CompoundButton.OnCheckedChangeListener{

        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param buttonView The compound button view whose state has changed.
         * @param isChecked  The new checked state of buttonView.
         */
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            //check if preferences allow show tips are on!
            if(UserPreferences.getShowTips(getActivity()) == true && loaded == true){
                //show tips is true!
                if(buttonView.getId() == getSymbols().getId() && isChecked){
                    //symbols was clicked!
                    showSnackBar("Uses symbols such as ! @ # $ % ^ & * ( ) in your password");
                }else if(buttonView.getId() == getNumbers().getId()  && isChecked){
                    showSnackBar("Uses numeric digits 0 - 9 in your password");
                }else if(buttonView.getId() == getLowercase().getId()  && isChecked){
                    showSnackBar("Uses lowercase letters in your password");
                }else if(buttonView.getId() == getUppercase().getId()  && isChecked){
                    showSnackBar("Uses uppercase letters in your password");
                }else if(buttonView.getId() == getSimiliar().getId()  && isChecked){
                    showSnackBar("Excludes similar letters and numbers from your password");
                }else if(buttonView.getId() == getAmbiguous().getId()  && isChecked){
                    showSnackBar("Excludes ambiguous characters such as { } [ ] ( ) / \\ \" from your password");
                }else if(buttonView.getId() == getCopy().getId()  && isChecked){
                    showSnackBar("Copies your password to your clipboard for easy pasting");
                }else if(buttonView.getId() == getSave().getId()  && isChecked){
                    showSnackBar("Saves current preferences to load the next time you launch application");
                }else{

                }
            }
        }
    }

    private void loadFromSettings() {

        getLengthButton().setText(UserPreferences.getLengthItem(getActivity()));
        getSymbols().setChecked(UserPreferences.getItem(getActivity(), UserPreferences.SYMBOLS));
        getNumbers().setChecked(UserPreferences.getItem(getActivity(),UserPreferences.NUMBERS));
        getLowercase().setChecked(UserPreferences.getItem(getActivity(),UserPreferences.LOWERCASE));
        getUppercase().setChecked(UserPreferences.getItem(getActivity(),UserPreferences.UPPERCASE));

        getSimiliar().setChecked(UserPreferences.getItem(getActivity(), UserPreferences.SIMILAR));
        getAmbiguous().setChecked(UserPreferences.getItem(getActivity(), UserPreferences.AMBIGUOUS));
        getCopy().setChecked(UserPreferences.getItem(getActivity(), UserPreferences.COPY));
        getSave().setChecked(UserPreferences.getItem(getActivity(), UserPreferences.SAVE));
    }

    private void showSnackBar(String message){
        Snackbar
                .make(coordinatorLayoutView, message, Snackbar.LENGTH_LONG)
                .show();
    }

    private void addCheckBoxes() {

        options.add(getSymbols());
        options.add(getNumbers());
        options.add(getLowercase());
        options.add(getUppercase());

    }

    public Button getGenerate() {
        return generate;
    }

    public CheckBox getAmbiguous() {
        return ambiguous;
    }

    public void setAmbiguous(CheckBox ambiguous) {
        this.ambiguous = ambiguous;
    }

    public CheckBox getLowercase() {
        return lowercase;
    }

    public void setLowercase(CheckBox lowercase) {
        this.lowercase = lowercase;
    }

    public CheckBox getNumbers() {
        return numbers;
    }

    public void setNumbers(CheckBox numbers) {
        this.numbers = numbers;
    }

    public CheckBox getSimiliar() {
        return similiar;
    }

    public void setSimiliar(CheckBox similiar) {
        this.similiar = similiar;
    }

    public CheckBox getSymbols() {
        return symbols;
    }

    public void setSymbols(CheckBox symbols) {
        this.symbols = symbols;
    }

    public CheckBox getUppercase() {
        return uppercase;
    }

    public void setUppercase(CheckBox uppercase) {
        this.uppercase = uppercase;
    }

    public void setGenerate(Button generate) {
        this.generate = generate;
    }

    public TextView getGeneratedPassword() {
        return generatedPassword;
    }

    public void setGeneratedPassword(TextView generatedPassword) {
        this.generatedPassword = generatedPassword;
    }

    class GeneratePasswordClicker implements View.OnClickListener{

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            //find out how many options are checked
            int checked = 0;
            for(CheckBox cb: options){
                if(cb.isChecked())
                    checked += 1;
            }

            if(checked == 0){
                //error no options chosen
                showSnackBar("Select At Least One Password Option (e.g. Use Lowercase Letter)");
            }else{
                //do password processing

                Password password = new Password(Integer.parseInt(getLengthButton().getText().toString()),getPasswordStates());
                getGeneratedPassword().setText(password.getPassword());

                passwordIsGenerated = true;


                //check if user checked to copy to clipboard
                if(getCopy().isChecked()){
                    copyToClipboard(getGeneratedPassword());
                }

                if(getSave().isChecked()){
                    //save user preferences for all checked states
                    setSavedStates();
                }

                if(UserPreferences.getShowTips(getActivity()) == true){

                    Animation shakeAnim = AnimationUtils.loadAnimation(getActivity(),R.anim.shake);
                    shakeLayout.setAnimation(shakeAnim);
                    shakeLayout.setVisibility(View.VISIBLE);
                    shakeAnim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            shakeLayout.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    shakeAnim.start();
                }

                View view = getView().findViewById(R.id.adBanner);
                view.getParent().requestChildFocus(view,view);

            }
        }
    }

    private List<States> getPasswordStates(){

        List<States> states = new ArrayList<>();

        if(getSymbols().isChecked()) {
            States state = new States();
            state.setName(UserPreferences.SYMBOLS);
            state.setState(true);
            states.add(state);

        }
        if(getNumbers().isChecked()) {
            States state = new States();
            state.setName(UserPreferences.NUMBERS);
            state.setState(true);
            states.add(state);

        }
        if(getLowercase().isChecked()) {
            States state = new States();
            state.setName(UserPreferences.LOWERCASE);
            state.setState(true);
            states.add(state);


        }
        if(getUppercase().isChecked()) {
            States state = new States();
            state.setName(UserPreferences.UPPERCASE);
            state.setState(true);
            states.add(state);

        }
        if(getSimiliar().isChecked()) {
            States state = new States();
            state.setName(UserPreferences.SIMILAR);
            state.setState(true);
            states.add(state);

        }

        if(getAmbiguous().isChecked()) {
            States state = new States();
            state.setName(UserPreferences.AMBIGUOUS);
            state.setState(true);
            states.add(state);
        }

        return states;

    }



    public void setSavedStates(){

        UserPreferences.setLengthItem(getActivity(),getLengthButton().getText().toString());
        UserPreferences.setItem(getActivity(),UserPreferences.SYMBOLS,getSymbols().isChecked());
        UserPreferences.setItem(getActivity(),UserPreferences.NUMBERS,getNumbers().isChecked());
        UserPreferences.setItem(getActivity(), UserPreferences.UPPERCASE, getUppercase().isChecked());
        UserPreferences.setItem(getActivity(),UserPreferences.LOWERCASE,getLowercase().isChecked());
        UserPreferences.setItem(getActivity(),UserPreferences.SIMILAR,getSimiliar().isChecked());
        UserPreferences.setItem(getActivity(), UserPreferences.AMBIGUOUS, getAmbiguous().isChecked());
        UserPreferences.setItem(getActivity(),UserPreferences.COPY,getCopy().isChecked());

    }

    public char getRandomLetter(){

        OptionsList optionsList = new OptionsList();
        return optionsList.getAlpha()[new Random().nextInt(optionsList.getAlpha().length)];
    }

    class LengthClicker implements View.OnClickListener {


        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {

            //create single side activity dialog that displays a list of integers to pick from
            final Dialog dialog = new Dialog(getActivity(),android.support.v7.appcompat.R.style.Theme_AppCompat_Light_Dialog);
            dialog.setTitle("Select A Password Length");
            dialog.setContentView(R.layout.dialog_fragment);
            dialog.setCancelable(true);

            ListView listView = (ListView) dialog.findViewById(R.id.lengthListView);
            listView.setAdapter(new ArrayAdapter<>(getActivity()
                    ,android.R.layout.simple_list_item_1,
                    android.R.id.text1,new LengthArray().getLengths()));

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    getLengthButton().setText(new LengthArray().getLengths().get(position).toString());
                    dialog.dismiss(); //dismiss and cancel the current dialog
                }
            });
            dialog.show();
        }
    }

    private void copyToClipboard(TextView tv){

        String stringYouExtracted = tv.getText().toString();
        System.out.println(stringYouExtracted);
        tv.setTextIsSelectable(true);
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);

        clipboard.setPrimaryClip(ClipData.newPlainText(getActivity().getString(R.string.app_name),stringYouExtracted));

        Log.i("TAG", stringYouExtracted);

        showSnackBar("Password Copied To Clipboard");

        //getActivity().startService(new Intent(Intent.ACTION_SYNC, null, getActivity(), DownloadService.class));
    }

    public Button getLengthButton() {
        return lengthButton;
    }

    public void setLengthButton(Button lengthButton) {
        this.lengthButton = lengthButton;
    }

    public TextView getPrevention() {
        return prevention;
    }

    public void setPrevention(TextView prevention) {
        this.prevention = prevention;
    }
}
