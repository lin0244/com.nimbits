package com.nimbits.it;

import com.nimbits.client.model.point.Point;
import com.nimbits.client.model.point.PointModel;
import com.nimbits.client.model.user.User;
import com.nimbits.client.model.value.Value;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CacheTest  extends NimbitsTest {

        @Before
        public void setUp() throws Exception {
            super.setUp();


        }

        @Test
        public void executeTest() throws InterruptedException {

            String name = UUID.randomUUID().toString();

            User me = nimbits.getMe(true);

            System.out.println(me.toString());

            nimbits.addPoint(me, new PointModel.Builder().name(name).create());

            assertTrue(nimbits.findPointByName(name).isPresent());

            nimbits.recordValue(name, new Value.Builder().doubleValue(42.0).create());

            Thread.sleep(100);

            double r = nimbits.getSnapshot(name).getDoubleValue();

            assertEquals(42.0, r, 0.001);

            for (int i = 0; i < 3; i++) {
                System.out.println(nimbits.getSnapshot(name));
            }



        }
}
