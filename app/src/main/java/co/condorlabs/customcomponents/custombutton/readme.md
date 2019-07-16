
# Custom Button

Provides a Button with the defined style 

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
|  Unit | *`fun changeState(state: ButtonState)`* <br> Change the button state `Normal` or `Loading`|


## Examples
1. Default <br>
<img src="/Images/custom_button_default.png" width="400" heigth="400"/>
2. Primary <br>
<img src="/Images/custom_button_primary.png" width="400" heigth="400"/>
3. Success <br>
<img src="/Images/custom_button_success.png" width="400" heigth="400"/>
4. Danger <br>
<img src="/Images/custom_button_danger.png" width="400" heigth="400"/>
5. Warning <br>
<img src="/Images/custom_button_warning.png" width="400" heigth="400"/>
6. Info <br>
<img src="/Images/custom_button_info.png" width="400" heigth="400"/>
7. Overlay <br>
<img src="/Images/custom_button_overlay.png" width="400" heigth="400"/>
