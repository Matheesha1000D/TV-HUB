package lk.javainstitute.tv_hub.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import lk.javainstitute.tv_hub.R;
import lk.javainstitute.tv_hub.models.FeedbackModel;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {

    private ArrayList<FeedbackModel> feedbackList;

    public FeedbackAdapter(ArrayList<FeedbackModel> feedbackList) {
        this.feedbackList = feedbackList;
    }

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_view, parent, false);
        return new FeedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position) {
        FeedbackModel feedback = feedbackList.get(position);
        holder.feedbackTextView.setText(feedback.getFeedback());
        holder.dateTimeTextView.setText(feedback.getDateTime());
        holder.emailTextView.setText(feedback.getEmail());
    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    public static class FeedbackViewHolder extends RecyclerView.ViewHolder {
        TextView feedbackTextView, dateTimeTextView, emailTextView;

        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);
            feedbackTextView = itemView.findViewById(R.id.textView20);
            dateTimeTextView = itemView.findViewById(R.id.textView22);
            emailTextView = itemView.findViewById(R.id.textView21);
        }
    }
}