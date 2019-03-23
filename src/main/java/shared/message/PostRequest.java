package shared.message;

public class PostRequest extends Request {

    private int data;

    public PostRequest(int clientID, int data) {
        super(clientID);
        this.data = data;
    }

    public Type getType() {
        return Type.POST;
    }

    public int getData() {
        return data;
    }
}
