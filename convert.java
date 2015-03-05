package kalpha;

import java.awt.Label;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class convertToR {
	//construct the label hashmap
	public static HashMap<String,Integer> labelMap = new HashMap<>();
	public static String[] files = {};
	public static String[] labels = {"As-I","As-M","As-S","A-D","ACK",
									"A-Y","A-N","Q-YN","Q-Wh","Q-D",
									"Q-C","EC","ST","BC","D-M","Rq-S",
									"P","H","G","CON"};
	public static int minSize;
	public static ArrayList<String> standardLines = new ArrayList<String>();
	public static ArrayList<ArrayList<String>> listOfLines = new ArrayList<ArrayList<String>>();
	public static void main(String[] args){
		//populate the label map
		for(int i=0; i < labels.length; i++){
			labelMap.put(labels[i], i);
		}
		
		//get the less size of the lines
		minSize = contentsOfTheFileAsList(files[0]).size();
		standardLines = originalLinesAsList(files[0]);
		for(int i=0; i < files.length; i++){
			ArrayList<String> lines = contentsOfTheFileAsList(files[i]);
			listOfLines.add(lines);
			if(lines.size() < minSize){
				minSize = lines.size();
				standardLines = originalLinesAsList(files[i]);
			}
			else continue;
		}
		
		//convert each line to R matirces
		for(int i=0; i < minSize; i++){
			for(int j=0; j < files.length; j++){
				String standard = standardLines.get(i);
				String chunk = listOfLines.get(j).get(i);
				//construct a 2D array for standard
				String[] standardArray = standard.trim().split(" ");
				String matrix[][] = new String[files.length][standardArray.length];
				//check if two chunks equal	
				if(!isChunk1EqualsChunk2(standard,chunk)){
					//populate the matrix with *
					for(int index = 0; index < standardArray.length; index++){
						matrix[j][index] = "NA";
					}
				} else {
					//populate the matrices with according keys
						 getKey(matrix,j,standardArray.length,chunk);
				}
				
			}
		}
		
	}
	
	public  static void getKey(String[][] matrix, int rowIndex, int colIndex, String chunk) {
		String[] chunkArray = chunk.trim().split("\\]");
		int index = 0;
		for(String word:chunkArray){
			String[] wordArray = word.split(" ");
			String label = wordArray[0].replaceAll("\\[", "");
			matrix[rowIndex][index] = labelMap.get(label).toString();
		}
		//case1: [XXX a green bike]
		
		//case2: [XXX+XXX a green bike]
		
	}
	public static boolean isChunk1EqualsChunk2(String chunk1, String chunk2) {
		String[] regex = new String[] {
				"As-I","As-M","As-S","A-D","ACK",
				"A-Y","A-N","Q-YN","Q-Wh","Q-D",
				"Q-C","EC","ST","BC","D-M","Rq-S",
				"P","H","G","CON","\\[","\\]"," "
		};
		for(String reg: regex){
			chunk1.replaceAll(reg, "");
			chunk2.replaceAll(reg, "");
		}
		
		if(chunk1.equals(chunk2))
			return true;
		else 
			return false;
	}
	public static ArrayList<String> contentsOfTheFileAsList(String fileName) {
				ArrayList<String> completeString = new ArrayList<String>();
				try {
						FileInputStream file = new FileInputStream(fileName);
						HSSFWorkbook workbook;
						workbook = new HSSFWorkbook(file);
						HSSFSheet sheet = workbook.getSheetAt(0);
						Iterator<Row> rowIterator = sheet.iterator();
						int limit = (int)rowIterator.next().getCell(0).getNumericCellValue();
					
						while(limit > 1 && rowIterator.hasNext()){
							Row row = rowIterator.next();
							completeString.add(row.getCell(5).getStringCellValue());
						}
					} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
					}
						return completeString;
	}
	public static ArrayList<String> originalLinesAsList(String fileName) {
		ArrayList<String> completeString = new ArrayList<String>();
		try {
				FileInputStream file = new FileInputStream(fileName);
				HSSFWorkbook workbook;
				workbook = new HSSFWorkbook(file);
				HSSFSheet sheet = workbook.getSheetAt(0);
				Iterator<Row> rowIterator = sheet.iterator();
				int limit = (int)rowIterator.next().getCell(0).getNumericCellValue();
			
				while(limit > 1 && rowIterator.hasNext()){
					Row row = rowIterator.next();
					completeString.add(row.getCell(4).getStringCellValue());
				}
			} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
				return completeString;
}
}
