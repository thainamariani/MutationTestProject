package metric;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Calculates Jaccard coefficient for two sets of items.
 * 
 * @author Thiago Nascimento
 * @since 2015-05-12
 * @version 1.0
 */
public class JaccardCoefficient {

	public JaccardCoefficient() {
		// empty
	}

	public double similarity(String[] x, String[] y) {
		if ((x == null || y == null)) {
			throw new IllegalArgumentException("The arguments x and y must be not NULL");
		} 
		
		return similarity(Arrays.asList(x), Arrays.asList(y));
	}

	public double similarity(List<String> x, List<String> y) {
		if (x.size() == 0 && y.size() == 0) {
			return 1.0;
		}

		Set<String> unionXY = new HashSet<String>(x);
		unionXY.addAll(y);

		Set<String> intersectionXY = new HashSet<String>(x);
		intersectionXY.retainAll(y);

		return (double) intersectionXY.size() / (double) unionXY.size();
	}
	
	protected double dissimilarity(List<String> x, List<String> y) {
		return 1.0 - similarity(x, y);
	}

	protected double dissimilarity(String[] x, String[] y) {
		return 1.0 - similarity(x, y);
	}

}
