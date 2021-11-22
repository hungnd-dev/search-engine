package vn.danghung.dev.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.danghung.dev.dao.DocumentDao;
import vn.danghung.dev.search.SearchUtils;

@Service
public class DocServiceImpl implements DocService {
    @Autowired
    private DocumentDao documentDao;

    private SearchUtils searchUtils = SearchUtils.getInstance();

    @Override
    public Object getDoc() {
        return documentDao.findAll();
    }

    @Override
    public Object getDocById(int id) {
        return documentDao.findById(id);
    }

    @Override
    public void indexing() {
        documentDao.indexing();
    }

    @Override
    public Object searching(String searchKey, int paging) {
        return searchUtils.search(searchKey,paging);
    }

    @Override
    public Object getIndexDocument(int id) {
        return searchUtils.getIndexedDocument(id);
    }

    @Override
    public Object getTfIdf() {
        return searchUtils.getTfIdfValue();
    }
}
