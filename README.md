# 🚗 AutoReplyDrive (SMS Sender During Driving)

**AutoReplyDrive** is an Android application designed to enhance driver safety by automatically handling incoming phone calls while you're driving. When enabled, the app listens for incoming calls and automatically sends a polite SMS to the caller, letting them know you're currently unavailable.

---

## 📱 Features

* Starts a **foreground service** with a persistent notification.
* Automatically **mutes** the ringtone on incoming calls.
* Sends a **customizable SMS** to the caller:  
  *"Sorry I am driving; will call you later — automated reply."*
* Allows you to **stop the service** manually anytime.
* Designed with Android 13 (API 33) support and above in mind.

---

## 🧠 Purpose

This app promotes **safe driving practices** by reducing distractions from incoming calls. It ensures that callers receive a timely and respectful message when you're on the road, without requiring you to interact with your phone.

---

## 📦 Installation

### ✅ Prerequisites

* Android Studio Dolphin or newer
* Android 13 (API 33) device or emulator (some features may not work on emulators)
* Minimum SDK: 26 (Android 8.0 Oreo)

### 🚀 Steps to Run

1. **Clone this repository:**

   ```bash
   git clone https://github.com/Aswinraj040/AutoReplyDrive
   ```

2. **Open the project in Android Studio.**

3. **Connect your Android device** (or use a compatible emulator).

4. **Build and Run** the app.

---

## 🛠️ Permissions Required

This app requires the following permissions:

| Permission           | Purpose                                           |
| -------------------- | ------------------------------------------------- |
| `READ_PHONE_STATE`   | To monitor phone call states.                     |
| `SEND_SMS`           | To send auto-replies to callers.                  |
| `RECEIVE_SMS`        | To handle incoming SMS events if extended.        |
| `READ_CALL_LOG`      | To identify incoming numbers.                     |
| `POST_NOTIFICATIONS` | To show the persistent driving mode notification. |

On Android 10+ (API 29+), the app **requests to become the default Dialer app** to gain access to call events.

---

## 📷 UI Overview

* **Start Button**: Initiates the foreground service.
* **Notification**: Indicates that Driving Mode is active.
* **Stop Button**: Terminates the service at any time.

---

## 🧪 How It Works

1. Tap the **Start** button.
2. The app starts a **foreground service** and requests required permissions.
3. Once permissions are granted, the app:
   * **Mutes the ringtone** for incoming calls.
   * **Captures the incoming number**.
   * **Sends an automated SMS** to that number.
4. The **Stop** button halts the service when you're done driving.

---

## 🚧 Notes

* Ensure the app is set as the **default Dialer** to monitor call events on Android 10 and above.
* SMS charges may apply based on your carrier plan.
* The message can be modified in the source (`SMS_MESSAGE` constant in `MainActivity.kt`).

---

## 📂 Project Structure

```
com.aswin.smssenderduringdriving/
├── MainActivity.kt              // UI and permission handling
├── DrivingModeService.kt       // Foreground service and call listener
├── DialerActivity.kt           // This is to monitor the incoming calls by making this app as a default dialer app
├── CallReceiver.kt             // It listens to the incoming call, mutes volume, then sends an SMS
├── AndroidManifest.xml         // Permission and service declarations
└── res/
    └── layout/
        └── activity_main.xml   // Simple UI with Start and Stop buttons
```

---

## 🔮 Future Improvements

* **Emergency Call Detection**  
  What if it is an emergency call?  
  As of now, the application sends an SMS to *all* incoming calls. In future versions, users will be able to configure a list of *priority contacts* (e.g., Father, Mother, close relatives). If a call is received from these saved numbers, the app will **not mute** the ringtone and will allow it to **ring at full volume**, helping users respond to genuine emergencies even while driving.

* **Customizable Auto-Reply Message**  
  Users will be able to personalize the automated reply message from the app settings.

* **Driving Detection via Sensors/GPS**  
  The app could automatically detect when the user is driving using motion sensors or GPS, eliminating the need to manually start the service.

* **Do Not Disturb Integration**  
  Integration with Android’s DND mode to further suppress visual or audio distractions.

* **Reply via WhatsApp**  
  Add functionality to send auto-replies via WhatsApp or other messaging platforms (where APIs allow).

* **Analytics Dashboard**  
  A dashboard showing logs of missed calls and sent auto-replies, helping users keep track.

---

## 👨‍💻 Author

**R. Aswin**  
📧 [aswinraj040@gmail.com](mailto:aswinraj040@gmail.com)  
🔗 [GitHub](https://github.com/Aswinraj040) | [LinkedIn](https://www.linkedin.com/in/r-aswin-51388a24b/)

---

## 📜 License

This project is licensed under the [MIT License](https://choosealicense.com/licenses/mit/).
