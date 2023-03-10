package it.bitrock.bitrockairways.utility;

import it.bitrock.bitrockairways.dto.CustomerFidelityDataDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ExcelWriter {
    public static void writeCustomersToExcel(List<CustomerFidelityDataDTO> customers, Path filePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Most Fidelity Customers");

        Row headerRow = sheet.createRow(0);
        Cell customerIdHeaderCell = headerRow.createCell(0);
        customerIdHeaderCell.setCellValue("Customer ID");
        Cell nameHeaderCell = headerRow.createCell(1);
        nameHeaderCell.setCellValue("Name");
        Cell surnameHeaderCell = headerRow.createCell(2);
        surnameHeaderCell.setCellValue("Surname");
        Cell emailHeaderCell = headerRow.createCell(3);
        emailHeaderCell.setCellValue("Email");
        Cell totalPointsHeaderCell = headerRow.createCell(4);
        totalPointsHeaderCell.setCellValue("Total Points");

        for (int i = 0; i < customers.size(); i++) {
            CustomerFidelityDataDTO customer = customers.get(i);
            Row row = sheet.createRow(i + 1);

            Cell customerIdCell = row.createCell(0);
            customerIdCell.setCellValue(customer.getCustomerId());
            Cell nameCell = row.createCell(1);
            nameCell.setCellValue(customer.getName());
            Cell surnameCell = row.createCell(2);
            surnameCell.setCellValue(customer.getSurname());
            Cell emailCell = row.createCell(3);
            emailCell.setCellValue(customer.getEmail());
            Cell totalPointsCell = row.createCell(4);
            totalPointsCell.setCellValue(customer.getTotalPoints());
        }

        Path parentPath = filePath.getParent();
        if (parentPath != null && !Files.exists(parentPath)) {
            Files.createDirectories(parentPath);
        }

        try (var outputStream = Files.newOutputStream(filePath)) {
            workbook.write(outputStream);
        } finally {
            workbook.close();
        }
    }
}
