package co.condorlabs.customcomponents.skeletonview

import co.condorlabs.customcomponents.COMPLETION_TYPE
import co.condorlabs.customcomponents.R
import co.condorlabs.customcomponents.SkeletonTypeNotSupportedException

/**
 * @author Oscar Gallon on 2019-05-17.
 */
typealias SkeletonLayout = Int

enum class SkeletonType(val type: String) {
    Completion(COMPLETION_TYPE)
}

class SkeletonFactory {

    /**
     *This should return a layout based o the skeleton type  we provide
     * Types should be Completion, List
     */
    fun getFragmentByType(type: SkeletonType): Int {

        return when (type.type) {
            COMPLETION_TYPE -> R.layout.fragment_skeleton
            else -> throw SkeletonTypeNotSupportedException()
        }
    }

    fun getSkeletonTypeFromValue(value: String): SkeletonType {
        try {
            return SkeletonType.valueOf(value)
        } catch (e: Exception) {
            throw SkeletonTypeNotSupportedException()
        }
    }
}
