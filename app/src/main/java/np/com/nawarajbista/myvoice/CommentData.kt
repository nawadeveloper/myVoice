package np.com.nawarajbista.myvoice


import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.comment_data_display.view.*
import kotlinx.android.synthetic.main.comment_data_display.view.textview_date

class CommentData(
    private val commentUserInformation: JustUserInformation?,
    private val date: String?,
    private val dataOfComment: CommentModel?
): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        Picasso.get().load(commentUserInformation?.defaultProfilePicture).into(viewHolder.itemView.image_user)
        viewHolder.itemView.textview_user_name.text = commentUserInformation?.fullName
        viewHolder.itemView.textview_date.text = date
        viewHolder.itemView.textview_comment.text = dataOfComment?.comment
    }

    override fun getLayout(): Int {
        return R.layout.comment_data_display
    }
}