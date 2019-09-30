package np.com.nawarajbista.myvoice


import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.friend_suggestion.view.*


class FriendSuggestion(
    private val fullName: String,
    private val image: String,
    private val friendId: String): Item<GroupieViewHolder>() {


    private val auth = FirebaseAuth.getInstance()
    private val currentUser = auth.currentUser?.uid


    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        try {

            Picasso.get().load(image).into(viewHolder.itemView.image_view_suggestion)
        }
        catch (e: Exception) {
            Log.d("FSError", e.message)
        }
        viewHolder.itemView.textview_suggestion.text = fullName

        viewHolder.itemView.button_send_request.setOnClickListener {

            addRequestSendTo()

            addRequestReceivedFrom()
        }

    }

    override fun getLayout(): Int {

        return R.layout.friend_suggestion
    }

    private fun addRequestSendTo() {

        val userDatabaseRef = FirebaseDatabase.getInstance().getReference("/users/$currentUser")
        userDatabaseRef.child("requestSendTo").push().setValue(friendId)
    }

    private fun addRequestReceivedFrom() {
        val userDataFireBase = FirebaseDatabase.getInstance().
            getReference("/users/$friendId")

        //to push multiple value create class and pass the value to setValue
        userDataFireBase.child("requestReceivedFrom").push().setValue(currentUser)
    }



}