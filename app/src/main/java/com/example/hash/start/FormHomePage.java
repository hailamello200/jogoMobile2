package com.example.hash.start;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hash.R;
import com.example.hash.post.Post;
import com.example.hash.records.FormRegisterPost;
import com.example.hash.user.FormProfile;
import com.example.hash.whatsapp.FormWhatsappSend;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class FormHomePage extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Post> postArrayList;
    MyPostAdapterHome myPostAdapterHome;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressDialog progressDialog;
    String userID;
    private TextView textViewEmail, textViewFullName;
    private ImageView image_post, image_profile, image_whats;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_home_page);
        getSupportActionBar().hide();

        startComponents();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        postArrayList = new ArrayList<Post>();
        myPostAdapterHome = new MyPostAdapterHome(FormHomePage.this, postArrayList);
        recyclerView.setAdapter(myPostAdapterHome);

        EventChangeListener();
        screenChanges();
    }

    private void EventChangeListener() {
        db.collection("UserPost").orderBy("date", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();

                            Log.e("Firestore error", Objects.requireNonNull(error.getMessage()));
                            return;
                        }
                        assert value != null;

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                postArrayList.add(dc.getDocument().toObject(Post.class));
                            }
                            myPostAdapterHome.notifyDataSetChanged();

                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                        }
                    }
                });
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
                    textViewFullName.setText("Olá, " + documentSnapshot.getString("full_name"));
                    textViewEmail.setText(documentSnapshot.getString("email"));
                }
            }
        });
    }

    private void screenChanges() {
        image_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FormHomePage.this, FormRegisterPost.class);
                startActivity(intent);
                finish();
            }
        });

        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FormHomePage.this, FormProfile.class);
                startActivity(intent);
                finish();
            }
        });

        image_whats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FormHomePage.this, FormWhatsappSend.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void startComponents() {
        textViewFullName = findViewById(R.id.textViewFullName);
        textViewEmail = findViewById(R.id.textViewEmail);
        image_post = findViewById(R.id.ic_post);
        image_profile = findViewById(R.id.ic_profile);
        image_whats = findViewById(R.id.ic_whatsapp);
        recyclerView = findViewById(R.id.recyclerView);
    }
}