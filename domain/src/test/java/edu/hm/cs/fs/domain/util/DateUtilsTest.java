package edu.hm.cs.fs.domain.util;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class DateUtilsTest {
    @Test
    public void testIsTodayTrue() throws Exception {
        Assert.assertThat(true, CoreMatchers.is(DateUtils.isToday(new Date())));
    }

    @Test
    public void testIsTodayWrong() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -15);
        Assert.assertThat(false, CoreMatchers.is(DateUtils.isToday(calendar.getTime())));
    }
}