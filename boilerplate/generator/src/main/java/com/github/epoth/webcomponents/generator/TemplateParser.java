package com.github.epoth.webcomponents.generator;

import com.github.epoth.webcomponents.TemplateBinding;
import com.google.common.annotations.GwtIncompatible;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@GwtIncompatible
public class TemplateParser {

    private int identifierCount = 0;

    private static String[] eventsAttributes;

    private static final String ID = "id";

    static {

        eventsAttributes = new String[]{"@afterprint", "@beforeprint", "@beforeunload", "@error", "@hashchange", "@load", "@message", "@offline", "@online", "@pagehide", "@pageshow", "@popstate", "@resize", "@storage", "@unload", "@blur", "@change", "@contextmenu", "@focus", "@input", "@invalid", "@reset", "@search", "@select", "@submit", "@keydown", "@keypress", "@keyup", "@click", "@dblclick", "@mousedown", "@mousemove", "@mouseout", "@mouseover", "@mouseup", "@mousewheel", "@wheel", "@drag", "@dragend", "@dragenter", "@dragleave", "@dragover", "@dragstart", "@drop", "@scroll", "@copy", "@cut", "@paste", "@abort", "@canplay", "@canplaythrough", "@cuechange", "@durationchange", "@emptied", "@ended", "@error", "@loadeddata", "@loadedmetadata", "@loadstart", "@pause", "@play", "@playing", "@progress", "@ratechange", "@seeked", "@seeking", "@stalled", "@suspend", "@timeupdate", "@volumechange", "@waiting", "@toggle"};

    }

    public static class TemplateParserResult {

        List<TemplateBinding> bindingList = new ArrayList<>();

        String parserOutput;

    }

    public TemplateParserResult parse(

            ProcessingEnvironment processingEnvironment,

            String templateContents

    ) {

        TemplateParserResult result = new TemplateParserResult();

        Document document = Jsoup.parse(templateContents);

        for (String attributeName : eventsAttributes) {

            Elements elements = document.select("[" + attributeName + "]");

            if (elements.size() != 0) {

                for (Element element : elements) {

                    TemplateBinding templateBinding = new TemplateBinding(TemplateBinding.FUNCTION);

                    /* */

                    String id = null;

                    if (element.attr(ID) != null && !element.attr(ID).trim().equals("")) {

                        id = element.attr(ID);

                    } else {

                        //If using HTML4 ids must start with a letter

                        id = Base64
                                .getEncoder()
                                .encodeToString(("" + (identifierCount++))
                                        .getBytes(StandardCharsets.UTF_8))
                                .replaceAll("=", "");

                        element.attr(ID, id);

                    }

                    /* */

                    templateBinding.setId(id);
                    templateBinding.setEvent(attributeName.substring(1));
                    templateBinding.setFunction(element.attr(attributeName));

                    result.bindingList.add(templateBinding);

                    processingEnvironment.getMessager().printMessage(

                            Diagnostic.Kind.NOTE,

                            "(" + templateBinding.getId() + ") --> " + templateBinding.getFunction());

                    /* */

                    element.removeAttr(attributeName);

                }

            }

        }

        result.parserOutput = document.body().html();

        return result;

    }

}


