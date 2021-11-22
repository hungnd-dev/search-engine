package vn.danghung.dev.adapter;

import org.springframework.stereotype.Component;
import vn.danghung.dev.entities.Doc;
import vn.danghung.dev.model.DocResponse;

@Component("docAdapter")
public class DocAdapter implements EntityAdapter<Doc, DocResponse> {
    @Override
    public DocResponse transform(Doc doc) {
        DocResponse docResponse = new DocResponse();
        docResponse.setId(doc.getDocumentId());
        docResponse.setLink(doc.getLink());
        docResponse.setTitle(doc.getTitle());
        docResponse.setAbout(doc.getAbout());
        return docResponse;
    }
}
