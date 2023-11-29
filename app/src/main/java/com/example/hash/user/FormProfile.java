package com.example.hash.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hash.R;
import com.example.hash.logInto.FormLogin;
import com.example.hash.records.FormRegisterPost;
import com.example.hash.start.FormHomePage;
import com.example.hash.whatsapp.FormWhatsappSend;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class FormProfile extends AppCompatActivity {

    private TextView textView1, buttonDelete;
    private EditText editText1, editText2, editText3, editText4, editText6;
    private Button bt_logout;
    private ImageView image_post, image_home, image_whats;
    String[] messages = {"Conta inativada com sucesso.", "Erro ao inativar conta, favor tente novamente."};

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_profile);
        getSupportActionBar().hide();
        startComponents();
        screenChanges();
    }

    @Override
    protected void onStart() {
        super.onStart();

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Users").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot != null) {
                    textView1.setText(documentSnapshot.getString("full_name"));
                    editText3.setText(documentSnapshot.getString("phone_number"));

                    DateMaskWatcher dateMaskWatcher = new DateMaskWatcher(editText4);
                    editText4.addTextChangedListener(dateMaskWatcher);

                    editText4.setText(documentSnapshot.getString("date_birth"));
                    editText1.setText(documentSnapshot.getString("full_name"));
                    editText6.setText(documentSnapshot.getString("cpf"));
                    editText2.setText(email);
                }
            }
        });
    }

    private void startComponents() {
        editText1 = findViewById(R.id.editTextName);
        editText2 = findViewById(R.id.editTextEmail);
        editText3 = findViewById(R.id.editTextPhone);
        editText4 = findViewById(R.id.editTextDateBirth);
        editText6 = findViewById(R.id.editTextCPF);
        textView1 = findViewById(R.id.textViewFullName);
        image_post = findViewById(R.id.ic_post);
        image_home = findViewById(R.id.ic_home);
        image_whats = findViewById(R.id.ic_whats);
        bt_logout = findViewById(R.id.buttonLogout);
        buttonDelete = findViewById(R.id.buttonDelete);
    }

    private void screenChanges() {
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference userRef = db.collection("Users").document(userID);

                Map<String, Object> updates = new HashMap<>();
                updates.put("status", "inativo");

                userRef.update(updates)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Snackbar snackbar = Snackbar.make(v, messages[0], Snackbar.LENGTH_SHORT);
                                snackbar.setBackgroundTint(Color.WHITE);
                                snackbar.setTextColor(Color.BLACK);
                                snackbar.show();

                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(FormProfile.this, FormLogin.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("Firestore error", "Erro ao inativar conta: " + e.getMessage());

                                Snackbar snackbar = Snackbar.make(v, messages[1], Snackbar.LENGTH_SHORT);
                                snackbar.setBackgroundTint(Color.WHITE);
                                snackbar.setTextColor(Color.BLACK);
                                snackbar.show();
                            }
                        });
            }
        });


        bt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(FormProfile.this, FormLogin.class);
                startActivity(intent);
                finish();
            }
        });

        image_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FormProfile.this, FormRegisterPost.class);
                startActivity(intent);
                finish();
            }
        });

        image_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FormProfile.this, FormHomePage.class);
                startActivity(intent);
                finish();
            }
        });

        image_whats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FormProfile.this, FormWhatsappSend.class);
                startActivity(intent);
                finish();
            }
        });
    }
}