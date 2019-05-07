package co.condorlabs.customcomponents.loadingfragment

import co.condorlabs.customcomponents.DEFAULT_TIME_BETWEEN_OBJECT_ANIMATION

/**
 * @author Oscar Gallon on 2019-05-06.
 */
interface LoadingItemsScreen {

    fun updateItemsTilPosition(
        position: Int,
        status: Status,
        timeBetweenObjectAnimation: Long = DEFAULT_TIME_BETWEEN_OBJECT_ANIMATION
    )

    fun showSuccessStatus(btnActionText: String, btnActionCallback: () -> Unit = {})

    fun showErrorStatus(btnActionText: String, btnActionCallback: () -> Unit = {})
}
