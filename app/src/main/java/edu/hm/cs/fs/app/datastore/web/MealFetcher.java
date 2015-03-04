package edu.hm.cs.fs.app.datastore.web;

import android.annotation.SuppressLint;
import android.content.Context;

import org.jsoup.nodes.Document;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Pattern;

import edu.hm.cs.fs.app.datastore.model.impl.MealImpl;
import edu.hm.cs.fs.app.datastore.web.fetcher.AbstractHtmlFetcher;

/**
 * The meal stores the data for a meal with the name of the meal and the date.
 * 
 * @author Fabio
 * @version 2
 */
public class MealFetcher extends AbstractHtmlFetcher<MealFetcher, MealImpl> {
    private static final String URL = "http://www.studentenwerk-muenchen.de/mensa/speiseplan/speiseplan_432_-de.html";
    private static final Pattern PATTERN_MEAL = Pattern.compile(".*<span style=\"float:left\">(.*)</span>.*");
    @SuppressLint("SimpleDateFormat")
    private static final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    protected MealFetcher(final Context context) {
        super(context, URL);
    }

    @Override
    protected List<MealImpl> readFromDoc(final Document document) {
        return null;
    }
}
