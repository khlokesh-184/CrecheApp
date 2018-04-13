package com.daemons.crecheapp.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.daemons.crecheapp.R;

/**
 * Created by LOKESH on 31-03-2018.
 */

public class ChildDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_details);
        Intent i = getIntent();
        final int cID = i.getExtras().getInt("childId");
        TextView childname = (TextView) findViewById(R.id.c_name);
        TextView childDOB = (TextView) findViewById(R.id.c_dob);
        TextView childPriContact = (TextView) findViewById(R.id.c_pri_contact);
        TextView childId = (TextView) findViewById(R.id.c_id);
        TextView childWorkerId = (TextView) findViewById(R.id.c_worker_id);
        TextView childParentId = (TextView) findViewById(R.id.c_parent_id);
        childDOB.setText("DOB:  "+i.getExtras().getString("dob"));
        childId.setText("CHILD ID:  "+cID);
        childname.setText(i.getExtras().getString("childName"));
        childPriContact.setText("Primary Contact:  "+i.getExtras().getString("priContact"));
        childWorkerId.setText("Worker ID:  "+i.getExtras().getInt("workerId"));
        childParentId.setText("Parent ID:  "+i.getExtras().getInt("parentId"));

        ImageView im = (ImageView) findViewById(R.id.profile);
        Glide.with(getApplicationContext()).load(getString(R.string.BaseURL)+"/child/getchild/photo/"+5001)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(im);

        Button b = (Button) findViewById(R.id.take_attendance);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ChildDetailsActivity.this, FaceVerificationActivity.class);
                i.putExtra("childId",cID );
                startActivity(i);
            }
        });
    }
}
