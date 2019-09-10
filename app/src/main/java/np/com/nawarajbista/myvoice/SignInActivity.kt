package np.com.nawarajbista.myvoice

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.alert_dialog.*

class SignInActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if(currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    fun goToSignUp(view: View) {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    fun signIn(view: View) {
        val email = edittext_sign_in_email.text.toString()
        val password = edittext_sign_in_password.text.toString()

        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "provide all information.", Toast.LENGTH_SHORT).show()
            return
        }

        val currentView = this.currentFocus

        if(currentView != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentView.windowToken, 0)
        }

        loading()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {

                if(!it.isSuccessful) {
                    dialog.dismiss()
                    return@addOnCompleteListener
                }
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                //dialog.dismiss()
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
            }
    }



    private fun loading() {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.alert_dialog,null)
        val message =dialogView.findViewById<TextView>(R.id.dialog_message)
        message.text = getText(R.string.logging)
        builder.setView(dialogView)
        builder.setCancelable(false)
        dialog = builder.create()

        dialog.show()


    }
}
