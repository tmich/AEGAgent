package it.aeg2000srl.aegagent.core;

/**
 * Created by Tiziano on 30/09/2015.
 */
public class OrderItem {
    protected int qty;
    protected Product product;
    protected String notes;
    protected String discount;

    public OrderItem(Product prod) {
        this(prod, 1);
    }

    public OrderItem(Product prod, int qty) {
        this.product = prod;
        this.qty = qty;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
