package kh.com.camface.utilities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by DARAPENHCHET on 2/11/2017.
 */
public class Response implements Serializable {

    @JsonProperty("CODE")
    public String code;
    @JsonProperty("MESSAGE")
    public String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        switch (code) {
            case StatusCode.SUCCESS:
                message = "SUCCESSFULLY...";
                break;
            case StatusCode.NOT_SUCCESS:
                message = "NOT SUCCESSFULLY...";
                break;
            case StatusCode.NOT_FOUND:
                message = "NOT FOUND...";
            case StatusCode.PASSWORD_NOT_MATCH:
                message = "Password not match";
            case StatusCode.PRINTING_ERROR:
                message = "Printing error.";
            case StatusCode.TRANSACTION_OVERTIME:
                message = "Transaction is overtime.";
            default:
                message = "GO HOME YOU ARE DRUNK";
        }
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Response [code=" + code + ", message=" + message + "]";
    }

}
