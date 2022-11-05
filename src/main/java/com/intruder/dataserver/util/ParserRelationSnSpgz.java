package com.intruder.dataserver.util;

import com.intruder.dataserver.model.RelationSnSpgz;
import com.intruder.dataserver.model.SampleTz;
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
public class ParserRelationSnSpgz {
    //объявляем все переменные
    private String codeWork = null;
    private String nameWork = null;
    private String spgz = null;

    ////////////////////////////////////////////
    public List<RelationSnSpgz> parse(InputStream inputStream) {
        RelationSnSpgz relationSnSpgz = new RelationSnSpgz();

        //добавляем переменные с номерами столбцов соответствующих значений
        int codeWorkNum = 0;
        int nameWorkNum = 0;
        int spgzNum = 0;
        /////
        log.debug("создаем список соотношений");
        //создаем список соотношений
        List<RelationSnSpgz> spisRelationSnSpgz = new ArrayList<>();
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
                relationSnSpgz = new RelationSnSpgz();
                log.debug("создаем новое соотношение = " + relationSnSpgz);
                //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                //проходим по ячейкам строки
                Iterator<Cell> cells = row.cellIterator();
                while (cells.hasNext()) {
                    Cell cell = cells.next();
                    log.debug("cell.getColumnIndex() = " + cell.getColumnIndex());

                    //парсим данные, относящиеся ко всему листу
                    if (spgzNum == 0) {

                        if (cell.getCellType().equals(CellType.STRING)) {
                            log.debug("Тип ячейки String");

                            //////////////////////////////////////////
                            if (cell.getStringCellValue() != null) {
                                log.debug("cell.getStringCellValue() " + cell.getStringCellValue() + " num = " + cell.getColumnIndex());

                                ///парсим номера столбцов
                                if (cell.getStringCellValue().contains("Шифр")) {
                                    codeWorkNum = cell.getColumnIndex();
                                    log.debug("codeWorkNum = " + codeWorkNum);
                                    continue;
                                }
                                if (cell.getStringCellValue().contains("работ")) {
                                    nameWorkNum = cell.getColumnIndex();
                                    log.debug("nameWorkNum = " + nameWorkNum);
                                    continue;
                                }
                                if (cell.getStringCellValue().contains("СПГЗ")) {
                                    spgzNum = cell.getColumnIndex();
                                    log.debug("spgzNum = " + spgzNum);
                                    continue;
                                }else {
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
                        //шифр работ
                        if (index == codeWorkNum) {
                            if (cell.getCellType().equals(CellType.STRING)) {
                                try {
                                    codeWork = cell.getStringCellValue();
                                    log.debug("codeWork = {}", codeWork);
                                } catch (Exception ignored) {
                                }
                            }
                        }
                        //наименование работ
                        if (index == nameWorkNum) {
                            if (cell.getCellType().equals(CellType.STRING)) {
                                try{
                                    nameWork = cell.getStringCellValue();
                                    log.debug("nameWork  = {}", nameWork);
                                }catch (Exception ignored) {
                                }
                            }
                        }
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
                    }
                }
            }
            relationSnSpgz = changeRelationSnSpgz(relationSnSpgz);
            //добавляем новую запись
            if (relationSnSpgz.getCodeWork() != null) {
                log.info("Новое соотношение добавлено = " + relationSnSpgz);
                spisRelationSnSpgz.add(relationSnSpgz);
            }
        }
        log.info(spisRelationSnSpgz.size());
        return spisRelationSnSpgz;
    }

    private RelationSnSpgz changeRelationSnSpgz(RelationSnSpgz relationSnSpgz){
        //создаем новую запись
        if(codeWork != null) relationSnSpgz.setCodeWork(codeWork);
        if(nameWork != null) relationSnSpgz.setNameWork(nameWork);
        if(spgz != null) relationSnSpgz.setSpgz(spgz);
        //проверяем шаблон
        log.debug("changeRecord" + relationSnSpgz);
        return relationSnSpgz;
    }
}
