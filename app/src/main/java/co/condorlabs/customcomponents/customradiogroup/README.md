
# Custom RadioGroup

Provides a radiogroup with the style

## Basic Usage

Create a new instance in your layout

```
<co.condorlabs.customcomponents.customradiogroup.RadioGroupFormField
        android:id="@+id/customRadioGroup"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Label" />
```

## Attributes

| Name | Description  |
| -| - |
|  hint  | the label of the spinner  |
|  is_required  | make the field required or not |

## Public methods
| Return Type | Name | Description |
| -| - | - |
|  Unit | *`fun setIsRequired(required: Boolean)`* | Make the field required|
|  Unit | *`setSelectables(selectables: List<Selectable>)`* | Set a list of options|
|  Unit | *`fun showError(message: String)`* | Show an error if there are no selected items|
|  Unit | *`fun clearError()`* | Remove view error|
|  ValidationResult | *`fun isValid()`* | Verify that an item is selected|
|  String | *`fun getValue()`* | Get the current selected item|
|  Unit | *`fun setEnable(isEnabled: Boolean)`* | Enable or disable the radio group and its radio buttons inside |

## Examples
<img src="/Images/customradiogroup/custom_radio_group.gif" width="400" heigth="400"/>
