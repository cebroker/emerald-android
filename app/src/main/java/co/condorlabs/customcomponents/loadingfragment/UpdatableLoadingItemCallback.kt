package co.condorlabs.customcomponents.loadingfragment

/**
 * @author Oscar Gallon on 2019-05-03.
 */
interface UpdatableLoadingItem {

    fun updateItemStatus(status: Status)
}

sealed class Status {
    object Pending : Status()
    object Loaded : Status()
    object Error : Status()
}
