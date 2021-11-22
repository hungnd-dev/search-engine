package vn.danghung.dev.search;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.index.MultiTerms;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import vn.danghung.dev.analyzer.vi.VietnameseAnalyzer;
import vn.danghung.dev.global.ConfigInfo;
import vn.danghung.dev.model.DocResponse;
import vn.danghung.dev.model.SearchResponse;
import vn.danghung.dev.utils.IndexDocumentUtil;

import java.io.IOException;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;
public class SearchUtils {
    private static SearchUtils searchUtilsInstance = null;
    static String indexDir = ConfigInfo.INDEX_DIR;
    private IndexSearcher searcher = null;
    private SearchUtils(){
        Directory fsDirectory;
        try {
            fsDirectory = FSDirectory.open(Paths.get(indexDir));
            IndexReader reader = DirectoryReader.open(fsDirectory);
            searcher = new IndexSearcher(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SearchUtils getInstance(){
        if(searchUtilsInstance == null){
            searchUtilsInstance = new SearchUtils();
        }
        return searchUtilsInstance;
    }

    public Object search(String searchKey, int paging){
        List<DocResponse> docResponseList = searchingByBooleanQuery(searchKey);
        SearchResponse searchResponse = new SearchResponse();
        int from = (paging - 1)*10;
        int to = paging * 10;
        if(docResponseList.size() > 10){
            searchResponse.setDocument(docResponseList.subList(from,to));
        }
        else{
            searchResponse.setDocument(docResponseList);
        }
        searchResponse.setTotal(docResponseList.size());
        return searchResponse;
    }

    public List<DocResponse> searchingByBooleanQuery(String searchKey) {
        List<DocResponse> docResponses = new ArrayList<>();
        try{
            QueryParser queryParserAbout = new QueryParser("about", new VietnameseAnalyzer());
            QueryParser queryParserTitle = new QueryParser("title", new VietnameseAnalyzer());
            queryParserAbout.setDefaultOperator(QueryParser.Operator.AND);
            queryParserTitle.setDefaultOperator(QueryParser.Operator.AND);
            BooleanQuery booleanQuery = new BooleanQuery.Builder()
                    .add(queryParserAbout.parse(searchKey), BooleanClause.Occur.SHOULD)
                    .add(queryParserTitle.parse(searchKey), BooleanClause.Occur.SHOULD)
                    .setMinimumNumberShouldMatch(1)
                    .build();
            TopDocs topDocs = searcher.search(booleanQuery,Integer.MAX_VALUE);

            for (ScoreDoc doc : topDocs.scoreDocs) {
                DocResponse docResponse = new DocResponse();
                Document document = searcher.doc(doc.doc);
                docResponse.setTitle(document.get("title"));
                docResponse.setAbout(document.get("about"));
                docResponse.setId(doc.doc);
                Explanation score = searcher.explain(booleanQuery, doc.doc);
                docResponse.setScore(score.getValue());
                docResponses.add(docResponse);
            }
        }catch (Exception e){
            System.out.println("Error when search using boolean query");
        }
        return docResponses;
    }

    public List<DocResponse> searchingByVectorQuery(String searchKey){
        List<DocResponse> docResponses = new ArrayList<>();
        try {

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return docResponses;
    }

    public Object getIndexedDocument(int id){
        return IndexDocumentUtil.freqAndPosADocument(searcher,id,"about");
    }

    public Object getTfIdfValue(){
        return IndexDocumentUtil.getTfIdfAllTermOfField(searcher,"about");
    }
}
