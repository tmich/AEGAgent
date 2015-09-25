package it.aeg2000srl.aegagent.core;

/**
 * Created by tiziano.michelessi on 25/09/2015.
 */
public class Product {
    protected long id;
    protected String code;
    protected String name;
    protected double price;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
