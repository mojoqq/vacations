package bgdn.vrbv.prjct.config;

import bgdn.vrbv.prjct.entity.Employee;
import bgdn.vrbv.prjct.entity.Vacation;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class EmployeeVacationsToExcelConfig {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Employee> employeeList;

    public EmployeeVacationsToExcelConfig(List<Employee> employeeList) {
        this.employeeList = employeeList;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Сотрудники");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "ФИО", style);
        createCell(row, 1, "Начало отпуска", style);
        createCell(row, 2, "Конец отпуска", style);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
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
        for (Employee user : employeeList) {
            rowCount = rowCount + 1;
            Row row = sheet.createRow(rowCount);
            createCell(row, 0,user.getLastname() + " " + user.getFirstname() + " " + user.getMiddlename(), style);
            for (int i = 0; i < user.getVacations().size(); i++) {
                int rownum = rowCount + 1;
                Row row1 = sheet.createRow(rownum);
                Vacation vacation = user.getVacations().get(i);
                createCell(row1, 1, vacation.getStartDate().toString(), style);
                createCell(row1, 2, vacation.getEndDate().toString(), style);
                rowCount++;
            }
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
