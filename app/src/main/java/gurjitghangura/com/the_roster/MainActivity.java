//Gurjit Singh Ghangura
package gurjitghangura.com.the_roster;

import android.app.DatePickerDialog;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.SharedPreferences;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    //Edit Texts To link to code
    EditText dateOfBirthText;
    EditText nameText;

    //CheckBox to link to code
    CheckBox steadyCheckbx;

    //Variable to save radiobutton selection in preferences
    String radioChecked = "none";

    //preferences to store data
    SharedPreferences prefs;
    SharedPreferences.Editor prefsEditor;

    //date picker
    DatePickerDialog.OnDateSetListener date;
    Calendar myCalendar;

    //Config for spinner
    Spinner spinner;
    private String[] arraySpinner;
    ArrayAdapter<String> spinnerAdapter;

    //seekbars to link to code
    SeekBar pantSeekBar;
    SeekBar shirtSeekBar;
    SeekBar shoeSeekBar;

    //textview to link to code
    TextView pantTextView;
    TextView shirtTextView;
    TextView shoeTextView;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //custom top bar
        final ActionBar abar = getSupportActionBar();
        View viewActionBar = getLayoutInflater().inflate(R.layout.customlayout, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        textviewTitle.setText("The Roster");
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);

        //init and links
        prefs = getSharedPreferences("TheRooster", MODE_PRIVATE);

        steadyCheckbx = (CheckBox) findViewById(R.id.checkBox);

        dateOfBirthText = (EditText) findViewById(R.id.editText2);
        nameText = (EditText) findViewById(R.id.editText5);

        spinner = (Spinner) findViewById(R.id.spinner);
        arraySpinner = new String[] {
                "Black", "Brown", "Blue", "Green", "Gray", "Hazel", "Red"
        };
        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        spinner.setAdapter(spinnerAdapter);

        pantSeekBar = (SeekBar) findViewById(R.id.seekBar);
        shirtSeekBar = (SeekBar) findViewById(R.id.shirtSize);
        shoeSeekBar = (SeekBar) findViewById(R.id.shoeSize);

        pantTextView = (TextView) findViewById(R.id.pantSizeValue);
        shirtTextView = (TextView) findViewById(R.id.shirtSizeValue);
        shoeTextView = (TextView) findViewById(R.id.shoeSizeValue);

        pantSeekBar.setOnSeekBarChangeListener(this);
        shirtSeekBar.setOnSeekBarChangeListener(this);
        shoeSeekBar.setOnSeekBarChangeListener(this);

        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };


        dateOfBirthText.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                );
                new DatePickerDialog(MainActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        if (prefs.getBoolean("isDataSaved",false)){
            getData();
        }

        setDefaultValues();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateOfBirthText.setText("Born on: "+sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == pantSeekBar){
            pantTextView.setText(String.valueOf(progress));
        } else if (seekBar == shirtSeekBar) {
            shirtTextView.setText(String.valueOf(progress));
        } else {
            progress += 4;
            shoeTextView.setText(String.valueOf(progress));

        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        radioChecked = ((RadioButton) view).getText().toString();

    }

    void setDefaultValues() {
        pantTextView.setText(String.valueOf(pantSeekBar.getProgress()));
        shirtTextView.setText(String.valueOf(shirtSeekBar.getProgress()));
        shoeTextView.setText(String.valueOf(shoeSeekBar.getProgress()+4));
    }

    void savedata(View v) {
        prefsEditor = prefs.edit();

        prefsEditor.putBoolean("isDataSaved", true);

        prefsEditor.putString("GirlName", nameText.getText().toString());
        prefsEditor.putString("EyeColor",spinner.getSelectedItem().toString());
        prefsEditor.putBoolean("ThingsSteady",steadyCheckbx.isChecked());
        prefsEditor.putString("DOB",dateOfBirthText.getText().toString());
        prefsEditor.putString("ShirtSizeRadio",radioChecked);
        prefsEditor.putString("PantSize",pantTextView.getText().toString());
        prefsEditor.putString("ShirtSize",shirtTextView.getText().toString());
        prefsEditor.putString("ShoeSize",shoeTextView.getText().toString());

        prefsEditor.commit();

    }
    void getData() {
        nameText.setText(prefs.getString("GirlName",null));
        spinner.setSelection(spinnerAdapter.getPosition(prefs.getString("EyeColor",null)));
        steadyCheckbx.setChecked(prefs.getBoolean("ThingsSteady",false));
        dateOfBirthText.setText(prefs.getString("DOB",null));

        String radioChecked = prefs.getString("ShirtSizeRadio",null);
        RadioButton radio;
        switch (radioChecked) {
            case "XS":
                radio = (RadioButton) findViewById(R.id.extraSmall);
                radio.setChecked(true);
                break;
            case "S":
                radio = (RadioButton) findViewById(R.id.small);
                radio.setChecked(true);
                break;
            case "M":
                radio = (RadioButton) findViewById(R.id.medium);
                radio.setChecked(true);
                break;
            case "L":
                radio = (RadioButton) findViewById(R.id.large);
                radio.setChecked(true);
                break;
            case "XL":
                radio = (RadioButton) findViewById(R.id.extraLarge);
                radio.setChecked(true);
                break;
            case "XXL":
                radio = (RadioButton) findViewById(R.id.doubleExtraLarge);
                radio.setChecked(true);
                break;
        }

        pantSeekBar.setProgress(Integer.parseInt(prefs.getString("PantSize",null)));
        shirtSeekBar.setProgress(Integer.parseInt(prefs.getString("ShirtSize",null)));
        shoeSeekBar.setProgress(Integer.parseInt(prefs.getString("ShoeSize",null)));
        setDefaultValues();
    }

}
