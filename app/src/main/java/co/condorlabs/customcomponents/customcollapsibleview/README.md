# Custom CollapsibleView
Provides a collapsible view with the style

## Anatomy
<img src="/Images/customcollapsibleview/collapsible_anatomy.png" width="400" heigth="400"/>

1. Item one
2. Item one
3. Item one
4. Item one
5. Item one
6. Item one
7. Item one

## Basic Usage
Create a new instance in your layout

```
<co.condorlabs.customcomponents.customcollapsibleview.CollapsibleView
            android:id="@+id/customCollapsibleView"
            app:collapsibleTag="Collapsible Tag"
            app:collapsibleTitle="Collapsible Title"
            app:collapsibleSubtitle="Collapsible Subtitle"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:collapsibleHiddenFooterText="Hide"
            app:collapsibleShowFooterText="Show"
            app:collapsibleContent="@layout/activity_statespinnerformfield_test"/>
```

## Attributes
| Name | Description  |
| - | - |
| collapsibleContent | a |
| collapsibleTag | a |
| collapsibleTitle | a |
| collapsibleSubtitle | a |
| collapsibleHiddenFooterText | a |
| collapsibleShowFooterText | a |
| startCollapsed | a |
| collapsibleFooterTextColor | a |
| collapsibleTagColor | a |

## Public methods
| Return Type | Description |
| -| - |
|  Unit | *`fun setCollapsibleContent(collapsibleContent: View)`* <br> a |
|  View? | *`getCollapsibleContent()`* <br> a |
|  Unit | *`setSectionTag(sectionTag: String?)`* <br> a |
|  Unit | *`setSectionTitle(sectionTitle: String?)`* <br> a |
|  Unit | *`setSectionSubtitle(sectionSubtitle: String?)`* <br> a |
|  Unit | *`setSectionHiddenFooterText(hiddenFooterText: String?)`* <br> a |
|  Unit | *`setSectionShowFooterText(showFooterText: String?)`* <br> a |
|  Unit | *`setStartCollapsed()`* <br> a |
|  Unit | *`setSectionFooterTextColor(footerTextColor: Int?)`* <br> a |
|  Unit | *`setSectionTagColor(footerTagColor: Int?)`* <br> a |
|  Unit | *`setOnCollapseListener(collapseListener: OnCollapseListener?)`* <br> a |
|  Boolean | *`callOnCollapse(isCollapsed: Boolean)`* <br> a |

## Examples
<img src="/Images/customcollapsibleview/collapsible_view.gif" width="400" heigth="400"/>
