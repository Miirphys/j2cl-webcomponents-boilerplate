package client.grid.model;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
@JsType(namespace="model")
public class Column {

    public Column(String name) {
        this.name = name;
    }

    @JsProperty(name="name")
    public String name;

}
