package com.intruder.dataserver.controller;

import com.intruder.dataserver.model.RecordSpgz;
import com.intruder.dataserver.service.RecordSpgzService;
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
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/spgz")
@Log4j2
public class RecordSpgzController {

    private final RecordSpgzService recordSpgzService;
    private ParserSn parser = new ParserSn();

    @Autowired
    public RecordSpgzController(RecordSpgzService recordSpgzService) {
        this.recordSpgzService = recordSpgzService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/")
    public ResponseEntity<Object> acceptData(HttpServletRequest requestEntity) throws Exception {
        byte[] processedText = IOUtils.toByteArray(requestEntity.getInputStream());
        List<RecordSpgz> list = new ArrayList<>();
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
    public ResponseEntity<List<RecordSpgz>> create() {
        //long start = System.currentTimeMillis();
        FileInputStream inputStream = null;
        try {
            log.info("читаем поток");
            inputStream = new FileInputStream("C:\\SN\\ул. Трофимова д.26_-1 (4) (1).xlsx");

            System.out.println(inputStream);
        } catch (FileNotFoundException e) {
            log.warn("Файл не найден");
            throw new RuntimeException(e);
        }
        log.info("поток прочтен");
        List<RecordSpgz> list = new ArrayList<>();

        /*list = parser.parse(inputStream);
        //System.out.println("list " + list);
        for (RecordSpgz recordSpgz : list){
            System.out.println(recordSpgz);
            //recordService.create(record);
        }*/

        //long finish = System.currentTimeMillis();
        //long elapsed = finish - start;
        //log.info("Прошло времени, мс: " + elapsed);


        return list != null &&  !list.isEmpty()
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
