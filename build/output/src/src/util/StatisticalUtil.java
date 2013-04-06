package util;

import java.util.List;

public class StatisticalUtil {
	public static double mean(List<Double> array) { // 산술 평균 구하기
		double sum = 0.0;

		for (int i = 0; i < array.size(); i++)
			sum += array.get(i);

		return sum / array.size();
	}

	public static double standardDeviation(List<Double> array, int option) {
		if (array.size() < 2)
			return Double.NaN;

		double sum = 0.0;
		double sd = 0.0;
		double diff;
		double meanValue = mean(array);

		for (int i = 0; i < array.size(); i++) {
			diff = array.get(i) - meanValue;
			sum += diff * diff;
		}
		sd = Math.sqrt(sum / (array.size()- option));

		return sd;
	}
}
