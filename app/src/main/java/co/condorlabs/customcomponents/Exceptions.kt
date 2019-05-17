package co.condorlabs.customcomponents

/**
 * @author Oscar Gallon on 2019-05-06.
 */
class PositionGreaterThatItemsSizeException : IndexOutOfBoundsException()

class LoadingAdapterNotFoundException : NullPointerException()
class RecyclerViewNotFoundException : NullPointerException()
class ViewHolderNotFoundForPositionException(private val position: Int) :
    NullPointerException("View holder not found for $position")
class ArgumentsNotFoundException: NullPointerException()
class ArgumentNotFoundException(private val argument: String): NullPointerException("The argument $argument need to be specified")
class LoadingFragmentListGreaterThatLimitException: IndexOutOfBoundsException("List should only contains 4 elements")
class MonthYearPickerDialogNotFoundException : NullPointerException()
class SkeletonFragmentNoArgumentsProvidedException: RuntimeException()
class SkeletonTypeNotSupportedException: RuntimeException("You should provide a valid skeleton type: [Completion]")
