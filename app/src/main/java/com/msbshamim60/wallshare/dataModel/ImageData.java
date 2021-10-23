package com.msbshamim60.wallshare.dataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class ImageData {

    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("status")
    @Expose
    private Long status;


    public ImageData() {
    }

    public ImageData(Data data, Boolean success, Long status) {
        super();
        this.data = data;
        this.success = success;
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public ImageData withData(Data data) {
        this.data = data;
        return this;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public ImageData withSuccess(Boolean success) {
        this.success = success;
        return this;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public ImageData withStatus(Long status) {
        this.status = status;
        return this;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result * 31) + ((this.data == null) ? 0 : this.data.hashCode()));
        result = ((result * 31) + ((this.success == null) ? 0 : this.success.hashCode()));
        result = ((result * 31) + ((this.status == null) ? 0 : this.status.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ImageData) == false) {
            return false;
        }
        ImageData rhs = ((ImageData) other);
        return Objects.equals(this.data, rhs.data) && Objects.equals(this.success, rhs.success) && this.status.equals(rhs.status);
    }

}
