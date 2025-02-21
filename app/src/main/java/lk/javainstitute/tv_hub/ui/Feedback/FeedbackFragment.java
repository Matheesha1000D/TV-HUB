package lk.javainstitute.tv_hub.ui.Feedback;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import lk.javainstitute.tv_hub.NotificationRepository;
import lk.javainstitute.tv_hub.R;
import lk.javainstitute.tv_hub.adapters.FeedbackAdapter;
import lk.javainstitute.tv_hub.models.FeedbackModel;

public class FeedbackFragment extends Fragment {

    private static final String CHANNEL_ID = "feedback_channel";
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private RecyclerView recyclerView;
    private FeedbackAdapter feedbackAdapter;
    private ArrayList<FeedbackModel> feedbackList;
    private EditText feedbackEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotificationChannel();
        requestNotificationPermission();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Feedback Channel";
            String description = "Channel for feedback notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_feedback, container, false);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        feedbackEditText = root.findViewById(R.id.feedback);
        Button submitFeedbackButton = root.findViewById(R.id.submitFeedback);
        recyclerView = root.findViewById(R.id.recyclerView);

        feedbackList = new ArrayList<>();
        feedbackAdapter = new FeedbackAdapter(feedbackList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(feedbackAdapter);

        submitFeedbackButton.setOnClickListener(v -> {
            String feedbackText = feedbackEditText.getText().toString().trim();
            if (!feedbackText.isEmpty()) {
                saveFeedbackToFirebase(feedbackText);
            } else {
                Toast.makeText(getActivity(), "Please enter your feedback", Toast.LENGTH_SHORT).show();
            }
        });

        fetchFeedbackFromFirebase();

        return root;
    }

    private void saveFeedbackToFirebase(String feedbackText) {
        String email = auth.getCurrentUser().getEmail();
        String dateTime = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault()).format(new Date());

        FeedbackModel feedback = new FeedbackModel(feedbackText, dateTime, email);

        db.collection("Feedback")
                .add(feedback)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getActivity(), "Feedback submitted", Toast.LENGTH_SHORT).show();
                    feedbackEditText.setText(""); // Clear the EditText field
                    showNotification(feedbackText); // Show notification
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getActivity(), "Error submitting feedback", Toast.LENGTH_SHORT).show());
    }

    private void fetchFeedbackFromFirebase() {
        db.collection("Feedback")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        feedbackList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            FeedbackModel feedback = document.toObject(FeedbackModel.class);
                            feedbackList.add(feedback);
                        }
                        feedbackAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), "Error getting feedback", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showNotification(String feedbackText) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_tv_logo) // Replace with your app's icon
                .setContentTitle("New Feedback Submitted")
                .setContentText(feedbackText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(1, builder.build());
        }

        // Save notification to SQLite
        NotificationRepository notificationRepository = new NotificationRepository(getContext());
        String timestamp = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault()).format(new Date());
        notificationRepository.insertNotification(feedbackText, timestamp);
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }
    }
}