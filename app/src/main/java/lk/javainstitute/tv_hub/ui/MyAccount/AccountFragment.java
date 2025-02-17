package lk.javainstitute.tv_hub.ui.MyAccount;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import lk.javainstitute.tv_hub.Navigaton;
import lk.javainstitute.tv_hub.R;
import lk.javainstitute.tv_hub.ui.updateUser.UpdateUserFragment;

public class AccountFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        Button buttonNavigateToUpdateUser = view.findViewById(R.id.buttonAccUpdate);
        buttonNavigateToUpdateUser.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_nav_myaccount_to_nav_updateuser)
        );


        return view;

    }
}