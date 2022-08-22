package client.grid.model;

import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

import java.util.List;

@JsType(namespace="model")
public class Row {

    @JsProperty(name="cells")
    public List<Cell> cells;

}
