package vn.danghung.dev.service;

public interface DocService {
    Object getDoc();

    Object getDocById(int id);

    void indexing();

    Object searching(String searchKey, int paging);

    Object getIndexDocument(int id);

    Object getTfIdf();
}
