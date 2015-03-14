package edu.hm.cs.fs.app.datastore.web;

import android.annotation.SuppressLint;
import android.content.Context;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import edu.hm.cs.fs.app.datastore.model.constants.MealType;
import edu.hm.cs.fs.app.datastore.model.impl.MealImpl;
import edu.hm.cs.fs.app.datastore.web.fetcher.AbstractHtmlFetcher;

/**
 * The meal stores the data for a meal with the name of the meal and the date.
 *
 * @author Fabio
 * @version 2
 */
public class MealFetcher extends AbstractHtmlFetcher<MealFetcher, MealImpl> {
    private static final String URL = "http://www.studentenwerk-muenchen.de/mensa/speiseplan/speiseplan_431_-de.html";
    private static final Pattern PATTERN_MEAL = Pattern.compile(".*<span style=\"float:left\">(.*)</span>.*");
    @SuppressLint("SimpleDateFormat")
    private static final DateFormat sdf = new SimpleDateFormat("dddd, dd.MM.yyyy");

    public MealFetcher(final Context context) {
        super(context, URL);
    }

    @Override
    protected List<MealImpl> readFromDoc(final Document document) {
        /*
        <table class="menu">
            <tr>
                <td class="headline"></td>
                <td class="headline">
                    <span style="float:left">
                        <a class="heute_2015-03-16 anker" name="heute"><strong>Montag, 16.03.2015</strong></a>
                        (<a href="mensa/speiseplan/speiseplan_2015-03-16_431_-de.html">Beilagen</a>)
                        <!--
                                <a href="mensa/speiseplan/speiseplan_2015-03-16_431_-de.pdf">PDF</a>
                        -->
                    </span>
                    <span style="float:right">
                        (Wochenplan als <a href="mensa/speiseplan/speiseplan-kw_2015-12_431_-de.pdf">PDF</a>)
                    </span>
                </td>
            </tr>

            <tr>
                <td class="gericht"><span class="stwm-artname">Tagesgericht 1</span></td>
                <td class="beschreibung" style="padding-right:30px">
                    <span style="float:left">Farfalle mit Champignonrahmsauce (f)</span>
                    <span title="fleischloses Gericht" class="fleischlos"><span>&nbsp;</span>fleischlos</span>


                </td>
            </tr>
            ...
         */

        List<MealImpl> result = new ArrayList<>();

        final Elements menuList = document.getElementsByClass("menu");
        for (int index = 0; index < menuList.size(); index++) {
            final Element menu = menuList.get(index);

            final String dateStr = menu.getElementsByTag("strong").get(0).text();

            final Elements descriptionList = menu.getElementsByClass("beschreibung");
            for (int indexDesc = 0; indexDesc < descriptionList.size(); indexDesc++) {
                MealImpl meal = new MealImpl();
                try {
                    meal.setDate(sdf.parse(dateStr));
                } catch (ParseException e) {
                    meal.setDate(new Date());
                }

                final Element description = descriptionList.get(indexDesc);

                meal.setDescription(description.getElementsByAttribute("style").get(0).text());

                if(!description.getElementsByClass("fleischlos").isEmpty()) {
                    meal.setType(MealType.MEATLESS.toString());
                } else if(!description.getElementsByClass("fleisch").isEmpty()) {
                    meal.setType(MealType.MEAT.toString());
                } else if(!description.getElementsByClass("vegan").isEmpty()) {
                    meal.setType(MealType.VEGAN.toString());
                }

                result.add(meal);
            }
        }

        return result;
    }
}