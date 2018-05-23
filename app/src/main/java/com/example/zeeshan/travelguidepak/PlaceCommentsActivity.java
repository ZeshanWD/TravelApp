package com.example.zeeshan.travelguidepak;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaceCommentsActivity extends AppCompatActivity {

    private Toolbar commentToolbar;
    private EditText tvComment;
    private ImageView commentBtn;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private PlaceCommentAdapter placeCommentAdapter;
    private List<Comment> commentList;
    private RecyclerView RecycletCommentList;

    private String placeId;
    private String currentUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_comments);

        commentToolbar = (Toolbar) findViewById(R.id.comment_toolbar);
        setSupportActionBar(commentToolbar);
        getSupportActionBar().setTitle("Comments");

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();

        placeId = getIntent().getExtras().getString("placeId");



        tvComment = (EditText) findViewById(R.id.tv_comment);
        RecycletCommentList = (RecyclerView) findViewById(R.id.comment_list);
        commentBtn = (ImageView) findViewById(R.id.comment_btn);

        commentList = new ArrayList<>();
        placeCommentAdapter = new PlaceCommentAdapter(commentList);
        RecycletCommentList.setHasFixedSize(true);
        RecycletCommentList.setLayoutManager(new LinearLayoutManager(this));
        RecycletCommentList.setAdapter(placeCommentAdapter);


        //
        firebaseFirestore.collection("Places/" + placeId + "/Comments").addSnapshotListener(PlaceCommentsActivity.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (!documentSnapshots.isEmpty()) { // para asegurarnos que no haga nada si no hay nada en la base de datos.

                    for(DocumentChange doc: documentSnapshots.getDocumentChanges()){
                        if(doc.getType() == DocumentChange.Type.ADDED){

                            Comment comment = doc.getDocument().toObject(Comment.class);
                            commentList.add(comment);
                            placeCommentAdapter.notifyDataSetChanged();

                        }
                    }

                }
            }
        });


        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = tvComment.getText().toString();
                if(!comment.isEmpty()){

                    Map<String, Object> commentMap = new HashMap<>();
                    commentMap.put("message", comment);
                    commentMap.put("userId", currentUserId);
                    commentMap.put("timestamp", FieldValue.serverTimestamp());

                    firebaseFirestore.collection("Places/" + placeId + "/Comments").add(commentMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(PlaceCommentsActivity.this, "Message Saved", Toast.LENGTH_LONG).show();
                                tvComment.setText("");
                            } else {
                                Toast.makeText(PlaceCommentsActivity.this, "Error while saving comment " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
            }
        });



    }
}
