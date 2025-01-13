////package com.example.myapplication;
////
////import androidx.appcompat.app.AppCompatActivity;
////
////import android.content.Context;
////import android.os.Bundle;
////import android.view.View;
////import android.widget.Button;
////import android.widget.ImageView;
////import android.widget.Toast;
////
////public class MainActivity extends AppCompatActivity {
////    int counterimagine=0;
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_main);
////        Button button = (Button) findViewById(R.id.button1);
////        Button button3=(Button)  findViewById(R.id.button3);
////        ImageView imageView1=(ImageView) findViewById(R.id.imageView2);
////       imageView1.setImageResource(R.drawable.poza1);
////        int[] myImageList = new int[]{R.drawable.poza1, R.drawable.dashboard,R.drawable.screenshot};
////        // operations to be performed
////        // when user tap on the button
////
////        if (button != null) {
////            button3.setOnClickListener((View.OnClickListener) (new View.OnClickListener() {
////                public final void onClick(View it) {
////
////
////                    counterimagine+=1;
////                    // displaying a toast message
////                    Toast.makeText((Context) MainActivity.this, "SAlut grasule", Toast.LENGTH_LONG).show();
////                    imageView1.setImageResource(myImageList[counterimagine%3]);
////
////
////                }
////            }));
////            button.setOnClickListener((View.OnClickListener) (new View.OnClickListener() {
////                public final void onClick(View it) {
////                    if(counterimagine>0)
////                        counterimagine-=1;
////
////                    // displaying a toast message
////                    Toast.makeText((Context) MainActivity.this, "SAlut grasule", Toast.LENGTH_LONG).show();
////                    imageView1.setImageResource(myImageList[counterimagine%3]);
////
////
////                }
////            }));
////        }
////
////    }
////}
//
//package com.example.myapplication;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.os.Handler;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//public class MainActivity extends AppCompatActivity {
//    int counterimagine = 0;
//    int[] myImageList = new int[]{R.drawable.poza1, R.drawable.dashboard, R.drawable.screenshot};
//    ImageView imageView1;
//    Handler handler = new Handler();
//    Runnable imageSwitcherRunnable;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        imageView1 = (ImageView) findViewById(R.id.imageView2);
//        imageView1.setImageResource(myImageList[0]); // Set initial image
//
//        // Create a Runnable to update the image every 2 seconds (2000 ms)
//        imageSwitcherRunnable = new Runnable() {
//            @Override
//            public void run() {
//                // Update the image resource
//                counterimagine++;
//                imageView1.setImageResource(myImageList[counterimagine % myImageList.length]);
//
//                // Display a toast message (optional)
//                Toast.makeText(MainActivity.this, "Image updated!", Toast.LENGTH_SHORT).show();
//
//                // Schedule the next image change
//                handler.postDelayed(this, 2000); // Run this every 2 seconds
//            }
//        };
//
//        // Start the image cycling when the activity is created
//        handler.post(imageSwitcherRunnable);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        // Stop the Runnable when the activity is destroyed to prevent memory leaks
//        handler.removeCallbacks(imageSwitcherRunnable);
//    }
//}


//////////////////// IMPLEMENTARE CU ASYNCTASK

package com.example.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText searchBar;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private ArrayList<Product> productList;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBar = findViewById(R.id.search_bar);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList, this::onProductClick);
        recyclerView.setAdapter(productAdapter);
        requestQueue = Volley.newRequestQueue(this);
    }

    public void searchProducts(View view) {
        String query = searchBar.getText().toString().trim();
        if (query.isEmpty()) {
            Toast.makeText(this, "Enter a product to search", Toast.LENGTH_SHORT).show();
            return;
        }
        fetchProducts(query);
    }

    private void fetchProducts(String query) {
        String url = "https://dummyjson.com/products/search?q=" + query;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray productsArray = response.getJSONArray("products");
                            productList.clear();
                            for (int i = 0; i < productsArray.length(); i++) {
                                JSONObject productObject = productsArray.getJSONObject(i);
                                Product product = new Product(
                                        productObject.getInt("id"),
                                        productObject.getString("title"),
                                        productObject.getString("thumbnail"),
                                        productObject.getString("description")
                                );
                                productList.add(product);
                            }
                            productAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(request);
    }

    private void onProductClick(Product product) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("title", product.getTitle());
        intent.putExtra("description", product.getDescription());
        intent.putExtra("thumbnail", product.getThumbnail());
        startActivity(intent);
    }
}
