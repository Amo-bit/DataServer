package com.intruder.dataserver.controller;

import com.intruder.dataserver.model.Record;
import com.intruder.dataserver.model.SampleTz;
import com.intruder.dataserver.service.SampleTzService;
import com.intruder.dataserver.util.ParserTz;
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
@RequestMapping("/tz")
@Log4j2
public class SampleTzController {

    private final SampleTzService sampleTzService;
    private ParserTz parser = new ParserTz();

    @Autowired
    public SampleTzController(SampleTzService sampleTzService) {
        this.sampleTzService = sampleTzService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/")
    public ResponseEntity<Object> acceptData(HttpServletRequest requestEntity) throws Exception {
        byte[] processedText = IOUtils.toByteArray(requestEntity.getInputStream());
        List<SampleTz> list = new ArrayList<>();
        log.info(processedText.length);
        list = parser.parse(new ByteArrayInputStream(processedText));
        for (SampleTz sampleTz : list){
            System.out.println(sampleTz);
            sampleTzService.create(sampleTz);
        }

        return !list.isEmpty()
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping(value = "/")
    public ResponseEntity<List<Record>> create() {
        long start = System.currentTimeMillis();
        FileInputStream inputStream = null;
        try {
            log.info("читаем поток");
            inputStream = new FileInputStream("C:\\SN\\tz1.xlsx");

            System.out.println(inputStream);
        } catch (FileNotFoundException e) {
            log.warn("Файл не найден");
            throw new RuntimeException(e);
        }
        log.info("поток прочтен");
        List<SampleTz> list = new ArrayList<>();

        list = parser.parse(inputStream);
        //System.out.println("list " + list);
        for (SampleTz sampleTz : list){
            System.out.println(sampleTz);
            //recordService.create(record);
        }

        long finish = System.currentTimeMillis();
        long elapsed = finish - start;
        log.info("Прошло времени, мс: " + elapsed);


        return list != null &&  !list.isEmpty()
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
