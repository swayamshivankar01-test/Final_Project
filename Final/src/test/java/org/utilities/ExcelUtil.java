package org.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelUtil {
    public static Workbook book;
    public static Sheet sheet;

    public static Object[][] getExcelData(String specificSheet) 
            throws EncryptedDocumentException, IOException {
        String path = System.getProperty("user.dir") + 
                "/src/test/resources/TestData/TestData.xlsx";
        FileInputStream fis = new FileInputStream(path);
        book = WorkbookFactory.create(fis);
        sheet = book.getSheet(specificSheet);

        int totalRows = sheet.getLastRowNum();
        int totalColumns = sheet.getRow(0).getLastCellNum();

        Object[][] obj = new Object[totalRows][totalColumns];
        for (int i = 0; i < obj.length; i++) {
            for (int j = 0; j < obj[0].length; j++) {
                obj[i][j] = sheet.getRow(i + 1).getCell(j).toString();
            }
        }
        return obj;
    }
}