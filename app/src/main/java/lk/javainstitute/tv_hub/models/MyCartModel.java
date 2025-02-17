package lk.javainstitute.tv_hub.models;

public class MyCartModel {
    String productName;
    String productPrice;
    String productImg;
    String productDate;
    String productTime;
    String productTotalQty;
    int productTotalPrice;
    String documentId;

    public MyCartModel() {
    }

    public MyCartModel(String productName, String productPrice, String productImg, String productDate, String productTime, String productTotalQty, int productTotalPrice, String documentId) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImg = productImg;
        this.productDate = productDate;
        this.productTime = productTime;
        this.productTotalQty = productTotalQty;
        this.productTotalPrice = productTotalPrice;
        this.documentId = documentId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public String getProductDate() {
        return productDate;
    }

    public void setProductDate(String productDate) {
        this.productDate = productDate;
    }

    public String getProductTime() {
        return productTime;
    }

    public void setProductTime(String productTime) {
        this.productTime = productTime;
    }

    public String getProductTotalQty() {
        return productTotalQty;
    }

    public void setProductTotalQty(String productTotalQty) {
        this.productTotalQty = productTotalQty;
    }

    public int getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(int productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
