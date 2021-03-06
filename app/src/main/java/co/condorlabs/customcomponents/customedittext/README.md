
# Custom EditText
Emerald provides this component to be used as EditText

## Basic Usage

```xml
    <co.condorlabs.customcomponents.customedittext.BaseEditTextFormField
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:id="@+id/tlBase"
            android:textSize="@dimen/body"
            app:regex="^[0-9]{5}$"
            app:show_validation_icon="true"
            app:placeholder="Hola"
            app:hint="@string/zip_hint"
            app:min_lines="5"
            app:max_lines="8"
            app:multiline="true"
            app:input_type="number"/>
```

## Attributes

| Name | Description |
| - | - |
| app:regex | Regex to be use for validations |
| app:placeholder | Placeholder to show in the EditText|
| app:show_validation_icon | This variable enable or disable the check icon shown if the field is valid|
| app:hint | Field hint|
| app:digits | Characters that it will accept|
| app:input_type | Field input type `number`, `numberDecimal`,`phone`,`password`, `textCapCharacters` or just `text`|
| app:max_lines | Maximum number of lines|
| app:min_lines | Minimum number of lines|
| app:multiline | Will the field display more than one line|
| app:max_characters | Maximum number of characters|
| app:phone_mask | Format used to display the phone number  ***(Only for EditTextPhoneField)*** |

## Public methods
| Return Type | Description |
| - | - |
|  Unit | *`fun setMinLines(minLines: Int)`* <br> Minimum number of lines|
|  Unit | *`fun setMaxLines(maxLines: Int)`* <br> Maximum number of lines|
|  Unit | *`fun setPlaceholder(placeholder: String)`* <br> Set the text placeholder|
|  Unit | *`fun setBackgroundAlpha(backgroundAlpha: Int)`* <br> Set alpha background to the EditText|
|  Unit | *`fun setIsRequired(isRequired: String)`* <br> is the field required?|
|  Unit | *`fun setRegex(regex: String)`* <br> Set the field regex|
|  String | *`fun getValue()`* <br> Get the current value of the field, if the field is an **EditTextPhoneField** it returns the phone number without special characters(**()-**)|
|  Unit | *`fun setEnable(isEnable: Boolean)`* <br> Show the component enable or disable| 
|  Unit | *`fun setRegex(regex: List<String>))`* <br> Add multiple regex to be checked against to| 


## Example
<img src="/Images/edit_text_field.png" width="400" heigth="400">

# Custom EditTextPhoneField	
Allow to apply default phone regex (XXX) XXX-XXXX	

## Basic Usage	

 ```xml	
    <co.condorlabs.customcomponents.customedittext.EditTextPhoneField	
            android:layout_width="match_parent"	
            android:layout_height="wrap_content"/>	
```	

## Important Note
`EditTextPhoneField?.text` shall receive a *10 digits* number to show, otherwise, appear as empty

## Example
<img src="/Images/edit_text_phone.png" width="400">

# Adding integer mask to BaseEditTextFormField
Allow add a integer mask, it is necessary specific the following attributes:

| Attribute | Value |
| - | - |
|  digits | String |
|  input_type | String |
|  mask | String |
|  regex | String |
|  max_characters | String |

## Basic Usage

```xml
   <co.condorlabs.customcomponents.customedittext.BaseEditTextFormField
            android:id="@+id/etWithMaskAndPoints"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:regex="\\d{5}-\\d{4}\\."
            app:mask="#####-####."
            app:input_type="number"
            app:digits="0123456789-."
            app:max_characters="11"
            />
```

## Example
<img src="/Images/edit_text_whit_mask.png" width="400">


