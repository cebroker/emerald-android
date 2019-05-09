
# Custom Button

Emerald provides a month and year date picker.

## Basic Usage

Create a new instance in your layout
```
 <co.condorlabs.customcomponents.custombutton.CustomButton android:layout_width="0dp"
                                                              app:layout_constraintTop_toTopOf="parent"
                                                              app:layout_constraintStart_toStartOf="parent"
                                                              app:layout_constraintEnd_toEndOf="parent"
                                                              android:text="@string/click_me"
                                                              android:id="@+id/btn"
                                                              app:type="default"
                                                              android:layout_height="wrap_content"/>
```

## Attributes



| Name | Description  |
| -| - |
|  type | Button type `default`,`primary`,`success`,`danger`,`warning`,`info`,`overlay`   |

## Public methods
| Return Type | Description |
| -| - |
|  Unit | *`fun setType(type: ButtonType)`* <br> Change the button type programatically|


## Examples
1. List <br>
<img src="/Images/loading_fragment_items.png" width="400" heigth="400"/>
2. Success Status <br>
<img src="/Images/loading_fragment_success_status.png" width="400" heigth="400"/>
3. Error Status <br>
<img src="/Images/loading_fragment_error_status.png" width="400" heigth="400"/>
