package com.opsc.onesecondapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.*

class MonthImagesActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MonthImagesAdapter
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var selectedMonthTextView: TextView
    private lateinit var backButton: ImageButton
    private lateinit var plusButton: ImageButton
    private lateinit var selectedMonth: String
    private var isEnglish: Boolean = true

    private val PICK_IMAGE_REQUEST = 71
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_month_images)

        isEnglish = intent.getBooleanExtra("isEnglish", true)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        if (user != null) {
            // Proceed with uploading
        } else {
            if(isEnglish){
                Toast.makeText(this, "User not signed in. Please log in.", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "Gebruiker nie aangemeld nie. Meld asseblief aan.", Toast.LENGTH_SHORT).show()
            }
            return
        }

        selectedMonth = intent.getStringExtra("month") ?: ""

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()

        // Set up UI components
        recyclerView = findViewById(R.id.recyclerView)
        selectedMonthTextView = findViewById(R.id.selectedMonthTextView)
        backButton = findViewById(R.id.BackButton)
        plusButton = findViewById(R.id.plusButton)

        // Display the selected month
        if (isEnglish) {
            selectedMonthTextView.text = "My Timeline: $selectedMonth"
        } else {
            selectedMonthTextView.text = "My Tydlyn: $selectedMonth"
        }

        // Initialize the adapter
        adapter = MonthImagesAdapter(listOf())
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = adapter

        // Load images for the selected month if available
        if (selectedMonth.isNotEmpty()) {
            loadImagesForMonth(selectedMonth)
        } else {
            if(isEnglish){
                Toast.makeText(this, "No month selected", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "Geen maand gekies nie", Toast.LENGTH_SHORT).show()
            }
        }

        setupButtonClickListeners()
    }

    private fun setupButtonClickListeners() {
        backButton.setOnClickListener {
            finish()
        }

        plusButton.setOnClickListener {
            openImagePicker()
        }
    }

    // open the image picker
    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    // Handle the result of the image picker
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let {
                imageUri = it
                uploadImageToFirebase(it)
            }
        }
    }

    // upload the selected image to Firebase Storage
    private fun uploadImageToFirebase(uri: Uri) {
        val userId = auth.currentUser?.uid ?: return
        val storageReference: StorageReference = firebaseStorage.reference
            .child("users/$userId/images/${System.currentTimeMillis()}.jpg")

        val uploadTask = storageReference.putFile(uri)

        uploadTask.addOnSuccessListener {
            storageReference.downloadUrl.addOnSuccessListener { downloadUri ->
                saveImageMetadataToFirestore(downloadUri.toString())
            }
        }.addOnFailureListener {
            if(isEnglish){
                Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "Beeldoplaai het misluk", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //save image metadata to Firestore
    private fun saveImageMetadataToFirestore(imageUrl: String) {
        val userId = auth.currentUser?.uid ?: return
        val timestamp = getFirstDayOfMonth(selectedMonth, isEnglish)

        val entryData = hashMapOf(
            "imageUrl" to imageUrl,
            "timestamp" to com.google.firebase.Timestamp(timestamp / 1000, 0),
            "userId" to userId
        )

        firestore.collection("users").document(userId).collection("timeline")
            .add(entryData)
            .addOnSuccessListener {
                if(isEnglish){
                    Toast.makeText(this, "Image metadata saved", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this, "Beeldmetadata gestoor", Toast.LENGTH_SHORT).show()
                }

                loadImagesForMonth(selectedMonth)
            }
            .addOnFailureListener {
                if(isEnglish){
                    Toast.makeText(this, "Failed to save metadata", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this, "Kon nie metadata stoor nie", Toast.LENGTH_SHORT).show()
                }

            }
    }

    // images from Firestore for the selected month
    private fun loadImagesForMonth(month: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val firstDay = getFirstDayOfMonth(month, isEnglish)
            val lastDay = getLastDayOfMonth(month, isEnglish)

            firestore.collection("users").document(userId).collection("timeline")
                .whereGreaterThanOrEqualTo("timestamp", com.google.firebase.Timestamp(firstDay / 1000, 0))
                .whereLessThanOrEqualTo("timestamp", com.google.firebase.Timestamp(lastDay / 1000, 0))
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        if(isEnglish){
                            Toast.makeText(this, "No images found for $month", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            val sdf = SimpleDateFormat("MMMM yyyy", Locale("af"))
                            val afrikaansMonth = sdf.format(SimpleDateFormat("MMMM yyyy", Locale.getDefault()).parse(month))
                            Toast.makeText(this, "Geen beelde gevind vir $afrikaansMonth", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        val imageItems = documents.map { doc ->
                            val imageUrl = doc.getString("imageUrl") ?: ""
                            ImageItem(imageUrl)
                        }

                        adapter.setImages(imageItems)
                    }
                }
                .addOnFailureListener {
                    if(isEnglish){
                        Toast.makeText(this, "Failed to load images", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this, "Kon nie beelde laai nie", Toast.LENGTH_SHORT).show()
                    }

                }
        }
    }

    // Get the first day of the month in milliseconds
    private fun getFirstDayOfMonth(month: String, isEnglish: Boolean): Long {
        val calendar = Calendar.getInstance()

        // Use appropriate locale for the selected language
        val sdf = if (isEnglish) {
            SimpleDateFormat("MMMM yyyy", Locale.getDefault()) // English Locale
        } else {
            SimpleDateFormat("MMMM yyyy", Locale("af"))  // Afrikaans Locale
        }

        calendar.time = sdf.parse(month) ?: Date()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        return calendar.timeInMillis
    }

    private fun getLastDayOfMonth(month: String, isEnglish: Boolean): Long {
        val calendar = Calendar.getInstance()

        val sdf = if (isEnglish) {
            SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        } else {
            SimpleDateFormat("MMMM yyyy", Locale("af"))
        }

        calendar.time = sdf.parse(month) ?: Date()
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        return calendar.timeInMillis
    }
}
