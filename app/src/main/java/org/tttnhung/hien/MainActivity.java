package org.tttnhung.hien;

import android.content.Context;

import android.os.Bundle;

import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText mSearchField;
    private ImageButton mSearchBtn;

    private RecyclerView mResultList;

    private FirebaseDatabase mData;
    private DatabaseReference mRefer;


    private ArrayList<ImageInfo> list = new ArrayList<>();
    private AdapterSearch adapter;
    private ImageInfo imageInfo;

    ArrayList<String> names;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //firebase
        mData = FirebaseDatabase.getInstance();
        mRefer = mData.getReference("Images");

        mSearchField = (EditText) findViewById(R.id.search_field);
        mSearchBtn = (ImageButton) findViewById(R.id.search_btn);

        mResultList = (RecyclerView) findViewById(R.id.result_list);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        names = new ArrayList<>();
        firebaseSearch();


        mSearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String searchText = mSearchField.getText().toString();

                if(searchText.isEmpty()){
                    Toast.makeText(MainActivity.this,"Nhập từ khóa",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this,"Đang load...",Toast.LENGTH_SHORT).show();

                    firebaseSearch(searchText);
                }


            }
        });

    }

    private void filter(String text) {
        ArrayList<ImageInfo> filteredList = new ArrayList<>();

        for (ImageInfo item : list) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
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

                    names.add(imageInfo.getName());
                }
                adapter = new AdapterSearch(MainActivity.this, list);

                mResultList.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("TAG", "Failed to read value.", databaseError.toException());

            }
        });
    }

    private void firebaseSearch(String searchText) {

        Query firebaseSearchQuery = mRefer.orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff");

        FirebaseRecyclerAdapter<ImageInfo, ImagesViewHolder>
                firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ImageInfo, ImagesViewHolder>(

                ImageInfo.class,
                R.layout.list_layout,
                ImagesViewHolder.class,
                firebaseSearchQuery
        ) {
            @Override
            protected void populateViewHolder(ImagesViewHolder viewHolder, ImageInfo model, int position) {
                viewHolder.setDetails(getApplicationContext(), model.getName() , model.getImage());
            }
        };

        mResultList.setAdapter(firebaseRecyclerAdapter);

    }


    // View Holder Class
    public static class ImagesViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public ImagesViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setDetails(Context ctx, String ImageName, String Image){

            TextView user_name = (TextView) mView.findViewById(R.id.name_text);
            ImageView user_image = (ImageView) mView.findViewById(R.id.image);

            user_name.setText(ImageName);
            Glide.with(ctx).load(Image).into(user_image);

        }
    }

}
