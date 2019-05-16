package co.condorlabs.customcomponents.loadingfragment

import co.condorlabs.customcomponents.DEFAULT_TIME_BETWEEN_OBJECT_ANIMATION

/**
 * @author Oscar Gallon on 2019-05-06.
 */
typealias ActionListener = () -> Unit

interface LoadingItemsScreen {

    suspend fun updateItemsTilPosition(
        position: Int,
        status: Status,
        timeBetweenObjectAnimation: Long = DEFAULT_TIME_BETWEEN_OBJECT_ANIMATION
    )

    suspend fun showSuccessStatus(btnActionText: String, btnActionCallback: ActionListener = {})

    suspend fun showErrorStatus(btnActionText: String, btnActionCallback: ActionListener = {})
}
