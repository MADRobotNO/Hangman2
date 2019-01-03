package no.madrobot.hangman;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class MainActivity extends Activity {
    Button start_button;
    Button lang_button;
    Button exit_button;
    Context context;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_help:
                showHelp(context);
                break;
            case R.id.action_exit:
                finishAndRemoveTask();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showHelp(Context context) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle(R.string.help);
        dialogBuilder.setMessage(R.string.help_text);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        context = this;

        Log.d("TestLog", "create");
        setContentView(R.layout.activity_main);

        start_button = findViewById(R.id.start_but_id);
        lang_button = findViewById(R.id.lang_but_id);
        exit_button = findViewById(R.id.exit_but_id);

        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Game.class);
                startActivity(intent);
            }
        });

        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAndRemoveTask();
            }
        });

        lang_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TestLog", "Resume");
        setLanguage();
    }

    private void setLanguage() {

        SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(this);
        Configuration config = getResources().getConfiguration();

        String getCurrConf = config.getLocales().get(0).toString();
        getCurrConf = getCurrConf.substring(0,2);

        Log.d("TestLog", spref.getString("LangList", "NaN"));
        Log.d("TestLog", getCurrConf);

        if (!(getCurrConf.equalsIgnoreCase(spref.getString("LangList", "NaN")))){

            Log.d("TestLog", "Change and recreate");

            switch (spref.getString("LangList", "NaN")){
                case "en":
                    langUpdate( "en");
                    Log.d("TestLog", "en");
                    recreate();
                    break;
                case "en_us":
                    langUpdate( "en");
                    Log.d("TestLog", "en_us");
                    recreate();
                    break;
                case "no":
                    langUpdate( "no");
                    Log.d("TestLog", "no");
                    recreate();
                    break;
                case "pl":
                    langUpdate( "pl");
                    Log.d("TestLog", "pl");
                    recreate();
                    break;
                case "NaN":
                    Log.d("TestLog", "NaN");

                    Intent intent = new Intent(MainActivity.this, Settings.class);
                    startActivity(intent);
                    break;
                default:
                    langUpdate( "en");
                    Log.d("TestLog", "def");
                    recreate();
            }

        }
        else{Log.d("TestLog", "do nothing, no change");
        }
    }


    private void langUpdate(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = getResources().getConfiguration();
        config.setLocale(locale);

        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }



}
