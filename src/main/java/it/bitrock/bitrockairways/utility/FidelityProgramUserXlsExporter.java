package it.bitrock.bitrockairways.utility;

import it.bitrock.bitrockairways.model.dto.FidelityProgramUserDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class FidelityProgramUserXlsExporter {
    private final String filePath;
    private final XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private final List<FidelityProgramUserDTO> usersList;

    public FidelityProgramUserXlsExporter(String filePath, List<FidelityProgramUserDTO> usersList) {
        this.filePath = filePath;
        this.usersList = usersList;
        workbook = new XSSFWorkbook();
    }


    private void writeHeaderLine() {
        sheet = workbook.createSheet("Fidelity Program Users");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "Name", style);
        createCell(row, 1, "Surname", style);
        createCell(row, 2, "E-mail", style);
        createCell(row, 3, "Total Points", style);

    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (FidelityProgramUserDTO user : usersList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, user.getName(), style);
            createCell(row, columnCount++, user.getSurname(), style);
            createCell(row, columnCount++, user.getEmail(), style);
            createCell(row, columnCount++, user.getTotalPoints(), style);

        }
    }

    public void export() throws IOException {
        writeHeaderLine();
        writeDataLines();

        FileOutputStream out = new FileOutputStream(filePath); // file name with path
        workbook.write(out);
        out.close();

    }
}
