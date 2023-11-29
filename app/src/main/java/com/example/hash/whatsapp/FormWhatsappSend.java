package com.example.hash.whatsapp;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hash.R;
import com.example.hash.records.FormRegisterPost;
import com.example.hash.start.FormHomePage;
import com.example.hash.user.FormProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FormWhatsappSend extends AppCompatActivity {

    private EditText editTextSend, editTextNumber;
    private Button bt_send;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userID;
    private TextView textView1, textView2;
    private ImageView ic_image_profile, ic_image_home, ic_image_post;

    private CheckBox checkbox_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_whatsapp_send);
        getSupportActionBar().hide();
        startComponents();
        screenChanges();
    }

    @Override
    protected void onStart() {
        super.onStart();

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        searchUserData();
        checkbox_message.setVisibility(View.VISIBLE);
    }

    private void screenChanges() {
        ic_image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormWhatsappSend.this, FormProfile.class);
                startActivity(intent);
                finish();
            }
        });

        ic_image_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormWhatsappSend.this, FormHomePage.class);
                startActivity(intent);
                finish();
            }
        });

        ic_image_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormWhatsappSend.this, FormRegisterPost.class);
                startActivity(intent);
                finish();
            }
        });

        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkbox_message.isChecked()) {
                    View mainView = findViewById(android.R.id.content);
                    checkIfThereIsAPredefinedMessage(mainView);
                } else {
                    ifThereIsAPreDefinedMessageRegisterAnUndefinedMessage(view);
                }
            }
        });
    }

    private void checkIfThereIsAPredefinedMessage(View mainView) {
        db.collection("UserMessage")
                .whereEqualTo("user_id", userID)
                .whereEqualTo("message_predefined", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            QuerySnapshot querySnapshot = task.getResult();

                            if (querySnapshot != null) {
                                if (!querySnapshot.isEmpty()) {

                                    String predefinedMessage = querySnapshot.getDocuments().get(0).getString("message_text");
                                    String predefinedContact = querySnapshot.getDocuments().get(0).getString("message_contact");

                                    Intent waIntent = new Intent(Intent.ACTION_VIEW);
                                    waIntent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + predefinedContact + "&text=" + predefinedMessage));
                                    startActivity(waIntent);
                                } else {
                                    String text = "Nenhuma mensagem pré-definida encontrada. Favor, informe os dados para cadastrar uma mensagem!";
                                    showSnackbar(mainView, text);
                                }
                            } else {
                                String text = "Nenhuma mensagem pré-definida encontrada. Favor, informe os dados para cadastrar uma mensagem!";
                                showSnackbar(mainView, text);
                                Log.e("Firestore error", "QuerySnapshot is null");
                            }
                        } else {
                            Log.e("Firestore error", "Error getting documents: " + task.getException());
                        }
                    }
                });
    }

    private void checksIfThereIsAnyMessageForTheUser(View view) {
        db.collection("UserMessage")
                .whereEqualTo("user_id", userID)
                .whereEqualTo("message_predefined", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();

                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                String message = editTextSend.getText().toString();
                                String number = editTextNumber.getText().toString();

                                createSentMessageRecord(message, number, false);
                            } else {
                                // Nenhuma mensagem pré-definida encontrada
                                String message = editTextSend.getText().toString();
                                String number = editTextNumber.getText().toString();

                                createSentMessageRecord(message, number, true);
                            }
                        } else {
                            // Tratamento de erro ao obter as mensagens
                            showSnackbar(view, "Erro ao obter mensagens pré-definidas.");
                        }
                    }
                });
    }

    private void ifThereIsAPreDefinedMessageRegisterAnUndefinedMessage(View view) {

        String message = editTextSend.getText().toString();
        String number = editTextNumber.getText().toString();

        if (message.isEmpty() && number.isEmpty()) {
            showSnackbar(view, "Por favor, digite as informações corretamente.");
        } else {
            checksIfThereIsAnyMessageForTheUser(view);

            Intent waIntent = new Intent(Intent.ACTION_VIEW);
            waIntent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + editTextNumber + "&text=" + editTextSend));
            startActivity(waIntent);
        }
    }

    private void searchUserData() {
        DocumentReference documentReference = db.collection("Users").document(userID);

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot != null){
                    textView1.setText(documentSnapshot.getString("full_name"));
                    textView2.setText(documentSnapshot.getString("email"));
                }
            }
        });
    }

    private void createSentMessageRecord(String message, String number, boolean messagePredefined) {

        Date current_post_date = new Date();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> messages = new HashMap<>();

        messages.put("message_text", message);
        messages.put("message_contact", number);
        messages.put("user_id", userID);
        messages.put("message_date", current_post_date);
        messages.put("message_predefined", messagePredefined);

        db.collection("UserMessage")
                .add(messages)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String idFirebase = documentReference.getId();

                        db.collection("UserMessage")
                                .document(idFirebase)
                                .update("message_id", idFirebase)
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

//    private void fetchPredefinedMessageForTheUser(View mainView) {
//        CollectionReference userMessageCollection = db.collection("UserMessage");
//
//        Query query = userMessageCollection
//                .whereEqualTo("user_id", userID)
//                .whereEqualTo("message_predefined", true);
//
//        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException error) {
//                if (error != null) {
//                    Log.e("Firestore error", "Erro ao obter mensagens pré-definidas", error);
//                    return;
//                }
//
//                if (querySnapshot != null && !querySnapshot.isEmpty()) {
//                    String predefinedMessage = querySnapshot.getDocuments().get(0).getString("message_text");
//                    String predefinedContact = querySnapshot.getDocuments().get(0).getString("message_contact");
//
//                    Intent waIntent = new Intent(Intent.ACTION_VIEW);
//                    waIntent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + predefinedContact + "&text=" + predefinedMessage));
//                    startActivity(waIntent);
//                } else {
//                    String text = "Nenhuma mensagem pré-definida encontrada. Favor, informe os dados e selecione o check para cadastrar uma mensagem!";
//                    showSnackbar(mainView, text);
//
//                    String message = editTextSend.getText().toString();
//                    String number = editTextNumber.getText().toString();
//
//                    if (!message.isEmpty() && !number.isEmpty() && checkbox_message.isChecked()) {
//                        createSentMessageRecord(message, number, true);
//                    } else {
//                        String text_error = "Para cadastrar uma mensagem pré-definida, favor, selecione o check e informe os dados da mensagem!";
//                        showSnackbar(mainView, text_error);
//                    }
//                }
//            }
//        });
//    }

    private void showSnackbar(View mainView, String text) {
        Snackbar snackbar = Snackbar.make(mainView, text, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(Color.WHITE);
        snackbar.setTextColor(Color.BLACK);
        snackbar.show();
    }

    private void startComponents() {
        editTextSend = findViewById(R.id.editTextSend);
        editTextNumber = findViewById(R.id.editTextNumber);
        bt_send = findViewById(R.id.buttonSend);
        textView1 = findViewById(R.id.textViewFullName);
        checkbox_message = findViewById(R.id.checkbox_message);
        textView2 = findViewById(R.id.textViewEmail);

        ic_image_profile = findViewById(R.id.ic_profile);
        ic_image_home = findViewById(R.id.ic_home);
        ic_image_post = findViewById(R.id.ic_post);
    }

}
