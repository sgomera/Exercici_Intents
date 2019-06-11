package com.example.exercici_intents;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ShowText extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_text);

        //hem de rescatar les dades del bundle.
        Bundle data = getIntent().getExtras();

        String enteredText = data.getString("text");

        //mostrem el resultat en la vista corresponent
        TextView textView = (TextView)findViewById(R.id.textView);
        textView.setText(enteredText);
    }
}
