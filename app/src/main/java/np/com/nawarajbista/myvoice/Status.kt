package np.com.nawarajbista.myvoice

import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.status.view.*

class Status(
    private val userInformation: UserInformation?,
    private val currentKey: String,
    private val currentPost: String
): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        Picasso.get().load(userInformation?.defaultProfilePicture).into(viewHolder.itemView.image_view_status)
        viewHolder.itemView.textview_name.text = userInformation?.fullName
        viewHolder.itemView.textview_date.text = currentKey
        viewHolder.itemView.textview_status.text = currentPost

    }

    override fun getLayout(): Int {
        return R.layout.status
    }
}