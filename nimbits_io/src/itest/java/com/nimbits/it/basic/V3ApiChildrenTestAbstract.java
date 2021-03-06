package com.nimbits.it.basic;

import com.nimbits.client.model.point.Point;
import com.nimbits.client.model.point.PointModel;
import com.nimbits.client.model.user.User;
import com.nimbits.it.AbstractNimbitsTest;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

/**
 * Create a points with children
 */
public class V3ApiChildrenTestAbstract extends AbstractNimbitsTest {

    @Before
    public void setUp() throws Exception {
        super.setUp();


    }

    @Test
    public void executeTest() throws InterruptedException {


        User me = nimbits.getMe(true);

        log(me.toString());


        for (int i = 0; i < 10; i++) {
            Point point = new PointModel.Builder().name("child_" + UUID.randomUUID()).create();
            nimbits.addPoint(me, point);

        }

        me = nimbits.getMe(true);
        log("Got Children: " + me.getChildren().size());
        log("Done " + getClass().getName());

    }


}
