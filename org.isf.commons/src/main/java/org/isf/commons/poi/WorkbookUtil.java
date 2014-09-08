package org.isf.commons.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.isf.commons.IOUtil;

public class WorkbookUtil {

	private Workbook workbook;
	private Sheet sheet;
	
	public WorkbookUtil() {
		setWorkbook(new XSSFWorkbook());
	}
	
	public WorkbookUtil(Workbook wb) {
		setWorkbook(wb);		
	}
	
	public WorkbookUtil(InputStream in) throws IOException {
		this(new XSSFWorkbook(in));
	}
	
	public WorkbookUtil(String filename) throws IOException {
		this(new File(filename));
	}
	
	public WorkbookUtil(File file) throws IOException {
		if ("xlsx".equalsIgnoreCase(IOUtil.extractFileExt(file)) || "xlsm".equalsIgnoreCase(IOUtil.extractFileExt(file)))
			setWorkbook(new XSSFWorkbook(new FileInputStream(file)));
		else setWorkbook(new HSSFWorkbook(new FileInputStream(file)));
//            setWorkbook(new HSSFWorkbook(new FileInputStream(file)));
	}
	
	public Workbook getWorkbook() { return workbook; }
	protected void setWorkbook(Workbook workbook) { this.workbook = workbook; }
	
	public Sheet getCurrentSheet()  {
		try {
			if (sheet == null) sheet = getWorkbook().getSheetAt(0);
		} catch(Exception e) {
			sheet = getWorkbook().createSheet();
		}
		return sheet;
	}
	
	public Sheet createSheet(String name) {
		sheet = getWorkbook().createSheet(name);
		return sheet;
	}
	
	public Sheet getSheet(String name) {
		sheet = getWorkbook().getSheet(name);
		return sheet;
	}
	
	public Sheet getSheet(int index) {
		sheet = getWorkbook().getSheetAt(index);
		return sheet;
	}
	
	public void selectSheet(String name) { getSheet(name); }
	public void selectSheet(int index) { getSheet(index); }
	public void selectSheet(Sheet sheet) { this.sheet = sheet; }
	
	public int sheetCount() { return getWorkbook().getNumberOfSheets(); }
	
	public int getFirstRowNum() { return (getCurrentSheet().getFirstRowNum() < 0) ? 0 : getCurrentSheet().getFirstRowNum(); }
	public int getLastRowNum() { return (getCurrentSheet().getLastRowNum() < 0) ? getFirstRowNum() : getCurrentSheet().getLastRowNum(); }
	
	public boolean rowExists(int row) {
//		return getCurrentSheet().getRow(row) != null;
		return row >= getFirstRowNum() && row <= getLastRowNum();
	}
	
	public boolean cellExists(int row, int column) {
		if(!rowExists(row)) return false;
		Row r = getRow(row);
		Cell c = r.getCell(column);
		return c != null;
//		return r.getFirstCellNum() >= column && r.getLastCellNum() <= column;
//		return 0 >= column && r.getLastCellNum() <= column;
	}
	
	public boolean cellExists(int[] coord) {
		return cellExists(coord[0], coord[1]);
	}
	
	public Row getRow(int row) {
		Row r = getCurrentSheet().getRow(row);
		if (r == null) r = getCurrentSheet().createRow(row);
		return r;
	}
	
	public Cell getCell(int row, int column) {
		Row r = getRow(row);
		Cell c = r.getCell(column);
		if (c == null) c = r.createCell(column);
		return c;
	}
	
	public Cell getCell(int[] coords) {
		return getCell(coords[0], coords[1]);
	}
	
	public String getStringValue(int[] coord) {
		return getStringValue(coord[0], coord[1]);
	}
	
	public String getStringValue(int row, int column) {
		String ret = null;
		try {
			Cell cell = getCell(row, column);
			switch (cell.getCellType()) {
				case Cell.CELL_TYPE_BOOLEAN: ret = String.valueOf(cell.getBooleanCellValue()); break;
				case Cell.CELL_TYPE_NUMERIC: ret = String.valueOf(cell.getNumericCellValue()); break;
				case Cell.CELL_TYPE_STRING: ret = cell.getStringCellValue(); break;
			}
		} catch(NullPointerException npe) {
			return null;
		}
		return ret;
	}
	
	public void setStringValue(String value, int[] coord) {
		setStringValue(value, coord[0], coord[1]);
	}
	
	public void setStringValue(String value, int row, int column) {
		getCell(row, column).setCellValue(value);
	}
	
	public Integer getIntegerValue(int[] coord, boolean force) {
		return getIntegerValue(coord[0], coord[1]);
	}
	
	public Integer getIntegerValue(int row, int column, boolean force) {
		if (force) return getIntegerValueForce(row, column);
		else {
			try {
				return getDoubleValue(row, column).intValue();
			} catch(NullPointerException npe) {
				return null;
			}
		}
	}
	
	public Integer getIntegerValue(int[] coord) {
		return getIntegerValue(coord[0], coord[1],false);
	}
	
	public Integer getIntegerValue(int row, int column) {
		return getIntegerValue(row, column, false);
	}
	
	public Integer getIntegerValueForce(int[] coords) {
		return getIntegerValueForce(coords[0], coords[1]);
	}
	
	public Integer getIntegerValueForce(int row, int column) {
		Integer ret = null;
		try {
			ret = getIntegerValue(row, column);
		} catch(Exception ex) {
			try {
				ret = Integer.valueOf(getStringValue(row, column).trim());
			} catch(Exception e) {
				// DO NOTHING :/
			}
		}
		return ret;
	}
	
	public void setIntegerValue(Integer value, int[] coord) {
		setIntegerValue(value, coord[0], coord[1]);
	}
	
	public void setIntegerValue(Integer value, int row, int column) {
		getCell(row, column).setCellValue(value);
	}
	
	public Long getLongValue(int[] coord) {
		return getLongValue(coord[0], coord[1]);
	}
	
	public Long getLongValue(int row, int column) {
		try {
			return getDoubleValue(row, column).longValue();
		} catch(NullPointerException npe) {
			return null;
		}
	}
	
	public void setLongValue(Long value, int[] coord) {
		setLongValue(value, coord[0], coord[1]);
	}
	
	public void setLongValue(Long value, int row, int column) {
		getCell(row, column).setCellValue(value);
	}
	
	public Double getDoubleValue(int[] coord) {
		return getDoubleValue(coord[0], coord[1]);
	}
	
	public Double getDoubleValue(int row, int column) {
		Cell cell = getCell(row, column);
		try {
			return cell.getNumericCellValue();
		} catch(NullPointerException npe) {
			return null;
		}
	}
	
	public void setDoubleValue(Double value, int[] coord) {
		setDoubleValue(value, coord[0], coord[1]);
	}
	
	public void setDoubleValue(Double value, int row, int column) {
		getCell(row, column).setCellValue(value);
	}
	
	public Date getDateValue(int[] coord) {
		return getDateValue(coord[0], coord[1]);
	}
	
	public Date getDateValue(int row, int column) { 
		return getCell(row, column).getDateCellValue();
	}
	
	public void setDateValue(Date value, int[] coord) {
		setDateValue(value, coord[0], coord[1]);
	}
	
	public void setDateValue(Date value, int row, int column) {
		getCell(row, column).setCellValue(value);
	}
	
	public Object getValue(int[] coord) {
		return getValue(coord[0], coord[1]);
	}
	public Object getValue(int row, int column) {
		Cell c = getCell(row, column);
		switch (c.getCellType()) {
			case Cell.CELL_TYPE_BOOLEAN: return new Boolean(c.getBooleanCellValue());
			case Cell.CELL_TYPE_NUMERIC: return new Double(c.getNumericCellValue());
			case Cell.CELL_TYPE_ERROR: 
			case Cell.CELL_TYPE_BLANK: return null;
			default: 
			try {
				String ret = c.getStringCellValue();
				return "null".equalsIgnoreCase(ret) ? null : ret;
			} catch(IllegalStateException iae) {
				return null;
			}
		}
	}
	public void setValue(Object value, int[] coord) {
		setValue(value, coord[0], coord[1]);
	}
	public void setValue(Object value, int row, int column) {
		Cell c = getCell(row, column);
		if (value == null) c.setAsActiveCell();
		else if (value instanceof Boolean) c.setCellValue(((Boolean)value).booleanValue());
		else if (value instanceof Date) c.setCellValue((Date)value);
		else if (value instanceof Double) c.setCellValue(((Double)value).doubleValue());
		else if (value instanceof String) c.setCellValue((String)value);
		else if (value instanceof Integer) c.setCellValue(new Double(((Integer)value).doubleValue()));
		else if (value instanceof Short) c.setCellValue(new Double(((Short)value).doubleValue()));
		else if (value instanceof Byte) c.setCellValue(new Double(((Byte)value).doubleValue()));
		else if (value instanceof Long) c.setCellValue(new Double(((Long)value).doubleValue()));
		else if (value instanceof Float) c.setCellValue(new Double(((Float)value).doubleValue()));
		else {
//			System.out.println("value: "+value);
//			try { System.out.println("class: "+value.getClass().getName()); } catch(Exception e) {}
			throw new IllegalArgumentException();
		}
	}
	
	public void write(OutputStream out) throws IOException {
		getWorkbook().write(out);
	}
	
}
