package apiquality.sonar.openapi.checks.format;

import org.sonar.check.Rule;

@Rule(key = OAR010DefaultResponseMediaTypeCheck.KEY)
public class OAR010DefaultResponseMediaTypeCheck extends AbstractDefaultMediaTypeCheck {

	public static final String KEY = "OAR010";
	private static final String MESSAGE = "generic.produce";

	public OAR010DefaultResponseMediaTypeCheck() {
		super(KEY,"produces", MESSAGE);
	}
}
