package lk.javainstitute.tv_hub.ui.Cart;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lk.javainstitute.tv_hub.Navigaton;
import lk.javainstitute.tv_hub.R;
import lk.javainstitute.tv_hub.StartActivity;
import lk.javainstitute.tv_hub.adapters.MyCartAdapter;
import lk.javainstitute.tv_hub.models.MyCartModel;
import lk.javainstitute.tv_hub.ui.Home.HomeFragment;
import lk.javainstitute.tv_hub.ui.orderDetails.orderDetailsActivity;
import lk.payhere.androidsdk.PHConfigs;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHMainActivity;
import lk.payhere.androidsdk.PHResponse;
import lk.payhere.androidsdk.model.InitRequest;
import lk.payhere.androidsdk.model.Item;
import lk.payhere.androidsdk.model.StatusResponse;

public class CartFragment extends Fragment {

    private static final String TAG = "CartFragment";
    private TextView textView;

    String totalAmount;

    FirebaseFirestore db;
    FirebaseAuth auth;
    RecyclerView recyclerView;
    MyCartAdapter cartAdapter;

    TextView overTotalAmount;
    ImageView emptyViewImage;

    List<MyCartModel> cartModelList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

// In CartFragment.java

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_cart, container, false);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        recyclerView = root.findViewById(R.id.cart_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        overTotalAmount = root.findViewById(R.id.tv_total_price); // Updated ID
        emptyViewImage = root.findViewById(R.id.empty_view_image);
        LocalBroadcastManager.getInstance(getActivity())
                .registerReceiver(mMessageReceiver, new IntentFilter("MyTotalAmount"));

        cartModelList = new ArrayList<>();
        cartAdapter = new MyCartAdapter(getActivity(), cartModelList);
        recyclerView.setAdapter(cartAdapter);

        if (auth.getCurrentUser() != null) {
            db.collection("AddToCart").document(auth.getCurrentUser().getUid())
                    .collection("CurrentUser")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                    MyCartModel cartModel = documentSnapshot.toObject(MyCartModel.class);
                                    cartModel.setDocumentId(documentSnapshot.getId());
                                    cartModelList.add(cartModel);
                                    cartAdapter.notifyDataSetChanged();
                                }
                                toggleEmptyView();
                            } else {
                                Log.e(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        } else {
            Log.e(TAG, "User not authenticated");
        }

        // Add ItemTouchHelper for swipe-to-delete functionality
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                // Remove item from Firestore
                db.collection("AddToCart")
                        .document(auth.getCurrentUser().getUid())
                        .collection("CurrentUser")
                        .document(cartModelList.get(position).getDocumentId())
                        .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Remove item from the list and notify adapter
                                    cartModelList.remove(position);
                                    cartAdapter.notifyItemRemoved(position);
                                    Toast.makeText(getActivity(), "Item removed from cart", Toast.LENGTH_LONG).show();
                                    toggleEmptyView();
                                } else {
                                    Log.e(TAG, "Error deleting document: ", task.getException());
                                }
                            }
                        });
            }
        }).attachToRecyclerView(recyclerView);

        Button process = root.findViewById(R.id.process);
        process.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), orderDetailsActivity.class);
            intent.putExtra("totalAmount", totalAmount);
            startActivity(intent);
        });

        return root;
    }

    private void toggleEmptyView() {
        TextView emptyViewText = getView().findViewById(R.id.textView16);
        TextView emptyViewText1 = getView().findViewById(R.id.tv_total_price); // Updated ID
        Button process = getView().findViewById(R.id.process);
        Button button = getView().findViewById(R.id.shop_Now);

        if (cartModelList.isEmpty()) {
            emptyViewImage.setVisibility(View.VISIBLE);
            button.setVisibility(View.VISIBLE);

            button.setOnClickListener(view -> {
                Intent i = new Intent(CartFragment.this.getActivity(), Navigaton.class);
                startActivity(i);
            });

            recyclerView.setVisibility(View.GONE);
            emptyViewText.setVisibility(View.GONE);
            process.setVisibility(View.GONE);
            emptyViewText1.setVisibility(View.GONE);
        } else {
            emptyViewText.setVisibility(View.VISIBLE);
            process.setVisibility(View.VISIBLE);
            emptyViewImage.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            emptyViewText1.setVisibility(View.VISIBLE);
            button.setVisibility(View.GONE);
        }
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int totalAmount1 = intent.getIntExtra("totalAmount", 0);
            totalAmount = String.valueOf(totalAmount1);
            overTotalAmount.setText(String.valueOf("Rs: " + totalAmount1) + ".00");
        }
    };
}