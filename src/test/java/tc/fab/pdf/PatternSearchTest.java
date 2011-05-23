package tc.fab.pdf;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PatternSearchTest {

	private String input;

	@Before
	public void setUp() throws Exception {

		input = "Imp-Cesar-38100-69.2010.5.03.0000" +
				"Nielsen-869640-62.2008.5.07.0000" +
				"Imp-Cesar-145600-98.2009.5.15.0000" +
				"Imp-Cesar-102-82.2010.5.24.0000" +
				"Imp-Cesar-103800-58.2009.5.09.0000" +
				"Imp-Beth-19-02.2010.5.12.0000" +
				"Imp-Beth-25400-13.2009.5.24.0000" +
				"Imp-Beth-27-97.2010.5.22.0000" +
				"Imp-Beth-397000-76.2009.5.04.0000" +
				"Imp-Beth-273800-32.2009.5.04.0000" +
				"Imp-Beth-40800-18.2010.5.03.0000";
		
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testOptions() {

		String r = null;

		if (r == null || r.length() < 1) {
			Assert.assertNull(r);
		}

	}

	@Test
	public void testSearch() {

		String pattern = "[0-9]{1,7}-[0-9]{2}\\.[0-9]{4}\\.[0-9]\\.[0-9]{2}\\.[0-9]{4}";

		Pattern p = Pattern.compile(pattern);

		Matcher m = p.matcher(input);
		
		if(m.find()){
			
			System.out.println(m.group());
			
		}

	}
	
	@Test
	public void testWriter() throws IOException{
		
		Writer output = new StringWriter();
		
		output.write("fabiano");
		
		output.close();

		Assert.assertTrue(output.toString().equals("fabiano"));
	}

}
