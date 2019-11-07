package np.com.nawarajbista.myvoice

import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.status.view.*

class Status(
    private val userInformation: UserInformation?,
    private val currentPost: Map.Entry<String, Post>
): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        Picasso.get().load(userInformation?.defaultProfilePicture).into(viewHolder.itemView.image_view_status)
        viewHolder.itemView.textview_name.text = userInformation?.fullName
        viewHolder.itemView.textview_date.text = currentPost.key
        viewHolder.itemView.textview_status.text = currentPost.value.status
        viewHolder.itemView.number_of_like.text = currentPost.value.like.toString().plus(" people like this post.")

        val numOfComment = currentPost.value.comment.size
        viewHolder.itemView.number_of_comment.text = numOfComment.toString().plus(" comments")

    }

    override fun getLayout(): Int {
        return R.layout.status
    }
}