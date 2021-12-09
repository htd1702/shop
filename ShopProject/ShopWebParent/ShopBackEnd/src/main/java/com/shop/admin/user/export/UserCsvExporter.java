package com.shop.admin.user.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shop.admin.AbstractExporter;
import com.shop.common.entities.User;

public class UserCsvExporter extends AbstractExporter {
	public void export(List<User> listUsers, HttpServletResponse response) throws IOException {

		super.setResponseHeader(response, "text/csv", ".csv");
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeeader = { "User ID", "E-mail", "First Name", "Last Name", "Roles", "Enabled" };
		String[] fieldsMapping = { "id", "email", "firstName", "lastName", "roles", "enabled" };
		csvWriter.writeHeader(csvHeeader);
		for (User user : listUsers) {
			csvWriter.write(user, fieldsMapping);
		}
		csvWriter.close();

	}
}
