package com.example.hash.start;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hash.R;
import com.example.hash.post.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MyPostAdapterHome extends RecyclerView.Adapter<MyPostAdapterHome.MyViewHolder>{
    Context context;
    ArrayList<Post> postArrayList;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MyPostAdapterHome(Context context, ArrayList<Post> postArrayList) {
        this.context = context;
        this.postArrayList = postArrayList;
    }

    @Override
    public MyPostAdapterHome.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.form_post_item_home, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        Post post = postArrayList.get(position);

        holder.textView1.setText(post.text);
        holder.textView2.setText(formatDate(post.date, "dd/MM/YYYY"));
        holder.textViewLikes.setText(String.valueOf(post.getLikes()) + " Curtidas");

        if (!post.anonymous) {
            searchUsername(post.user_id, new UserRefCallback() {
                @Override
                public void onUserRefReady(DocumentReference userRef) {
                    userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String username = documentSnapshot.getString("full_name");
                                holder.textView3.setText(username);
                            }
                        }
                    });
                }
            });
        } else {
            holder.textView3.setText("Anônimo");
        }

        holder.ic_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int likes = (int) post.getLikes();

                likes++;

                final MyViewHolder myViewHolder = holder;
                updateTheLikesOnThePost(likes, post.getPost_id(), myViewHolder);
                holder.ic_like.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void updateTheLikesOnThePost(int likes, String post_id, final MyViewHolder myHolder) {
        DocumentReference docRef = db.collection("UserPost").document(post_id);

        Map<String, Object> updates = new HashMap<>();

        updates.put("likes", likes);

        docRef.update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("db", "Change performed successfully!" +likes);
                        myHolder.textViewLikes.setText(likes + " Curtidas");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("db_error", "Error when changing the amount of likes on the post", e);
                    }
                });
    }

    public interface UserRefCallback {
        void onUserRefReady(DocumentReference userRef);
    }

    private void searchUsername(String userID, UserRefCallback callback) {
        db.collection("Users")
                .whereEqualTo("id", userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DocumentReference userRef = document.getReference();
                                callback.onUserRefReady(userRef);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private String formatDate(Timestamp timestamp, String format) {
        Locale locale = new Locale("pt", "BR");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            Instant instant = Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanoseconds());
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format, locale);
            return localDateTime.format(formatter);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView1, textView2, textView3, textViewLikes;
        ImageView ic_like;

        public MyViewHolder(View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.textViewPost1);
            textView3 = itemView.findViewById(R.id.textViewUser3);
            textView2 = itemView.findViewById(R.id.textViewDatePost2);
            ic_like = itemView.findViewById(R.id.ic_like);
            textViewLikes = itemView.findViewById(R.id.textViewLikes);
        }
    }
}
