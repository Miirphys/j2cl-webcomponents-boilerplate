package com.epoth.grid.client.components;

import com.github.epoth.webcomponents.annotations.WebComponent;
import elemental2.dom.Element;
import jsinterop.annotations.JsType;

import static elemental2.dom.DomGlobal.document;

@JsType
@WebComponent(
        tagName = "m-button",
        templateUrl = "Button.html"
)
public class Button extends Component {

    public static final String TAGNAME = "m-button";

    public Element render() {

        Element buttonElement = document.createElement("button");

        return buttonElement;

    }

}