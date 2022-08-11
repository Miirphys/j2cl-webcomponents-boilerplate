package com.epoth.grid.client.components;

import elemental2.dom.*;
import jsinterop.annotations.JsType;
import static elemental2.dom.DomGlobal.document;

@JsType
public abstract class Component extends HTMLElement {

    private ShadowRoot root;

    private String enclosedHTML;

    private HTMLTemplateElement templateElement;

    public Component() {

        AttachShadowOptionsType options = AttachShadowOptionsType.create();

        options.setMode("open");

        root = attachShadow(options);

    }

    public void connectedCallback() {

        enclosedHTML = this.innerHTML;

        root.append(render());

    }

    public abstract Element render();

    public String getEnclosedHTML() {

        return enclosedHTML;

    }

}
