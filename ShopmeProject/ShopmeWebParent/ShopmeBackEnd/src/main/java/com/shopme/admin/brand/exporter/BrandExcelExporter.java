package com.shopme.admin.brand.exporter;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.shopme.admin.AbstractExporter;
import com.shopme.common.entity.Brand;

public class BrandExcelExporter extends AbstractExporter{
	
	private XSSFWorkbook workbook = new XSSFWorkbook();
	private XSSFSheet sheet;
	
	public BrandExcelExporter() {
		workbook = new XSSFWorkbook();
	}
	
	private void writeHeaderLine() {
		sheet = workbook.createSheet("Brands");
		XSSFRow row = sheet.createRow(0);
		
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(14);
		cellStyle.setFont(font);
		
		createCell(row, 0, "Brand Id", cellStyle);
		createCell(row, 1, "Brand Name", cellStyle);
		createCell(row, 2, "Categories", cellStyle);
	}
	
	private void createCell(XSSFRow row, int columnIndex, Object value, CellStyle style) {
		XSSFCell cell = row.createCell(columnIndex);
		sheet.autoSizeColumn(columnIndex);
		
		if(value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else {
			cell.setCellValue((String) value);
		}
		
		cell.setCellStyle(style);
	}
	
	private void writeDataLines(List<Brand> listBrands) {
		int rowIndex = 1;
		
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(10);
		cellStyle.setFont(font);
		
		for(Brand brand : listBrands) {
			XSSFRow row = sheet.createRow(rowIndex++);
			int columnIndex = 0;
			createCell(row, columnIndex++, brand.getId(), cellStyle);
			createCell(row, columnIndex++, brand.getName(), cellStyle);
			createCell(row, columnIndex++, brand.getCategories().toString(), cellStyle);
		}
	}
	
	public void export(List<Brand> listBrands, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, ".application/octet-stream", ".xlsx", "brands_");
		
		writeHeaderLine();
		
		writeDataLines(listBrands);
		
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
		
		
	}
}
