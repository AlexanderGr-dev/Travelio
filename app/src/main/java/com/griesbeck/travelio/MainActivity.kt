
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
   

        binding.appBarMain.fab.setOnClickListener {
            val tripIntent = Intent(this,TripActivity::class.java)
            startActivity(tripIntent)
               R.id.nav_trips