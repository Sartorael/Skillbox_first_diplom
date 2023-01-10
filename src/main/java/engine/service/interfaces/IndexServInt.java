package engine.service.interfaces;

import engine.operations.Index;
import engine.service.IndexingServ;
import engine.service.response.FalseRespServ;
import engine.service.response.RespServ;
import engine.service.response.TrueRespServ;
import org.apache.commons.logging.*;
import org.springframework.stereotype.Service;

@Service
public class IndexServInt implements IndexingServ {

    private final Index index;

    private static final Log log = LogFactory.getLog(IndexServInt.class);

    public IndexServInt(Index index) {
        this.index = index;
    }

    @Override
    public RespServ startIndexingAll() {
        RespServ response;
        boolean indexing;
        try {
            indexing = index.fullSiteIndexing();
            log.info("Попытка запуска индексации всех сайтов");
        } catch (InterruptedException e) {
            response = new FalseRespServ("Ошибка запуска индексации");
            log.error("Ошибка запуска индексации", e);
            return response;
        }
        if (indexing) {
            response = new TrueRespServ();
            log.info("Индексация всех сайтов запущена");
        } else {
            response = new FalseRespServ("Индексация уже запущена");
            log.warn("Индексация всех сайтов не запущена. Т.к. процесс индексации был запущен ранее.");
        }
        return response;
    }

    @Override
    public RespServ stopIndexing() {
        boolean indexing = index.stopIndexing();
        log.info("Попытка остановки индексации");
        RespServ response;
        if (indexing) {
            response = new TrueRespServ();
            log.info("Индексация остановлена");
        } else {
            response = new FalseRespServ("Индексация не запущена");
            log.warn("Остановка индексации не может быть выполнена, потому что процесс индексации не запущен.");
        }
        return response;
    }

    @Override
    public RespServ startIndexingOne(String url) {
        RespServ resp;
        String response;
        try {
            response = index.checkForIndex(url);
        } catch (InterruptedException e) {
            resp = new FalseRespServ("Ошибка запуска индексации");
            return resp;
        }

        if (response.equals("not found")) {
            resp = new FalseRespServ("Страница находится за пределами сайтов," +
                    " указанных в конфигурационном файле");
        }
        else if (response.equals("false")) {
            resp = new FalseRespServ("Индексация страницы уже запущена");
        }
        else {
            resp = new TrueRespServ();
        }
        return resp;
    }
}
