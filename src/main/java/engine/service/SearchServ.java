package engine.service;

import engine.models.Request;
import engine.service.response.RespServ;

import java.io.IOException;

public interface SearchServ {
    RespServ getResponse (Request request, String url, int offset, int limit) throws IOException;
}
