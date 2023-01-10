package engine.service;

import engine.service.response.RespServ;

public interface IndexingServ {
    RespServ startIndexingAll();
    RespServ stopIndexing();
    RespServ startIndexingOne(String url);
}
