package tc.fab.firma.utils;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.math.NumberRange;

public class FormatUtils {

	public static String format(Number number) {

		if (number == null) { return ""; }

		Map<String, NumberRange> mapRange = new HashMap<String, NumberRange>();
		mapRange.put("bytes", new NumberRange(0, 3));
		mapRange.put("KB", new NumberRange(3, 6));
		mapRange.put("MB", new NumberRange(6, 9));
		mapRange.put("GB", new NumberRange(9, 12));
		Set<String> keys = mapRange.keySet();

		String unit = null;

		NumberRange range = null;

		Integer log;
		if (number.intValue() > 0) {
			log = (int) Math.floor(Math.log10(number.doubleValue()));
		} else {
			log = 0;
		}

		for (String key : keys) {
			range = mapRange.get(key);
			if (range.containsNumber(log) && !log.equals(range.getMaximumNumber())) {
				unit = key;
				break;
			}
		}

		Integer index = range.getMinimumInteger() / 3;
		number = number.doubleValue() / Math.pow(1024d, index);
		DecimalFormat formatter = new DecimalFormat("0.0");

		return formatter.format(number) + " " + unit;

	}

}
