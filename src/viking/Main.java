package viking;

import static java.lang.System.out;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

public class Main {
	static HashMap<String, Integer> dateToOffset = new HashMap<String, Integer>();
	static HashMap<Integer, String> offsetToDate = new HashMap<Integer, String>();
	static String base = "E:\\Be_A_Quant\\MoneyFloatStrategy\\FullText\\";
	static HashMap<String, HashMap<String, HashMap<String, Double>>> data = new HashMap<String, HashMap<String, HashMap<String, Double>>>();

	public static void renameFiles() throws Exception {
		String folder = "E:\\Be_A_Quant\\MoneyFloatStrategy\\FullTextTemp";
		LinkedList<String> fileList = FileOperation.getFileNames(folder);
		for (String fileName : fileList) {
			String newName = fileName.substring(fileName.indexOf("2"));
			out.println(newName);
			FileOperation.rename(folder + "\\" + fileName, folder + "\\"
					+ newName);
		}
	}

	public static void loadData() throws Exception {
		LinkedList<String> list = FileOperation.getFileNames(base);
		int offset = 0;
		for (int i = list.size() - 1; i > -1; i--) {
			dateToOffset.put(
					list.get(i).substring(0, list.get(i).indexOf(".")), offset);
			offsetToDate.put(offset,
					list.get(i).substring(0, list.get(i).indexOf(".")));
			offset--;

		}

		for (String fileName : list) {
			String date = fileName.substring(0, 10);
			System.out.println("loading " + date);
			BufferedReader br = FileOperation.openFile(base + fileName);
			String s;
			HashMap<String, HashMap<String, Double>> todayMap = new HashMap<String, HashMap<String, Double>>();
			br.readLine();
			br.readLine();
			while ((s = br.readLine()) != null) {
				if (s.split("\t").length == 36) {
					HashMap<String, Double> thisStockMap = new HashMap<String, Double>();
					String arr[] = s.split("\t");
					String symbol = arr[0].trim().substring(0, 6);
					String dateTest = arr[2].trim();
					if (!date.equals(dateTest)) {
						out.println(symbol + "\t" + date + "\t" + dateTest);
						System.exit(1);
					}
					String price = arr[3].trim();
					String change = arr[4].trim();
					String netIncome = arr[6].replace(",", "")
							.replace("\"", "");
					String netIncomePercentage = arr[7].trim();
					thisStockMap.put("price", Double.parseDouble(price));
					thisStockMap.put("change", Double.parseDouble(change));
					thisStockMap
							.put("netIncome", Double.parseDouble(netIncome));
					thisStockMap.put("netIncomePercentage",
							Double.parseDouble(netIncomePercentage));
					todayMap.put(symbol, thisStockMap);
				}
			}
			data.put(date, todayMap);
		}

		MAX_OFFSET = 1 - data.size();
		out.println(MAX_OFFSET);

	}

	public static double getLow(String symbol, int offset) {
		return DailyDataService.getLow(symbol, offsetToDate.get(offset));
	}

	public static double getLow(String symbol, String date) {
		return DailyDataService.getLow(symbol, date);
	}

	public static double getOpen(String symbol, int offset) {
		return DailyDataService.getOpen(symbol, offsetToDate.get(offset));
	}

	public static double getOpen(String symbol, String date) {
		return DailyDataService.getOpen(symbol, date);
	}
	
	public static double getChangeDailyDataService(String symbol, int offset) {
		return DailyDataService.getChange(symbol, offsetToDate.get(offset));
	}

	public static double getChangeDailyDataService(String symbol, String date) {
		return DailyDataService.getChange(symbol, date);
	}

	public static double getAdjustPriceF(String symbol, int offset) {
		return DailyDataService.getAdjustPriceF(symbol,
				offsetToDate.get(offset));
	}

	public static double getAdjustPriceF(String symbol, String date) {
		return DailyDataService.getAdjustPriceF(symbol, date);
	}

	public static double getClose(String symbol, int offset) {
		return DailyDataService.getClose(symbol, offsetToDate.get(offset));
	}

	public static double getClose(String symbol, String date) {
		return DailyDataService.getClose(symbol, date);
	}

	public static double getVolume(String symbol, int offset) {
		return DailyDataService.getVolume(symbol, offsetToDate.get(offset));
	}

	public static double getVolume(String symbol, String date) {
		return DailyDataService.getVolume(symbol, date);
	}

	public static double getHigh(String symbol, int offset) {
		return DailyDataService.getHigh(symbol, offsetToDate.get(offset));
	}
	
	public static double getHigh(String symbol, String date) {
		return DailyDataService.getHigh(symbol, date);
	}
	

	
	
	public static double getHigh(String symbol,int startOffset,int endOffset){
		double highest = Double.MIN_VALUE;
		for(int offset = startOffset;offset<=endOffset;offset++){
			double temp =  DailyDataService.getHigh(symbol, offsetToDate.get(offset));
			if(highest<temp){
				highest = temp;
			}			
		}
		return highest;
	}
	
	public static Bar getBar(String symbol, String date) {
		 double high = getHigh( symbol,  date);
		 double low = getLow( symbol,  date);
		 double open= getOpen( symbol,  date);
		 double close= getClose( symbol,  date);
		 double volume = getVolume( symbol,  date);
		 double change = getChangeDailyDataService(symbol,  date);
		 return new Bar( high,  low,  open,  close, volume,change);		
	}



	public static double getChange(String symbol, int offset) {
		return data.get(offsetToDate.get(offset)).get(symbol).get("change");
	}

	public static double getChange(String symbol, String date) {
		return data.get(date).get(symbol).get("change");
	}

	public static double getNetIncome(String symbol, int offset) {
		return data.get(offsetToDate.get(offset)).get(symbol).get("netIncome");
	}

	public static double getNetIncome(String symbol, String date) {
		return data.get(date).get(symbol).get("netIncome");
	}

	public static double getNetIncomePercentage(String symbol, int offset) {
		return data.get(offsetToDate.get(offset)).get(symbol)
				.get("netIncomePercentage");
	}

	public static double getNetIncomePercentage(String symbol, String date) {
		return data.get(date).get(symbol).get("netIncomePercentage");
	}

	public static double getPrice(String symbol, int offset) {
		return data.get(offsetToDate.get(offset)).get(symbol).get("price");
	}

	public static double getPrice(String symbol, String date) {
		return data.get(date).get(symbol).get("price");
	}

	
	
	//第n天巨量微涨，当天买进，第二天卖出
	public static void moneyStratedy2() {
		int averagePeriod = 6;
		int count = 0;
		int up = 0;
		int down = 0;
		double profit = 0;
		ArrayList<String> buyList = new ArrayList<String>();
		ArrayList<String> sellList = new ArrayList<String>();
		
		for (int offset = MAX_OFFSET + 840; offset < 1; offset++) {
			
			String date_n_0 = offsetToDate.get(offset);
			String date_n_3 = offsetToDate.get(offset+3);
			Set<String> todaySymbols = data.get(offsetToDate.get(offset))
					.keySet();
			String maxSymbol="";
			double max = Double.MIN_VALUE;
			double n_0_close = 0;
			double n_3_close = 0;
			double n_7_days_ago=0;
			for (String symbol : todaySymbols) {
				if (!symbol.startsWith("6") && !symbol.startsWith("0"))
					continue;
				try {
					double todayNetIncome = getNetIncome(symbol, offset);					
					if(getChange(symbol, offset)>5&&getChange(symbol, offset)<6){}else continue;
					double netIncomePrevious = 0;
					for (int i = offset - 1; i >= (offset - averagePeriod); i--) {
						netIncomePrevious = netIncomePrevious
								+ Math.abs(getNetIncome(symbol, i));
					}
					double ratio = (todayNetIncome / netIncomePrevious);
					double changeTo7daysBefore = (getPrice(symbol, date_n_0 )/getPrice(symbol, offset-7)-1);
					if(changeTo7daysBefore<0.02){} else continue;
					if(ratio>max){
						maxSymbol = symbol;
						max = ratio;
						n_0_close = getPrice(symbol, date_n_0);
						n_3_close = getPrice(symbol, date_n_3);
						n_7_days_ago = getPrice(symbol, offset-7);
					}

				} catch (Exception e) {
				}
			}
			
			if(n_3_close<=0||n_0_close<=0)
				continue;			
			try{
				String str = date_n_0+"\t"+maxSymbol+"\t"+n_7_days_ago+"\t"+n_0_close+"\t"+n_3_close+"\t"+100*(getPrice(maxSymbol, offset+1)/n_0_close-1)+"\t"+100*(getPrice(maxSymbol, offset+2)/n_0_close-1)+"\t"+100*(n_3_close/n_0_close-1)+"\t"+max;
			}catch(Exception e){
				continue;
			}
			double buy = n_0_close;
			double sell = 0;
			String sellDate = "";
			if(100*(getPrice(maxSymbol, offset+1)/n_0_close-1)<0){
				sell = getPrice(maxSymbol, offset+1);
				sellDate = offsetToDate.get(offset+1);
			}else if(100*(getPrice(maxSymbol, offset+2)/n_0_close-1)<0){
				sell = getPrice(maxSymbol, offset+2);
				sellDate = offsetToDate.get(offset+2);
			}else{
				sell = n_3_close;
				sellDate = offsetToDate.get(offset+3);
			}
			
			sellDate = offsetToDate.get(offset+2);
			sell = getPrice(maxSymbol, offset+2);
			profit = profit + 100*(sell/buy-1);
			out.println(date_n_0+"\t"+maxSymbol+"\t"+buy+"\t"+sell+"\t"+100*(sell/buy-1));
			
			buyList.add(date_n_0+ "\t"
					+ (maxSymbol.startsWith("6") ? (maxSymbol + ".XSHG")
							: (maxSymbol + ".XSHE")));
			sellList.add(sellDate+ "\t"
					+ (maxSymbol.startsWith("6") ? (maxSymbol + ".XSHG")
							: (maxSymbol + ".XSHE")));
			count++;
			if(sell>buy)
				up++;
			if(sell<buy)
				down++;
		}
		
		out.println(profit);
		out.println(up+" "+(double) up / (double) count);
		out.println(down+" "+(double) down / (double) count);
		out.println(count);
		out.println();
		for (String buyInfo : buyList) {
			//out.println(buyInfo);
		}
		out.println();
		for (String sellInfo : sellList) {
			//out.println(sellInfo);
		}
		
	}
	
	
	
	// 第n天巨量微涨，第n+1天缩量（需要测试涨跌幅的影响），第n+2天的价格和第n+3天价格关系
	public static void moneyStratedy1() {
		int averagePeriod = 6;
		int up = 0;
		int down = 0;
		double profit = 0;
		int number = 0;

		for (int offset = MAX_OFFSET + 850; offset < 1; offset++) {
			String date_n_0 = offsetToDate.get(offset);
			String date_n_1 = offsetToDate.get(offset + 1);
			String date_n_2 = offsetToDate.get(offset + 2);
			TreeMap<Double, String> treeMap = new TreeMap<Double, String>();
			Set<String> todaySymbols = data.get(offsetToDate.get(offset))
					.keySet();
			for (String symbol : todaySymbols) {
				if (!symbol.startsWith("6") && !symbol.startsWith("0"))
					continue;
				try {
					double todayNetIncome = getNetIncome(symbol, offset);
					double todayChange = getChange(symbol, offset) + 10.2;
					if (Math.abs(getChange(symbol, offset)) > 3)
						continue;
					double netIncomePrevious = 0;
					double changePrevious = 0;
					for (int i = offset - 1; i >= (offset - averagePeriod); i--) {
						netIncomePrevious = netIncomePrevious
								+ Math.abs(getNetIncome(symbol, i));
						changePrevious = changePrevious
								+ Math.abs(getChange(symbol, i)) + 10.2;
					}

					double yesterday_Change = getChange(symbol, offset - 1);
					if (yesterday_Change > 3)
						continue;
					double ratio = (todayNetIncome / netIncomePrevious)
							/ (todayChange);

					treeMap.put(ratio, symbol + "\t" + todayChange);
				} catch (Exception e) {
				}
			}
			NavigableMap<Double, String> nmap = treeMap.descendingMap();
			int top = 100;
			int count = 0;
			for (Double ratio : nmap.keySet()) {
				count++;
				if (count > top)
					break;
				try {
					String symbol = nmap.get(ratio).substring(0, 6);

					double n_0_NetIncome = getNetIncome(symbol, offset + 0);
					double n_0_close = getClose(symbol, date_n_0);
					double n_0_Change = getChange(symbol, offset);

					double n_1_NetIncome = getNetIncome(symbol, offset + 1);
					double n_1_Change = getChange(symbol, offset + 1);

					// if((Math.abs(n_1_NetIncome)/n_0_NetIncome)>0.15||Math.abs(n_1_Change)>2)
					// continue;

					getNetIncome(symbol, offset + 2);
					double n_1_open = getOpen(symbol, date_n_1);
					double n_1_low = getLow(symbol, date_n_1);
					double n_1_high = getHigh(symbol, date_n_1);
					double n_1_close = getClose(symbol, date_n_1);

					double n_2_open = getOpen(symbol, date_n_2);
					double n_2_low = getLow(symbol, date_n_2);
					double n_2_high = getHigh(symbol, date_n_2);
					double n_2_close = getClose(symbol, date_n_2);

					String trend = n_0_Change + "\t" + n_0_NetIncome + "\t"
							+ n_1_NetIncome + "\t" + n_1_Change + "\t"
							+ n_0_close + "\t" + n_1_open + "\t" + n_1_low
							+ "\t" + n_1_high + "\t" + n_1_close + "\t"
							+ n_2_open + "\t" + n_2_low + "\t" + n_2_high
							+ "\t" + n_2_close;

					double tempProfit = 100 * (getClose(symbol, offset + 5)
							/ n_0_close - 1);
					profit = profit + tempProfit;
					number++;
					if (tempProfit > 0)
						up++;
					if (tempProfit < 0)
						down++;

					out.println(date_n_0 + "\t" + ratio + "\t"
							+ nmap.get(ratio) + "\t" + trend + "\t"
							+ tempProfit);
					break;

				} catch (Exception e) {
				}
			}
		}
		out.println((double) up / (double) number);
		out.println((double) down / (double) number);
		out.println(number);
		out.println(profit + " " + profit / (double) number);
	}

	
	
	static int MAX_OFFSET;
	static String targetDate = "2016-10-31";

	public static void findHugeOutputButLittleIncreaseSimple(int buyOffset,
			int sellOffset, int averagePeriod) {
		double totalProfit = 0;
		int up = 0;
		int down = 0;
		ArrayList<String> buyList = new ArrayList<String>();
		ArrayList<String> sellList = new ArrayList<String>();

		for (int offset = MAX_OFFSET; offset < 1; offset++) {

			String date = offsetToDate.get(offset);
			if(!date.startsWith("2016")&&!date.startsWith("2015"))
				continue;

			HashMap<String, HashMap<String, Double>> todayData = data
					.get(offsetToDate.get(offset));
			TreeMap<Double, String> treeMap = new TreeMap<Double, String>();
			for (String symbol : todayData.keySet()) {
				if (!symbol.startsWith("6") && !symbol.startsWith("0"))
					continue;
				try {
					double todayNetIncome = getNetIncome(symbol, offset);
					double todayChange = getChange(symbol, offset);

					if (todayChange > 3 || todayChange < 2)
						continue;
					double netIncomePrevious = 0;
					for (int i = offset - 1; i >= (offset - averagePeriod); i--) {
						netIncomePrevious = netIncomePrevious
								+ Math.abs(getNetIncome(symbol, i));
					}
					double ratio = (todayNetIncome / netIncomePrevious)
							/ (todayChange / 10 + 1);

					treeMap.put(ratio, symbol);
				} catch (Exception e) {
				}
			}

			String todayStock = "";
			double maxRatio = Double.MIN_VALUE;
			NavigableMap<Double, String> nmap = treeMap.descendingMap();
			if (date.equals(targetDate)) {
				for (Double ratio : nmap.keySet()) {
					String symbol = nmap.get(ratio);
					try {

						double todayPrice = getPrice(symbol, offset);
						double netIncome = getNetIncome(symbol, offset);
						double netIncome1 = getNetIncome(symbol, offset + 1);
						System.out.println("\"" + symbol + "\",\t" + date
								+ "\t" + symbol + "\t" + ratio + "\t"
								+ todayPrice + "\t" + netIncome + "\t"
								+ netIncome1);
					} catch (Exception e) {
					}
				}
			}

			for (Double ratio : nmap.keySet()) {
				try {
					String symbol = nmap.get(ratio);
					double todayPrice = getPrice(symbol, offset);
					double buyPrice = getPrice(symbol, offset + buyOffset);
					getPrice(symbol, offset + sellOffset);
					if ((buyPrice / todayPrice - 1) < -0.02
							|| (buyPrice / todayPrice - 1) > 0.015)
						continue;
					todayStock = symbol;
					maxRatio = ratio;
					break;

				} catch (Exception e) {
				}
			}
			

			if (todayStock.equals(""))
				continue;
			double buyChange = getChange(todayStock, offset + buyOffset);
			double netIncome = getNetIncome(todayStock, offset);
			double netIncome1 = getNetIncome(todayStock, offset + 1);
			double netIncome2 = getNetIncome(todayStock, offset + 2);
			// double absoluteChange1 = Math.abs(getChange(todayStock, offset +
			// 1));
			// double absoluteChange2 = Math.abs(getChange(todayStock, offset +
			// 2));
			if (buyChange < 9.8
					&& buyChange > -9
					&& (netIncome1 * netIncome2) < 0
					&& ((netIncome + netIncome1 + netIncome2) / netIncome) > 0.85) {
			} else
				continue;

			/*
			 * if (offsetToDate.get(offset + buyOffset).startsWith("2016") &&
			 * !offsetToDate.get(offset + buyOffset).startsWith( "2016-01")) { }
			 * else continue;
			 */
			//out.println(date + "\t" + todayStock + "\t" + maxRatio + "t");
			

			try{
				String netIncomeLine = getNetIncome(todayStock, offset)+"\t"+getNetIncome(todayStock, offset+1)+"\t"+getNetIncome(todayStock, offset+2)+"\t"+getNetIncome(todayStock, offset+3)+"\t"+getNetIncome(todayStock, offset+4)+"\t"+getNetIncome(todayStock, offset+5)+"\t"+getNetIncome(todayStock, offset+6);
				out.println(date+"\t"+todayStock+"\t"+getPrice(todayStock, offset + 2)+"\t"+getHigh(todayStock, offset + 3)+"\t"+getPrice(todayStock, offset + 3)+"\t"+getPrice(todayStock, offset + 4)+"\t"+getPrice(todayStock, offset + 5)+"\t"+getPrice(todayStock, offset + 6)+"\t"+netIncomeLine);
				buyList.add(offsetToDate.get(offset + buyOffset)
					+ "\t"
					+ (todayStock.startsWith("0") ? (todayStock + ".XSHE")
							: (todayStock + ".XSHG")));
			
			double sellPrice = getPrice(todayStock, offset + sellOffset);
			double buyPrice = getPrice(todayStock, offset + buyOffset);
			double profit = sellPrice / buyPrice - 1;
			totalProfit = totalProfit + profit;

			if (profit > 0)
				up++;
			if (profit < 0)
				down++;

			sellList.add(offsetToDate.get(offset + sellOffset)
					+ "\t"
					+ (todayStock.startsWith("0") ? (todayStock + ".XSHE")
							: (todayStock + ".XSHG")));
			}catch(Exception e){}
		}

		out.println("correctness " + (double) up / (double) (up + down));
		out.println("up " + up);
		out.println("down " + down);
		out.println("totalProfit " + totalProfit);

		out.println();
		for (String buyInfo : buyList) {
			out.println(buyInfo);
		}
		out.println();
		for (String sellInfo : sellList) {
			out.println(sellInfo);
		}
	}

	public static void findHugeOutputButLittleIncreaseMoreTrades(int buyOffset,
			int sellOffset, int averagePeriod) {
		double totalProfit = 0;
		int up = 0;
		int down = 0;
		ArrayList<String> buyList = new ArrayList<String>();
		ArrayList<String> sellList = new ArrayList<String>();

		for (int offset = MAX_OFFSET; offset < 1; offset++) {

			String date = offsetToDate.get(offset);

			HashMap<String, HashMap<String, Double>> todayData = data
					.get(offsetToDate.get(offset));
			TreeMap<Double, String> treeMap = new TreeMap<Double, String>();
			for (String symbol : todayData.keySet()) {
				if (!symbol.startsWith("6") && !symbol.startsWith("0"))
					continue;
				try {
					double todayNetIncome = getNetIncome(symbol, offset);
					double todayChange = getChange(symbol, offset);

					if (todayChange > 3 || todayChange < 2)
						continue;
					double netIncomePrevious = 0;
					for (int i = offset - 1; i >= (offset - averagePeriod); i--) {
						netIncomePrevious = netIncomePrevious
								+ Math.abs(getNetIncome(symbol, i));
					}
					double ratio = (todayNetIncome / netIncomePrevious)
							/ (todayChange / 10 + 1);

					treeMap.put(ratio, symbol);
				} catch (Exception e) {
				}
			}

			String todayStock = "";
			double maxRatio = Double.MIN_VALUE;
			NavigableMap<Double, String> nmap = treeMap.descendingMap();
			if (date.equals(targetDate)) {
				for (Double ratio : nmap.keySet()) {
					String symbol = nmap.get(ratio);
					try {

						double todayPrice = getPrice(symbol, offset);
						double netIncome = getNetIncome(symbol, offset);
						double netIncome1 = getNetIncome(symbol, offset + 1);
						System.out.println("\"" + symbol + "\",\t" + date
								+ "\t" + symbol + "\t" + ratio + "\t"
								+ todayPrice + "\t" + netIncome + "\t"
								+ netIncome1);
					} catch (Exception e) {
					}
				}
			}

			for (Double ratio : nmap.keySet()) {
				try {
					String symbol = nmap.get(ratio);
					double todayPrice = getPrice(symbol, offset);
					double buyPrice = getPrice(symbol, offset + buyOffset);
					getPrice(symbol, offset + sellOffset);
					if ((buyPrice / todayPrice - 1) < -0.02
							|| (buyPrice / todayPrice - 1) > 0.01)
						continue;
					double buyChange = getChange(symbol, offset + buyOffset);
					double netIncome = getNetIncome(symbol, offset);
					double netIncome1 = getNetIncome(symbol, offset + 1);
					double netIncome2 = getNetIncome(symbol, offset + 2);

					if (buyChange < 9.8
							&& buyChange > -9
							&& (netIncome1 * netIncome2) < 0
							&& ((netIncome + netIncome1 + netIncome2) / netIncome) > 0.9) {
					} else
						continue;

					todayStock = symbol;
					maxRatio = ratio;
					break;

				} catch (Exception e) {
				}
			}
			out.println(date + "\t" + todayStock + "\t" + maxRatio + "t");

			if (todayStock.equals(""))
				continue;
			// if(offsetToDate.get(offset +
			// buyOffset).startsWith("2016")&&!offsetToDate.get(offset +
			// buyOffset).startsWith("2016-01"))
			// {}else
			// continue;
			double sellPrice = getPrice(todayStock, offset + sellOffset);
			double buyPrice = getPrice(todayStock, offset + buyOffset);
			double profit = sellPrice / buyPrice - 1;
			totalProfit = totalProfit + profit;

			if (profit > 0)
				up++;
			if (profit < 0)
				down++;
			buyList.add(offsetToDate.get(offset + buyOffset)
					+ "\t"
					+ (todayStock.startsWith("0") ? (todayStock + ".XSHE")
							: (todayStock + ".XSHG")));
			sellList.add(offsetToDate.get(offset + sellOffset)
					+ "\t"
					+ (todayStock.startsWith("0") ? (todayStock + ".XSHE")
							: (todayStock + ".XSHG")));
		}

		out.println("correctness " + (double) up / (double) (up + down));
		out.println("up " + up);
		out.println("down " + down);
		out.println("totalProfit " + totalProfit);

		out.println();
		for (String buyInfo : buyList) {
			out.println(buyInfo);
		}
		out.println();
		for (String sellInfo : sellList) {
			out.println(sellInfo);
		}
	}

	public static void findHugeOutputButLittleIncreaseComplex(int buyOffset,
			int sellOffset, int averagePeriod) {
		double totalProfit = 0;
		int up = 0;
		int down = 0;
		ArrayList<String> buyList = new ArrayList<String>();
		ArrayList<String> sellList = new ArrayList<String>();

		for (int offset = MAX_OFFSET; offset < 1; offset++) {
			String date = offsetToDate.get(offset);
			HashMap<String, HashMap<String, Double>> todayData = data
					.get(offsetToDate.get(offset));
			TreeMap<Double, String> treeMap = new TreeMap<Double, String>();
			for (String symbol : todayData.keySet()) {
				if (!symbol.startsWith("6") && !symbol.startsWith("0"))
					continue;
				try {
					double todayNetIncome = getNetIncome(symbol, offset);
					double todayChange = getChange(symbol, offset);
					if (todayChange > 3 || todayChange < 2)
						continue;
					double netIncomePrevious = 0;
					for (int i = offset - 1; i >= (offset - averagePeriod); i--) {
						netIncomePrevious = netIncomePrevious
								+ Math.abs(getNetIncome(symbol, i));
					}
					double ratio = (todayNetIncome / netIncomePrevious)
							/ (todayChange / 10 + 1);
					treeMap.put(ratio, symbol);
				} catch (Exception e) {
				}
			}

			String todayStock = "";
			double maxRatio = Double.MIN_VALUE;
			NavigableMap<Double, String> nmap = treeMap.descendingMap();
			if (date.equals(targetDate)) {
				for (Double ratio : nmap.keySet()) {
					String symbol = nmap.get(ratio);
					try {

						double todayPrice = getPrice(symbol, offset);
						double netIncome = getNetIncome(symbol, offset);
						double netIncome1 = getNetIncome(symbol, offset + 1);
						System.out.println(date + "\t" + symbol + "\t" + ratio
								+ "\t" + todayPrice + "\t" + netIncome + "\t"
								+ netIncome1 + "\t\"" + symbol + "\",");
					} catch (Exception e) {
					}
				}
			}

			for (Double ratio : nmap.keySet()) {
				try {
					String symbol = nmap.get(ratio);
					double todayPrice = getPrice(symbol, offset);
					double buyPrice = getPrice(symbol, offset + buyOffset);

					getPrice(symbol, offset + sellOffset);
					if ((buyPrice / todayPrice - 1) < -0.02
							|| (buyPrice / todayPrice - 1) > 0.01)
						continue;
					todayStock = symbol;
					maxRatio = ratio;
					break;

				} catch (Exception e) {
				}
			}

			out.println(date + "\t" + todayStock + "\t" + maxRatio + "t");
			if (todayStock.equals(""))
				continue;
			double buyChange = getChange(todayStock, offset + buyOffset);
			double netIncome = getNetIncome(todayStock, offset);
			double netIncome1 = getNetIncome(todayStock, offset + 1);
			double netIncome2 = getNetIncome(todayStock, offset + 2);
			// double absoluteChange1 = Math.abs(getChange(todayStock, offset +
			// 1));
			// double absoluteChange2 = Math.abs(getChange(todayStock, offset +
			// 2));
			if (buyChange < 9.8
					&& buyChange > -9
					&& (netIncome1 * netIncome2) < 0
					&& ((netIncome + netIncome1 + netIncome2) / netIncome) > 0.9) {
			} else
				continue;

			/*
			 * double high = -10; for(int offsetTemp =
			 * buyOffset+2;offsetTemp<=buyOffset+2;offsetTemp++){ double
			 * tempHigh = getHigh(todayStock,offset+offsetTemp);
			 * if(tempHigh>high) high = tempHigh; }
			 */

			double sellPrice = getPrice(todayStock, offset + sellOffset);
			double buyPrice = getPrice(todayStock, offset + buyOffset);
			double profit = sellPrice / buyPrice - 1;
			totalProfit = totalProfit + profit;

			// high = 100*((high-buyPrice)/buyPrice);
			String incomeChange = "";
			incomeChange = incomeChange + "" + getNetIncome(todayStock, offset)
					+ "\t" + getNetIncome(todayStock, offset + 1) + "\t";
			String priceChange = "";
			for (int offsetTemp = buyOffset; offsetTemp <= sellOffset; offsetTemp++) {
				priceChange = priceChange
						+ ""
						+ (getPrice(todayStock, offset + offsetTemp) - buyPrice)
						/ buyPrice + "\t";
				incomeChange = incomeChange + ""
						+ getNetIncome(todayStock, offset + offsetTemp) + "\t";
			}
			priceChange = priceChange + ""
					+ (getPrice(todayStock, offset + 8) - buyPrice) / buyPrice
					+ "\t";

			if (profit > 0)
				up++;
			if (profit < 0)
				down++;
			buyList.add(offsetToDate.get(offset + buyOffset)
					+ "\t"
					+ (todayStock.startsWith("0") ? (todayStock + ".XSHE")
							: (todayStock + ".XSHG")) + "\t" + priceChange
					+ "\t" + incomeChange);
			sellList.add(offsetToDate.get(offset + sellOffset)
					+ "\t"
					+ (todayStock.startsWith("0") ? (todayStock + ".XSHE")
							: (todayStock + ".XSHG")));
		}

		out.println("correctness " + (double) up / (double) (up + down));
		out.println("up " + up);
		out.println("down " + down);
		out.println("totalProfit " + totalProfit);

		out.println();
		for (String buyInfo : buyList) {
			out.println(buyInfo);
		}
		out.println();
		for (String sellInfo : sellList) {
			out.println(sellInfo);
		}
	}
	
	public static void doubleTwoYangJiaYiYin()throws Exception {
		LinkedList<String> list = FileOperation.getFileNames(base);
		int offset = 0;
		for (int i = list.size() - 1; i > -1; i--) {
			dateToOffset.put(
					list.get(i).substring(0, list.get(i).indexOf(".")), offset);
			offsetToDate.put(offset,
					list.get(i).substring(0, list.get(i).indexOf(".")));
			offset--;
		}
		int holdingPeriod = 1;
		double total = 0;
		double up = 0;
		double profit = 0;
		ArrayList<String> buyList = new ArrayList<String>();
		ArrayList<String> sellList = new ArrayList<String>();
		for (offset = offset + 830; offset < 1; offset++) {		
			String date_4 = offsetToDate.get(offset-4);
			String date_3 = offsetToDate.get(offset-3);
			String date_2 = offsetToDate.get(offset-2);
			String date_1 = offsetToDate.get(offset-1);
			String date_0 = offsetToDate.get(offset);
			double maxKey = -10;
			String todayStock = "";
			for (String symbol : DailyDataService.getSymbols(date_0)) {
				if(symbol.startsWith("3"))
					continue;
				if(DailyDataService.getSymbols(date_1).contains(symbol) && DailyDataService.getSymbols(date_2).contains(symbol)&&DailyDataService.getSymbols(date_3).contains(symbol) && DailyDataService.getSymbols(date_4).contains(symbol)){
					Bar bar_4 = getBar(symbol,date_4);
					Bar bar_3 = getBar(symbol,date_3);
					Bar bar_2 = getBar(symbol,date_2);
					Bar bar_1 = getBar(symbol,date_1);
					Bar bar_0 = getBar(symbol,date_0);
					if(bar_0.isYang()&&(!bar_1.isYang())&&bar_2.isYang()&&(!bar_3.isYang())&&(bar_4.isYang()))
						if(bar_0.getChange()>0&&bar_1.getChange()<=0&&bar_2.getChange()>0&&bar_3.getChange()<=0&&bar_4.getChange()>0)
							if( bar_0.getVolume()>bar_1.getVolume()&&bar_0.getVolume()>bar_2.getVolume())
								if( bar_2.getVolume()>bar_3.getVolume()&&bar_2.getVolume()>bar_4.getVolume()){									
									if(bar_0.getClose()>bar_1.getOpen()&&bar_1.getOpen()>bar_2.getClose())
										if(bar_2.getClose()>bar_3.getOpen()&&bar_3.getOpen()>bar_4.getClose())
									//	if(bar_0.getOpen()>bar_1.getClose()&&bar_1.getClose()>bar_2.getOpen())
										{										
									//		if(getClose(symbol,  offsetToDate.get(offset+holdingPeriod))==-10 )
										//		continue;
											String priceAfterwards = "";
											for(int x=0;x<5;x++)
												priceAfterwards = priceAfterwards + " " +getClose( symbol,  offsetToDate.get(offset+x));
											out.println(date_0+" "+symbol+" "+priceAfterwards);
																	
											double xiaYin = bar_1.getDownShadow()/bar_1.getBody();
											double key = xiaYin;
											if(key>maxKey){
												maxKey = key;
												todayStock = symbol;
												
											}
													
										}
								}
						
					
				}
			}
			if(todayStock.equals(""))
				continue;
			Bar bar = getBar(todayStock,date_0);
			total++;
			double buyPrice = bar.getClose();
			double sellPrice = getClose(todayStock,  offsetToDate.get(offset+holdingPeriod));
			if(sellPrice>buyPrice){
				up++;
			}
			profit = profit + (sellPrice-buyPrice)/buyPrice;
			String priceAfterwards = "";
			for(int x=0;x<5;x++)
				priceAfterwards = priceAfterwards + " " +getClose( todayStock,  offsetToDate.get(offset+x));
			out.println(date_0+" "+todayStock+" "+priceAfterwards);
		}
		out.println(up/total);
		out.println(up);
		out.println(total);
		out.println(profit);
		out.println();
		
	}

	public static void twoYangJiaYiYin()throws Exception {
		LinkedList<String> list = FileOperation.getFileNames(base);
		int offset = 0;
		for (int i = list.size() - 1; i > -1; i--) {
			dateToOffset.put(
					list.get(i).substring(0, list.get(i).indexOf(".")), offset);
			offsetToDate.put(offset,
					list.get(i).substring(0, list.get(i).indexOf(".")));
			offset--;
		}
		int holdingPeriod = 3;
		double total = 0;
		double up = 0;
		double profit = 0;
		ArrayList<String> buyList = new ArrayList<String>();
		ArrayList<String> sellList = new ArrayList<String>();
		for (offset = offset + 830; offset < 1; offset++) {			
			String date_2 = offsetToDate.get(offset-2);
			String date_1 = offsetToDate.get(offset-1);
			String date_0 = offsetToDate.get(offset);
			out.println(date_0);
			if(!date_0.startsWith("2016"))
				continue;
			double maxKey = -10;
			String todayStock = "";
			for (String symbol : DailyDataService.getSymbols(date_0)) {
				if(DailyDataService.getSymbols(date_2).contains(symbol) && DailyDataService.getSymbols(date_1).contains(symbol)){
					Bar bar_2 = getBar(symbol,date_2);
					Bar bar_1 = getBar(symbol,date_1);
					Bar bar_0 = getBar(symbol,date_0);
					if(bar_0.isYang()&&(!bar_1.isYang())&&bar_2.isYang())
						if(bar_0.getChange()>0&&bar_1.getChange()<=0&&bar_2.getChange()>0)
							if( bar_0.getVolume()>bar_1.getVolume()&&bar_0.getVolume()>bar_2.getVolume())
								if(bar_0.getClose()>bar_1.getOpen()&&bar_1.getOpen()>bar_2.getClose())
								//	if(bar_0.getOpen()>(bar_1.getClose()+0.3*(bar_1.getOpen()-bar_1.getClose())))
									//	if(bar_1.getBody()/bar_2.getBody()<0.25||bar_1.getBody()/bar_0.getBody()<0.125)
						//			if(bar_1.getBody()/bar_2.getClose()<0.003)
									if(bar_1.getClose()>(bar_2.getOpen()+0*(bar_2.getClose()-bar_2.getOpen())))
								//		if( bar_2.getClose()<getClose( symbol, offsetToDate.get(offset-10)))									
									//		if((bar_0.getBody()/bar_0.getLow())>0.03&&(bar_0.getBody()/bar_0.getLow())<0.07)
											{
												double maxVolume = 0;
												/*
												for(int x=1;x<=10;x++){
													double volume = getVolume( symbol,  offsetToDate.get(offset-x));
													if(volume>maxVolume)
														maxVolume = volume;
												}
												*/
												if(bar_0.getVolume()>maxVolume){
													if(getClose(symbol,  offsetToDate.get(offset+holdingPeriod))==-10 &&bar_0.getChange()>0.098)
															continue;
													String priceAfterwards=""+getOpen( symbol,  offsetToDate.get(offset+1));
													for(int x=1;x<4;x++)
														priceAfterwards = priceAfterwards + " " +getClose( symbol,  offsetToDate.get(offset+x));
													
													out.println(date_0+" "+symbol+" "+bar_1.getBody()/bar_2.getClose()+" "+priceAfterwards);
													double downFudu = 1-bar_2.getClose()/getClose(symbol, offsetToDate.get(offset-10))-1;
													double xiaYin = bar_1.getDownShadow()/bar_1.getBody();
													double shangYang = bar_0.getUpShadow()/bar_0.getBody();
													double volumeChange = bar_0.getVolume()/bar_1.getVolume();
													double body = bar_0.getBody()/bar_0.getLow();
													double key =xiaYin;
													
													if(key>maxKey){
														maxKey = key;
														todayStock = symbol;
														
													}
												}
											}																						
				}

			}
			if(todayStock.equals(""))
				continue;
			out.println(date_0+" "+todayStock);
			Bar bar = getBar(todayStock,date_0);
			
			double buyPrice =  getOpen(todayStock,  offsetToDate.get(offset+1));
			if(buyPrice<0)
				continue;
			if(getClose(todayStock,  offsetToDate.get(offset+1))<buyPrice&&getClose(todayStock,  offsetToDate.get(offset+1))>0){					
					buyPrice = (4*getOpen(todayStock,  offsetToDate.get(offset+1)) + 5*getClose(todayStock,  offsetToDate.get(offset+1)))/9;
				if(getClose(todayStock,  offsetToDate.get(offset+2))<buyPrice&&getClose(todayStock,  offsetToDate.get(offset+2))>0)
					buyPrice = (4*getOpen(todayStock,  offsetToDate.get(offset+1)) + 5*getClose(todayStock,  offsetToDate.get(offset+1))+6*getClose(todayStock,  offsetToDate.get(offset+2)))/15;
			}
			else{
				if(getClose(todayStock,  offsetToDate.get(offset+2))<buyPrice&&getClose(todayStock,  offsetToDate.get(offset+2))>0)
					buyPrice =  (4*getOpen(todayStock,  offsetToDate.get(offset+1)) + 5*getClose(todayStock,  offsetToDate.get(offset+2)))/9;
			}
			double sellPrice = getClose(todayStock,  offsetToDate.get(offset+holdingPeriod));
			if(sellPrice<0)
				continue;
			total++;
			if(sellPrice>buyPrice){
				up++;
			}
			profit = profit + (sellPrice-buyPrice)/buyPrice;
			String priceAfterwards = "";
			for(int x=0;x<5;x++)
				priceAfterwards = priceAfterwards + " " +getClose( todayStock,  offsetToDate.get(offset+x));
			out.println(date_0+" "+todayStock+" "+buyPrice+" "+sellPrice);
			buyList.add(offsetToDate.get(offset)
					+ "\t"
					+ (todayStock.startsWith("0") ? (todayStock + ".XSHE")
							: (todayStock + ".XSHG")));
			sellList.add(offsetToDate.get(offset + holdingPeriod)
					+ "\t"
					+ (todayStock.startsWith("0") ? (todayStock + ".XSHE")
							: (todayStock + ".XSHG")));
		}
		
		out.println(up/total);
		out.println(up);
		out.println(total);
		out.println(profit);
		out.println();
		for (String buyInfo : buyList) {
			out.println(buyInfo);
		}
		out.println();
		for (String sellInfo : sellList) {
			out.println(sellInfo);
		}
	}
	
	public static void longUnderShadowWithLargeVolumn() throws Exception {
		LinkedList<String> list = FileOperation.getFileNames(base);
		int offset = 0;
		for (int i = list.size() - 1; i > -1; i--) {
			dateToOffset.put(
					list.get(i).substring(0, list.get(i).indexOf(".")), offset);
			offsetToDate.put(offset,
					list.get(i).substring(0, list.get(i).indexOf(".")));
			offset--;

		}

		for (offset = offset + 1; offset < 1; offset++) {
			// if (offset != 0)
			// continue;
			String date = offsetToDate.get(offset);
			if (!date.startsWith("2016"))
				continue;
			// out.println(offset+"\t"+date);
			double maxRating = Double.MIN_VALUE;
			String maxStock = "-";
			for (String symbol : DailyDataService.getSymbols(date)) {
				if (!symbol.startsWith("6") && !symbol.startsWith("0"))
					continue;
				double pre_close_close = getClose(symbol, offset-2);
				double pre_close = getClose(symbol, offset-1);
				if(pre_close/pre_close_close>1.09)
					continue;

				double close = getClose(symbol, date);
				double low = getLow(symbol, date);
				double high = getHigh(symbol, date);
				double open = getOpen(symbol, date);
				if (low == open)
					continue;
				double body = Math.abs(close - open);
				double upperShadow = high - Math.max(close, open);
				double underShadow = Math.min(close, open) - low;
				double filter = underShadow / pre_close;
				if (filter < 0.04)
					continue;
				double ratio = underShadow / (body + upperShadow);
				try{
					if(getNetIncome(symbol, offset)<0)
						continue;
					out.println(date + "\t" + symbol + "\t" +filter+"\t"+(ratio*getNetIncome(symbol, offset)) +"\t"+ratio + "\t"+ getNetIncome(symbol, offset) +"\t"+getHigh(symbol, offset-15, offset-1)+"\t"+getClose(symbol, offset)+"\t"+getClose(symbol, offset+3));
				}catch(Exception e){continue;}
				/*
				 * if (!symbol.startsWith("6") && !symbol.startsWith("0"))
				 * continue; double twentyDaysBefore = getAdjustPriceF(symbol,
				 * offset - 20); if (twentyDaysBefore == -10.0) continue; double
				 * todayAdjustPrice = getAdjustPriceF(symbol, date); if
				 * (((todayAdjustPrice - twentyDaysBefore) / twentyDaysBefore) >
				 * -0.125) continue; double yesterdayClose1 = getClose(symbol,
				 * offset - 1); double yesterdayClose2 = getClose(symbol, offset
				 * - 2); double yesterdayClose3 = getClose(symbol, offset - 3);
				 * // if(yesterdayClose1==-10.0||yesterdayClose2==-10.0||
				 * yesterdayClose3==-10.0) // continue; //
				 * if(yesterdayClose1>yesterdayClose2
				 * ||yesterdayClose2>yesterdayClose3) // continue; if
				 * (((todayAdjustPrice - yesterdayClose1) / yesterdayClose1) >
				 * 0.05) continue; double close = getClose(symbol, date); double
				 * low = getLow(symbol, date); double high = getHigh(symbol,
				 * date); double open = getOpen(symbol, date); if (low == open)
				 * continue;
				 * 
				 * double body = close - open; // if(body>0.025||body<-0.025) //
				 * continue; double upperShadow = high - Math.max(close, open);
				 * // if(upperShadow>0.02) // continue; double underShadow =
				 * Math.min(close, open) - low; if ((underShadow / low) < 0.035)
				 * continue; double rating = underShadow / (Math.abs(body) +
				 * upperShadow); if (rating > maxRating) { maxRating = rating;
				 * maxStock = symbol; }
				 */
			}
			/*
			 * out.println(date + "\t" + maxStock + "\t" + maxRating); String
			 * result = ""; for (int i = offset; i < offset + 10; i++) { result
			 * = result + "\t" + getClose(maxStock, i); } out.println(result);
			 */
		}
	}

	public static void printMeiRiZhangTingGu() {
		for (int offset = -172; offset < 1; offset++) {
			String date = offsetToDate.get(offset);
			out.println("date: " + date);
			Set<String> set = DailyDataService.getSymbols(date);
			for (String symbol : set) {
				double close = getClose(symbol, date);
				double low = getLow(symbol, date);
				double high = getHigh(symbol, date);
				double open = getOpen(symbol, date);
				double pre_close = getClose(symbol,
						offsetToDate.get(offset - 1));
				double pre_pre_close = getClose(symbol,
						offsetToDate.get(offset - 2));
				if ((close / pre_close - 1) > 0.099
						&& (pre_close / pre_pre_close - 1) < 0.099)
					out.println(date + " " + symbol + " " + close + " "
							+ pre_close);

			}
		}
	}
	

	public static void main(String[] args) throws Exception {
		// "I am the Test"
		// renameFiles();

		loadData();
		//DailyDataService.loadDailyData();
		// printMeiRiZhangTingGu();
	  //longUnderShadowWithLargeVolumn();

		findHugeOutputButLittleIncreaseSimple(2, 5, 6);
		// moneyStratedy1();
		//moneyStratedy2();
		
		//twoYangJiaYiYin();
		//doubleTwoYangJiaYiYin();
	}
}
