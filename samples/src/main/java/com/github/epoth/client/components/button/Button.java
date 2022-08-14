package com.github.epoth.client.components.button;

import com.github.epoth.boilerplate.Component;
import com.github.epoth.boilerplate.annotations.WebComponent;
import elemental2.dom.Event;
import elemental2.dom.HTMLElement;
import jsinterop.annotations.JsType;


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
        tagName = "m-button",
        templateUrl = "Button.html"
)
public class Button extends Component {

    public HTMLElement label;

    public Button() {

        super(Component.OPEN);

    }

    @Override
    public void onConnect() {

    }

    public void onClick(Event ev) {

        // in the case of a component without shadow , the parent tag is the parentElement

        if (this.parentElement.getAttribute("onclick") != null) {

            this.parentElement.onclick.onInvoke(ev);

        }

    }

}