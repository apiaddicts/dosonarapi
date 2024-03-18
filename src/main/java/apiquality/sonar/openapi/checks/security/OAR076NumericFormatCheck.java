package apiquality.sonar.openapi.checks.security;

import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

@Rule(key = OAR076NumericFormatCheck.KEY)
public class OAR076NumericFormatCheck extends AbstractFormatCheck2 {

    public static final String KEY = "OAR076";
	private static final String MESSAGE = "OAR076.error";

    @Override
    public void validate(String type, String format, JsonNode typeNode) {
        System.out.println("TYPENODE: " + typeNode);
        if ("integer".equals(type)) {
            if (format == null) {
                addIssue(KEY, translate(MESSAGE), typeNode.key());
            } else if (!isValidIntegerFormat(format)) {
                addIssue(KEY, translate(MESSAGE), typeNode.key());
            }
        } else if ("number".equals(type)) {
            if (format == null) {
                addIssue(KEY, translate(MESSAGE), typeNode.key());
            } else if (!isValidNumberFormat(format)) {
                addIssue(KEY, translate(MESSAGE  ), typeNode.key());
            }
        }
    }

    private boolean isValidIntegerFormat(String format) {
        return "int32".equals(format) || "int64".equals(format);
    }

    private boolean isValidNumberFormat(String format) {
        return "float".equals(format) || "double".equals(format);
    }
}