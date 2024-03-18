package apiquality.sonar.openapi.checks.operations;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

@Rule(key = OAR015ResourceLevelMaxAllowedCheck.KEY)
public class OAR015ResourceLevelMaxAllowedCheck extends AbstractResourceLevelCheck {

	public static final String KEY = "OAR015";
	private static final int DEFAULT_MAX_LEVEL_ALLOWED_VALUE = 5;

	@RuleProperty(
			key = "max-level-allowed",
			description = "Default max level allowed value.",
			defaultValue = "" + DEFAULT_MAX_LEVEL_ALLOWED_VALUE,
			type = "INTEGER"
	)
	private Integer maxLevelAllowed = DEFAULT_MAX_LEVEL_ALLOWED_VALUE;

	public OAR015ResourceLevelMaxAllowedCheck() {
		super(KEY);
	}

	@Override
	boolean matchLevel(long level) {
		return maxLevelAllowed < level;
	}
}
