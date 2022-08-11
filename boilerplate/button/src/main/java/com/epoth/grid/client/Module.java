package com.epoth.grid.client;

import com.epoth.grid.client.components.Button;
import elemental2.dom.DomGlobal;
public class Module {

	public void onLoad() {

		DomGlobal.customElements.define(Button.TAGNAME, Button.class);

	}

}
