package tc.fab.pdf.signer.options;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

public class PlainDescription {

	private SignerOptions options;
	private String name;
	private String template;

	public PlainDescription(SignerOptions options) {
		this.options = options;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getDate() {
		GregorianCalendar signDate = new GregorianCalendar(new Locale("pt",
				"BR"));
		SimpleDateFormat sd = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z",
				new Locale("pt", "BR"));
		return sd.format(signDate.getTime());
	}

	public String getDate(GregorianCalendar signDate) {
		signDate = new GregorianCalendar(new Locale("pt", "BR"));
		SimpleDateFormat sd = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z",
				new Locale("pt", "BR"));
		return sd.format(signDate);
	}

	public String getReason() {
		return options.getReason();
	}

	public String getLocation() {
		return options.getLocal();
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getTemplate() {
		return template;
	}

}
