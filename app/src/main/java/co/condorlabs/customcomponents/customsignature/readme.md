# Signature
Emerald provides a signature dialog, it allows the user to sign in a canvas and returns a bitmap through the implementation of the OnDoneSignatureListener interface.

## Basic Usage
```
// Open dialog
SignatureDialog().apply {
    setOnSignatureDoneListener(
        object : SignatureDialog.OnDoneSignatureListener {
            override fun onDoneSignature(bitmap: Bitmap) {
                // do something with the bitmap
        }
    )
    (currentContext as? AppCompatActivity)?.supportFragmentManager
        ?.beginTransaction()
        ?.add(this, MonthYearPickerDialog::class.java.name)
        ?.commitAllowingStateLoss()
}
```

## Example
<img src="/Images/custom-signature-example.png" width="400">
