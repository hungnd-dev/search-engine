package vn.danghung.dev.model;
import java.util.List;
public class SearchResponse {
    private int total;
    private List<DocResponse> document;

    public SearchResponse() {
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DocResponse> getDocument() {
        return document;
    }

    public void setDocument(List<DocResponse> document) {
        this.document = document;
    }
}
