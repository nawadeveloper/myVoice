package np.com.nawarajbista.myvoice


import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
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
        numOfComment(userInformation.uid, postDate)


        button_status_like.setOnClickListener {
            checkIfLiked = if(checkIfLiked) {
                removeCurrentUser(userInformation.uid, postDate)
                false
            } else {
                addCurrentUser(userInformation.uid, postDate)
                true
            }

        }


        addDataToAdapter(userInformation, postDate)

        RecyclerView_comments.adapter = adapter


        button_add_comment.setOnClickListener {
            if (edittext_comment.text.isEmpty()) return@setOnClickListener

            addCommentToDatabase(userInformation.uid, postDate)

            val currentView = this.currentFocus

            if(currentView != null) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(currentView.windowToken, 0)
            }
        }

    }


    private fun addDataToAdapter(userInformation: UserInformation, postDate: String) {
        val ref = FirebaseDatabase.getInstance().getReference("users/${userInformation.uid}")


        ref.child("post/$postDate/comment").addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                adapter.clear()

                for(data in p0.children) {
                    val dataOfComment = data.getValue(CommentModel::class.java)

                    getCommenterInformation(dataOfComment?.commenter, data.key, dataOfComment)

                }
            }
        })
    }

    private fun getCommenterInformation(commenter: String?, commentDate: String?, dataOfComment: CommentModel?){
        val ref = FirebaseDatabase.getInstance()
            .getReference("users/$commenter")

        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val commentUserInformation = p0.getValue(JustUserInformation::class.java)
                adapter.add(CommentData(commentUserInformation, commentDate, dataOfComment))
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

    private fun numOfComment(userUID: String?, postDate: String) {
        val ref = FirebaseDatabase.getInstance().getReference("users/$userUID/post/$postDate/comment")

        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                number_of_comment.text = p0.children.count().toString().plus(" comments")

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
