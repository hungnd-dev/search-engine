package vn.danghung.dev.utils;

import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.util.BytesRef;
import vn.danghung.dev.model.FreqAndPosResponse;
import vn.danghung.dev.model.TfIdfResponse;
import vn.danghung.dev.search.SearchUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class IndexDocumentUtil {
    public static Terms getTerms(IndexReader r, String field) throws IOException {
        final List<LeafReaderContext> leaves = r.leaves();
        if (leaves.size() == 1) {
            return leaves.get(0).reader().terms(field);
        }
        final List<Terms> termsPerLeaf = new ArrayList<>(leaves.size());
        final List<ReaderSlice> slicePerLeaf = new ArrayList<>(leaves.size());
        for (int leafIdx = 0; leafIdx < leaves.size(); leafIdx++) {
            LeafReaderContext ctx = leaves.get(leafIdx);
            Terms subTerms = ctx.reader().terms(field);
            if (subTerms != null) {
                termsPerLeaf.add(subTerms);
                slicePerLeaf.add(new ReaderSlice(ctx.docBase, r.maxDoc(), leafIdx - 1));
            }
        }
        if (termsPerLeaf.size() == 0) {
            return null;
        } else {
            return new MultiTerms(termsPerLeaf.toArray(Terms.EMPTY_ARRAY),
                    slicePerLeaf.toArray(ReaderSlice.EMPTY_ARRAY));
        }
    }

    public static List<TfIdfResponse> getTfIdfAllTermOfField(IndexSearcher indexSearcher, String field){
        List<TfIdfResponse> tfIdfResponseList = new ArrayList<>();
        try {
            IndexReader index = indexSearcher.getIndexReader();
            double N = index.numDocs();
            double corpusLength = index.getSumTotalTermFreq( field );
            Terms voc = IndexDocumentUtil.getTerms(index,field);
            TermsEnum termsEnum = voc.iterator();
            BytesRef term;
            while ( ( term = termsEnum.next() ) != null ) {
                TfIdfResponse tfIdfResponse = new TfIdfResponse();

                int n = termsEnum.docFreq();
                long freq = termsEnum.totalTermFreq();
                double idf = Math.log( ( N + 1.0 ) / ( n + 1.0 ) ); // well,I normalize N and n by adding 1 to avoid n = 0
                double pwc = freq / corpusLength;

                tfIdfResponse.setTerm(term.utf8ToString());
                tfIdfResponse.setDf(n);
                tfIdfResponse.setTotal_if(freq);
                tfIdfResponse.setIdf(idf);
                tfIdfResponse.setPwc(pwc);
                tfIdfResponseList.add(tfIdfResponse);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return tfIdfResponseList;
    }

    public static List<FreqAndPosResponse> freqAndPosADocument(IndexSearcher searcher, int docId , String field){
        List<FreqAndPosResponse> freqAndPosResponses = new ArrayList<>();

        try{
            IndexReader index = searcher.getIndexReader();

            Terms vector = index.getTermVector( docId, field );
            TermsEnum terms = vector.iterator();
            PostingsEnum positions = null;
            BytesRef term;
            while ( ( term = terms.next() ) != null ) {
                FreqAndPosResponse freqAndPosResponse = new FreqAndPosResponse();
                //prepare data
                long freq = terms.totalTermFreq();
                positions = terms.postings( positions, PostingsEnum.POSITIONS );
                positions.nextDoc();
                String position = "";
                for ( int i = 0; i < freq; i++ ) {
                     position += ( i > 0 ? "," : "" ) + positions.nextPosition();
                }
                //set data
                freqAndPosResponse.setFreq(freq);
                freqAndPosResponse.setPosition(position);
                freqAndPosResponse.setTerm(term.utf8ToString());
                //add to list
                freqAndPosResponses.add(freqAndPosResponse);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return freqAndPosResponses;
    }
}
