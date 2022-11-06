package com.intruder.dataserver.controller;

import com.intruder.dataserver.model.RecordSn;
import com.intruder.dataserver.model.RelationKeyWord;
import com.intruder.dataserver.model.SampleTz;
import com.intruder.dataserver.service.RecordSnService;
import com.intruder.dataserver.service.RelationKeyWordService;
import com.intruder.dataserver.service.SampleTzService;
import com.intruder.dataserver.util.ParserSn;
import com.intruder.dataserver.util.SearchSpgz;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/v1/load/document")
@Log4j2
public class RecordSnController {

    private final RecordSnService recordService;
    private final RelationKeyWordService relationKeyWordService;
    private final SampleTzService sampleTzService;
    ParserSn parser = new ParserSn();
    private String spgz = null;
    private List<SampleTz> sampleTzList;


    public RecordSnController(RecordSnService recordService, RelationKeyWordService relationKeyWordService, SampleTzService sampleTzService) {
        this.recordService = recordService;
        this.relationKeyWordService = relationKeyWordService;
        this.sampleTzService = sampleTzService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/")

    public @ResponseBody ResponseEntity<?> acceptData(@RequestParam("file") MultipartFile uploadFile, @RequestParam("id") int id) throws Exception {

        List<RecordSn> list = new ArrayList<>();
        list = parser.parse(uploadFile.getInputStream());
        SearchSpgz searchSpgz = new SearchSpgz(relationKeyWordService);

        List<RelationKeyWord> relationKeyWordList = relationKeyWordService.findALL();
        int beforeId = 0;
        for(RecordSn recordSn : list) {
            recordSn.setDocumentId(id);
            //recordSn.setTypeDocument("СН-2012");

            if(beforeId == recordSn.getSubItemNumber()){
                recordSn.setIdSubChapter((int) sampleTzList.get(0).getIdSpgz());
            }else {
                spgz = searchSpgz.findSpgz(recordSn.getNameWorksAndCosts(), relationKeyWordList);
                beforeId = recordSn.getSubItemNumber();
                sampleTzList = sampleTzService.findAllBySpgzContains(spgz);

                recordSn.setIdSubChapter((int) sampleTzList.get(0).getIdSpgz());
                log.info("запись после полной обработки = " + recordSn );
            }

            log.debug("spgz = " + spgz);
            log.debug("f.getNameWorksAndCosts() = " + recordSn.getNameWorksAndCosts());
        }

        ////////////////////
        recordService.saveAll(list);
        log.info("файл загружен в базу данных = " + uploadFile.getOriginalFilename());


        return !list.isEmpty()
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
