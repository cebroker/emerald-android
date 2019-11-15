# Custom CameraActivity
Provides a custom camera to take photos

## Anatomy
<img src="/Images/camera/cameraactivity.png" width="400" heigth="400"/>

1. Camera preview
2. Description text
3. Capture photo button
4. Camera title
5. Rectangle clipping tool
6. Retake photo button
7. Crop photo button

## Basic Usage
Create a new starActivityForResult() instruction and send a CameraConfig object to setup initial camera properties as title, description, etc.

```
if (permissionsGranted()) {
    startActivityForResult(Intent(context, CameraActivity::class.java).putExtras(Bundle().apply {
        putParcelable(CameraActivity.CAMERA_CONFIG_OBJ_PARAM, CameraConfig())
    }),  CAMERA_REQUEST_CODE)
}
```

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
<img src="/Images/camera/camerax_animated.gif" width="400" heigth="400"/>
