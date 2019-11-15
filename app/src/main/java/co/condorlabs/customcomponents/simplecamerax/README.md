# Custom CameraActivity
Provides a custom camera to take photos

## Anatomy
<img src="/Images/camera/cameraactivity.png" width="400" heigth="400"/>

<a name="one">1</a>. Camera preview
<a name="two">2</a>. Description text
<a name="three">3</a>. Capture photo button
<a name="four">4</a>. Camera title
<a name="five">5</a>. Rectangle clipping tool
<a name="six">6</a>. Retake photo button
<a name="seven">7</a>. Crop photo button

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
    urlToSavePhoto = "route/to/save/photo/captured.jpg"
)
```

| Type | Name | Description |
| - | - | - |
|  String? | titleText | [1](#four) |
|  String? | descriptionText | [2](#two) |
|  String? | cancelButtonText | [6](#six) |
|  String? | cropButtonText | [7](#seven) |
|  String? | urlToSavePhoto | Provide a local or external path to save the photo on device |

## Retrieving data
```kotlin
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode == Activity.RESULT_OK) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            data?.getByteArrayExtra(CAMERA_TAKE_PHOTO_PARAM)?.let {
                val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                myImageView.setImageBitmap(bitmap)
            }
        }
    }
}
```

## Examples
<img src="/Images/camera/camerax_animated.gif" width="400" heigth="400"/>
