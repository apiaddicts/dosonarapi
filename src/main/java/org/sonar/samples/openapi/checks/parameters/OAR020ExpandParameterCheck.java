package org.sonar.samples.openapi.checks.parameters;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

import static org.sonar.samples.openapi.utils.VerbPathMatcher.GET_ALL_1ST_LEVEL;
import static org.sonar.samples.openapi.utils.VerbPathMatcher.GET_ALL_2ND_LEVEL;
import static org.sonar.samples.openapi.utils.VerbPathMatcher.GET_ALL_3RD_LEVEL;
import static org.sonar.samples.openapi.utils.VerbPathMatcher.GET_ONE_1ST_LEVEL;
import static org.sonar.samples.openapi.utils.VerbPathMatcher.GET_ONE_2ND_LEVEL;
import static org.sonar.samples.openapi.utils.VerbPathMatcher.GET_ONE_3RD_LEVEL;

@Rule(key = OAR020ExpandParameterCheck.KEY)
public class OAR020ExpandParameterCheck extends AbstractParameterCheck {

	public static final String KEY = "OAR020";
	private static final String PARAM = "$expand";
	private static final String DEFAULT_PATTERN = 
		GET_ALL_1ST_LEVEL + GET_ALL_2ND_LEVEL + GET_ALL_3RD_LEVEL + 
		GET_ONE_1ST_LEVEL + GET_ONE_2ND_LEVEL + GET_ONE_3RD_LEVEL;
	private static final String DEFAULT_EXCLUSION = "get:/status";

	@RuleProperty(
			key = "resources-paths",
			description = "List of resources paths to apply the rule. Format: <V>,<V>:<RX>,<RX>;<V>,<V>:<RX>,<RX>",
			defaultValue = DEFAULT_PATTERN
	)
	private String pattern = DEFAULT_PATTERN;

	@RuleProperty(
			key = "resources-exclusions",
			description = "List of explicit resources to exclude from this rule. Format: <V>,<V>:<R>,<R>;<V>,<V>:<R>,<R>",
			defaultValue = DEFAULT_EXCLUSION
	)
	private String exclusion = DEFAULT_EXCLUSION;

	public OAR020ExpandParameterCheck() {
		super(KEY, PARAM);
		super.verbPathPattern = pattern;
		super.verbExclusions = exclusion;
	}
}
