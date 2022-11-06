package com.intruder.dataserver.controller;

import com.intruder.dataserver.model.RecordSn;
import com.intruder.dataserver.model.SampleTz;
import com.intruder.dataserver.service.RecordSnService;
import com.intruder.dataserver.service.RelationKeyWordService;
import com.intruder.dataserver.service.RelationSnSpgzService;
import com.intruder.dataserver.service.SampleTzService;
import com.intruder.dataserver.util.ParserSn;
import com.intruder.dataserver.util.SearchSpgz;
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
    private final RelationKeyWordService relationKeyWordService;
    private final SampleTzService sampleTzService;
    ParserSn parser = new ParserSn();


    public RecordSnController(RecordSnService recordService, RelationSnSpgzService relationSnSpgzService, RelationKeyWordService relationKeyWordService, SampleTzService sampleTzService) {
        this.recordService = recordService;
        this.relationSnSpgzService = relationSnSpgzService;
        this.relationKeyWordService = relationKeyWordService;
        this.sampleTzService = sampleTzService;
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
    String spgz = null;
    List<SampleTz> sampleTzList;
    @GetMapping(value = "/")
    public ResponseEntity<List<RecordSn>> create() {
        //long start = System.currentTimeMillis();
        FileInputStream inputStream = null;
        try {
            log.info("читаем поток");
            inputStream = new FileInputStream("C:\\SN\\example.xlsx");

            System.out.println(inputStream);
        } catch (FileNotFoundException e) {
            log.warn("Файл не найден");
            throw new RuntimeException(e);
        }
        log.info("поток прочтен");
        List<RecordSn> list = new ArrayList<>();
        SearchSpgz searchSpgz = new SearchSpgz(relationKeyWordService);
        StringBuilder beforeNameWork = new StringBuilder();
        list = parser.parse(inputStream);

        list.forEach(f -> {
            f.setDocumentId(1);
            if(beforeNameWork.toString().equals(f.getNameWorksAndCosts())){

            }else {
                spgz = searchSpgz.findSpgz(f.getNameWorksAndCosts());
                beforeNameWork.setLength(0);
                beforeNameWork.append(f.getNameWorksAndCosts());
                sampleTzList = sampleTzService.findAllBySpgzContains(spgz);
            }

            System.out.println("spgz = " + spgz);
            System.out.println("f.getNameWorksAndCosts() = " + f.getNameWorksAndCosts());

            f.setIdSubChapter((int) sampleTzList.get(0).getIdSpgz());
            System.out.println("запись после полной обработки = " + f);
        });

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
