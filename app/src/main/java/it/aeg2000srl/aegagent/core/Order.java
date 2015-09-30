package it.aeg2000srl.aegagent.core;

import java.util.Date;
import java.util.List;

/**
 * Created by Tiziano on 30/09/2015.
 */
public class Order {
    protected long id;
    protected Customer customer;
    protected Date creationDate;
    protected long userId;
    protected List<OrderItem> items;

    public Order(Customer customer) {
        this.customer = customer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void add(OrderItem it) {
        items.add(it);
    }

    public void remove(OrderItem it) {
        items.remove(it);
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
    }
}
