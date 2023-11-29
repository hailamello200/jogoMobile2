package com.example.hash.game;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hash.R;
import com.example.hash.logInto.FormLogin;
import com.example.hash.user.FormProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WinnerActivity extends AppCompatActivity {

    private TextView winnerText;
    private Button replayButton;
    private ImageView image_star_access, image_arrow;
    String winner1 = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner);
        getSupportActionBar().hide();

        winnerText = findViewById(R.id.winnerText);
        replayButton = findViewById(R.id.replayButton);
        image_star_access = findViewById(R.id.image_star_access);
        image_arrow = findViewById(R.id.image_arrow);

        Bundle data = getIntent().getExtras();

        winner1 = data.getString("winner");

        if ("old".equals(winner1)) {
            winnerText.setText("Deu velha");
        }else {
            winnerText.setText("O jogador " + winner1 + " venceu a partida");
        }

        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        image_star_access.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                if (currentUser != null){
                    Intent intent = new Intent(WinnerActivity.this, FormProfile.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(WinnerActivity.this, FormLogin.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        image_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WinnerActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
