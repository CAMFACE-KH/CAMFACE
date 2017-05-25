package kh.com.camface.utilities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DARAPENHCHET on 2/11/2017.
 */
public class ResponseList<T> extends Response implements Serializable {

    @JsonProperty("DATA")
    public List<T> data;

    @JsonProperty("PAGINATION")
    public Pagination pagination;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

}
