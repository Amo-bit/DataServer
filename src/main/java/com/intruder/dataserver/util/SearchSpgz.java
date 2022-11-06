package com.intruder.dataserver.util;

import com.intruder.dataserver.model.RelationKeyWord;
import com.intruder.dataserver.service.RelationKeyWordService;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
public class SearchSpgz {

    private final RelationKeyWordService relationKeyWordService;

    public SearchSpgz(RelationKeyWordService relationKeyWordService) {
        this.relationKeyWordService = relationKeyWordService;
    }

    public String findSpgz(String workName, List<RelationKeyWord> relationKeyWordList){
        log.debug("workName for search = " + workName);
        //счетчик совпадений ключевых слов предыдущего спгз
        int iterKeyBefore = 0;
        //счетчик совпадение текущего спгз
        int iterKey = 0;
        //ключевые слова
        StringBuilder spisKeyWord  = new StringBuilder();
        //ключевоее слово
        StringBuilder keyword = new StringBuilder();
        //подходящий спгз
        StringBuilder spgz = new StringBuilder();
        for(RelationKeyWord relationKeyWord : relationKeyWordList){

            iterKey = 0;
            spisKeyWord.append(relationKeyWord.getKeyWord());
            log.debug("spisKeyWord = " + spisKeyWord);
            while(spisKeyWord.toString().contains("*") && spisKeyWord.length() > 2){

                keyword.setLength(0);
                keyword.append(spisKeyWord.toString().substring(0, spisKeyWord.toString().indexOf("*")));
                //log.debug("keyword = " + keyword);
                spisKeyWord.delete(0, spisKeyWord.toString().indexOf("*") + 1);
                if(spisKeyWord.length() > 2){
                    if(spisKeyWord.substring(0,2).equals(" ")) {
                        spisKeyWord.delete(0, spisKeyWord.toString().indexOf(" ") + 1);
                    }
                }
                log.debug("workNameForControl = "  + workName);
                log.debug("keyWord for control = " + keyword);
                if(workName.contains(keyword.toString())){
                    iterKey++;
                }
                if(spisKeyWord.length() == 0) {
                    continue;
                }
            }
            log.debug("iterKey = " + iterKey);
            log.debug("iterKeyBefore = " + iterKeyBefore);
            if(iterKey > iterKeyBefore) {
                iterKeyBefore = iterKey;
                if(relationKeyWord.getSpgz().contains(",")){
                    spgz.setLength(0);
                    spgz.append(relationKeyWord.getSpgz().substring(0, relationKeyWord.getSpgz().indexOf(",")));
                }else{
                    spgz.setLength(0);
                    spgz.append(relationKeyWord.getSpgz());
                }
            }

        }
        log.debug("конечный spgz = " + spgz);
        return spgz.toString();
    }
}
