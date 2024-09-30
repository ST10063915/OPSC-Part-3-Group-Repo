import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.opsc.onesecondapp.R
import com.opsc.onesecondapp.TimelineItem
import com.opsc.onesecondapp.MonthImagesActivity
import java.text.SimpleDateFormat
import java.util.*

data class TimelineItem(val month: String, val imageUrl: String, val startDate: Long, val endDate: Long)

class TimelineAdapter(private var items: List<TimelineItem>, private val isEnglish: Boolean) : RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_timeline, parent, false)
        return TimelineViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        val item = items[position]
        holder.monthTextView.text = item.month

        // Format the name text (date range) as "1st-31st Aug 2024" or in Afrikaans based on isEnglish
        val dateRangeText = formatDateRange(item.startDate, item.endDate)
        holder.nameTextView.text = dateRangeText

        // Set the "In Review" text based on isEnglish
        if (isEnglish) {
            holder.inReviewTextView.text = "In Review"
        } else {
            holder.inReviewTextView.text = "In Oorsig"
        }

        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .placeholder(R.drawable.sample_image)
            .into(holder.itemImageView)

        // Click listener for the image to open MonthImagesActivity
        holder.itemImageView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, MonthImagesActivity::class.java)
            intent.putExtra("month", item.month)
            intent.putExtra("isEnglish", isEnglish)
            context.startActivity(intent)
        }

        // Click listener for the play button (optional behavior)
        holder.playButton.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, MonthImagesActivity::class.java)
            intent.putExtra("month", item.month)
            intent.putExtra("isEnglish", isEnglish)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size

    // ViewHolder class to hold references to each view
    class TimelineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImageView: ImageView = itemView.findViewById(R.id.itemImageView)
        val monthTextView: TextView = itemView.findViewById(R.id.monthTextView)
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val inReviewTextView: TextView = itemView.findViewById(R.id.inReviewTextView)
        val playButton: ImageButton = itemView.findViewById(R.id.playButton)
    }

    // Update the list of timeline items and refresh the RecyclerView
    fun setEntries(newItems: List<TimelineItem>) {
        this.items = newItems
        notifyDataSetChanged()
    }

    // Function to format the date range (e.g., "1st-31st Aug 2024")
    private fun formatDateRange(startDateMillis: Long, endDateMillis: Long): String {
        val dateFormat = SimpleDateFormat("d", Locale.getDefault())

        // Check the locale based on isEnglish flag
        val monthYearFormat = if (isEnglish) {
            SimpleDateFormat("MMM yyyy", Locale.getDefault())
        } else {
            SimpleDateFormat("MMM yyyy", Locale("af"))
        }

        val startDate = Date(startDateMillis)
        val endDate = Date(endDateMillis)

        val startDay = dateFormat.format(startDate)
        val endDay = dateFormat.format(endDate)
        val monthYear = monthYearFormat.format(endDate)

        return "$startDay-$endDay $monthYear"
    }
}
