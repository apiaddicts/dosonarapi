package apiquality.sonar.openapi.checks.format;

import org.sonar.check.Rule;

@Rule(key = OAR009DefaultRequestMediaTypeCheck.KEY)
public class OAR009DefaultRequestMediaTypeCheck extends AbstractDefaultMediaTypeCheck {

	public static final String KEY = "OAR009";
	private static final String MESSAGE = "generic.consume";

	public OAR009DefaultRequestMediaTypeCheck() {
		super(KEY, "consumes", MESSAGE);
	}
}
