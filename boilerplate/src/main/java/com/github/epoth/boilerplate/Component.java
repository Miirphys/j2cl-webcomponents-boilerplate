package com.github.epoth.boilerplate;

import elemental2.dom.Element;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLTemplateElement;
import elemental2.dom.Node;
import elemental2.dom.ShadowRoot;
import jsinterop.annotations.JsType;

import static elemental2.dom.DomGlobal.document;
import static elemental2.dom.DomGlobal.setTimeout;


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

    private static HTMLTemplateElement __htmlTemplateElement;

    public static final int OPEN = 0;
    public static final int CLOSED = 1;
    public static final int NON_SHADOWED = 2;
    private ShadowRoot root;

    public Component(final int MODE) {

        switch (MODE) {

            case OPEN:

                AttachShadowOptionsType openOptions = AttachShadowOptionsType.create();

                openOptions.setMode("open");

                root = attachShadow(openOptions);

                break;

            case CLOSED:

                AttachShadowOptionsType closedOptions = AttachShadowOptionsType.create();

                closedOptions.setMode("closed");

                root = attachShadow(closedOptions);

        }

        setTimeout(o -> initialize(), 0);

    }

    private void initialize() {

        Node node;

        if (root != null) {

            node = root;

        } else {

            node = this;

        }

        if (__htmlTemplateElement == null) {

            __htmlTemplateElement = TemplateRegistry.get(this.getClass().getSimpleName().toLowerCase());

        }

        if (__htmlTemplateElement != null) {

            node.appendChild(__htmlTemplateElement.content.cloneNode(true));

        } else {

            node.appendChild(render());

        }

        ComponentBinder binder = ComponentBinderRegistry.get(this.getClass().getSimpleName().toLowerCase());

        binder.bind(this);

    }

    public Element render() {

        return document.createElement("div");

    }

    public final Element getElementById(String id) {

        if (root != null) return root.getElementById(id);

        return this.querySelector("#" + id);
    }


}
