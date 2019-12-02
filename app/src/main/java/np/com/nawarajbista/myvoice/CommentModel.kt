package np.com.nawarajbista.myvoice

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CommentModel(
    val commenter: String,
    val comment: String
): Parcelable {
    constructor(): this("", "")
}