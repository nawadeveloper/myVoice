package np.com.nawarajbista.myvoice

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var dialog: AlertDialog
    private lateinit var fullName: String
    private lateinit var email: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.action_bar)

        auth = FirebaseAuth.getInstance()

    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    fun registerUser(view: View) {
        hideKeyboard()


        fullName = edittext_fullname.text.toString()
        email = edittext_create_account_email.text.toString()
        val password = edittext_create_account_password.text.toString()
        val rePassword = edittext_create_account_re_password.text.toString()



        if(email.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
            Toast.makeText(this, "fill all empty form", Toast.LENGTH_SHORT).show()
            return
        }

        if(password != rePassword) {
            Toast.makeText(this, "Your password does not match.", Toast.LENGTH_SHORT).show()
            return
        }



        loading()



        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(!it.isSuccessful) {
                    dialog.dismiss()
                    return@addOnCompleteListener
                }

                storeDataToDatabase()


            }
            .addOnFailureListener {
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
            }
    }




    fun goToSignIn(view: View) {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }



    private fun storeDataToDatabase() {

        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")


        val defaultProfilePicture = "https://firebasestorage.googleapis.com/v0/b/my-voice-prototype.appspot.com/o/user_profile_image%2Fprofile_default_image.png?alt=media&token=beee1a6a-2c77-49bf-8732-19a0acdd85b2"

        val userData = UserDataFireBase(
            fullName,
            email,
            defaultProfilePicture
        )

        ref.setValue(userData)
            .addOnCompleteListener {task ->

                if(!task.isSuccessful) return@addOnCompleteListener
                Log.d("success", "user data base created")

                goToMainActivity()
            }

            .addOnFailureListener {failureMessage ->
                Log.d("error", "${failureMessage.message}")
                Toast.makeText(this, "${failureMessage.message}", Toast.LENGTH_SHORT).show()
                auth.currentUser?.delete()
                return@addOnFailureListener
            }



    }

    private fun loading() {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.alert_dialog,null)
        val dialogMessage = dialogView.findViewById<TextView>(R.id.dialog_message)
        dialogMessage.text = getString(R.string.registration_in_process)

        builder.setView(dialogView)
        builder.setCancelable(false)
        dialog = builder.create()
        dialog.show()
    }

    

    private fun hideKeyboard() {
        val currentView = this.currentFocus

        if(currentView != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentView.windowToken, 0)


            //window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        }
    }
}
