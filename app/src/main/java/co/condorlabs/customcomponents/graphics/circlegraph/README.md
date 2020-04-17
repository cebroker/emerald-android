# Custom circle graph

#### CircleGraph basic Usage
```html
<co.condorlabs.customcomponents.graphics.circlegraph.CircleGraph
    android:id="@+id/myIdOne"
    android:layout_width="20dp"
    android:layout_height="20dp"/>
```

#### CircleGraph attributes <img src="/Images/graph/circle_graph.png" width="50" heigth="50"/>
| Name                                  | Description                                                   |
|               -                       |                           -                                   |
| circleGraphStrokeWidth    | Set the stroke width of the circle border |

#### Public methods
| Return type   | method                                                        | Description                                                                                                                                       |
|       -       |                    -                                          |                                                                       -                                                                           |
| Unit          | setCircleGraphConfig(circleGraphConfig: CircleGraphConfig)    | Setup the data to paint on the graph. There is a default setting that is used only to see the graph design when adding it to an xml container.    |

> The tests for class CircleGraph was not fully covered because it's not possible to verify the texts and colors properties of the objects that have been drawn using canvas.
