package apiquality.sonar.openapi;

public class I18nContext {

    private I18nContext() {
        // Intentional blank
    }

    private static final String DEFAULT_LANG = "en";

    private static String lang;

    public static String getLang() {
        return lang == null ? DEFAULT_LANG : lang;
    }

    public static void initializeFromUserLanguage() {
        if (lang == null) setLang(System.getProperty("user.language"));
    }

    public static void setLang(String lang) {
        I18nContext.lang = lang;
    }

}
