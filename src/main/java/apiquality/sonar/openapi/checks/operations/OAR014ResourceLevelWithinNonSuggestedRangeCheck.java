package apiquality.sonar.openapi.checks.operations;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

@Rule(key = OAR014ResourceLevelWithinNonSuggestedRangeCheck.KEY)
public class OAR014ResourceLevelWithinNonSuggestedRangeCheck extends AbstractResourceLevelCheck {

	public static final String KEY = "OAR014";
	private static final int DEFAULT_MIN_LEVEL_VALUE = 4;
	private static final int DEFAULT_MAX_LEVEL_VALUE = 5;

	@RuleProperty(
			key = "min-level",
			description = "Default min level value.",
			defaultValue = "" + DEFAULT_MIN_LEVEL_VALUE,
			type = "INTEGER"
	)
	private Integer minLevel = DEFAULT_MIN_LEVEL_VALUE;

	@RuleProperty(
			key = "max-level",
			description = "Default max level value.",
			defaultValue = "" + DEFAULT_MAX_LEVEL_VALUE,
			type = "INTEGER"
	)
	private Integer maxLevel = DEFAULT_MAX_LEVEL_VALUE;

	public OAR014ResourceLevelWithinNonSuggestedRangeCheck() {
		super(KEY);
	}

	@Override
	boolean matchLevel(long level) {
		return minLevel <= level && maxLevel >= level;
	}
}
