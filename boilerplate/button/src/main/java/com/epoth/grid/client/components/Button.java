package com.epoth.grid.client.components;

import com.github.epoth.webcomponents.Component;
import com.github.epoth.webcomponents.annotations.WebComponent;
import elemental2.dom.Event;
import jsinterop.annotations.JsType;

import static elemental2.dom.DomGlobal.console;

@JsType
@WebComponent(
        tagName = "m-button",
        templateUrl = "Button.html"
)
public class Button extends Component {

    public Button() {

        super(Component.NON_SHADOWED);

    }

    public void onClick(Event ev) {

        console.log("click");

    }

}