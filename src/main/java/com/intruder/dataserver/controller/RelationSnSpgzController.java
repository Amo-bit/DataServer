package com.intruder.dataserver.controller;

import com.intruder.dataserver.model.RelationSnSpgz;
import com.intruder.dataserver.service.RelationSnSpgzService;
import com.intruder.dataserver.util.ParserRelationSnSpgz;
import com.intruder.dataserver.util.ParserSn;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/spgz")
@Log4j2
public class RelationSnSpgzController {

    private final RelationSnSpgzService relationSnSpgzService;
    private ParserRelationSnSpgz parser = new ParserRelationSnSpgz();

    @Autowired
    public RelationSnSpgzController(RelationSnSpgzService relationSnSpgzService) {
        this.relationSnSpgzService = relationSnSpgzService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/")
    public ResponseEntity<Object> acceptData(HttpServletRequest requestEntity) throws Exception {
        byte[] processedText = IOUtils.toByteArray(requestEntity.getInputStream());
        List<RelationSnSpgz> list = new ArrayList<>();
        log.info(processedText.length);
        /*list = parser.parse(new ByteArrayInputStream(processedText));
        for (RecordSpgz recordSpgz : list){
            System.out.println(recordSpgz);
            recordSpgzService.create(recordSpgz);
        }*/

        return !list.isEmpty()
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping(value = "/")
    public ResponseEntity<List<RelationSnSpgz>> create() {
        //long start = System.currentTimeMillis();
        FileInputStream inputStream = null;
        try {
            log.info("читаем поток");
            inputStream = new FileInputStream("C:\\SN\\ТСН-2001 С СПГЗ.xlsx");

            System.out.println(inputStream);
        } catch (FileNotFoundException e) {
            log.warn("Файл не найден");
            throw new RuntimeException(e);
        }
        log.info("поток прочтен");
        List<RelationSnSpgz> list = new ArrayList<>();
        list = parser.parse(inputStream);
        relationSnSpgzService.saveAll(list);

        //long finish = System.currentTimeMillis();
        //long elapsed = finish - start;
        //log.info("Прошло времени, мс: " + elapsed);


        return list != null &&  !list.isEmpty()
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
