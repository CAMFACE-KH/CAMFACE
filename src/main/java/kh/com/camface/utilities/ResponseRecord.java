package kh.com.camface.utilities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by DARAPENHCHET on 2/11/2017.
 */
public class ResponseRecord<T> extends Response implements Serializable {

    @JsonProperty("DATA")
    public T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseRecord [data=" + data + "]";
    }
}
