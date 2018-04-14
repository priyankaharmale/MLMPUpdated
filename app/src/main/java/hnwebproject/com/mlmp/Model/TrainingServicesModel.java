package hnwebproject.com.mlmp.Model;

import java.io.Serializable;

/**
 * Created by neha on 4/4/2018..
 */

public class TrainingServicesModel implements Serializable{
    String
            ser_dis_id,
            user_id,
            service_id,
            event_name,
            company,
            location,
            address,
            date,
            amount_of_time,
            audience,
            size_of_audience,
            topic,
            budget,
            gps_training,
            pick_tranning,
            created_date;

    public String getSer_dis_id() {
        return ser_dis_id;
    }

    public void setSer_dis_id(String ser_dis_id) {
        this.ser_dis_id = ser_dis_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount_of_time() {
        return amount_of_time;
    }

    public void setAmount_of_time(String amount_of_time) {
        this.amount_of_time = amount_of_time;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public String getSize_of_audience() {
        return size_of_audience;
    }

    public void setSize_of_audience(String size_of_audience) {
        this.size_of_audience = size_of_audience;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getGps_training() {
        return gps_training;
    }

    public void setGps_training(String gps_training) {
        this.gps_training = gps_training;
    }

    public String getPick_tranning() {
        return pick_tranning;
    }

    public void setPick_tranning(String pick_tranning) {
        this.pick_tranning = pick_tranning;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }
}
