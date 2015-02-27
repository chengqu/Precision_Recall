package agreement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class ParseEval {

	public static String combinedURL = "c6-cross-brackets-2-tree";
	public static int SIZE_LINE_MAX = 1000;

	public static String resFileTemp = "comparisons-temp";
	public static String absentFileTemp = "absentees";

	public static int totalCorrectAll = 0;
	public static int totalTarsAll = 0;
	public static int totalRefsAll = 0;

	public static void main(String[] args) {
		//String X = "[[yellow eyes] [big [yellow eyes]]]";
		String p = "41yellow eyes1 2big yellow eyes24";
		String X = "1yellow eyes1 2big 1yellow eyes12";
		//String X = "mostly uh 78mostly 64solid 53green background347 2with 1orange eyes12568";
		//String p = "40mostly uh mostly 52solid green02 1background 3with 6orange eyes65314";
		Double precision = 0.0;
		Double recall = 0.0;
		Double crossings = 0.0;

		String val = parsevalCrossBracketings(p, X, precision, recall, crossings);
		System.out.println(val);
	}


	//	public static void main1(String[] args) {
	//		ArrayList<String> lines = FileUtils.contentsOfTheFileAsList(combinedURL);
	//		FileUtils.writeToFileNew(resFileTemp, "");
	//		FileUtils.writeToFileNew(absentFileTemp, "");
	//
	//		ArrayList<String> lines1 = new ArrayList<>();
	//		ArrayList<String> lines2 = new ArrayList<>();
	//		String refLabel = "C";
	//		String tarLabel = "x";
	//		BracketingUtils.fillUp(lines, lines1, lines2, refLabel, tarLabel);
	//
	//		Double precision = 0.0;
	//		Double recall = 0.0;
	//		Double crossings = 0.0;
	//
	//		System.out.println(lines1.size());
	//		System.out.println(lines2.size());
	//
	//		int lessSize = Math.min(lines1.size(), lines2.size());
	//
	//		double totalPrecision = 0.0;
	//		double totalRecall = 0.0;
	//		double totalBracketedPrecision = 0.0;
	//		double totalBracketedRecall = 0.0;
	//
	//		int precLen = 0;
	//		int recLen = 0;
	//		int braPrecLen = 0;
	//		int braRecLen = 0;
	//		int totNum = 0;
	//		for (int i=0; i < lessSize; i++) {
	//			String line1 = lines1.get(i).trim().replaceAll("\\;", "").replaceAll("um", "").replaceAll("xxx", "").replaceAll("uh", "").
	//					replaceAll("  ", " ").replaceAll("\\;", "").replaceAll("\\[ ", "\\[").replaceAll("\\]\\[", "\\] \\[").trim();
	//			String line2 = lines2.get(i).trim().replaceAll("\\;", "").replaceAll("um", "").replaceAll("  ", " ").replaceAll("\\;", "").
	//					replaceAll("\\[ ", "\\[").replaceAll("\\]\\[", "\\] \\[").trim();
	//			String val = "";
	//			/*if (line1.contains("click") || 
	//					(line1.contains("diagonal") && line1.contains("right") && line1.contains("top"))
	//					|| line1.contains("nevermind")){
	//				continue;
	//			}*/
	//			if (refLabel.equals("C")) {
	//				if (line1.length()< SIZE_LINE_MAX) {
	//					totNum++;
	//					if (line1.contains("white one")) {
	//						System.out.println();
	//					}
	//					val = parsevalCrossBracketings(line1, line2, precision, recall, crossings);					
	//				} else {
	//					continue;
	//				}
	//			} else {
	//				val = parseval(line1, line2, precision, recall, crossings);
	//			}
	//			if (!val.split("\\:")[0].equals("NaN") && !val.split("\\:")[1].equals("NaN")
	//					//&& !val.split("\\:")[2].equals("NaN") && !val.split("\\:")[3].equals("NaN")
	//					) {
	//				Double precisionNow = Double.parseDouble(val.split("\\:")[0]);
	//				totalPrecision += precisionNow;
	//				precLen++;
	//
	//				Double recallNow = Double.parseDouble(val.split("\\:")[1]);
	//				totalRecall += recallNow;
	//				recLen++;
	//				/*System.out.println(line1);
	//				System.out.println(line2);*/
	//				Double bracketedIntersectionPrecision = 
	//						Double.parseDouble(val.split("\\:")[2]);
	//				totalBracketedPrecision+=bracketedIntersectionPrecision;
	//				braPrecLen++;
	//
	//				Double bracketedIntersectionRecall = 
	//						Double.parseDouble(val.split("\\:")[3]);
	//				totalBracketedRecall+=bracketedIntersectionRecall;
	//				braRecLen++;
	//				System.out.println(refLabel+" : "+line1);
	//				System.out.println(tarLabel+" : "+line2);
	//				System.out.println(precisionNow+"\t"+recallNow+"\t"+totNum);
	//				//System.out.println(bracketedIntersectionPrecision+"\t"+bracketedIntersectionRecall);
	//			}
	//		}
	//
	//		System.out.println("Precision : " + totalPrecision/(double)precLen);
	//		System.out.println("Recall : " + totalRecall/(double)recLen);
	//
	//		System.out.println("Final Precision : "+ (double)totalCorrectAll/(double)totalTarsAll);
	//		System.out.println("Final Recall : "+ (double)totalCorrectAll/(double)totalRefsAll);
	//
	//		System.out.println("Bracketed Precision Avg: " + totalBracketedPrecision/(double)braPrecLen);
	//		System.out.println("Bracketed Recall Avg: " + totalBracketedRecall/(double)braRecLen);
	//
	//		System.out.print(SIZE_LINE_MAX);
	//		System.out.print("\t");
	//		System.out.print((double)totalCorrectAll/(double)totalTarsAll);
	//		System.out.print("\t");
	//		System.out.print((double)totalCorrectAll/(double)totalRefsAll);
	//		System.out.print("\t");
	//		System.out.print(totNum);
	//	}


	private static LinkedHashSet<String> tarChunks = new LinkedHashSet<>();
	private static LinkedHashSet<String> refChunks = new LinkedHashSet<>();
	private static boolean DONLUFILTER = false;
	public static String parsevalCrossBracketings(String crossbracketed, String target, Double precision, 
			Double recall, Double crossings) {
		String[] wordsRef = crossbracketed.split(" ");
		String[] wordsTar = target.split(" ");

		if(target.contains("[looking to the right with [[[bright orange eyes]]")) {
			System.out.println("--");
		}

		HashMap<String, Boolean[]> geMapsRef = new HashMap<>();
		HashMap<String, Boolean[]> geMapsTar = new HashMap<>();
		int lengthExpected = Math.max(wordsRef.length, wordsTar.length);
		int minLen = Math.min(wordsRef.length, wordsTar.length);
		/*if (wordsRef.length != wordsTar.length) {
			return "ERROR";
		}*/
		//String original = target.replaceAll("\\[", "").replaceAll("\\]", "");
		//tarChunks = new LinkedHashSet<>();
		String original = ExtractSelectAnnotation.parseCrossBracketings(target);

		ArrayList<String> chunks1 = ExtractSelectAnnotation.splitCrossBracketings(crossbracketed);
		ArrayList<String> chunks2 = ExtractSelectAnnotation.splitCrossBracketings(target);
		ArrayList<String> referenceChunks =  new ArrayList<String>(chunks1);
		// Create the correct list
		HashMap<String, Integer> chunksToNumberOfTimesSeen = new HashMap<String, Integer>();
		for (String tempC1: chunks1) {
			Integer value = chunksToNumberOfTimesSeen.get(tempC1);
			if (value == null) {
				value = 1;
			} else {
				value++;
			}
			chunksToNumberOfTimesSeen.put(tempC1, value);
		}

		for (String otherChunk: chunks2) {
			Integer value = chunksToNumberOfTimesSeen.get(otherChunk);
			if (value == null || value == 0) {
				referenceChunks.add(otherChunk);
			} else {
				value--;
				chunksToNumberOfTimesSeen.put(otherChunk, value);
			}
		}


		String[] chunksArray1 = new String[chunks1.size()];
		chunksArray1 = chunks1.toArray(chunksArray1);
		String[] chunksArray2 = new String[chunks2.size()];
		chunksArray2 = chunks2.toArray(chunksArray2);

		geMapsRef = fillMapForCrossBracketings(chunksArray1, referenceChunks);
		geMapsTar = fillMapForCrossBracketings(chunksArray2, referenceChunks);
		//printMap(geMapsTar);
		//printMap(geMapsRef);
		//		ArrayList<Boolean[]> listsRef = new ArrayList<>();
		//		ArrayList<Boolean[]> listsTar = new ArrayList<>();

		//		for (int i=0; i < minLen; i++) {
		//			String wordTarNow = wordsTar[i];
		//
		//			if (wordTarNow.contains("[")) {
		//				bracketingBegins(wordTarNow, 
		//						i, lengthExpected, listsTar);
		//			}
		//
		//			if(wordTarNow.contains("]")) {
		//				listsTar = bracketingEnds(wordTarNow, 
		//						i, listsTar, geMapsTarTemp);
		//			}
		//
		//			if (!wordTarNow.contains("[") && 
		//					!wordTarNow.contains("]")) {
		//				updateBracketMaps(wordTarNow, i, lengthExpected, 
		//						listsTar);
		//			}
		//
		//		}

		// This part of the code filters the chunks off based on the NLU scores.
		/*		HashMap<String, Boolean[]> geMapsTar = new HashMap<>();
		String[] words = tarchunksArray; //= original.split(" ");
		if (DONLUFILTER ) {

			for (String tarKey: geMapsTarTemp.keySet()) {
				Boolean[] targetBrackets = geMapsTarTemp.get(tarKey);
				String wordNowForNLU = "";
				for (int i=0; i < targetBrackets.length; i++) {
					Boolean b = targetBrackets[i];					
					if (b) {
						wordNowForNLU = wordNowForNLU + words[i] + " "; 
					}					
				}
				wordNowForNLU = wordNowForNLU.trim();

			}
		} else {
			geMapsTar = geMapsTarTemp;
		}
		System.out.println("===============");
		printMap(geMapsRef);
		System.out.println("===============");
		printMap(geMapsTar);

		for (String key: geMapsTar.keySet()) {
			String chunkTarNow = "";
			Boolean[] wordsHere = geMapsTar.get(key);
			int indexNowHere = 0;
			for (Boolean b: wordsHere) {
				if (b) {
					chunkTarNow += words[indexNowHere]+" ";
				}
				indexNowHere++;
			}
			tarChunks.add(chunkTarNow);
		}

		int sizeChunks = chunks.size();
		int sizeTarChunks = tarChunks.size();
		ArrayList<String> tempTarChunks = new ArrayList<>(tarChunks);
		int maxSizeChunks = Math.max(sizeTarChunks, sizeChunks);
		 */
		FileUtils.writeToFileAlreadyExisting(resFileTemp, "---------------");
		//precision = getPrecision(geMapsRef, geMapsTar, tarChunks.size());
		//recall = getRecall(geMapsRef, geMapsTar, (new HashSet<>(referenceChunks)).size());
		precision = getPrecision(chunks1, chunks2);
		recall = getRecall(chunks1, chunks2);
		//Double precisionBracketed = getPrecisionUnmatchedCrossBracketings(geMapsRef, geMapsTar, minLen);
		//Double recallBracketed = getRecallUnmatchedCrossBracketings(geMapsRef, geMapsTar, minLen);

		/*Double precisionBracketed = 0.0;
		Double recallBracketed = 0.0;

		for (int i=0; i < referenceChunks.size(); i++) {
			if (i == 0) {
				FileUtils.writeToFileAlreadyExisting(resFileTemp, 
						"P="+precision + "\tR="+ recall);
				FileUtils.writeToFileAlreadyExisting(resFileTemp, 
						original);
			}*/
			/*if (i > sizeChunks-1) {
				FileUtils.writeToFileAlreadyExisting(resFileTemp, " " + "\t" + tempTarChunks.get(i));
			} else if (i > sizeTarChunks-1) {
				FileUtils.writeToFileAlreadyExisting(resFileTemp, chunks.get(i) + " " + "\t" + " ");
			} else {
				FileUtils.writeToFileAlreadyExisting(resFileTemp, chunks.get(i) + "\t" + tempTarChunks.get(i));
			}*/
		//}

		/*FileUtils.writeToFileAlreadyExisting(absentFileTemp, 
				"ORIGINAL:"+original);
		for (String t: tarChunks) {
			boolean present = false;
			for (String chunk: referenceChunks){
				if (chunk.trim().toUpperCase().equals(
						t.trim().toUpperCase())) {
					present = true;
				}				
			}
			if (!present) {
				FileUtils.writeToFileAlreadyExisting(absentFileTemp, "->"+t);
			}
		}
		FileUtils.writeToFileAlreadyExisting(absentFileTemp, "================");*/
		return precision + ":"+ recall;
	}


	public static double getPrecision(ArrayList<String> chunks1,
			ArrayList<String> chunks2) {
		int numOfMatches = 0;
		for (String c1: chunks1) {
			if (chunks2.contains(c1)) {
				numOfMatches++;
			}
		}
		return (double)numOfMatches/(double)chunks1.size();
	}

	public static double getRecall(ArrayList<String> chunks1,
			ArrayList<String> chunks2) {
		int numOfMatches = 0;
		Boolean[] coveredMatches = new Boolean[chunks2.size()];
		
		for (String c1: chunks1) {
			if (chunks2.contains(c1)) {
				int indexNow = 0;
				for (String c2: chunks2) {
					if (c2.equals(c1)) {
						if (coveredMatches[indexNow] == null || coveredMatches[indexNow] == false) {
							coveredMatches[indexNow] = true;
							numOfMatches++;
							break;
						}
					}
					indexNow++;
				}
				
			}
			
		}
		return (double)numOfMatches/(double)chunks2.size();
	}
	
	public static String parseval(String reference, String target, Double precision, 
			Double recall, Double crossings) {
		String[] wordsRef = reference.split(" ");
		String[] wordsTar = target.split(" ");
		indexNow = 0;
		HashMap<String, Boolean[]> geMapsRef = new HashMap<>();
		HashMap<String, Boolean[]> geMapsTar = new HashMap<>();
		int lengthExpected = Math.max(wordsRef.length, wordsTar.length);
		int minLen = Math.min(wordsRef.length, wordsTar.length);
		/*if (wordsRef.length != wordsTar.length) {
			return "ERROR";
		}*/

		ArrayList<Boolean[]> listsRef = new ArrayList<>();
		ArrayList<Boolean[]> listsTar = new ArrayList<>();

		for (int i=0; i < minLen; i++) {
			String wordRefNow = wordsRef[i];			
			String wordTarNow = wordsTar[i];

			if (wordRefNow.contains("[")) {
				bracketingBegins(wordRefNow, 
						i, lengthExpected, listsRef);
			}

			if(wordRefNow.contains("]")) {
				listsRef = bracketingEnds(wordRefNow, 
						i, listsRef, geMapsRef);
			}

			if (!wordRefNow.contains("[") && 
					!wordRefNow.contains("]")) {
				updateBracketMaps(wordRefNow, i, lengthExpected, 
						listsRef);
			}

			if (wordTarNow.contains("[")) {
				bracketingBegins(wordTarNow, 
						i, lengthExpected, listsTar);
			}

			if(wordTarNow.contains("]")) {
				listsTar = bracketingEnds(wordTarNow, 
						i, listsTar, geMapsTar);
			}

			if (!wordTarNow.contains("[") && 
					!wordTarNow.contains("]")) {
				updateBracketMaps(wordTarNow, i, lengthExpected, 
						listsTar);
			}

		}

		/*System.out.println("===============");
		printMap(geMapsRef);
		System.out.println("===============");
		printMap(geMapsTar);*/
		String original = target.replaceAll("\\[", "").replaceAll("\\]", "");
		String[] words = original.split(" ");

		for (String key: geMapsTar.keySet()) {
			String chunkTarNow = "";
			Boolean[] wordsHere = geMapsTar.get(key);
			int indexNowHere = 0;
			for (Boolean b: wordsHere) {
				if (b) {
					chunkTarNow += words[indexNowHere]+" ";
				}
				indexNowHere++;
			}
			tarChunks.add(chunkTarNow);
		}

		for (String key: geMapsTar.keySet()) {
			String chunkTarNow = "";
			Boolean[] wordsHere = geMapsTar.get(key);
			int indexNowHere = 0;
			for (Boolean b: wordsHere) {
				if (b) {
					chunkTarNow += words[indexNowHere]+" ";
				}
				indexNowHere++;
			}
			refChunks.add(chunkTarNow);
		}


		precision = getPrecision(geMapsRef, geMapsTar, tarChunks.size());
		recall = getRecall(geMapsRef, geMapsTar, refChunks.size());

		//Double precisionBracketed = getPrecisionUnmatchedCrossBracketings(geMapsRef, geMapsTar, minLen);
		//Double recallBracketed = getRecallUnmatchedCrossBracketings(geMapsRef, geMapsTar, minLen);
		tarChunks = new LinkedHashSet<>();
		refChunks = new LinkedHashSet<>();
		Double precisionBracketed = 0.0;
		Double recallBracketed = 0.0;

		return precision + ":"+ recall + ":"+ precisionBracketed + ":"+recallBracketed;

	}

	private static int indexNow = 0;
	public static HashMap<String, Boolean[]> fillMapForCrossBracketings(
			String[] refWords, 
			ArrayList<String> chunks ){

		HashMap<String, Boolean[]> vals = new HashMap<>();

		for (String chunk: chunks) {
			chunk = chunk.trim();
			ArrayList<int[]> allPositions = findPositions(chunk, refWords);
			for (int[] positions: allPositions) {
				Boolean[] booNow = new Boolean[refWords.length];

				for (int i=0; i < booNow.length; i++) {
					booNow[i] = false;
				}

				for (int pos: positions) {
					booNow[pos] = true;
				}
				vals.put(""+indexNow++, booNow);
			}

		}
		return vals;
	}

	public static ArrayList<int[]> findPositions(String chunk, String[] words) {
		//String[] chunkWords = chunk.split(" ");
		String[] chunkWords = new String[]{chunk};

		ArrayList<Integer> positions = new ArrayList<>();
		ArrayList<ArrayList<Integer>> allPositions = new ArrayList<>();
		for (int i =0; i < words.length; i++) {
			if (positions.size() > 0) {
				allPositions.add(positions);
				positions = new ArrayList<>();
			}
			int k = i;
			for (int j =0; j < chunkWords.length; j++) {
				if (!chunkWords[j].equals(words[k])) {
					positions = new ArrayList<>();
					break;
				} else {
					positions.add(k);
					k++;
					if (k>=words.length) {
						allPositions.add(positions);
						positions = new ArrayList<>();
						break;
					}
				}
			}
		}
		ArrayList<int[]> allPoss = new ArrayList<>();
		for (ArrayList<Integer> pos: allPositions) {
			int[] posMain = new int[pos.size()];
			for (int i=0; i < pos.size(); i++) {
				posMain[i] = pos.get(i);
			}
			allPoss.add(posMain);
		}
		return allPoss;
	}


	public static Double getPrecisionUnmatchedCrossBracketings(HashMap<String, Boolean[]> reference, 
			HashMap<String, Boolean[]> target, int totWords) {
		Double precision = 0.0;
		ArrayList<Double> precisions = new ArrayList<>();
		HashMap<String, Boolean[]> uncoveredTargets= getUncoveredTargets(reference, target);
		Boolean[] wordsCoveredInRef = getAllCoveredWordsInReferences(reference, totWords);


		for (String s: uncoveredTargets.keySet()) {
			Boolean[] uncovered = uncoveredTargets.get(s);
			if(uncovered.length != wordsCoveredInRef.length) {
				break;
			}
			int numMatches = 0;
			int denominator = 0;
			for (int i=0; i < uncovered.length; i++) {
				if ((uncovered[i] == wordsCoveredInRef[i]) && 
						uncovered[i] == true){
					numMatches++;
					denominator++;
				} else if (uncovered[i] == true) {
					denominator++;
				}
			}
			double precisionNow = (double)numMatches/(double)denominator;
			precisions.add(precisionNow);
		}

		double sum = 0.0;
		for (double d: precisions) {
			sum+=d;
		}
		precision = sum/(double)precisions.size();
		return precision;
	}

	public static Double getRecallUnmatchedCrossBracketings(HashMap<String, Boolean[]> reference, 
			HashMap<String, Boolean[]> target, int totWords) {
		Double recall = 0.0;

		ArrayList<Double> recalls = new ArrayList<>();
		HashMap<String, Boolean[]> uncoveredTargets= getUncoveredTargets(reference, target);
		Boolean[] wordsCoveredInRef = getAllCoveredWordsInReferences(reference, totWords);

		for (String s: uncoveredTargets.keySet()) {
			Boolean[] uncovered = uncoveredTargets.get(s);
			int numMatches = 0;
			int denominator = 0;
			if(uncovered.length != wordsCoveredInRef.length) {
				break;
			}
			for (int i=0; i < uncovered.length; i++) {
				if ((uncovered[i] == wordsCoveredInRef[i]) && 
						uncovered[i] == true){
					numMatches++;
					denominator++;
				} else if (wordsCoveredInRef[i] == true) {
					denominator++;
				}
			}
			double recallNow = (double)numMatches/(double)denominator;
			recalls.add(recallNow);
		}

		double sum = 0.0;
		for (double d: recalls) {
			sum+=d;
		}
		recall = sum/(double)recalls.size();
		return recall;

	}

	public static HashMap<String, Boolean[]> getUncoveredTargets(HashMap<String, Boolean[]> reference, 
			HashMap<String, Boolean[]> target) {
		HashMap<String, Boolean[]> uncovered = new HashMap<>();
		boolean tarPresent = false;
		for (String tarKey: target.keySet()) {
			Boolean[] targetNow = target.get(tarKey);
			for (String refKey: reference.keySet()) {
				Boolean[] refNow = reference.get(refKey);
				boolean equals = true;
				for (int i=0; i < refNow.length; i++) {
					if (refNow[i] != targetNow[i]) {
						equals = false;
						break;
					}
				}
				if (equals) {
					tarPresent = true;
					break;
				}
			}
			if (!tarPresent) {
				uncovered.put(tarKey, targetNow);
			}
			tarPresent=false;
		}

		return uncovered;
	}

	public static Boolean[] getAllCoveredWordsInReferences(HashMap<String, Boolean[]> reference,
			int totWords){

		Boolean[] covered = new Boolean[totWords];

		for (String key: reference.keySet()) {
			Boolean[] coveredForThisKey = reference.get(key);
			for (int i=0; i < totWords; i++) {
				covered[i] = coveredForThisKey[i];
			}
		}
		return covered;
	} 

	public static Double getRecall(HashMap<String, Boolean[]> reference, 
			HashMap<String, Boolean[]> target, int uniqueChunksInRef) {
		int numCorrect = 0;
		HashSet<Boolean[]> uniquesTarget = new HashSet<>();
		HashSet<Boolean[]> uniquesReference = new HashSet<>();

		for (String tarKey: target.keySet()) {
			Boolean[] targetNow = target.get(tarKey);
			boolean completelyEqual = false;
			for (Boolean[] uts: uniquesTarget) {
				completelyEqual = true;
				for (int i=0; i < uts.length; i++) {
					if (uts[i]!=targetNow[i]) {
						completelyEqual = false;
					}
				}
				// found someone already present in the uniquestarget
				if(completelyEqual) {
					break;
				}
			}
			if (!completelyEqual) {
				uniquesTarget.add(targetNow);
			}
		}

		for (String refKey: reference.keySet()) {
			Boolean[] refNow = reference.get(refKey);
			boolean completelyEqual = false;
			for (Boolean[] urs: uniquesReference) {
				completelyEqual = true;
				for (int i=0; i < urs.length; i++) {
					if (urs[i]!=refNow[i]) {
						completelyEqual = false;
					}
				}
				if(completelyEqual) {
					break;
				}
			}
			if (!completelyEqual) {
				uniquesReference.add(refNow);
			}
		}

		for (Boolean[] tarNow: uniquesTarget) {
			for (Boolean[] refNow: uniquesReference) {
				boolean equals = true;
				for (int i=0; i < refNow.length; i++) {
					if (refNow[i] != tarNow[i]) {
						equals = false;
						break;
					}
				}
				if (equals) {
					numCorrect++;
					totalCorrectAll++;
					break;
				}
			}
		}
		totalRefsAll += reference.size();
		return (double)numCorrect/(double)uniquesReference.size();
		//return (double)numCorrect/(double)uniqueChunksInRef;
	}

	public static Double getPrecision(HashMap<String, Boolean[]> reference, 
			HashMap<String, Boolean[]> target, int uniqueChunksInTarget) {
		int numCorrect = 0;
		for (String tarKey: target.keySet()) {
			Boolean[] targetNow = target.get(tarKey);
			for (String refKey: reference.keySet()) {
				Boolean[] refNow = reference.get(refKey);
				boolean equals = true;
				for (int i=0; i < refNow.length; i++) {
					if (refNow[i] != targetNow[i]) {
						equals = false;
						break;
					}
				}
				if (equals) {
					numCorrect++;
					break;
				}
			}
		}
		totalTarsAll += target.size();
		return (double)numCorrect/(double)target.size();
		//return (double)numCorrect/(double)uniqueChunksInTarget;
	}

	/*
	 * Called when word has been detected to have a beginning bracket. The presence of
	 * beginning bracket indicates a new branch.
	 */
	public static void bracketingBegins(String wordWithBeginBrackets, 
			int positionNow, int numOfWords,
			ArrayList<Boolean[]> currentMaps){
		int bracketCount = wordWithBeginBrackets.length() - 
				wordWithBeginBrackets.replaceAll("\\[", "").length();
		ArrayList<Boolean[]> newMaps = new ArrayList<>();

		for (int i=0; i < bracketCount; i++) {
			Boolean[] temp = new Boolean[numOfWords];
			for (int j=0; j < temp.length; j++) {
				temp[j] = false;
			}
			newMaps.add(temp);
		}
		currentMaps.addAll(newMaps);

		for (Boolean[] curr: currentMaps) {
			curr[positionNow] = true;
		}
	}

	/*
	 *  Called for all other words
	 */


	public static void updateBracketMaps(String wordWithBeginBrackets, 
			int positionNow, int numOfWords,
			ArrayList<Boolean[]> currentMaps) {
		for (Boolean[] curr: currentMaps) {
			curr[positionNow] = true;
		}
	}

	/*
	 * Called when the ending bracket has been detected in the word.
	 */
	public static ArrayList<Boolean[]> bracketingEnds(String wordWithEndingBrackets, 
			int positionNow, ArrayList<Boolean[]> currentMaps, 
			HashMap<String, Boolean[]> moi){

		int numberOfEndingBrackets = wordWithEndingBrackets.length() - 
				wordWithEndingBrackets.replaceAll("\\]", "").length();

		for (Boolean[] curr: currentMaps) {
			curr[positionNow] = true;
		}

		int size = moi.size();
		ArrayList<Boolean[]> newCurrMaps = new ArrayList<>();
		int mapNow = 0;

		for (int i=currentMaps.size()-1; i >= 0 ; i--) {
			Boolean[] curr = currentMaps.get(i);
			if(mapNow < numberOfEndingBrackets) {
				moi.put(size+"", curr);
				size++;
			} else {
				newCurrMaps.add(curr);
			}
			mapNow++;
		}
		return newCurrMaps;
	}

	/*
	 * Prints the maps
	 */
	public static void printMap(HashMap<String, Boolean[]> map) {
		for (String key: map.keySet()) {
			System.out.println();
			System.out.print(key+": ");
			Boolean[] arr = map.get(key);
			for (Boolean b: arr) {
				System.out.print(b+"\t");
			}
		}
		System.out.println();
	}
}

