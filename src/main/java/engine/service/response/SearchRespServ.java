package engine.service.response;

import engine.service.searchResponseEntity.SearchData;

public class SearchRespServ implements RespServ {
    private boolean result;
    private int count;
    private SearchData[] data;

    public SearchRespServ() {
    }

    public SearchRespServ(boolean result) {
        this.result = result;
    }

    public SearchRespServ(boolean result, int count, SearchData[] data) {
        this.result = result;
        this.count = count;
        this.data = data;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public SearchData[] getData() {
        return data;
    }

    public void setData(SearchData[] data) {
        this.data = data;
    }
}
