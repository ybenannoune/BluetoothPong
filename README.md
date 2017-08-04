# Bluetooth Pong

This is a proof of concept of real time multiplayer game using bluetooth.

![alt tag](http://image.noelshack.com/fichiers/2017/31/5/1501864881-screenshot-20170804-183745.png)

## Getting Started

Launch the application on both smartphones, choose one as Host, and on the other just push the button "Discover", wait the spinner is filled with available devices.
Then you have to push the button "Connect", and the game start right after.

### Informations

Since Android API 6.0 you need to allow permissions ACCESS_COARSE_LOCATION, location permission is required because Bluetooth scans can be used to gather information about the location of the user.
If permissions are denied the application close itself.

Data transmitted over bluetooth (Position of paddle and Ball after hit with the paddle)
/com.mygdx.game I/BluetoothPong: [80.0] - position of the opponent paddle
com.mygdx.game I/BluetoothPong: {144.77269:465.0:-155.2809:-210.0} - ball.x : ball.y : ball.velocityX : ball.velocityY

###Interfacing with platform specific code
The UI and Bluetooth connection is managed in Android native, while the game is Core.
It is necessary to access platform specific APIs in core to be able to send Bluetooth messages.

The interface is put into the core project : IBluetooth

```java
public interface IBluetooth {
    void write(String str);
    boolean isConnected();
}
```

BluetoothFragment implements this interface.

## Video

https://www.youtube.com/watch?v=8fA_Q2hFQ8w&feature=youtu.be

## License

This project is licensed under the MIT License

