package vn.danghung.dev.dao;

import vn.danghung.dev.entities.Doc;
import java.util.List;
public interface DocumentDao {
    List<Doc> findAll();
    Doc findById(int id);

    void indexing();

    void create(String title, String about, String link);
}
