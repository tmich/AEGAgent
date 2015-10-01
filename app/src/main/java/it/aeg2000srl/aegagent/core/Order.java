package it.aeg2000srl.aegagent.core;

import java.util.Date;
import java.util.List;

/**
 * Created by Tiziano on 30/09/2015.
 */
public class Order {
    protected long id;
    protected Customer customer;
    protected long customer_id;
    protected Date creationDate;
    protected long userId;
    protected String notes;
    protected List<OrderItem> items;
    protected Date sentDate;

    public Order() {
        setCreationDate(new Date());
    }

    public Order(Customer customer) {
        this();
        this.customer = customer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void add(OrderItem it) {
        getItems().add(it);
    }

    public void remove(OrderItem it) {
        getItems().remove(it);
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        this.customer_id = customer.getId();
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public long getCustomerId() {
        return customer_id;
    }

    public void setCustomerId(long customer_id) {
        this.customer_id = customer_id;
    }

    @Override
    public String toString() {
        return getCustomer().getName() + " del " + getCreationDate() + "(x" + getItems().size() + ")";
    }
}
