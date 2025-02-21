package lk.javainstitute.tv_hub.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lk.javainstitute.tv_hub.R;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<String> notifications;

    public NotificationAdapter(List<String> notifications) {
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String notification = notifications.get(position);
        String[] parts = notification.split(" - ");
        holder.notificationText.setText(parts[0]);
        holder.notificationTimestamp.setText(parts[1]);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView notificationText;
        public TextView notificationTimestamp;

        public ViewHolder(View view) {
            super(view);
            notificationText = view.findViewById(R.id.notification_text);
            notificationTimestamp = view.findViewById(R.id.notification_timestamp);
        }
    }
}