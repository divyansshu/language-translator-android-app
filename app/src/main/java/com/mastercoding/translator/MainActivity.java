package com.mastercoding.translator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

public class MainActivity extends AppCompatActivity {

    //widgets
    private Spinner fromSpinner, toSpinner;
    private EditText sourceEdit;
    private Button btn;
    private TextView translatedTV;

    //Source Array of Strings - Spinner's Data
    String[] fromLanguages = {"from", "English", "Spanish", " Arabic", "French", "Japanese", "Russian", "Hindi", "Urdu"};
    String[] toLanguages = {"to","English", "Spanish", " Arabic", "French", "Japanese", "Russian", "Hindi", "Urdu"};

    //permissions
    private static final int REQUEST_CODE = 1;
    String  languagesCode, fromLanguageCode, toLanguageCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fromSpinner = findViewById(R.id.idFromSpinner);
        toSpinner = findViewById(R.id.idToSpinner);
        sourceEdit = findViewById(R.id.idEditSource);
        btn = findViewById(R.id.button);
        translatedTV = findViewById(R.id.idTvTranslatedTV);

        //spinner 1
        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fromLanguageCode = getLanguageCode(fromLanguages[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter fromAdapter = new ArrayAdapter(this, R.layout.spinner, fromLanguages);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(fromAdapter);

        //spinner 2
        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                toLanguageCode = getLanguageCode(toLanguages[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter toAdapter = new ArrayAdapter(this, R.layout.spinner, toLanguages);
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toSpinner.setAdapter(toAdapter);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translatedTV.setText("");

                if(sourceEdit.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "please enter any text", Toast.LENGTH_SHORT).show();
                }
                else if(fromLanguageCode.isEmpty()) {
                    Toast.makeText(MainActivity.this, "please select any Source language", Toast.LENGTH_SHORT).show();
                }
                else if(toLanguageCode.isEmpty()) {
                    Toast.makeText(MainActivity.this, "please select the target language", Toast.LENGTH_SHORT).show();
                }
                else {
                    translateText(fromLanguageCode, toLanguageCode, sourceEdit.getText().toString());
                }
            }
        });

    }

    private void translateText(String fromLanguageCode, String toLanguageCode, String src) {

        translatedTV.setText("Downloading Language Model");

        try {

            TranslatorOptions options = new TranslatorOptions.Builder().setSourceLanguage(fromLanguageCode)
                    .setTargetLanguage(toLanguageCode).build();

            Translator translator = Translation.getClient(options);

            DownloadConditions conditions = new DownloadConditions.Builder().build();

            translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {

                    translatedTV.setText("Translating....");

                    translator.translate(src).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            translatedTV.setText(s);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Fail to translate", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Fail to download language", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e) {
         e.printStackTrace();
        }
    }

    private String getLanguageCode(String language) {

        String languageCode;

        switch (language) {

            case "English":
                languageCode = TranslateLanguage.ENGLISH;
                break;

            case "Spanish":
                languageCode = TranslateLanguage.SPANISH;
                break;

            case "Arabic":
                languageCode = TranslateLanguage.ARABIC;
                break;

            case "French":
                languageCode = TranslateLanguage.FRENCH;
                break;


            case "Japanese":
                languageCode = TranslateLanguage.JAPANESE;
                break;

            case "Russian":
                languageCode = TranslateLanguage.RUSSIAN;
                break;

            case "Hindi":
                languageCode = TranslateLanguage.HINDI;
                break;

            case "Urdu":
                languageCode = TranslateLanguage.URDU;
                break;

            default:
                languageCode = "";
        }
        return languageCode;
    }
}