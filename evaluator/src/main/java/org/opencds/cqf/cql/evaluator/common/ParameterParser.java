package org.opencds.cqf.cql.evaluator.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.tuple.Pair;
import org.cqframework.cql.elm.execution.VersionedIdentifier;
import org.opencds.cqf.cql.engine.execution.LibraryLoader;


/**
 * The default implementation of the ParameterParser is currently just a pass through of the strings.
 * The intention is for it to eventually support CQL-defined types, and provide an interface for supporting
 * other types, such as FHIR resources. NOTE: in order to support picking the correct serializer this class
 * needs the context of which Library the parameters are defined on so that it can pick the correct type.
 */
public class ParameterParser implements org.opencds.cqf.cql.evaluator.ParameterParser {
    @Override
    public Pair<String, Object> parseContextParameter(LibraryLoader libraryLoader, VersionedIdentifier libraryIdentifier, Pair<String, String> contextParameter) {
        if (contextParameter == null) {
            return null;
        }

        return Pair.of(contextParameter.getLeft(), 
            this.parseContextParameter(libraryLoader, libraryIdentifier, contextParameter.getLeft(), contextParameter.getRight()));
    }

    @Override
    public Map<String, Object> parseParameters(LibraryLoader libraryLoader, VersionedIdentifier libraryIdentifier, Map<String, String> parameters) {
        if (parameters == null) {
            return null;
        }

        Map<String, Object> deseralized = new HashMap<>();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            deseralized.put(entry.getKey(), this.parseParameter(libraryLoader, libraryIdentifier, entry.getKey(), entry.getValue()));
        }
        return deseralized;
    }

    @Override
    public Object parseContextParameter(LibraryLoader libraryLoader, VersionedIdentifier libraryIdentifier, String context, String contextValue) {
        Objects.requireNonNull(libraryIdentifier);
        Objects.requireNonNull(context);
        Objects.requireNonNull(contextValue);

        return contextValue;
    }

    @Override
    public Object parseParameter(LibraryLoader libraryLoader, VersionedIdentifier libraryIdentifier, String parameterName, String parameterValue) {
        Objects.requireNonNull(libraryIdentifier);
        Objects.requireNonNull(parameterName);
        Objects.requireNonNull(parameterValue);

        return parameterValue;
    }
}