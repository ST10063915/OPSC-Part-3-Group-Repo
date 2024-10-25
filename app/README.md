
# **One Second App**

For the full version, including GitHub workflows and automated testing, visit the repository: [One Second App Repository](https://github.com/VCDN-2024/opsc7312-part-2-ST10063915/tree/master)

## **Overview**

**One Second App** is a multimedia journaling application that allows users to capture daily moments through photos, text, and videos. Entries are organized by date and accessible in a timeline view, creating a personalized journaling experience. The app supports features like offline mode with sync, multilingual support (English and Afrikaans), biometric authentication, and push notifications.

## **Features**

### **Implemented Features**

-   **User Settings Banner:**

  -   Located in the `activity_home` screen, the settings icon (`@drawable/settings_icon`) is linked to a settings menu (`@id/accountButton`) that includes user settings options.
-   **User Profile Banner:**

  -   Displays the current user's profile information instead of a placeholder.
-   **Capture Activity:**

  -   Captures and stores images with metadata, including `imageUrl`, `userId`, and `timestamp`, in Firebase Firestore.
  -   Allows users to upload images directly to Firebase Storage.
-   **Authentication:**

  -   Supports both Standard Email/Password login and Single Sign-On (SSO) with providers like Google.
  -   Users can register, log in, log out, and manage sessions securely.
-   **Profile Section:**

  -   Users can view and update their profile.
  -   Options for changing profile photos and basic user settings are available from the profile settings banner.
-   **Help Center and Contact Support:**

  -   Accessible Help Center and Contact Support banners are linked to the help button in `activity_home`.
-   **Import Media Functionality:**

  -   Media import is available on both the home page and settings menu, allowing selection of specific dates for upload.
-   **Image Timeline:**

  -   Displays images in a monthly-organized timeline, allowing easy navigation through months and their respective entries.
-   **Quick Capture Image:**

  -   Allows quick image capture from the Capture Activity, with automatic upload and storage of image metadata.
-   **Settings and Language Support:**

  -   Multilingual support in both English and Afrikaans.
  -   Language and other profile settings adjustments are accessible in the settings menu.
-   **Push Notifications:**

  -   Real-time notifications for events such as successful uploads, pending uploads, and settings changes.
-   **Firebase Sync:**

  -   Syncs user data, including entries and profile images, to Firebase Firestore and Firebase Storage.
-   **Offline Mode with Sync:**

  -   Supports offline capture, syncing data to Firebase once reconnected.

----------

### **Features Added in Part 3**

-   **Profile Photo Change:**

  -   Users can change their profile photo directly from the profile section.
-   **Enhanced Profile Image Outline:**

  -   Ensures a visible outline around the profile image for improved visibility.
-   **Expanded Settings Menu:**

  -   A settings menu pop-up includes options such as "Change Profile Photo" and "Logout" for easy access.
-   **Backup and Restore:**

  -   Enables media backup and restoration, allowing users to access their timeline on different devices.
-   **Advanced Account Management:**

  -   Users can manage and update account details, such as email and password.
-   **Comprehensive Help and Contact Support:**

  -   Expanded Help Center and Contact Support options, providing users with direct access to assistance.
-   **Extended Import Media:**

  -   Allows users to import multiple media types from their devices directly into the app.
-   **Entry Filtering and Search:**

  -   Users can filter and search journal entries by tags and other criteria for easier organization and retrieval.
-   **More Settings Options:**

  -   An additional More Settings menu offers custom configuration options for user preferences.
-   **Notification Management:**

  -   Users have a notification management screen for viewing and adjusting alert settings.
-   **Text Entries Support:**

  -   Supports text-based journal entries alongside photo and video uploads.
-   **Biometric Authentication:**

  -   Fingerprint and facial recognition support for secure app access.
-   **Video Timeline:**

  -   Users can capture and compile daily videos into a timeline, creating a visual diary.
-   **Multimedia Support:**

  -   Photos, text, and videos can be combined in entries, allowing flexibility in documenting experiences.
-   **Real-time Notifications:**

  -   Push notifications remind users to make daily entries, with customizable reminder preferences.
-   **Multi-language Support:**

  -   Includes English and two South African languages, making the app accessible to a wider audience.
-   **SSO Registration and Login:**

  -   Allows users to log in using Google, Facebook, or Apple, simplifying authentication.
-   **Customizable Timelines:**

  -   Enables users to create personalized timelines with tags, captions, and background music.
-   **Gamification:**

  -   Reward system with badges encourages consistent journaling and interaction with the app.

----------

## **Prerequisites**

Before starting, ensure the following installations are completed:

-   **Android Studio**
-   **Firebase Project** (Set up with Firestore and Firebase Storage)
-   **Firebase Authentication** (Email/Password and SSO providers)
-   **Java SDK (JDK 8+)**
-   **Google Play Services** (for Firebase Authentication)

----------

## **How to Run the Application**

### **Step 1: Clone the Repository**

```bash

`git clone <gh repo clone VCDN-2024/opsc7312-part-poe-ST10063915>`
```

### **Step 2: Open the Project in Android Studio**

1.  Open Android Studio.
2.  Select **Open an existing project**.
3.  Navigate to the cloned project folder.
4.  Open the project.

### **Step 3: Set Up Firebase**

1.  Create a Firebase Project in the Firebase Console.
2.  Add an Android app to your Firebase project.
3.  Download the `google-services.json` file from the Firebase console and place it in the `app/` directory of your project.
4.  Enable Firebase Authentication, Firestore, and Firebase Storage in the Firebase console.
5.  Configure rules for Firestore and Storage as necessary.

### **Step 4: Run the Application**

1.  In Android Studio, click on the green **Run** button or use the shortcut `Shift + F10` to build and run the app on your connected device or emulator.
2.  The app should launch, and you can start testing the features.

### **Step 5: Test Firebase Authentication**

You can test logging in and registering through the app’s SSO or email/password authentication. Ensure that you have set up SSO providers in the Firebase console.

----------

## **Development Schedule and Completed Features**

### **Part 3 Features and Updates**

-   **Capture Activity:** Users can capture daily images, which are compiled into a timeline.

-   **Multimedia Support:** Users can add text, images, and videos to entries.

-   **Offline Mode:** Users can record entries offline, and they will sync later when online.

-   **Notifications:** Custom push notifications remind users to capture moments.

-   **SSO Login:** Supports email/password login and third-party login via SSO.

-   **Multilingual Support:** App supports English and Afrikaans, as well as other South African languages.

-   **REST API:** Connected to REST API that communicates with a cloud database.


----------

### **Planned Features and Enhancements**

-   **Backup and Restore:** Enables media and timeline entry restoration on new devices.

-   **Advanced Biometric Authentication:** Adds fingerprint and facial recognition for secure login.

-   **Memory Slideshow:** Allows users to create memory slideshows from entries, with music and effects.

-   **Gamification:** Adds achievements and badges for consistent journaling.

-   **Responsive Design:** Implements a responsive design to enhance user experience across devices.


----------

## **Demonstration Video**

The following features are shown in the demo video:

-   Login with SSO, standard login or biometric .
-   Registering an account.
-   Profile section and notification bar.
-   Import images via Home page or in selected category 
-   Settings bar with language change and other preferences.
-   Monthly timeline of images.
-   Quick Capture Image feature and image upload.
-   Timeline overview by month.
-   Media import functionality by month from the timeline overview.
-   Adjust user profile settings 
-   contact support, help center and other settings have been enabled
-   Notifications have been enabled