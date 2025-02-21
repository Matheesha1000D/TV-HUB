package lk.javainstitute.tv_hub.ui.orderDetails;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import lk.javainstitute.tv_hub.R;
import lk.javainstitute.tv_hub.ui.paymentSuccessActivity;
import lk.payhere.androidsdk.PHConfigs;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHMainActivity;
import lk.payhere.androidsdk.model.InitRequest;

public class orderDetailsActivity extends AppCompatActivity {
    private static final int PAYHERE_REQUEST = 100;

    FirebaseFirestore firestore;
    FirebaseAuth auth;
    String CustomerfName;
    String CustomerlName;
    String CustomerAddress;
    String PhoneNumber;
    String city;
    String totalAmount;
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_order_details);
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
        Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
        return insets;
    });

    EditText customerFName = findViewById(R.id.et_customer_fname);
    EditText customerLName = findViewById(R.id.et_customer_lname);
    EditText customerAddress = findViewById(R.id.et_address);
    EditText phoneNumber = findViewById(R.id.et_phone1);
    EditText city = findViewById(R.id.et_city);

    // Retrieve totalAmount from the intent
    Intent intent = getIntent();
    String totalAmount = intent.getStringExtra("totalAmount");
    // Set totalAmount to tv_total_price TextView
    TextView tvTotalPrice = findViewById(R.id.tv_total_price);
    if (totalAmount != null) {
        tvTotalPrice.setText("Rs: " + totalAmount + ".00");
    } else {
        tvTotalPrice.setText("0.00");
    }

    Button btnPay = findViewById(R.id.btn_pay);
    btnPay.setOnClickListener(v -> {
        try {
            if (validateFields()) {
                InitRequest req = new InitRequest();
                req.setMerchantId("1221173");       // Merchant ID
                req.setCurrency("LKR");             // Currency code LKR/USD/GBP/EUR/AUD
                req.setAmount(Double.parseDouble(totalAmount)); // Final Amount to be charged
                req.setOrderId("230000123");        // Unique Reference ID
                req.setItemsDescription("Door bell wireless");  // Item description title
                req.setCustom1("This is the custom message 1");
                req.setCustom2("This is the custom message 2");
                req.getCustomer().setFirstName(customerFName.getText().toString());
                req.getCustomer().setLastName(customerLName.getText().toString());
                req.getCustomer().setEmail("");
                req.getCustomer().setPhone(phoneNumber.getText().toString());
                req.getCustomer().getAddress().setAddress(customerAddress.getText().toString());
                req.getCustomer().getAddress().setCity(city.getText().toString());
                req.getCustomer().getAddress().setCountry("Sri Lanka");

                Intent intent1 = new Intent(this, PHMainActivity.class);
                intent1.putExtra(PHConstants.INTENT_EXTRA_DATA, req);
                PHConfigs.setBaseUrl(PHConfigs.SANDBOX_URL);
                startActivityForResult(intent1, PAYHERE_REQUEST); // unique request ID e.g. "11001"
            }
        } catch (Exception e) {
            Log.i("orderDetailsActivity", "Error during payment process");
            Toast.makeText(this, "An error occurred: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    });
}

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == PAYHERE_REQUEST) {
        if (resultCode == RESULT_OK) {
            String status = data.getStringExtra("status");
            if (status != null) {
                if (status.equals("Success")) {
                    // Payment Success
                    Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show();

                    // Insert data into Firebase Firestore
                    Map<String, Object> orderData = new HashMap<>();
                    orderData.put("firstName", ((EditText) findViewById(R.id.et_customer_fname)).getText().toString());
                    orderData.put("lastName", ((EditText) findViewById(R.id.et_customer_lname)).getText().toString());
                    orderData.put("email", Objects.requireNonNull(auth.getCurrentUser()).getEmail());
                    orderData.put("phone", ((EditText) findViewById(R.id.et_phone1)).getText().toString());
                    orderData.put("address", ((EditText) findViewById(R.id.et_address)).getText().toString());
                    orderData.put("city", ((EditText) findViewById(R.id.et_city)).getText().toString());
                    orderData.put("totalAmount", totalAmount);
                    orderData.put("timestamp", FieldValue.serverTimestamp());

                    firestore.collection("order")
                            .add(orderData)
                            .addOnSuccessListener(documentReference -> {
                                Toast.makeText(this, "Order placed successfully", Toast.LENGTH_SHORT).show();

                                // Clear cart items
                                clearCartItems();

                                // Navigate to paymentSuccessActivity
                                Intent intent = new Intent(orderDetailsActivity.this, paymentSuccessActivity.class);
                                startActivity(intent);
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Error placing order", Toast.LENGTH_SHORT).show();
                            });

                } else if (status.equals("Error")) {
                    Toast.makeText(this, "Payment Error", Toast.LENGTH_SHORT).show();
                } else if (status.equals("Cancel")) {
                    Toast.makeText(this, "Payment Cancel", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}

private void clearCartItems() {
    // Logic to clear cart items from Firestore or local storage
    firestore.collection("cart")
            .document(auth.getCurrentUser().getUid())
            .delete()
            .addOnSuccessListener(aVoid -> Log.d("orderDetailsActivity", "Cart items cleared"))
            .addOnFailureListener(e -> Log.e("orderDetailsActivity", "Error clearing cart items", e));
}
    private boolean validateFields() {
        EditText customerFName = findViewById(R.id.et_customer_fname);
        EditText customerLName = findViewById(R.id.et_customer_lname);
        EditText customerAddress = findViewById(R.id.et_address);
        EditText phoneNumber = findViewById(R.id.et_phone1);
        EditText city = findViewById(R.id.et_city);

        if (customerFName.getText().toString().trim().isEmpty()) {
            customerFName.setError("First name is required");
            customerFName.requestFocus();
            return false;
        }

        if (customerLName.getText().toString().trim().isEmpty()) {
            customerLName.setError("Last name is required");
            customerLName.requestFocus();
            return false;
        }

        if (customerAddress.getText().toString().trim().isEmpty()) {
            customerAddress.setError("Address is required");
            customerAddress.requestFocus();
            return false;
        }

        if (city.getText().toString().trim().isEmpty()) {
            city.setError("City is required");
            city.requestFocus();
            return false;
        }

        if (phoneNumber.getText().toString().trim().isEmpty()) {
            phoneNumber.setError("Phone number is required");
            phoneNumber.requestFocus();
            return false;
        }

        return true;
    }
}