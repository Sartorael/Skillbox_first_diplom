package engine.service.response;


import engine.service.indRespEnt.Statt;

public class StatRespServ implements RespServ {
    boolean result;
    Statt statt;

    public StatRespServ(boolean result, Statt statt) {
        this.result = result;
        this.statt = statt;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public Statt getStatistics() {
        return statt;
    }

    public void setStatistics(Statt statt) {
        this.statt = statt;
    }
}
