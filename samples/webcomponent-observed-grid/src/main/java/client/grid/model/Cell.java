package client.grid.model;

import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

@JsType(namespace="model")
public class Cell {

    public Cell(String value) {
        this.value = value;
    }

    @JsProperty(name="value")
    public String value;

}
