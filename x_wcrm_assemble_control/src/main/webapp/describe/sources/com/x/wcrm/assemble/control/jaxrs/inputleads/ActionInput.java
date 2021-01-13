package com.x.wcrm.assemble.control.jaxrs.inputleads;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.annotation.CheckPersistType;
import com.x.base.core.project.annotation.FieldDescribe;
import com.x.base.core.project.gson.GsonPropertyObject;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.base.core.project.tools.DateTools;
import com.x.base.core.project.tools.StringTools;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.core.entity.Leads;

import net.sf.ehcache.Element;

public class ActionInput extends BaseAction {
	//这个功能还没有写完，暂时放下，写其他服务。
	Logger logger = LoggerFactory.getLogger(ActionInput.class);

	private static int SHEET_NUMBER = 0; //读取的数据sheet
	private static int BEGINROW_NUMBER = 1; //起始行

	ActionResult<Wo> execute(EffectivePerson effectivePerson, byte[] bytes, FormDataContentDisposition disposition) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create();
				InputStream is = new ByteArrayInputStream(bytes);
				XSSFWorkbook workbook = new XSSFWorkbook(is);
				ByteArrayOutputStream os = new ByteArrayOutputStream()) {
			Business business = new Business(emc);
			ActionResult<Wo> result = new ActionResult<>();
			String name = "leads_result_" + DateTools.formatDate(new Date()) + ".xlsx";
			//this.scanAndComment(business, workbook);
			this.persist(business, workbook, effectivePerson);
			workbook.write(os);

			CacheInputResult cacheInputResult = new CacheInputResult();
			cacheInputResult.setName(name);
			cacheInputResult.setBytes(os.toByteArray());
			String flag = StringTools.uniqueToken();
			cache.put(new Element(flag, cacheInputResult));

			Wo wo = new Wo();
			wo.setFlag(flag);
			result.setData(wo);
			return result;
		}

	}

	public static class Wo extends GsonPropertyObject {

		@FieldDescribe("返回的结果标识")
		private String flag;

		public String getFlag() {
			return flag;
		}

		public void setFlag(String flag) {
			this.flag = flag;
		}

	}

	private void persist(Business business, XSSFWorkbook workbook, EffectivePerson effectivePerson) throws Exception {
		boolean validate = true;
		logger.info("NumberOfSheet:" + SHEET_NUMBER + ":" + workbook.getSheetName(SHEET_NUMBER));
		XSSFSheet sheet = workbook.getSheetAt(SHEET_NUMBER);
		int lastRowNum = sheet.getLastRowNum();
		Row row = null;
		XSSFComment comment = null;

		String _name = "";
		String _source = "";
		String _cellphone = "";
		String _telephone = "";
		String _industry = "";
		String _province = "";
		String _address = "";
		String _level = "";
		String _remark = "";
		Date _nexttime = new Date();

		for (int i = BEGINROW_NUMBER; i <= lastRowNum; i++) {
			row = sheet.getRow(i);
			Leads leads = new Leads();
			for (Cell cell : row) {
				int ColumnIndex = cell.getColumnIndex();
				if (ColumnIndex == 0) {
					_name = getCellValue(cell);
					leads.setName(_name);
				}

				if (ColumnIndex == 1) {
					_source = getCellValue(cell);
					leads.setSource(_source);
				}

				if (ColumnIndex == 2) {
					_cellphone = getCellValue(cell);
					leads.setCellphone(_cellphone);
				}
				if (ColumnIndex == 3) {
					_telephone = getCellValue(cell);
					leads.setTelephone(_telephone);
				}
				if (ColumnIndex == 4) {
					_industry = getCellValue(cell);
					leads.setIndustry(_industry);
				}

				if (ColumnIndex == 5) {
					_level = getCellValue(cell);
					leads.setLevel(_level);
				}

				if (ColumnIndex == 6) {
					_province = getCellValue(cell);//获取所在地区
					leads.setProvince(_province);
				}

				if (ColumnIndex == 7) {
					_address = getCellValue(cell);
					leads.setAddress(_address);
				}

				if (ColumnIndex == 8) {
					_remark = getCellValue(cell);
					leads.setRemark(_remark);
				}
				if (ColumnIndex == 9) {
					_nexttime = cell.getDateCellValue(); //获取时间。
					leads.setNexttime(_nexttime);
				}

			}

			leadsService.initDefaultValue(effectivePerson, leads);

			EntityManagerContainer emc = business.entityManagerContainer();
			emc.beginTransaction(Leads.class);
			emc.persist(leads, CheckPersistType.all);
			emc.commit();
		}

	}

	String getCellValue(Cell cell) {
		if (cell == null)
			return "";
		if (cell.getCellType() == CellType.STRING) {
			return cell.getStringCellValue();
		} else if (cell.getCellType() == CellType.BOOLEAN) {
			return String.valueOf(cell.getBooleanCellValue());
		} else if (cell.getCellType() == CellType.FORMULA) {
			return cell.getCellFormula();
		} else if (cell.getCellType() == CellType.NUMERIC) {
			DataFormatter formatter = new DataFormatter();
			String _str = formatter.formatCellValue(cell);
			return String.valueOf(_str);
		}
		return "";
	}

}
