package engine.service.response;

public class FalseRespServ implements RespServ {
    private final String error;

    public FalseRespServ(String error) {
        this.error = error;
    }

    @Override
    public boolean getResult() {
        return false;
    }

    public String getError() {
        return error;
    }
}
