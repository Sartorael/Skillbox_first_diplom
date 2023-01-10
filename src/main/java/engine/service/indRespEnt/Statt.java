package engine.service.indRespEnt;

public class Statt {
    entityConstructor entityConstructor;
    Detail[] detail;

    public Statt(entityConstructor entityConstructor, Detail[] detail) {
        this.entityConstructor = entityConstructor;
        this.detail = detail;
    }

    public entityConstructor getTotal() {
        return entityConstructor;
    }

    public void setTotal(entityConstructor entityConstructor) {
        this.entityConstructor = entityConstructor;
    }

    public Detail[] getDetailed() {
        return detail;
    }

    public void setDetailed(Detail[] detail) {
        this.detail = detail;
    }
}
