package apiquality.sonar.openapi.checks.format;

import org.sonar.check.Rule;

@Rule(key = OAR007UndefinedResponseMediaTypeCheck.KEY)
public class OAR007UndefinedResponseMediaTypeCheck extends AbstractUndefinedMediaTypeCheck {

	public static final String KEY = "OAR007";

	public OAR007UndefinedResponseMediaTypeCheck() {
		super(KEY, "produces");
	}
}
