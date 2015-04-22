import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {

	Crawler crawler = new Crawler();
	IOHelper io = new IOHelper();
	DatabaseHelper db = new DatabaseHelper();
	
	public static void main(String[] args) {
		new Main().run();
	}

	private void run() {
		//getAirlineData();
		
		process();
		
		comapre();
	}
	
	
	private void comapre() {
		ArrayList<String> names = new ArrayList<String>();
		
		//读取机场名称
		BufferedReader namereader = io.getReader("data2.txt");
		try {
			String s = null;
			while((s = namereader.readLine()) != null){
				names.add(s.split("\t")[1]);
			}
			namereader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//读取原始数据和时刻表统计数据
		BufferedReader reader = io.getReader("data1.txt");
		BufferedReader r = io.getReader("result.txt");
		int data[][] = new int[201][201];
		int result[][] = new int[201][201];
		try {
			String s;
			int i = 0;
		    while((s = reader.readLine()) != null){
		    	String[] strs =s.split("\t");		
		    	for (int j = 0; j < strs.length; j++) {
					data[i][j] = Integer.parseInt(strs[j]);
				}
		    	i++;
		    }
		    
		    
			i = 0;
		    while((s = r.readLine()) != null){
		    	String[] strs =s.split("\t");		
		    	for (int j = 0; j < strs.length; j++) {
					result[i][j] = Integer.parseInt(strs[j]);
				}
		    	i++;
		    }
			reader.close();
			r.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		BufferedWriter writer = io.getWriter("1.txt");
		//找出原始数据没有但是统计数据有
		for(int i = 0; i < 201; i++){
			for(int j = 0; j < 201; j++){
				if((data[i][j] == 0)&&(result[i][j] != 0)){
					int row = i+1;
					String name1 = names.get(i);
					int col = j+1;
					String name2 = names.get(j);
					//System.out.println(row+name1 + "\t" + col+name2);
					try {
						writer.write(row+name1 + "\t" + col+name2 + "\r\n");
						writer.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		writer = io.getWriter("2.txt");
		//找出原始数据有但是统计数据没有
		for(int i = 0; i < 201; i++){
			for(int j = 0; j < 201; j++){
				if((data[i][j] != 0)&&(result[i][j] == 0)){
					int row = i+1;
					String name1 = names.get(i);
					int col = j+1;
					String name2 = names.get(j);
					//System.out.println(row+name1 + "\t" + col+name2);
					try {
						writer.write(row+name1 + "\t" + col+name2 + "\r\n");
						writer.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	private void process(){
		ArrayList<String> names = new ArrayList<String>();
		int result[][] = new int[201][201];
		for(int i = 0; i < 201; i++){
			for(int j = 0; j < 201; j++){
				result[i][j] = 0;
			}
		}
		//读取机场名称
		BufferedReader reader = io.getReader("data2.txt");
		try {
			String s = null;
			while((s = reader.readLine()) != null){
				names.add(s.split("\t")[1]);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//读取航班数据，并添加到2维数组中
		reader = io.getReader("data.txt");
		try {
			String s = null;
			while((s = reader.readLine()) != null){
				String[] strs = s.split(" ");
				int i = names.indexOf(strs[0]);
				int j = names.indexOf(strs[1]);
				if((i < 0)||(j < 0)){
					System.out.println("不存在"+s);
				}
				else{
					result[i][j]++;
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//数组转置，获取a[i][j] != a[j][i]的情况
		BufferedWriter wr = io.getWriter("mismatch.txt");
		for(int i = 0; i < 201; i++){
			for(int j = 0; j < 201; j++){
				if((result[i][j]==0)&&(result[j][i]!=0)){
					result[i][j] = result[j][i];
					//System.out.println(i+","+j);
				}
				else if((result[i][j]!=0)&&(result[j][i]==0)){
					result[j][i] = result[i][j];
					//System.out.println(j+","+i);
				}
				else if((result[i][j]!=0)&&(result[j][i]!=0)){
					if((result[i][j] != result[j][i])){
						int row = i+1;
						int col = j+1;
						//System.out.println("不匹配 " + row+","+col + "\t" +col+","+row);
						try {
							wr.write("不匹配 " + row+","+col + "\t" +col+","+row +"\r\n");
							wr.flush();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		try {
			wr.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//保存时刻表航班的结果
		BufferedWriter writer = io.getWriter("result.txt");
		try {
			for (int i = 0; i < 201; i++) {
				for (int j = 0; j < 201; j++) {
					writer.write(Integer.toString(result[i][j]));
					writer.write("\t");
				}
				writer.write("\r\n");
				writer.flush();
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	//获取时刻表中航班数据
	private void getAirlineData(){
		String url = "http://gsair.flights.ctrip.com/shikebiao.html";
		ArrayList<String> urls = firstPath(url);
		
		secondPath(urls);
	}

	//获取所有url地址
	private ArrayList<String> firstPath(String url) {
		System.out.println("fir");
		String s = crawler.getHtmlSourceCode(url);
		//db.write(s);
		String regEx = "<a href=\"http://flights.ctrip.com/schedule/flights-aline([^\"]+\")";
		
		Pattern pat = Pattern.compile(regEx);
		Matcher matcher = pat.matcher(s);
		
		ArrayList<String> urls = new ArrayList<String>();
		
		while(matcher.find()){
			String str = matcher.group().split("\"")[1];
			urls.add(str);
			//System.out.println(matcher.group().split("\"")[1]);
		}
		return urls;
	}
	
	
	//统计机场航班信息
	private void secondPath(ArrayList<String> urls) {
		System.out.println("sec");
		BufferedWriter writer = io.getWriter("data.txt");
		
		for (String url : urls) {
			
			String s = crawler.getHtmlSourceCode(url);
			String regEx = "class=\"airport\" title=\"([^\"]+)\"";
			
			Pattern pat = Pattern.compile(regEx);
			Matcher matcher = pat.matcher(s);
			
			while(matcher.find()){
				String airport1 = matcher.group().split("\"")[3];
				airport1 = airport1.split("T")[0];
				matcher.find();
				String airport2 = matcher.group().split("\"")[3];
				airport2 = airport2.split("T")[0];
				System.out.println(airport1 + airport2);
				try {
					writer.write(airport1 +" "+ airport2 +"\r\n");
					writer.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
}
