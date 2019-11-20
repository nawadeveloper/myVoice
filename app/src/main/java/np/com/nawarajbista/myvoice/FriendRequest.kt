package np.com.nawarajbista.myvoice

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.friend_request.view.*

class FriendRequest(
    private val userDataFireBase: UserDataFireBase?,
    private val friendId: String?

): Item<GroupieViewHolder>() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        Picasso.get().load(userDataFireBase?.defaultProfilePicture).into(viewHolder.itemView.image_view_request)
        viewHolder.itemView.textview_request.text = userDataFireBase?.fullName

        viewHolder.itemView.button_accept.setOnClickListener {
            friendRequestAccepted()
        }

    }

    override fun getLayout(): Int {
        return R.layout.friend_request
    }


    private fun friendRequestAccepted() {
        val currentUser = FirebaseAuth.getInstance().currentUser?.uid

        //on user side
        //remove data from requestReceivedFrom
        //add data to friendList
        addFriendToUser(currentUser)


        //on friend side
        //remove data from requestSendTo
        //add data to friendList
        addFriendToSender(currentUser)
    }


    private fun addFriendToUser(user: String?) {
        val dbRef = FirebaseDatabase.getInstance().getReference("users/$user")

        dbRef.child("friendList/$friendId").setValue("friend")

        dbRef.child("requestReceivedFrom").orderByValue().equalTo(friendId).addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                for (snap in p0.children) {
                    dbRef.child("requestReceivedFrom/${snap.key}").removeValue()
                }
            }
        })
    }

    private fun addFriendToSender(user: String?) {
        val dbRef = FirebaseDatabase.getInstance().getReference("users/$friendId")

        dbRef.child("friendList/$user").setValue("friend")

        dbRef.child("requestSendTo").orderByValue().equalTo(user)
            .addValueEventListener(object: ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    for(snap in p0.children) {
                        dbRef.child("requestSendTo/${snap.key}").removeValue()
                    }
                }
            })
    }
}