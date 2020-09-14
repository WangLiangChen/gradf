package liangchen.wang.gradf.framework.commons.utils;


import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public enum PoiUtil {
    /**
     * instance
     */
    INSTANCE;
    private final static String xls = "xls";
    private final static String xlsx = "xlsx";

    public HSSFWorkbook workbook(String[] titles, Iterator<String[]> datas) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = workbook.createSheet("导出数据");
        HSSFRow hssfRow = hssfSheet.createRow(0);
        HSSFCellStyle hssfCellStyle = workbook.createCellStyle();
        hssfCellStyle.setAlignment(HorizontalAlignment.CENTER);
        //创建表头
        HSSFCell hssfCell;
        for (int i = 0; i < titles.length; i++) {
            hssfCell = hssfRow.createCell(i);
            hssfCell.setCellValue(titles[i]);
            hssfCell.setCellStyle(hssfCellStyle);
        }
        //创建内容
        String[] rows;
        int index = 1;
        while (datas.hasNext()) {
            hssfRow = hssfSheet.createRow(index++);
            rows = datas.next();
            for (int i = 0; i < rows.length; i++) {
                hssfRow.createCell(i).setCellValue(rows[i]);
            }
        }
        for (int i = 0; i < titles.length; i++) {
            hssfSheet.autoSizeColumn(i);
        }
        return workbook;
    }

    public List<String[]> readExcel(InputStream inputStream, String fileName) throws IOException {
        Workbook workbook = null;
        if (fileName.endsWith(xls)) {
            //2003
            workbook = new HSSFWorkbook(inputStream);
        } else if (fileName.endsWith(xlsx)) {
            //2007
            workbook = new XSSFWorkbook(inputStream);
        }
        List<String[]> result = new ArrayList<>();
        if (workbook == null) {
            return result;
        }
        for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
            Sheet sheet = workbook.getSheetAt(sheetNum);
            if (sheet == null) {
                continue;
            }
            int firstRowNum = sheet.getFirstRowNum();
            int lastRowNum = sheet.getLastRowNum();
            for (int rowNum = firstRowNum; rowNum <= lastRowNum; rowNum++) {
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    continue;
                }
                int firstCellNum = row.getFirstCellNum();
                int lastCellNum = row.getPhysicalNumberOfCells();
                String[] cells = new String[row.getPhysicalNumberOfCells()];
                //循环当前行
                for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                    Cell cell = row.getCell(cellNum);
                    cells[cellNum] = null == cell ? "" : cell.toString();
                }
                result.add(cells);
            }
        }
        return result;
    }

}
