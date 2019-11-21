package np.com.nawarajbista.myvoice


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.status.view.*

class Status(
    private val userInformation: UserInformation?,
    private val currentPost: Map.Entry<String, Post>
): Item<GroupieViewHolder>() {

    private val currentUser = FirebaseAuth.getInstance().currentUser?.uid

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {



        Picasso.get().load(userInformation?.defaultProfilePicture).into(viewHolder.itemView.image_view_status)
        viewHolder.itemView.textview_name.text = userInformation?.fullName
        viewHolder.itemView.textview_date.text = currentPost.key
        viewHolder.itemView.textview_status.text = currentPost.value.status

        val likedNum = countLike() ?: 0

        viewHolder.itemView.number_of_like.text = likedNum.toString().plus(" people like this post.")

        val numOfComment = countComment() ?: 0
        viewHolder.itemView.number_of_comment.text = numOfComment.toString().plus(" comments")


        if(checkCurrentUserLiked()) {
            likedView(viewHolder.itemView.button_status_like)
        }


        viewHolder.itemView.button_status_like.setOnClickListener {
            if(checkCurrentUserLiked()) {
                removeFromLikeList(viewHolder)
            }
            else {
                addToLikeList(viewHolder)
            }
        }

        viewHolder.itemView.button_status_comment.setOnClickListener {
            val context = viewHolder.itemView.context
            val intent = Intent(context, CommentSection::class.java)
            context.startActivity(intent)
        }
    }

    override fun getLayout(): Int {
        return R.layout.status
    }

    private fun countLike(): Int? {
        return currentPost.value.like.size
    }



    private fun removeFromLikeList(viewHolder: GroupieViewHolder) {
        val ref = FirebaseDatabase.getInstance()
            .getReference("users/${userInformation?.uid}/post/${currentPost.key}/like")

        ref.child(currentUser!!).removeValue()
            .addOnFailureListener {
                Log.d("Status", it.message)
            }
            .addOnCompleteListener {
                unLikedView(viewHolder.itemView.button_status_like)
            }

    }

    private fun checkCurrentUserLiked(): Boolean {

        var userHasLike = false

        if(currentUser in currentPost.value.like) {
            userHasLike = true

        }
        return userHasLike
    }

    private fun likedView(view: Button) {
        view.text = "LIKED"
        view.setTextColor(Color.parseColor("#524FDD"))
    }

    private fun unLikedView(view: Button) {
        view.text = "LIKE"
        view.setTextColor(Color.parseColor("#ffffff"))
    }

    private fun addToLikeList(viewHolder: GroupieViewHolder) {
        val ref = FirebaseDatabase.getInstance()
            .getReference("users/${userInformation?.uid}/post/${currentPost.key}/like")

        ref.child(currentUser!!).setValue("liked")
            .addOnFailureListener {
                Log.d("Status", it.message)
            }

            .addOnCompleteListener {
                likedView(viewHolder.itemView.button_status_like)
            }

    }

    private fun countComment(): Int? {
        return currentPost.value.comment.size
    }
}