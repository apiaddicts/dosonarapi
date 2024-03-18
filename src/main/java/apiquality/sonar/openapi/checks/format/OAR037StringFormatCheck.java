package apiquality.sonar.openapi.checks.format;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Rule(key = OAR037StringFormatCheck.KEY)
public class OAR037StringFormatCheck extends AbstractFormatCheck {

    public static final String KEY = "OAR037";
    private static final String MESSAGE = "OAR037.error";
    private static final String DEFAULT_FORMATS_ALLOWED = "date,date-time,password,byte,binary,email,uuid,uri,hostname,ipv4,ipv6,HEX,HEX(16)";

    @RuleProperty(
            key = "formats-allowed",
            description = "List of allowed formats for string types (separated by comma)",
            defaultValue = DEFAULT_FORMATS_ALLOWED
    )
    private String formatsAllowed = DEFAULT_FORMATS_ALLOWED;

    @Override
    public void validate(String type, String format, JsonNode typeNode) {
        Set<String> validFormats = Stream.of(formatsAllowed.split(","))
                .map(f -> f.trim().toLowerCase())
                .collect(Collectors.toSet());

        if (isInvalidString(type, format, validFormats)) {
            addIssue(KEY, translate(MESSAGE), typeNode.key());
        }
    }

    private boolean isInvalidString(String type, String format, Set<String> validFormats) {
        return "string".equals(type) && (format != null && !validFormats.contains(format.toLowerCase()));
    }
}