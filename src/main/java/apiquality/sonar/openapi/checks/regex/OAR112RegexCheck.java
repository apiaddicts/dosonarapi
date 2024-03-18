package apiquality.sonar.openapi.checks.regex;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import apiquality.sonar.openapi.checks.BaseCheck;

import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.impl.MissingNode;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Rule(key = OAR112RegexCheck.KEY)
public class OAR112RegexCheck extends BaseCheck {

    public static final String KEY = "OAR112";
    private static final String NODE = "info/description";
    private static final String ERROR_MESSAGE = "The field must start with an uppercase letter.";
    private static final String VALIDATION = "^[A-Z].*";

    @RuleProperty(
        key = "Node",
        description = "Specify the sections of the OpenAPI document to be validated. This defines the scope for the regular expression validation.",
        defaultValue = NODE
    )
    private String nodes = NODE;

    @RuleProperty(
        key = "Error Message",
        description = "Enter a custom error message that will appear if the rule is violated.",
        defaultValue = ERROR_MESSAGE
    )
    private String errorMessage = ERROR_MESSAGE;

    @RuleProperty(
        key= "Validation",
        description= "Provide the regular expression that will be used to validate the specified sections of the OpenAPI document.",
        defaultValue = VALIDATION
    )
    private String valid = VALIDATION;

    String[] segments = NODE.split("/");

    String segment1 = segments.length > 0 ? segments[0] : null;
    String segment2 = segments.length > 1 ? segments[1] : null;
    String segment3 = segments.length > 2 ? segments[2] : null;
    String segment4 = segments.length > 3 ? segments[3] : null;
    String segment5 = segments.length > 4 ? segments[4] : null;

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(
            OpenApi3Grammar.ROOT,
            OpenApi3Grammar.PATHS,
            OpenApi3Grammar.OPERATION,
            OpenApi3Grammar.INFO,
            OpenApi3Grammar.RESPONSES,
            OpenApi3Grammar.PARAMETER,
            OpenApi3Grammar.COMPONENTS,
            OpenApi3Grammar.REQUEST_BODY,
            OpenApi3Grammar.SCHEMA,
            OpenApi3Grammar.SECURITY_SCHEME,
            OpenApi3Grammar.TAG,
            OpenApi3Grammar.SERVER,
            OpenApi3Grammar.EXTERNAL_DOC,
            OpenApi2Grammar.ROOT,
            OpenApi2Grammar.PATHS,
            OpenApi2Grammar.OPERATION,
            OpenApi2Grammar.INFO,
            OpenApi2Grammar.RESPONSES,
            OpenApi2Grammar.PARAMETER,
            OpenApi2Grammar.DEFINITIONS,
            OpenApi2Grammar.SCHEMA,
            OpenApi2Grammar.SECURITY_SCHEME,
            OpenApi2Grammar.TAG,
            OpenApi2Grammar.EXTERNAL_DOC

        );
    }

    @Override
    public void visitNode(JsonNode node) {
        List<String> pathSegments = Arrays.asList(nodes.split("/"));
        String[] pathSegmentsArray = nodes.split("/");

        if (pathSegments.contains("info") && (OpenApi3Grammar.ROOT.equals(node.getType()) || OpenApi2Grammar.ROOT.equals(node.getType()))){
            handleInfoSection(node, pathSegments);
        } else if (pathSegments.contains("servers") && (OpenApi3Grammar.SERVER.equals(node.getType()))) {
            handleServerNode(node, pathSegments); 
        } else if (pathSegmentsArray.length > 1 && "paths".equals(pathSegmentsArray[0]) &&
        (pathSegmentsArray[1].equals("get") || pathSegmentsArray[1].equals("post") || pathSegmentsArray[1].equals("put") || 
        pathSegmentsArray[1].equals("patch") || pathSegmentsArray[1].equals("delete")) &&
        (OpenApi3Grammar.OPERATION.equals(node.getType()) || OpenApi2Grammar.OPERATION.equals(node.getType()))) {
            handleOperationsNode(node, pathSegments);
        } else if ((pathSegments.contains("tags")) && (OpenApi3Grammar.TAG.equals(node.getType()) || OpenApi2Grammar.TAG.equals(node.getType()))) {
            handleTagsNode(node, pathSegments);
        } else if ((pathSegments.contains("externalDocs")) && (OpenApi3Grammar.EXTERNAL_DOC.equals(node.getType()) || OpenApi2Grammar.EXTERNAL_DOC.equals(node.getType()))) {
            handleExternalDocsNode(node, pathSegments);
        } else if (pathSegmentsArray.length > 2 && "paths".equals(pathSegmentsArray[0]) &&
        (pathSegmentsArray[1].equals("get") || pathSegmentsArray[1].equals("post") || pathSegmentsArray[1].equals("put") || 
        pathSegmentsArray[1].equals("patch") || pathSegmentsArray[1].equals("delete")) && "parameters".equals(pathSegmentsArray[2]) && 
        (OpenApi3Grammar.PARAMETER.equals(node.getType()) || OpenApi2Grammar.PARAMETERS.equals(node.getType()))) {
            handleParametersNode(node, pathSegments);
        }
    }

    private void handleInfoSection(JsonNode node, List<String> pathSegments) {
        JsonNode infoNode = node.get("info");
        validateSection(infoNode, pathSegments, Arrays.asList("title", "description", "termsOfService", "contact", "license", "version"));
    }

    private void handleServerNode(JsonNode serverNode, List<String> pathSegments) {
        validateSection(serverNode, pathSegments, Arrays.asList("url", "description"));
    }

    private void handleOperationsNode(JsonNode operationsNode, List<String> pathSegments) {
        if ("responses".equals(segment3)) {
            JsonNode responsesNode = operationsNode.get("responses");
            if (responsesNode != null && !responsesNode.isMissing()) {
                String statusCodeToCheck = segment4;
                JsonNode responseNode = responsesNode.get(statusCodeToCheck);
                if (responseNode != null && !responseNode.isMissing()) {
                    JsonNode descriptionNode = responseNode.get("description");
                    if (descriptionNode != null && !descriptionNode.isMissing() && !descriptionNode.getTokenValue().matches(valid)) {
                        addIssue(KEY, errorMessage, descriptionNode.key());
                    }
                }
            }
        }
        else{
            validateSection(operationsNode, pathSegments, Arrays.asList("summary", "description", "operationId"));
        }
    }

    private void handleParametersNode(JsonNode parametersNode, List<String> pathSegments){
        validateSection(parametersNode, pathSegments, Arrays.asList("description"));
    }
    private void handleTagsNode(JsonNode tagsNode, List<String> pathSegments) {
        validateSection(tagsNode, pathSegments, Arrays.asList("name", "description"));   
    }
    private void handleExternalDocsNode(JsonNode externalDocNode, List<String> pathSegments) {
        validateSection(externalDocNode, pathSegments, Arrays.asList("url", "description"));   
    }

    private void validateSection(JsonNode parentNode, List<String> pathSegments, List<String> keysToValidate) {
        for (String key : keysToValidate) {
            if (pathSegments.contains(key)) {
                validateNodeWithRegex(parentNode, key, valid);
            }
        }
    }

    private void validateNodeWithRegex(JsonNode parentNode, String childKey, String validation) {
        JsonNode childNode = parentNode.get(childKey);
        boolean validationAsBoolean = "true".equals(validation) || "false".equals(validation);
    
        if (validationAsBoolean && "false".equals(validation)) {
            if (!(childNode == null || childNode instanceof MissingNode)) {
                addIssue(KEY, errorMessage, childNode.key());
            }
        } else if (validationAsBoolean && "true".equals(validation)) {
            if (childNode == null || childNode instanceof MissingNode || childNode.getTokenValue().isEmpty()) {
                addIssue(KEY, "Expected to find a value but didn't.", parentNode.key());
            }
        } else {
            if (childNode != null && !(childNode instanceof MissingNode) && !childNode.getTokenValue().matches(validation)) {
                addIssue(KEY, errorMessage, childNode.key());
            }
        }
    }
}