/*
 * This code was written by Bear Giles <bgiles@coyotesong.com> and he
 * licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Any contributions made by others are licensed to this project under
 * one or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 * Copyright (c) 2013 Bear Giles <bgiles@coyotesong.com>
 */
package com.invariantproperties.sandbox.student.webservice.client;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mockito;

import com.invariantproperties.sandbox.student.domain.Instructor;
import com.invariantproperties.sandbox.student.webservice.client.impl.InstructorFinderRestClientImpl;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * Unit tests for InstructorFinderRestClientImpl. Remember that we want to test
 * the behavior, not the implementation.
 * 
 * @author Bear Giles <bgiles@coyotesong.com>
 */
public class InstructorFinderRestClientImplTest {
    private static final String UUID = "uuid";

    @Test
    public void testGetAllInstructorsEmpty() {
        InstructorFinderRestClient client = new FinderInstructorMock(200, new Instructor[0]);
        Instructor[] results = client.getAllInstructors();
        assertEquals(0, results.length);
    }

    @Test
    public void testGetAllInstructorsNonEmpty() {
        Instructor instructor = new Instructor();
        instructor.setUuid(UUID);
        InstructorFinderRestClient client = new FinderInstructorMock(200, new Instructor[] { instructor });
        Instructor[] results = client.getAllInstructors();
        assertEquals(1, results.length);
    }

    @Test(expected = RestClientFailureException.class)
    public void testGetAllInstructorsError() {
        InstructorFinderRestClient client = new FinderInstructorMock(500, null);
        client.getAllInstructors();
    }

    @Test
    public void testGetInstructor() {
        Instructor expected = new Instructor();
        expected.setUuid(UUID);
        InstructorFinderRestClient client = new FinderInstructorMock(200, expected);
        Instructor actual = client.getInstructor(expected.getUuid());
        assertEquals(expected.getUuid(), actual.getUuid());
        // assertEquals(InstructorRestClientMock.RESOURCE +
        // instructor.getUuid(),
        // actual.getSelf());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void testGetInstructorMissing() {
        InstructorFinderRestClient client = new FinderInstructorMock(404, null);
        client.getInstructor(UUID);
    }

    @Test(expected = RestClientFailureException.class)
    public void testGetInstructorError() {
        InstructorFinderRestClient client = new FinderInstructorMock(500, null);
        client.getInstructor(UUID);
    }
}

/**
 * InstructorFinderRestClientImpl extended to mock jersey API. This class
 * requires implementation details.
 */
class FinderInstructorMock extends InstructorFinderRestClientImpl {
    static final String RESOURCE = "test://rest/instructor/";
    private Client client;
    private WebResource webResource;
    private WebResource.Builder webResourceBuilder;
    private ClientResponse response;
    private final int status;
    private final Object results;

    FinderInstructorMock(int status, Object results) {
        super(RESOURCE);
        this.status = status;
        this.results = results;
    }

    /**
     * Override createClient() so it returns mocked object. These expectations
     * will handle basic CRUD operations, more advanced functionality will
     * require inspecting JSON payload of POST call.
     */
    @SuppressWarnings("unchecked")
    @Override
    Client createClient() {
        client = Mockito.mock(Client.class);
        webResource = Mockito.mock(WebResource.class);
        webResourceBuilder = Mockito.mock(WebResource.Builder.class);
        response = Mockito.mock(ClientResponse.class);
        when(client.resource(any(String.class))).thenReturn(webResource);
        when(webResource.accept(any(String.class))).thenReturn(webResourceBuilder);
        when(webResource.type(any(String.class))).thenReturn(webResourceBuilder);
        when(webResourceBuilder.accept(any(String.class))).thenReturn(webResourceBuilder);
        when(webResourceBuilder.type(any(String.class))).thenReturn(webResourceBuilder);
        when(webResourceBuilder.get(eq(ClientResponse.class))).thenReturn(response);
        when(webResourceBuilder.post(eq(ClientResponse.class), any(String.class))).thenReturn(response);
        when(webResourceBuilder.put(eq(ClientResponse.class), any(String.class))).thenReturn(response);
        when(webResourceBuilder.delete(eq(ClientResponse.class))).thenReturn(response);
        when(response.getStatus()).thenReturn(status);
        when(response.getEntity(any(Class.class))).thenReturn(results);
        return client;
    }
}
