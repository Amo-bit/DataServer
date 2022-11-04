package com.intruder.dataserver.util;

import com.intruder.dataserver.model.Record;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Log4j2
public class ParserSN {

    public static @NotNull List<Record> parse(InputStream inputStream) {
        Record record = new Record();
        //объявляем все переменные
        String typeDocument = null;
        String addition = null;
        Integer numberDocument = null;
        String dateDocument = null;
        int subItemNumber = 0;
        String codeDocument = null;
        String nameWorksAndCosts = null;
        String costItem = null;
        String unit = null;
        double countUnits = 0;
        double pricePerUnit = 0;
        double correctionCoefficient = 0;
        double winterCoefficient = 0;
        double basicCosts = 0;
        double conversionCoefficient = 0;
        double totalCostsAtTheCurrentPriceLevel = 0;
        double totalCostsForTheWorkName = 0;
        double totalBySubChapter = 0;
        double totalByChapter = 0;
        double total = 0;
        double nds = 0;
        double finalSum = 0;
        double finalSumWithCoefficient= 0;
        ////////////////////////////////////////////
        //добавляем переменные с номерами столбцов соответствующих значений
        int subItemNumberNum = 0;
        int codeDocumentNum = 0;
        int nameWorksAndCostsNum = 0;
        int unitNum = 0;
        int countUnitsNum = 0;
        int pricePerUnitNum = 0;
        int correctionCoefficientNum = 0;
        int winterCoefficientNum = 0;
        int basicCostsNum = 0;
        int conversionCoefficientNum = 0;
        int totalCostsAtTheCurrentPriceLevelNum = 0;
        /////
        log.info("создаем список записей по наименованию работ");
        //создаем список записей по наименованию работ
        List<Record> spisDocForNameWork = new ArrayList<>();
        //создаем общий список работ по разделу
        List<Record> spisDocForChapter = new ArrayList<>();
        log.info("получаем xssfWorkbook");
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



        //счетчик итерация для поиска суммарных значений
        int iteratorSumValue = 0;

        //парсим основные значения
        log.info("парсим основные значения");
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        while(totalByChapter == 0) {
            Row row = null;
            try {
                row = sheet.getRow(sheet.getLastRowNum() - iteratorSumValue);
                row.getPhysicalNumberOfCells();
            }catch (Exception ex) {
                log.warn("ошибка NPE");
                iteratorSumValue++;
                continue;
            }
            Iterator<Cell> cells = row.cellIterator();
            ///искомая переменная
            StringBuilder findValue = new StringBuilder();
            ///счетчик значений (первая и вторая сумма в строке)
            int iteratorNum = 1;
            //первая сумма
            double firstSum = 0;
            //вторая сумма
            double secondSum = 0;
            //искомая сумма
            double findSum = 0;

            while (cells.hasNext()) {
                Cell cell = cells.next();
                //проверяем что ячейка не пустая
                if(cell.getCellType().equals(CellType.STRING)){

                    if (cell.getStringCellValue() != null){
                        if(cell.getStringCellValue().contains("Всего")) {
                            findValue.append("finalSum");
                        }
                        if(cell.getStringCellValue().contains("НДС")){
                            findValue.append("nds");
                        }
                        if(cell.getStringCellValue().contains("разделам")){
                            findValue.append("total");
                        }
                        if(cell.getStringCellValue().contains("разделу")){
                            findValue.append("totalByChapter");
                        }
                        if(cell.getStringCellValue().contains("подразделу")){
                            findValue.append("totalBySubChapter");
                        }
                    }
                }
                if(cell.getCellType().equals(CellType.NUMERIC)){
                    if(iteratorNum == 1 ){
                        firstSum = cell.getNumericCellValue();
                        findSum = firstSum;
                        iteratorNum++;
                    }else{
                        secondSum = cell.getNumericCellValue();
                        if(secondSum > firstSum){
                            findSum = secondSum;
                        }else {
                            findSum = firstSum;
                        }
                        iteratorNum = 1;
                    }
                }
            }
            switch(findValue.toString()){
                case "finalSum":
                    finalSum = findSum;
                    log.info("finalSum = " + finalSum);
                    break;
                case "nds":
                    nds = findSum;
                    log.info("nds = " + nds);
                    break;
                case "total":
                    total = findSum;
                    log.info("total = " + total);
                    break;
                case "totalByChapter":
                    totalByChapter = findSum;
                    log.info("totalByChapter = " + totalByChapter);
                    break;
                case "totalBySubChapter":
                    totalBySubChapter = findSum;
                    log.info("totalBySubChapter = " + totalBySubChapter);
                    break;
            }
            findValue.setLength(0);
            iteratorSumValue++;
        }
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //конец парсинга суммм
        log.info("конец парсинга суммм");
        log.info("начало парсинга значений");
        parseFile:{
            //флаг начала парсинга
            boolean flagList = false;
            //флаг парсинга общей части
            boolean flagParse = true;
            //флаг окончания парсинга по наименованию
            boolean flagParsWorkName = false;
            //счетчик количества обновлений записей по наименованию
            int countName = 0;
            //флаг начала парсинга записей
            int flagParseDoc = 0;
            //флаг добавления готовых значени
            boolean flagTotalByName = false;
            //начало парсинга значений
            int rowIndex = 0;
            int difference =0;
            while (iterator.hasNext()) {
                ///////////////////////////////////////
                out:
                {
                    Row row = iterator.next();
                    difference = row.getRowNum() - rowIndex;
                    rowIndex = row.getRowNum();
                    log.info("row.getRowNum() = " + row.getRowNum());
                    //Проверяем на начало документа
                    if (!flagList) {
                        if (row.getCell(0).getStringCellValue().contains("ТСН")) {
                            flagList = true;
                        }
                    }
                    //расчитываем сумму затрат по наименованию
                    if (flagTotalByName && difference > 1) {
                        totalCostsForTheWorkName = 0;
                        for (Record spisRecordForNameWork : spisDocForNameWork) {
                            log.info("флаг расчета затрат по наименованию = " + (flagTotalByName && difference > 1));
                            totalCostsForTheWorkName += spisRecordForNameWork.getTotalCostsAtTheCurrentPriceLevel();
                        }
                        for (Record recordCost : spisDocForNameWork) {
                            recordCost.setTotalCostsForTheWorkName(totalCostsForTheWorkName);
                            spisDocForChapter.add(recordCost);
                            log.info("controlRecordCost = " + recordCost);
                        }
                        log.info("spisDocForChapter.size() = " + spisDocForChapter.size());
                        log.info("spisDocForNameWork.size() = " + spisDocForNameWork.size());

                        spisDocForNameWork.clear();
                    }

                    //добавляем новую запись
                    flagTotalByName = false;
                    if (dateDocument != null && subItemNumber != 0) {
                        if (record.getCostItem() != null && record.getNameWorksAndCosts().length() != 0) {
                            if (record.getCostItem().contains("ЗП")
                                    || record.getCostItem().contains("МР")
                                    || record.getCostItem().contains("ЭМ")
                                    || record.getCostItem().contains("ЗПМ")
                                    || record.getCostItem().contains("ЗТР")) {
                                log.info("Новая запись добавлена = " + record);
                                spisDocForNameWork.add(record);
                                //флаг парсинга значения суммы по наименованию
                                flagTotalByName = true;
                                log.debug("flagTotalByName = " + flagTotalByName);
                            }
                        }
                    }


                    //начало парсинга

                    //создаем новый документ
                    record = new Record();
                    log.debug("создаем новый документ = " + record);
                    StringBuilder infoCell = new StringBuilder();
                    ///счетчик значений (первая и вторая сумма в строке)
                    int iteratorNum = 1;
                    //вторая сумма
                    double secondSum = 0;
                    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                    //проходим по ячейкам строки
                    Iterator<Cell> cells = row.cellIterator();
                    while (cells.hasNext()) {
                        Cell cell = cells.next();
                        log.info("cell.getColumnIndex() = " + cell.getColumnIndex());
                        //парсим данные, относящиеся ко всему листу
                        if (flagParse) {
                            //проверка на пропуск неинформативных строк
                            if (cell.getCellType().equals(CellType.STRING)) {
                                log.info("Тип ячейки String");
                                if (cell.getStringCellValue() != null) {
                                    if (cell.getStringCellValue().contains("Раздел")) {
                                        flagParse = false;
                                        log.info("flagParse после проверки на пропуск неинформативных строк = " + flagParse);
                                        flagParseDoc++;
                                        break out;
                                    }
                                }
                                //////////////////////////////////////////
                                if (cell.getStringCellValue() != null) {
                                    log.info("cell.getStringCellValue() " + cell.getStringCellValue() + " num = " + cell.getColumnIndex());
                                    infoCell.setLength(0);
                                    if (cell.getStringCellValue().contains("ТСН")) {
                                        String info = infoCell.append(cell.getStringCellValue()).toString();
                                        typeDocument = info.substring(info.indexOf("ТСН"))
                                                .substring(0, info.substring(info.indexOf("ТСН")).indexOf(" "));
                                        addition = info.substring(info.indexOf(":") + 2);
                                        log.info("typeDocument = " + typeDocument);
                                        log.info("addition = " + addition);
                                        break out;
                                    }
                                    if (cell.getStringCellValue().contains("период")) {
                                        String info = infoCell.append(cell.getStringCellValue()).toString();
                                        String substringInfo = info.substring(info.indexOf(":") + 3);
                                        numberDocument = Integer.valueOf(substringInfo
                                                .substring(0, substringInfo.indexOf(" ")));
                                        dateDocument = info.substring(info.indexOf("за") + 2);
                                        log.info("numberDocument = " + numberDocument);
                                        log.info("dateDocument = " + dateDocument);
                                        break out;
                                    }
                                    ///парсим номера столбцов
                                    if (cell.getStringCellValue().contains("пп")) {
                                        subItemNumberNum = cell.getColumnIndex();
                                        log.info("subItemNumberNum = " + subItemNumberNum);
                                        continue;
                                    }
                                    if (cell.getStringCellValue().contains("Шифр")) {
                                        codeDocumentNum = cell.getColumnIndex();
                                        log.info("codeDocumentNum = " + codeDocumentNum);
                                        continue;
                                    }
                                    if (cell.getStringCellValue().contains("Наименование")) {
                                        nameWorksAndCostsNum = cell.getColumnIndex();
                                        log.info("nameWorksAndCostsNum = " + nameWorksAndCostsNum);
                                        continue;
                                    }
                                    if (cell.getStringCellValue().contains("Ед.")) {
                                        unitNum = cell.getColumnIndex();
                                        log.info("unitNum = " + unitNum);
                                        continue;
                                    }
                                    if (cell.getStringCellValue().contains("Кол-во")) {
                                        countUnitsNum = cell.getColumnIndex();
                                        log.info("countUnitsNum = " + countUnitsNum);
                                        continue;
                                    }
                                    if (cell.getStringCellValue().contains("Цена")) {
                                        pricePerUnitNum = cell.getColumnIndex();
                                        log.info("pricePerUnitNum = " + pricePerUnitNum);
                                        continue;
                                    }
                                    if (cell.getStringCellValue().contains("Поправочные")) {
                                        correctionCoefficientNum = cell.getColumnIndex();
                                        log.info("correctionCoefficientNum = " + correctionCoefficientNum);
                                        continue;
                                    }
                                    if (cell.getStringCellValue().contains("зимних")) {
                                        winterCoefficientNum = cell.getColumnIndex();
                                        log.info("winterCoefficientNum = " + winterCoefficientNum);
                                        continue;
                                    }
                                    if (cell.getStringCellValue().contains("базисном")) {
                                        basicCostsNum = cell.getColumnIndex();
                                        log.info("basicCostsNum = " + basicCostsNum);
                                        continue;
                                    }
                                    if (cell.getStringCellValue().contains("пересчета")) {
                                        conversionCoefficientNum = cell.getColumnIndex();
                                        log.info("conversionCoefficientNum = " + conversionCoefficientNum);
                                        continue;
                                    }
                                    if (cell.getStringCellValue().contains("текущем") || cell.getStringCellValue().contains("ВСЕГО")) {
                                        totalCostsAtTheCurrentPriceLevelNum = cell.getColumnIndex();
                                        log.info("totalCostsAtTheCurrentPriceLevelNum = " + totalCostsAtTheCurrentPriceLevelNum);
                                        continue;
                                    }else {
                                        log.info("break in columnName");
                                        break out;
                                    }
                                }
                            } else {
                                if(cell.getColumnIndex() == row.getRowNum()){
                                    log.info("break in parse row");
                                    break out;
                                }else {
                                    log.info("continue in parse row");
                                    continue;
                                }
                            }
                        }

                        //парсим записи
                        else {
                            int index = cell.getColumnIndex();
                            //номер п/п
                            if (index == subItemNumberNum) {
                                if (cell.getCellType().equals(CellType.STRING)) {
                                    try {
                                        subItemNumber = Integer.parseInt(cell.getStringCellValue());

                                        log.info("subItemNumber = {}", subItemNumber);
                                    } catch (Exception e) {

                                        log.info("subItemNumberError = {}", subItemNumber);
                                    }
                                }
                            }
                            //шифр документа
                            if (index == codeDocumentNum) {
                                if (cell.getCellType().equals(CellType.STRING)) {
                                    codeDocument = cell.getStringCellValue();
                                    log.info("codeDocument = {}", codeDocument);
                                }
                            }
                            //наименование работ или статьи затрат
                            if (index == nameWorksAndCostsNum) {
                                try {
                                    if (cell.getStringCellValue() != null) {
                                        log.info("nameWorksAndCosts = " + cell.getStringCellValue());
                                        if (cell.getStringCellValue().contains("разделу") || cell.getStringCellValue().contains("подразделу")) {
                                            break parseFile;
                                        }

                                        if (cell.getStringCellValue().contains("ЗП")
                                                || cell.getStringCellValue().contains("МР")
                                                || cell.getStringCellValue().contains("ЭМ")
                                                || cell.getStringCellValue().contains("ЗПМ")
                                                || cell.getStringCellValue().contains("ЗТР")) {
                                            costItem = cell.getStringCellValue();
                                            log.info("costItem = {}", costItem);
                                        } else {
                                            nameWorksAndCosts = cell.getStringCellValue();
                                            costItem = null;
                                            log.info("nameWorksAndCosts = {}", nameWorksAndCosts);
                                        }
                                    }
                                } catch (Exception ignored) {
                                }
                            }
                            //единицы измерения
                            if (index == unitNum) {
                                if (cell.getCellType().equals(CellType.STRING)) {
                                    try {
                                        if (!cell.getStringCellValue().equals(""))
                                            unit = cell.getStringCellValue();
                                    } catch (Exception ignored) {
                                    }
                                    log.info("unit = {}", unit);
                                }
                            }
                            //количество
                            if (index == countUnitsNum) {
                                if (cell.getCellType().equals(CellType.NUMERIC)) {
                                    try {
                                        countUnits = cell.getNumericCellValue();
                                    } catch (Exception ignored) {
                                    }
                                    log.info("countUnits = {}", countUnits);
                                }
                            }
                            //цена за единицу
                            if (index == pricePerUnitNum) {
                                if (cell.getCellType().equals(CellType.NUMERIC)) {
                                    try {
                                        pricePerUnit = cell.getNumericCellValue();
                                    } catch (Exception ignored) {

                                    }
                                    log.info("pricePerUnit = {}", pricePerUnit);
                                } else {
                                    pricePerUnit = 0;
                                }
                            }
                            //поправочный коэффициент
                            if (index == correctionCoefficientNum) {
                                if (cell.getCellType().equals(CellType.NUMERIC)) {
                                    try {
                                        correctionCoefficient = cell.getNumericCellValue();
                                    } catch (Exception ignored) {

                                    }
                                    log.info("correctionCoefficient = {}", correctionCoefficient);
                                } else {
                                    correctionCoefficient = 0;
                                }
                            }
                            //коэффициент зимних удорожаний
                            if (index == winterCoefficientNum) {
                                if (cell.getCellType().equals(CellType.NUMERIC)) {
                                    try {
                                        winterCoefficient = cell.getNumericCellValue();
                                    } catch (Exception ignored) {

                                    }
                                    log.info("winterCoefficient = {}", winterCoefficient);
                                } else {
                                    winterCoefficient = 0;
                                }
                            }
                            //всего в базисном уровне цен
                            if (index == basicCostsNum) {
                                if (cell.getCellType().equals(CellType.NUMERIC)) {
                                    try {
                                        basicCosts = cell.getNumericCellValue();
                                    } catch (Exception ignored) {

                                    }
                                    log.info("basicCosts = {}", basicCosts);
                                }else {
                                    basicCosts = 0;
                                }
                            }
                            //коэффициент пересчета
                            if (index == conversionCoefficientNum) {
                                if (cell.getCellType().equals(CellType.NUMERIC)) {
                                    try {
                                        conversionCoefficient = cell.getNumericCellValue();
                                    } catch (Exception ignored) {

                                    }
                                    log.info("conversionCoefficient = {}", conversionCoefficient);
                                }else{
                                    conversionCoefficient = 0;
                                }
                            }
                            //всего  в текущем уровне цен
                            if (index == totalCostsAtTheCurrentPriceLevelNum) {
                                if (cell.getCellType().equals(CellType.NUMERIC)) {
                                    try {
                                        totalCostsAtTheCurrentPriceLevel = cell.getNumericCellValue();
                                    } catch (Exception ignored) {
                                    }
                                    log.info("totalCostsAtTheCurrentPriceLevel = {}", totalCostsAtTheCurrentPriceLevel);
                                }else{
                                    totalCostsAtTheCurrentPriceLevel = 0;
                                }
                                log.info("break out");
                                break;
                            }
                        }
                    }


                    //создаем новую запись
                    record.setTypeDocument(typeDocument);
                    record.setAddition(addition);
                    record.setNumberDocument(numberDocument);
                    record.setDateDocument(dateDocument);
                    record.setCostItem(costItem);
                    record.setCodeDocument(codeDocument);
                    record.setSubItemNumber(subItemNumber);
                    record.setNameWorksAndCosts(nameWorksAndCosts);
                    record.setUnit(unit);
                    record.setCountUnits(countUnits);
                    record.setPricePerUnit(pricePerUnit);
                    record.setCorrectionCoefficient(correctionCoefficient);
                    record.setWinterCoefficient(winterCoefficient);
                    record.setBasicCosts(basicCosts);
                    record.setConversionCoefficient(conversionCoefficient);
                    record.setTotalCostsAtTheCurrentPriceLevel(totalCostsAtTheCurrentPriceLevel);
                    record.setTotalCostsForTheWorkName(totalCostsForTheWorkName);
                    record.setTotalBySubChapter(totalBySubChapter);
                    record.setTotalByChapter(totalByChapter);
                    record.setTotal(total);
                    record.setNds(nds);
                    record.setFinalSum(finalSum);
                    record.setFinalSumWithCoefficient(finalSumWithCoefficient);
                    //проверяем документ документ
                    log.info("changeRecord" + record);

                }
            }
        }
        return spisDocForChapter;
    }
}