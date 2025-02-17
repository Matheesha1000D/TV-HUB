package lk.javainstitute.tv_hub.ui.Details;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import lk.javainstitute.tv_hub.R;
import lk.javainstitute.tv_hub.models.AllProductModel;
import com.google.firebase.auth.FirebaseAuth;

public class DetailedActivity extends AppCompatActivity {
    ImageView detailsImage;
    TextView price, qty, title, description, addqty;
    Button btnAddToCart;
    ImageView addItem, removeItem;
    AllProductModel allProductModel = null;
    int totalQuantity = 1;
    int totalPrice = 0;

    FirebaseFirestore firestore;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detailed);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_details), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final Object object = getIntent().getSerializableExtra("detail");
        if (object instanceof AllProductModel) {
            allProductModel = (AllProductModel) object;
        }

        detailsImage = findViewById(R.id.details_img);
        price = findViewById(R.id.details_price);
        qty = findViewById(R.id.details_qty);
        addqty = findViewById(R.id.addqty);
        title = findViewById(R.id.details_name);
        description = findViewById(R.id.details_description);
        btnAddToCart = findViewById(R.id.addtocart);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        if (allProductModel != null) {
            Glide.with(getApplicationContext()).load(allProductModel.getImg_url()).into(detailsImage);
            title.setText(allProductModel.getName());
            price.setText("Rs. " + allProductModel.getPrice() + ".00");
            qty.setText(allProductModel.getQty());
            description.setText(allProductModel.getDescription());

            totalPrice = Integer.parseInt(allProductModel.getPrice()) * totalQuantity;
        }

        addItem = findViewById(R.id.add_item);
        removeItem = findViewById(R.id.remove_item);

        addItem.setOnClickListener(v -> {
            int quantity = Integer.parseInt(addqty.getText().toString());
            if (quantity < Integer.parseInt(allProductModel.getQty())) {
                addqty.setText(String.valueOf(quantity + 1));
                totalQuantity = quantity + 1;
                totalPrice = Integer.parseInt(allProductModel.getPrice()) * totalQuantity;
            }
        });
        removeItem.setOnClickListener(v -> {
            int quantity = Integer.parseInt(addqty.getText().toString());
            if (quantity > 1) {
                addqty.setText(String.valueOf(quantity - 1));
                totalQuantity = quantity - 1;
                totalPrice = Integer.parseInt(allProductModel.getPrice()) * totalQuantity;
            }
        });

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addedtoCart();
            }
        });
    }

    private void addedtoCart() {
        String saveCurrentDate, saveCurruntTime;
        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurruntTime = currentTime.format(calForDate.getTime());

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("productName", allProductModel.getName());
        cartMap.put("productPrice", allProductModel.getPrice());
        cartMap.put("productDate", saveCurrentDate);
        cartMap.put("productTime", saveCurruntTime);
        cartMap.put("productTotalPrice", totalPrice);
        cartMap.put("productTotalQty", addqty.getText().toString());
        cartMap.put("productImg", allProductModel.getImg_url());
        cartMap.put("productDescription", allProductModel.getDescription());

        firestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("CurrentUser").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(DetailedActivity.this, "Added to Cart", Toast.LENGTH_LONG).show();
                    }
                });
    }
}