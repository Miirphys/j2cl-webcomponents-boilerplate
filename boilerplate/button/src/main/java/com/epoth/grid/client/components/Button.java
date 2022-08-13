package com.epoth.grid.client.components;

import com.github.epoth.webcomponents.Component;
import com.github.epoth.webcomponents.annotations.WebComponent;
import elemental2.dom.Event;
import jsinterop.annotations.JsType;

import static elemental2.dom.DomGlobal.console;


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

    public Button() {

        super(Component.NON_SHADOWED);

    }

    public void onClick(Event ev) {

        console.log("click");

    }

}