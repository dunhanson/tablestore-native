package com.bidizhaobiao.tablestore.nat.exception;

/**
 * 类描述
 *
 * @author dunhanson
 * @version 1.0.0
 * @since 2023-09-26
 */
public class TablestoreException extends RuntimeException{
    public TablestoreException(String message) {
        super(message);
    }

    public TablestoreException(Exception e) {
        super(e.getMessage());
    }
}
