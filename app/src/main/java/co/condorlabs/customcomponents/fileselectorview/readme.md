# File Selector
A component that allows the users to upload files and show a preview through one of the file selector options.
The file selector options can be choose from gallery, take a picture from camera or launch the file picker provided by Android OS.

## Basic Usage

```xml
<co.condorlabs.customcomponents.fileselectorview.FileSelectorField
    android:id="@+id/myCustomLayoutSelector"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:tap_button_title="This is the Title"
    app:fileSelectorOptions="file|camera|gallery"
    app:dialog_title="Options" />
```

## Attributes
| Name | Description |
| - | - |
| fileSelectorOptions | Should be **gallery**, **file**, **camera** |

## Public methods
| Return Type | Description |
| -| - |
|  Unit | *`fun setFileSelectorClickListener(fileSelectorClickListener: FileSelectorClickListener)`* <br> Set the listener that is called when the fileSelectorOptions is selected|
|  Unit | *`fun setFileValue(fileSelectorValue: FileSelectorValue)`* <br> Set the file to show when the user chose the file|

## Example
### File selector
<img src="/Images/fileselector/fileselector.png" width="400" heigth="400">

### preview of the image
<img src="/Images/fileselector/extensionimg.png" width="400" heigth="400">

### Doc Extension
<img src="/Images/fileselector/extensiondoc.png" width="400" heigth="400">

### PDF Extension
<img src="/Images/fileselector/extensionpdf.png" width="400" heigth="400">

### Unknown Extension
<img src="/Images/fileselector/extensionunknown.png" width="400" heigth="400">
