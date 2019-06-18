# Custom CollapsibleView
Provides a collapsible view with the style

## Anatomy
<img src="/Images/customcollapsibleview/collapsible_view_anatomy.png" width="400" heigth="400"/>

1. Image
2. Title
3. Subtitle
4. View collapsible
5. Action label
6. Action indicator arrow
7. Container

## Basic Usage
Create a new instance in your layout

```
<co.condorlabs.customcomponents.customcollapsibleview.CollapsibleView
    android:id="@+id/collapsibleViewTest"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:content="@layout/activity_collapsible_content_view_test"
    app:subtitle="Collapsible subtitle"
    app:collapsibleTitle="Collapsible title"/>
```

## Attributes
| Name | Description  |
| - | - |
| actionLabelColor | the text color of the action label |
| content | add the layout resource file to collapse |
| collapsibleTitle | the title for the collapsible view |
| hideActionLabel | the text of the action label when the view is expanded |
| image | the collapsible image |
| imageTintColor | the tint color for the image |
| isCollapsed | expand view when this attribute is `false` |
| subtitle | the subtitle for the collapsible view |
| showActionLabel | the text of the action label when the view is collapsed |
| useAppCompactPadding | use compact padding |
| visibleIndicatorArrow | show the indicator arrow when is `true` |

## Public methods
| Return Type | Description |
| -| - |
|  Unit | *`collapse()`* <br> Collapse the view |
|  Unit | *`startExpanded()`* <br> Init the view in expanded mode |
|  Unit | *`setImage(imageResourceId: Int)`* <br> Add an image |
|  Unit | *`setImageTint(colorTint: Int)`* <br> Set color tint to CollapsibleView icon |
|  Unit | *`setContent(collapsibleContent: View)`* <br> Add view to collapse |
|  Unit | *`setTitle(title: String?)`* <br> Set title |
|  Unit | *`setSubtitle(subtitle: String?)`* <br> Set subtitle |
|  Unit | *`setHideActionLabel(actionLabel: String?)`* <br> Set text when the view is collapsed |
|  Unit | *`setShowActionLabel(actionLabel: String?)`* <br> Set text when the view is displayed |
|  Unit | *`setActionLabelColor(actionLabelColor: Int?)`* <br> Set color to action label |
|  Unit | *`setOnCollapseListener(collapseListener: OnCollapseListener?)`* <br> Add listener to collapse event |
|  View? | *`getContent()`* <br> Get view |
|  String? | *`getTitle()`* <br> Get subtitle |
|  String? | *`getSubtitle()`* <br> Get subtitle |
|  String? | *`getHideActionLabel()`* <br> Get hide action label |
|  String? | *`getShowActionLabel()`* <br> Get show action label |
|  Unit | *`visibleIndicatorArrow(isVisible: Boolean)`* <br> Set visibility to the indicator arrow |

## Examples
<img src="/Images/customcollapsibleview/collapsible_animation.gif" width="400" heigth="400"/>
