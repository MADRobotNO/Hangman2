package no.madrobot.hangman;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import java.util.Random;

/**
 * Created by marti on 14.11.2018.
 */
public class Game extends MainActivity {

    private Context context;

    private ImageView hangman_pic;
    private Button check_button;
    private EditText enter_letter;
    private TextView word, wins, fails, words, words_used, letters_used;

    private String input_letter = "";
    private String word_to_guess = "";
    private ArrayList<Integer> picked_words;

    private String[] words_array;
    private String shown_string = "";

    private int fail_word_counter = 0;
    private int correct_word_counter = 0;
    private int words_used_counter = 0;
    private int fail_letter_counter = 0;

    ArrayList<String> letters_used_array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        context = this;
        setContentView(R.layout.activity_game);

        hangman_pic = findViewById(R.id.hang_pic_id);
        check_button = findViewById(R.id.check_but_id);
        enter_letter = findViewById(R.id.enter_letter_id);
        word = findViewById(R.id.word_id);
        wins = findViewById(R.id.wins_counter_id);
        fails = findViewById(R.id.fails_counter_id);
        words = findViewById(R.id.words_counter_id);
        words_used = findViewById(R.id.words_used_count_id);
        letters_used = findViewById(R.id.letters_used_list_id);

        createLettersUsedArray();


        words_array = getResources().getStringArray(R.array.words_arr);
        picked_words = new ArrayList<>();

        words.setText(String.valueOf(words_array.length));
        words_used.setText(String.valueOf(picked_words.size()));

        newWord();

        check_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (enter_letter.getText().toString().isEmpty()){
                    Toast.makeText(context, R.string.choose_a_letter, Toast.LENGTH_SHORT).show();
                }

                else{
                    input_letter = enter_letter.getText().toString();
                    checkAndUpdateLetter(input_letter);
                    checkForWin();
                }

            }
        });

    }

    private boolean checkForEnd() {
        Log.d("TestLog", "words_used_counter: " + words_used_counter );
        if (words_used_counter == 10){
            Log.d("TestLog", "That was Your last round. Game over.");
            return true;
        }
        return false;
    }

    private void createLettersUsedArray() {
        letters_used_array = new ArrayList<>();
        letters_used.setText("");
    }

    private void checkForWin() {

            if (fail_letter_counter == 11) {
                lostGame();
                if(checkForEnd()){
                    gameEnds();
                }
                else{
                    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                    dialogBuilder.setMessage("You lost round!");
                    dialogBuilder.setCancelable(false);
                    dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            resetGame();
                        }
                    });
                    AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();
                }
            }

            else if (!shown_string.contains("_")) {

                Log.d("TestLog", "You won round!");
                Toast.makeText(context, R.string.good, Toast.LENGTH_LONG).show();
                winGame();

                if(checkForEnd()){
                    gameEnds();
                }
                else{
                resetGame();
                }
            }

    }

    private void lostGame() {
        fail_word_counter ++;
        fails.setText(String.valueOf(fail_word_counter));

        words_used_counter++;
        words_used.setText(String.valueOf(words_used_counter));

        updateImage(fail_letter_counter);
        updateLettersUsed(input_letter);
    }

    private void gameEnds() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

        dialogBuilder.setMessage(getString(R.string.game_over) + " " + getString(R.string.score)
                + "\n" + getString(R.string.wins) + " " + String.valueOf(correct_word_counter)
                + ", " + getString(R.string.fails) + " " + String.valueOf(fail_word_counter));

        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void resetGame() {
        newWord();
        fail_letter_counter = 0;
        createLettersUsedArray();
        updateImage(fail_letter_counter);
    }

    private void winGame() {
        words_used_counter ++;
        correct_word_counter ++;
        wins.setText(String.valueOf(correct_word_counter));
        words_used.setText(String.valueOf(words_used_counter));
    }



    private void createShowString() {
        shown_string = "";
        for (int i = 0; i<word_to_guess.length(); i++){
            shown_string += "_";
        }
        word.setText(shown_string);
    }

    private void newWord() {

        word_to_guess = pickAWord();

        Log.d("TestLog", "New word: " + word_to_guess);
        Log.d("TestLog", "Used words id's: " + picked_words.toString());

        createShowString();

    }

    private String pickAWord() {

        String value = "";

        Random random = new Random();
        int pick_word_id = random.nextInt(words_array.length);

        while (picked_words.contains(pick_word_id)){
            pick_word_id = random.nextInt(words_array.length);
            Log.d("TestLog", "reshuffle");
        }

        Log.d("TestLog", "Word id used: " + pick_word_id);
        value = words_array[pick_word_id];
        picked_words.add(pick_word_id);

        return value;

    }

    private void checkAndUpdateLetter(String input_letter) {

        //letter already used
        if (letters_used_array.contains(input_letter) || shown_string.contains(input_letter)){
            Toast.makeText(context, R.string.letter_already_used, Toast.LENGTH_SHORT).show();
        }

        //letter not used
        else{

            String[] temp_split_string = shown_string.split(""); //split current shown_string

            String[] temp_string = new String[temp_split_string.length-1]; //refactor temp_split_string to temp_string to delete first element
            for (int i = 0; i<temp_split_string.length-1; i++){
                temp_string[i]=temp_split_string[i+1];
            }

            //if word contains input letter...
            if (word_to_guess.contains(input_letter)){

                //check position and create new shown_string
                for (int i = word_to_guess.indexOf(input_letter); i>= 0; i = word_to_guess.indexOf(input_letter, i+1)){
                    Log.d("TestLog", "Letter pos: " + String.valueOf(i));
                    temp_string[i] = input_letter;
                }

                shown_string = ""; //clear shown_string

                //create new show_string
                for (int i = 0; i<temp_string.length; i++){
                    shown_string += temp_string[i];
                }

                Log.d("TestLog", "TempString: " + shown_string);

                updateWordView(shown_string);  //update shown_string_view

            }

            //...if not
            else{
                fail_letter_counter += 1;
                if (fail_letter_counter<11) {
                    updateImage(fail_letter_counter);
                    updateLettersUsed(input_letter);
                }
            }
        }

        enter_letter.setText("");  //clear enter_letter field for user
    }

    private void updateLettersUsed(String letter) {
        letters_used_array.add(letter);

        if (letters_used_array.size() == 1){
            letters_used.setText(letters_used_array.get(0));

        }
        else {
            String temp = letters_used.getText().toString();
            temp += ", " + letter;
            letters_used.setText(temp);

        }

        Log.d("TestLog", letters_used_array.toString());
    }

    private void updateImage(int number) {

        switch (number){
            case 0:
                hangman_pic.setImageResource(R.drawable.hangman0);
                break;
            case 1:
                hangman_pic.setImageResource(R.drawable.hangman1);
                break;

            case 2:
                hangman_pic.setImageResource(R.drawable.hangman2);
                break;

            case 3:
                hangman_pic.setImageResource(R.drawable.hangman3);
                break;

            case 4:
                hangman_pic.setImageResource(R.drawable.hangman4);
                break;

            case 5:
                hangman_pic.setImageResource(R.drawable.hangman5);
                break;

            case 6:
                hangman_pic.setImageResource(R.drawable.hangman6);
                break;

            case 7:
                hangman_pic.setImageResource(R.drawable.hangman7);
                break;

            case 8:
                hangman_pic.setImageResource(R.drawable.hangman8);
                break;

            case 9:
                hangman_pic.setImageResource(R.drawable.hangman9);
                break;

            case 10:
                hangman_pic.setImageResource(R.drawable.hangman10);
                break;

            case 11:
                hangman_pic.setImageResource(R.drawable.hangman11);
                break;

        }

    }

    private void updateWordView(String value) {
        word.setText(value);
    }

}
