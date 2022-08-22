package client.grid;

import com.github.epoth.boilerplate.Component;
import com.github.epoth.boilerplate.annotations.Observed;
import com.github.epoth.boilerplate.annotations.WebComponent;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLTableCellElement;
import elemental2.dom.HTMLTableElement;
import jsinterop.annotations.JsType;
import client.grid.model.Column;
import client.grid.model.Row;

import java.util.List;

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
        mode = WebComponent.OPEN,
        tagName = "m-grid",
        template = "Grid.html"
)
public class Grid extends Component {

    public HTMLTableElement table;
    public HTMLElement header;
    public HTMLElement body;

    public Grid() {

        super();

    }

    @Observed(attribute = "fetch")
    public void fetchData(String url) {

    }

    void renderHeader(List<Column> columns) {

        for (int i = 0; i < columns.size(); i++) {

            Column column = columns.get(i);

            HTMLElement cellElement = (HTMLTableCellElement) document.createElement("TD");

            cellElement.className = "mdc-data-table__header-cell";

            cellElement.setAttribute("role", "col");

            cellElement.innerHTML = column.name;

            header.appendChild(cellElement);

        }

    }

    void renderRows(List<Row> rows) {


    }

}