package shared.message;

public class GetRequest extends Request {

    public GetRequest(int clientID) {
        super(clientID);
    }

    public Type getType() {
        return Type.GET;
    }
}
