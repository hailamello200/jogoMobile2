package com.example.hash.records;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hash.R;
import com.example.hash.start.FormHomePage;
import com.example.hash.user.FormProfile;
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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FormRegisterPost extends AppCompatActivity {

    private Button buttonPost;
    private EditText editText1;
    private TextView textView1, textView2;
    String[] messages = {"Verifique se existe informação para registrar"};
    private ImageView ic_image_profile, ic_image_home, ic_image_whats;

    private CheckBox checkbox_post_anonymous;

    FirebaseFirestore db;

    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_register_post);
        getSupportActionBar().hide();
        startComponents();
        screenChanges();

        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Users").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot != null){
                    textView1.setText(documentSnapshot.getString("name"));
                    textView2.setText(documentSnapshot.getString("email"));
                }
            }
        });
    }

    private void registerPost() {

        String post_text = editText1.getText().toString();
        Date current_post_date = new Date();

        Map<String, Object> postUser = new HashMap<>();
        postUser.put("text", post_text);
        postUser.put("user_id", userID);
        postUser.put("date", current_post_date);
        postUser.put("likes", 0);

        if (checkbox_post_anonymous.isChecked()) {
            postUser.put("anonymous", true);
        } else {
            postUser.put("anonymous", false);
        }

        db.collection("UserPost")
                .add(postUser)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String idFirebase = documentReference.getId();

                        db.collection("UserPost")
                                .document(idFirebase)
                                .update("post_id", idFirebase)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("Firestore", "Document successfully updated with ID: " + idFirebase);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("Firestore", "Error updating document", e);
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Firestore", "Error adding new document: " + e);
                    }
                });
    }

    private void screenChanges() {
        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String post_text = editText1.getText().toString();

                if (post_text.isEmpty()) {

                    Snackbar snackbar = Snackbar.make(v, messages[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                } else {

                    registerPost();

                    Intent intent = new Intent(FormRegisterPost.this, FormRegisterPost.class);
                    startActivity(intent);
                }
            }
        });

        ic_image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormRegisterPost.this, FormProfile.class);
                startActivity(intent);
                finish();
            }
        });

        ic_image_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormRegisterPost.this, FormHomePage.class);
                startActivity(intent);
                finish();
            }
        });

        ic_image_whats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormRegisterPost.this, FormWhatsappSend.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void startComponents(){
        buttonPost = findViewById(R.id.buttonPost);
        editText1 = findViewById(R.id.editTextPost1);
        textView1 = findViewById(R.id.textViewFullNamePost);
        textView2 = findViewById(R.id.textViewEmailPost);
        ic_image_profile = findViewById(R.id.ic_profile);
        ic_image_home = findViewById(R.id.ic_home);
        ic_image_whats = findViewById(R.id.ic_whats);
        checkbox_post_anonymous = findViewById(R.id.checkbox_post_anonymous);
    }
}