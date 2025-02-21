package lk.javainstitute.tv_hub.models;

public class FeedbackModel {

    String feedback;
    String dateTime;
    String email;

    public FeedbackModel() {
    }
    public FeedbackModel(String feedback, String dateTime, String email) {
        this.feedback = feedback;
        this.dateTime = dateTime;
        this.email = email;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
