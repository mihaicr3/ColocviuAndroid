package com.example.myapplication;



import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ProductDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        ImageView productImage = findViewById(R.id.product_image);
        TextView productTitle = findViewById(R.id.product_title);
        TextView productDescription = findViewById(R.id.product_description);

        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String thumbnail = getIntent().getStringExtra("thumbnail");

        productTitle.setText(title);
        productDescription.setText(description);
        Glide.with(this).load(thumbnail).into(productImage);
    }
}
