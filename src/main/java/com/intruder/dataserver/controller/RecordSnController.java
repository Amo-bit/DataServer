package com.intruder.dataserver.controller;

import com.intruder.dataserver.model.RecordSn;
import com.intruder.dataserver.model.RelationSnSpgz;
import com.intruder.dataserver.service.RecordSnService;
import com.intruder.dataserver.service.RelationSnSpgzService;
import com.intruder.dataserver.util.ParserSn;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.util.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/v1/load/document")
@Log4j2
public class RecordSnController {

    private final RecordSnService recordService;
    private final RelationSnSpgzService relationSnSpgzService;
    ParserSn parser = new ParserSn();


    public RecordSnController(RecordSnService recordService, RelationSnSpgzService relationSnSpgzService) {
        this.recordService = recordService;
        this.relationSnSpgzService = relationSnSpgzService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/")
    public ResponseEntity<Object> acceptData(HttpServletRequest requestEntity) throws Exception {
        byte[] processedText = IOUtils.toByteArray(requestEntity.getInputStream());
        List<RecordSn> list = new ArrayList<>();
        log.info(processedText.length);
        list = parser.parse(new ByteArrayInputStream(processedText));
        for (RecordSn record : list){
            System.out.println(record);
            recordService.create(record);
        }

        return !list.isEmpty()
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping(value = "/")
    public ResponseEntity<List<RecordSn>> create() {
        //long start = System.currentTimeMillis();
        FileInputStream inputStream = null;
        try {
            log.info("читаем поток");
            inputStream = new FileInputStream("C:\\SN\\sn\\Глава 1. Здания.xlsx");

            System.out.println(inputStream);
        } catch (FileNotFoundException e) {
            log.warn("Файл не найден");
            throw new RuntimeException(e);
        }
        log.info("поток прочтен");
        List<RecordSn> list = new ArrayList<>();

        list = parser.parse(inputStream);
        list.forEach(f -> {f.setDocumentId(6);});

        //добавление спгз и пгз
        /*list.forEach(f -> {
            RelationSnSpgz relationSnSpgz = new RelationSnSpgz();
            try {
                relationSnSpgz = relationSnSpgzService.findAllByCodeWork(f.getCodeDocument());
                log.info("f.getCodeDocument() = " + f.getCodeDocument());
                log.info(relationSnSpgz);
            }catch (Exception ignored){}
            try{
                if(relationSnSpgz == null) {
                    String keyWord = findKeyWord(f.getNameWorksAndCosts());
                    log.info("keuWord = " + keyWord);
                    List<RelationSnSpgz> relationSnSpgzList = relationSnSpgzService.findALLByNameWorkContains(keyWord);
                    relationSnSpgz = relationSnSpgzList.get(0);
                    log.info("relationSnSpgzList.get(0) = " + relationSnSpgzList.get(0));
                }
            }catch (Exception ex){}

            if(relationSnSpgz != null){
                if(relationSnSpgz.getIdSpgz() != 0) {
                    f.setIdSubChapter((int)relationSnSpgz.getIdSpgz());
                }else{
                    String codeWork = relationSnSpgz.getSpgz().substring(relationSnSpgz.getSpgz().indexOf(" к ") + 1);
                    long idSubchapter = relationSnSpgzService.findIdSpgzByCodeWork(codeWork);
                    f.setIdSubChapter((int)idSubchapter);
                }
            }


        });*/
        ////////////////////
        recordService.saveAll(list);
        //list.forEach(System.out::println);
        //long finish = System.currentTimeMillis();
        //long elapsed = finish - start;
        //log.info("Прошло времени, мс: " + elapsed);


        return !list.isEmpty()
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private String findKeyWord(String nameWork){
        StringBuilder keyWord = new StringBuilder();
        keyWord.append(nameWork.substring(0, nameWork.indexOf(" ") + 1));
        keyWord.append(nameWork.substring(nameWork.indexOf(" ") + 1)
                .substring(0, nameWork.substring(nameWork.indexOf(" ") + 1).indexOf(" ") - 3));
        return keyWord.toString();
    }
}
