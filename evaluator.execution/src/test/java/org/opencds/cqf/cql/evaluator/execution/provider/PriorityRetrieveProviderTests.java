
package org.opencds.cqf.cql.evaluator.execution.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collections;

import com.google.common.collect.Lists;

import org.opencds.cqf.cql.engine.retrieve.RetrieveProvider;
import org.opencds.cqf.cql.engine.runtime.Code;
import org.opencds.cqf.cql.engine.runtime.Interval;
import org.testng.annotations.Test;


public class PriorityRetrieveProviderTests {

    @Test(expectedExceptions = NullPointerException.class)
    public void test_nullConstructorParameterThrowsException() {
        new PriorityRetrieveProvider(null);
    }

    @Test
    public void test_noProviders_returnsEmptySet() {
        var retrieve = new PriorityRetrieveProvider(Collections.emptyList());
        var result = retrieve.retrieve(null, null, null, null, null, null, null, null, null, null, null, null);
        assertNotNull(result);
        var resultList = Lists.newArrayList(result);
        assertEquals(0, resultList.size());
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void test_badProvider_throwsException() {

        var badProvider = new RetrieveProvider(){
            @Override
            public Iterable<Object> retrieve(String context, String contextPath, Object contextValue, String dataType,
                    String templateId, String codePath, Iterable<Code> codes, String valueSet, String datePath,
                    String dateLowPath, String dateHighPath, Interval dateRange) {

                // This is an invalid results. Providers should return an empty set.
                return null;
            }
        };
        var retrieve = new PriorityRetrieveProvider(Collections.singletonList(badProvider));
        retrieve.retrieve(null, null, null, null, null, null, null, null, null, null, null, null);
    }

    @Test
    public void test_retrieve_returnsFirstNonEmpty() {
        var providerOne = new RetrieveProvider(){
            @Override
            public Iterable<Object> retrieve(String context, String contextPath, Object contextValue, String dataType,
                    String templateId, String codePath, Iterable<Code> codes, String valueSet, String datePath,
                    String dateLowPath, String dateHighPath, Interval dateRange) {
                return Collections.emptySet();
            }
        };

        var providerTwo = new RetrieveProvider(){
            @Override
            public Iterable<Object> retrieve(String context, String contextPath, Object contextValue, String dataType,
                    String templateId, String codePath, Iterable<Code> codes, String valueSet, String datePath,
                    String dateLowPath, String dateHighPath, Interval dateRange) {
                return Lists.newArrayList(1, 2, 3);
            }
        };

        var providerThree = new RetrieveProvider(){
            @Override
            public Iterable<Object> retrieve(String context, String contextPath, Object contextValue, String dataType,
                    String templateId, String codePath, Iterable<Code> codes, String valueSet, String datePath,
                    String dateLowPath, String dateHighPath, Interval dateRange) {
                return Lists.newArrayList(5, 4, 3, 2, 1);
            }
        };

        var retrieve = new PriorityRetrieveProvider(Lists.newArrayList(providerOne, providerTwo, providerThree));
        var results = retrieve.retrieve(null, null, null, null, null, null, null, null, null, null, null, null);
        assertNotNull(results);
        var resultList = Lists.newArrayList(results);
        assertEquals(3, resultList.size());
    }
}