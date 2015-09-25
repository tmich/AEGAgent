package it.aeg2000srl.aegagent.core;

/**
 * Created by tiziano.michelessi on 24/09/2015.
 */
public class Customer {
    protected long id;
    protected String code;
    protected String name;
    protected String address;
    protected String cap;
    protected String city;
    protected String prov;
    protected String telephone;
    protected String vatNumber;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }

    public String getProv() { return prov; }

    public void setProv(String prov) { this.prov = prov; }
}
