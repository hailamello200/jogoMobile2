package com.example.hash.game;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hash.R;
import com.example.hash.logInto.FormLogin;
import com.example.hash.user.FormProfile;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText editPlayer1, editPlayer2;
    private Button playButton;
    private ImageView image_star_accessMain;

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        startComponents();
        screenChanges();
    }

    private void startComponents(){
        editPlayer1 = findViewById(R.id.editPlayer1);
        editPlayer2 = findViewById(R.id.editPlayer2);
        playButton  = findViewById(R.id.btnStart);
        image_star_accessMain = findViewById(R.id.image_star_accessMain);
    }

    private void screenChanges() {
        image_star_accessMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentUser != null) {
                    Intent intent = new Intent(MainActivity.this, FormProfile.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(MainActivity.this, FormLogin.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String player1 = editPlayer1.getText().toString();
                String player2 = editPlayer2.getText().toString();

                if ("".equals(player1) || "".equals(player2)) {
                    Toast.makeText(getApplicationContext(), "Por favor preencha todos campos", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), HeadsOrTailsActivity.class);

                    intent.putExtra("Player1", player1);
                    intent.putExtra("Player2", player2);

                    startActivity(intent);
                }
            }
        });
    }
}
