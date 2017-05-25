package kh.com.camface.utilities;

import java.io.Serializable;

/**
 * Created by DARAPENHCHET on 2/11/2017.
 */
public class StatusCode implements Serializable {
    public static final String SUCCESS = "0000";
    public static final String NOT_SUCCESS = "9999";
    public static final String NOT_FOUND = "1111";
    public static final String PRINTING_ERROR = "7777";
    public static final String TRANSACTION_OVERTIME = "8888";
    public static final String PASSWORD_NOT_MATCH = "1100";
}
