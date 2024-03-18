package apiquality.sonar.openapi.checks;

import org.apiaddicts.apitools.dosonarapi.api.OpenApiCheck;
import org.apiaddicts.apitools.dosonarapi.api.PreciseIssue;
import apiquality.sonar.openapi.I18nContext;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class BaseCheck extends OpenApiCheck {

    private static ResourceBundle resourceBundle;

    protected final PreciseIssue addIssue(String key, String message, JsonNode node) {
        return addIssue(key + ": " + message, node);
    }

    protected final PreciseIssue addLineIssue(String key, String message, Integer lineNumber) {
        return addLineIssue(key + ": " + message, lineNumber);
    }

    protected static String translate(String key, Object ... vars) {
        if (resourceBundle == null) {
            I18nContext.initializeFromUserLanguage();
            resourceBundle = ResourceBundle.getBundle("messages.errors", new Locale(I18nContext.getLang()),
                    ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_DEFAULT));
        }
        String text = resourceBundle.getString(key);
        return MessageFormat.format(text, vars);
    }

}
