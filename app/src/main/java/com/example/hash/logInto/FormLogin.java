package com.example.hash.logInto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hash.R;
import com.example.hash.records.FormRegisterUser;
import com.example.hash.user.FormProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FormLogin extends AppCompatActivity {

    private EditText editText1, editText2;
    private TextView textView3;
    private Button bt_login;
    String[] messages = {"Preencha todos os campos"};

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);

        getSupportActionBar().hide();
        startComponents();
        onClick();
    }

    private void startComponents(){
        bt_login    = findViewById(R.id.button_login);
        editText1   = findViewById(R.id.editTextPersonEmail1);
        editText2   = findViewById(R.id.editTextPersonPassword2);
        textView3   = findViewById(R.id.textViewCreateAnAccount);
    }

    private void onClick() {

        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FormLogin.this, FormRegisterUser.class);
                startActivity(intent);
                finish();
            }
        });

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = editText1.getText().toString();
                String password = editText2.getText().toString();

                if (email.isEmpty() || password.isEmpty()){

                    Snackbar snackbar = Snackbar.make(v, messages[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }else {
                    loginUserAndCheckStatus(v);
                }
            }
        });
    }

    private void loginUserAndCheckStatus(View v) {
        // Realize a autenticação do usuário
        AuthenticateUser(v);
    }

    private void checkUserStatusAndActivateIfInactive() {
        // Obtenha a referência do usuário no Firestore
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference userRef = db.collection("Users").document(userID);

        // Verifique o status atual do usuário
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        // Verifique se o status é inativo
                        String status = document.getString("status");
                        if ("inativo".equals(status)) {
                            // Ative o usuário
                            activateUser(userRef);
                        }
                    } else {
                        Log.e("Firestore error", "Document does not exist");
                    }
                } else {
                    Log.e("Firestore error", "Error getting document: " + task.getException());
                }
            }
        });
    }

    private void activateUser(DocumentReference userRef) {
        // Atualize o status do usuário para "ativo"
        Map<String, Object> updates = new HashMap<>();
        updates.put("status", "ativo");

        userRef.update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firestore", "Usuário ativado com sucesso");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firestore error", "Erro ao ativar conta: " + e.getMessage());
                    }
                });
    }

    private void AuthenticateUser(View view){

        String email = editText1.getText().toString();
        String password = editText2.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    checkUserStatusAndActivateIfInactive();
                    mainScreen();
                } else {
                    String error;

                    try {
                        throw task.getException();
                    } catch (Exception e) {
                        e.printStackTrace();
                        error = "Erro ao realizar login";
                    }

                    Snackbar snackbar = Snackbar.make(view, error, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            profileScreen();
        }
    }

    private void mainScreen() {
        Intent intent = new Intent(FormLogin.this, FormProfile.class);
        startActivity(intent);
        finish();
    }

    private void profileScreen() {
        Intent intent = new Intent(FormLogin.this, FormProfile.class);
        startActivity(intent);
        finish();
    }
}