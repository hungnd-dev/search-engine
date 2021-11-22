package vn.danghung.dev.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.danghung.dev.service.DocService;

@RestController
public class DocController extends BaseController{
    @Autowired
    private DocService docService;

    @GetMapping("/all")
    public ResponseEntity<?> findAllDoc(){
        Object svcResponse = docService.getDoc();
        return new ResponseEntity<>(svcResponse, HttpStatus.OK);
    }

    @GetMapping("/doc/{id}")
    public ResponseEntity<?> findDocById(
            @PathVariable("id") int id
    ){
        Object svcResponse = docService.getDocById(id);
        return new ResponseEntity<>(svcResponse,HttpStatus.OK);
    }

    @GetMapping("/search/")
    public ResponseEntity<?> search(
            @RequestParam("searchKey") String searchKey,
            @RequestParam("paging") int paging
    ){
        Object svcResponse = docService.searching(searchKey, paging);
        return new ResponseEntity<>(svcResponse,HttpStatus.OK);
    }

    @GetMapping("/index/{docId}")
    public ResponseEntity<?> getIndexDoc(
            @PathVariable("docId") int id
    ){
        Object svcResponse = docService.getIndexDocument(id);
        return new ResponseEntity<>(svcResponse,HttpStatus.OK);
    }

    @GetMapping("index/tfidf")
    public ResponseEntity<?> getTfIdf(){
        Object svcResponse = docService.getTfIdf();
        return new ResponseEntity<>(svcResponse,HttpStatus.OK);
    }
}
