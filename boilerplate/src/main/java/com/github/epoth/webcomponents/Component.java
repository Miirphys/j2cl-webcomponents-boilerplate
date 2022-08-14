package com.github.epoth.webcomponents;

import elemental2.dom.Element;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLTemplateElement;
import elemental2.dom.ShadowRoot;
import jsinterop.annotations.JsType;

import static elemental2.dom.DomGlobal.console;
import static elemental2.dom.DomGlobal.document;


/**
 * Copyright 2022 Eric Ponthiaux -/- ponthiaux.eric@gmail.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@JsType
public abstract class Component extends HTMLElement {

    public static final int SHADOWED_OPEN = 0;
    public static final int SHADOWED_CLOSED = 1;
    public static final int NON_SHADOWED = 2;
    private ShadowRoot root;

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

        ComponentBinder binder = ComponentBinderRegistry.get(this.getClass().getSimpleName().toLowerCase());

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

        console.log("beforeBind...");

        binder.bind(this);

        console.log("afterBind...");

    }

    public Element render() {

        return document.createElement("div");

    }

    private HTMLTemplateElement getTemplate() {

        return TemplateRegistry.get(this.getClass().getSimpleName());

    }

    public Element getElementById(String id) {

        if (root != null) return root.getElementById(id);

        return this.querySelector("#" + id);
    }

}
