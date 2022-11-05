package com.intruder.dataserver.util;

import com.intruder.dataserver.model.SampleTz;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Log4j2
public class ParserTz {
    //объявляем все переменные
    private String nameTz = null;
    private String kpgz = null;
    private long idSpgz = 0;
    private String spgz = null;

    ////////////////////////////////////////////
    public List<SampleTz> parse(InputStream inputStream) {
        SampleTz sampleTz = new SampleTz();

        //добавляем переменные с номерами столбцов соответствующих значений
        int nameNum = 0;
        int kpgzNum = 0;
        int idSpgzNum = 0;
        int spgzNum = 0;
        /////
        log.debug("создаем список шаблонов");
        //создаем список шаблонов
        List<SampleTz> spisSampleTz = new ArrayList<>();
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

        log.info("начало парсинга шаблонов ТЗ");
        while (iterator.hasNext()) {

            out:{
                Row row = iterator.next();
                log.debug("row.getRowNum() = " + row.getRowNum());

                //добавляем новую запись
                if (sampleTz.getIdSpgz() != 0) {
                    log.info("Новая запись добавлена = " + sampleTz);
                    spisSampleTz.add(sampleTz);
                }

                //начало парсинга
                //создаем новый документ
                sampleTz = new SampleTz();
                log.debug("создаем новый документ = " + sampleTz);
                //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                //проходим по ячейкам строки
                Iterator<Cell> cells = row.cellIterator();
                while (cells.hasNext()) {
                    Cell cell = cells.next();
                    log.debug("cell.getColumnIndex() = " + cell.getColumnIndex());

                    //парсим данные, относящиеся ко всему листу
                    if (spgzNum == 0) {
                        //проверка на пропуск неинформативных строк
                        if (cell.getCellType().equals(CellType.STRING)) {
                            log.debug("Тип ячейки String");

                            //////////////////////////////////////////
                            if (cell.getStringCellValue() != null) {
                                log.debug("cell.getStringCellValue() " + cell.getStringCellValue() + " num = " + cell.getColumnIndex());

                                ///парсим номера столбцов
                                if (cell.getStringCellValue().contains("ТЗ")) {
                                    nameNum = cell.getColumnIndex();
                                    log.debug("nameNum = " + nameNum);
                                    continue;
                                }
                                if (cell.getStringCellValue().contains("КПГЗ")) {
                                    kpgzNum= cell.getColumnIndex();
                                    log.debug("kpgzNum = " + kpgzNum);
                                    continue;
                                }
                                if (cell.getStringCellValue().contains("ID")) {
                                    idSpgzNum = cell.getColumnIndex();
                                    log.debug("idSpgzNum= " + idSpgzNum);
                                    continue;
                                }
                                if (cell.getStringCellValue().contains("СПГЗ")) {
                                    spgzNum = cell.getColumnIndex();
                                    log.debug("spgzNum = " + spgzNum);
                                    continue;
                                }else {
                                    log.debug("break in columnName");
                                    break out;
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

                    //парсим записи
                    else {

                        int index = cell.getColumnIndex();
                        //наименование шаблона ТЗ
                        if (index == nameNum) {
                            if (cell.getCellType().equals(CellType.STRING)) {
                                try {
                                    if(!cell.getStringCellValue().isEmpty()) nameTz = cell.getStringCellValue();
                                    log.debug("name = {}", nameTz);
                                } catch (Exception ignored) {
                                }
                            }
                        }
                        //КПГЗ
                        if (index == kpgzNum) {
                            if (cell.getCellType().equals(CellType.STRING)) {
                                try{
                                    kpgz = cell.getStringCellValue();
                                    log.debug("kpgz = {}", kpgz);
                                }catch (Exception ignored) {
                                }
                            }
                        }
                        //ID СПГЗ
                        if (index == idSpgzNum) {
                            if (cell.getCellType().equals(CellType.STRING)) {
                                try {
                                    idSpgz = Integer.parseInt(cell.getStringCellValue());
                                    log.debug("idSpgz = {}", idSpgz);
                                } catch (Exception ignored) {
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
            sampleTz = changeSampleTz(sampleTz);
        }
        log.info(spisSampleTz.size());
        return spisSampleTz;
    }

    private SampleTz changeSampleTz(SampleTz sampleTz){
        //создаем новую запись
        if(nameTz != null) sampleTz.setNameTz(nameTz);
        if(kpgz != null) sampleTz.setKpgz(kpgz);
        if(idSpgz != 0) sampleTz.setIdSpgz(idSpgz);
        if(spgz != null) sampleTz.setSpgz(spgz);
        //проверяем шаблон
        log.debug("changeRecord" + sampleTz);
        return sampleTz;
    }
}
