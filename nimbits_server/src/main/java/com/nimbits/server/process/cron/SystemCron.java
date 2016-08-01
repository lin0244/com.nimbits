/*
 * Copyright 2016 Benjamin Sautner
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.nimbits.server.process.cron;


import com.google.common.base.Optional;
import com.nimbits.client.enums.EntityType;
import com.nimbits.client.exception.ValueException;
import com.nimbits.client.model.entity.Entity;
import com.nimbits.client.model.point.Point;
import com.nimbits.client.model.schedule.Schedule;
import com.nimbits.client.model.user.User;
import com.nimbits.client.model.value.Value;
import com.nimbits.server.data.DataProcessor;

import com.nimbits.server.process.task.TaskService;
import com.nimbits.server.process.task.ValueTask;
import com.nimbits.server.transaction.BaseProcessor;
import com.nimbits.server.transaction.calculation.CalculationService;
import com.nimbits.server.transaction.entity.dao.EntityDao;
import com.nimbits.server.transaction.entity.EntityService;
import com.nimbits.server.transaction.subscription.SubscriptionService;
import com.nimbits.server.transaction.summary.SummaryService;
import com.nimbits.server.transaction.sync.SyncService;
import com.nimbits.server.transaction.user.service.UserService;
import com.nimbits.server.transaction.value.ValueDao;
import com.nimbits.server.transaction.value.service.ValueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;


/**
 * Sets a point as idle based on the individual point's idle alert settings. Idle alerts trigger when
 * a data point has not received a new value over a set amount of time. Idle alerts are processed and sent out and the
 * point is flagged so no further alerts will be sent until the point receives another value. If you only care if a point
 * is idle for more than 24 hours, it wouldn't make sense to run this every minute.
 */
@Service
public class SystemCron extends HttpServlet  {

    private static final Logger logger = LoggerFactory.getLogger(SystemCron.class.getName());

    @Autowired
    private EntityService entityService;

    @Autowired
    private UserService userService;

    @Autowired
    private ValueService valueService;

    @Autowired
    private EntityDao entityDao;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private TaskService taskService;

    @Autowired
    CalculationService calculationService;

    @Autowired
    ValueTask valueTask;

    @Autowired
    ValueDao valueDao;

    @Autowired
    SyncService syncService;

    @Autowired
    SummaryService summaryService;

    @Autowired
    DataProcessor dataProcessor;

    @Override
    public void init() throws ServletException {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);


    }


    @Override
    public void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws IOException {


        try {
            process();
        } catch (ValueException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        resp.setStatus(HttpServletResponse.SC_OK);





    }

    public void process() throws  ValueException {

        final List<Entity> points = entityDao.getIdleEntities(userService.getAdmin());

        User admin = userService.getAdmin();
        //TODO OOM - delete expired data here



        for (final Entity p : points) {
            Value v = valueService.getCurrentValue(p);

            valueService.process(admin, (Point) p, v);


        }

         processSchedules();


    }


    private long processSchedules() throws ValueException {

        List<Schedule> schedules = entityDao.getSchedules();

        long counter = 0;
        for (Schedule schedule : schedules) { //TODO start task for GAE

            if (schedule.getLastProcessed() + schedule.getInterval() < new Date().getTime()) {

                User owner = userService.getUserByKey(schedule.getOwner()).get();

                schedule.setLastProcessed(new Date().getTime());

                entityDao.addUpdateEntity(owner,schedule);
                Optional<Entity> sourcePoint = entityDao.getEntity(owner, schedule.getSource(), EntityType.point);
                Optional<Entity> targetPoint = entityDao.getEntity(owner, schedule.getTarget(), EntityType.point);

                if (sourcePoint.isPresent() && targetPoint.isPresent()) {
                    Value value = valueService.getCurrentValue(sourcePoint.get());
                    Value newValue = new Value.Builder().initValue(value).timestamp(new Date()).create();// ValueFactory.createValue(value, new Date());
                    counter++;
                    logger.info("DP:: " + this.getClass().getName() + " " + (dataProcessor == null));
                    taskService.process(owner, (Point) targetPoint.get(), newValue);
                }
                else {
                    schedule.setEnabled(false);
                    entityService.addUpdateEntity(valueService, owner, schedule);
                }



            }


        }
        return counter;
    }


}
