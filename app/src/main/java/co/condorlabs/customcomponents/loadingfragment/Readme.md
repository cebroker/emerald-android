
# Loading Screen

Loading screen, this screen show an spinner with the ability to select multiple items of the list with an animation

## Basic Usage

Create a new instance
```
LoadingFragment.newInstance(  
  title: String,  
  items: ArrayList<LoadingItem>,  
  successTitle: String,  
  errorTitle: String,  
  successMessage: String,  
  errorMessage: String  
)
```

## Attributes



| Name | Description  |
| -| - |
|  title | View title   |
|  items | List of `LoadingItem` maximum number of items 4 |
|  successTitle | Success status title|
|  errorTitle | Error status title|
|  successTitle | Success status message|
|  errorTitle | Error status message|

## Public methods
| Return Type | Description |
| -| - |
|  Unit | *`updateItemsTilPosition(position:Int,status: Status,timeBetweenObjectAnimation:Long)`* <br> Update the `LoadingItem`  list with the status provided til the position provided. This is a `suspend` function |
|  Unit | *`showSuccessStatus(btnActionText: String, btnActionCallback: ActionListener = {})`* <br> Shows the success status, and set the listener for the `btnAction` clickListener. This is a `suspend` function |
|  Unit | *`showErrorStatus(btnActionText: String, btnActionCallback: ActionListener = {})`* <br> Shows the error status, and set the listener for the `btnAction` clickListener. This is a `suspend` function |


## Examples
1. List <br>
<img src="/Images/loading_fragment_items.png" width="400" heigth="400"/>
2. Success Status <br>
<img src="/Images/loading_fragment_success_status.png" width="400" heigth="400"/>
3. Error Status <br>
<img src="/Images/loading_fragment_error_status.png" width="400" heigth="400"/>
