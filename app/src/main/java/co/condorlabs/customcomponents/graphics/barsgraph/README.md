# Custom bar charts

## BarsGraph anatomy
<p align="center"><img src="/Images/graph/bars_graph.png" align="middle" width="55%"/></p>

## StackedBarsGraph anatomy
<p align="center"><img src="/Images/graph/stacked_bars_graph.png" align="middle" width="55%"/></p>

#### BarsGraph basic Usage
```html
<co.condorlabs.customcomponents.graphics.barsgraph.BarsGraph
    android:id="@+id/myBarsGraph"
    android:layout_width="match_parent"
    android:layout_height="200dp" />
```

#### StackedBarsGraph basic Usage
```html
<co.condorlabs.customcomponents.graphics.barsgraph.StackedBarsGraph
    android:id="@+id/myStackedBarsGraph"
    android:layout_width="match_parent"
    android:layout_height="200dp" />
```

#### BarsGraph attributes <img src="/Images/graph/graph1.png" width="50" heigth="50"/>
| Name                                  | Description                                           |
|               -                       |                           -                           |
| barsGraphNumbersTextSize              | Set the size of the numbers                           |
| barsGraphNumbersBold                  | Set the text color of the numbers                     |
| barsGraphLabelsTextSize               | Set the size of the labels                            |
| barsGraphLabelsColor                  | Set the text color of the labels                      |
| barsGraphHorizontalLinesStrokeWidth   | Set the stroke width of the horizontal lines          |
| barsGraphHorizontalLinesColor         | Set the background color of the horizontal lines      |
| barsGraphBarsStrokeWidth              | Set the stroke width of the bars                      |
| barsGraphBarsMargin                   | Set start and end margins between bars and canvas     |

#### Public methods
| Return type   | method                                    | Description                                                                                                                                       |
|       -       |                    -                      |                                                                       -                                                                           |
| Unit          | setupConfig(chartConfig: BarsGraphConfig) | Setup the data to paint on the graph. There is a default setting that is used only to see the graph design when adding it to an xml container.    |

#### StackedBarsGraph attributes <img src="/Images/graph/graph2.png" width="50" heigth="50"/>
| Name                                          | Description                                       |
|               -                               |                       -                           |
| stackedBarsGraphNumbersTextSize               | Set the size of the numbers                       |
| stackedBarsGraphNumbersColor                  | Set the text color of the numbers                 |
| stackedBarsGraphLabelsTextSize                | Set the size of the labels                        |
| stackedBarsGraphLabelsColor                   | Set the text color of the labels                  |
| stackedBarsGraphHorizontalLinesStrokeWidth    | Set the stroke width of the horizontal lines      |
| stackedBarsGraphHorizontalLinesColor          | Set the background color of the horizontal lines  |
| stackedBarsGraphBarsStrokeWidth               | Set the stroke width of the bars                  |
| stackedBarsGraphBarsMargin                    | Set start and end margins between bars and canvas |

#### Public methods
| Return type   | method                                                        | Description                                                                                                                                       |
|       -       |                                -                              |                                                                       -                                                                           |
| Unit          | setupConfig(stackedBarsGraphConfig: StackedBarsGraphConfig)   | Setup the data to paint on the graph. There is a default setting that is used only to see the graph design when adding it to an xml container.    |

## Examples
<img src="/Images/graph/graphics_types.png" width="400" heigth="400"/>
