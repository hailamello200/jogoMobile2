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

import java.util.Random;

public class HeadsOrTailsActivity extends AppCompatActivity {

    private ImageView imageStarts, image_arrow;
    private TextView playerText;
    private Button startGameButton;

    String playerStarts = "";
    String anotherPlayer = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heads_or_tails);
        getSupportActionBar().hide();

        imageStarts = findViewById(R.id.imageStarts);
        playerText = findViewById(R.id.playerText);
        startGameButton = findViewById(R.id.btnStartGame);
        image_arrow = findViewById(R.id.image_home);

        validate();
        whenClicking();
    }

    public void validate() {

        int start = new Random().nextInt(2);

        Bundle dados = getIntent().getExtras();

        String player1 = dados.getString("Player1");
        String player2 = dados.getString("Player2");

        if(start == 0){
            imageStarts.setImageResource(R.drawable.logo3);
            playerText.setText("Jogador " + player1 + " começa jogando");
            playerStarts = player1;
            anotherPlayer = player2;
        }
        else{
            imageStarts.setImageResource(R.drawable.logo3);
            playerText.setText("Jogador " + player2 + " começa jogando");
            playerStarts = player2;
            anotherPlayer = player1;
        }
    }

    public void whenClicking() {

        image_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HeadsOrTailsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), GameActivity.class);

                intent.putExtra("PlayerStarts", playerStarts);
                intent.putExtra("anotherPlayer", anotherPlayer);

                startActivity(intent);
                finish();
            }
        });

        imageStarts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                if (currentUser != null){
                    Intent intent = new Intent(HeadsOrTailsActivity.this, FormProfile.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(HeadsOrTailsActivity.this, FormLogin.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
