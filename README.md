# led-light-strip-android-app
led-light-strip-android-app is a simple and powerful application on the Android operating system that allows users to control an Arduino operating RGB LED Strips with over one billion possible sequences.

## Installation
In order to use led-light-strip-android-app download Android Studio. Clone this repository using:

`$ git clone https://github.com/KDharmarajanDev/led-light-strip-android-app.git`

On the starting screen of Android Studio, open this project. Connect the controller Android phone. With both `Developer Settings` and `USB Debugging` enabled, build the project and run the app on the Android phone. In order to use the app, the hardware discussed [here](https://github.com/KDharmarajanDev/led-light-strip-scheduler) must be used.

## Usage

### Creating and Sending Information
<div>
  <img src="https://user-images.githubusercontent.com/52500655/87620717-21c2c380-c6d4-11ea-8efe-0a2b1b0b3bf1.png" width="25%" height="25%" align="right">
</div>

1. Connect to the Arduino by clicking on `Connection Settings`.

2. Ensure bluetooth is already on. Discover new devices or look at ones already paired. Click on HC-05 bluetooth module to connect. Return back to the main screen.

3. Click the + button to add an `LED Strip` or click the green download button to receive information from the Arduino about the `LED Strips` and sequences.

4. Set the pin numbers for the red pin, green pin, and blue pin to those that correspond on the Arduino. Then proceed to add a sequential generator by clicking the + button.

5. When creating a generator, note that the selection of `LED states` may be randomized by clicking on the random generator option. By default, `LED states` are not randomized. Click on the + button to add an LED state.

6. When choosing an LED state, please specify the duration in seconds. There are two options at this point: `LED State` and `Transition LED State`. An `LED State` makes an `LED Strip` emit one solid color for the duration specified. A `Transition LED State` smoothly transitions between two specified colors across the specified duration. Once this decision has been made, click on the black box to change the color.

7. Use the sliders to change the `RGB` values of each color. Once done, please tap the save button.

8. Repeat this process until satisfying sequences have been completed.

9. Hit the orange upload button to send the information and apply the settings to the Arduino.


<div align = "center">
  <img src="https://user-images.githubusercontent.com/52500655/87620778-4454dc80-c6d4-11ea-9fd8-f4cc04409b6e.png" width="25%" height="25%">
</div>

### Saving Configurations

<div>
  <img src="https://user-images.githubusercontent.com/52500655/90323007-1996b780-df10-11ea-858d-13b5ded1b8d1.png" width="25%" height="25%" align="right">
</div>

1. Click the green button with three stacks on the top right corner. 

2. Click on the button that says `Save Current Configuration`.

3. Type in a name to save the configuration. Duplicate names, a blank space, and "Default" may not be used. "Default" is used to save configurations when the app is shutdown. 

4. Click the save button. 

5. Now, there will be a card populated with the name of the configuration, and two buttons, a trash can and an arrow. Clicking the arrow will set that configuration to the current one. Clicking the trashcan will remove it from the system entirely. 

<div align = "center">
  <img src="https://user-images.githubusercontent.com/52500655/90323046-a8a3cf80-df10-11ea-9b54-90b3a50883a6.png" width="25%" height="25%">
</div>

## License
This project uses the MIT License.
