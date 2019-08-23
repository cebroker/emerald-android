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
    app:fileSelectorOptions="gallery|file|camera"
    app:dialog_title="Options" />
```

## Attributes
| Name | Description |
| - | - |
| type_text | Should be **title**, **subtitle**, **body**, **sectionTitle**, **link** |

## Public methods
| Return Type | Description |
| -| - |
|  Unit | *`fun setCustomTextViewType(typeText: Int)`* <br> Change type text programatically|

## Example
<img src="/Images/custom_textview.jpg" width="400" heigth="400">
