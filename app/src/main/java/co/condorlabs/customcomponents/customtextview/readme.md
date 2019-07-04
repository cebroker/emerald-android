
## Basic Usage

```xml
<co.condorlabs.customcomponents.customtextview.CustomTextView
            android:layout_width="wrap_content"
            android:text="test text"
            app:type_text="title"
            android:id="@+id/tvSubtitle"
            android:layout_height="wrap_content"/>
```

## Attributes

| Name | Description |
| - | - |
| type_text | Should be **title**, **subtitle**, **body**, **sectionTitle**, **link** |

| FontType | FontSize |
| - | - |
| h1 | 30 |
| h2 | 24 |
| h3 | 18 |
| h4 | 16 |
| h5 | 14 |
| h6 | 12 |
| h7 | 9 |
| body | 14 |
| button | 18 |
| smallButton | 14 |
| largeButton | 20 |
| sectionTitle | 14 |
| Link | 18 |

## Public methods
| Return Type | Description |
| -| - |
|  Unit | *`fun setCustomTextViewType(typeText: Int)`* <br> Change type text programatically|

## Constants to defined CustomTextViewType
| Constants | Value |
| -| - |
|  BODY_TYPE | 0 |
|  TITLE_TYPE | 1 |
|  SUBTITLE_TYPE | 2 |
|  SECTION_TITLE_TYPE | 3 |
|  LINK_TYPE | 4 |
|  H2_TITLE_TYPE | 5 |


## Example
<img src="/Images/custom_textview.png" width="400" heigth="400">
