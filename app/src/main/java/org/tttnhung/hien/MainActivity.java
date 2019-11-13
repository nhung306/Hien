package org.tttnhung.hien;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    private ArrayList<ImageInfo> list = new ArrayList<>();
    private ImageButton btnSearch;
    private RecyclerView recyclerView;
    private EditText textSearch;
    private RecyclerViewAdapter adapter;
    private ImageInfo imageInfo;
    private String name;

    private FirebaseDatabase mData;
    private DatabaseReference mRefer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //firebase
        mData = FirebaseDatabase.getInstance();
        mRefer = mData.getReference("Images");

        textSearch = (EditText) findViewById(R.id.SearchField);
        //recyclerview
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        //clickButton
        btnSearch = (ImageButton) findViewById(R.id.imageBtn);

        firebaseSearch(

        );

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //take name from edit text
                name = textSearch.getText().toString();
//                if(name != null){
//                    Toast.makeText(MainActivity.this,"Load file",Toast.LENGTH_SHORT).show();
//
//                    progressDialog = new ProgressDialog(MainActivity.this);
//
//                    // Setting up message in Progress dialog.
//                    progressDialog.setMessage("Loading Images From Firebase.");
//
//                    // Showing progress dialog.
//                    progressDialog.show();
//                }
//                else {
//                    Toast.makeText(MainActivity.this,"Điền thông tin cần tìm",Toast.LENGTH_SHORT).show();
//                }

            }
            
        });
    }
    private void firebaseSearch() {
            mRefer.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<String> keys = new ArrayList<>();
                    for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                        keys.add(keyNode.getKey());
                        imageInfo = keyNode.getValue(ImageInfo.class);
                        list.add(imageInfo);
                    }
                    adapter = new RecyclerViewAdapter(MainActivity.this, list);
                    recyclerView.setAdapter(adapter);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w("TAG", "Failed to read value.", databaseError.toException());

                }
            });
        }
}
