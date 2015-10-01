package it.aeg2000srl.aegagent.core;

/**
 * Created by Tiziano on 30/09/2015.
 */
public class OrderItem {
    protected long id;
    protected int qty;
    protected long product_id;
    protected long order_id;
    protected Product product;
    protected String notes;
    protected String discount;

    public OrderItem() {}

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

    @Override
    public String toString() {
        return getQty() + "x " + getProduct().getName();
    }

    public long getProductId() {
        return product_id;
    }

    public void setProductId(long product_id) {
        this.product_id = product_id;
    }

    public long getOrderId() {
        return order_id;
    }

    public void setOrderId(long order_id) {
        this.order_id = order_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
