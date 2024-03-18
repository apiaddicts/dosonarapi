package apiquality.sonar.openapi.checks.format;

import org.sonar.check.Rule;

@Rule(key = OAR006UndefinedRequestMediaTypeCheck.KEY)
public class OAR006UndefinedRequestMediaTypeCheck extends AbstractUndefinedMediaTypeCheck {

	public static final String KEY = "OAR006";

	public OAR006UndefinedRequestMediaTypeCheck() {
		super(KEY, "consumes");
	}
}
