import java.util.Arrays;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.junit.Test;

public class TestClass {
	private final static Logger logger = Logger.getLogger(Functional_Intereface.class.getName());
	@Test
	public void pureFunctionTest() {
		List<Integer> inputList = Arrays.asList(1,2,3,4,5,6,7,8);

		List<Integer> resultList = inputList.stream().filter(n -> n > 5).collect(Collectors.toList());
	}

	@Test
	public void CollectorsTest() {
		List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
		Set<Integer> set = list.stream().collect(Collectors.toSet());
		Optional<Integer> minNum = list.stream().min(Comparator.comparingInt(Integer::intValue));
		logger.info(String.valueOf(minNum));

		long count = list.stream().collect(Collectors.counting());
		logger.info(String.valueOf(count));

		int sum = list.stream().collect(Collectors.summingInt(Integer::intValue));
		logger.info(String.valueOf(sum));

		double avg = list.stream().collect(Collectors.averagingInt(Integer::intValue));
		logger.info(String.valueOf(avg));

		//Optional<Integer> min = list.stream().collect(Collectors.minBy((n, v) -> Integer.compare(n, v)));
		Optional<Integer> min = list.stream().collect(Collectors.minBy(Comparator.comparingInt(Integer::intValue)));
		logger.info(String.valueOf(min.get()));

		IntSummaryStatistics stat = list.stream().collect(Collectors.summarizingInt(Integer::intValue));
		logger.info(stat.toString());

		Map<Integer, List<Integer>> map = list.stream().collect(Collectors.groupingBy(Integer::intValue));
		logger.info(String.valueOf(map));

		Map<Integer, Integer> sumMap = list.stream().collect(Collectors.groupingBy(Integer::intValue, Collectors.summingInt(Integer::intValue)));
		logger.info(String.valueOf(sumMap));

		Map<Integer, Double> avgMap = list.stream().collect(Collectors.groupingBy(Integer::intValue, Collectors.averagingInt(Integer::intValue)));
		logger.info(String.valueOf(avgMap));

		Map<Integer, Optional<Integer>> maxMap = list.stream().collect(Collectors.groupingBy(Integer::intValue, Collectors.maxBy(Comparator.comparingInt(Integer::intValue))));
		logger.info(String.valueOf(avgMap));

		Map<Boolean, List<Integer>> partMap = list.stream().collect(Collectors.partitioningBy(n -> n > 5));
		logger.info(String.valueOf(partMap));
	}
}
