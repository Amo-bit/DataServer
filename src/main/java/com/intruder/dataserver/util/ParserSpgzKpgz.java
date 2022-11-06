package com.intruder.dataserver.util;

//import com.monitorjbl.xlsx.StreamingReader;
import com.github.pjfanning.xlsx.StreamingReader;
import com.intruder.dataserver.model.SpgzKpgz;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Log4j2
public class ParserSpgzKpgz {
    //объявляем все переменные

    private String kpgz = null;
    private String spgz = null;
    private String units = null;
    private String okpd = null;
    private String okpd2 = null;


    ////////////////////////////////////////////
    public List<SpgzKpgz> parse(InputStream inputStream) {
        SpgzKpgz spgzKpgz = new SpgzKpgz();

        //добавляем переменные с номерами столбцов соответствующих значений
        int kpgzNum = 0;
        int spgzNum = 0;
        int unitsNum = 0;
        int okpdNum = 0;
        int okpd2Num = 0;
        /////
        log.debug("создаем список КПГЗ и СПГЗ");
        //создаем список шаблонов
        List<SpgzKpgz> spgzKpgzList = new ArrayList<>();
        //получаем файл xlsx
        log.debug("получаем Workbook");

        try (Workbook workbook = StreamingReader.builder()
                .rowCacheSize(100)
                .bufferSize(4096)
                .open(inputStream)) {

            log.info("получили xssfWorkbook");

            DataFormatter dataFormatter = new DataFormatter();
            for (Sheet sheet : workbook) {
                Iterator<Row> iterator = sheet.iterator();

                log.info("начало парсинга КПГЗ и СПГЗ");
                while (iterator.hasNext()) {

                    out:
                    {
                        Row row = iterator.next();
                        log.debug("row.getRowNum() = " + row.getRowNum());

                        //начало парсинга
                        //создаем новый документ
                        spgzKpgz = new SpgzKpgz();
                        log.debug("создаем новый документ = " + spgzKpgz);
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
                                        if (cell.getStringCellValue().contains("КПГЗ")) {
                                            kpgzNum = cell.getColumnIndex();
                                            log.debug("kpgzNum = " + kpgzNum);
                                            continue;
                                        }
                                        if (cell.getStringCellValue().contains("СПГЗ")) {
                                            spgzNum = cell.getColumnIndex();
                                            log.debug("spgzNum = " + spgzNum);
                                            continue;
                                        }
                                        if (cell.getStringCellValue().contains("Единицы")) {
                                            unitsNum = cell.getColumnIndex();
                                            log.debug("unitsNum = " + unitsNum);
                                            continue;
                                        }
                                        if (cell.getStringCellValue().contains("ОКПД")) {
                                            okpdNum = cell.getColumnIndex();
                                            log.debug("okpdNum = " + okpdNum);
                                            continue;
                                        }
                                        if (cell.getStringCellValue().contains("ОКПД 2")) {
                                            okpd2Num = cell.getColumnIndex();
                                            log.debug("okpd2Num = " + okpd2Num);
                                            continue;
                                        } else {
                                            log.debug("break in columnName");
                                            continue;
                                        }
                                    }
                                } else {
                                    if (cell.getColumnIndex() == row.getRowNum()) {
                                        log.debug("break in parse row");
                                        break out;
                                    } else {
                                        log.debug("continue in parse row");
                                        continue;
                                    }
                                }
                            }

                            //парсим записи
                            else {

                                int index = cell.getColumnIndex();
                                //КПГЗ
                                if (index == kpgzNum) {
                                    if (cell.getCellType().equals(CellType.STRING)) {
                                        try {
                                            kpgz = cell.getStringCellValue();
                                            log.debug("kpgz = {}", kpgz);
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
                                //единицы измерения
                                if (index == unitsNum) {
                                    if (cell.getCellType().equals(CellType.STRING)) {
                                        try {
                                            units = cell.getStringCellValue();
                                            log.debug("units = {}", units);
                                        } catch (Exception ignored) {
                                        }
                                    }
                                }
                                //ОКПД
                                if (index == okpdNum) {
                                    if (cell.getCellType().equals(CellType.STRING)) {
                                        try {
                                            okpd = cell.getStringCellValue();
                                            log.debug("okpd = {}", okpd);
                                        } catch (Exception ignored) {
                                        }
                                    }
                                }
                                //ОКПД 2
                                if (index == okpd2Num) {
                                    if (cell.getCellType().equals(CellType.STRING)) {
                                        try {
                                            okpd2 = cell.getStringCellValue();
                                            log.debug("okpd2 = {}", okpd2);
                                        } catch (Exception ignored) {
                                        }
                                    }
                                }
                            }
                        }
                    }
                    spgzKpgz = changeSpgzKpgz(spgzKpgz);
                    //добавляем новую запись
                    if (spgzKpgz.getOkpd2() != null) {
                        log.info("Новая запись добавлена = " + spgzKpgz);
                        spgzKpgzList.add(spgzKpgz);
                    }

                }
            }
        }catch (IOException exception){
            log.warn("ошибка ввода файла xlsx");
        }
        log.info(spgzKpgzList.size());
        return spgzKpgzList;
    }

    private SpgzKpgz changeSpgzKpgz(SpgzKpgz spgzKpgz){
        //создаем новую запись
        if(kpgz != null) spgzKpgz.setKpgz(kpgz);
        if(spgz != null) spgzKpgz.setSpgz(spgz);
        if(units != null) spgzKpgz.setUnits(units);
        if(okpd != null) spgzKpgz.setOkpd(okpd);
        if(okpd2 != null) spgzKpgz.setOkpd2(okpd2);
        //проверяем шаблон
        log.debug("changespgzKpgz" + spgzKpgz);
        return spgzKpgz;
    }
}
