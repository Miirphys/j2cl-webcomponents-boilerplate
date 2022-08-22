package com.github.epoth.client.components.grid;

import com.github.epoth.boilerplate.Component;
import com.github.epoth.boilerplate.annotations.WebComponent;
import elemental2.dom.Element;
import elemental2.dom.Event;
import elemental2.dom.HTMLButtonElement;
import elemental2.dom.HTMLElement;
import jsinterop.annotations.JsType;

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
@WebComponent(
        mode = WebComponent.FREE,
        tagName = "m-button"
)
public class Button extends Component {

    public Button() {

        super();

    }

    public void onClick(Event ev) {

        if (this.parentElement.getAttribute("onclick") != null) {

            this.parentElement.onclick.onInvoke(ev);

        }

    }

    @Override
    public Element render() {

        HTMLElement touchTarget = (HTMLElement) document.createElement("div");
        touchTarget.className = "mdc-touch-target-wrapper";

        HTMLButtonElement mdcButton = (HTMLButtonElement) document.createElement("button");
        mdcButton.className = "mdc-button mdc-button--touch mdc-button--raised";
        mdcButton.addEventListener("click", this::onClick);

        HTMLElement buttonLabel = (HTMLElement) document.createElement("span");
        buttonLabel.innerHTML = "Click Me";

        touchTarget.appendChild(mdcButton);
        mdcButton.appendChild(buttonLabel);

        return touchTarget;

    }
}