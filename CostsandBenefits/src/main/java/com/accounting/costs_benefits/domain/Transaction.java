package com.accounting.costs_benefits.domain;

import java.util.Date;

/**
 * Created by carlos on 9/06/13.
 */
public class Transaction {
    private String title;
    private final Date timestamp;
    private Double quantity;
    private String description;

    protected Transaction(String title, Long timestamp, double quantity, String description) {
        this.title = title;

        this.timestamp = new Date(timestamp);
        this.quantity = quantity;
        this.description = description;
    }

    protected Transaction(){
        this.timestamp = new Date();
    }

    protected Transaction(Transaction tr){
        this(tr.getTitle(), tr.getTimestamp(), tr.getQuantity(), tr.getDescription());
    }

    public String getId(){
        return timestamp.getTime()+"."+quantity;
    }

    public String getTitle() {
        return title;
    }

    public Long getTimestamp() {
        return timestamp.getTime();
    }

    public double getQuantity() {
        return quantity;
    }

    public String getDescription() {
        return description;
    }

    protected void setDescription(String desc){
        this.description = desc;
    }

    protected void setTitle(String title){
        this.title = title;
    }

    protected void setTimestamp(Long time){
        this.timestamp.setTime(time);
    }

    protected void setQuantity(Double qty){
        this.quantity = qty;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "title='" + title + '\'' +
                ", timestamp=" + timestamp +
                ", quantity=" + quantity +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (Double.compare(that.quantity, quantity) != 0) return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        if (!timestamp.equals(that.timestamp)) return false;
        if (!title.equals(that.title)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = title.hashCode();
        result = 31 * result + timestamp.hashCode();
        temp = Double.doubleToLongBits(quantity);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
