package com.intruder.dataserver.util;

import com.intruder.dataserver.model.Estimate;
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
public class ParserEstimate {
    //объявляем все переменные
    private String typeDocument = null;
    private String addition = null;
    private Integer numberDocument = null;
    private String dateDocument = null;
    private int subItemNumber = 0;
    private String codeDocument = null;
    private String nameWorksAndCosts = null;
    private String costItem = null;
    private String unit = null;
    private double countUnits = 0;
    private double pricePerUnit = 0;
    private double correctionCoefficient = 0;
    private double winterCoefficient = 0;
    private double basicCosts = 0;
    private double conversionCoefficient = 0;
    private double totalCostsAtTheCurrentPriceLevel = 0;
    private double totalCostsForTheWorkName = 0;
    private double totalBySubChapter = 0;
    private double totalByChapter = 0;
    private double total = 0;
    private double nds = 0;
    private double finalSum = 0;
    private double finalSumWithCoefficient= 0;
    ////////////////////////////////////////////
    public List<Estimate> parse(InputStream inputStream) {
        Estimate estimate = new Estimate();

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
        log.debug("создаем список записей по наименованию работ");
        //создаем список записей по наименованию работ
        List<Estimate> spisDocForNameWork = new ArrayList<>();
        //создаем общий список работ по разделу
        List<Estimate> spisDocForChapter = new ArrayList<>();
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
                    log.debug("row.getRowNum() = " + row.getRowNum());
                    //Проверяем на начало документа
                    if (!flagList) {
                        try{
                            if (row.getCell(0).getStringCellValue().contains("Составлен")) {
                                flagList = true;
                            }
                        }catch (Exception ignored){

                        }
                    }
                    //расчитываем сумму затрат по наименованию
                    log.debug("flagTotalByName " + flagTotalByName);
                    log.debug("(difference > 1) " + (difference > 1));
                    log.debug("spisDocForNameWork.size() != 0 " + (spisDocForNameWork.size() != 0));
                    if (flagTotalByName && (difference > 1) && spisDocForNameWork.size() != 0) {
                        totalCostsForTheWorkName = 0;
                        for (Estimate spisRecordForNameWork : spisDocForNameWork) {
                            log.debug("флаг расчета затрат по наименованию = " + true);
                            totalCostsForTheWorkName += spisRecordForNameWork.getTotalCostsAtTheCurrentPriceLevel();
                        }
                        for (Estimate estimateCost : spisDocForNameWork) {
                            estimateCost.setTotalCostsForTheWorkName(totalCostsForTheWorkName);
                            spisDocForChapter.add(estimateCost);
                            log.debug("controlRecordCost = " + estimateCost);
                        }
                        log.debug("spisDocForChapter.size() = " + spisDocForChapter.size());
                        log.debug("spisDocForNameWork.size() = " + spisDocForNameWork.size());

                        spisDocForNameWork.clear();
                    }


                    //добавляем новую запись
                    flagTotalByName = false;
                    //if (dateDocument != null && subItemNumber != 0) {

                        if (estimate.getCostItem() != null && estimate.getNameWorksAndCosts().length() != 0) {
                            if (estimate.getCostItem().contains("ЗП")
                                    || estimate.getCostItem().contains("МР")
                                    || estimate.getCostItem().contains("ЭМ")
                                    || estimate.getCostItem().contains("ЗПМ")
                                    || estimate.getCostItem().contains("ЗТР")) {
                                log.debug("Новая запись добавлена = " + estimate);
                                spisDocForNameWork.add(estimate);
                                //флаг парсинга значения суммы по наименованию
                                flagTotalByName = true;
                                log.debug("flagTotalByName = " + flagTotalByName);
                            }
                        }
                    //}


                    //начало парсинга

                    //создаем новый документ
                    estimate = new Estimate();
                    log.debug("создаем новый документ = " + estimate);
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
                        log.debug("cell.getColumnIndex() = " + cell.getColumnIndex());

                        //парсим данные, относящиеся ко всему листу
                        if (flagParse) {
                            //проверка на пропуск неинформативных строк
                            if (cell.getCellType().equals(CellType.STRING)) {
                                log.debug("Тип ячейки String");
                                if (cell.getStringCellValue() != null) {
                                    if (cell.getStringCellValue().contains("Раздел")) {
                                        flagParse = false;
                                        log.debug("flagParse после проверки на пропуск неинформативных строк = " + flagParse);
                                        flagParseDoc++;
                                        break out;
                                    }
                                }
                                //////////////////////////////////////////
                                if (cell.getStringCellValue() != null) {
                                    log.debug("cell.getStringCellValue() " + cell.getStringCellValue() + " num = " + cell.getColumnIndex());
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
                                        log.debug("subItemNumberNum = " + subItemNumberNum);
                                        continue;
                                    }
                                    if (cell.getStringCellValue().contains("Шифр")) {
                                        codeDocumentNum = cell.getColumnIndex();
                                        log.debug("codeDocumentNum = " + codeDocumentNum);
                                        continue;
                                    }
                                    if (cell.getStringCellValue().contains("Наименование")) {
                                        nameWorksAndCostsNum = cell.getColumnIndex();
                                        log.debug("nameWorksAndCostsNum = " + nameWorksAndCostsNum);
                                        continue;
                                    }
                                    if (cell.getStringCellValue().contains("Ед.")) {
                                        unitNum = cell.getColumnIndex();
                                        log.debug("unitNum = " + unitNum);
                                        continue;
                                    }
                                    if (cell.getStringCellValue().contains("Кол-во")) {
                                        countUnitsNum = cell.getColumnIndex();
                                        log.debug("countUnitsNum = " + countUnitsNum);
                                        continue;
                                    }
                                    if (cell.getStringCellValue().contains("Цена")) {
                                        pricePerUnitNum = cell.getColumnIndex();
                                        log.debug("pricePerUnitNum = " + pricePerUnitNum);
                                        continue;
                                    }
                                    if (cell.getStringCellValue().contains("Поправочные")) {
                                        correctionCoefficientNum = cell.getColumnIndex();
                                        log.debug("correctionCoefficientNum = " + correctionCoefficientNum);
                                        continue;
                                    }
                                    if (cell.getStringCellValue().contains("зимних")) {
                                        winterCoefficientNum = cell.getColumnIndex();
                                        log.debug("winterCoefficientNum = " + winterCoefficientNum);
                                        continue;
                                    }
                                    if (cell.getStringCellValue().contains("базисном")) {
                                        basicCostsNum = cell.getColumnIndex();
                                        log.debug("basicCostsNum = " + basicCostsNum);
                                        continue;
                                    }
                                    if (cell.getStringCellValue().contains("пересчета")) {
                                        conversionCoefficientNum = cell.getColumnIndex();
                                        log.debug("conversionCoefficientNum = " + conversionCoefficientNum);
                                        continue;
                                    }
                                    if (cell.getStringCellValue().contains("текущем") || cell.getStringCellValue().contains("ВСЕГО")) {
                                        totalCostsAtTheCurrentPriceLevelNum = cell.getColumnIndex();
                                        log.debug("totalCostsAtTheCurrentPriceLevelNum = " + totalCostsAtTheCurrentPriceLevelNum);
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
                            if (cell.getCellType().equals(CellType.STRING)) {
                                if (cell.getCellType().equals(CellType.STRING)) {
                                    try {
                                        if(cell.getStringCellValue().contains("Итого")){
                                            return spisDocForChapter;
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                            }
                            int index = cell.getColumnIndex();
                            //номер п/п
                            if (index == subItemNumberNum) {
                                if (cell.getCellType().equals(CellType.STRING)) {
                                    try {
                                        subItemNumber = Integer.parseInt(cell.getStringCellValue());

                                        log.debug("subItemNumber = {}", subItemNumber);
                                    } catch (Exception e) {
                                    }
                                }
                            }
                            //шифр документа
                            if (index == codeDocumentNum) {
                                if (cell.getCellType().equals(CellType.STRING)) {
                                    codeDocument = cell.getStringCellValue();
                                    log.debug("codeDocument = {}", codeDocument);
                                }
                            }
                            //наименование работ или статьи затрат
                            if (index == nameWorksAndCostsNum) {
                                try {
                                    if (cell.getStringCellValue() != null) {
                                        log.debug("nameWorksAndCosts = " + cell.getStringCellValue());
                                        if (cell.getStringCellValue().contains("разделу") || cell.getStringCellValue().contains("подразделу")) {
                                            break parseFile;
                                        }

                                        if (cell.getStringCellValue().contains("ЗП")
                                                || cell.getStringCellValue().contains("МР")
                                                || cell.getStringCellValue().contains("ЭМ")
                                                || cell.getStringCellValue().contains("ЗПМ")
                                                || cell.getStringCellValue().contains("ЗТР")) {
                                            costItem = cell.getStringCellValue();
                                            log.debug("costItem = {}", costItem);
                                        } else {
                                            nameWorksAndCosts = cell.getStringCellValue();
                                            costItem = null;
                                            log.debug("nameWorksAndCosts = {}", nameWorksAndCosts);
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
                                    log.debug("unit = {}", unit);
                                }
                            }
                            //количество
                            if (index == countUnitsNum) {
                                if (cell.getCellType().equals(CellType.NUMERIC)) {
                                    try {
                                        countUnits = cell.getNumericCellValue();
                                    } catch (Exception ignored) {
                                    }
                                    log.debug("countUnits = {}", countUnits);
                                }
                            }
                            //цена за единицу
                            if (index == pricePerUnitNum) {
                                if (cell.getCellType().equals(CellType.NUMERIC)) {
                                    try {
                                        pricePerUnit = cell.getNumericCellValue();
                                    } catch (Exception ignored) {

                                    }
                                    log.debug("pricePerUnit = {}", pricePerUnit);
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
                                    log.debug("correctionCoefficient = {}", correctionCoefficient);
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
                                    log.debug("winterCoefficient = {}", winterCoefficient);
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
                                    log.debug("basicCosts = {}", basicCosts);
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
                                    log.debug("conversionCoefficient = {}", conversionCoefficient);
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
                                    log.debug("totalCostsAtTheCurrentPriceLevel = {}", totalCostsAtTheCurrentPriceLevel);
                                }else{
                                    totalCostsAtTheCurrentPriceLevel = 0;
                                }
                            }
                        }
                    }
                }
                estimate = changeRecord(estimate);
            }
        }
        log.info(spisDocForChapter.size());
        return spisDocForChapter;
    }

    private Estimate changeRecord(Estimate estimate){
        //создаем новую запись
        if(typeDocument != null) estimate.setTypeDocument(typeDocument);
        if(addition != null) estimate.setAddition(addition);
        if(numberDocument != null) estimate.setNumberDocument(numberDocument);
        if(dateDocument != null) estimate.setDateDocument(dateDocument);
        if(costItem != null) estimate.setCostItem(costItem);
        if(codeDocument != null) estimate.setCodeDocument(codeDocument);
        if(subItemNumber != 0) estimate.setSubItemNumber(subItemNumber);
        if(nameWorksAndCosts != null) estimate.setNameWorksAndCosts(nameWorksAndCosts);
        if(unit != null) estimate.setUnit(unit);
        if(countUnits != 0) estimate.setCountUnits(countUnits);
        if(pricePerUnit != 0) estimate.setPricePerUnit(pricePerUnit);
        if(correctionCoefficient != 0) estimate.setCorrectionCoefficient(correctionCoefficient);
        if(winterCoefficient != 0) estimate.setWinterCoefficient(winterCoefficient);
        if(basicCosts != 0) estimate.setBasicCosts(basicCosts);
        if(conversionCoefficient != 0) estimate.setConversionCoefficient(conversionCoefficient);
        if(totalCostsAtTheCurrentPriceLevel != 0) estimate.setTotalCostsAtTheCurrentPriceLevel(totalCostsAtTheCurrentPriceLevel);
        if(totalCostsForTheWorkName != 0) estimate.setTotalCostsForTheWorkName(totalCostsForTheWorkName);
        if(totalBySubChapter != 0) estimate.setTotalBySubChapter(totalBySubChapter);
        if(totalByChapter != 0) estimate.setTotalByChapter(totalByChapter);
        if(total != 0) estimate.setTotal(total);
        if(nds != 0) estimate.setNds(nds);
        if(finalSum != 0) estimate.setFinalSum(finalSum);
        if(finalSumWithCoefficient != 0) estimate.setFinalSumWithCoefficient(finalSumWithCoefficient);
        //проверяем документ документ
        log.debug("changeRecord" + estimate);
        return estimate;
    }
}
