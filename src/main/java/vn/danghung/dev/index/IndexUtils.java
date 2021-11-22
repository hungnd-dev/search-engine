package vn.danghung.dev.index;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import vn.danghung.dev.analyzer.vi.VietnameseAnalyzer;
import org.apache.lucene.document.Document;
import vn.danghung.dev.global.ConfigInfo;

import java.io.IOException;
import java.nio.file.Paths;
public class IndexUtils {
    private  static IndexUtils indexUtilsInstance = null;
    static String indexDir = ConfigInfo.INDEX_DIR;
    private static IndexWriter writer = null;
    private IndexUtils() {
        Directory fsDirectory;
        try {
            fsDirectory = FSDirectory.open(Paths.get(indexDir));
            IndexWriterConfig conf = new IndexWriterConfig(new VietnameseAnalyzer());
            conf.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            writer = new IndexWriter(fsDirectory, conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static IndexUtils getInstance(){
        if(indexUtilsInstance == null){
            indexUtilsInstance = new IndexUtils();
        }
        return indexUtilsInstance;
    }

    public static IndexWriter getWriterInstance(){
        return writer;
    }

    public void add(Document document) throws IOException {
        writer.addDocument(document);
    }

    public void commit() throws IOException {
        writer.commit();
    }
}
