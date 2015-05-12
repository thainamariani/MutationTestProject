package test.metric;

import static org.junit.Assert.*;
import metric.JaccardCoefficient;
import org.junit.Assert;
import org.junit.Test;

public class JaccardCoefficientTest {

	 @Test
	 public void testShouldReturnCorrectValues() throws Exception {
		JaccardCoefficient coe = new JaccardCoefficient();
		
		assertEquals(0.5,coe.similarity(new String[] { "1", "1", "1" }, new String[] { "1", "0", "0" }), 0.0);
		Assert.assertEquals(1.0/3.0,coe.similarity(new String[] { "1", "2", "3" }, new String[] { "1" }), 0.0);
		
		// If A and B are both empty, we define J(A,B) = 1.
		// @see http://en.wikipedia.org/wiki/Jaccard_index
		assertEquals(1.0,coe.similarity(new String[]{}, new String[]{}), 0.0);
		
		assertEquals(0.0,coe.similarity(new String[]{"a"}, new String[]{}), 0.0);
		assertEquals(0.0,coe.similarity(new String[]{}, new String[]{"a"}), 0.0);
		
		// Inverse		
		double resultOne = coe.similarity(new String[] { "a", "b", "c" }, new String[] { "c", "d", "e" });
		double resultTwo = coe.similarity(new String[] { "c", "d", "e" }, new String[] { "a", "b", "c" });
		assertEquals(resultOne, resultTwo, 0.0);
	 }
}
