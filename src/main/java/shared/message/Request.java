package shared.message;

import java.io.Serializable;

public abstract class Request implements Serializable {

    private int clientID;

    public enum Type {
        GET,
        POST
    }

    public Request(int clientID) {
        this.clientID = clientID;
    }

    public int getClientID() {
        return clientID;
    }

    public abstract Type getType();
}