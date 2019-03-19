package message;

import java.io.Serializable;

public class Response implements Serializable {

    private int data;

    public Response(int data) {
        this.data = data;
    }

    public int getData() {
        return data;
    }
}
