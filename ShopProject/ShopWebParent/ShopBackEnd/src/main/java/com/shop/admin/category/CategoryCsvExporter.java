package com.shop.admin.category;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shop.admin.user.export.AbstractExporter;
import com.shop.common.entities.Category;

public class CategoryCsvExporter extends AbstractExporter {
	public void export(List<Category> listCategory, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "text	/csv", ".csv", "categories_");
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeeader = { "Category ID", "Category Name" };
		String[] fieldsMapping = { "id", "name" };
		csvWriter.writeHeader(csvHeeader);
		for (Category category : listCategory) {
			category.setName(category.getName().replace("--", "  "));
			csvWriter.write(category, fieldsMapping);
		}
		csvWriter.close();

	}
}
