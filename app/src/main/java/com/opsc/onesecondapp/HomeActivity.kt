package com.opsc.onesecondapp

import TimelineAdapter
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class HomeActivity : AppCompatActivity() {

    private var isEnglish = true
    private var isProfileImageSelection = false

    private var notifications = false

    private lateinit var captureButton: ImageButton
    private lateinit var settingsButton: ImageButton
    private lateinit var profileButton: ImageView
    private lateinit var timelineRecyclerView: RecyclerView
    private lateinit var timelineAdapter: TimelineAdapter
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseStorage: FirebaseStorage

    // Profile banner elements
    private lateinit var bannerLayout: LinearLayout
    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var notificationIcon: ImageButton
    private lateinit var settingsIcon: ImageButton
    private lateinit var profileImageView: ImageView

    // Settings banner elements
    private lateinit var languageButton: TextView
    private lateinit var backupRestoreButton: TextView
    private lateinit var accountButton: TextView
    private lateinit var helpCenterButton: TextView
    private lateinit var contactSupportButton: TextView
    private lateinit var importMediaButton: TextView
    private lateinit var moreSettingsButton: TextView
    private lateinit var logoutButton: TextView
    private lateinit var notificationSwitch: Switch
    private lateinit var settingsBannerLayout: LinearLayout
    private lateinit var notificationText: TextView

    // Profile Settings banner elements
    private lateinit var changeProfileBannerLayout: LinearLayout
    private lateinit var changeProfileImageText: TextView
    private lateinit var changeUsernameText: TextView
    private lateinit var changeEmailText: TextView
    private lateinit var changePasswordText: TextView

    // Help Center banner elements
    private lateinit var helpCenterBannerLayout: LinearLayout
    private lateinit var faqText: TextView
    private lateinit var uploadMediaText: TextView
    private lateinit var importMediaText: TextView
    private lateinit var backupRestoreText: TextView
    private lateinit var accountText: TextView

    // Contact Us banner elements
    private lateinit var contactSupportBannerLayout: LinearLayout
    private lateinit var contactUsText: TextView
    private lateinit var phoneNumberText: TextView
    private lateinit var emailAddressText: TextView

    // notification repeater
    private lateinit var dynamicBannerLayout: LinearLayout
    private lateinit var sampleItems: MutableList<String>



    private lateinit var auth: FirebaseAuth
    private lateinit var rootLayout: RelativeLayout

    companion object {
        const val IMAGE_PICK_REQUEST_CODE = 1001
        const val PREFS_NAME = "NotificationPrefs"
        const val NOTIFICATIONS_ENABLED = "notificationsEnabled"
        const val CHANNEL_ID = "ImmediateNotificationChannel"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()

        loadProfileImage()





        rootLayout = findViewById(R.id.rootLayout)
        captureButton = findViewById(R.id.captureButton)
        settingsButton = findViewById(R.id.settingsButton)
        profileButton = findViewById(R.id.profileButton)
        timelineRecyclerView = findViewById(R.id.timelineRecyclerView)

        // Profile banner elements
        bannerLayout = findViewById(R.id.bannerLayout)
        nameTextView = findViewById(R.id.nameTextView)
        emailTextView = findViewById(R.id.emailTextView)
        dateTextView = findViewById(R.id.dateTextView)
        notificationIcon = findViewById(R.id.notificationIcon)
        settingsIcon = findViewById(R.id.settingsIcon)
        profileImageView = findViewById(R.id.profileImageView)

        // Settings banner elements
        languageButton = findViewById(R.id.languageButton)
        backupRestoreButton = findViewById(R.id.backupRestoreButton)
        accountButton = findViewById(R.id.accountButton)
        helpCenterButton = findViewById(R.id.helpCenterButton)
        contactSupportButton = findViewById(R.id.contactSupportButton)
        importMediaButton = findViewById(R.id.importMediaButton)
        moreSettingsButton = findViewById(R.id.moreSettingsButton)
        logoutButton = findViewById(R.id.logoutButton)
        notificationSwitch = findViewById(R.id.notificationSwitch)
        settingsBannerLayout = findViewById(R.id.settingsBannerLayout)
        notificationText = findViewById(R.id.notificationText)

        // Profile Settings Banner
        changeProfileBannerLayout = findViewById(R.id.changeProfileBannerLayout)
        changeProfileImageText = findViewById(R.id.changeProfileImageText)
        changeUsernameText = findViewById(R.id.changeUsernameText)
        changeEmailText = findViewById(R.id.changeEmailText)


        // Help Center Banner
        helpCenterBannerLayout = findViewById(R.id.helpCenterBannerLayout)
        faqText = findViewById(R.id.faqText)
        uploadMediaText = findViewById(R.id.uploadMediaText)
        importMediaText = findViewById(R.id.importMediaText)
        backupRestoreText = findViewById(R.id.backupRestoreText)
        accountText = findViewById(R.id.accountText)

        // Contact Us Banner
        contactSupportBannerLayout = findViewById(R.id.contactSupportBannerLayout)
        contactUsText = findViewById(R.id.contactUsText)
        phoneNumberText = findViewById(R.id.phoneNumberText)
        emailAddressText = findViewById(R.id.emailAddressText)


        // Initialize dynamic banner and sample items
        dynamicBannerLayout = findViewById(R.id.dynamicBannerLayout)
        sampleItems = mutableListOf() // Will hold notification messages dynamically

        // Initialize the notification state from SharedPreferences
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val notificationsEnabled = sharedPreferences.getBoolean(NOTIFICATIONS_ENABLED, false)
        notificationSwitch.isChecked = notificationsEnabled


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1001)
            }
        }

        if(notifications){
            sendNotification("Welcome To the One Second App!")
        }


        populateDynamicBanner(sampleItems)





        val plusButton: ImageButton = findViewById(R.id.plusButton)

        val languageButton: TextView = findViewById(R.id.languageButton)
        updateLanguageText()

        timelineRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        timelineAdapter = TimelineAdapter(listOf(), isEnglish)
        timelineRecyclerView.adapter = timelineAdapter


        loadTimelineEntries()


        captureButton.setOnClickListener {
            startActivity(Intent(this, CaptureActivity::class.java))

        }

        settingsButton.setOnClickListener {
            toggleSettingsBanner()


        }

        //import
        plusButton.setOnClickListener {
            isProfileImageSelection = false
            importMedia()
        }

        profileButton.setOnClickListener {
            toggleProfileBanner()
        }

        logoutButton.setOnClickListener {
            logout()
        }

        notificationIcon.setOnClickListener {
            openNotifications()
        }

        settingsIcon.setOnClickListener {
            showSettingsToast()
        }

        // Notification switch toggle
        notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                enableNotifications()

                notifications = true
                sendNotification(if (isEnglish) "Notifications have been enabled" else "Kennisgewings is geaktiveer")
            } else {
                disableNotifications()
                sendNotification(if (isEnglish) "Notifications have been disabled" else "Kennisgewings is gedeaktiveer")
                notifications = false
            }

            // Save the notification state in SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putBoolean(NOTIFICATIONS_ENABLED, isChecked)
            editor.apply()


            populateDynamicBanner(sampleItems)
        }

        languageButton.setOnClickListener {
            isEnglish = !isEnglish
            updateLanguageText()
        }

        backupRestoreButton.setOnClickListener {
            Toast.makeText(this, if (isEnglish) "Backup and Restoring Content!" else "Rugsteun en herstel inhoud!", Toast.LENGTH_SHORT).show()
        }
        //USer account settings, this is the same user setting banner from the profile image
        accountButton.setOnClickListener {
            toggleProfileSettingsBanner()
        }
        helpCenterButton.setOnClickListener {
            toggleHelpCenterBanner()
        }

        contactSupportButton.setOnClickListener {
            toggleContactSupportBanner()
        }

        importMediaButton.setOnClickListener {
            isProfileImageSelection = false
            importMedia()
        }

        moreSettingsButton.setOnClickListener {
            toggleProfileSettingsBanner()
        }

        // Profile Settings Banner click listeners
        changeProfileImageText.setOnClickListener {
            isProfileImageSelection = true
            selectProfileImage()
        }

        changeUsernameText.setOnClickListener {
            Toast.makeText(this, if (isEnglish) "Change Username" else "Verander Gebruikersnaam", Toast.LENGTH_SHORT).show()
            // Logic to change username goes here
        }

        changeEmailText.setOnClickListener {
            Toast.makeText(this, if (isEnglish) "Change Email" else "Verander E-pos", Toast.LENGTH_SHORT).show()
            // Logic to change email goes here
        }


        // Help Center Banner click listeners
        uploadMediaText.setOnClickListener {
            isProfileImageSelection = false
            importMedia()
        }

        //import
        importMediaText.setOnClickListener {
            isProfileImageSelection = false
            importMedia()
        }

        backupRestoreText.setOnClickListener {
            Toast.makeText(this, if (isEnglish) "Backup and Restore Coming Soon!" else "Rugsteun en herstel kom binnekort!", Toast.LENGTH_SHORT).show()
        }

        accountText.setOnClickListener {
            toggleProfileSettingsBanner()
        }

        // Contact Us Banner click listeners
        contactUsText.setOnClickListener {
            toggleContactSupportBanner()
        }

        phoneNumberText.setOnClickListener {
            Toast.makeText(this, if (isEnglish) "Calling +1-123-456-7890" else "Skakel +1-123-456-7890", Toast.LENGTH_SHORT).show()
        }

        emailAddressText.setOnClickListener {
            Toast.makeText(this, if (isEnglish) "Email: onesecond@gmail.com" else "E-pos: onesecond@gmail.com", Toast.LENGTH_SHORT).show()
        }


        setCurrentDate()
        loadProfileDetails()
        setupTouchListeners()
    }

    // Function to update the language button text based on the boolean
    private fun updateLanguageText() {
        languageButton.text = if (isEnglish) "Change Language" else "Taal Verander"
        backupRestoreButton.text = if (isEnglish) "Backup and Restore" else "Rugsteun en Herstel"
        accountButton.text = if (isEnglish) "Account" else "Rekening"
        helpCenterButton.text = if (isEnglish) "Help Center" else "Hulpsentrum"
        contactSupportButton.text = if (isEnglish) "Contact Support" else "Kontak Ondersteuning"
        importMediaButton.text = if (isEnglish) "Import Media" else "Media Invoer"
        moreSettingsButton.text = if (isEnglish) "More Settings" else "Meer Instellings"
        logoutButton.text = if (isEnglish) "Logout" else "Teken Uit"
        notificationText.text = if (isEnglish) "Notifications" else "Kennisgewings"

        // Help Center banner translations
        faqText.text = if (isEnglish) "FAQ" else "Vrae"
        uploadMediaText.text = if (isEnglish) "Upload Media" else "Laai Media Op"
        importMediaText.text = if (isEnglish) "Import Media" else "Invoer Media"
        backupRestoreText.text = if (isEnglish) "Backup and Restore" else "Rugsteun en Herstel"
        accountText.text = if (isEnglish) "Account" else "Rekening"

        // Contact Us banner translations
        contactUsText.text = if (isEnglish) "Contact Us" else "Kontak Ons"
        phoneNumberText.text = if (isEnglish) "Phone: +1-123-456-7890" else "Telefoon: +1-123-456-7890"
        emailAddressText.text = if (isEnglish) "Email: onesecond@gmail.com" else "E-pos: onesecond@gmail.com"

        // User Profile Settings
        changeProfileImageText.text = if (isEnglish) "Change Profile Image" else "Verander Profielprent"
        changeUsernameText.text = if (isEnglish) "Change Username" else "Verander Gebruikersnaam"
        changeEmailText.text = if (isEnglish) "Change Email" else "Verander E-pos"

        loadTimelineEntries()
    }

    // Function to load timeline entries from Firestore
    private fun loadTimelineEntries() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            firestore.collection("users").document(userId).collection("timeline")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener { documents ->
                    val groupedItems = documents.groupBy { doc ->

                        val timestamp = doc.get("timestamp")
                        val date = when (timestamp) {
                            is com.google.firebase.Timestamp -> timestamp.toDate()
                            is Long -> Date(timestamp)
                            is String -> {
                                try {
                                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(timestamp)
                                } catch (e: Exception) {
                                    Date()
                                }
                            }
                            else -> Date()
                        }

                        val locale = if (isEnglish) Locale.getDefault() else Locale("af")
                        val calendar = Calendar.getInstance().apply { time = date }
                        SimpleDateFormat("MMMM yyyy", locale).format(calendar.time)
                    }

                    val items = groupedItems.map { (month, docs) ->
                        val sortedDocs = docs.sortedBy { doc ->
                            val timestamp = doc.get("timestamp")
                            val date = when (timestamp) {
                                is com.google.firebase.Timestamp -> timestamp.toDate()
                                is Long -> Date(timestamp)
                                is String -> {
                                    try {
                                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(timestamp)
                                    } catch (e: Exception) {
                                        Date()
                                    }
                                }
                                else -> Date()
                            }
                            date.time
                        }

                        val firstImage = sortedDocs.firstOrNull()

                        val firstDayOfMonth = getFirstDayOfMonth(month, isEnglish)
                        val lastDayOfMonth = getLastDayOfMonth(month, isEnglish)

                        TimelineItem(
                            month = month,
                            name = firstImage?.getString("name") ?: "",
                            imageUrl = firstImage?.getString("imageUrl") ?: "",
                            startDate = firstDayOfMonth,
                            endDate = lastDayOfMonth
                        )
                    }

                    timelineAdapter = TimelineAdapter(items, isEnglish)
                    timelineRecyclerView.adapter = timelineAdapter
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to load timeline", Toast.LENGTH_SHORT).show()
                }
        }
    }

    // Function to load profile details from Firestore
    private fun loadProfileDetails() {
        val user = auth.currentUser
        user?.let {
            firestore.collection("users").document(it.uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val name = document.getString("username")
                        val email = document.getString("email")
                        nameTextView.text = name
                        emailTextView.text = email
                    }
                }
                .addOnFailureListener {
                    //need to implement error handling (fix)
                }
        }
    }

    // Function to toggle the visibility of the profile banner
    private fun toggleProfileBanner() {
        if (bannerLayout.visibility == View.GONE) {
            bannerLayout.visibility = View.VISIBLE
            settingsBannerLayout.visibility = View.GONE
            changeProfileBannerLayout.visibility = View.GONE
            helpCenterBannerLayout.visibility = View.GONE
            contactSupportBannerLayout.visibility = View.GONE
            dynamicBannerLayout.visibility = View.GONE
        } else {
            bannerLayout.visibility = View.GONE
        }
    }

    // Function to toggle the visibility of the profile settings banner
    private fun toggleProfileSettingsBanner() {
        if (changeProfileBannerLayout.visibility == View.GONE) {
            changeProfileBannerLayout.visibility = View.VISIBLE
            settingsBannerLayout.visibility = View.GONE
            helpCenterBannerLayout.visibility = View.GONE
            contactSupportBannerLayout.visibility = View.GONE
            dynamicBannerLayout.visibility = View.GONE
        } else {
            changeProfileBannerLayout.visibility = View.GONE
        }
    }

    // Function to toggle the visibility of the settings banner
    private fun toggleSettingsBanner() {
        if (settingsBannerLayout.visibility == View.GONE) {
            settingsBannerLayout.visibility = View.VISIBLE
            bannerLayout.visibility = View.GONE
            changeProfileBannerLayout.visibility = View.GONE
            helpCenterBannerLayout.visibility = View.GONE
            contactSupportBannerLayout.visibility = View.GONE
            dynamicBannerLayout.visibility = View.GONE
        } else {
            settingsBannerLayout.visibility = View.GONE
        }
    }

    // Function to toggle the visibility of the help center banner
    private fun toggleHelpCenterBanner() {
        if (helpCenterBannerLayout.visibility == View.GONE) {
            helpCenterBannerLayout.visibility = View.VISIBLE
            bannerLayout.visibility = View.GONE
            changeProfileBannerLayout.visibility = View.GONE
            settingsBannerLayout.visibility = View.GONE
            contactSupportBannerLayout.visibility = View.GONE
            dynamicBannerLayout.visibility = View.GONE
        } else {
            helpCenterBannerLayout.visibility = View.GONE
        }
    }

    // Function to toggle the visibility of the contact support banner
    private fun toggleContactSupportBanner() {
        if (contactSupportBannerLayout.visibility == View.GONE) {
            contactSupportBannerLayout.visibility = View.VISIBLE
            bannerLayout.visibility = View.GONE
            changeProfileBannerLayout.visibility = View.GONE
            settingsBannerLayout.visibility = View.GONE
            helpCenterBannerLayout.visibility = View.GONE
            dynamicBannerLayout.visibility = View.GONE
        } else {
            contactSupportBannerLayout.visibility = View.GONE
        }
    }

    // Function to log out the user
    private fun logout() {
        auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun openNotifications() {
        // Assuming you have already defined dynamicBannerLayout somewhere in your activity
        if (dynamicBannerLayout.visibility == View.GONE) {
            dynamicBannerLayout.visibility = View.VISIBLE
            // Hide other banners if they are visible
            bannerLayout.visibility = View.GONE
            changeProfileBannerLayout.visibility = View.GONE
            settingsBannerLayout.visibility = View.GONE
            helpCenterBannerLayout.visibility = View.GONE
            contactSupportBannerLayout.visibility = View.GONE

        } else {
            dynamicBannerLayout.visibility = View.GONE
        }
    }

    private fun showSettingsToast() {
        toggleProfileSettingsBanner()

    }

    private fun setCurrentDate() {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val currentDate = sdf.format(Date())
        dateTextView.text = currentDate
    }

    // Setting up touch listeners on the root layout to close the banners
    private fun setupTouchListeners() {
        rootLayout.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val bannerRect = Rect()
                val settingsRect = Rect()

                bannerLayout.getGlobalVisibleRect(bannerRect)
                settingsBannerLayout.getGlobalVisibleRect(settingsRect)

                if (!bannerRect.contains(event.rawX.toInt(), event.rawY.toInt()) &&
                    !settingsRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    bannerLayout.visibility = View.GONE
                    settingsBannerLayout.visibility = View.GONE
                }
            }
            view.performClick()
            false
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }


    private fun getFirstDayOfMonth(month: String, isEnglish: Boolean): Long {
        val calendar = Calendar.getInstance()
        val locale = if (isEnglish) Locale.getDefault() else Locale("af")
        val sdf = SimpleDateFormat("MMMM yyyy", locale)
        return try {
            calendar.time = sdf.parse(month) ?: throw IllegalArgumentException("Invalid month format")
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            calendar.timeInMillis
        } catch (e: Exception) {
            e.printStackTrace()
            System.currentTimeMillis()
        }
    }

    private fun getLastDayOfMonth(month: String, isEnglish: Boolean): Long {
        val calendar = Calendar.getInstance()
        val locale = if (isEnglish) Locale.getDefault() else Locale("af")
        val sdf = SimpleDateFormat("MMMM yyyy", locale)
        return try {
            calendar.time = sdf.parse(month) ?: throw IllegalArgumentException("Invalid month format")
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
            calendar.timeInMillis
        } catch (e: Exception) {
            e.printStackTrace()
            System.currentTimeMillis()
        }
    }



    private fun importMedia() {
        // Open the file picker to select an image
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, IMAGE_PICK_REQUEST_CODE)
    }


    // Function to show a date picker and return the selected date as a timestamp
    private fun showDatePicker(onDateSelected: (Long) -> Unit) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                onDateSelected(calendar.timeInMillis)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun uploadImageToFirebase(uri: Uri, selectedDate: Long) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "User is not authenticated", Toast.LENGTH_SHORT).show()
            return
        }

        val storageReference = firebaseStorage.reference
            .child("users/$userId/images/${System.currentTimeMillis()}.jpg")

        val uploadTask = storageReference.putFile(uri)

        uploadTask.addOnSuccessListener {
            storageReference.downloadUrl.addOnSuccessListener { downloadUri ->
                saveImageMetadataToFirestore(downloadUri.toString(), selectedDate)
            }
        }.addOnFailureListener {
            val message = if (isEnglish) "Image upload failed" else "Beeldoplaai het misluk"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
    // Save image metadata to Firestore
    private fun saveImageMetadataToFirestore(imageUrl: String, selectedDate: Long) {
        val userId = auth.currentUser?.uid ?: return

        val entryData = hashMapOf(
            "imageUrl" to imageUrl,
            "timestamp" to com.google.firebase.Timestamp(selectedDate / 1000, 0),
            "userId" to userId
        )

        firestore.collection("users").document(userId).collection("timeline")
            .add(entryData)
            .addOnSuccessListener {
                val message = if (isEnglish) "Image metadata saved" else "Beeldmetadata gestoor"
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

                loadImagesForMonth(selectedDate)
            }
            .addOnFailureListener {
                val message = if (isEnglish) "Failed to save metadata" else "Kon nie metadata stoor nie"
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
    }

    // Function to reload images for the selected month
    private fun loadImagesForMonth(selectedDate: Long) {
        // Logic to reload the images for the selected month from Firestore
        loadTimelineEntries()
    }

    // Function to load profile image from Firebase Storage
    private fun loadProfileImage() {
        val userId = auth.currentUser?.uid ?: return
        val storageReference: StorageReference = firebaseStorage.reference
            .child("users/$userId/profileImage.jpg") // Assuming this is the profile image path

        storageReference.downloadUrl.addOnSuccessListener { uri ->
            // Load the image using Glide
            Glide.with(this)
                .load(uri)
                .placeholder(R.drawable.sample_image)
                .error(R.drawable.sample_image)
                .into(profileButton)

            Glide.with(this)
                .load(uri)
                .placeholder(R.drawable.sample_image)
                .error(R.drawable.sample_image)
                .into(profileImageView)
        }.addOnFailureListener {
            Toast.makeText(this, if (isEnglish) "Failed to load profile image" else "Kon nie profielfoto laai nie", Toast.LENGTH_SHORT).show()
        }
    }


    // Function to allow the user to select a profile image
    private fun selectProfileImage() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, IMAGE_PICK_REQUEST_CODE)
    }

    // Handle the result from image selection or media import
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            val uri: Uri? = data?.data

            when (requestCode) {
                IMAGE_PICK_REQUEST_CODE -> {
                    // This handles both profile image and media import cases
                    if (uri != null) {
                        if (isProfileImageSelection) {
                            // Upload profile image
                            uploadProfileImageToFirebase(uri)
                        } else {
                            // Import media and upload it with selected date
                            showDatePicker { selectedDate ->
                                uploadImageToFirebase(uri, selectedDate)
                            }
                        }
                    }
                }
            }
        }
    }

    // Function to upload the selected profile image to Firebase Storage
    private fun uploadProfileImageToFirebase(uri: Uri) {
        val userId = auth.currentUser?.uid ?: return
        val storageReference: StorageReference = firebaseStorage.reference
            .child("users/$userId/profileImage.jpg")

        val uploadTask = storageReference.putFile(uri)

        uploadTask.addOnSuccessListener {
            storageReference.downloadUrl.addOnSuccessListener { downloadUri ->
                Toast.makeText(this, if (isEnglish) "Profile image uploaded successfully!" else "Profielfoto suksesvol opgelaai!", Toast.LENGTH_SHORT).show()

                // Call loadProfileImage() to refresh the profile button
                loadProfileImage()
            }
        }.addOnFailureListener {
            val message = if (isEnglish) "Failed to upload profile image" else "Kon nie profielfoto oplaai nie"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendNotification(message: String) {
        createNotificationChannel()  // Ensure the channel is created

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Notification")
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_notification_overlay) // Use your app's icon here
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)  // Automatically remove the notification when clicked
            .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1001, notification) // Ensure the ID is unique per notification

        sampleItems.add(message)
        populateDynamicBanner(sampleItems)
    }

    // Enable background notifications
    private fun enableNotifications() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()

        val workRequest = PeriodicWorkRequest.Builder(
            NotificationWorker::class.java,
            15, TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueue(workRequest)

        sendImmediateNotification("Notifications have just been enabled")

    }

    // Send an immediate notification
    private fun sendImmediateNotification(message: String) {
        createNotificationChannel()

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Notification Alert")
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_notification_overlay)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1003, notification) // Use a unique ID for the notification
    }

    // Create notification channel (for Android O and higher)
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Immediate Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for immediate notifications"
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Disable background notifications
    private fun disableNotifications() {
        WorkManager.getInstance(this).cancelAllWork()
    }

    // Function to populate the dynamic banner with items
    private fun populateDynamicBanner(items: List<String>) {
        // Clear any existing views before populating
        dynamicBannerLayout.removeAllViews()

        for ((index, item) in items.withIndex()) {
            // Create a new LinearLayout programmatically for each item
            val itemLayout = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(0, 32, 0, 32)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }

            // Create a TextView for each item
            val itemText = TextView(this).apply {
                text = item
                setTextColor(resources.getColor(android.R.color.black, null))
                textSize = 16f
                isClickable = true
                isFocusable = true
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }

            itemLayout.addView(itemText)
            dynamicBannerLayout.addView(itemLayout)

            if (index < items.size - 1) {
                val divider = View(this).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        2
                    ).apply {
                        setMargins(0, 0, 0, 0)
                    }
                    setBackgroundColor(resources.getColor(android.R.color.black, null))
                }
                dynamicBannerLayout.addView(divider)
            }
        }
    }

}
