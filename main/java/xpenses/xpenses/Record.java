package xpenses.xpenses;

import java.util.Date;

/**
 * Created by HP on 17-05-2017.
 */

public class Record {
    private int id;
    private Date date;
    private float amount;
    private String paymentMode;
    private String comments;

    public Record() {
    }

    public Record(int id, Date date, float amount, String paymentMode, String comments) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.paymentMode = paymentMode;
        this.comments = comments;
    }

    public Record(Date date, float amount, String paymentMode, String comments) {
        this.date = date;
        this.amount = amount;
        this.paymentMode = paymentMode;
        this.comments = comments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
