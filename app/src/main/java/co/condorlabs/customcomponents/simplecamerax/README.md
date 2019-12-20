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

```kotlin
if (permissionsGranted()) {
    startActivityForResult(Intent(context, CameraActivity::class.java).putExtras(Bundle().apply {
        putParcelable(CameraActivity.CAMERA_CONFIG_OBJ_PARAM, CameraConfig())
    }),  CAMERA_REQUEST_CODE) 
}
```

### CameraConfig object
```kotlin
CameraConfig(
    titleText = "Camera title",
    descriptionText = "Camera description",
    cancelButtonText = "Cancel button text",
    cropButtonText = "Crop button text",
    keepAspectRatio = false,
    savePhotoPath = "route/to/save/photo/captured.jpg"
)
```

## Public methods
| Return Type | Description |
| -| - |
|  Unit | *`setKeepAspectRatio(keepAspectRatio: Boolean)`* <br> Set if the aspect ratio should be maintained when cropping |

## Attributes
| Type | Name | Description |
| - | - | - |
|  String? | titleText | Camera title |
|  String? | descriptionText | Description text |
|  String? | cancelButtonText | Retake photo button |
|  String? | cropButtonText | Crop photo button |
|  Boolean | keepAspectRatio | Keep aspect ratio when cropping |
|  String? | savePhotoPath | Provide a local or external path to save the photo on device |

## Retrieving data
```kotlin
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode == Activity.RESULT_OK) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            CameraBitmapCache.getBitmap()?.let { bitmapResult ->
                // do something with the captured image...
            }
        }
    }
}
```
