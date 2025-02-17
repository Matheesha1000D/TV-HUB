package lk.javainstitute.tv_hub.models;

import java.io.Serializable;

public class AllProductModel implements Serializable {
    String img_url;
    String name;
    String price;
    String qty;
    String description;

    public AllProductModel(){

    }

    public AllProductModel(String img_url, String name, String price, String qty, String description) {
        this.img_url = img_url;
        this.name = name;
        this.price = price;
        this.qty = qty;
        this.description = description;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
