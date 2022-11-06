package com.intruder.dataserver.util;

import com.intruder.dataserver.model.RelationKeyWord;
import com.intruder.dataserver.service.RelationKeyWordService;

import java.util.List;

public class SearchSpgz {

    private final RelationKeyWordService relationKeyWordService;

    public SearchSpgz(RelationKeyWordService relationKeyWordService) {
        this.relationKeyWordService = relationKeyWordService;
    }

    String findSpgz(String workName){
        List<RelationKeyWord> relationKeyWordList = relationKeyWordService.findALL();
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
            iterKey = iterKeyBefore;
            spisKeyWord.append(relationKeyWord.getKeyWord());
            while(spisKeyWord.toString().contains("*")){
                iterKey = 0;
                keyword.append(spisKeyWord.toString().substring(0, spisKeyWord.toString().indexOf("*")));
                spisKeyWord.delete(0, spisKeyWord.toString().indexOf("*") + 2);
                if(spisKeyWord.substring(0,1).equals(" ")){
                    spisKeyWord.delete(0,1);
                }
                if(workName.contains(keyword.toString())){
                    iterKey++;
                }
            }
        }
        return null;
    }
}
