
# Custom Spinner

Provides a spinner with the style

## Basic Usage

Create a new instance in your layout

```
<co.condorlabs.customcomponents.customspinner.SpinnerFormField
            android:layout_width="match_parent"
            app:label="State"
            app:is_required="true"
            app:hint="Select an State"
            android:id="@+id/tlState"
            android:layout_height="wrap_content"/>
```

## Attributes

| Name | Description  |
| -| - |
|  hint  | the label of the spinner  |
|  is_required  | make the field required or not |
|  enable  | enable or disable the spinner by default true  |

## Public methods
| Return Type | Description |
| -| - |
|  Unit | *`fun setIsRequired(required: Boolean)`* <br> make the field required|
|  Unit | *`fun setData(data: List<SpinnerData>)`* <br> set the spinner data|
|  Unit | *`fun setItemSelectedById(id: String)`* <br> Select an item by his id|
|  Unit | *`fun setItemSelectedHint()`* <br> Select the hint item|
|  SpinnerData | *`fun getValue()* <br> Get the current selected data|

## Examples
1. Close <br>
<img src="/Images/custom_spinner_closed.png" width="400" heigth="400"/>
2. Open <br>
<img src="/Images/custom_spinner_open.png" width="400" heigth="400"/>
3. Selected <br>
<img src="/Images/custom_spinner_selected.png" width="400" heigth="400"/>

