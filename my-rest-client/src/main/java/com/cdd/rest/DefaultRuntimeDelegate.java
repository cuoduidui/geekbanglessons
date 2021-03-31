package com.cdd.rest;

import com.cdd.rest.client.DefaultVariantListBuilder;
import com.cdd.rest.core.DefaultResponseBuilder;
import com.cdd.rest.core.DefaultUriBuilder;

import javax.ws.rs.core.*;
import javax.ws.rs.ext.RuntimeDelegate;

public class DefaultRuntimeDelegate extends RuntimeDelegate {

    @Override
    public UriBuilder createUriBuilder() {
        return new DefaultUriBuilder();
    }

    @Override
    public Response.ResponseBuilder createResponseBuilder() {
        return new DefaultResponseBuilder();
    }

    @Override
    public Variant.VariantListBuilder createVariantListBuilder() {
        return new DefaultVariantListBuilder();
    }

    @Override
    public <T> T createEndpoint(Application application, Class<T> endpointType) throws IllegalArgumentException, UnsupportedOperationException {
        return null;
    }

    @Override
    public <T> HeaderDelegate<T> createHeaderDelegate(Class<T> type) throws IllegalArgumentException {
        return new DefaultHeaderDelegate();
    }

    @Override
    public Link.Builder createLinkBuilder() {
        return null;
    }

    class DefaultHeaderDelegate implements HeaderDelegate {

        @Override
        public Object fromString(String s) {
            return (Object) s;
        }

        @Override
        public String toString(Object o) {
            return o.toString();
        }
    }
}
