package lk.javainstitute.tv_hub.ui.updateUser;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import lk.javainstitute.tv_hub.LogoActivity;
import lk.javainstitute.tv_hub.R;
import lk.javainstitute.tv_hub.SignUpActivity;
import lk.javainstitute.tv_hub.ui.MyAccount.AccountFragment;

public class UpdateUserFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_user, container, false);

        ImageView buttonNavigateToUpdateUser = view.findViewById(R.id.back2);
        buttonNavigateToUpdateUser.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_nav_updateuser_to_nav_myaccount)
        );
        return view;

    }
}