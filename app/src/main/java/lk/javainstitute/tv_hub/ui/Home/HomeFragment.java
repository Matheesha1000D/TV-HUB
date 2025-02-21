package lk.javainstitute.tv_hub.ui.Home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.StringValue;

import java.util.ArrayList;
import java.util.List;

import lk.javainstitute.tv_hub.R;
import lk.javainstitute.tv_hub.adapters.AllproductAdapter;
import lk.javainstitute.tv_hub.adapters.CategoryAdapter;
import lk.javainstitute.tv_hub.models.AllProductModel;
import lk.javainstitute.tv_hub.models.HomeCategory;

public class HomeFragment extends Fragment {

    RecyclerView categoryRecyclerView, allProductRecyclerView;
    FirebaseFirestore db;

    //category list
    List<HomeCategory> categoryList;
    CategoryAdapter categoryAdapter;

    //home product list
    List<AllProductModel> allProductModelList;
    AllproductAdapter allproductAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        db = FirebaseFirestore.getInstance();

        categoryRecyclerView = root.findViewById(R.id.category_rec);
        allProductRecyclerView = root.findViewById(R.id.product_rec);


        //category list
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getActivity(), categoryList);
        categoryRecyclerView.setAdapter(categoryAdapter);

        db.collection("HomeCategory")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                HomeCategory homeCategory = document.toObject(HomeCategory.class);
                                categoryList.add(homeCategory);
                            }
                            categoryAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getActivity(), "Error fetching data: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //Product list
        allProductRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        allProductModelList = new ArrayList<>();
        AllproductAdapter allproductAdapter = new AllproductAdapter(getActivity(), allProductModelList);
        allProductRecyclerView.setAdapter(allproductAdapter);


        db.collection("Products")
                .whereEqualTo("status", "1")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                              Log.i("Document", String.valueOf(document));
                                AllProductModel allProductModel = document.toObject(AllProductModel.class);
                                allProductModelList.add(allProductModel);
                            }
                            allproductAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getActivity(), "Error fetching data: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        return root;
    }
}