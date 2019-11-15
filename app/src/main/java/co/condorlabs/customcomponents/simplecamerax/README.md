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
