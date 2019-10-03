package np.com.nawarajbista.myvoice

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

        //on user side
        //remove data from requestReceivedFrom
        //add data to friendList


        //on friend side
        //remove data from requestSendTo
        //add data to friendList

        TODO("start here")
    }
}