package shared.message;

public class PostRequest extends Request {

    private int data;

    public PostRequest(int data) {
        this.data = data;
    }

    public Type getType() {
        return Type.POST;
    }

    public int getData() {
        return data;
    }
}
