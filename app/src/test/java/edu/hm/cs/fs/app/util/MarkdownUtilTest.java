package edu.hm.cs.fs.app.util;

import android.text.Html;
import android.text.Spanned;

import com.fk07.BuildConfig;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

/**
 * Created by FHellman on 13.08.2015.
 */
@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class MarkdownUtilTest {
    private static final String BOLD_INPUT = "this is a *test*.";
    private static final Spanned BOLD_OUTPUT = Html.fromHtml("this is a <b>test</b>.");
    private static final String NEW_LINE_INPUT = "this #is #a #test.";
    private static final Spanned NEW_LINE_OUTPUT = Html.fromHtml("this <br>is <br>a <br>test.");
    private static final String ALL_INPUT = "*Bachelor's Thesis* #Whitin the scope of this thesis, " +
            "the student shall implement and integrate Geometry Instancing to the VIRES " +
            "ImageGenerator, which is a rendering engine based on OpenSceneGraph. This inlcudes " +
            "the preperation of the user generated virtual world for the visualization of trees, " +
            "grass, buildings and any other objects in real time. It is also possible to finish " +
            "this work during an internship. #*Master's Thesis* #In addition to the above " +
            "mentioned implementation, the student shall extend the approach by variations. This " +
            "includes modifcations in size, color or even materials and textures. A method for " +
            "the procedural generation of the world can be discussed as well. It is also " +
            "possible to finish this work as a student trainee within one year. #*Your Profile* " +
            ".Student of Computer Science or comparable engineering programs .Strong knowledge " +
            "in C++ and OpenGL .Experience in OpenSceneGraph is a benefit . #*Company* #VIRES - " +
            "a products and services company – has its main fields of operation in the " +
            "automotive and railroad industries. In the automotive market, VIRES provides " +
            "simulation solutions for the development, validation and presentation of driver " +
            "assistance and active safety systems. Additionally, VIRES is a main contributor and " +
            "partner in the following standardization projects in the automotive industry: " +
            "OpenDRIVE, OpenCRG and OpenSCENARIO. If you are interested in this work, please " +
            "send us a short application and we can discuss further steps. #*Contact* #VIRES " +
            "Simulationstechnologie GmbH #Grassinger Straße 8 #83043 Bad Aibling #Phone: " +
            "+49 8061 / 939093-21 #e-mail: daniel.wiesenhuetter@vires.com";
    private static final Spanned ALL_OUTPUT = Html.fromHtml("<b>Bachelor's Thesis</b> <br>" +
            "Whitin the scope of this thesis, the student shall implement and integrate Geometry " +
            "Instancing to the VIRES ImageGenerator, which is a rendering engine based on " +
            "OpenSceneGraph. This inlcudes the preperation of the user generated virtual world " +
            "for the visualization of trees, grass, buildings and any other objects in real " +
            "time. It is also possible to finish this work during an internship. <br><b>Master's " +
            "Thesis</b> <br>In addition to the above mentioned implementation, the student shall " +
            "extend the approach by variations. This includes modifcations in size, color or " +
            "even materials and textures. A method for the procedural generation of the world " +
            "can be discussed as well. It is also possible to finish this work as a student " +
            "trainee within one year. <br>*Your Profile* <ul><li>Student of Computer Science or " +
            "comparable engineering programs</li> <li>Strong knowledge in C++ and OpenGL</li> " +
            "<li>Experience in OpenSceneGraph is a benefit</li> </ul> <br>*Company* <br>VIRES - " +
            "a products and services company – has its main fields of operation in the automotive " +
            "and railroad industries. In the automotive market, VIRES provides simulation " +
            "solutions for the development, validation and presentation of driver assistance and " +
            "active safety systems. Additionally, VIRES is a main contributor and partner in the " +
            "following standardization projects in the automotive industry: OpenDRIVE, OpenCRG " +
            "and OpenSCENARIO. If you are interested in this work, please send us a short " +
            "application and we can discuss further steps. <br><b>Contact</b> <br>VIRES " +
            "Simulationstechnologie GmbH <br>Grassinger Straße 8 <br>83043 Bad Aibling " +
            "<br>Phone: +49 8061 / 939093-21 <br>e-mail: daniel.wiesenhuetter@vires.com");

    @Test
    public void testMarkdownBold() {
        final Spanned result = MarkdownUtil.toHtml(BOLD_INPUT);
        Assert.assertThat(BOLD_OUTPUT, CoreMatchers.equalTo(result));
    }

    @Test
    public void testMarkdownNewLine() {
        final Spanned result = MarkdownUtil.toHtml(NEW_LINE_INPUT);
        Assert.assertThat(NEW_LINE_OUTPUT, CoreMatchers.equalTo(result));
    }

    // TODO List conversion is not yet implemented
    /*
    @Test
    public void testMarkdownAll() {
        final Spanned result = MarkdownUtil.toHtml(ALL_INPUT);
        Assert.assertThat(ALL_OUTPUT, CoreMatchers.equalTo(result));
    }
    */
}
