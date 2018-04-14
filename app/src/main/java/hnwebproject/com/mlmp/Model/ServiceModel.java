package hnwebproject.com.mlmp.Model;

/**
 * Created by hnwebmarketing on 1/30/2018.
 */

public class ServiceModel {
 private String service_id,services,parent_id;

    public ServiceModel() {
    }

    public ServiceModel(String service_id, String services, String parent_id) {
        this.service_id = service_id;
        this.services = services;
        this.parent_id = parent_id;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }
}
