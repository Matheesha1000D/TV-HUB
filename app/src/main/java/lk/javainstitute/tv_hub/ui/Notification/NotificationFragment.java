package lk.javainstitute.tv_hub.ui.Notification;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import lk.javainstitute.tv_hub.NotificationRepository;
import lk.javainstitute.tv_hub.R;
import lk.javainstitute.tv_hub.adapters.NotificationAdapter;

public class NotificationFragment extends Fragment {
    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_notification, container, false);

        recyclerView = root.findViewById(R.id.recyclerViewNotification);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        NotificationRepository notificationRepository = new NotificationRepository(getContext());
        List<String> notifications = notificationRepository.getAllNotifications();
        notificationAdapter = new NotificationAdapter(notifications);
        recyclerView.setAdapter(notificationAdapter);

        return root;
    }
}