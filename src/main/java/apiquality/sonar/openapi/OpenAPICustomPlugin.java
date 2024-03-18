package apiquality.sonar.openapi;

import org.sonar.api.Plugin;

/**
 * Entry point of your plugin containing your custom rules.
 */
public class OpenAPICustomPlugin implements Plugin {

	@Override
	public void define(Context context) {
		context.addExtensions(
				// server extensions -> objects are instantiated during server start
				OpenAPICustomProfileDefinition.class,
				OpenAPICustomRulesDefinition.class,
				// batch extensions -> objects are instantiated during code analysis
				OpenAPICustomRuleRepository.class
		);
	}

}
