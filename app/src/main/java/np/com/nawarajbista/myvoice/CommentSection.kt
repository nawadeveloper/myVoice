package np.com.nawarajbista.myvoice


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_comment_section.*
import kotlinx.android.synthetic.main.status.image_view_status
import kotlinx.android.synthetic.main.status.textview_name
import java.text.SimpleDateFormat
import java.util.*

class CommentSection : AppCompatActivity() {

    lateinit var currentUser: String
    var checkIfLiked: Boolean = false
    val adapter = GroupAdapter<GroupieViewHolder>()

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

        numOfLike(userInformation.uid, postDate)


        button_status_like.setOnClickListener {
            if(checkIfLiked) {
                removeCurrentUser(userInformation.uid, postDate)
                checkIfLiked = false
            }
            else {
                addCurrentUser(userInformation.uid, postDate)
                checkIfLiked = true
            }

            Log.d("commentSection1", "$checkIfLiked")

        }



        number_of_comment.text = post.comment.size.toString().plus(" comments")


        addDataToAdapter(userInformation.uid, postDate)

        RecyclerView_comments.adapter = adapter


        button_add_comment.setOnClickListener {
            if (edittext_comment.text.isEmpty()) return@setOnClickListener

            addCommentToDatabase(userInformation.uid, postDate)
        }

    }


    private fun addDataToAdapter(userUID: String?, postDate: String) {
        val ref = FirebaseDatabase.getInstance()
            .getReference("users/$userUID/post/$postDate/comment")
        ref.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                adapter.clear()

                for(data in p0.children) {
                    val dataOfComment = data.getValue(CommentModel::class.java)

                    adapter.add(CommentData(data.key, dataOfComment))
                }
            }
        })
    }


    private fun addCurrentUser(userUID: String?, postDate: String) {

        val ref = FirebaseDatabase.getInstance()
            .getReference("users/$userUID/post/$postDate/like")

        ref.child(currentUser).setValue("liked")
    }



    private fun removeCurrentUser(userUID: String?, postDate: String) {
        val ref = FirebaseDatabase.getInstance().getReference("users/$userUID/post/$postDate/like/$currentUser")
        ref.setValue(null)
    }


    private fun numOfLike(userUID: String?, postDate: String){
        val ref = FirebaseDatabase.getInstance().getReference("users/$userUID/post/$postDate/like")



        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                number_of_like.text = p0.children.count().toString().plus(" Likes")

                if(p0.hasChild(currentUser)) {
                    checkIfLiked = true
                }

                updatedLikeButton()

            }
        })

    }

    private fun updatedLikeButton() {
        if(checkIfLiked) {
            button_status_like.text = getText(R.string.liked)
            button_status_like.setTextColor(getColor(R.color.colorBlue))
        }
        else {
            button_status_like.text = getText(R.string.like)
            button_status_like.setTextColor(getColor(R.color.white))
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
