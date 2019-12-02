package np.com.nawarajbista.myvoice

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_comment_section.*
import kotlinx.android.synthetic.main.status.image_view_status
import kotlinx.android.synthetic.main.status.textview_name
import java.text.SimpleDateFormat
import java.util.*

class CommentSection : AppCompatActivity() {

    lateinit var currentUser: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment_section)


        val userInformation = intent.getParcelableExtra<UserInformation>("userInformation")
        val post = intent.getParcelableExtra<Post>("post")
        val postDate = intent.getStringExtra("postDate")


        currentUser = FirebaseAuth.getInstance().currentUser!!.uid

        Picasso.get().load(userInformation.defaultProfilePicture).into(image_view_status)

        textview_name.text = userInformation.fullName
        textview_date.text = postDate
        textview_status.text = post.status
        number_of_like.text = post.like.size.toString().plus(" likes")
        number_of_comment.text = post.comment.size.toString().plus(" comments")


        button_add_comment.setOnClickListener {
            if (edittext_comment.text.isEmpty()) return@setOnClickListener

            addCommentToDatabase(userInformation.uid, postDate)
        }

    }


    private fun addCommentToDatabase(userUID: String?, postDate: String) {
        val ref = FirebaseDatabase.getInstance().getReference("users/$userUID/post/$postDate/comment")

        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateTimeInstance()
        val currentDate = formatter.format(date)

        val comment = edittext_comment.text.toString()

        val addComment = CommentModel(currentUser, comment)

        ref.child(currentDate).setValue(addComment)
            .addOnFailureListener {
                Toast.makeText(this,"could not add your comment.", Toast.LENGTH_SHORT).show()
            }
            .addOnCompleteListener {
                edittext_comment.setText("")
            }

    }


}
