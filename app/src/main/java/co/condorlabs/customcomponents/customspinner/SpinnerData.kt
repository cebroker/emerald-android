package co.condorlabs.customcomponents.customspinner

data class SpinnerData(
    var id: String,
    var label: String
) {

    override fun toString(): String {
        return label
    }
}