package client.grid.model;

import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

import java.util.List;

@JsType(name="DataSet",namespace="model")
public class DataSet {

    @JsProperty(name="title")
    public String title;

    @JsProperty(name="columns")
    public List<Column> columns;

    @JsProperty(name="rows")
    public List<Row> rows;

}
