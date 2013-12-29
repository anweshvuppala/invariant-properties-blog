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
package com.invariantproperties.sandbox.student.business;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.invariantproperties.sandbox.student.domain.Section;
import com.invariantproperties.sandbox.student.domain.TestRun;
import com.invariantproperties.sandbox.student.repository.SectionRepository;

/**
 * Implementation of SectionService
 * 
 * @author Bear Giles <bgiles@coyotesong.com>
 */
@Service
public class SectionManagerServiceImpl implements SectionManagerService {
    private static final Logger log = LoggerFactory.getLogger(SectionManagerServiceImpl.class);

    @Resource
    private SectionRepository sectionRepository;

    /**
     * Default constructor
     */
    public SectionManagerServiceImpl() {

    }

    /**
     * Constructor used in unit tests
     */
    SectionManagerServiceImpl(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    /**
     * @see com.invariantproperties.sandbox.student.business.SectionFinderService#
     *      createSection(java.lang.String)
     */
    @Transactional
    @Override
    public Section createSection(String name) {
        final Section section = new Section();
        section.setName(name);

        Section actual = null;
        try {
            actual = sectionRepository.saveAndFlush(section);
        } catch (DataAccessException e) {
            if (!(e instanceof UnitTestException)) {
                log.info("internal error retrieving section: " + name, e);
            }
            throw new PersistenceException("unable to create section", e);
        }

        return actual;
    }

    /**
     * @see com.invariantproperties.sandbox.student.business.SectionFinderService#
     *      createSectionForTesting(java.lang.String,
     *      com.invariantproperties.sandbox.student.common.TestRun)
     */
    @Transactional
    @Override
    public Section createSectionForTesting(String name, TestRun testRun) {
        final Section section = new Section();
        section.setName(name);
        section.setTestRun(testRun);

        Section actual = null;
        try {
            actual = sectionRepository.saveAndFlush(section);
        } catch (DataAccessException e) {
            if (!(e instanceof UnitTestException)) {
                log.info("internal error retrieving section: " + name, e);
            }
            throw new PersistenceException("unable to create section", e);
        }

        return actual;
    }

    /**
     * @see com.invariantproperties.sandbox.SectionFinderService.persistence.SectionService#
     *      updateSection(com.invariantproperties.sandbox.section.domain.Section,
     *      java.lang.String)
     */
    @Transactional
    @Override
    public Section updateSection(Section section, String name) {
        Section updated = null;
        try {
            final Section actual = sectionRepository.findSectionByUuid(section.getUuid());

            if (actual == null) {
                log.debug("did not find section: " + section.getUuid());
                throw new ObjectNotFoundException(section.getUuid());
            }

            actual.setName(name);
            updated = sectionRepository.saveAndFlush(actual);
            section.setName(name);

        } catch (DataAccessException e) {
            if (!(e instanceof UnitTestException)) {
                log.info("internal error deleting section: " + section.getUuid(), e);
            }
            throw new PersistenceException("unable to delete section", e, section.getUuid());
        }

        return updated;
    }

    /**
     * @see com.invariantproperties.sandbox.student.business.SectionFinderService#
     *      deleteSection(java.lang.String, java.lang.Integer)
     */
    @Transactional
    @Override
    public void deleteSection(String uuid, Integer version) {
        Section section = null;
        try {
            section = sectionRepository.findSectionByUuid(uuid);

            if (section == null) {
                log.debug("did not find section: " + uuid);
                throw new ObjectNotFoundException(uuid);
            }
            sectionRepository.delete(section);

        } catch (DataAccessException e) {
            if (!(e instanceof UnitTestException)) {
                log.info("internal error deleting section: " + uuid, e);
            }
            throw new PersistenceException("unable to delete section", e, uuid);
        }
    }
}