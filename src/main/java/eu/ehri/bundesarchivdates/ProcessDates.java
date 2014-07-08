package eu.ehri.bundesarchivdates;

import java.util.HashMap;

public class ProcessDates {

		
		public static HashMap<Integer,String> splitDate(String datefield){
			HashMap<Integer,String> original = new HashMap<Integer,String>(); 
			datefield = datefield.replaceAll(" -", "-");
			datefield = datefield.replaceAll("- ", "-");
			
			String[] splitted = datefield.split(" ");
			int cnt = 0;
			for (String item : splitted){
				
				original.put(cnt, item);
				cnt++;
			}			
			return original;
		}
	
		public static HashMap<Integer,String> normalizeDate (HashMap<Integer,String> rawdates){
			
			HashMap<Integer,String> normalized = new HashMap<Integer,String>();
			
			for (int key : rawdates.keySet()){
				String newdate;
				newdate = rawdates.get(key).replaceAll("([0-9]{4})-([0-9]{4})",
						"$1/$2");
				
			
				if (newdate.matches("[0-9]+\\.[0-9]+\\.[0-9]{4}")) {
					String[] splittedDate = newdate.split("\\.");
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
					newdate = splittedDate[2]+ "-" + month + "-"
							+ day;
				}
				newdate = newdate.replaceAll("([0-9])\\(", "$1 \\(");
				newdate = newdate.replaceAll("\\)([0-9])", "\\) $1");
				newdate = newdate.replaceAll(" bis ", "/");
				newdate = newdate.replaceAll("Ca. |ca. |Anfang ", "");
				newdate = newdate.replaceAll("August ", "08/");
				newdate = newdate.replaceAll("19. Jh.", "1800");
				newdate = newdate.replaceAll("17. Jh.", "1600");
				newdate = newdate.replaceAll("o. Dat.", "0000");
				newdate = newdate.replaceAll(",", "0000");
				newdate = newdate.replaceAll("\\(", "");
				newdate = newdate.replaceAll("\\)", "");
				
				normalized.put(key, newdate);
				
				
			}
			
			
			
			
			
			return normalized;
		}
		
		
	
//
//		return datefield;
//
//	}

}
