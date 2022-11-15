
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
   
 R.id.nav_trips

        binding.appBarMain.fab.setOnClickListener {
            val tripIntent = Intent(this,TripActivity::class.java)
            startActivity(tripIntent)