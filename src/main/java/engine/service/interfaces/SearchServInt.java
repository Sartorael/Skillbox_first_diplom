package engine.service.interfaces;

import engine.operations.Search;
import engine.models.Request;
import engine.service.SearchServ;
import engine.service.response.FalseRespServ;
import engine.service.response.RespServ;
import org.apache.commons.logging.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SearchServInt implements SearchServ {

    private static final Log log = LogFactory.getLog(SearchServInt.class);

    private final Search search;

    public SearchServInt(Search search) {
        this.search = search;
    }

    RespServ response;

    @Override
    public RespServ getResponse(Request request, String url, int offset, int limit) throws IOException {
        log.info("Запрос на поиск строки- \"" + request.getReq() + "\"");
        if (request.getReq().equals("")){
            response = new FalseRespServ("Задан пустой поисковый запрос");
            log.warn("Задан пустой поисковый запрос");
            return response;
            }
        if(url.equals("")) {
            response = search.searchService(request, null, offset, limit);
        } else {
            response = search.searchService(request, url, offset, limit);
        }
        if (response.getResult()) {
            log.info("Запрос на поиск строки обработан, результат получен.");
            return response;
        } else {
            log.warn("Запрос на поиск строки обработан, указанная страница не найдена.");
            return new FalseRespServ("Указанная страница не найдена");
        }
    }
}
