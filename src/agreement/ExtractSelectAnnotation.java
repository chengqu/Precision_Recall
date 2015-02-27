package agreement;

import java.util.ArrayList;

public class ExtractSelectAnnotation {

	public static void main(String[] args) {
		ArrayList<String> lines = FileUtils.contentsOfTheFileAsList(
				"maike-numbers");
		/*ArrayList<String> lines1 = new ArrayList<>();
		ArrayList<String> lines2 = new ArrayList<>();		
		BracketingUtils.fillUp(lines, lines1, lines2, "m", "M");

		for (String line: lines1) {
			FileUtils.writeToFileAlreadyExisting("maike", line);
		}*/

		System.out.println("1abcda1gh".split("1")[1]);
		String[] regexSplits = new String[]{
				"1", "2", "3", "4", "5", "6", "7", "8", "9", 
				"\\@", "\\#", "\\$", "\\%", "\\^", "\\*", "\\+", 
				"\\|", "\\\\", "\\:", "\\=", "\\?", "\\!", "\\-"
		};

		ArrayList<ArrayList<String>> chunkings = new ArrayList<>();

		for (String line: lines) {
			System.out.println("=====");
			for (String reg: regexSplits) {
				String[] chunks = line.split(reg);
				if (chunks.length > 1) {
					for (int i=1; i < chunks.length; i = i+2) {
						String toPrint =chunks[i];
						for (String r: regexSplits) {
							toPrint= toPrint.replaceAll(r, "");							
						}
						System.out.println(toPrint);
					}
				}
			}

			System.out.println("=====");
		}
	}

	public static ArrayList<String> splitCrossBracketings(String bracketedSentence) {
		String[] regexSplits = new String[] {
				"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", 
				"\\@", "\\>", "\\$", "\\%", "\\^", "\\*", "\\+", 
				"\\|", "\\\\", "\\<", "\\=", "\\?", "\\!", "\\-", 
				"\\}"
		};
		ArrayList<String> sentences = new ArrayList<>();
		
		for (String reg: regexSplits) {
			String[] chunks = bracketedSentence.split(reg);
			if (chunks.length > 1) {
				for (int i=1; i < chunks.length; i = i+2) {
					String toPrint =chunks[i];
					for (String r: regexSplits) {
						toPrint= toPrint.replaceAll(r, "");							
					}
					System.out.println(toPrint);
					sentences.add(toPrint);
				}
			}
		}
		
		return sentences;
	}
	
	public static String parseCrossBracketings(String bracketedSentence) {
		String[] regexSplits = new String[] {
				"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", 
				"\\@", "\\>", "\\$", "\\%", "\\^", "\\*", "\\+", 
				"\\|", "\\\\", "\\<", "\\=", "\\?", "\\!", "\\-", 
				"\\}"
		};
		String sentence = bracketedSentence;
		
		for (String reg: regexSplits) {
			sentence = sentence.replaceAll(reg, "");
		}
		return sentence;
	}
}
