

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileOperations {

	private String fileName = "ScribbleSavedGames";
	
	
	/* This method will read from file and display on screen */
	public String readFromFile() {
		String contentToReturn = "";
		try {
			if (isFileExist(getFullFileName(fileName))) {
				BufferedReader fileReader = new BufferedReader(new FileReader(getFullFileName(fileName)));
				String line = fileReader.readLine();
				while (line != null) {
					contentToReturn = contentToReturn.concat(line+"\n");
					// read next line
					if(line.isEmpty()) {
					}
					line = fileReader.readLine();
				}
				fileReader.close();
			} else {
				System.err.println("ERROR: " + getFullFileName(fileName).concat(ScribbleConstants.NOT_EXIST));
			}
			System.out.println();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
		return contentToReturn;

	}

	
	public void writeIntoFile(String contentToWriteInFileString) {
		// Content to be assigned to a file
		// Custom input just for illustration purposes
		// Try block to check if exception occurs
		try {
			if(!isFileExist(fileName)) {
				createFileIfNotExist(getFullFileName(fileName));
			}
			
			if (isFileExist(getFullFileName(fileName))) {
				
				// Create a FileWriter object
				// to write in the file
				FileWriter fWriter = new FileWriter(getFullFileName(fileName));

				fWriter.write(contentToWriteInFileString);
				fWriter.write("\n");
				// Closing the file writing connection
				fWriter.close();

				// Display message for successful execution of
				// program on the console
				System.err.println("INFO: CONTENT is successfully saved to ".concat(getFullFileName(fileName)));
			} else {
				System.err.println(getFullFileName(fileName).concat(ScribbleConstants.NOT_EXIST));
			}
			System.out.println();
		}

		// Catch block to handle if exception occurs
		catch (IOException e) {
			// Print the exception
			System.err.print(e.getMessage());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}
	
	private String getFullFileName(String fileName) {
		return fileName.concat(ScribbleConstants.EXT_TXT_FILE);
}
	
	private Boolean isFileExist(String fileName) {
		return new File(fileName).exists();
	}
	
	private void createFileIfNotExist(String fileName) {
		File file = new File(fileName);
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.err.println("ERROR: while creating file " + fileName);
			}
	}
	
}
