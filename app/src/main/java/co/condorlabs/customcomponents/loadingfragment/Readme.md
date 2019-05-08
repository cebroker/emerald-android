
# Loading Screen

Emerald provides a month and year date picker.

## Basic Usage

Create a new instance in your layout
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
1. List

2. Success Status
