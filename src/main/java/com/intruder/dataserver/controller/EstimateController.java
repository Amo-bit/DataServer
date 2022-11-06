package com.intruder.dataserver.controller;

import com.intruder.dataserver.model.Estimate;
import com.intruder.dataserver.model.RecordSn;
import com.intruder.dataserver.model.RelationKeyWord;
import com.intruder.dataserver.model.SampleTz;
import com.intruder.dataserver.service.EstimateService;
import com.intruder.dataserver.service.RelationKeyWordService;
import com.intruder.dataserver.service.SampleTzService;
import com.intruder.dataserver.util.ParserEstimate;
import com.intruder.dataserver.util.ParserSn;
import com.intruder.dataserver.util.SearchSpgz;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/v1/load/estimate")
@Log4j2
public class EstimateController {

    private final EstimateService estimateService;
    private final RelationKeyWordService relationKeyWordService;
    private final SampleTzService sampleTzService;
    ParserEstimate parser = new ParserEstimate();
    private String spgz = null;
    private List<SampleTz> sampleTzList;

    public EstimateController(EstimateService estimateService, RelationKeyWordService relationKeyWordService, SampleTzService sampleTzService) {
        this.estimateService = estimateService;
        this.relationKeyWordService = relationKeyWordService;
        this.sampleTzService = sampleTzService;
    }


    @RequestMapping(method = RequestMethod.POST, value = "/")

    public @ResponseBody ResponseEntity<?> acceptData(@RequestParam("file") MultipartFile uploadFile) throws Exception {

        List<Estimate> list = new ArrayList<>();
        list = parser.parse(uploadFile.getInputStream());
        SearchSpgz searchSpgz = new SearchSpgz(relationKeyWordService);
        StringBuilder beforeNameWork = new StringBuilder();

        List<RelationKeyWord> relationKeyWordList = relationKeyWordService.findALL();

        list.forEach(f -> {

            if(!beforeNameWork.toString().equals(f.getNameWorksAndCosts())){
                spgz = searchSpgz.findSpgz(f.getNameWorksAndCosts(), relationKeyWordList);
                beforeNameWork.setLength(0);
                beforeNameWork.append(f.getNameWorksAndCosts());
                sampleTzList = sampleTzService.findAllBySpgzContains(spgz);

                f.setIdSubChapter((int) sampleTzList.get(0).getIdSpgz());
                log.debug("запись после полной обработки = " + f);
            }else {
                f.setIdSubChapter((int) sampleTzList.get(0).getIdSpgz());
            }

            log.debug("spgz = " + spgz);
            log.debug("f.getNameWorksAndCosts() = " + f.getNameWorksAndCosts());


        });

        ////////////////////
        estimateService.saveAll(list);


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
