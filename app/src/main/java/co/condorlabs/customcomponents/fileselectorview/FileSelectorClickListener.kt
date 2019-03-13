package co.condorlabs.customcomponents.fileselectorview

/**
 * @author Oscar Gallon on 3/13/19.
 */
sealed class FileSelectorOption {
    object Gallery : FileSelectorOption()
    object Photo : FileSelectorOption()

}

interface FileSelectorClickListener {

    fun onOptionSelected(fileSelectorOption: FileSelectorOption)
}
