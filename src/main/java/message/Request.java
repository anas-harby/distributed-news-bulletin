package message;

import java.io.Serializable;

public abstract class Request implements Serializable {
    public enum Type {
        GET,
        POST
    }

    public abstract Type getType();
}