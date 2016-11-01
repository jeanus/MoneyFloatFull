package viking;

import static java.lang.System.out;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

public class DailyDataService {
	
	public static void main(String[] args) throws Exception {
		//transformData();
		loadDailyData(); 
	//	 loadData();
		 out.println(getClose("002726","2016-01-13"));
		 out.println(getOpen("000002","2016-10-14"));
		 out.println(getHigh("000417","2016-08-30"));
		 out.println(getLow("002293","2016-09-08"));
	}
	
	static double getHigh(String symbol,String date){
		double high = -10;
		try{
			high = dailyData.get(date).get(symbol).get("high");
		}catch(Exception e){}
		return high;
	}
	
	static double getLow(String symbol,String date){
		double low = -10;
		try{
			low = dailyData.get(date).get(symbol).get("low");
		}catch(Exception e){}
		return low;
	}
	
	static double getChange(String symbol,String date){
		double open = -100;
		try{
			open = dailyData.get(date).get(symbol).get("change");
		}catch(Exception e){}
		return open;
	}
	
	static double getOpen(String symbol,String date){
		double open = -10;
		try{
			open = dailyData.get(date).get(symbol).get("open");
		}catch(Exception e){}
		return open;
	}
	
	static double getAdjustPriceF(String symbol,String date){
		double adjust_price_f  = -10;
		try{
			adjust_price_f = dailyData.get(date).get(symbol).get("adjust_price_f");
		}catch(Exception e){}
		return adjust_price_f;
	}
	
	static double getClose(String symbol,String date){
		double close = -10;
		try{
			close = dailyData.get(date).get(symbol).get("close");
		}catch(Exception e){}
		return close;
	}
	
	static double getVolume(String symbol,String date){
		double volume  = -10;
		try{
			volume = dailyData.get(date).get(symbol).get("volume");
		}catch(Exception e){}
		return volume;
	}
	
	static private HashMap<String, HashMap<String, HashMap<String, Double>>> dailyData = new HashMap<String, HashMap<String, HashMap<String, Double>>>();
	static String base = "E:\\Be_A_Quant\\Daily_Data\\stock_data\\";
	
	static HashMap<String,HashMap<String,String>> transformMap = new HashMap<String,HashMap<String,String>>();
	static void transformData() throws Exception{

			LinkedList<String> list = FileOperation.getFileNames(base);
			for (String fileName : list) {
				
				String symbol = fileName.substring(0,fileName.indexOf("."));
				out.println(symbol);
				BufferedReader br = FileOperation.openFile(base + fileName);
				String s = "";
				s = br.readLine();
			
				while ((s = br.readLine()) != null) {
					String date = s.split(",")[1];
					HashMap<String,String> map  = new HashMap<String,String>();
					if(transformMap.containsKey(date))
						map = transformMap.get(date);
					map.put(symbol, s.trim());
					transformMap.put(date, map);
					
				}				
				br.close();

			}
			out.println(transformMap.size());
		
			for(String date:transformMap.keySet()){
				out.println(date);
				String output = "E:\\Be_A_Quant\\Daily_Data\\stock_data_daily\\"+date+".csv";
				BufferedWriter bw = FileOperation.writeFile(output);
				bw.append("code,date,open,high,low,close,change,volume,money,traded_market_value,market_value,turnover,adjust_price,report_type,report_date,PE_TTM,PS_TTM,PC_TTM,PB,adjust_price_f");
				bw.newLine();
				bw.flush();
				HashMap<String,String> map  =  transformMap.get(date);
				for(String symbol:map.keySet()){
					bw.append(map.get(symbol));
					bw.newLine();
				}
				bw.flush();
				bw.close();
			}
	}
	static Set<String> getSymbols(String date){
		return dailyData.get(date).keySet();
	}
	
	static void loadDailyData() throws Exception{
		String folder = "E:\\Be_A_Quant\\Daily_Data\\stock_data_daily\\";
		LinkedList<String> list = FileOperation.getFileNames(folder);
		for(String fileName:list)
		{
			String date = fileName.substring(0,fileName.indexOf("."));
			if(!date.startsWith("2015")&&!date.startsWith("2016"))
				continue;
			out.println(date);
			HashMap<String, HashMap<String, Double>> todayMap = new HashMap<String, HashMap<String, Double>>();			
			BufferedReader br = FileOperation.openFile(folder + fileName);
			String s=br.readLine().trim();
			String[] keyArr = s.split(",");				
			while ((s = br.readLine()) != null) {
				String[] valueArr = s.trim().split(",");
				String symbol = valueArr[0].substring(2);
				HashMap<String, Double> stockMap = new HashMap<String, Double>();
				stockMap.put(keyArr[2], Double.parseDouble(valueArr[2]));
				stockMap.put(keyArr[3], Double.parseDouble(valueArr[3]));
				stockMap.put(keyArr[4], Double.parseDouble(valueArr[4]));
				stockMap.put(keyArr[5], Double.parseDouble(valueArr[5]));
				stockMap.put(keyArr[6], Double.parseDouble(valueArr[6]));
				stockMap.put(keyArr[7], Double.parseDouble(valueArr[7]));
				todayMap.put(symbol,stockMap);
			}			
			dailyData.put(date, todayMap);
			br.close();
		}
		out.println(dailyData.size());
	}
	
	static void loadData() throws Exception{

		//String output = "E:\\Be_A_Quant\\out.txt";
		//BufferedWriter bw = FileOperation.writeFile(output);
		LinkedList<String> list = FileOperation.getFileNames(base);
		for (String fileName : list) {
			
			String symbol = fileName.substring(2,fileName.indexOf("."));
			BufferedReader br = FileOperation.openFile(base + fileName);
			String s = "";
			s = br.readLine();
			String[] keyArr = s.split(",");	
			//double low = Double.MAX_VALUE;
			//double current =0;
			//double market_value = 0.0;
			//int currentFlag= 0;
			//String lastDate = "";
			while ((s = br.readLine()) != null) {
				
				String[] valueArr = s.split(",");
				String date = valueArr[1];
				HashMap<String, HashMap<String, Double>> dailyMap = new HashMap<String, HashMap<String, Double>>();
				if(dailyData.containsKey(date))
					dailyMap = dailyData.get(date);
				HashMap<String, Double> stockMap = new HashMap<String, Double>();
				stockMap.put(keyArr[2], Double.parseDouble(valueArr[2]));
				stockMap.put(keyArr[3], Double.parseDouble(valueArr[3]));
				stockMap.put(keyArr[4], Double.parseDouble(valueArr[4]));
				stockMap.put(keyArr[5], Double.parseDouble(valueArr[5]));
				//stockMap.put(keyArr[7], Double.parseDouble(valueArr[7]));
				//stockMap.put(keyArr[19], Double.parseDouble(valueArr[19]));
				// stockMap.put(keyArr[4], Double.parseDouble(valueArr[4]));
				
				//double adjuct_f = Double.parseDouble(valueArr[19]);
				//stockMap.put(keyArr[19], adjuct_f);
				dailyMap.put(symbol,stockMap);
				dailyData.put(date, dailyMap);
				/*
				if(currentFlag<1){
					current = adjuct_f;
					lastDate = date;
					market_value = Double.parseDouble(valueArr[10]);
				}
				currentFlag ++;
				if(adjuct_f<low){
					low = adjuct_f;					
				}
				*/
			}
			br.close();
			//if(market_value<5500000000.0&&lastDate.startsWith("2016-09")){
			//	bw.append(fileName+"\t"+current+"\t"+low+"\t"+(current/low)+"\t");
			//	bw.newLine();
			//	bw.flush();
			//}
			out.println(fileName);//+"\t"+current+"\t"+low+"\t"+(current/low));
		}
		out.println(dailyData.size());
		//bw.close();
		
	}
	

}
