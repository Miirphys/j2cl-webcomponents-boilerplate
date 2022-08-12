package com.github.epoth.webcomponents;

import elemental2.dom.Element;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLTemplateElement;
import elemental2.dom.ShadowRoot;
import jsinterop.annotations.JsType;
import static elemental2.dom.DomGlobal.console;

@JsType
public abstract class Component extends HTMLElement {

    private ShadowRoot root;

    public Component() {

        AttachShadowOptionsType options = AttachShadowOptionsType.create();

        options.setMode("open");

        root = attachShadow(options);

    }

    public void connectedCallback() {

        root.append(render());

    }

    public abstract Element render();

    public HTMLTemplateElement getTemplate() {

        return TemplateRegistry.get(this.getClass().getSimpleName());

    }

}
