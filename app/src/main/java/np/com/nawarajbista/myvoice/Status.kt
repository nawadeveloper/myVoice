package np.com.nawarajbista.myvoice

import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.status.view.*

class Status(private val userInformation: UserInformation?): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        Picasso.get().load(userInformation?.defaultProfilePicture).into(viewHolder.itemView.image_view_status)
        viewHolder.itemView.textview_name.text = userInformation?.fullName
        viewHolder.itemView.textview_date.text = userInformation?.post?.keys.toString()
        viewHolder.itemView.textview_status.text = userInformation?.post?.values.toString()

    }

    override fun getLayout(): Int {
        return R.layout.status
    }
}