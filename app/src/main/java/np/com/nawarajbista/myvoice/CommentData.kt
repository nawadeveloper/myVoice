package np.com.nawarajbista.myvoice


import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.comment_data_display.view.*

class CommentData(val date: String?, val dataOfComment: CommentModel?): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.itemView.textview_date.text = date
        viewHolder.itemView.textview_comment.text = dataOfComment?.comment
    }

    override fun getLayout(): Int {
        return R.layout.comment_data_display
    }
}