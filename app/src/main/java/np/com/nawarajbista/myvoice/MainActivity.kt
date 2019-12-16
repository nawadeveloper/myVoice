package np.com.nawarajbista.myvoice

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.Exception


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)

       navView.setupWithNavController(navController)

        //action bar
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.action_bar)

        try {
            supportActionBar?.setCustomView(R.layout.action_bar)
        }
        catch (e: Exception) {
            Log.d("mainActivity", e.message)
        }

    }

    fun getUserData(): DatabaseReference {

        val currentUser = FirebaseAuth.getInstance().currentUser?.uid

        return FirebaseDatabase.getInstance().reference.child("/users/$currentUser")
    }
}
