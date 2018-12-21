import java.awt.List;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;


public class CodeAnalyzer {
	
	String path;
	int count;
	int totalLines;
	int totalWhiteSpacesLines;
	int totalLinesOfCode;
	int totalCommentLines;
	int totalSourceCodeLine;
	int totalPakageCount;
	int pakageCount;
	ArrayList<String> pakageNameArray;
	
	public CodeAnalyzer(){
		this.totalLines = 0;
		this.count = 0;
		this.pakageCount = 0;
		pakageNameArray = new ArrayList<String>();
	}
	
	public void countFiles(String path) throws IOException{
		int checker = 0;
		File f = new File(path);
		File[] files= f.listFiles();
		if(files != null){
			for(int i = 0; i < files.length; i++){
				String fileName = files[i].getName();
				checker = fileName.lastIndexOf(".");
				if(checker != -1 && checker != 0){
					String extensionName = fileName.substring(fileName.lastIndexOf(".")+1);
					if(extensionName.equals("java")){
						System.out.println(files[i].toString());
						countTotalLines(files[i].toString());
						countTheWhiteSpaces(files[i].toString());
						countTheComment(files[i].toString());
						countThePakages(files[i].toString());
						count++;
					}
				}
				File file= files[i];
				if(file.isDirectory()){
					countFiles(file.getAbsolutePath());
				}
			}
		}
	}
	
	public void countTotalLines(String javaPath) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(javaPath));
		int line = 0;
		while(reader.readLine() != null){
			line++;
		}
		totalLines += line;
		reader.close();
	}
	
	public void countTheWhiteSpaces(String javaPath) throws FileNotFoundException{
		BufferedReader reader = new BufferedReader(new FileReader(javaPath));
		String line;
		int empty = 0;
		try {
			while((line = reader.readLine()) != null){
				if(line.trim().isEmpty()){
					empty++;
				}
			}
//			System.out.println(empty);
			totalWhiteSpacesLines += empty;
		} catch (IOException e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}
	
	public void countTheComment(String javaPath){
		String line;
		int lastComment = 0;
		int count = 0;
		try {
            BufferedReader reader = new BufferedReader(new FileReader(javaPath));
                try {
					while((line = reader.readLine())!=null)
					{
					    line = line.trim();
					    if(line.startsWith("//")){
					    	count++;
					    }
					    if(line.startsWith("/*"))
					    {
					        count++;
					        while(!(line = reader.readLine()).endsWith("'*\'"))
					        {
					        	lastComment++;
					        	if(lastComment == 1){
					        		count++;
					        	}
					        	count++;
					        	break;
					        }
					    }

					}
					totalCommentLines += count;
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
	}
	
	
	public void countThePakages(String javaPath){
		String line;
		int lastComment = 0;
		int count = 0;
		try {
            BufferedReader reader = new BufferedReader(new FileReader(javaPath));
                try {
					while((line = reader.readLine())!=null)
					{
					    if(line.startsWith("package")){
					    	String[] parts = line.split(" ");
					    	String[] pakageName = parts[1].split(";");
					    	System.out.println(pakageName[0]);
					    	pakageNameArray.add(pakageName[0]);
					    	pakageCount++;
					    	count++;
					    }
					}
					totalPakageCount += count;
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
	}
	public void countTheUnComment(String javaPath){
		
	}
	
	public int getCount(){
		return count;
	}
	
	public int getTotalLines(){
		return totalLines;
	}
	
	public int getTotalWhiteSpaceLines(){
		return totalWhiteSpacesLines;
	}
	
	public int getTotalLinesOfCode(){
		return 0;
	}
	
	public int getTotalCommentLines(){
		return totalCommentLines;
	}
	
	public int getTotalSourceCodeLines(){
		totalSourceCodeLine = totalLines - ( totalWhiteSpacesLines +  totalCommentLines);
		return totalSourceCodeLine;
	}
	
	public int removeTheSameElement(){
		Set<String> hs1 = new LinkedHashSet<String>(pakageNameArray);
        ArrayList<String> al2 = new ArrayList<String>(hs1);
        return al2.size();
	}

}
