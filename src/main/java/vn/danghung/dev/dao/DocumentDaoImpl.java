package vn.danghung.dev.dao;

import com.ecyrd.speed4j.StopWatch;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexOptions;
import org.springframework.stereotype.Component;
import vn.danghung.dev.entities.Doc;
import vn.danghung.dev.factory.MySQLConnectionFactory;
import vn.danghung.dev.index.IndexUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Component
public class DocumentDaoImpl extends AbstractDao implements DocumentDao {
    private IndexUtils indexUtils;

    @Override
    public List<Doc> findAll() {
        List<Doc> docList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM `data`";
        try {
            conn = MySQLConnectionFactory.getInstance().getMySQLConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Doc doc = new Doc();
                doc.setDocumentId(rs.getLong("id"));
                doc.setAbout(rs.getString("about"));
                doc.setLink(rs.getString("link"));
                doc.setTitle(rs.getString("title"));
                docList.add(doc);
            }
        } catch (Exception e) {
            System.out.println("Error in findById dao " + e.getMessage());
        } finally {
            releaseConnect(conn, stmt, rs);
        }
        return docList;
    }

    @Override
    public Doc findById(int id) {
        Doc doc = new Doc();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM `data` WHERE id = " + id;
        try {
            conn = MySQLConnectionFactory.getInstance().getMySQLConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                doc.setDocumentId(rs.getLong("id"));
                doc.setAbout(rs.getString("about"));
                doc.setLink(rs.getString("link"));
                doc.setTitle(rs.getString("title"));
            }
        } catch (Exception e) {
            System.out.println("Error in findById dao " + e.getMessage());
        } finally {
            releaseConnect(conn, stmt, rs);
        }
        return doc;
    }

    @Override
    public void indexing() {
        indexUtils = IndexUtils.getInstance();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM `data`";
        try {
            conn = MySQLConnectionFactory.getInstance().getMySQLConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            int i = 0;
            StopWatch swGlobal = new StopWatch();
            FieldType fieldTypeText = new FieldType();
            fieldTypeText.setIndexOptions( IndexOptions.DOCS_AND_FREQS_AND_POSITIONS );
            fieldTypeText.setStoreTermVectors( true );
            fieldTypeText.setStoreTermVectorPositions( true );
            fieldTypeText.setTokenized( true );
            fieldTypeText.setStored( true );
            fieldTypeText.freeze();

            while (rs.next()) {
                i++;
                StopWatch sw = new StopWatch();
                Document document = new Document();
                document.add(new Field("title", rs.getString("title"), fieldTypeText));
                document.add(new Field("about", rs.getString("about"), fieldTypeText));
                indexUtils.add(document);
                if (i % 50 == 0) {
                    System.out.println("Indexing document " + i + " in" + sw.stop());
                }
            }
            indexUtils.commit();
            System.out.println("Indexing done in "+ swGlobal.stop());
        } catch (Exception e) {
            System.out.println("Error in findById dao " + e.getMessage());
        } finally {
            releaseConnect(conn, stmt, rs);
        }
    }

    @Override
    public void create(String title, String about, String link) {
        Doc doc = new Doc();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "INSERT INTO data(title,link,about) VALUES (?,?,?)";
        try {
            conn = MySQLConnectionFactory.getInstance().getMySQLConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, title);
            stmt.setString(2,link);
            stmt.setString(3,about);
            stmt.executeUpdate();
            Document document = new Document();
            document.add(new TextField("title", title, Field.Store.YES));
            document.add(new TextField("about", about, Field.Store.YES));
            indexUtils.add(document);
        } catch (Exception e) {
            System.out.println("Error in findById dao " + e.getMessage());
        } finally {
            releaseConnect(conn, stmt, rs);
        }
    }

}
