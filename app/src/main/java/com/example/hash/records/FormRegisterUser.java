package com.example.hash.records;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hash.R;
import com.example.hash.logInto.FormLogin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class FormRegisterUser extends AppCompatActivity {

    private EditText editText1, editText2, editText3, editText4, editText5, editText7;
    private ImageView registerUser;
    String[] messages = {"Preencha todos os campos.", "Cadastro realizado com sucesso."};
    String userID;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_register_user_first_data);
        getSupportActionBar().hide();
        FirebaseApp.initializeApp(this);

        startComponents();
        screenChanges();
    }

    private void startComponents(){
        editText1 = findViewById(R.id.editTextUserEmail1);
        editText2 = findViewById(R.id.editTextUserPassword5);
        editText3 = findViewById(R.id.editTextUserBirthDate3);
        editText4 = findViewById(R.id.editTextUserTelephone4);
        editText5 = findViewById(R.id.editTextUserName6);
        editText7 = findViewById(R.id.editTextUserCPF6);
        registerUser = findViewById(R.id.ic_arrow);
    }

    private void screenChanges() {
        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user_email = editText1.getText().toString();
                String user_password = editText2.getText().toString();
                String user_name = editText5.getText().toString();
                String user_birth_date = editText3.getText().toString();
                String user_telephone = editText4.getText().toString();
                String user_cpf = editText7.getText().toString();

                if (user_email.trim().isEmpty() || user_password.trim().isEmpty() || user_name.trim().isEmpty() ||
                    user_birth_date.trim().isEmpty() || user_telephone.trim().isEmpty() || user_cpf.trim().isEmpty()) {

                    Snackbar snackbar = Snackbar.make(v, messages[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();

                } else {
                    checksIfThereIsAUserForTheCpfAndEmailProvided(v);
                }
            }
        });
    }

    private void checksIfThereIsAUserForTheCpfAndEmailProvided(View v) {
        String user_email = editText1.getText().toString();
        String user_cpf = editText7.getText().toString();

        db.collection("Users")
                .whereEqualTo("email", user_email)
                .whereEqualTo("cpf", user_cpf)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null) {
                                if (!querySnapshot.isEmpty()) {
                                    Snackbar snackbar = Snackbar.make(v, "Usuário já cadastrado, revise os dados", Snackbar.LENGTH_SHORT);
                                    snackbar.setBackgroundTint(Color.WHITE);
                                    snackbar.setTextColor(Color.BLACK);
                                    snackbar.show();
                                } else {
                                    registerUser(v);
                                }
                            } else {
                                Log.e("Firestore error", "QuerySnapshot is null");
                            }
                        } else {
                            Log.e("Firestore error", "Error getting documents: " + task.getException());
                        }
                    }
                });

    }

    private void loginScreen() {
        Intent intent = new Intent(FormRegisterUser.this, FormLogin.class);
        startActivity(intent);
        finish();
    }

    private void registerUser(View v) {
        String user_email = editText1.getText().toString();
        String user_password = editText2.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    saveUserData();

                    Snackbar snackbar = Snackbar.make(v, messages[1], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                } else {
                    returnsErrorWhenTryingToRegisterUserWithoutCorrectData(task, v);
                }
            }
        });
    }

    private void returnsErrorWhenTryingToRegisterUserWithoutCorrectData(Task task, View v) {
        String errorMessage;

        try {
            throw task.getException();

        } catch (FirebaseAuthWeakPasswordException e) {
            errorMessage = "A senha precisa ter no mínimo 6 caracteres";

        }catch (FirebaseAuthUserCollisionException e) {
            errorMessage = "Já existe uma conta com este email";

        }catch (FirebaseAuthInvalidCredentialsException e){
            errorMessage = "E-mail inválido";

        } catch (Exception e){
            errorMessage = "Erro ao cadastrar usuário";
        }

        Snackbar snackbar = Snackbar.make(v, errorMessage, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(Color.WHITE);
        snackbar.setTextColor(Color.BLACK);
        snackbar.show();
    }

    private void saveUserData(){
        String user_email = editText1.getText().toString();
        String user_password = editText2.getText().toString();
        String user_date_birth = editText3.getText().toString();
        String user_phone = editText4.getText().toString();
        String user_full_name = editText5.getText().toString();
        String user_cpf = editText7.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Map<String, Object> users = new HashMap<>();
        users.put("date_birth", user_date_birth);
        users.put("full_name", user_full_name);
        users.put("phone_number", user_phone);
        users.put("email", user_email);
        users.put("password", user_password);
        users.put("cpf", user_cpf);
        users.put("id", userID);
        users.put("status", "ativo");

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Users").document(userID);
        documentReference.set(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("db", "Success in saving data");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("db_error", "Error saving data " + e.toString());
                    }
                });
        loginScreen();
    }
}