package org.tttnhung.hien;

import android.content.Context;

import android.os.Bundle;

import android.os.PersistableBundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainActivity extends AppCompatActivity {

    private EditText mSearchField;
    private ImageButton mSearchBtn;

    private RecyclerView mResultList;

    private FirebaseDatabase mData;
    private DatabaseReference mRefer;

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

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String searchText = mSearchField.getText().toString();

                firebaseUserSearch(searchText);

            }
        });

    }

    private void firebaseUserSearch(String searchText) {

        Toast.makeText(MainActivity.this, "Started Search", Toast.LENGTH_LONG).show();

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
