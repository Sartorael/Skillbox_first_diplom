package engine.models;

import engine.operations.Morph;

import java.util.*;


public class Request {

    private String req;
    private List<String> reqLemmas;

    public List<String> getReqLemmas() {
        return reqLemmas;
    }

    public String getReq() {
        return req;
    }

    public Request(String req){
        this.req = req;
        reqLemmas = new ArrayList<>();
        try {
            Morph analyzer = new Morph();
            reqLemmas.addAll(analyzer.getLemmas(req));
        }catch (Exception e) {
            System.out.println("ошибка морфологочиского анализа");
        }
    }
}
