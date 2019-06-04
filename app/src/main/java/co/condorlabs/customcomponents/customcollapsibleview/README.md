# Custom CollapsibleView
Provides a collapsible view with the style

## Anatomy
<img src="/Images/customcollapsibleview/collapsible_anatomy.png" width="400" heigth="400"/>

1. Tag
2. Title
3. Subtitle
4. View collapsible
5. Footer text
6. Footer indicator
7. Container

## Basic Usage
Create a new instance in your layout

```
<co.condorlabs.customcomponents.customcollapsibleview.CollapsibleView
        android:id="@+id/customCollapsibleView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:collapsibleContent="@layout/activity_statespinnerformfield_test"
        app:collapsibleHiddenFooterText="Hide"
        app:collapsibleShowFooterText="Show"
        app:collapsibleSubtitle="Collapsible Subtitle"
        app:collapsibleTag="Collapsible Tag"
        app:collapsibleTitle="Collapsible Title" />
```

## Attributes
| Name | Description  |
| - | - |
| collapsibleContent | add the layout resource file to collapse |
| collapsibleTag | the label for the collapsible view |
| collapsibleTitle | the title for the collapsible view |
| collapsibleSubtitle | the subtitle for the collapsible view |
| collapsibleHiddenFooterText | the text of the footer when the view is collapsed |
| collapsibleShowFooterText | the text of the footer when the view is displayed |
| startCollapsed | start collapsed view when this attribute is `true` |
| collapsibleFooterTextColor | the text color of the footer |
| collapsibleTagColor | the text color of the tag |

## Public methods
| Return Type | Description |
| -| - |
|  Unit | *`fun setCollapsibleContent(collapsibleContent: View)`* <br> Add the collapsible view to the container |
|  View? | *`getCollapsibleContent()`* <br> Get collapsible view from the container |
|  Unit | *`setSectionTag(sectionTag: String?)`* <br> Set tag text |
|  Unit | *`setSectionTitle(sectionTitle: String?)`* <br> Set title text |
|  Unit | *`setSectionSubtitle(sectionSubtitle: String?)`* <br> Set subtitle text |
|  Unit | *`setSectionHiddenFooterText(hiddenFooterText: String?)`* <br> Set footer text for hidden state |
|  Unit | *`setSectionShowFooterText(showFooterText: String?)`* <br> Set footer text for show state |
|  Unit | *`collapse()`* <br> Start view in collapsed mode |
|  Unit | *`setSectionFooterTextColor(footerTextColor: Int?)`* <br> Set footer text color |
|  Unit | *`setSectionTagColor(footerTagColor: Int?)`* <br>  Set tag text color|
|  Unit | *`setOnCollapseListener(collapseListener: OnCollapseListener?)`* <br> Set listener for changes between states |
|  Boolean | *`callOnCollapse(isCollapsed: Boolean)`* <br> Call listener event |

## Examples
<img src="/Images/customcollapsibleview/collapsible_view.gif" width="400" heigth="400"/>
