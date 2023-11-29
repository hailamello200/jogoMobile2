package com.example.hash.game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hash.logInto.FormLogin;
import com.example.hash.records.FormRegisterPost;
import com.example.hash.user.FormProfile;
import com.example.hash.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GameActivity extends AppCompatActivity {

    private TextView playerText1, playerText2;
    private ImageView image00, image01, image02, image10, image11, image12, image20, image21, image22, image23, image_arrow;
    private String player1 = "";
    private String player2 = "";
    Game game = new Game();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().hide();
        startComponents();
        screenChanges();

        Bundle data = getIntent().getExtras();

        player1 = data.getString("PlayerStarts");
        player2 = data.getString("anotherPlayer");

        playerText1.setText(player1);
        playerText2.setText(player2);

        message();
        whenClicking();
    }

    private void screenChanges() {
        image_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void message() {

        if (game.turn()) {
            Toast.makeText(getApplicationContext(), "Vez do jogador: " + player1, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Vez do jogador: " + player2, Toast.LENGTH_SHORT).show();
        }
    }

    public void winner() {

        if (game.checking() == 0) {
            Intent intent = new Intent(getApplicationContext(), WinnerActivity.class);
            intent.putExtra("winner", player2);
            startActivity(intent);

            finish();

        } else if (game.checking() == 1) {
            Intent intent = new Intent(getApplicationContext(), WinnerActivity.class);
            intent.putExtra("winner", player1);
            startActivity(intent);

            finish();

        } else if (game.checking() == 2) {
            Intent intent = new Intent(getApplicationContext(), WinnerActivity.class);
            intent.putExtra("winner", "old");
            startActivity(intent);

            finish();
        }
    }

    public void startComponents() {

        playerText1 = findViewById(R.id.playerText1);
        playerText2 = findViewById(R.id.playerText2);

        image00 = findViewById(R.id.image00);
        image01 = findViewById(R.id.image01);
        image02 = findViewById(R.id.image02);
        image10 = findViewById(R.id.image10);
        image11 = findViewById(R.id.image11);
        image12 = findViewById(R.id.image12);
        image20 = findViewById(R.id.image20);
        image21 = findViewById(R.id.image21);
        image22 = findViewById(R.id.image22);
        image23 = findViewById(R.id.image_star_access);
        image_arrow = findViewById(R.id.image_arrow);
    }

    public void whenClicking() {

        image23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                if (currentUser != null){
                    Intent intent = new Intent(GameActivity.this, FormProfile.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(GameActivity.this, FormLogin.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        image00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.setFisrt(0);
                game.setSecond(0);
                game.turn();

                if (game.turn()) {
                    image00.setImageResource(R.drawable.bolinha);
                } else {
                    image00.setImageResource(R.drawable.x);
                }

                game.toCheck(0,0);
                winner();
                message();
                image00.setEnabled(false);
            }
        });

        image01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.setFisrt(0);
                game.setSecond(1);
                game.turn();
                if (game.turn()) {
                    image01.setImageResource(R.drawable.bolinha);
                } else {
                    image01.setImageResource(R.drawable.x);
                }
                game.toCheck(0, 1);
                winner();
                message();
                image01.setEnabled(false);
            }
        });

        image02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.setFisrt(0);
                game.setSecond(1);
                game.turn();
                if (game.turn()) {
                    image02.setImageResource(R.drawable.bolinha);
                } else {
                    image02.setImageResource(R.drawable.x);
                }
                game.toCheck(0, 2);
                winner();
                message();
                image02.setEnabled(false);
            }
        });

        image10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.setFisrt(1);
                game.setSecond(0);
                game.turn();
                if (game.turn()) {
                    image10.setImageResource(R.drawable.bolinha);
                } else {
                    image10.setImageResource(R.drawable.x);
                }
                game.toCheck(1, 0);
                winner();
                message();
                image10.setEnabled(false);
            }
        });

        image11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.setFisrt(1);
                game.setSecond(1);
                game.turn();
                if (game.turn()) {
                    image11.setImageResource(R.drawable.bolinha);
                } else {
                    image11.setImageResource(R.drawable.x);
                }
                game.toCheck(1, 1);
                winner();
                message();
                image11.setEnabled(false);
            }
        });

        image12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.setFisrt(1);
                game.setSecond(2);
                game.turn();
                if (game.turn()) {
                    image12.setImageResource(R.drawable.bolinha);
                } else {
                    image12.setImageResource(R.drawable.x);
                }
                game.toCheck(1, 2);
                winner();
                message();
                image12.setEnabled(false);
            }
        });

        image20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.setFisrt(2);
                game.setSecond(0);
                game.turn();
                if (game.turn()) {
                    image20.setImageResource(R.drawable.bolinha);
                } else {
                    image20.setImageResource(R.drawable.x);
                }
                game.toCheck(2, 0);
                winner();
                message();
                image20.setEnabled(false);
            }
        });

        image21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.setFisrt(2);
                game.setSecond(1);
                game.turn();
                if (game.turn()) {
                    image21.setImageResource(R.drawable.bolinha);
                } else {
                    image21.setImageResource(R.drawable.x);
                }
                game.toCheck(2, 1);
                winner();
                message();
                image21.setEnabled(false);
            }
        });

        image22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.setFisrt(2);
                game.setSecond(2);
                game.turn();
                if (game.turn()) {
                    image22.setImageResource(R.drawable.bolinha);
                } else {
                    image22.setImageResource(R.drawable.x);
                }
                game.toCheck(2, 2);
                winner();
                message();
                image22.setEnabled(false);
            }
        });
    }
}




