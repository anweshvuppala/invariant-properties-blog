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
package com.invariantproperties.sandbox.student.webservice.server.rest;

import org.apache.log4j.Logger;

import com.invariantproperties.sandbox.student.domain.Classroom;
import com.invariantproperties.sandbox.student.domain.Course;
import com.invariantproperties.sandbox.student.domain.Student;

/**
 * @author Bear Giles <bgiles@coyotesong.com>
 */
public abstract class AbstractResource {
    private static final Logger log = Logger.getLogger(AbstractResource.class);

    public Classroom scrubClassroom(final Classroom dirty) {
        final Classroom clean = new Classroom();
        clean.setUuid(dirty.getUuid());
        clean.setName(dirty.getName());
        // clean.setSelf("resource/" + dirty.getUuid());
        return clean;
    }

    public Course scrubCourse(final Course dirty) {
        final Course clean = new Course();
        clean.setUuid(dirty.getUuid());
        clean.setName(dirty.getName());
        // clean.setSelf("resource/" + dirty.getUuid());
        return clean;
    }

    public Student scrubStudent(final Student dirty) {
        log.info("scrubbing student");
        final Student clean = new Student();
        clean.setUuid(dirty.getUuid());
        clean.setName(dirty.getName());
        clean.setEmailAddress(dirty.getEmailAddress());
        // clean.setSelf("resource/" + dirty.getUuid());
        return clean;
    }
}
