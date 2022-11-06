package com.intruder.dataserver.controller;

import com.intruder.dataserver.model.RecordSn;
import com.intruder.dataserver.model.SampleTz;
import com.intruder.dataserver.model.SpgzKpgz;
import com.intruder.dataserver.service.SpgzKpgzService;
import com.intruder.dataserver.util.ParserSpgzKpgz;
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
@RequestMapping("/api/v1/load/document/kpgz")
@Log4j2
public class SpgzKpgzController {

    private final SpgzKpgzService spgzKpgzService;
    private ParserSpgzKpgz parser = new ParserSpgzKpgz();

    @Autowired
    public SpgzKpgzController(SpgzKpgzService spgzKpgzService) {
        this.spgzKpgzService = spgzKpgzService;
    }

    @GetMapping(value = "/")
    public ResponseEntity<List<SpgzKpgz>> create() {
        long start = System.currentTimeMillis();
        FileInputStream inputStream = null;
        try {
            log.info("читаем поток");
            inputStream = new FileInputStream("C:\\SN\\Список СПГЗ 27_5_2022.xlsx");

            System.out.println(inputStream);
        } catch (FileNotFoundException e) {
            log.warn("Файл не найден");
            throw new RuntimeException(e);
        }
        log.info("поток прочтен");
        List<SpgzKpgz> list = new ArrayList<>();

        list = parser.parse(inputStream);
        //System.out.println("list " + list);
        spgzKpgzService.saveAll(list);
        /*for (SampleTz sampleTz : list){
            //System.out.println(sampleTz);
            sampleTzService.create(sampleTz);
        }*/

        long finish = System.currentTimeMillis();
        long elapsed = finish - start;
        log.info("Прошло времени, мс: " + elapsed);


        return list != null &&  !list.isEmpty()
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
