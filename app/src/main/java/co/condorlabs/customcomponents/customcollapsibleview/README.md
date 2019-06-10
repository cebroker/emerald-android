# Custom CollapsibleView
Provides a collapsible view with the style

## Anatomy
<img src="/Images/customcollapsibleview/collapsible_view_anatomy.png" width="400" heigth="400"/>

1. Icon
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
        app:collapsibleTitle="Collapsible Title" />
```

## Attributes
| Name | Description  |
| - | - |
| collapsibleContent | add the layout resource file to collapse |
| collapsibleTitle | the title for the collapsible view |
| collapsibleSubtitle | the subtitle for the collapsible view |
| collapsibleHiddenFooterText | the text of the footer when the view is collapsed |
| collapsibleShowFooterText | the text of the footer when the view is displayed |
| startCollapsed | start collapsed view when this attribute is `true` |
| collapsibleFooterTextColor | the text color of the footer |
| useAppCompactPadding | use compact padding |
| collapsibleIcon | the collapsible icon |
| imageTintColor | the tint color for the icon |

## Public methods
| Return Type | Description |
| -| - |
|  Unit | *`fun setImage(imageIconResourceId: Int)`* <br> Add an image icon |
|  Unit | *`fun setContent(collapsibleContent: View)`* <br> Add view to collapse |
|  View? | *`fun getContent()`* <br> Get view |
|  Unit | *`fun setTitle(sectionTitle: String?)`* <br> Set title |
|  String? | *`fun getTitle()`* <br> Get subtitle |
|  Unit | *`fun setSubtitle(sectionSubtitle: String?)`* <br> Set subtitle |
|  String? | *`fun getSubtitle()`* <br> Get subtitle |
|  Unit | *`fun setHiddenFooterText(hiddenFooterText: String?)`* <br> Set text when the view is collapsed |
|  String? | *`fun getHiddenFooterText()`* <br> Get hide text |
|  Unit | *`fun setShowFooterText(showFooterText: String?)`* <br> Set text when the view is displayed |
|  String? | *`fun getShowFooterText()`* <br> Get show text |
|  Unit | *`fun setFooterTextColor(footerTextColor: Int?)`* <br> Set color to footer text |
|  Unit | *`fun setOnCollapseListener(collapseListener: OnCollapseListener?)`* <br> Add listener to collapse event |
|  Unit | *`fun startCollapsed()`* <br> Init the view in collapse mode |
|  Unit | *`fun collapse()`* <br> Collapse the view |
|  Unit | *`setImageTint(colorTint: Int)`* <br> Set color tint to CollapsibleView icon |

## Examples
<img src="/Images/customcollapsibleview/collapsible_animation.gif" width="400" heigth="400"/>
