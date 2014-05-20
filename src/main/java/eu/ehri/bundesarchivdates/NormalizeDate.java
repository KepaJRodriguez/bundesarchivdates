package eu.ehri.bundesarchivdates;

public class NormalizeDate {

	public static String processDate(String datefield) {
		
		datefield = datefield.replaceAll(" -", "-");
		datefield = datefield.replaceAll("- ", "-");
		datefield = datefield.replaceAll("(19[0-9]{2})-19([0-9]{2})",
				"$1/$2");
		datefield = datefield.replaceAll("(18[0-9]{2})-18([0-9]{2})",
				"$1/$2");
		datefield = datefield.replaceAll("([0-9]{4})-([0-9]{4})",
				"$1/$2");
		
		if (datefield.matches("[0-9]+\\.[0-9]+\\.[0-9]{4}")) {
			String[] splittedDate = datefield.split("\\.");
			String month;
			String day;
			if (splittedDate[0].length() == 1) {
				day = "0" + splittedDate[0];
			} else {
				day = splittedDate[0];
			}
			if (splittedDate[1].length() == 1) {
				month = "0" + splittedDate[1];
			} else {
				month = splittedDate[1];
			}
			datefield = splittedDate[2]+ "-" + month + "-"
					+ day;
		}
		datefield = datefield.replaceAll("([0-9])\\(", "$1 \\(");
		datefield = datefield.replaceAll("\\)([0-9])", "\\) $1");
		

		return datefield;

	}

}
