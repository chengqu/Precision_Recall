package agreement;

import java.util.ArrayList;

public class BracketingUtils {

	public static String combinedURL = "c6-np-vp-tree";
	public static String user1 = "T";
	public static String user2 = "p";
	
	public static void main(String[] args) {
		ArrayList<String> lines = FileUtils.contentsOfTheFileAsList(combinedURL);
		
		ArrayList<String> lines1 = new ArrayList<>();
		ArrayList<String> lines2 = new ArrayList<>();
		fillUp(lines, lines1, lines2, user1, user2);
		
		/*String s1 = "[yellow eyes] big yellow [eyes]";
		String s2 = "[yellow eyes] big [yellow eyes]";
		double distancePerc = getLevenshteinMatching(s1, s2, false);
		System.out.println(distancePerc);*/
		double sum = 0.0;
		for (int i=0; i < lines1.size(); i++) {
			String user1Line = lines1.get(i);
			String user2Line = lines2.get(i);
			double distancePerc = getLevenshteinMatching(user1Line, user2Line, false);
			sum += distancePerc;
			System.out.println("DISTANCE PERCENTAGE : " + distancePerc);
		}
		System.out.println("======================");
		System.out.println("Percent distance: "+sum/(double)lines1.size());
		
	}
	
	
	public static double getLevenshteinMatching(String line1, String line2, boolean perc) {
		double percentageMatching = 0.0;
		
		int dist = LevenshteinDistance.computeLevenshteinDistance(line1, line2);
		int max = Math.max(line1.length(), line2.length());
		if (perc) {
		percentageMatching = (double)dist/(double)max;
		} else {
			percentageMatching = dist;
		}
		return percentageMatching;
	}
	
	public static void fillUp(ArrayList<String> lines, ArrayList<String> lines1,
			ArrayList<String> lines2, String u1, String u2) {
		int indexNow = 0;
		for (String line: lines) {
			if (indexNow == 87) {
				//System.out.println("here");
			}
			if (line.length() > 1 && line.contains(":") && !line.contains("#")) {
				String from = line.split("\\:")[0];
				String annt = line.split("\\:")[1];
				if (from.equals(u1)) {
					//System.out.println((indexNow++) + " <-> " + line);
					lines1.add(annt);
				} else if (from.equals(u2)){
					//System.out.println((indexNow++) + " <-> " + line);
					lines2.add(annt);
				}
			}
		}
	}
		
}
