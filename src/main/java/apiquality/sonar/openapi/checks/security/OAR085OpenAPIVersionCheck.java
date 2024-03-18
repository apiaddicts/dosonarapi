package apiquality.sonar.openapi.checks.security;

import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import apiquality.sonar.openapi.checks.BaseCheck;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.Set;
import java.util.List;
import java.util.Arrays;
import com.google.common.collect.ImmutableSet;

@Rule(key = OAR085OpenAPIVersionCheck.KEY)
public class OAR085OpenAPIVersionCheck extends BaseCheck {

    public static final String KEY = "OAR085";
    private static final String MESSAGE = "OAR085.error";
    private static final String DEFAULT_VALID_VERSIONS = "2.0,3.0.0,3.0.1,3.0.2,3.0.3,3.1";

    @RuleProperty(
            key = "valid-versions",
            description = "Valid OpenAPI versions",
            defaultValue = DEFAULT_VALID_VERSIONS
    )
    private String validVersionsStr = DEFAULT_VALID_VERSIONS;

    private final List<String> validVersions = Arrays.asList(validVersionsStr.split(","));

    @Override
    protected void visitFile(JsonNode root) {
        JsonNode swaggerNode = root.get("swagger");
        JsonNode openapiNode = root.get("openapi");

        String version = getVersion(swaggerNode, openapiNode);
        
        System.out.println("OpenAPI Version: " + version); 

        if (version == null || !validVersions.contains(version)) {
            addIssue(KEY, translate(MESSAGE, version), root.key());
        }
    }

    private String getVersion(JsonNode swagger, JsonNode openapi) {
        if (swagger != null && !"null".equals(swagger.getTokenValue())) {
            return swagger.getTokenValue();
        }
        
        if (openapi != null && !"null".equals(openapi.getTokenValue())) {
            return openapi.getTokenValue();
        }
        
        return null;
    }

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(OpenApi2Grammar.ROOT, OpenApi3Grammar.ROOT); 
    }
}