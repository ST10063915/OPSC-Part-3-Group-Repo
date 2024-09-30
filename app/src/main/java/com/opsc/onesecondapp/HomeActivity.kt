package com.opsc.onesecondapp

import TimelineAdapter
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class HomeActivity : AppCompatActivity() {

    private var isEnglish = true

    private lateinit var captureButton: ImageButton
    private lateinit var settingsButton: ImageButton
    private lateinit var profileButton: ImageView
    private lateinit var timelineRecyclerView: RecyclerView
    private lateinit var timelineAdapter: TimelineAdapter
    private lateinit var firestore: FirebaseFirestore

    // Profile banner elements
    private lateinit var bannerLayout: LinearLayout
    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var notificationIcon: ImageButton
    private lateinit var settingsIcon: ImageButton

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

    private lateinit var auth: FirebaseAuth
    private lateinit var rootLayout: RelativeLayout



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

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

        plusButton.setOnClickListener {
            //show add image menu (fix)
            Toast.makeText(this, "Media Import Coming Soon!", Toast.LENGTH_SHORT).show()
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
            if (isEnglish) {
                Toast.makeText(this, "Notifications: ${if (isChecked) "Enabled" else "Disabled"}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Kennisgewings: ${if (isChecked) "Geaktiveer" else "Gedeaktiveer"}", Toast.LENGTH_SHORT).show()
            }
        }

        languageButton.setOnClickListener {
            isEnglish = !isEnglish
            updateLanguageText()
        }

        backupRestoreButton.setOnClickListener {
            Toast.makeText(this, if (isEnglish) "Backup and Restore Coming Soon!" else "Rugsteun en herstel binnekort!", Toast.LENGTH_SHORT).show()
        }

        accountButton.setOnClickListener {
            Toast.makeText(this, if (isEnglish) "Account Coming Soon!" else "Rekening kom binnekort!", Toast.LENGTH_SHORT).show()
        }

        helpCenterButton.setOnClickListener {
            Toast.makeText(this, if (isEnglish) "Help Center Coming Soon!" else "Hulpsentrum kom binnekort!", Toast.LENGTH_SHORT).show()
        }

        contactSupportButton.setOnClickListener {
            Toast.makeText(this, if (isEnglish) "Contact Support Coming Soon!" else "Kontak ondersteuning binnekort!", Toast.LENGTH_SHORT).show()
        }

        importMediaButton.setOnClickListener {
            Toast.makeText(this, if (isEnglish) "Import Media Coming Soon!" else "Voeg media binnekort by!", Toast.LENGTH_SHORT).show()
        }

        moreSettingsButton.setOnClickListener {
            Toast.makeText(this, if (isEnglish) "More Settings Coming Soon!" else "Meer instellings binnekort beskikbaar!", Toast.LENGTH_SHORT).show()
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
        } else {
            bannerLayout.visibility = View.GONE
        }
    }

    // Function to toggle the visibility of the settings banner
    private fun toggleSettingsBanner() {
        if (settingsBannerLayout.visibility == View.GONE) {
            settingsBannerLayout.visibility = View.VISIBLE
            bannerLayout.visibility = View.GONE
        } else {
            settingsBannerLayout.visibility = View.GONE
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
        if (isEnglish) {
            Toast.makeText(this, "Notifications Coming Soon", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(this, "Kennisgewings kom binnekort", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showSettingsToast() {
        Toast.makeText(this, "Settings Coming soon", Toast.LENGTH_SHORT).show()
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
}
