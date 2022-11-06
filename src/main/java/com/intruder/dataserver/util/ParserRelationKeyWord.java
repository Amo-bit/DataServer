package com.intruder.dataserver.util;

import com.intruder.dataserver.model.RelationKeyWord;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Log4j2
public class ParserRelationKeyWord {
    //объявляем все переменные
    private String keyWord = null;
    private String spgz = null;

    ////////////////////////////////////////////
    public List<RelationKeyWord> parse(InputStream inputStream) {
        RelationKeyWord relationKeyWord = new RelationKeyWord();

        //добавляем переменные с номерами столбцов соответствующих значений
        int keyWordNum = 0;
        int spgzNum = 0;
        /////
        log.debug("создаем список соотношений");
        //создаем список соотношений
        List<RelationKeyWord> spisRelationKeyWord = new ArrayList<>();
        //получаем файл xlsx
        log.debug("получаем xssfWorkbook");
        XSSFWorkbook workBook = null;
        try {
            workBook = new XSSFWorkbook(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            log.warn("Ошибка ввода данных: {0}", e);
        }
        log.info("получили xssfWorkbook");
        if(workBook.getSheetAt(0) != null) {
            log.info("getSheetAt notnull");
        }
        //разбираем первый лист входного файла на объектную модель
        Sheet sheet = workBook.getSheetAt(0);
        Iterator<Row> iterator = sheet.iterator();

        log.info("начало парсинга соотношений");
        while (iterator.hasNext()) {

            out:{
                Row row = iterator.next();
                log.debug("row.getRowNum() = " + row.getRowNum());

                //начало парсинга
                //создаем новый документ
                relationKeyWord = new RelationKeyWord();
                log.debug("создаем новое соотношение = " + relationKeyWord);
                //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                //проходим по ячейкам строки
                Iterator<Cell> cells = row.cellIterator();
                while (cells.hasNext()) {
                    Cell cell = cells.next();
                    log.debug("cell.getColumnIndex() = " + cell.getColumnIndex());

                    //парсим данные, относящиеся ко всему листу
                    if (keyWordNum == 0) {

                        if (cell.getCellType().equals(CellType.STRING)) {
                            log.debug("Тип ячейки String");

                            //////////////////////////////////////////
                            if (cell.getStringCellValue() != null) {
                                log.debug("cell.getStringCellValue() " + cell.getStringCellValue() + " num = " + cell.getColumnIndex());

                                ///парсим номера столбцов
                                if (cell.getStringCellValue().contains("СПГЗ")) {
                                    spgzNum = cell.getColumnIndex();
                                    log.debug("spgzNum = " + spgzNum);
                                    continue;
                                }
                                if (cell.getStringCellValue().contains("Ключевые")) {
                                    keyWordNum = cell.getColumnIndex();
                                    log.debug("keyWordNum = " + keyWordNum);
                                    continue;
                                } else {
                                    log.debug("break in columnName");
                                    continue;
                                }
                            }
                        } else {
                            if(cell.getColumnIndex() == row.getRowNum()){
                                log.debug("break in parse row");
                                break out;
                            }else {
                                log.debug("continue in parse row");
                                continue;
                            }
                        }
                    }

                    //парсим соотношения
                    else {

                        int index = cell.getColumnIndex();
                        //СПГЗ
                        if (index == spgzNum) {
                            if (cell.getCellType().equals(CellType.STRING)) {
                                try {
                                    spgz = cell.getStringCellValue();
                                    log.debug("spgz = {}", spgz);
                                } catch (Exception ignored) {
                                }
                            }
                        }
                        //Ключевые слова
                        if (index == keyWordNum) {
                            if (cell.getCellType().equals(CellType.STRING)) {
                                try {
                                    keyWord = cell.getStringCellValue();
                                    log.debug("keyWord = {}", keyWord);
                                } catch (Exception ignored) {
                                }
                            }
                        }
                    }
                }
            }
            relationKeyWord = changeRelationSnSpgz(relationKeyWord);
            //добавляем новую запись
            if (relationKeyWord.getKeyWord() != null) {
                log.info("Новое соотношение добавлено = " + relationKeyWord);
                spisRelationKeyWord.add(relationKeyWord);
            }
        }
        log.info(spisRelationKeyWord.size());
        return spisRelationKeyWord;
    }

    private RelationKeyWord changeRelationSnSpgz(RelationKeyWord relationKeyWord){
        //создаем новую запись
        if(spgz != null) relationKeyWord.setSpgz(spgz);
        if(keyWord != null) relationKeyWord.setKeyWord(keyWord);
        //проверяем шаблон
        log.debug("changeRecord" + relationKeyWord);
        return relationKeyWord;
    }
}
