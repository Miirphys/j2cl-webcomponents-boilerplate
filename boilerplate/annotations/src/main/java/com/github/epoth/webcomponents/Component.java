package com.github.epoth.webcomponents;

import elemental2.dom.Element;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLTemplateElement;
import elemental2.dom.ShadowRoot;
import jsinterop.annotations.JsType;

import static elemental2.dom.DomGlobal.document;

@JsType
public abstract class Component extends HTMLElement {

    private ShadowRoot root;
    public static final int SHADOWED_OPEN = 0;
    public static final int SHADOWED_CLOSED = 1;
    public static final int NON_SHADOWED = 2;

    public Component(final int MODE) {

        switch (MODE) {

            case SHADOWED_OPEN:

                AttachShadowOptionsType openOptions = AttachShadowOptionsType.create();

                openOptions.setMode("open");

                root = attachShadow(openOptions);

                break;

            case SHADOWED_CLOSED:

                AttachShadowOptionsType closedOptions = AttachShadowOptionsType.create();

                closedOptions.setMode("close");

                root = attachShadow(closedOptions);

        }

    }

    public void connectedCallback() {

        HTMLTemplateElement templateElement = TemplateRegistry.get(this.getClass().getSimpleName().toLowerCase());

        if (templateElement != null) {

            if (root != null) {

                root.append(templateElement.content.cloneNode(true));

            } else {

                this.append(templateElement.content.cloneNode(true));

            }

        } else {

            if (root != null) {

                root.append(render());

            } else {

                this.append(render());

            }

        }

    }

    public Element render() {

        return document.createElement("div");

    }

    private HTMLTemplateElement getTemplate() {

        return TemplateRegistry.get(this.getClass().getSimpleName());

    }

}
