package hnwebproject.com.mlmp.Model;

/**
 * Created by hnwebmarketing on 1/29/2018.
 */

public class ContactModel {

    private String contact_id,contact_name,contact_image;

    public ContactModel() {

    }

    public ContactModel(String contact_id, String contact_name, String contact_image) {
        this.contact_id = contact_id;
        this.contact_name = contact_name;
        this.contact_image = contact_image;
    }

    public String getContact_id() {
        return contact_id;
    }

    public void setContact_id(String contact_id) {
        this.contact_id = contact_id;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_image() {
        return contact_image;
    }

    public void setContact_image(String contact_image) {
        this.contact_image = contact_image;
    }
}
