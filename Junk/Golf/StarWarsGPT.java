import java.util.LinkedHashMap;
import java.util.HashMap;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.lang.StringBuilder;
import java.util.Scanner;

public class StarWarsGPT{
	private static String readFromInputStream(InputStream inputStream) throws IOException{
		StringBuilder resultStringBuilder = new StringBuilder();
		try(BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))){
			String line;
			while((line = br.readLine()) != null){
				resultStringBuilder.append(line).append("\n");
			}
		}
		return resultStringBuilder.toString();
	}
	
	public static void main(String[] args){
		try{
			HashMap<String,LinkedHashMap<String,Integer>>analysis_results=new HashMap<>();
			
			File initialFile = new File(args[0]);
			InputStream targetStream = new FileInputStream(initialFile);
			
			String[]corpus=readFromInputStream(targetStream)
				.replaceAll("\n", " ")
				.replaceAll("\\p{Punct}", "")
				.toLowerCase()
				.split(" ");
			
			for(var s:corpus){
				if(!analysis_results.containsKey(s)){
					analysis_results.putIfAbsent(s,new LinkedHashMap<>());\
					for(int i=0;i<corpus.length-1;i++){
						if(corpus[i].equals(s)){
							analysis_results.get(s).putIfAbsent(corpus[i+1],0);
							analysis_results.get(s).put(corpus[i+1],analysis_results.get(s).get(corpus[i+1])+1);
						}
					}
				}
			}
			Scanner s = new Scanner(System.in);
			
			System.out.println("# of words:");
			int words = s.nextInt();
			
			System.out.println("First word:");
			String o = s.next().toLowerCase();
			
			for(int i=1;i<words;i++){
				LinkedHashMap<String,Integer>result=analysis_results.get(o);
				System.out.print(o+" ");
				
				int j=0;
				for(var k:result.keySet())
					if(result.get(k)>j){
						o=k;
						j=result.get(k);
					}
			}
			System.out.println(o);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}