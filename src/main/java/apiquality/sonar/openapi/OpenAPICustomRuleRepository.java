package apiquality.sonar.openapi;

import org.sonar.api.ExtensionPoint;
import org.sonar.api.scanner.ScannerSide;
import org.apiaddicts.apitools.dosonarapi.api.OpenApiCustomRuleRepository;
import apiquality.sonar.openapi.checks.RulesLists;
import org.sonarsource.api.sonarlint.SonarLintSide;

import java.util.List;

import static apiquality.sonar.openapi.OpenAPICustomRulesDefinition.REPOSITORY_KEY;

/**
 * Makes the rules visible to the OpenAPI scanner sensor,
 * hence adds to the classes that are going to be executed during source code analysis.
 * <p>
 * This class is a batch extension by implementing the {@link OpenApiCustomRuleRepository}
 */
@SonarLintSide
@ScannerSide
@ExtensionPoint
public class OpenAPICustomRuleRepository implements OpenApiCustomRuleRepository {
	@Override
	public String repositoryKey() {
		return REPOSITORY_KEY;
	}

	@Override
    public List<Class<?>> checkClasses() {
		return RulesLists.getAllChecks();
    }
}
