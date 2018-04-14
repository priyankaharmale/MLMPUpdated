package hnwebproject.com.mlmp.Model;

import java.io.Serializable;

/**
 * Created by hnwebmarketing on 1/27/2018.
 */

public class ProductModel implements Serializable {



    private String product_name, price, product_image, product_id,product_category,copy_version,grade,product_status, product_count,description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductModel() {

    }

    public String getProduct_count() {
        return product_count;
    }

    public void setProduct_count(String product_count) {
        this.product_count = product_count;
    }

    public ProductModel(String product_name, String pPrice, String pImage, String product_id) {
        this.product_name = product_name;
        this.price = pPrice;
        this.product_image = pImage;
        this.product_id = product_id;
    }

    public String getProduct_category() {
        return product_category;
    }

    public void setProduct_category(String product_category) {
        this.product_category = product_category;
    }

    public String getCopy_version() {
        return copy_version;
    }

    public void setCopy_version(String copy_version) {
        this.copy_version = copy_version;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getProduct_status() {
        return product_status;
    }

    public void setProduct_status(String product_status) {
        this.product_status = product_status;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
}
