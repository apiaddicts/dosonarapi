package apiquality.sonar.openapi.checks;

import apiquality.sonar.openapi.checks.apim.wso2.*;
import apiquality.sonar.openapi.checks.format.*;
import apiquality.sonar.openapi.checks.schemas.*;
import apiquality.sonar.openapi.checks.examples.*;
import apiquality.sonar.openapi.checks.owasp.*;
import apiquality.sonar.openapi.checks.parameters.*;
import apiquality.sonar.openapi.checks.regex.OAR112RegexCheck;
import apiquality.sonar.openapi.checks.security.*;
import apiquality.sonar.openapi.checks.operations.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public final class RulesLists {

    private RulesLists() {
    }

    public static List<Class<?>> getFormatChecks() {
        return Arrays.asList(
            OAR006UndefinedRequestMediaTypeCheck.class,
            OAR007UndefinedResponseMediaTypeCheck.class,
            OAR009DefaultRequestMediaTypeCheck.class,
            OAR010DefaultResponseMediaTypeCheck.class,
            OAR011UrlNamingConventionCheck.class,
            OAR037StringFormatCheck.class,
            OAR066SnakeCaseNamingConventionCheck.class,
            OAR067CamelCaseNamingConventionCheck.class,
            OAR068PascalCaseNamingConventionCheck.class,
            OAR077ParametersInQuerySnakeCaseCheck.class,
            OAR086DescriptionFormatCheck.class,
            OAR087SummaryFormatCheck.class,
            OAR088RefParamCheck.class,
            OAR089RefRequestBodyCheck.class,
            OAR090RefResponseCheck.class,
            OAR097ShortBasePathCheck.class,
            OAR098LongBasePathCheck.class,
            OAR099ApiPrefixBasePathCheck.class,
            OAR100LastPartBasePathCheck.class,
            OAR101FirstPartBasePathCheck.class,
            OAR102SecondPartBasePathCheck.class,
            OAR044MediaTypeCheck.class,
            OAR050ProvideOpSummaryCheck.class,
            OAR051DescriptionDiffersSummaryCheck.class,
            OAR110LicenseInformationCheck.class,
            OAR111ContactInformationCheck.class
        );
    }

    public static List<Class<?>> getParametersChecks() {
        return Arrays.asList(
            OAR019SelectParameterCheck.class,
            OAR020ExpandParameterCheck.class,
            OAR021ExcludeParameterCheck.class,
            OAR022OrderbyParameterCheck.class,
            OAR023TotalParameterCheck.class,
            OAR024StartParameterCheck.class,
            OAR025LimitParameterCheck.class,
            OAR026TotalParameterDefaultValueCheck.class,
            OAR028FilterParameterCheck.class,
            OAR060QueryParametersOptionalCheck.class,
            OAR069PathParamAndQueryCheck.class  );
    }            


    public static List<Class<?>> getSecurityChecks() {
        return Arrays.asList(
            OAR001MandatoryHttpsProtocolCheck.class,
            OAR033HttpHeadersCheck.class,
            OAR035UnauthorizedResponseCheck.class,
            OAR036SessionMechanismsCheck.class,
            OAR054HostCheck.class,
            OAR072NonOKModelResponseCheck.class,
            OAR074NumericParameterIntegrityCheck.class,
            OAR075StringParameterIntegrityCheck.class,
            OAR076NumericFormatCheck.class,
            OAR078VerbsSecurityCheck.class,
            OAR079PathParameter404Check.class,
            OAR081PasswordFormatCheck.class,
            OAR082BinaryOrByteFormatCheck.class,
            OAR083ForbiddenQueryParamsCheck.class,
            OAR084ForbiddenFormatsInQueryCheck.class,
            OAR085OpenAPIVersionCheck.class,
			OAR096ForbiddenResponseCheck.class,
            OAR045DefinedResponseCheck.class,
            OAR049NoContentIn204Check.class
        );
    }

    public static List<Class<?>> getOperationsChecks() {
        return Arrays.asList(
            OAR008AllowedHttpVerbCheck.class,
            OAR013DefaultResponseCheck.class,
            OAR014ResourceLevelWithinNonSuggestedRangeCheck.class,
            OAR015ResourceLevelMaxAllowedCheck.class,
            OAR017ResourcePathCheck.class,
            OAR027PostResponseLocationHeaderCheck.class,
            OAR030StatusEndpointCheck.class,
            OAR032AmbiguousElementsPathCheck.class,
            OAR038StandardCreateResponseCheck.class,
            OAR061GetMethodCheck.class,
            OAR062PostMethodCheck.class,
            OAR063PutMethodCheck.class,
            OAR064PatchMethodCheck.class,
            OAR065DeleteMethodCheck.class,
            OAR071GetQueryParamsDefinedCheck.class,
            OAR091ParamOnlyRefCheck.class,
            OAR092RequestBodyOnlyRefCheck.class,
            OAR093ResponseOnlyRefCheck.class,
            OAR103ResourcesByGetVerbCheck.class,
            OAR104ResourcesByPostVerbCheck.class,
            OAR105ResourcesByPutVerbCheck.class,
            OAR106ResourcesByPatchVerbCheck.class,
            OAR107ResourcesByDeleteVerbCheck.class,
            OAR043ParsingErrorCheck.class,
            OAR046DeclaredTagCheck.class,
            OAR047DocumentedTagCheck.class,
            OAR109ForbiddenInternalServerErrorCheck.class
        );
    }

    public static List<Class<?>> getSchemasChecks() {
        return Arrays.asList(
            OAR029StandardResponseSchemaCheck.class,
            OAR034StandardPagedResponseSchemaCheck.class,
            OAR080SecuritySchemasCheck.class,
            OAR108SchemaValidatorCheck.class
            );
    }

    public static List<Class<?>> getRegexChecks() {
        return Arrays.asList(
            OAR112RegexCheck.class
            );
    }

    public static List<Class<?>> getExamplesChcecks() {
        return Arrays.asList(
            OAR031ExamplesCheck.class,
            OAR094UseExamplesCheck.class
        );
    }

    public static List<Class<?>> getOWASPChecks() {
        return Arrays.asList(
            OAR070BrokenAccessControlCheck.class,
            OAR073RateLimitCheck.class
        );
    }
    public static List<Class<?>> getWSO2Checks() {
        return Arrays.asList(
            OAR002ValidWso2ScopesCheck.class,
            OAR003DefinedWso2ScopesDescriptionCheck.class,
            OAR004ValidWso2ScopesRolesCheck.class,
            OAR005UndefinedWso2ScopeUseCheck.class,
            OAR040StandardWso2ScopesNameCheck.class,
            OAR041UndefinedAuthTypeForWso2ScopeCheck.class
        );
    }

    public static List<Class<?>> getAllChecks() {
        List<Class<?>> allChecks = new LinkedList<>();
        allChecks.addAll(getFormatChecks());
        allChecks.addAll(getParametersChecks());
        allChecks.addAll(getSchemasChecks());
        allChecks.addAll(getExamplesChcecks());
        allChecks.addAll(getOWASPChecks());
        allChecks.addAll(getSecurityChecks());
        allChecks.addAll(getOperationsChecks());
        allChecks.addAll(getWSO2Checks());
        allChecks.addAll(getRegexChecks());
        return allChecks;
    }
}