# **One Second App**

## **Overview**
**One Second App** is a multimedia journaling application designed to help users capture their daily moments through images, text, and videos. The app compiles these moments into a visually engaging timeline, providing a unique and personalized journaling experience. Users can capture photos, videos, and text entries, which are organized by date and accessible in a timeline view. The app offers features like offline mode with sync, multilingual support (English and Afrikaans), biometric authentication, and more.

## **Features**

### **Implemented Features**

- **Capture Activity:**
  - Captures images and stores the `imageUrl`, `userId`, and `timestamp` in Firebase Firestore.
  - Users can upload images using Firebase Storage.

- **Authentication:**
  - Supports login via Standard Email/Password or SSO (Single Sign-On) using third-party services such as Google.
  - Users can register, log in, log out, and manage sessions.

- **Profile Section:**
  - Users can view and update their profile information.
  - Features a profile banner with profile photo and email.
  - Profile settings menu is available with options to log out or change the profile picture (coming part 3).

- **Image Timeline:**
  - Users can view their images, organized by month in a timeline view.
  - Each month shows all images captured during that time.

- **Quick Capture Image:**
  - Users can quickly capture an image via the Capture Activity.
  - The image is automatically uploaded to Firebase and stored in the timeline.

- **Settings and Languages:**
  - Supports multilingual functionality (English and Afrikaans).
  - Settings for changing language preferences and other profile-related adjustments.

- **SSO Support:**
  - Allows users to register and log in using third-party services such as Google.

- **Push Notifications:**
  - Real-time notifications to remind users to capture their daily moments (coming part 3).

- **Syncing with Firebase:**
  - The app syncs user data (e.g., entries, profile images) with Firebase Firestore and Firebase Storage.

- **Offline Mode with Sync:**
  - Users can capture moments offline, and the app will sync the data once they regain internet connectivity (coming part 3).

---

### **Features In Progress / To Be Implemented (coming part 3)**

- **Profile Photo Change:**
  Allow users to change their profile photo directly from the profile section in the app.

- **Fix Profile Image Outline:**
  Ensure that the black outline around the profile image is visible.

- **Settings Menu:**
  Add a profile and app settings pop-up menu under the settings button. Menu options will include "Change Profile Photo" and "Logout."

- **Backup and Restore:**
  Implement a feature that allows users to back up their media and restore it on different devices.

- **Account Settings:**
  Enhance account management features, such as changing the password, updating email, etc.

- **Help Center and Contact Support:**
  Provide users with access to a help center and allow them to contact support within the app.

- **Import Media:**
  Add the ability to import media files directly from the user’s device into the app.

- **Entry Filtering:**
  Implement filters that allow users to search and filter their journal entries based on various tags.

- **Plus Button Menu:**
  Add a Plus Button Menu with options for adding various types of entries (e.g., photo, video, text).

- **Notification Screen:**
  Provide a screen for viewing and managing notifications within the app.

- **Text to Media Uploads:**
  Allow users to upload text-based entries along with photos and videos.

- **Biometric Authentication:**
  Implement facial recognition and fingerprint authentication for added security.

- **Customizable Timelines:**
  Allow users to customize their timeline with captions, background music, and more.

- **Gamification:**
  Add badges and achievements for consistent journaling activities to motivate users.

- **Memory Slideshow Creation:**
  Allow users to create memory slideshows from selected entries, with options for adding music and transitions.

---

## **Prerequisites**

Before you start, ensure that you have the following installed:

- Android Studio
- Firebase Project (Set up with Firestore and Firebase Storage)
- Firebase Authentication (Standard Email/Password and SSO providers)
- Java SDK (JDK 8+)
- Google Play Services (for Firebase Authentication)

---

## **How to Run the Application**

### **Step 1: Clone the Repository**
```bash
git clone <repository-url>
```
### **Step 2: Open the Project in Android Studio**
1. Open Android Studio.
2. Select **Open an existing project**.
3. Navigate to the cloned project folder.
4. Open the project.

### **Step 3: Set Up Firebase**
1. Create a Firebase Project in the Firebase Console.
2. Add an Android app to your Firebase project.
3. Download the `google-services.json` file from the Firebase console and place it in the `app/` directory of your project.
4. Enable Firebase Authentication, Firestore, and Firebase Storage in the Firebase console.
5. Configure rules for Firestore and Storage as necessary.

### **Step 4: Run the Application**
1. In Android Studio, click on the green **Run** button or use the shortcut `Shift + F10` to build and run the app on your connected device or emulator.
2. The app should launch, and you can start testing the features.

### **Step 5: Test Firebase Authentication**
You can test logging in and registering through the app’s SSO or email/password authentication. Ensure that you have set up SSO providers in the Firebase console.

---

## **Development Schedule (Features from Part 1)**

### **Features Implemented**

- **Capture Activity:**
  Users can capture daily images, which are compiled into a timeline.

- **Multimedia Support:**
  Users can add text, images, and videos to entries (coming part 3).

- **Offline Mode:**
  Users can record entries offline, and they will sync later (coming part 3).

- **Notifications:**
  Custom push notifications remind users to capture moments (fully setup in part 3).

- **SSO Login:**
  Supports email/password login and third-party login via SSO.

- **Multilingual Support:**
  App supports English and Afrikaans.

- **REST API:**
  Connected to REST API that is connected to a database.

---

### **Features To Be Completed**

- **Backup and Restore:**
  Feature for restoring media and timeline entries across devices.

- **Biometric Authentication:**
  Add fingerprint and facial recognition for secure login.

- **Memory Slideshow:**
  Generate memory slideshows from entries with music and effects.

- **Gamification:**
  Implement achievements and badges for consistent journaling.

- **UI Responsiveness:**
  Implement a responsive and adaptable design to allow multiple devices

---

## **Demonstration Video (must contain voice over)**

The following features are shown in the demo video:
- Login with SSO and standard login.
- Creating an account.
- Profile Section with notification and profile settings bar.
- Settings bar with language change, etc.
- Timeline images by month.
- Quick Capture Image and add it to the current month.
- Timeline overview by month.
- Add media to the selected timeline month from the timeline overview.

---
