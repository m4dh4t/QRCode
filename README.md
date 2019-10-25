# QR Code generator
QR Code generator entirely written in Java.

`images` contains QR Codes samples at different stages of the project, useful for debugging purposes and verifying that the project is going in the right way during the developpment phase.

`src/qrcode/` contains the all the code used to generate a fully functional QR Code, separated in different java classes to clarify the process.

`src/reedsolomon/` contains the code used to implement the ECC correction onto the QR Code, to allow devices to read it even if it is partially obfuscated.

`test/qrcode/` contains all the testing java classes used to verify that the code implemented for each classes in `src/qrcode/` is working as expected.
