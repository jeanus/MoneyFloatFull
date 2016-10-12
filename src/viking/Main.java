package viking;

import static java.lang.System.out;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.NavigableMap;
import java.util.TreeMap;

public class Main {
	static HashMap<String, Integer> dateToOffset = new HashMap<String, Integer>();
	static HashMap<Integer, String> offsetToDate = new HashMap<Integer, String>();
	static String base = "E:\\360_Quant\\Quant_Strategy\\Be_A_Quant\\MoneyFloatStrategy\\FullText\\";
	static HashMap<String, HashMap<String, HashMap<String, Double>>> data = new HashMap<String, HashMap<String, HashMap<String, Double>>>();

	public static void renameFiles() throws Exception {
		String folder = "E:\\360_Quant\\Quant_Strategy\\Be_A_Quant\\MoneyFloatStrategy\\FullTextTemp";
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

	static int MAX_OFFSET;
	static String targetDate = "2016-10-11";

	public static void findHugeOutputButLittleIncreaseSimple(int buyOffset,
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
		//	if (offset != 0)
		//		continue;
			String date = offsetToDate.get(offset);
			if(!date.equals("2016-09-30")&&!date.equals("2016-09-29")&&!date.equals("2016-09-28"))
				continue;
			// out.println(offset+"\t"+date);
			double maxRating = Double.MIN_VALUE;
			String maxStock = "-";
			for (String symbol : DailyDataService.getSymbols(date)) {
				if (!symbol.startsWith("6") && !symbol.startsWith("0"))
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
				double filter = underShadow/low;
				if(filter<0.04)
					continue;
				double ratio = underShadow/(body+upperShadow);
				out.println(date+"\t"+symbol+"\t"+ratio+"\t");
				
				/*
				if (!symbol.startsWith("6") && !symbol.startsWith("0"))
					continue;
				double twentyDaysBefore = getAdjustPriceF(symbol, offset - 20);
				if (twentyDaysBefore == -10.0)
					continue;
				double todayAdjustPrice = getAdjustPriceF(symbol, date);
				if (((todayAdjustPrice - twentyDaysBefore) / twentyDaysBefore) > -0.125)
					continue;
				double yesterdayClose1 = getClose(symbol, offset - 1);
				double yesterdayClose2 = getClose(symbol, offset - 2);
				double yesterdayClose3 = getClose(symbol, offset - 3);
				// if(yesterdayClose1==-10.0||yesterdayClose2==-10.0||yesterdayClose3==-10.0)
				// continue;
				// if(yesterdayClose1>yesterdayClose2||yesterdayClose2>yesterdayClose3)
				// continue;
				if (((todayAdjustPrice - yesterdayClose1) / yesterdayClose1) > 0.05)
					continue;
				double close = getClose(symbol, date);
				double low = getLow(symbol, date);
				double high = getHigh(symbol, date);
				double open = getOpen(symbol, date);
				if (low == open)
					continue;

				double body = close - open;
				// if(body>0.025||body<-0.025)
				// continue;
				double upperShadow = high - Math.max(close, open);
				// if(upperShadow>0.02)
				// continue;
				double underShadow = Math.min(close, open) - low;
				if ((underShadow / low) < 0.035)
					continue;
				double rating = underShadow / (Math.abs(body) + upperShadow);
				if (rating > maxRating) {
					maxRating = rating;
					maxStock = symbol;
				}
				*/
			}
			/*
			out.println(date + "\t" + maxStock + "\t" + maxRating);
			String result = "";
			for (int i = offset; i < offset + 10; i++) {
				result = result + "\t" + getClose(maxStock, i);
			}
			out.println(result);
			*/
		}
	}

	public static void main(String[] args) throws Exception {
		//renameFiles();
		
		//DailyDataService.loadData();
		//longUnderShadowWithLargeVolumn();
		
		 loadData();
		 findHugeOutputButLittleIncreaseSimple(2, 5, 6);
	}
}
