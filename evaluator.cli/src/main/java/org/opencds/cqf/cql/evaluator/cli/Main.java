package org.opencds.cqf.cql.cli;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.Map.Entry;

import org.cqframework.cql.elm.execution.VersionedIdentifier;
import org.opencds.cqf.cql.execution.LibraryResult;
import org.opencds.cqf.cql.service.Parameters;
import org.opencds.cqf.cql.service.Response;
import org.opencds.cqf.cql.service.Service;
import org.opencds.cqf.cql.service.serialization.DefaultEvaluationResultsSerializer;
import org.opencds.cqf.cql.service.serialization.EvaluationResultsSerializer;

public class Main {

    public static void main(String[] args) {

        // TODO: Update cql engine dependencies
        disableAccessWarnings();

        Parameters params = null;
        try {
            params = new ArgumentProcessor().parseAndConvert(args);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        try {
            Service service = new Service(EnumSet.of(Service.Options.EnableFileUri));
            Response response  = service.evaluate(params);
            EvaluationResultsSerializer serializer;

            serializer = new DefaultEvaluationResultsSerializer();


            for (Entry<VersionedIdentifier, LibraryResult> libraryEntry : response.evaluationResult.libraryResults.entrySet()) {
                serializer.printResults(params.verbose, libraryEntry);
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    @SuppressWarnings("unchecked")
    public static void disableAccessWarnings() {
        try {
            Class unsafeClass = Class.forName("sun.misc.Unsafe");
            Field field = unsafeClass.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            Object unsafe = field.get(null);

            Method putObjectVolatile = unsafeClass.getDeclaredMethod("putObjectVolatile", Object.class, long.class, Object.class);
            Method staticFieldOffset = unsafeClass.getDeclaredMethod("staticFieldOffset", Field.class);

            Class loggerClass = Class.forName("jdk.internal.module.IllegalAccessLogger");
            Field loggerField = loggerClass.getDeclaredField("logger");
            Long offset = (Long) staticFieldOffset.invoke(unsafe, loggerField);
            putObjectVolatile.invoke(unsafe, loggerClass, offset, null);
        } catch (Exception ignored) {
        }
    }

}