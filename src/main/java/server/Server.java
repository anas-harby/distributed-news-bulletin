package server;

public interface Server {
    class ServerException extends Exception {
        public ServerException(Exception e) {
            super(e.getMessage(), e.getCause());
        }
    }
    void listen() throws ServerException;
    void accept() throws ServerException;
    void terminate() throws ServerException;
}
